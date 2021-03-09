package org.openpaas.paasta.portal.api.rabbitmq.intrface;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.openpaas.paasta.portal.api.rabbitmq.model.ErrorMessage;
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
		} else if( resultObject instanceof ErrorMessage){
			ErrorMessage errorMessage = (ErrorMessage) resultObject;
			this.result_code = String.valueOf(errorMessage.getCode());
			this.msg = errorMessage.getMessage();
		}
	}
}
