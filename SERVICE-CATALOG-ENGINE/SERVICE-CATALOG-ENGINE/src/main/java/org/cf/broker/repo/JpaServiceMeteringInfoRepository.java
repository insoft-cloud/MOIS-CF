package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceMeteringInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceMeteringInfoRepository extends JpaRepository<JpaServiceMeteringInfo, Integer> {

    @Query(value = "" +
            " SELECT  a.id      		 											    AS id									                                                                                "+
            "        ,a.svc_id  		 												AS svc_id								                                                                                "+
            "		,a.svc_instn_id													AS svc_instn_id							                                                                                "+
            "		,a.svc_plan_id													AS svc_plan_id								                                                                            "+
            "		,DATE_FORMAT(a.begin_de, '%Y-%m-%d %H:%i:%s')					AS begin_date 												                                                            "+
            "		,DATE_FORMAT(a.end_de  , '%Y-%m-%d %H:%i:%s')					AS end_date												                                                                "+
            "		,''																AS unit_ty						                                                                                        "+
            "		,'' 															AS unit_value							                                                                                "+
            "		,(CASE WHEN a.begin_de IS NOT NULL AND a.end_de IS NOT NULL																                                                                "+
            "		     THEN BIG_SEC_TO_TIME(TIMESTAMPDIFF(SECOND, 																	                                                                    "+
            "				(SELECT (CASE WHEN REPLACE(:use_month,',', '') = DATE_FORMAT(a.begin_de, '%Y%m')											                                                    "+
            "							 THEN  DATE_FORMAT(a.begin_de, '%Y-%m-%d %H:%i:%s')												                                                                    "+
            "							 ELSE  CONCAT(DATE_FORMAT(CONCAT(REPLACE(:use_month,',', ''),'','01'),'%Y-%m-%d'),' ','00:00:00')						                                            "+
            "					 END)), (SELECT (CASE WHEN REPLACE(:use_month,',', '') < DATE_FORMAT(a.end_de , '%Y%m')                                                                                     "+
            "                                     THEN DATE_ADD(CONCAT(LAST_DAY(DATE_FORMAT(CONCAT(REPLACE(:use_month,',', ''),'','01'),'%Y-%m-%d')),' ','23:59:59'), INTERVAL 9 HOUR)                       "+
            "                                     WHEN REPLACE(:use_month,',', '') = DATE_FORMAT(a.end_de , '%Y%m') AND REPLACE(:use_month,',', '') = DATE_FORMAT(a.begin_de , '%Y%m')                       "+
            "                                     THEN DATE_FORMAT(a.end_de, '%Y-%m-%d %H:%i:%s')                                                                                                            "+
            "                                     WHEN REPLACE(:use_month,',', '') = DATE_FORMAT(a.end_de , '%Y%m')                                                                                          "+
            "                                     THEN DATE_ADD(DATE_FORMAT(a.end_de, '%Y-%m-%d %H:%i:%s'), INTERVAL 9 HOUR)                                                                                 "+
            "                                     ELSE DATE_FORMAT(a.end_de, '%Y-%m-%d %H:%i:%s')                                                                                                            "+
            "                                     END))))													                                                                                                "+
            "		       WHEN DATE_FORMAT(a.begin_de, '%Y%m') = DATE_FORMAT(NOW(), '%Y%m') AND a.end_de IS NULL												                                            "+
            "			   THEN BIG_SEC_TO_TIME(TIMESTAMPDIFF(SECOND, DATE_FORMAT(a.begin_de, '%Y-%m-%d %H:%i:%s'), NOW()))										                                            "+
            "		       WHEN DATE_FORMAT(a.begin_de, '%Y%m') < DATE_FORMAT(NOW(), '%Y%m')														                                                        "+
            "				   THEN																					                                                                                        "+
            "				   BIG_SEC_TO_TIME(																			                                                                                    "+
            "				   TIMESTAMPDIFF(SECOND,																		                                                                                "+
            "				   (SELECT  (CASE WHEN REPLACE(:use_month,',', '') = DATE_FORMAT(a.begin_de, '%Y%m')											                                                "+
            "							 THEN  DATE_ADD(DATE_FORMAT(a.begin_de, '%Y-%m-%d %H:%i:%s'), INTERVAL 9 HOUR)												                                        "+
            "							 ELSE  CONCAT(DATE_FORMAT(CONCAT(REPLACE(:use_month,',', ''),'','01'),'%Y-%m-%d'),' ','00:00:00')						                                            "+
            "				END))																					                                                                                        "+
            "				   ,(SELECT (CASE WHEN REPLACE(:use_month,',', '') < DATE_FORMAT(NOW(), '%Y%m')												                                                    "+
            "							 THEN  CONCAT(LAST_DAY(DATE_FORMAT(CONCAT(REPLACE(:use_month,',', ''),'','01'),'%Y-%m-%d')),' ','23:59:59')					                                        "+
            "							 ELSE  DATE_ADD(NOW(), INTERVAL 9 HOUR)																	                                                            "+
            "						END))																			                                                                                        "+
            "				   ))																					                                                                                        "+
            "		       ELSE ''																						                                                                                    "+
            "	     END) 					    AS use_time     																                                                                            "+
            "        ,a.prjct_id                 AS prjct_id																				                                                                    "+
            "        ,a.creat_id                 AS creat_id																				                                                                    "+
            "        ,a.mesur_ty    			    AS mesur_ty																		                                                                            "+
            " FROM   metering_info a																							                                                                                "+
            " WHERE  a.mesur_ty = 'MT1'																						                                                                                "+
            " AND    1 = (CASE WHEN REPLACE(:use_month,',', '') <= DATE_FORMAT(NOW(), '%Y%m')															                                                    "+
            " 		  		  THEN '1'																				                                                                                        "+
            " 	         ELSE '2' END)																						                                                                                "+
            " AND    a.svc_instn_id = (CASE WHEN LENGTH(:svc_instn_id) = 0 THEN a.svc_instn_id ELSE :svc_instn_id END)											                                            "+
            " AND    a.prjct_id = :project_id																					                                                                            "+
            " AND   (DATE_FORMAT(a.begin_de, '%Y%m') = :use_month OR (DATE_FORMAT(a.begin_de, '%Y%m') < :use_month AND a.end_de IS NULL) OR DATE_FORMAT(a.end_de , '%Y%m') = :use_month)				        "+
            " UNION  ALL																								                                                                                        "+
            " SELECT   b.id      		 		AS id																			                                                                            "+
            "         ,b.svc_id  				AS svc_id																		                                                                            "+
            "		 ,b.svc_instn_id			AS svc_instn_id																	                                                                            "+
            "		 ,b.svc_plan_id			    AS svc_plan_id																		                                                                        "+
            "		 ,DATE_FORMAT(b.begin_de, '%Y-%m-%d %H:%i:%s')					AS begin_date																	                                        "+
            "		 ,DATE_FORMAT(b.end_de  , '%Y-%m-%d %H:%i:%s')					AS end_date																                                                "+
            "		 ,b.unit_ty				    AS unit_ty																	                                                                                "+
            "		 ,b.unit_value			    AS unit_value																		                                                                        "+
            "		 ,'0' 					    AS use_time     																                                                                            "+
            "         ,b.prjct_id                AS prjct_id																				                                                                    "+
            "         ,b.creat_id                AS creat_id																				                                                                    "+
            "         ,b.mesur_ty    			AS mesur_ty																		                                                                            "+
            " FROM   metering_info b																							                                                                                "+
            " WHERE  b.mesur_ty = 'MT2'																					                                                                                    "+
            " AND    1 = (CASE WHEN REPLACE(:use_month,',', '') <= DATE_FORMAT(NOW(), '%Y%m')															                                                    "+
            " 		  		  THEN '1'																				                                                                                        "+
            " 	         ELSE '2' END)																						                                                                                "+
            " AND    b.svc_instn_id = (CASE WHEN LENGTH(:svc_instn_id) = 0 THEN b.svc_instn_id ELSE :svc_instn_id END)											                                            "+
            " AND    b.prjct_id = :project_id																					                                                                            "+
            " AND    DATE_FORMAT(b.begin_de, '%Y%m') = :use_month				                                                                                                                            "
            , nativeQuery = true)
    List<JpaServiceMeteringInfo> selectJpaServiceMetering(@Param("project_id") String project_id, @Param("svc_instn_id") String svc_instn_id, @Param("use_month") String use_month);
}