package org.cf.broker.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.cf.broker.common.annotation.FieldMapper;
import org.cf.broker.common.annotation.NecessaryParam;
import org.cf.broker.exception.ServiceRequiredParameterException;
import org.cf.broker.intrface.EgovplatformMsgBody;
import org.cf.broker.intrface.MessageDocument;
import org.cf.broker.intrface.EgovplatformMsgHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


/**
 * Bean 관련 처리 담당 Class.
 */
public class BeanUtil {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 인터페이스 전문의 Header 값 유효성 검증
     *
     * @param egovplatformMsgHeader
     * @return
     */
    public static String documentHeaderCheck(EgovplatformMsgHeader egovplatformMsgHeader) {
        String documentId = ""; // 전문의 키값(interfaceId)
        Field[] arrHeaderField = egovplatformMsgHeader.getClass().getDeclaredFields();
        for (Field field : arrHeaderField) {
            NecessaryParam necessaryParam = (NecessaryParam) field.getAnnotation(NecessaryParam.class);
            if (necessaryParam != null) {
                Object fieldValue = getObjectValue(egovplatformMsgHeader, field.getName());
                if (StringUtils.isEmpty(fieldValue)) {
                    String name = "".equals(necessaryParam.name()) ? field.getName() : necessaryParam.name();
                    throw new ServiceRequiredParameterException(name);
                }

                if (necessaryParam.isDocumentId()) {
                    documentId = (String) fieldValue;
                }
            }
        }

        return documentId;
    }

    /**
     * 인터페이스 전문의 body 유효성 검증
     *
     * @param messageBody : BODY OBJECT - 전문은 하위 Object 값때문에 Object 타입으로 받음.
     * @param documentId  : 전문의 인터페이스 아이디
     */
    public static void documentBodyCheck(Object messageBody, String documentId) {
        Field[] arrBodyField = messageBody.getClass().getDeclaredFields();

        for (Field field : arrBodyField) {
            NecessaryParam necessaryParam = (NecessaryParam) field.getAnnotation(NecessaryParam.class);
            if (necessaryParam != null) {
                Object fieldValue = getObjectValue(messageBody, field.getName());
                if (necessaryParam.isLeaf()) {
                    // useDocument 값이 "" 이면 모든 전문에서 사용
                    // useDocument 값이 "" 이거나 현재 documentId 가 들어가 있는데 값이 없는 경우.
                    if (("".equals(necessaryParam.useDocument()) || necessaryParam.useDocument().indexOf(documentId) > -1)
                            && StringUtils.isEmpty(fieldValue)) {
                        String name = "".equals(necessaryParam.name()) ? field.getName() : necessaryParam.name();
                        throw new ServiceRequiredParameterException(name);
                    }
                } else {
                    if ("".equals(necessaryParam.useDocument()) || necessaryParam.useDocument().indexOf(documentId) > -1) {
                        documentBodyCheck(fieldValue, documentId);
                    }
                }
            }
        }
    }

    /**
     * 인터페이스 전문 파라미터의 유효성 검증
     *
     * @param messageDocument
     */
    public static String documentValidCheck(MessageDocument messageDocument) {
        String documentId = documentHeaderCheck(messageDocument.getEgovplatformMsgHeader());
        documentBodyCheck(messageDocument.getEgovplatformMsgBody(), documentId);
        return documentId;
    }

//	public static void objectValidCheck(Object document) {
//		Field [] arrField = document.getClass().getDeclaredFields();
//		for(Field field : arrField) {
//			NecessaryParam necessaryParam = (NecessaryParam) field.getAnnotation(NecessaryParam.class);
//			if(necessaryParam != null) {
//				String methodName =  makeMethodName("get", field.getName().trim());
//				Object requiredValue = null;
//				try {
//					requiredValue = document.getClass().getDeclaredMethod(methodName, null)
//					                                   .invoke(document, null);
//				} catch (NoSuchMethodException | SecurityException 
//						| IllegalAccessException | IllegalArgumentException 
//						| InvocationTargetException e) {
//					throw new RuntimeException(e);
//				}
//				
//				if("".equals(necessaryParam.useDocument())) {
//					
//				}
//			}
//		}
//	}

    /**
     * AOP 에서 사용되는 필수 파라미터 값 체크
     *
     * @param args           - Service 메소드에 넘어오는 Parameter Object
     * @param arrTargetParam - 필수 항목 파라미터 명
     */
    public static void necessaryParamChecker(Object[] args, String[] arrTargetParam) throws RuntimeException {
        Arrays.asList(args).stream().forEach(paramObject -> {
            Arrays.asList(arrTargetParam).stream().forEach(param -> {
                paramValueCheck(paramObject, param.trim());
            });
        });
    }

