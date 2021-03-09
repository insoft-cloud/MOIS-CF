package org.cf.broker.repo;

import org.cf.broker.model.StatisticsUseService;
import org.cf.broker.model.jpa.JpaServiceUseInfo;
import org.cf.broker.model.jpa.JpaStatisticsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface JpaStatisticsRepository extends JpaRepository<JpaStatisticsService, String> {
//    @Query(value = "SELECT new org.cf.broker.model.StatisticsUseService(b.searchYear, b.createCnt)\n" +
    @Query(value = "SELECT b.searchYear as search_year, b.createCnt as create_cnt\n" +
            "FROM (\n" +
              "SELECT DATE_FORMAT(begin_de, '%Y%m%d') AS searchYear\n" +
//            "       , DATE_FORMAT(begin_de, '%m') AS searchMonth\n" +
//            "       , DATE_FORMAT(begin_de, '%d') AS searchDay\n" +
            "       , SUM(CASE WHEN (SELECT count(id)\n" +
            "                          FROM service_use_info ssui\n" +
            "                         WHERE ssui.svc_instn_id = a.svc_instn_id\n" +
            "                           AND a.svc_instn_cnt = 1) = 1 OR (SELECT id\n" +
            "                                                              FROM service_use_info ssui\n" +
            "                                                             WHERE ssui.svc_instn_id = a.svc_instn_id\n" +
            "                                                               AND a.svc_instn_cnt > 1\n" +
            "                                                               AND ssui.id = a.min_instn_id) = a.id\n" +
            "                  THEN 1 \n" +
            "                  ELSE 0 END) AS createCnt\n" +
/*            "       , SUM(CASE WHEN (SELECT count(id)\n" +
            "                          FROM service_use_info ssui\n" +
            "                         WHERE ssui.svc_instn_id = a.svc_instn_id\n" +
            "                           AND ssui.end_de IS null) > 0 AND a.svc_instn_cnt > 1 AND a.id != a.min_instn_id\n" +
            "                  THEN 1 \n" +
            "                  ELSE 0 END) AS update_cnt\n" +
            "       , SUM(CASE WHEN (SELECT count(id)\n" +
            "                          FROM service_use_info ssui\n" +
            "                         WHERE ssui.svc_instn_id = a.svc_instn_id\n" +
            "                           AND ssui.end_de IS null) = 0 \n" +
            "                  THEN 1 \n" +
            "                  ELSE 0 END) AS delete_cnt\n" +*/
            "  FROM (\n" +
            "        SELECT sui.id\n" +
            "               , sui.begin_de\n" +
            "               , sui.end_de\n" +
/*            "               , sp.plan_nm\n" +
            "               , si.instn_nm\n" +*/
            "               , sui.svc_instn_id\n" +
            "               , gsui.svc_instn_cnt\n" +
            "               , gsui.min_instn_id\n" +
            "          FROM service_use_info sui\n" +
/*            "         INNER JOIN service_plan sp\n" +
            "                 ON sp.id = sui.svc_plan_id\n" +
            "         INNER JOIN service_instn si\n" +
            "                 ON si.id = sui.svc_instn_id\n" +*/
            "         INNER JOIN (SELECT svc_instn_id, count(id) AS svc_instn_cnt, min(id) AS min_instn_id\n" +
            "                       FROM service_use_info\n" +
            "                      GROUP BY svc_instn_id) AS gsui\n" +
            "                 ON sui.svc_instn_id = gsui.svc_instn_id\n" +
            "         WHERE EXISTS (SELECT '1'\n" +
            "                         FROM service_use_info ssui\n" +
            "                        WHERE ssui.begin_de BETWEEN DATE_FORMAT(CONCAT(:start, '000000'), '%Y%m%d%H%i%S') AND DATE_FORMAT(CONCAT(:end, '235959'), '%Y%m%d%H%i%S')\n" +
            "                          AND sui.svc_instn_id = ssui.svc_instn_id)\n" +
            "  ) a\n" +
            " WHERE begin_de BETWEEN DATE_FORMAT(CONCAT(:start, '000000'), '%Y%m%d%H%i%S') AND DATE_FORMAT(CONCAT(:end, '235959'), '%Y%m%d%H%i%S')\n" +
            " GROUP BY DATE_FORMAT(begin_de, '%Y%m%d')\n" +
            " ORDER BY begin_de\n" +
            " ) b", nativeQuery = true)
    List<JpaStatisticsService> selectServiceStatistics(@Param("start") String startDate, @Param("end") String endDate);

    @Query(value = "SELECT searchYear  as search_year, SUM(createCnt) as create_cnt\n" +
            "FROM (\n" +
            "SELECT DATE_FORMAT(begin_de, '%Y%m') AS searchYear\n" +
            "       , DATE_FORMAT(begin_de, '%m') AS searchMonth\n" +
            "       , DATE_FORMAT(begin_de, '%d') AS searchDay\n" +
            "       , SUM(CASE WHEN (SELECT count(id)\n" +
            "                          FROM service_use_info ssui\n" +
            "                         WHERE ssui.svc_instn_id = a.svc_instn_id\n" +
            "                           AND a.svc_instn_cnt = 1) = 1 OR (SELECT id\n" +
            "                                                              FROM service_use_info ssui\n" +
            "                                                             WHERE ssui.svc_instn_id = a.svc_instn_id\n" +
            "                                                               AND a.svc_instn_cnt > 1\n" +
            "                                                               AND ssui.id = a.min_instn_id) = a.id\n" +
            "                  THEN 1 \n" +
            "                  ELSE 0 END) AS createCnt\n" +
            "  FROM (\n" +
            "        SELECT sui.id\n" +
            "               , sui.begin_de\n" +
            "               , sui.end_de\n" +
            "               , sui.svc_instn_id\n" +
            "               , gsui.svc_instn_cnt\n" +
            "               , gsui.min_instn_id\n" +
            "          FROM service_use_info sui\n" +
            "         INNER JOIN (SELECT svc_instn_id, count(id) AS svc_instn_cnt, min(id) AS min_instn_id\n" +
            "                       FROM service_use_info\n" +
            "                      GROUP BY svc_instn_id) AS gsui\n" +
            "                 ON sui.svc_instn_id = gsui.svc_instn_id\n" +
            "         WHERE EXISTS (SELECT '1'\n" +
            "                         FROM service_use_info ssui\n" +
            "                        WHERE ssui.begin_de BETWEEN DATE_FORMAT(CONCAT(:start, '000000'), '%Y%m%d%H%i%S') AND DATE_FORMAT(CONCAT(:end, '235959'), '%Y%m%d%H%i%S')\n" +
            "                          AND sui.svc_instn_id = ssui.svc_instn_id)\n" +
            "  ) a\n" +
            " WHERE begin_de BETWEEN DATE_FORMAT(CONCAT(:start, '000000'), '%Y%m%d%H%i%S') AND DATE_FORMAT(CONCAT(:end, '235959'), '%Y%m%d%H%i%S')\n" +
            " GROUP BY DATE_FORMAT(begin_de, '%Y%m%d')\n" +
            " ) b\n" +
            " GROUP BY searchYear" +
            " ORDER BY searchYear", nativeQuery = true)
    List<JpaStatisticsService> selectServiceStatisticsForMonth(@Param("start") String startDate, @Param("end") String endDate);


}
