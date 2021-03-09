package org.cf.broker.intrface;

import java.util.Map;

public abstract class AbstractMessageResponse {

	public abstract void initialzeResponseBody(Object object);
	
	/**
	 * <pre>
	 * 후 처리 작업용 키 값 반환용.
	 * - 후 처리 작업이 없는 경우 null 리턴 처리
	 * @return
	 * </pre>
	 */
	public abstract String getAfterJobId();
}
