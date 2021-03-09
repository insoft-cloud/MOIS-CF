package org.cf.broker.repository3;

import org.cf.broker.model3.JpaVwLng;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaVwLngRepository extends CrudRepository<JpaVwLng, String> {

        @Query(value = "" +
                " SELECT svc_id, svc_nm, http_stat_cd, app_nm, call_success, crt_dt, res_tm, data_sz_res, data_sz, crt_prjt_id, api_uri, clnt_id" +
                "   FROM VW_LNG" +
                "  WHERE call_success = 'Y'" +
                "    AND crt_dt > date_format(?1, '%Y-%m-%d %H:%i%:%s') \n" +
                "    AND crt_dt <= date_format(now(), '%Y-%m-%d %H:%i%:%s')"
                , nativeQuery = true)
        List<JpaVwLng> findAllByCrtDtBetween(String startDateTime);
}
