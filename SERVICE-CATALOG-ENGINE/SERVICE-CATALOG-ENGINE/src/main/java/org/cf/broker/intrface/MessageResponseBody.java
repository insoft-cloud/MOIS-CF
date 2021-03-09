package org.cf.broker.intrface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.cf.broker.common.Constants;
import org.cf.broker.model.common.ErrorMessage;
import org.cf.broker.util.MapUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

/**
 * 인터페이스 Response Body
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponseBody {

	private String result_code; // 결과 코드
	private String msg;         // 결과 메세지
	@JsonIgnore
	private String interfaceId; // 인터페이스 아이디(전문 번호)

	// 인터페이스 아이디 별 응답 전문,
	private Object response;

	public MessageResponseBody() {
		//this.result_code = Constants.RESULT_CODE_SUCCESS; // 정상 코드
		this.msg = "";
	}

	public MessageResponseBody(String interfaceId) {
		this();
		this.interfaceId = interfaceId;
	}

	public MessageResponseBody(String result_code, String msg) {
		this.result_code = result_code;
		this.msg = msg;
	}

	public MessageResponseBody(String interfaceId, Object resultObject) {
		this(interfaceId);
		if(resultObject instanceof ResponseEntity){
			ResponseEntity responseEntity = (ResponseEntity)resultObject;
			this.result_code = String.valueOf(responseEntity.getStatusCodeValue());
			response = responseEntity.getBody();
			switch (interfaceId){
				case Constants.SERVICE_REQUEST_1001:
				case Constants.SERVICE_REQUEST_1002:
				case Constants.SERVICE_REQUEST_1003:
				case Constants.SERVICE_REQUEST_1020:
				case Constants.SERVICE_REQUEST_1021:
				case Constants.SERVICE_REQUEST_1022:
				case Constants.SERVICE_REQUEST_1031:
				case Constants.SERVICE_REQUEST_1032:
				case Constants.SERVICE_REQUEST_1033: response = null; break;
				default: break;
			}
		} else if( resultObject instanceof ErrorMessage){
			ErrorMessage errorMessage = (ErrorMessage) resultObject;
			this.result_code = String.valueOf(errorMessage.getCode());
			this.msg = errorMessage.getMessage();
		}
	}
}
