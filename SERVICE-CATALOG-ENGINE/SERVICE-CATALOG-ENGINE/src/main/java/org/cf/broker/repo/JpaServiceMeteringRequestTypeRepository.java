package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceMeteringRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceMeteringRequestTypeRepository extends JpaRepository<JpaServiceMeteringRequestType, String> {

    JpaServiceMeteringRequestType findByUnitType(@Param("unit_ty") String unit_ty);

    @Query(value = "SELECT CONCAT('MU',(SELECT MAX(SUBSTR(unit_ty , 3) + 1) FROM metering_request_type)) AS unit_ty", nativeQuery = true)
    String createMeteringRequertTypeUnitTypeKey();

    List<JpaServiceMeteringRequestType> findByUnitNmContainingAndUseAt(@Param("unit_nm") String unit_nm,  @Param("use_at") int use_at);

}
