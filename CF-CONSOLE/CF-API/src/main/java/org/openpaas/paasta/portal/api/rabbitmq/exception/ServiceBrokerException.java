package org.openpaas.paasta.portal.api.rabbitmq.exception;

public class ServiceBrokerException extends Exception {
    private static final long serialVersionUID = 1L;
    private String detailMessage;
    private int ERR_CODE;

    public ServiceBrokerException(String message) {
        super(message);
        detailMessage = message;
    }

    public ServiceBrokerException(String message, int err_code) {
        super(message);
        ERR_CODE = err_code;
    };

    public ServiceBrokerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceBrokerException(Throwable cause) {
        super(cause);
    }

    public int getErrCode(){
        return ERR_CODE;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
