package org.cf.servicebroker.interceptor;


import org.cf.servicebroker.exception.ServiceBrokerApiVersionException;
import org.cf.servicebroker.model.BrokerApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BrokerApiVersionInterceptor extends HandlerInterceptorAdapter {
    private final BrokerApiVersion version;
    private static final Logger LOGGER = LoggerFactory.getLogger(BrokerApiVersionInterceptor.class);

    public BrokerApiVersionInterceptor() {
        this((BrokerApiVersion)null);
    }

    public BrokerApiVersionInterceptor(BrokerApiVersion version) {
        this.version = version;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServiceBrokerApiVersionException {
        if (this.version != null && !this.anyVersionAllowed()) {
            String apiVersion = request.getHeader(this.version.getBrokerApiVersionHeader());
            boolean contains = false;
            String[] var6 = this.version.getApiVersions().split(", ");
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String brokerApiVersion = var6[var8];
                if (brokerApiVersion.contains(".") && apiVersion.contains(".") && "x".equals(brokerApiVersion.split("[.]")[1]) && apiVersion.split("[.]")[0].equals(brokerApiVersion.split("[.]")[0])) {
                    contains = true;
                    break;
                }

                if (brokerApiVersion.equals(apiVersion)) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                throw new ServiceBrokerApiVersionException(this.version.getApiVersions(), apiVersion);
            }
        }

        return true;
    }

    private boolean anyVersionAllowed() {
        return "*".equals(this.version.getApiVersions());
    }
}
