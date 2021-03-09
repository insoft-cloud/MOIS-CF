package org.cf.broker.common.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cf.broker.common.annotation.NecessaryParam;
import org.cf.broker.util.BeanUtil;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class NecessaryParamChecker {
	
	@Pointcut("@annotation(org.cf.broker.common.annotation.NecessaryParam)")
	public void necessaryParam() {}

	@Before("necessaryParam()")
	public void serviceParamChecker(JoinPoint joinPoint) throws Exception {
		
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		NecessaryParam necessaryParam = method.getAnnotation(NecessaryParam.class);
		
		String [] arrCheckTargetParams = necessaryParam.useDocument().split(",");
//		String [] arrCheckTargetParams = necessaryParam.names().split(",");
		Object [] args = joinPoint.getArgs();
		
		if(arrCheckTargetParams != null
				&& arrCheckTargetParams.length > 0
				&& args != null
				&& args.length > 0) {
			BeanUtil.necessaryParamChecker(args, arrCheckTargetParams);
		}
	}
}