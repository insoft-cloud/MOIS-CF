package org.cf.broker.util;

import com.fasterxml.jackson.databind.ObjectMapper;


public class StringUtil {

    public StringUtil() {

    }


    public static String convertObjectToString(Object obj) {
        ObjectMapper oMapper = new ObjectMapper();

        // object -> String
        String string = oMapper.convertValue(obj, String.class);
        return string;
    }
}
