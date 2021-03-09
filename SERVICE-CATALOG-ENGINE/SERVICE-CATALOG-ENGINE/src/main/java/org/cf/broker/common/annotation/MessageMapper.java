package org.cf.broker.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 통합 포탈의 메세지 필드명의 값을 매핑 시키는 annotation
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageMapper {
	public static final String HEADER = "HEAD";
	public static final String BODY = "BODY";
	
	String dataLoc() default BODY; // 파라미터 값의 위치 구분(전문 헤더, 바디)
	String field() default "";// 파라미터 값과 매칭될 전문의 필드 명
}
