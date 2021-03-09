package org.openpaas.paasta.portal.api.rabbitmq.annotation;

import java.lang.annotation.*;

/**
 * AOP 를 이용해 파라미터 필수 값을 확인하기 위한 Annotation
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NecessaryParam {
	/**
	 * 해당 파라미터가 사용되는 field에 사용.
	 * useDocument 값이 "" 일 경우 모든 전문에 필수 값.
	 * ex) useDocument = "egovplatform.up.sc.1001:egovplatform.up.sc.1002:egovplatform.up.sc.1003"
	 *     의 경우 1001, 1002, 1003 번 전문에서 필수 값.
	 * @return
	 */
	String useDocument() default ""; // 해당 파라미터가 사용되는 전문명
	String name() default ""; // 필수 누락시 전달될 이름(전문에서의 이름). 빈 문자열의 경우 field 명이 넘어감.
	boolean isDocumentId() default false;
	boolean isLeaf() default true;
}
