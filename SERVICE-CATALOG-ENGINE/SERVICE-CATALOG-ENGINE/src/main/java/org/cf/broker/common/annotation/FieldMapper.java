package org.cf.broker.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 통합 포털에서 큐를 통해 넘어오는 인터 페이스 전문과 서비스 브로커에서 사용되는 필드 매핑
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMapper {
	/**
	 * MessageBody 기준으로 "경로 + "."+ 변수명" 으로 작성
	 * -. Map 일 경우 : 전문의 변수 명:파라미터로 받을 entity 의 변수명
	 * ex) egovplatform.up.sc.1001 의 password
	 * parameter.password:password
	 * 
	 * @return
	 */
	String fieldName() default "";	// 통합 포털 인터페이스 정의서의 필드 영문명
	boolean isMap() default false;
	boolean isLeaf() default true;
	String fieldDesc() default ""; // 통합 포털 인터페이스 정의서의 필드 한글명
	Class dataType() default String.class;  // 데이터 타입의 Class
	int dataSize() default 0;               // 데이터 사이즈
}
