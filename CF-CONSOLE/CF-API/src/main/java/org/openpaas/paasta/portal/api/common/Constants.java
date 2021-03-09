package org.openpaas.paasta.portal.api.common;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {

    /**
     * 생성자
     */
    public Constants(){};

    /**
     * 사용유무 사용
     */
    public static final String USE_YN_Y = "Y";
    /**
     * 결과 코드 성공
     */
    public static final String RESULT_STATUS_SUCCESS = "SUCCESS";
    /**
     * 결과 코드 실패
     */
    public static final String RESULT_STATUS_FAIL = "FAIL";


    public static final String DUPLICATION_SEPARATOR = "::";
    /**
     * 카탈로그 EGOV 빌드팩 문자열
     */
    public static final String CATALOG_EGOV_BUILD_PACK_CHECK_STRING = "egov";
    /**
     * 카탈로그 EGOV 빌드팩 환경 키
     */
    public static final String CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_KEY = "JBP_CONFIG_COMPONENTS";

    /**
     * 카탈로그 EGOV 빌드팩 환경 값
     */
    public static final String CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_VALUE = "[containers: Tomcat]";

    
    /**
     * API REST URL prefix
     */
    public static final String V2_URL = "/v2";
    public static final String V3_URL = "/v3";

    public static final String EXTERNAL_URL = "/ext/app";

    public static final String SERVICE_REQUEST_1001 = "egovplatform.up.cf.1001";    // 유저 생성
    public static final String SERVICE_REQUEST_1002 = "egovplatform.mp.cf.1002";    // 조직생성
    public static final String SERVICE_REQUEST_1004 = "egovplatform.mp.cf.1004";    // 조직 수정
    public static final String SERVICE_REQUEST_1005 = "egovplatform.up.cf.1005";    // 쿼터 조회
    public static final String SERVICE_REQUEST_1011 = "egovplatform.up.cf.1011";    // 어플리케이션 실행
    public static final String SERVICE_REQUEST_1012 = "egovplatform.mp.cf.1012";    // 유저 조직 탈퇴
    public static final String SERVICE_REQUEST_1013 = "egovplatform.mp.cf.1013";    // 유저 삭제
    public static final String SERVICE_PACKAGE = "org.openpaas.paasta.portal.api.rabbitmq.service"; // 서비스 클래스의 Package

    public static final Map<String, String> SERVICE_FUNCTION_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put(SERVICE_REQUEST_1001, SERVICE_PACKAGE + ":userService:createUser");     // 서비스 생성
            put(SERVICE_REQUEST_1002, SERVICE_PACKAGE + ":projectService:createProject");     // 서비스 수정
            put(SERVICE_REQUEST_1004, SERVICE_PACKAGE + ":projectService:updateProject");     // 서비스 조회
            put(SERVICE_REQUEST_1005, SERVICE_PACKAGE + ":projectService:getProjectQuota");     // 서비스 목록조회
            put(SERVICE_REQUEST_1011, SERVICE_PACKAGE + ":appService:createCloudfoundryApplication");     // 서비스 목록조회
            put(SERVICE_REQUEST_1012, SERVICE_PACKAGE + ":userService:deleteUser");     // 서비스 목록조회
            put(SERVICE_REQUEST_1013, SERVICE_PACKAGE + ":userService:removeUser");     // 서비스 목록조회
        }
    });

    public static final String RESULT_CODE_UNKNOW_ERROR = "9000";

    public static final String ORG = "org_id";
    public static final String SPACE = "space_id";
    public static final String USER_ID = "user_id";
    public static final String USER_PWD = "user_password";





}
