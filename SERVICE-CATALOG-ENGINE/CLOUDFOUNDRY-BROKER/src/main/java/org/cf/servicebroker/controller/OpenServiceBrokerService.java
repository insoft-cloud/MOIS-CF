package org.cf.servicebroker.controller;

import org.cf.servicebroker.common.Constants;
import org.cf.servicebroker.exception.ServiceBrokerException;
import org.cf.servicebroker.model.catalog.Catalog;
import org.cf.servicebroker.model.deployment.DeploymentInstance;
import org.cf.servicebroker.model.deployment.JpaDeployment;
import org.cf.servicebroker.model.plan.JpaServicePlan;
import org.cf.servicebroker.model.plan.MaintenanceInfo;
import org.cf.servicebroker.model.plan.Plan;
import org.cf.servicebroker.model.service.JpaService;
import org.cf.servicebroker.model.service.Service;
import org.cf.servicebroker.model.serviceinstance.*;;
import org.cf.servicebroker.repo.JpaDeploymentRepository;
import org.cf.servicebroker.repo.JpaServiceInstanceRepository;
import org.cf.servicebroker.repo.JpaServicePlanRepository;
import org.cf.servicebroker.repo.JpaServiceRepository;
import org.cf.servicebroker.util.JsonUtils;
import org.cf.servicebroker.util.YamlUtil;
import org.openpaas.paasta.bosh.director.BoshDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.yaml.snakeyaml.Yaml;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@org.springframework.stereotype.Service
public class OpenServiceBrokerService {

    private final JpaServiceRepository jpaServiceRepository;
    private final BoshService boshService;
    private final JpaServiceInstanceRepository jpaServiceInstanceRepository;
    private final JpaServicePlanRepository jpaServicePlanRepository;
    private final JpaDeploymentRepository jpaDeploymentRepository;

    private static final Logger logger = LoggerFactory.getLogger(OpenServiceBrokerService.class);
    public OpenServiceBrokerService(JpaServiceRepository jpaServiceRepository, BoshService boshService, JpaServiceInstanceRepository jpaServiceInstanceRepository, JpaServicePlanRepository jpaServicePlanRepository, JpaDeploymentRepository jpaDeploymentRepository) {
        this.jpaServiceRepository = jpaServiceRepository;
        this.boshService = boshService;
        this.jpaServiceInstanceRepository = jpaServiceInstanceRepository;
        this.jpaServicePlanRepository = jpaServicePlanRepository;
        this.jpaDeploymentRepository = jpaDeploymentRepository;
    }


