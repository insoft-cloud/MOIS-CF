package org.cf.broker.common;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {
	//CloudFoundry API

	public static final String V2 = "/v2" ;
	public static final String V3 = "/v3" ;
	public static final String CATALOG = "/catalog";
	public static final String SERVICE_INSTANCE = V2 + "/service_instances";
	public static final String LAST_OPERATION = "/last_operation";

	public static final String DELETE = "delete";
	public static final String SUCCEEDED = "succeeded";
	public static final String IN_PROGRESS = "in progress";
	public static final String FAILED = "failed";
	public static final String X_BROKER_API_VERSION= "X-Broker-API-Version";
	public static final String AUTHORIZATION = "Authorization";

    public Constants() {
        throw new IllegalStateException();
    }

    public static final long JOB_WAIT_TIME = 900000; // 15분

	/** 서비스 요청시 처리 메소드 매핑 정보 */
	/** <font color="red"><b> 서비스 인스턴스 생성 : egovplatform.up.sc.1001 </b></font>*/
	public static final String SERVICE_REQUEST_1001 = "egovplatform.up.sc.1001";    // 서비스 생성
	/** <font color="red"><b> 서비스 인스턴스 수정 : egovplatform.up.sc.1002 </b></font>*/
	public static final String SERVICE_REQUEST_1002 = "egovplatform.up.sc.1002";    // 서비스 수정
	/** <font color="red"><b> 서비스 인스턴스 삭제 : egovplatform.up.sc.1003 </b></font>*/
	public static final String SERVICE_REQUEST_1003 = "egovplatform.up.sc.1003";    // 서비스 삭제
	/** <font color="red"><b> 서비스 인스턴스 조회 : egovplatform.up.sc.1004 </b></font>*/
	public static final String SERVICE_REQUEST_1004 = "egovplatform.up.sc.1004";    // 서비스 조회
	/** <font color="red"><b> 서비스 인스턴스 목록 조회 : egovplatform.mp.sc.1005 </b></font>*/
	public static final String SERVICE_REQUEST_1005 = "egovplatform.mp.sc.1005";    // 서비스 목록 조회
	/** <font color="red"><b> 서비스 broker 목록 조회 : egovplatform.mp.sc.1006 </b></font>*/
	public static final String SERVICE_REQUEST_1006 = "egovplatform.mp.sc.1006";    // 서비스 broker 목록 조회
	/** <font color="red"><b> 서비스 broker 등록 : egovplatform.mp.sc.1007 </b></font>*/
	public static final String SERVICE_REQUEST_1020 = "egovplatform.mp.sc.1020";    // 서비스 broker 등록
	/** <font color="red"><b> 서비스 broker 이용 정보 조회 : egovplatform.mp.sc.1008 </b></font>*/
	public static final String SERVICE_REQUEST_1021 = "egovplatform.mp.sc.1021";    // 서비스 broker 이용 정보 조회
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1009 </b></font>*/
	public static final String SERVICE_REQUEST_1022 = "egovplatform.mp.sc.1022";    // 플랫폼 별 서비스 이용정보 조회
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1010 </b></font>*/
	public static final String SERVICE_REQUEST_1030 = "egovplatform.mp.sc.1030";    // 서비스 이미지  목록 조회
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1011 </b></font>*/
	public static final String SERVICE_REQUEST_1031 = "egovplatform.mp.sc.1031";    // 서비스 이미지 등록
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1012 </b></font>*/
	public static final String SERVICE_REQUEST_1032 = "egovplatform.mp.sc.1032";    // 서비스 이미지 수정
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1013 </b></font>*/
	public static final String SERVICE_REQUEST_1033 = "egovplatform.mp.sc.1033";    // 서비스 이미지 삭제
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1013 </b></font>*/
	public static final String SERVICE_REQUEST_1040 = "egovplatform.mp.sc.1040";    // 서비스 이미지 삭제
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1013 </b></font>*/
	public static final String SERVICE_REQUEST_1041 = "egovplatform.mp.sc.1041";    // 서비스 이미지 삭제
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1013 </b></font>*/
	public static final String SERVICE_REQUEST_1042 = "egovplatform.mp.sc.1042";    // 서비스 이미지 삭제
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1013 </b></font>*/
	public static final String SERVICE_REQUEST_1043 = "egovplatform.mp.sc.1043";    // 서비스 이미지 삭제
	/** <font color="red"><b> 플랫폼 별 서비스 이용 정보 조회 : egovplatform.mp.sc.1013 </b></font>*/
	public static final String SERVICE_REQUEST_1050 = "egovplatform.mp.sc.1050";    // 서비스 이용 통계정보 조회

	/** <font color="red"><b> 미터링 할당형 : egovplatform.cm.sc.1001 </b></font>*/
	public static final String SERVICE_REQUEST_1051 = "egovplatform.up.sc.1060";
	/** <font color="red"><b> 미터링 호출형 : egovplatform.cm.sc.1002 </b></font>*/
	public static final String SERVICE_REQUEST_1052 = "egovplatform.cm.sc.1061";
	/** <font color="red"><b> 미터링 정보 조회 : egovplatform.mp.sc.1003 </b></font>*/
	public static final String SERVICE_REQUEST_1053 = "egovplatform.mp.sc.1062";

	/** <font color="red"><b> 미터링(호출형) 메트릭 등록 egovplatform.mp.sc.1070 </b></font>*/
	public static final String SERVICE_REQUEST_1054 = "egovplatform.mp.sc.1070";
	/** <font color="red"><b> 미터링(호출형) 메트릭 수정 : egovplatform.mp.sc.1071</b></font>*/
	public static final String SERVICE_REQUEST_1055 = "egovplatform.mp.sc.1071";
	/** <font color="red"><b> 미터링(호출형) 메트릭 삭제 : egovplatform.mp.sc.1072 </b></font>*/
	public static final String SERVICE_REQUEST_1056 = "egovplatform.mp.sc.1072";
	/** <font color="red"><b> 미터링(호출형) 메트릭 조회 : egovplatform.mp.sc.1073 </b></font>*/
	public static final String SERVICE_REQUEST_1057 = "egovplatform.mp.sc.1073";


	/** <font color="red"><b> 플랫폼 별 플랫폼 서비스 목록 정보 조회 : egovplatform.mp.sc.1019 </b></font>*/
	public static final String SERVICE_REQUEST_1019 = "egovplatform.mp.sc.1019";
	/** <font color="red"><b> 서비스 상태조회(생성이나 변경 요청 후 진행 상태 확인 용) : egovplatform.mp.sc.status.check </b></font>*/
	public static final String SERVICE_REQUEST_STATUS_CHECK = "egovplatform.mp.sc.status.check";    // 플랫폼 별 서비스 이용정보 조회

	// TODO : 인터페이스 아이디 부여 전 임시 사용 (서비스 브로커 상세 조회, 서비스 통계), 인터페이스 아이디 나오면 수정
	/** <font color="red"><b> 서비스 broker 등록 : egovplatform.mp.sc.1001 </b></font>*/
	public static final String SERVICE_REQUEST_BROKER_DETAIL = "egovplatform.mp.sc.broker.detail";    // 서비스 broker 상세 조회
	/** <font color="red"><b> 서비스 broker 등록 : egovplatform.mp.sc.service.statistics </b></font>*/
	public static final String SERVICE_REQUEST_SERVICE_STATISTICS = "egovplatform.mp.sc.service.statistics";    // 서비스 통계조회


	public static final String SERVICE_PACKAGE = "org.cf.broker.service"; // 서비스 클래스의 Package
	/*
	 * 서비스 요청에 따라 해당 요청 처리 비지니스 메소드 호출 매핑(ServiceBrokerService 의 메소드 지정)
	 * - 해당 메소드가 ServiceBrokerService 에 있어야함.
	 * package:SpringBean Service Class Name:method name
	 * */
	public static final Map<String, String> SERVICE_FUNCTION_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {
		{
			put(SERVICE_REQUEST_1001, SERVICE_PACKAGE + ":serviceInstanceService:createServiceInstance");     // 서비스 생성
			//put(SERVICE_REQUEST_1002, SERVICE_PACKAGE + ":ServiceInstanceService:createPortalServiceInstance");     // 서비스 수정
			put(SERVICE_REQUEST_1003, SERVICE_PACKAGE + ":serviceInstanceService:deleteServiceInstance");     // 서비스 삭제
			put(SERVICE_REQUEST_1004, SERVICE_PACKAGE + ":serviceInstanceService:getServiceInstance");     // 서비스 조회
			put(SERVICE_REQUEST_1005, SERVICE_PACKAGE + ":serviceInstanceService:listServiceInstance");     // 서비스 목록조회
			put(SERVICE_REQUEST_1006, SERVICE_PACKAGE + ":serviceService:listServices");     // 플랫폼 서비스 목록조회
			put(SERVICE_REQUEST_1020, SERVICE_PACKAGE + ":serviceBrokerService:createServiceBroker");       // 서비스 broker 등록
			put(SERVICE_REQUEST_1021, SERVICE_PACKAGE + ":serviceBrokerService:updateServiceBroker");       // 서비스 broker 수정
			put(SERVICE_REQUEST_1022, SERVICE_PACKAGE + ":serviceBrokerService:deleteServiceBroker");       // 서비스 broker 수정
			put(SERVICE_REQUEST_1030, SERVICE_PACKAGE + ":imageService:getServiceImageList");       // 서비스 broker 삭제
			put(SERVICE_REQUEST_1031, SERVICE_PACKAGE + ":imageService:createServiceImage");       // 이미지등록
			put(SERVICE_REQUEST_1032, SERVICE_PACKAGE + ":imageService:updateServiceImage");       // 이미지 수정
			put(SERVICE_REQUEST_1033, SERVICE_PACKAGE + ":imageService:deleteServiceImage");       // 이미지 삭제
			put(SERVICE_REQUEST_1040, SERVICE_PACKAGE + ":templateService:getServiceTemplateList");       // 서비스 템플릿 목록 조회
			put(SERVICE_REQUEST_1041, SERVICE_PACKAGE + ":templateService:createServiceTemplate");        // 서비스 템플릿 등록
			put(SERVICE_REQUEST_1042, SERVICE_PACKAGE + ":templateService:updateServiceTemplate");        // 서비스 템플릿 수정
			put(SERVICE_REQUEST_1043, SERVICE_PACKAGE + ":templateService:deleteServiceTemplate");        // 서비스 템플릿 삭제
			put(SERVICE_REQUEST_1050, SERVICE_PACKAGE + ":statisticsService:getServiceUseInfo");        // 서비스 템플릿 삭제

//			put(SERVICE_REQUEST_BROKER_DETAIL, SERVICE_PACKAGE + "serviceBrokerService:getServiceBroker");                                               // 서비스 broker 상세 조회
//			put(SERVICE_REQUEST_SERVICE_STATISTICS, SERVICE_PACKAGE + "statisticsService:selectServiceStatistics");                                               // 서비스 통계 조회
//
//			put(SERVICE_REQUEST_STATUS_CHECK, SERVICE_PACKAGE + ":serviceService:getStatusServiceInstance");        // 서비스 상태조회

			//미터링
			put(SERVICE_REQUEST_1051, SERVICE_PACKAGE + ":meteringService:meteringServiceAllocation");        	// 미터링 할당형
			put(SERVICE_REQUEST_1052, SERVICE_PACKAGE + ":meteringService:meteringServiceCall");        		// 미터링 호출형
			put(SERVICE_REQUEST_1053, SERVICE_PACKAGE + ":meteringService:meteringServiceRetreive");        	// 미터링 정보 조회

			//미터링(호출형) 메트릭
			put(SERVICE_REQUEST_1054, SERVICE_PACKAGE + ":meteringService:meteringServiceMetricsCreate");        	// 미터링(호출형) 메트릭 등록
			put(SERVICE_REQUEST_1055, SERVICE_PACKAGE + ":meteringService:meteringServiceMetricsUpdate");        	// 미터링(호출형) 메트릭 수정
			put(SERVICE_REQUEST_1056, SERVICE_PACKAGE + ":meteringService:meteringServiceMetricsDelete");        	// 미터링(호출형) 메트릭 삭제
			put(SERVICE_REQUEST_1057, SERVICE_PACKAGE + ":meteringService:meteringServiceMetricsRetreive");        	// 미터링(호출형) 메트릭 조회

		}
	});

	public static final String[] ERROR_CODS = {"description", "error", "message"};
	public static final String RESULT_CODE_SUCCESS = "0000";
	public static final String RESULT_CODE_PARAMETER_ERROR = "1000";
	public static final String RESULT_CODE_UNKNOW_ERROR = "9000";
	public static final String VERSION_SERVICE_BROKER = "2.*";
	public static final String SERVICE_BROKER_ID_KEY = "id";
	public static final String SERVICE_BROKER_PW_KEY = "pw";
}
