package org.cf.broker.service;

import org.cf.broker.common.annotation.MessageMapper;
import org.cf.broker.model.common.ErrorMessage;
import org.cf.broker.model.jpa.*;
import org.cf.broker.repo.*;
import org.cf.broker.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeteringService {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);


    private final JpaServiceMeteringRepository jpaServiceMeteringRepository;
    private final JpaServiceMeteringInfoRepository jpaServiceMeteringInfoRepository;
    private final JpaServiceInstanceModelRepository jpaServiceInstanceModelRepository;
    private final JpaServiceMeteringUseInfoRepository jpaServiceMeteringUseInfoRepository;
    private final JpaServiceMeteringRequestTypeRepository jpaServiceMeteringRequestTypeRepository;


    public MeteringService(JpaServiceMeteringRepository jpaServiceMeteringRepository, JpaServiceMeteringInfoRepository jpaServiceMeteringInfoRepository,
                           JpaServiceInstanceModelRepository jpaServiceInstanceModelRepository, JpaServiceMeteringUseInfoRepository jpaServiceMeteringUseInfoRepository, JpaServiceMeteringRequestTypeRepository jpaServiceMeteringRequestTypeRepository) {
        this.jpaServiceMeteringRepository = jpaServiceMeteringRepository;
        this.jpaServiceMeteringInfoRepository = jpaServiceMeteringInfoRepository;
        this.jpaServiceInstanceModelRepository = jpaServiceInstanceModelRepository;
        this.jpaServiceMeteringUseInfoRepository = jpaServiceMeteringUseInfoRepository;
        this.jpaServiceMeteringRequestTypeRepository = jpaServiceMeteringRequestTypeRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object meteringServiceAllocation(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId
            , @MessageMapper(field = "fromPartyId",  dataLoc = MessageMapper.HEADER) String fromPartyId
            , @MessageMapper(field = "crud", dataLoc = MessageMapper.HEADER) String crud
            , @MessageMapper(field = "service_id") String service_id
            , @MessageMapper(field = "service_instance_id") String service_instance_id
            , @MessageMapper(field = "plan_id") String plan_id
            , @MessageMapper(field = "project_id") String project_id
    ) {

/*      logger.info("EgovplatformMsgHeader  msgPubUserId : {} || crud: {} ", usrId, crud);
        logger.info("service_instn table  svc_id = '{}' AND id = '{}' AND plan_id = '{}' AND project_id = '{}' AND use_at = '{}' ", service_id, service_instance_id, plan_id, project_id, '1');
        logger.info("metering_info table  svc_id = '{}' AND svc_instn_id = '{}' AND svc_plan_id = '{}'  AND prjct_id = '{}' ", service_id, service_instance_id,plan_id, project_id);*/

        //인스턴스 존재여부 체크
        List<JpaServiceInstnModel> jpaServiceInstnModel = jpaServiceInstanceModelRepository.findAll().stream()
                .filter(res -> (service_id == null || res.getService().equals(service_id)))
                .filter(res -> (service_instance_id == null || res.getId().equals(service_instance_id)))
                .filter(res -> (project_id == null || res.getProjectId().equals(project_id)))
                .filter(res -> (plan_id == null || res.getServicePlan().equals(plan_id)))
                .collect(Collectors.toList());

        if(!"CF".equals(fromPartyId.toUpperCase())){
            if(jpaServiceInstnModel.size() < 1){
                return ErrorMessage.builder().message(service_instance_id + " - Invalid Service Instance ID").code(3013).build();
            }
        }

        if("c".equalsIgnoreCase(crud)){

            //미터링 중복체크
            List<JpaServiceMetering> jpaServiceMetering = jpaServiceMeteringRepository.findAll().stream()
                    .filter(res -> (service_id == null || res.getServiceId().equals(service_id)))
                    .filter(res -> (service_instance_id == null || res.getServiceInstanceId().equals(service_instance_id)))
                    .filter(res -> (project_id == null || res.getProjectId().equals(project_id)))
                    .filter(res -> (plan_id == null || res.getPlanId().equals(plan_id)))
                    .collect(Collectors.toList());

            if(jpaServiceMetering.size() > 0){
                return ErrorMessage.builder().message(" - Duplicate Metering Service Instance ID").code(3070).build();
            }

            jpaServiceMeteringRepository.save(JpaServiceMetering.builder()
                    .serviceId(service_id)
                    .serviceInstanceId(service_instance_id)
                    .planId(plan_id)
                    .mesurTy("MT1")
                    .beginDe(LocalDateTime.now().toString())
                    .projectId(project_id)
                    .useAt(1)
                    .creatId(usrId)
                    .updtId(usrId)
                    .build());

        }else if("d".equalsIgnoreCase(crud)){

            //미터링 존재여부 체크
            List<JpaServiceMetering> jpaServiceMetering = jpaServiceMeteringRepository.findAll().stream()
                    .filter(res -> (service_id == null || res.getServiceId().equals(service_id)))
                    .filter(res -> (service_instance_id == null || res.getServiceInstanceId().equals(service_instance_id)))
                    .filter(res -> (project_id == null || res.getProjectId().equals(project_id)))
                    .filter(res -> (plan_id == null || res.getPlanId().equals(plan_id)))
                    .collect(Collectors.toList());

            if(jpaServiceMetering.size() < 1){
                return ErrorMessage.builder().message(service_instance_id + "- Invalid Service Instance ID").code(3013).build();
            }

            int id = jpaServiceMetering.get(0).getMeteringId();

            JpaServiceMetering jpaServiceMeteringSave = jpaServiceMeteringRepository.findById(id).get();
            jpaServiceMeteringSave = jpaServiceMeteringRepository.findById(id).get();
            jpaServiceMeteringSave.setEndDe(LocalDateTime.now().toString());
            jpaServiceMeteringSave.setUpdtDe(LocalDateTime.now());
            jpaServiceMeteringSave.setUpdtId(usrId);
            jpaServiceMeteringRepository.save(jpaServiceMeteringSave);

            //logger.info("MeteringServiceAllocation EgovplatformMsgBody  svc_id = '{}' AND svc_instn_id = '{}' AND svc_plan_id = '{}'  AND prjct_id = '{}' AND use_at = 1 ", service_id, service_instance_id,plan_id, project_id);

        }else{
            return ErrorMessage.builder().message("- Metering Service CURD Check").code(3071).build();
        }

        return new ResponseEntity(null, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object meteringServiceCall(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId
            ,@MessageMapper(field = "fromPartyId",  dataLoc = MessageMapper.HEADER) String fromPartyId
            ,@MessageMapper(field = "crud", dataLoc = MessageMapper.HEADER) String crud
            ,@MessageMapper(field = "service_id") String service_id
            ,@MessageMapper(field = "service_instance_id") String service_instance_id
            ,@MessageMapper(field = "plan_id") String plan_id
            ,@MessageMapper(field = "project_id") String project_id
            ,@MessageMapper(field = "unit_type") String unit_type
            ,@MessageMapper(field = "unit_value") String unit_value
    ) {

 /*     logger.info("service_instn table  svc_id = '{}' AND id = '{}' AND plan_id = '{}' AND project_id = '{}' ", service_id, service_instance_id, plan_id, project_id,);

        logger.info("service_id : {} || service_instance_id: {} || plan_id: {}  || project_id: {} || unit_type: {} || unit_value: {}", service_id, service_instance_id, plan_id, project_id, unit_type, unit_value);

        logger.info("metering_info table  svc_id = '{}' AND svc_instn_id = '{}' AND svc_plan_id = '{}'  AND prjct_id = '{}' ", service_id, service_instance_id,plan_id, project_id);*/

        //인스턴스 존재여부 체크
        List<JpaServiceInstnModel> jpaServiceInstnModel = jpaServiceInstanceModelRepository.findAll().stream()
                .filter(res -> (service_id == null || res.getService().equals(service_id)))
                .filter(res -> (service_instance_id == null || res.getId().equals(service_instance_id)))
                .filter(res -> (project_id == null || res.getProjectId().equals(project_id)))
                .filter(res -> (plan_id == null || res.getServicePlan().equals(plan_id))).collect(Collectors.toList());

        BigInteger limitValue = new BigInteger("9999999999999999999");

        if(!"CF".equals(fromPartyId.toUpperCase())){
            if(jpaServiceInstnModel.size() < 1){
                return ErrorMessage.builder().message(service_instance_id + " - Invalid Service Instance ID").code(3013).build();
            }
        }

        //unit_value 파라메터 값이 숫자인지 확인
        if (!checkNumber(unit_value)){
            return ErrorMessage.builder().message(" - Metering Unit Value Not Number").code(3072).build();
        }

        if("c".equalsIgnoreCase(crud)){

            //미터링 중복체크
            List<JpaServiceMetering> jpaServiceMetering = jpaServiceMeteringRepository.findAll().stream()
                    .filter(res -> (service_id == null || res.getServiceId().equals(service_id)))
                    .filter(res -> (service_instance_id == null || res.getServiceInstanceId().equals(service_instance_id)))
                    .filter(res -> (project_id == null || res.getProjectId().equals(project_id)))
                    .filter(res -> (plan_id == null || res.getPlanId().equals(plan_id)))
                    .collect(Collectors.toList());

            //logger.info("MeteringServiceCall Service Metering Size : {}", jpaServiceMetering.size());

            if(jpaServiceMetering.size() > 0) {

                //호출형으로 등록되어있는지 확인
                if(!"MT2".equals(jpaServiceMetering.get(0).getMesurTy())){
                    return ErrorMessage.builder().message("- Metering Invalid Metering Type").code(3073).build();
                }

                BigInteger dUnitValue = jpaServiceMetering.get(0).getUnitValue();

                //미터링 unit_value ,존재 유무 확인
                if(dUnitValue == null){
                    return ErrorMessage.builder().message(" - Metering Unit Value Not Number").code(3072).build();
                }

                BigInteger pUnitValue = new BigInteger(unit_value);

                BigInteger sumUnitValue = pUnitValue.add(dUnitValue);

                //unit_value 입력값 제한
                if(limitValue.compareTo(sumUnitValue) == -1){
                    return ErrorMessage.builder().message("- Metering Unit Value Limit:'9999999999999999999'").code(3074).build();
                }

                int id = jpaServiceMetering.get(0).getMeteringId();

                //미터링 테이블
                JpaServiceMetering jpaServiceMeteringSave = jpaServiceMeteringRepository.findById(id).get();
                jpaServiceMeteringSave = jpaServiceMeteringRepository.findById(id).get();
                jpaServiceMeteringSave.setUpdtDe(LocalDateTime.now());
                jpaServiceMeteringSave.setUnitValue(sumUnitValue);
                jpaServiceMeteringSave.setUpdtId(usrId);
                jpaServiceMeteringRepository.save(jpaServiceMeteringSave);

                //미터링 USE INFO 테이블
                jpaServiceMeteringUseInfoRepository.save(JpaServiceMeteringUseInfo.builder()
                        .serviceId(service_id)
                        .serviceInstanceId(service_instance_id)
                        .planId(plan_id)
                        .beginDe(LocalDateTime.now().toString())
                        .unitType(unit_type)
                        .unitValue(pUnitValue)
                        .projectId(project_id)
                        .mesurAt(1)
                        .build());

            }else{

                BigInteger pUnitValue = new BigInteger(unit_value);

                //unit_value 입력값 제한
                if(limitValue.compareTo(pUnitValue) == -1){
                    return ErrorMessage.builder().message("- Metering Unit Value Limit:'9999999999999999999'").code(3074).build();
                }

                //미터링 테이블
                jpaServiceMeteringRepository.save(JpaServiceMetering.builder()
                        .serviceId(service_id)
                        .serviceInstanceId(service_instance_id)
                        .planId(plan_id)
                        .mesurTy("MT2")
                        .beginDe(LocalDateTime.now().toString())
                        .unitType(unit_type)
                        .unitValue(pUnitValue)
                        .projectId(project_id)
                        .useAt(1)
                        .creatId(usrId)
                        .updtId(usrId)
                        .build());

                //미터링 USE INFO 테이블
                jpaServiceMeteringUseInfoRepository.save(JpaServiceMeteringUseInfo.builder()
                        .serviceId(service_id)
                        .serviceInstanceId(service_instance_id)
                        .planId(plan_id)
                        .beginDe(LocalDateTime.now().toString())
                        .unitType(unit_type)
                        .unitValue(pUnitValue)
                        .projectId(project_id)
                        .mesurAt(1)
                        .build());
            }



        }else if("d".equalsIgnoreCase(crud)){

            //미터링 존재여부 체크
            List<JpaServiceMetering> jpaServiceMetering = jpaServiceMeteringRepository.findAll().stream()
                    .filter(res -> (service_id == null || res.getServiceId().equals(service_id)))
                    .filter(res -> (service_instance_id == null || res.getServiceInstanceId().equals(service_instance_id)))
                    .filter(res -> (project_id == null || res.getProjectId().equals(project_id)))
                    .filter(res -> (plan_id == null || res.getPlanId().equals(plan_id)))
                    .collect(Collectors.toList());

            if(jpaServiceMetering.size() < 1){
                return ErrorMessage.builder().message(service_instance_id + "- Invalid Service Instance ID").code(3013).build();
            }

            int id = jpaServiceMetering.get(0).getMeteringId();

           /*logger.info("Metering Id : {} ", id);

           logger.info("EgovplatformMsgBody  svc_id = '{}' AND svc_instn_id = '{}' AND svc_plan_id = '{}'  AND prjct_id = '{}'  ", service_id, service_instance_id,plan_id, project_id);*/

            //미터링 테이블
            JpaServiceMetering jpaServiceMeteringSave = jpaServiceMeteringRepository.findById(id).get();
            jpaServiceMeteringSave = jpaServiceMeteringRepository.findById(id).get();
            jpaServiceMeteringSave.setEndDe(LocalDateTime.now().toString());
            jpaServiceMeteringSave.setUpdtDe(LocalDateTime.now());
            jpaServiceMeteringSave.setUpdtId(usrId);

        }else{
            return ErrorMessage.builder().message("- Metering Service CURD Check").code(3071).build();
        }

        return new ResponseEntity(null, HttpStatus.OK);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Object meteringServiceRetreive(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId
            ,@MessageMapper(field = "crud", dataLoc = MessageMapper.HEADER) String crud
            ,@MessageMapper(field = "project_id") String project_id
            ,@MessageMapper(field = "service_instance_id") String service_instance_id
            ,@MessageMapper(field = "use_month") String use_month
    )
    {
        //logger.info(" project_id = '{}' AND service_instance_id = '{}' AND use_month = '{}' ", project_id, service_instance_id, use_month);

        List<JpaServiceMeteringInfo> jpaServiceMeteringInfo = null;

        if("r".equalsIgnoreCase(crud)) {
            jpaServiceMeteringInfo = jpaServiceMeteringInfoRepository.selectJpaServiceMetering(project_id, service_instance_id, use_month);
        }else{
            return ErrorMessage.builder().message("- Metering Service CURD Check").code(3071).build();
        }

        return new ResponseEntity(jpaServiceMeteringInfo, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object meteringServiceMetricsCreate(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId
            //,@MessageMapper(field = "unit_type") String unit_type
            ,@MessageMapper(field = "unit_info") String unit_info
            ,@MessageMapper(field = "unit_nm") String unit_nm
            ,@MessageMapper(field = "description") String description
    ){

//        JpaServiceMeteringRequestType JpaServiceMeteringRequestType = jpaServiceMeteringRequestTypeRepository.findByUnitType(unit_type);
//
//        if(JpaServiceMeteringRequestType != null){
//            return ErrorMessage.builder().message(" - Duplicate Metering Unit Type ID").code(3070).build();
//        }
        String unit_type = jpaServiceMeteringRequestTypeRepository.createMeteringRequertTypeUnitTypeKey();

        jpaServiceMeteringRequestTypeRepository.save(JpaServiceMeteringRequestType.builder()
                .unitType(unit_type)
                .unitInfo(unit_info)
                .unitNm(unit_nm)
                .description(description)
                .useAt(1)
                .creatDe(LocalDateTime.now())
                .creatId(usrId)
                .updtDe(LocalDateTime.now())
                .updtId(usrId)
                .build());

        return new ResponseEntity("", HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object meteringServiceMetricsUpdate(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId
            ,@MessageMapper(field = "unit_type") String unit_type
            ,@MessageMapper(field = "unit_info") String unit_info
            ,@MessageMapper(field = "unit_nm") String unit_nm
            ,@MessageMapper(field = "description") String description
    ){

        JpaServiceMeteringRequestType JpaServiceMeteringRequestType = jpaServiceMeteringRequestTypeRepository.findByUnitType(unit_type);

        if(JpaServiceMeteringRequestType == null){
            return ErrorMessage.builder().message(unit_type + "- Invalid Unit Type ID").code(3013).build();
        }

        //미터링 테이블
        JpaServiceMeteringRequestType JpaServiceMeteringRequestTypeSave = jpaServiceMeteringRequestTypeRepository.findByUnitType(unit_type);
        JpaServiceMeteringRequestTypeSave = jpaServiceMeteringRequestTypeRepository.findByUnitType(unit_type);
        JpaServiceMeteringRequestTypeSave.setUnitInfo(unit_info);
        JpaServiceMeteringRequestTypeSave.setUnitNm(unit_nm);
        JpaServiceMeteringRequestTypeSave.setDescription(description);
        JpaServiceMeteringRequestTypeSave.setUpdtDe(LocalDateTime.now());
        JpaServiceMeteringRequestTypeSave.setUpdtId(usrId);
        jpaServiceMeteringRequestTypeRepository.save(JpaServiceMeteringRequestTypeSave);


        return new ResponseEntity("", HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object meteringServiceMetricsDelete(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId
            ,@MessageMapper(field = "unit_type") String unit_type
    ){

        JpaServiceMeteringRequestType JpaServiceMeteringRequestType = jpaServiceMeteringRequestTypeRepository.findByUnitType(unit_type);

        if(JpaServiceMeteringRequestType == null){
            return ErrorMessage.builder().message(unit_type + "- Invalid Unit Type ID").code(3013).build();
        }

        //jpaServiceMeteringRequestTypeRepository.delete(JpaServiceMeteringRequestType);


        //미터링 테이블
        JpaServiceMeteringRequestType JpaServiceMeteringRequestTypeSave = jpaServiceMeteringRequestTypeRepository.findByUnitType(unit_type);
        JpaServiceMeteringRequestTypeSave = jpaServiceMeteringRequestTypeRepository.findByUnitType(unit_type);
        JpaServiceMeteringRequestTypeSave.setUseAt(0);
        JpaServiceMeteringRequestTypeSave.setUpdtDe(LocalDateTime.now());
        JpaServiceMeteringRequestTypeSave.setUpdtId(usrId);
        //jpaServiceMeteringUseInfoRepository.save(JpaServiceMeteringRequestTypeSave);
        jpaServiceMeteringRequestTypeRepository.save(JpaServiceMeteringRequestTypeSave);

        return new ResponseEntity("", HttpStatus.OK);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Object meteringServiceMetricsRetreive(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId
            ,@MessageMapper(field = "unit_type") String unit_type
            ,@MessageMapper(field = "unit_nm") String unit_nm
    ){

        List<JpaServiceMeteringRequestType> jpaServiceMeteringRequestType = null;

        if("".equals(unit_type.toUpperCase()) && "".equals(unit_nm.toUpperCase())){
            //전체조회
            jpaServiceMeteringRequestType = jpaServiceMeteringRequestTypeRepository.findAll().stream().filter(res -> res.getUseAt() == 1).collect(Collectors.toList());
        }else if(!"".equals(unit_nm.toUpperCase())){
            //단위명 Like 조회
            jpaServiceMeteringRequestType = jpaServiceMeteringRequestTypeRepository.findByUnitNmContainingAndUseAt(unit_nm, 1);
        }else if(!"".equals(unit_type.toUpperCase())){
            //단위유형 조회
            jpaServiceMeteringRequestType = jpaServiceMeteringRequestTypeRepository.findAll().stream().filter(res -> res.getUseAt() == 1).filter(res -> (unit_type == null || res.getUnitType().equals(unit_type))).collect(Collectors.toList());
        }

        return new ResponseEntity(jpaServiceMeteringRequestType, HttpStatus.OK);
    }

    //숫자 확인 여부
    public boolean checkNumber(String str){

        char check;

        for(int i = 0; i< str.length(); i++){
            check = str.charAt(i);
            if( check < 48 || check > 58){
                //해당 char값이 숫자가 아닐 경우
                return false;
            }
        }
        return true;
    }

}
