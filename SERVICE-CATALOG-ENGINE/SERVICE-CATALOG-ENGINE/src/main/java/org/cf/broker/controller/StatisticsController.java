package org.cf.broker.controller;

import org.cf.broker.model.StatisticsUseService;
import org.cf.broker.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }


    /**
     * 서비스 이용 조회(기간 중 일별 생성/수정/삭제 횟수)
     *
     * @return
     */
    @GetMapping("/service")
    public List getServiceBroker() {

        return statisticsService.getServiceUseInfo2("m","20200721", "20200725");
    }

}
