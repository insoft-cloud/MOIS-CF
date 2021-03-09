package org.cf.broker.service;

import org.cf.broker.common.annotation.MessageMapper;
import org.cf.broker.exception.ServiceRuntimeException;
import org.cf.broker.model.common.ErrorMessage;
import org.cf.broker.model.jpa.JpaServiceImage;
import org.cf.broker.repo.JpaServiceImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    final JpaServiceImageRepository jpaServiceImageRepository;

    public ImageService(JpaServiceImageRepository jpaServiceImageRepository) {
        this.jpaServiceImageRepository = jpaServiceImageRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object getServiceImageList(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
            @MessageMapper(field = "image_type") String image_ty,
            @MessageMapper(field = "platform_type") String platform_ty,
            @MessageMapper(field = "service_type") String service_ty) {
        List<JpaServiceImage> jpaServiceImage = jpaServiceImageRepository.findAll().stream().filter(res -> (image_ty == null || res.getImageTy().equals(image_ty)))
                .filter(res -> (platform_ty == null || res.getPlatformType().equals(platform_ty)))
                .filter(res -> (service_ty == null || res.getServiceType().equals(service_ty)))
                .filter(res -> res.getUseAt() == 1).collect(Collectors.toList());

        return new ResponseEntity(jpaServiceImage, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object createServiceImage(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
            @MessageMapper(field = "platform_type") String platform_ty,
            @MessageMapper(field = "service_type") String service_ty,
            @MessageMapper(field = "image_nm") String image_nm,
            @MessageMapper(field = "use_at") String use_at,
            @MessageMapper(field = "image_type") String image_ty,
            @MessageMapper(field = "dc") String dc
    ) {

        jpaServiceImageRepository.save(JpaServiceImage.builder()
                .imageNm(image_nm)
                .dc(dc)
                .serviceType(service_ty)
                .platformType(platform_ty)
                .useAt(Integer.valueOf(use_at))
                .imageTy(image_ty)
                .creatId(usrId)
                .updtId(usrId)
                .build());
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object updateServiceImage(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
            @MessageMapper(field = "platform_type") String platform_ty,
            @MessageMapper(field = "service_type") String service_ty,
            @MessageMapper(field = "image_nm") String image_nm,
            @MessageMapper(field = "use_at") String use_at,
            @MessageMapper(field = "image_type") String image_ty,
            @MessageMapper(field = "dc") String dc,
            @MessageMapper(field = "service_image_id") String service_image_id) {

        int id = Integer.valueOf(service_image_id);
        if (!jpaServiceImageRepository.findById(id).isPresent()) {
            return ErrorMessage.builder().message(service_image_id + "- Invalid Service Broker ID").code(3050).build();
        }
        JpaServiceImage jpaServiceImage = jpaServiceImageRepository.findById(id).get();
        jpaServiceImage.setImageNm(image_nm);
        jpaServiceImage.setImageTy(image_ty);
        jpaServiceImage.setPlatformType(platform_ty);
        jpaServiceImage.setServiceType(service_ty);
        jpaServiceImage.setUseAt(Integer.valueOf(use_at));
        jpaServiceImage.setDc(dc);
        jpaServiceImage.setUpdtId(usrId);
        jpaServiceImage.setUpdtDe(LocalDateTime.now());
        jpaServiceImageRepository.save(jpaServiceImage);
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object deleteServiceImage(
            @MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
            @MessageMapper(field = "service_image_id") String service_image_id) {
        int id = Integer.valueOf(service_image_id);
        if (!jpaServiceImageRepository.findById(id).isPresent()) {
            return ErrorMessage.builder().message(service_image_id + "- Invalid Service Image ID").code(3050).build();
        } else if (jpaServiceImageRepository.findById(id).get().getUseAt() != 1) {
            return ErrorMessage.builder().message(service_image_id + "- Invalid Service Image ID").code(3050).build();
        }
        JpaServiceImage jpaServiceImage = jpaServiceImageRepository.findById(id).get();
        jpaServiceImage.setUseAt(0);
        jpaServiceImage.setUpdtId(usrId);
        jpaServiceImage.setUpdtDe(LocalDateTime.now());
        return new ResponseEntity(null, HttpStatus.OK);
    }

}
