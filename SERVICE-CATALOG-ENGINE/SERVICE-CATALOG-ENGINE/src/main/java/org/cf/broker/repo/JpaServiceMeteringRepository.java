package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceMetering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceMeteringRepository extends JpaRepository<JpaServiceMetering, Integer> {

    List<JpaServiceMetering> findByServiceInstanceId(@Param("service_instance_id") String service_instance_id);

    List<JpaServiceMetering> findByServiceInstanceIdAndMesurTy(@Param("svc_instn_id") String service_instance_id, @Param("mesur_ty") String mesur_ty);

}
