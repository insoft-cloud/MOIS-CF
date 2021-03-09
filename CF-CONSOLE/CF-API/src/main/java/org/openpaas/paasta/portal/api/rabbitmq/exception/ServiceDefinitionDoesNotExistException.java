package org.openpaas.paasta.portal.api.rabbitmq.exception;

public class ServiceDefinitionDoesNotExistException extends Exception {
    private static final long serialVersionUID = -62090827040416788L;

    public ServiceDefinitionDoesNotExistException(String serviceDefinitionId) {
        super("ServiceDefinition does not exist: id = " + serviceDefinitionId);
    }
}
