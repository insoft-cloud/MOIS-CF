package org.openpaas.paasta.portal.common.api.scheduling;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.jinq.orm.stream.JinqStream;
import org.openpaas.paasta.portal.common.api.config.JinqSource;
import org.openpaas.paasta.portal.common.api.domain.common.CommonService;
import org.openpaas.paasta.portal.common.api.entity.cc.EventCc;
import org.openpaas.paasta.portal.common.api.repository.cc.EventCcRepository;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MonitoringScheduler {

    private final Logger logger = getLogger(this.getClass());

    private final String delete_request = "audit.app.process.delete";

    private final String create_request = "audit.app.create";

    public final JinqSource jinqSource;

    public final String java = "java_offline_buildpack";
    public final String python = "python_offline_buildpack";
    public final String nodejs = "nodejs_offline_buildpack";
    public final String php = "php_offline_buildpack";
    public final String ruby = "ruby_offline_buildpack";
    public final ObjectMapper objectMapper = new ObjectMapper();

    final EventCcRepository eventCcRepository;
    final CommonService commonService;

    public MonitoringScheduler(JinqSource jinqSource, EventCcRepository eventCcRepository, CommonService commonService) {
        this.jinqSource = jinqSource;
        this.eventCcRepository = eventCcRepository;
        this.commonService = commonService;
    }

    @Scheduled(fixedDelay = 30000)
    public void monitoring() {
        try {
            Date date = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant());
            Date date2 = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).minusSeconds(30).toInstant());
            //logger.info("Date : " + date + "...... Date 2" + date2);
            JinqStream<EventCc> streams = jinqSource.streamAllCc(EventCc.class);
            List<EventCc> list = streams.where(c -> date.after(c.getCreatedAt()) && date2.before(c.getCreatedAt())).toList();
            for(int i = 0 ; i < list.size(); i++){
                if (list.get(i).getType().equals(create_request)) {
                    try {
                        Map map = objectMapper.readValue(list.get(i).getMetadata(), Map.class);
                        Map map2 = (Map)map.get("request");
                        metering(map2.get("buildpack").toString(),list.get(i).getOrganization_guid(),list.get(i).getActee(),"c",list.get(i).getActor_name());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (list.get(i).getType().equals(delete_request)) {
                    try {
                        EventCc cc = eventCcRepository.findFirstByActeeAndType(list.get(i).getActee(),create_request);
                        Map map = objectMapper.readValue(cc.getMetadata(), Map.class);
                        Map map2 = (Map)map.get("request");
                        metering(map2.get("buildpack").toString(),list.get(i).getOrganization_guid(),list.get(i).getActee(),"d",list.get(i).getActor_name());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private void metering(String buildpack, String org_guid, String instance_id, String curd, String user){
        StringBuffer stringBuffer = new StringBuffer();
        switch (buildpack){
            case java: stringBuffer.append("cf_devlang_java"); break;
            case php: stringBuffer.append("cf_devlang_php"); break;
            case python: stringBuffer.append("cf_devlang_python"); break;
            case nodejs: stringBuffer.append("cf_devlang_node"); break;
            case ruby: stringBuffer.append("cf_devlang_ruby"); break;
        }
        Map map = new HashMap();
        map.put("plan",stringBuffer.toString());
        map.put("org_guid", org_guid);
        map.put("instance_id", instance_id);
        map.put("curd",curd);
        map.put("user", user);
        commonService.procCfApiRestTemplate2("/mois/metering", HttpMethod.POST,map,null);
    }
}
