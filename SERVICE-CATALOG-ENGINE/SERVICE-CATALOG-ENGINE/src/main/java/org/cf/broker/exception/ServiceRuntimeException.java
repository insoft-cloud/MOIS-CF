package org.cf.broker.exception;

public class ServiceRuntimeException extends BaseBizException {
	private static final long serialVersionUID = -1288712633779609678L;

	public ServiceRuntimeException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
	}
}
