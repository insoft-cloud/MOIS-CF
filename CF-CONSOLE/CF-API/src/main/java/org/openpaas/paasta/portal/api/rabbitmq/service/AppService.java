package org.openpaas.paasta.portal.api.rabbitmq.service;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.v2.applications.CreateApplicationRequest;
import org.cloudfoundry.client.v2.applications.UpdateApplicationRequest;
import org.cloudfoundry.client.v2.applications.UploadApplicationRequest;
import org.cloudfoundry.client.v2.routemappings.CreateRouteMappingRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteRequest;
import org.cloudfoundry.client.v2.shareddomains.ListSharedDomainsRequest;
import org.cloudfoundry.client.v2.shareddomains.ListSharedDomainsResponse;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Catalog;
import org.openpaas.paasta.portal.api.rabbitmq.annotation.MessageMapper;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.EgovplatformMsgHeader;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.MessageResponseBody;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.MessageResponseDocument;
import org.openpaas.paasta.portal.api.rabbitmq.model.BuildpackCategory;
import org.openpaas.paasta.portal.api.rabbitmq.model.EgovpProject;
import org.openpaas.paasta.portal.api.rabbitmq.model.MeteringInstanceModel;
import org.openpaas.paasta.portal.api.rabbitmq.model.MeteringModel;
import org.openpaas.paasta.portal.api.service.CommonService;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AppService extends Common {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    private final VaultTemplate vaultTemplate;
    @Value("${config.vault.project}")
    String PATH_PROJECT;

    @Value("${config.vault.iaas}")
    String PATH_IAAS;
    @Value("${config.vault.list}")
    String PATH_LIST;

    private final RabbitTemplate rabbitTemplate;

    private final CommonService commonService;
    public AppService(CommonService commonService, VaultTemplate vaultTemplate, RabbitTemplate rabbitTemplate) {
        this.commonService = commonService;
        this.vaultTemplate = vaultTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Object createCloudfoundryApplication(@MessageMapper(field = "serviceAccessToken", dataLoc = MessageMapper.HEADER) String token,
                                    @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
                                    @MessageMapper(field = "build_pack_name") String buildPackName,
                                    @MessageMapper(field = "application_name") String applicationName,
                                    @MessageMapper(field = "disk_size") String diskSize,
                                    @MessageMapper(field = "memory_size") String memorySize,
                                    @MessageMapper(field = "project_id") String projectId,
                                    @MessageMapper(field = "host_name")String hostName) {
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        String applicationid = "applicationID";
        String routeid = "route ID";
        File file = null;
        try {
            VaultResponseSupport<Object> vault_result = vaultTemplate.read(PATH_PROJECT+projectId, Object.class);
            Map<String, Map<String, String>> result  = (Map<String, Map<String, String>>) vault_result.getData();
            String space = result.get("data").get(Constants.SPACE);
            Map buildpackCategory = commonService.procCommonApiRestTemplate2("/v2/Y/developpacks", HttpMethod.GET, null,token,  Map.class);
            List<Map<String, Object>> category_s = (List<Map<String, Object>>) buildpackCategory.get("list");
            List<BuildpackCategory> buildpackCategoryList = category_s.stream().flatMap(res -> Stream.of(BuildpackCategory.builder().no(Integer.valueOf(res.get("no").toString()))
                    .appSampleFileName(res.get("appSampleFileName").toString())
                    .appSampleFilePath(res.get("appSampleFilePath").toString())
                    .buildPackName(res.get("buildPackName").toString()).build())).collect(Collectors.toList());
            BuildpackCategory  result_buildpack = buildpackCategoryList.stream().filter(res ->
                    res.getBuildPackName().equals(buildPackName)
            ).collect(Collectors.toList()).get(0);
            file = createTempFile(result_buildpack.getAppSampleFileName(), result_buildpack.getAppSampleFilePath()); // 임시파일을 생성합니다.
            applicationid = createApplication(applicationName,Integer.valueOf(memorySize), buildPackName, Integer.valueOf(diskSize), space, reactorCloudFoundryClient); // App을 만들고 guid를 return 합니다.
            ListSharedDomainsResponse listSharedDomainsResponse = reactorCloudFoundryClient.sharedDomains().list(ListSharedDomainsRequest.builder().build()).block();
            routeid = createRoute(listSharedDomainsResponse.getResources().get(0).getMetadata().getId(),hostName,space, reactorCloudFoundryClient); //route를 생성후 guid를 return 합니다.
            routeMapping(applicationid, routeid, reactorCloudFoundryClient); // app와 route를 mapping합니다.
            fileUpload(file, applicationid, reactorCloudFoundryClient); // app에 파일 업로드 작업을 합니다.
            procCatalogStartApplication(applicationid, reactorCloudFoundryClient); //앱 시작
            Catalog catalog = new Catalog();
            catalog.setCatalogNo(result_buildpack.getNo());
            catalog.setCatalogType("buildPack");
            catalog.setUserId(usrId);
            commonService.procCommonApiRestTemplate("/v2/history", HttpMethod.POST, catalog, null);
            return new ResponseEntity(null, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            if (!applicationid.equals("applicationID")) {
                if (!routeid.equals("route ID")) {
                    //reactorCloudFoundryClient.routes().delete(DeleteRouteRequest.builder().routeId(routeid).build()).block();
                }
                //reactorCloudFoundryClient.applicationsV2().delete(DeleteApplicationRequest.builder().applicationId(applicationid).build()).block();
            }
            return new ResponseEntity(null, HttpStatus.CONFLICT);
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }


    private File createTempFile(String fileName, String filePath) throws Exception {

        try {
            File file = File.createTempFile(fileName.substring(0, fileName.length() - 4), fileName.substring(fileName.length() - 4));
            InputStream is = (new URL(filePath).openConnection()).getInputStream();
            OutputStream out = new FileOutputStream(file);
            IOUtils.copy(is, out);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
            return file;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    private String getDisposition(String filename, String browser) throws Exception {
        String encodedFilename = null;

        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            throw new RuntimeException("Not supported browser");
        }

        return encodedFilename;
    }


    /**
     * 앱을 생성한다.
     *
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private String createApplication(String applicationName, int memorySize, String buildPackName, int DiskSize, String spaceId, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        return reactorCloudFoundryClient.
                applicationsV2().create(CreateApplicationRequest.builder().buildpack(buildPackName).memory(memorySize).name(applicationName).diskQuota(DiskSize).spaceId(spaceId).build()).block().getMetadata().getId();

    }


    /**
     * 라우트를 생성한다..
     *
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private String createRoute(String domainId, String hostName, String spaceId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        return reactorCloudFoundryClient.
                routes().create(CreateRouteRequest.builder().host(hostName).domainId(domainId).spaceId(spaceId).build()).block().getMetadata().getId();
    }


    /**
     * 라우트를 앱에 매핑한다.
     *
     * @param applicationid             String
     * @param routeid                   String
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void routeMapping(String applicationid, String routeid, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        reactorCloudFoundryClient.
                routeMappings().create(CreateRouteMappingRequest.builder().routeId(routeid).applicationId(applicationid).build()).block();
    }

    /**
     * 파일을 업로드한다.
     *
     * @param file                      File
     * @param applicationid             String
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void fileUpload(File file, String applicationid, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        try {
            reactorCloudFoundryClient.
                    applicationsV2().upload(UploadApplicationRequest.builder().applicationId(applicationid).application(file.toPath()).build()).block();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    /**
     * 카탈로그 앱을 시작한다.
     *
     * @param applicationid             applicationid
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> procCatalogStartApplication(String applicationid, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        try {
            Thread.sleep(500);
            reactorCloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(applicationid).state("STARTED").build()).block();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    public Map moisMetering(Map map){
        List<String> vault_list = vaultTemplate.list(PATH_LIST);
        for(String projectId : vault_list){
            if(!projectId.equals("user/")){
                VaultResponseSupport<Object> vault_result = vaultTemplate.read(PATH_PROJECT+projectId, Object.class);
                Map<String, Map<String, String>> result  = (Map<String, Map<String, String>>) vault_result.getData();
                if(map.get("org_guid").toString().equals(result.get("data").get(Constants.ORG))){

                    EgovplatformMsgHeader egovplatformMsgHeader = new EgovplatformMsgHeader();
                    egovplatformMsgHeader.setCrud(map.get("curd").toString());
                    DateFormat df = new SimpleDateFormat("YYYYMMDDHHmmss");
                    egovplatformMsgHeader.setCreateDtm(df.format(new Date()));
                    egovplatformMsgHeader.setFromPartyId("CF");
                    egovplatformMsgHeader.setToPartyId("SC");
                    egovplatformMsgHeader.setSeq("CF");
                    egovplatformMsgHeader.setServiceAccessToken("CF");
                    egovplatformMsgHeader.setMsgId(UUID.randomUUID().toString());
                    egovplatformMsgHeader.setInterfaceId("egovplatform.up.sc.1060");
                    egovplatformMsgHeader.setMsgPubUserId(map.get("user").toString());
                    egovplatformMsgHeader.setProcessStatus("0000");

                    MeteringInstanceModel meteringInstanceModel = MeteringInstanceModel.builder().planId(map.get("plan").toString()).projectId(projectId).serviceId(map.get("plan").toString()).serviceInstanceId(map.get("instance_id").toString()).build();
                    MeteringModel messageResponseDocument = new MeteringModel(egovplatformMsgHeader, meteringInstanceModel);

                    rabbitTemplate.convertAndSend("sc.topic", "egovplatform.*.sc.*", messageResponseDocument);
                    LOGGER.info("METERING" + messageResponseDocument.toString());
                    break;
                }
            }
        }


        return map;
    }

}
