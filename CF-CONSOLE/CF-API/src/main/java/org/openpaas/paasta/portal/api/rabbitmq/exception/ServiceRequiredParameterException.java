package org.openpaas.paasta.portal.api.rabbitmq.exception;



public class ServiceRequiredParameterException extends BaseBizException {
	private static final long serialVersionUID = -1288712633779609678L;

	public ServiceRequiredParameterException(String parameterName) {
//		super(parameterName + " is a required value.", Constants.RESULT_CODE_PARAMETER_ERROR);
		super(parameterName + " is a required value", "9000");
    }
}
