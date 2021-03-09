package org.openpaas.paasta.portal.api.rabbitmq.exception;
public class BaseBizException extends RuntimeException {

	private static final long serialVersionUID = 1032826776466587212L;
	
	private String errorMessage;
    private String errorCode;

    public BaseBizException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public BaseBizException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    };
    
    public BaseBizException(String errorMessage, String errorCode, Throwable cause) {
    	super(errorMessage, cause);
    	this.errorMessage = errorMessage;
    	this.errorCode = errorCode;
    };

    public BaseBizException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
    }

    public BaseBizException(Throwable cause) {
        super(cause);
    }

    public String getErrorCode(){
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
