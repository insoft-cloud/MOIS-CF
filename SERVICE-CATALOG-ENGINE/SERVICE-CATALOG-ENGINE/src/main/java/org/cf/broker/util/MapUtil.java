package org.cf.broker.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class MapUtil {
    public MapUtil() {
    }

    public static Map convertObjectToMap(Object obj) {
        ObjectMapper oMapper = new ObjectMapper();

        // object -> Map
        Map<String, Object> map = oMapper.convertValue(obj, Map.class);
        return map;

    }
}
