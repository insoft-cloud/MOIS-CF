package org.cf.broker;

import org.cf.broker.model.jpa.JpaServiceMetering;
import org.cf.broker.model.jpa.JpaServiceMeteringUseInfo;
import org.cf.broker.model2.JpaServiceMeteringView;
import org.cf.broker.repo.*;
import org.cf.broker.repository.JpaServiceMeteringViewRepository;
import org.cf.broker.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ApplicationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private final JpaServiceMeteringRepository jpaServiceMeteringRepository;
    private final JpaServiceMeteringUseInfoRepository jpaServiceMeteringUseInfoRepository;
    private final JpaServiceMeteringViewRepository jpaServiceMeteringViewRepository;


    public ApplicationRunner(JpaServiceMeteringRepository jpaServiceMeteringRepository,
                      JpaServiceMeteringUseInfoRepository jpaServiceMeteringUseInfoRepository
                    , JpaServiceMeteringViewRepository jpaServiceMeteringViewRepository) {
        this.jpaServiceMeteringRepository = jpaServiceMeteringRepository;
        this.jpaServiceMeteringUseInfoRepository = jpaServiceMeteringUseInfoRepository;
        this.jpaServiceMeteringViewRepository = jpaServiceMeteringViewRepository;
    }

    @Override
    public void run(String[] args) throws Exception {

        logger.info("======== Init Run Start ==========");

        try {

            List<String> svc_stts = Arrays.asList("SS01","SS04");   //사용중, 종료 상태인것만 조회
            List<JpaServiceMeteringView> jpaServiceMeteringViewInit = jpaServiceMeteringViewRepository.findBySvcSttIn(svc_stts);

            for (int i = 0; i < jpaServiceMeteringViewInit.size(); i++){

                String instnIdView = jpaServiceMeteringViewInit.get(i).getInstnId();

                String svcStts = jpaServiceMeteringViewInit.get(i).getSvcStt();

                String creatDt = "";

                if(jpaServiceMeteringViewInit.get(i).getCreatDt() == null){
                    creatDt = null;
                }else{
                    creatDt = getTimestampToDate(jpaServiceMeteringViewInit.get(i).getCreatDt().toString());
                }

                String updDt = "";

                if(jpaServiceMeteringViewInit.get(i).getUpdDt() == null){
                    updDt = null;
                }else{
                    updDt = getTimestampToDate(jpaServiceMeteringViewInit.get(i).getUpdDt().toString());
                }

                //상태가 사용중일때는 종료일자를 null값으로
                if("SS01".equals(svcStts)){
                    updDt = null;
                }

                List<JpaServiceMeteringUseInfo> meterUseList = jpaServiceMeteringUseInfoRepository.findByServiceInstanceId(instnIdView);

                if(meterUseList.size() < 1){

                    List<JpaServiceMetering> jpaServiceMeteringValidCheck = jpaServiceMeteringRepository.findByServiceInstanceId(instnIdView);

                    if(jpaServiceMeteringValidCheck.size() < 1){

                        //미터링 테이블
                        jpaServiceMeteringRepository.save(JpaServiceMetering.builder()
                                .serviceId(jpaServiceMeteringViewInit.get(i).getSvcId())
                                .serviceInstanceId(jpaServiceMeteringViewInit.get(i).getInstnId())
                                .beginDe(creatDt)
                                .endDe(updDt)
                                .projectId(jpaServiceMeteringViewInit.get(i).getPrjctId())
                                .useAt(1)
                                .mesurTy("MT1")
                                .creatId(jpaServiceMeteringViewInit.get(i).getCreatUserId())
                                .updtId(jpaServiceMeteringViewInit.get(i).getUpdUserId())
                                .creatDe(LocalDateTime.now())
                                .updtDe(LocalDateTime.now())
                                .build());

                        //미터링 USE INFO 테이블
                        jpaServiceMeteringUseInfoRepository.save(JpaServiceMeteringUseInfo.builder()
                                .serviceId(jpaServiceMeteringViewInit.get(i).getSvcId())
                                .serviceInstanceId(jpaServiceMeteringViewInit.get(i).getInstnId())
                                .beginDe(creatDt)
                                .endDe(updDt)
                                .projectId(jpaServiceMeteringViewInit.get(i).getPrjctId())
                                .svcSttus(jpaServiceMeteringViewInit.get(i).getSvcStt())
                                .mesurAt(1)
                                .build());
                    }

                }else{

                    List<JpaServiceMetering> memtering = jpaServiceMeteringRepository.findByServiceInstanceId(instnIdView);

                    if(memtering.size() > 0){

                        JpaServiceMetering jpaServiceMetering = jpaServiceMeteringRepository.findById(memtering.get(0).getMeteringId()).get();
                        jpaServiceMetering = jpaServiceMeteringRepository.findById(memtering.get(0).getMeteringId()).get();
                        jpaServiceMetering.setEndDe(updDt);
                        jpaServiceMetering.setUpdtDe(LocalDateTime.now());
                        jpaServiceMetering.setUpdtId(jpaServiceMeteringViewInit.get(i).getUpdUserId());
                        jpaServiceMeteringRepository.saveAndFlush(jpaServiceMetering);


                        List<JpaServiceMeteringUseInfo> meterUseInfo = jpaServiceMeteringUseInfoRepository.findByServiceInstanceIdAndAndSvcSttus(instnIdView, svcStts);

                        //USE INFO 데이터 중복체크
                        if(meterUseInfo.size() < 1){
                            jpaServiceMeteringUseInfoRepository.save(JpaServiceMeteringUseInfo.builder()
                                    .serviceId(jpaServiceMeteringViewInit.get(i).getSvcId())
                                    .serviceInstanceId(jpaServiceMeteringViewInit.get(i).getInstnId())
                                    .beginDe(creatDt)
                                    .endDe(updDt)
                                    .projectId(jpaServiceMeteringViewInit.get(i).getPrjctId())
                                    .svcSttus(jpaServiceMeteringViewInit.get(i).getSvcStt())
                                    .mesurAt(1)
                                    .build());
                        }
                    }
                }
            }
        } catch (Exception ex ) {
            logger.info("======== Init Run Fail ==========");
            logger.error(ex.getMessage());
        }

        logger.info("======== Init Run End ==========");
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

}