    @Transactional(propagation= Propagation.REQUIRED)
    public ResponseEntity<Catalog> getCatalog() {
        List<JpaService> jpaServiceList = jpaServiceRepository.findAll();
        List<Service> services = jpaServiceList.stream().flatMap(res -> {
            List<String> tags = Arrays.asList(res.getTag().split(","));
            return Stream.of(Service.builder().id(res.getId())
                    .dashboardClient(null)
                    .allowContextUpdates(true)
                    .bindable(res.isBindable())
                    .description(res.getDc())
                    .bindingsRetrievable(true)
                    .instancesRetrievable(true)
                    .name(res.getSvcNm())
                    .tags(tags)
                    .plans(res.getServicePlanList().stream().flatMap(plans ->
                        Stream.of(Plan.builder()
                                .bindable(true)
                                .description(plans.getDc())
                                .free(true)
                                .id(plans.getId())
                                .maintenance_info(MaintenanceInfo.builder().description(plans.getMntncVer()).version(plans.getMntncVer()).build())
                                .name(plans.getPlanNm())
                                .build())
                    ).collect(Collectors.toList()))
                    .build());
        }).collect(Collectors.toList());
        return new ResponseEntity(Catalog.builder().service(services).build(), HttpStatus.OK);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public ResponseEntity<ServiceInstanceProvision> createServiceInstance(String instance_id, ServiceInstanceProvisionRequest serviceInstanceProvisionRequest ) throws ServiceBrokerException {
        logger.info("CreateInstance Ready ::: " + serviceInstanceProvisionRequest.toString());
        Optional<JpaService> opt_service = jpaServiceRepository.findById(serviceInstanceProvisionRequest.getServiceId());
        if(!opt_service.isPresent()){
            logger.error("Unprocessable Entity(Nothing Service ::"+serviceInstanceProvisionRequest.getServiceId() +")");
            throw new ServiceBrokerException("Unprocessable Entity(Nothing Service ::"+serviceInstanceProvisionRequest.getServiceId() +")");
        }
        Optional<JpaServicePlan> opt_plan = jpaServicePlanRepository.findById(serviceInstanceProvisionRequest.getPlanId());
        if(!opt_plan.isPresent()){
            logger.error("Unprocessable Entity(Nothing Service Plan ::"+serviceInstanceProvisionRequest.getPlanId() +")");
            throw new ServiceBrokerException("Unprocessable Entity(Nothing Service Plan ::"+serviceInstanceProvisionRequest.getPlanId() +")");
        }
        JpaDeployment jpaDeployments = jpaDeploymentRepository.findByName(opt_plan.get().getJpaService().getSvcNm());
        Map deployment_map = new YamlUtil().fromYaml(jpaDeployments.getDeployments_yml());



        /// 네트워크 변경
        logger.info("네트워크 변경");
        List<Map> instance_groups = (ArrayList) deployment_map.get(Constants.INSTANCE_GROUPS);
        List<String> azs = new ArrayList<>();
        List<Map> networkslist = new ArrayList<>();
        Map networks = new HashMap();
        azs.add(serviceInstanceProvisionRequest.getParameters().get("project_id").toString());
        networks.put("name", serviceInstanceProvisionRequest.getParameters().get("project_id").toString());
        networkslist.add(networks);
        instance_groups.get(0).put(Constants.AZS,azs);
        instance_groups.get(0).put(Constants.NETWORKS, networkslist);
        deployment_map.put(Constants.INSTANCE_GROUPS, instance_groups);
        String deployment_yml = new Yaml().dump(deployment_map);
        logger.info(deployment_yml);
        logger.info(jpaDeployments.getDeployments_yml());


        // 파라미터 검사
        AtomicBoolean check_Parameter = new AtomicBoolean(true);
        StringBuffer message = new StringBuffer().append("Unprocessable parameter : [");
        Map properties = (Map) deployment_map.get(Constants.PROPERTIES);
        properties.forEach((key, value) -> {
            if(!serviceInstanceProvisionRequest.getParameters().containsKey(key)){
                check_Parameter.set(false);
                message.append(" "+ key);
            } else if (value == null || value.toString().length() ==0){
                check_Parameter.set(false);
                message.append(" "+ key);
            }
        });
       message.append("]");
        if(!check_Parameter.get()){
            logger.error(message.toString());
            throw new ServiceBrokerException(message.toString(), 400);
        }
        // 서비스 예외 구문
        if(opt_service.get().getSvcNm().equals(Constants.RABBITMQ)){
            if(serviceInstanceProvisionRequest.getParameters().get("admin").equals("admin")){
                throw new ServiceBrokerException("rabbitmq admin name it cannot be created as 'admin'", 400);
            }
        } else if(opt_service.get().getSvcNm().equals(Constants.CUBRID)){
            if(serviceInstanceProvisionRequest.getParameters().get("database_name").toString().length() >= 16){
                throw new ServiceBrokerException("The length of The database name of CubridDB must be 16 characters or less", 400);
            }
        }
        for (String s : Constants.MARIADB_PORT) {
            if(serviceInstanceProvisionRequest.getParameters().containsKey("port")){
                if(serviceInstanceProvisionRequest.getParameters().get("port").toString().equals(s)){
                    throw new ServiceBrokerException("this port("+s+") cannot be used", 400);
                }
            }
        }
        String username = serviceInstanceProvisionRequest.getParameters().get("owner").toString();
        String service_instance_nm = serviceInstanceProvisionRequest.getParameters().get("service_instance_nm").toString();
        serviceInstanceProvisionRequest.getParameters().remove("owner");
        serviceInstanceProvisionRequest.getParameters().remove("org_name");
        serviceInstanceProvisionRequest.getParameters().remove("project_id");
        serviceInstanceProvisionRequest.getParameters().remove("service_instance_nm");
        // 1. manifest 정보를 가지고 deploy.
        String resultYml = boshService.createInstance(opt_plan.get(),serviceInstanceProvisionRequest.getParameters(),deployment_yml);
        if(StringUtils.isEmpty(resultYml)) {
            logger.error("Unprocessable Entity(Service Not Deploying)");
            throw new ServiceBrokerException("Unprocessable Entity(Service Not Deploying)");
        }
        logger.info("CreateInstance >>> " + resultYml.toString());

        Map resultMap = new YamlUtil().fromYaml(resultYml);
        String deployment_name = resultMap.get("name").toString();
        String taskID = "";
        int index = 0;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new ServiceBrokerException("Unprocessable Entity(Thread Sleep Error ::"+e.getMessage() +")");
            } taskID = boshService.getTaskID(deployment_name);
            if (taskID != null) {
                logger.info("Create Instance taskID : " + taskID);
                break;
            }
            if(index >= 10){
                throw new ServiceBrokerException("SERVICE DEPLOY ERROR :: " + deployment_name);
            }
            logger.info("index" + index);
            index++;
        }
        Map<String, Object> result_map = serviceInstanceProvisionRequest.getParameters();
        if(opt_service.get().getSvcNm().equals(Constants.STORAGE)){
            result_map.put("username", "admin");
        }

