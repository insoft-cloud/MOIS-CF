package org.cf.servicebroker.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class MapUtil {
    public MapUtil() {
    }

    public static Map convertObjectToMap(Object obj) {
        ObjectMapper oMapper = new ObjectMapper();

        oMapper.setDefaultPropertyInclusion(
                JsonInclude.Value.construct(JsonInclude.Include.ALWAYS, JsonInclude.Include.NON_NULL));
        // object -> Map
        Map<String, Object> map = oMapper.convertValue(obj, Map.class);
        return map;

    }


}
