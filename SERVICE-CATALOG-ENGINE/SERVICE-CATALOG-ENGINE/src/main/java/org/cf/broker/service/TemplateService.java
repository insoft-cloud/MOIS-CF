package org.cf.broker.service;


import org.cf.broker.common.annotation.MessageMapper;
import org.cf.broker.model.QueueServiceTemplate;
import org.cf.broker.model.ResponseTemplate;
import org.cf.broker.model.ResponseTemplateService;
import org.cf.broker.model.common.ErrorMessage;
import org.cf.broker.model.jpa.JpaService;
import org.cf.broker.model.jpa.JpaServiceImage;
import org.cf.broker.model.jpa.JpaServiceTemplate;
import org.cf.broker.model.jpa.JpaServiceTemplateDetail;
import org.cf.broker.repo.JpaServiceRepository;
import org.cf.broker.repo.JpaServiceTemplateDetailRepository;
import org.cf.broker.repo.JpaServiceTemplateRepository;
import org.hibernate.sql.OracleJoinFragment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class TemplateService {

    final JpaServiceRepository jpaServiceRepository;
    final JpaServiceTemplateDetailRepository jpaServiceTemplateDetailRepository;
    final JpaServiceTemplateRepository jpaServiceTemplateRepository;

    public TemplateService(JpaServiceRepository jpaServiceRepository, JpaServiceTemplateDetailRepository jpaServiceTemplateDetailRepository, JpaServiceTemplateRepository jpaServiceTemplateRepository) {
        this.jpaServiceRepository = jpaServiceRepository;
        this.jpaServiceTemplateDetailRepository = jpaServiceTemplateDetailRepository;
        this.jpaServiceTemplateRepository = jpaServiceTemplateRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object getServiceTemplateList(@MessageMapper(field = "use_at") String useAt) {
        List<ResponseTemplate> jpaServiceTemplateList = jpaServiceTemplateRepository.findAll().stream()
                .filter(res -> res.getUseAt() == Integer.valueOf(useAt))
                .flatMap(res -> Stream.of(ResponseTemplate.builder()
                        .useAt(String.valueOf(res.getUseAt()))
                        .dc(res.getDc())
                        .serviceTemplateId(String.valueOf(res.getId()))
                        .tag(res.getTag())
                        .templateFile(res.getTmplatFilm())
                        .templateNm(res.getTmplatNm())
                        .templateServices(res.getServiceTemplateDetails()
                                .stream()
                                .flatMap(r -> Stream.of(new ResponseTemplateService(r.getJpaService())))
                                .collect(Collectors.toList())
                        )
                        .build()))
                .collect(Collectors.toList());

        return new ResponseEntity(jpaServiceTemplateList, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object createServiceTemplate(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
            QueueServiceTemplate queue) {
        if (queue.getTemplate_services() instanceof List) {
            List<String> services = (List<String>) queue.getTemplate_services();
            List<JpaService> jpaServices = new ArrayList<>();
            for (String id : services) {
                if (!jpaServiceRepository.findById(id).isPresent()) {
                    return ErrorMessage.builder().message(id + "- Invalid Service ID").code(3010).build();
                } else if (jpaServiceRepository.findById(id).get().getUseAt() != 1) {
                    return ErrorMessage.builder().message(id + "- Invalid Service ID").code(3010).build();
                }
                jpaServices.add(jpaServiceRepository.findById(id).get());
            }
            JpaServiceTemplate jpaServiceTemplate = JpaServiceTemplate.builder()
                    .dc(queue.getDc())
                    .tag(queue.getTag())
                    .useAt(Integer.valueOf(queue.getUse_at()))
                    .tmplatNm(queue.getTemplate_nm())
                    .tmplatFilm(queue.getTemplate_file())
                    .build();
            jpaServiceTemplateRepository.save(jpaServiceTemplate);
            for (String id : services) {
                jpaServiceTemplateDetailRepository.save(JpaServiceTemplateDetail.builder()
                        .jpaService(jpaServiceRepository.findById(id).get())
                        .jpaServiceTemplate(jpaServiceTemplate)
                        .creatId(usrId)
                        .updtId(usrId)
                        .build());
            }

        } else {
            return ErrorMessage.builder().message("Service Template Error").code(1001).build();
        }

        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object updateServiceTemplate(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
            QueueServiceTemplate queue
    ) {
        if (queue.getTemplate_services() instanceof List) {
            List<String> services = (List<String>) queue.getTemplate_services();
            List<JpaService> jpaServices = new ArrayList<>();
            for (String id : services) {
                if (!jpaServiceRepository.findById(id).isPresent()) {
                    return ErrorMessage.builder().message(id + "- Invalid Service ID").code(3010).build();
                } else if (jpaServiceRepository.findById(id).get().getUseAt() != 1) {
                    return ErrorMessage.builder().message(id + "- Invalid Service ID").code(3010).build();
                }
                jpaServices.add(jpaServiceRepository.findById(id).get());
            }
            if (!jpaServiceTemplateRepository.findById(Integer.valueOf(queue.getService_template_id())).isPresent()) {
                return ErrorMessage.builder().message(queue.getService_template_id() + "- Invalid Service Template ID").code(3015).build();
            } else if (jpaServiceTemplateRepository.findById(Integer.valueOf(queue.getService_template_id())).get().getUseAt() != 1) {
                return ErrorMessage.builder().message(queue.getService_template_id() + "- Invalid Service Template ID").code(3015).build();
            }
            JpaServiceTemplate jpaServiceTemplate = jpaServiceTemplateRepository.findById(Integer.valueOf(queue.getService_template_id())).get();
            jpaServiceTemplate.setDc(queue.getDc());
            jpaServiceTemplate.setTag(queue.getTag());
            jpaServiceTemplate.setUseAt(Integer.valueOf(queue.getUse_at()));
            jpaServiceTemplate.setTmplatNm(queue.getTemplate_nm());
            jpaServiceTemplate.setTmplatFilm(queue.getTemplate_file());
            jpaServiceTemplate.getServiceTemplateDetails().clear();

            jpaServiceTemplateDetailRepository.deleteAll(jpaServiceTemplate.getServiceTemplateDetails());
            for (String id : services) {
                jpaServiceTemplate.getServiceTemplateDetails().add(JpaServiceTemplateDetail.builder()
                        .jpaService(jpaServiceRepository.findById(id).get())
                        .jpaServiceTemplate(jpaServiceTemplate)
                        .creatId(usrId)
                        .updtId(usrId)
                        .build());
            }
            jpaServiceTemplateRepository.save(jpaServiceTemplate);
        } else {
            return ErrorMessage.builder().message("Service Template Error").code(1001).build();
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object deleteServiceTemplate(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
            @MessageMapper(field = "service_template_id") String service_template_id) {
        if (!jpaServiceTemplateRepository.findById(Integer.valueOf(service_template_id)).isPresent()) {
            return ErrorMessage.builder().message(service_template_id + "- Invalid Service Template ID").code(3015).build();
        } else if (jpaServiceTemplateRepository.findById(Integer.valueOf(service_template_id)).get().getUseAt() != 1) {
            return ErrorMessage.builder().message(service_template_id + "- Invalid Service Template ID").code(3015).build();
        }
        JpaServiceTemplate jpaServiceTemplate = jpaServiceTemplateRepository.findById(Integer.valueOf(service_template_id)).get();
        jpaServiceTemplate.setUseAt(0);
        for (JpaServiceTemplateDetail jpaServiceTemplateDetail : jpaServiceTemplate.getServiceTemplateDetails()) {
            jpaServiceTemplateDetail.setUpdtId(usrId);
        }
        jpaServiceTemplateDetailRepository.saveAll(jpaServiceTemplate.getServiceTemplateDetails());
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
