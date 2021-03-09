package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceMeteringUseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceMeteringUseInfoRepository extends JpaRepository<JpaServiceMeteringUseInfo, Integer> {

    List<JpaServiceMeteringUseInfo> findByServiceInstanceId(@Param("service_instance_id") String service_instance_id);

    List<JpaServiceMeteringUseInfo> findByServiceInstanceIdAndAndSvcSttus(@Param("service_instance_id") String service_instance_id, @Param("svc_sttus") String svc_sttus);

    @Query(value = "" +
            " SELECT begin_de" +
            "   FROM metering_use_info" +
            "  WHERE svc_sttus = 'MT2'" +
            "  ORDER BY begin_de desc" +
            "   LIMIT 1"
            , nativeQuery = true)
    String findByBeginDeDesc();
}

