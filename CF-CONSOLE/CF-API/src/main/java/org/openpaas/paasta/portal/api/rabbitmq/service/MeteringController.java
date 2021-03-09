package org.openpaas.paasta.portal.api.rabbitmq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MeteringController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeteringController.class);

    final AppService appService;

    public MeteringController(AppService appService) {
        this.appService = appService;
    }


    @PostMapping("/v3/mois/metering")
    public Map metering(@RequestBody Map map) {
        return appService.moisMetering(map);
    }


}
