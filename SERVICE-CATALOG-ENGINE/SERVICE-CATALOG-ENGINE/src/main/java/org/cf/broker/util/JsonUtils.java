package org.cf.broker.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cf.broker.exception.ServiceBrokerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public JsonUtils() {
    }

    public static JsonNode convertToJson(ResponseEntity<String> httpResponse) throws ServiceBrokerException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;

        try {
            json = (JsonNode) mapper.readValue((String) httpResponse.getBody(), JsonNode.class);
        } catch (JsonParseException var4) {
            throw new ServiceBrokerException(getSwitchedMessage(1));
        } catch (JsonMappingException var5) {
            throw new ServiceBrokerException(getSwitchedMessage(2));
        } catch (IOException var6) {
            throw new ServiceBrokerException(getSwitchedMessage(3));
        } catch (Exception var7) {
            logger.warn("Json Convert: Exception");
            throw new ServiceBrokerException("Json Convert: " + var7.getMessage());
        }

        logger.info("Json Convert: Complete");
        return json;
    }

    private static String getSwitchedMessage(int status) {
        String switchedMessage = null;
        switch (status) {
            case 1:
                switchedMessage = "Exception: JsonParseException";
            case 2:
                switchedMessage = "Exception: JsonMappingException";
            case 3:
                switchedMessage = "Exception: IOException";
            case 4:
                switchedMessage = "Exception: JsonProcessingException";
            default:
                return switchedMessage;
        }
    }

    public static String jsonStrFromObject(Object object) throws ServiceBrokerException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ServiceBrokerException(getSwitchedMessage(4));
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJsonStr(String jsonStr) throws ServiceBrokerException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (JsonParseException var4) {
            throw new ServiceBrokerException(getSwitchedMessage(1));
        } catch (JsonMappingException var5) {
            throw new ServiceBrokerException(getSwitchedMessage(2));
        } catch (IOException var6) {
            throw new ServiceBrokerException(getSwitchedMessage(3));
        } catch (Exception var7) {
            throw new ServiceBrokerException("Json Convert: " + var7.getMessage());
        }
    }

}

