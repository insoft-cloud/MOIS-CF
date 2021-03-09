package org.cf.broker.service;

import org.cf.broker.common.annotation.MessageMapper;
import org.cf.broker.model.StatisticsUseService;
import org.cf.broker.model.jpa.JpaStatisticsService;
import org.cf.broker.repo.JpaStatisticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    private final JpaStatisticsRepository jpaStatisticsRepository;

    public StatisticsService(JpaStatisticsRepository jpaStatisticsRepository) {
        this.jpaStatisticsRepository = jpaStatisticsRepository;
    }

    public Object getServiceUseInfo(
            @MessageMapper (field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
            @MessageMapper(field = "search_type") String search_type,
            @MessageMapper(field = "start_date") String start_date,
            @MessageMapper(field = "end_date") String end_date) {

        int sumCreateCnt = 0;
        List<JpaStatisticsService> Statisticslist = null;

        if("m".equalsIgnoreCase(search_type)) {
            Statisticslist = jpaStatisticsRepository.selectServiceStatisticsForMonth(start_date, end_date);

        } else {
            Statisticslist = jpaStatisticsRepository.selectServiceStatistics(start_date, end_date);
        }

        return new ResponseEntity(Statisticslist, HttpStatus.OK);
    }

    public List getServiceUseInfo2(String search_type, String start_date, String end_date) {

        int sumCreateCnt = 0;
        List<JpaStatisticsService> Statisticslist = null;

        if("m".equalsIgnoreCase(search_type)) {
            Statisticslist = jpaStatisticsRepository.selectServiceStatisticsForMonth(start_date, end_date);

        } else {
            Statisticslist = jpaStatisticsRepository.selectServiceStatistics(start_date, end_date);
        }

        return Statisticslist;
    }

    /**
     * 서비스 이용 조회(기간 중 일별 생성/수정/삭제 횟수)
     *
     * @param searchParam
     * @return
     */
    /*public List<Map> selectServiceStatistics(StatisticsUseService searchParam) {
        return jpaStatisticsRepository.selectServiceStatistics(searchParam.getStartDate(), searchParam.getEndDate());
    }*/
}
