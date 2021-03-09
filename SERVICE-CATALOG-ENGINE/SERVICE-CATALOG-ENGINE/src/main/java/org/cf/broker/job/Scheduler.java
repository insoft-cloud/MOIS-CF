package org.cf.broker.job;

import org.cf.broker.model.jpa.JpaServiceBatch;
import org.cf.broker.model.jpa.JpaServiceInstn;
import org.cf.broker.model.jpa.JpaServiceMetering;
import org.cf.broker.model.jpa.JpaServiceMeteringUseInfo;
import org.cf.broker.model2.JpaServiceMeteringView;
import org.cf.broker.model3.JpaVwLng;
import org.cf.broker.repo.JpaServiceBatchRepository;
import org.cf.broker.repo.JpaServiceInstanceRepository;
import org.cf.broker.repo.JpaServiceMeteringRepository;
import org.cf.broker.repo.JpaServiceMeteringUseInfoRepository;
import org.cf.broker.repository.JpaServiceMeteringViewRepository;
import org.cf.broker.repository3.JpaVwLngRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    private final JpaServiceMeteringRepository jpaServiceMeteringRepository;
    private final JpaServiceMeteringUseInfoRepository jpaServiceMeteringUseInfoRepository;
    private final JpaServiceMeteringViewRepository jpaServiceMeteringViewRepository;
    private final JpaServiceBatchRepository jpaServiceBatchRepository;

    private final JpaVwLngRepository jpaVwLngRepository;
    private final JpaServiceInstanceRepository jpaServiceInstanceRepository;


    public Scheduler(JpaServiceMeteringRepository jpaServiceMeteringRepository,
                     JpaServiceMeteringUseInfoRepository jpaServiceMeteringUseInfoRepository
            , JpaServiceMeteringViewRepository jpaServiceMeteringViewRepository
            , JpaServiceBatchRepository jpaServiceBatchRepository
            , JpaVwLngRepository jpaVwLngRepository
            , JpaServiceInstanceRepository jpaServiceInstanceRepository) {
        this.jpaServiceMeteringRepository = jpaServiceMeteringRepository;
        this.jpaServiceMeteringUseInfoRepository = jpaServiceMeteringUseInfoRepository;
        this.jpaServiceMeteringViewRepository = jpaServiceMeteringViewRepository;
        this.jpaServiceBatchRepository = jpaServiceBatchRepository;
        this.jpaVwLngRepository = jpaVwLngRepository;
        this.jpaServiceInstanceRepository = jpaServiceInstanceRepository;
    }

    @Scheduled(cron = "0 0/10 * * * *")
    //@Scheduled(cron = "* * * * * *")
    public void meteringView() {

        LocalDateTime startDateTime = LocalDateTime.now();  //시작시간

        try {
            logger.info("======== Scheduler Job Start ====  Start Time : {}  ======"+ startDateTime);

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
            String beginDate = LocalDateTime.now().minusMinutes(10).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            List<JpaServiceMeteringView> jpaServiceMeteringView = jpaServiceMeteringViewRepository.findAllByUpdDtBetween();

            for(int i = 0; i < jpaServiceMeteringView.size(); i++){

                String instnIdView = jpaServiceMeteringView.get(i).getInstnId();
                String svcStts = jpaServiceMeteringView.get(i).getSvcStt();

                //시작일자
                String creatDt = "";

                if(jpaServiceMeteringView.get(i).getCreatDt() == null){
                    creatDt = null;
                }else{
                    creatDt = getTimestampToDate(jpaServiceMeteringView.get(i).getCreatDt().toString());
                }

                //종료일자
                String updDt = "";

                if(jpaServiceMeteringView.get(i).getUpdDt() == null){
                    updDt = null;
                }else{
                    updDt = getTimestampToDate(jpaServiceMeteringView.get(i).getUpdDt().toString());
                }

                //상태가 사용중일때는 종료일자를 null값으로
                if("SS01".equals(svcStts)){
                    updDt = null;
                }

                //사용중
                if("SS01".equals(svcStts)){

                    List<JpaServiceMetering> jpaServiceMeteringValidCheck = jpaServiceMeteringRepository.findByServiceInstanceId(instnIdView);

                    if(jpaServiceMeteringValidCheck.size() < 1){

                        //미터링 테이블
                        jpaServiceMeteringRepository.save(JpaServiceMetering.builder()
                                .serviceId(jpaServiceMeteringView.get(i).getSvcId())
                                .serviceInstanceId(jpaServiceMeteringView.get(i).getInstnId())
                                .beginDe(creatDt)
                                .projectId(jpaServiceMeteringView.get(i).getPrjctId())
                                .mesurTy("MT1")
                                .useAt(1)
                                .creatId(jpaServiceMeteringView.get(i).getCreatUserId())
                                .updtId(jpaServiceMeteringView.get(i).getUpdUserId())
                                .creatDe(LocalDateTime.now())
                                .updtDe(LocalDateTime.now())
                                .build());


                        jpaServiceMeteringUseInfoRepository.save(JpaServiceMeteringUseInfo.builder()
                                .serviceId(jpaServiceMeteringView.get(i).getSvcId())
                                .serviceInstanceId(jpaServiceMeteringView.get(i).getInstnId())
                                .beginDe(creatDt)
                                .projectId(jpaServiceMeteringView.get(i).getPrjctId())
                                .svcSttus(jpaServiceMeteringView.get(i).getSvcStt())
                                .mesurAt(1)
                                .build());
                    }
                }

                //종료
                if("SS04".equals(svcStts)){

                    List<JpaServiceMetering> jpaServiceMeteringValidCheck = jpaServiceMeteringRepository.findByServiceInstanceId(instnIdView);

                    if(jpaServiceMeteringValidCheck.size() > 0){

                        List<JpaServiceMeteringUseInfo> meterUseList = jpaServiceMeteringUseInfoRepository.findByServiceInstanceIdAndAndSvcSttus(instnIdView, svcStts);

                        //USE INFO 데이터 중복체크
                        if(meterUseList.size() < 1){

                            //미터링 INFO 테이블
                            JpaServiceMetering jpaServiceMetering = jpaServiceMeteringRepository.findById(jpaServiceMeteringValidCheck.get(0).getMeteringId()).get();
                            jpaServiceMetering = jpaServiceMeteringRepository.findById(jpaServiceMeteringValidCheck.get(0).getMeteringId()).get();
                            jpaServiceMetering.setEndDe(updDt);
                            jpaServiceMetering.setUpdtDe(LocalDateTime.now());
                            jpaServiceMetering.setUpdtId(jpaServiceMeteringView.get(i).getUpdUserId());
                            jpaServiceMeteringRepository.saveAndFlush(jpaServiceMetering);

                            //미터링 USE_INFO 테이블
                            jpaServiceMeteringUseInfoRepository.save(JpaServiceMeteringUseInfo.builder()
                                    .serviceId(jpaServiceMeteringView.get(i).getSvcId())
                                    .serviceInstanceId(jpaServiceMeteringView.get(i).getInstnId())
                                    .beginDe(creatDt)
                                    .endDe(updDt)
                                    .projectId(jpaServiceMeteringView.get(i).getPrjctId())
                                    .svcSttus(jpaServiceMeteringView.get(i).getSvcStt())
                                    .mesurAt(1)
                                    .build());

                        }

                    }else if(jpaServiceMeteringValidCheck.size() == 0){

                        //SS04 종료 상태 데이터가 존재 하지 않을 경우

                        //미터링 INFO 테이블
                        jpaServiceMeteringRepository.save(JpaServiceMetering.builder()
                                .serviceId(jpaServiceMeteringView.get(i).getSvcId())
                                .serviceInstanceId(jpaServiceMeteringView.get(i).getInstnId())
                                .beginDe(creatDt)
                                .endDe(updDt)
                                .projectId(jpaServiceMeteringView.get(i).getPrjctId())
                                .mesurTy("MT1")
                                .useAt(1)
                                .creatId(jpaServiceMeteringView.get(i).getCreatUserId())
                                .updtId(jpaServiceMeteringView.get(i).getUpdUserId())
                                .creatDe(LocalDateTime.now())
                                .updtDe(LocalDateTime.now())
                                .build());

                        //미터링 USE_INFO 테이블
                        jpaServiceMeteringUseInfoRepository.save(JpaServiceMeteringUseInfo.builder()
                                .serviceId(jpaServiceMeteringView.get(i).getSvcId())
                                .serviceInstanceId(jpaServiceMeteringView.get(i).getInstnId())
                                .beginDe(creatDt)
                                .endDe(updDt)
                                .projectId(jpaServiceMeteringView.get(i).getPrjctId())
                                .svcSttus(jpaServiceMeteringView.get(i).getSvcStt())
                                .mesurAt(1)
                                .build());
                    }
                }
            }

            //종료시간
            LocalDateTime endDateTime = LocalDateTime.now();

            logger.info("======== Scheduler Job End ====  End Time  :  {}======"+ endDateTime);

            //성공로그기록
            log(startDateTime, endDateTime, "S");

        } catch (Exception ex ) {
            //종료시간
            LocalDateTime endDateTime = LocalDateTime.now();
            logger.info("======== Schedule Job Fail ====End Time : {} ======"+ LocalDateTime.now());
            //실패로그기록
            log(startDateTime, endDateTime, "F");

            logger.error(ex.getMessage());
        }
    }


    //배치테이블 기록
    private void log(LocalDateTime startDateTime, LocalDateTime endDateTime, String stts) {
        jpaServiceBatchRepository.save(JpaServiceBatch.builder()
                .batchName("MeteringView")
                .sucesStts(stts)
                .beginDe(startDateTime)
                .endDe(endDateTime)
                .build());
    }


    //unix time -> datetime으로 변경
    private static String getTimestampToDate(String timestampStr){
        long timestamp = Long.parseLong(timestampStr);
        Date date = new java.util.Date(timestamp*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";

        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        String formmattedDate = sdf.format(date);
        formmattedDate.replaceAll(match,"");

        return formmattedDate;
    }

//    @Scheduled(cron = "0 */3 * * * *")
    @Scheduled(cron = "0 5,15,25,35,45,55 * * * *")
    public void collectMeteringData() {
        logger.info("//// START JOB collectMeteringData");

        SimpleDateFormat prevFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMM");
        String lastJobExecTime = jpaServiceMeteringUseInfoRepository.findByBeginDeDesc();

        logger.info("//// lastJobExecTime : " + lastJobExecTime);
        // 1. 미터링 정보 조회 VIEW 테이블 조회
        List<JpaVwLng> vwLngList = jpaVwLngRepository.findAllByCrtDtBetween(lastJobExecTime);

//        System.out.println("/// VIEW LIST : " + vwLngList);
        for(JpaVwLng vwLng : vwLngList) {
            // 2. 미터링 추가 정보 조회 Service Inatance 테이블 조회
            JpaServiceInstn serviceInstn = jpaServiceInstanceRepository.findByClientId(vwLng.getClntId());
            // serviceInstn 정보가 없으면 안됨. 없는경우 skip
            if(serviceInstn == null) {
                continue;
            }

            // 처리 이력 데이터 생성, 저장은 finally 구문에서 처리(성공 실패 여부는 mesurAt 값으로 구분)
            JpaServiceMeteringUseInfo meteringUseInfo = JpaServiceMeteringUseInfo.builder()
                    .projectId(vwLng.getCrtPrjtId())
                    .serviceId(vwLng.getSvcId())
                    .serviceInstanceId(serviceInstn.getId())
                    .planId(serviceInstn.getServicePlan().getId())
                    .unitType("MU2")
                    .unitValue(new BigInteger("1"))
                    .svcSttus("MT2")
                    .beginDe(vwLng.getCrtDt())
                    .endDe(vwLng.getCrtDt())
                    .build();

            try {

                String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
                JpaServiceMetering serviceMetering = null;
                // 3. 호출형과 인스턴스 아이디가 같고 현재 yyyyMM(현재 년월)에 등록된게 있는지 조회
                List<JpaServiceMetering> meteringList = jpaServiceMeteringRepository.findByServiceInstanceIdAndMesurTy(serviceInstn.getId(), "MT2")
                        .stream()
//                        .filter(meteringInfo -> currentMonth.equals(meteringInfo.getCreatDe().format(DateTimeFormatter.ofPattern("yyyyMM"))))
                        .filter(meteringInfo -> {
                            boolean isSame = false;
                            try {
                                isSame = currentMonth.equals(transFormat.format(prevFormat.parse(meteringInfo.getBeginDe())));
                            } catch (ParseException e) {
                                logger.error(e.getMessage(), e);
                            }
                            return isSame;
                         })
                        .collect(Collectors.toList());

//                System.out.println("/// METERING INFO : " + meteringList);
                // 4-1. 등록된게 없으면(size == 0) 신규 데이터 등록
                if(meteringList.size() == 0) {
                    logger.info("/////////////////////////////////////////////////////////////// 신규 등록");
                    // 현재 월에 기존 데이터 없음, 신규 insert
                    serviceMetering = JpaServiceMetering.builder()
                            .serviceId(vwLng.getSvcId()) // vwlng.svc_id
                            .serviceInstanceId(serviceInstn.getId()) // serviceInstance.id
                            .planId(serviceInstn.getServicePlan().getId()) // serviceInstance.svc_plac_id
                            .projectId(vwLng.getCrtPrjtId()) // vwLng.crt_prjt_id
                            .mesurTy("MT2") // MT2
                            .beginDe(vwLng.getCrtDt())
                            .endDe(vwLng.getCrtDt())
                            .unitType("MU2")
                            .unitValue(new BigInteger("1"))
                            .useAt(1)
                            .creatDe(LocalDateTime.now())
                            .creatId(serviceInstn.getUserId())
                            .updtDe(LocalDateTime.now())
                            .updtId(serviceInstn.getUserId())
                            .build();
//                    System.out.println("/// METERING INFO NEW : " + serviceMetering);
                } else {
                    // 4-2. 등록된게 있으면 호출건수 + 1 업데이트
                    // 현재 월에 기존 데이터 있어서 호출 건수 + 1 후 업데이트
                    serviceMetering = meteringList.get(0); // 1건 초과된 데이터가 나와선 안됨.
                    BigInteger unitValue = serviceMetering.getUnitValue();
                    if(unitValue == null) {
                        unitValue = new BigInteger("0");
                    }
                    serviceMetering.setUnitValue(unitValue.add(new BigInteger("1")));
//                    System.out.println("/// METERING INFO MODIFY : " + serviceMetering);
                }

                jpaServiceMeteringRepository.save(serviceMetering);
                meteringUseInfo.setMesurAt(1); // 항목 정상 처리시 metering_use_info.mesurAt : 1
            } catch(Exception e) {
                logger.error(e.getMessage(), e);
                meteringUseInfo.setMesurAt(0); // 항목 처리 실패시 metering_use_info.mesurAt : 0
            } finally {
                // 5. 로그 이력 저장
                // 로그성 이력 metering_use_info 테이블에 등록
//                System.out.println("/// METERING USE INFO LOG : " + meteringUseInfo);
                jpaServiceMeteringUseInfoRepository.save(meteringUseInfo);
            }
        }

        logger.info("//// END JOB collectMeteringData");
    }
}