    /**
     * 필드 명으로 get, set 메소드 명 생성
     *
     * @param prefix
     * @param fieldName
     * @return
     */
    public static String makeMethodName(String prefix, String fieldName) {
        return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * Object 의 값 조회
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getObjectValue(Object obj, String fieldName) throws RuntimeException {
        Object rtnObject = null;

        if (obj instanceof Map) {
            rtnObject = ((Map<?, ?>) obj).get(fieldName.trim());
        } else {
            String[] arrFieldName = fieldName.split("\\.");
            // 전문의 파라미터 명에 경로가 포함되어 있을 경우.
            if (arrFieldName.length > 1) {
                String methodName = makeMethodName("get", arrFieldName[0].trim());
                String[] arrSubParam = Arrays.copyOfRange(arrFieldName, 1, arrFieldName.length);

                try {
                    Method method = obj.getClass().getDeclaredMethod(methodName, null);
                    Object recursiveObj = method.invoke(obj, null);

                    // 현재 전문에 사용되는 Object 가 아닐 경우 null 나올 수 있음.
                    if (recursiveObj != null) {
                        rtnObject = getObjectValue(recursiveObj, String.join(".", arrSubParam));
                    }
                } catch (NoSuchMethodException | SecurityException
                        | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else {
                String methodName = makeMethodName("get", fieldName.trim());
                Method method = null;
                try {
                    method = obj.getClass().getDeclaredMethod(methodName, null);
                    rtnObject = method.invoke(obj, null);
                } catch (NoSuchMethodException | SecurityException
                        | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return rtnObject;
    }

    /**
     * set*** 메소드를 활용해 Object 의 field 에 값 주입
     *
     * @param obj
     * @param fieldName
     * @param valueObj
     */
    public static void setObjectValue(Object obj, String fieldName, Object valueObj) {
        if (obj instanceof Map) {
            if (valueObj != null) {
                ((Map) obj).put(fieldName, valueObj);
            }
        } else {
            String methodName = makeMethodName("set", fieldName);
            try {
                // Object 에 선언된 메소드 중 fieldName의 set 메소드를 조회
                Method paramMethod = Arrays.asList(obj.getClass().getDeclaredMethods()).stream().filter(m -> m.getName().equals(methodName)).findFirst().get();

                // set 메소드의 파라미터 타입으로 메소드 조회, Map 형식으로 선언된 field 때문에 ..
                Method method = obj.getClass().getDeclaredMethod(methodName, paramMethod.getParameterTypes()[0]);
                method.invoke(obj, valueObj);
            } catch (NoSuchMethodException | SecurityException
                    | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * <pre>
     * Object 에서 파라미터 명으로 value 값 체크.
     * Map 형태 및 Entity 형태(get*** 형태의 메소드) 두가지 처리.
     *
     * <font color="red">
     * paramName 의 형태에따라 Recursive 발생.
     * (aaa.bbb.ccc)
     * </font>
     *
     * @param paramObj
     * @param paramName
     * @throws RuntimeException
     * </pre>
     */
    public static void paramValueCheck(Object paramObj, String paramName) throws RuntimeException {
        String[] arrParam = paramName.split("\\.");
        if (arrParam.length == 1) {

            String paramValue = (String) getObjectValue(paramObj, arrParam[0]);

            if (StringUtils.isEmpty(paramValue)) {
                throw new ServiceRequiredParameterException(arrParam[0]);
            }

        } else if (arrParam.length > 1) {

            String[] arrSubParam = Arrays.copyOfRange(arrParam, 1, arrParam.length);
            Object subParamObj = getObjectValue(paramObj, arrParam[0]);

            paramValueCheck(subParamObj, String.join(".", arrSubParam));
        }
    }


    /**
     * MessageBody 의 값을 Service 메소드에서 사용하는 field 값으로 매핑
     *
     * @param egovplatformMsgBody
     * @param paramObject
     * @return
     */
    public static Object convertMessageToParameter(EgovplatformMsgBody egovplatformMsgBody, Object paramObject) {

        Field[] paramFields = paramObject.getClass().getDeclaredFields();

        for (Field field : paramFields) {
            String fieldName = field.getName();
            FieldMapper fieldMapper = (FieldMapper) field.getAnnotation(FieldMapper.class);
            if (fieldMapper != null) {
                if (fieldMapper.isLeaf()) {
                    if (fieldMapper.isMap()) {
                        Object obj = getObjectValue(paramObject, fieldName);
                        if (obj == null) {
                            obj = new HashMap<>();
                            setObjectValue(paramObject, fieldName, obj);
                        }

                        String[] arrFieldName = fieldMapper.fieldName().split(",");
                        for (String name : arrFieldName) {
                            String[] arrName = name.split(":");
                            Object value = getObjectValue(egovplatformMsgBody, arrName[0]);       // MessageBody field 명으로 value 조회
                            setObjectValue(obj, arrName[1], value);                       // MessageBody field 값을 파라미터 Object 에 셋팅
                        }
                    } else {
                        Object value = getObjectValue(egovplatformMsgBody, fieldMapper.fieldName()); // MessageBody field 명으로 value 조회
                        setObjectValue(paramObject, fieldName, value);                       // MessageBody field 값을 파라미터 Object 에 셋팅
                    }
                } else {
                    Object obj = getObjectValue(paramObject, fieldName);
                    if (obj == null) {
                        if (fieldMapper.isMap()) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            setObjectValue(paramObject, fieldName, map);
                            convertMessageToParameter(egovplatformMsgBody, map); // recursive
                        } else {
                            try {

                                Constructor constructor = field.getType().getDeclaredConstructor(null);
                                Object objInstance = constructor.newInstance(null);
                                setObjectValue(paramObject, fieldName, objInstance);
                                convertMessageToParameter(egovplatformMsgBody, objInstance); // recursive

                            } catch (NoSuchMethodException | SecurityException
                                    | InstantiationException | IllegalAccessException | IllegalArgumentException
                                    | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
        return paramObject;
    }
}
