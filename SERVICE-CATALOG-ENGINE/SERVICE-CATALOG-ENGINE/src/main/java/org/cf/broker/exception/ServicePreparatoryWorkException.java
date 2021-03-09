package org.cf.broker.exception;

public class ServicePreparatoryWorkException extends BaseBizException {
	private static final long serialVersionUID = -1288712633779609678L;

	public ServicePreparatoryWorkException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
    }
	
	public ServicePreparatoryWorkException(String errorMessage, String errorCode, Throwable cause) {
		super(errorMessage, errorCode, cause);
	}
}