        jpaServiceInstanceRepository.save(JpaServiceInstn.builder().instnNm(service_instance_nm).creatId(username).updtId(username).service(opt_service.get()).servicePlan(opt_plan.get()).dtbNm(deployment_name).id(instance_id).conectInfo(JsonUtils.jsonStrFromObject(result_map)).taskId(taskID).build());
        return new ResponseEntity(ServiceInstanceProvision.builder().dashboardUrl("").operation("task_" + "_In Progress").build(), HttpStatus.ACCEPTED);
    }


    @Transactional(propagation= Propagation.REQUIRED)
    public ResponseEntity<ServiceInstanceResource> getServiceInstance(String instance_id) throws ServiceBrokerException {
        logger.info(instance_id + ":::instance ID");
        Optional<JpaServiceInstn> opt_ServiceInstance = jpaServiceInstanceRepository.findById(instance_id);
        if(!opt_ServiceInstance.isPresent()){
            throw new ServiceBrokerException("Not Found(Nothing Service Instance ::"+instance_id +")");
        }
        JpaServiceInstn jpaServiceInstn = opt_ServiceInstance.get();
        String dashboard = "";
        Map map = JsonUtils.getMapFromJsonStr(jpaServiceInstn.getConectInfo());
        if(jpaServiceInstn.getService().getSvcNm().equals(Constants.RABBITMQ)){
            String[] _dash = map.get("ip").toString().split(",");
            dashboard = "http://"+_dash[_dash.length-1].trim()+":"+map.get("dashboard_port").toString();
        }else if(jpaServiceInstn.getService().getSvcNm().equals(Constants.STORAGE)){
            map.put("auth_url","http://"+ map.get("ip").toString()+":5000/v3/auth/tokens");
        }else if (jpaServiceInstn.getService().getSvcNm().equals(Constants.CUBRID) || jpaServiceInstn.getService().getSvcNm().equals(Constants.REDIS)){
            map.put("username","admin");
        }
        return new ResponseEntity(ServiceInstanceResource.builder()
                .dashboardUrl(dashboard)
                .description(jpaServiceInstn.getServicePlan().getDc())
                .parameters(map)
                .planId(jpaServiceInstn.getServicePlan().getId())
                .serviceId(jpaServiceInstn.getService().getId())
                .build()
                , HttpStatus.OK);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public ResponseEntity<ServiceInstanceLastOperation> getServiceInstanceLastOperation(String instance_id) throws Exception {
        OperationState state = OperationState.IN_PROGRESS;;
        Optional<JpaServiceInstn> opt_ServiceInstance = jpaServiceInstanceRepository.findById(instance_id);
        if(!opt_ServiceInstance.isPresent()){
            throw new ServiceBrokerException("Not Found(Nothing Service Instance ::"+instance_id +")");
        }
        JpaServiceInstn jpaServiceInstn = opt_ServiceInstance.get();
        JpaService service = jpaServiceInstn.getService();
        logger.info("service ::" + service.getId());
        if (boshService.runningTask(jpaServiceInstn)) {
            Map map = boshService.getState(jpaServiceInstn.getTaskId());
            if((boolean)map.get("result")){
                this.deleteServiceInstance(instance_id);
                return new ResponseEntity(ServiceInstanceLastOperation.builder().state(OperationState.FAILED).description(map.get("dc").toString()).build(), HttpStatus.OK);
            }
            Map deployment = boshService.getIp(jpaServiceInstn.getDtbNm(),service.getSvcNm());
            String ips = deployment.get("ips").toString();
            jpaServiceInstn.setVmInstnId(deployment.get("id").toString());
            Map re = JsonUtils.getMapFromJsonStr(jpaServiceInstn.getConectInfo());
            re.put("ip",ips.substring(1,ips.length()-1));
            jpaServiceInstn.setConectInfo(JsonUtils.jsonStrFromObject(re));
            jpaServiceInstn.setSttusValue(Constants.SUCCEEDED);
            jpaServiceInstanceRepository.save(jpaServiceInstn);
            state = OperationState.SUCCEEDED;
        }
        return new ResponseEntity(ServiceInstanceLastOperation.builder().state(state).description("").build(), HttpStatus.OK);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public ResponseEntity<ServiceInstanceResource> deleteServiceInstance(@PathVariable String instance_id) throws ServiceBrokerException {
        JpaServiceInstn jpaServiceInstance;
        try {
            jpaServiceInstance = jpaServiceInstanceRepository.getOne(instance_id);
            String deployment_name = jpaServiceInstance.getDtbNm();
            boshService.deleteServiceInstance(deployment_name);
            logger.info("DEPLOYMENT DELETE SUCCEED : DEPLOYMENT NAME : " + jpaServiceInstance.getDtbNm());
            jpaServiceInstanceRepository.delete(jpaServiceInstance);
        } catch (Exception e) {
            throw new ServiceBrokerException(instance_id + " 는 유효하지 않은 service instance id 입니다.");
        }
        return new ResponseEntity(ServiceInstanceResource.builder()
                .dashboardUrl("")
                .description(jpaServiceInstance.getServicePlan().getDc())
                .parameters(jpaServiceInstance.getConectInfo())
                .planId(jpaServiceInstance.getServicePlan().getId())
                .serviceId(jpaServiceInstance.getService().getId()).build()
                , HttpStatus.OK);
    }
}
