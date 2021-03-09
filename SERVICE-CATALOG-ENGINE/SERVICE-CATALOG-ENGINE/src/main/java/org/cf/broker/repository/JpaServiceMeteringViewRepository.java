package org.cf.broker.repository;

import org.cf.broker.model2.JpaServiceMeteringView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceMeteringViewRepository extends JpaRepository<JpaServiceMeteringView, Integer> {

    @Query(value = "" +
            " SELECT svc_nm, dc, creat_dt, creat_user_id, upd_dt, upd_user_id, prjct_id, instn_id, svc_stt, svc_id, end_dt" +
            " FROM   TOPS_SERVICE_VIEW" +
            " WHERE  svc_stt IN ('SS01','SS04')" +
            " AND    from_unixtime(upd_dt) between date_format(date_add(now(), interval-10 minute), '%Y-%m-%d %H:%i%:%s') and date_format(now(), '%Y-%m-%d %H:%i%:%s')"
            , nativeQuery = true)
    List<JpaServiceMeteringView> findAllByUpdDtBetween();

    List<JpaServiceMeteringView> findBySvcSttIn(@Param("svc_sttus") List svc_sttus);
}

