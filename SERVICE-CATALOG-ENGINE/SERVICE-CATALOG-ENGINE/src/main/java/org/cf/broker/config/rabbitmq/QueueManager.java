package org.cf.broker.config.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cf.broker.common.ApplicationContextProvider;
import org.cf.broker.common.Constants;
import org.cf.broker.common.annotation.MessageMapper;
import org.cf.broker.exception.BaseBizException;
import org.cf.broker.exception.ServiceBrokerException;
import org.cf.broker.exception.ServicePreparatoryWorkException;
import org.cf.broker.intrface.MessageDocument;
import org.cf.broker.intrface.MessageResponseBody;
import org.cf.broker.intrface.MessageResponseDocument;
import org.cf.broker.model.QueueServiceTemplate;
import org.cf.broker.model.StatisticsUseService;
import org.cf.broker.model.servicebroker.QueueServiceBroker;
import org.cf.broker.model.serviceinstance.QueueServiceInstance;
import org.cf.broker.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QueueManager {

    private static final Logger logger = LoggerFactory.getLogger(QueueManager.class);

    private final RabbitMQProducer rabbitMQProducer;

    public QueueManager(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public void processQueueMessage(MessageDocument messageDocument) throws Exception {
        // 1. 전문의 유효성 검증
        String interfaceId = BeanUtil.documentValidCheck(messageDocument);
        // 2. 전문 처리를 위한 서비스 호출
        Object resultObject = this.execServiceMethod(interfaceId, messageDocument);
        // 3. 처리 결과 응답 전문 생성
        MessageResponseDocument messageResponseDocument = this.responseResult(resultObject, messageDocument, interfaceId);
        // 4. 요청에 대한 응답 회신
        this.sendResponse(messageResponseDocument);
        // 5. 후 처리 작업 진행(요청 전문에 따라 선택적 처리)
        //this.procAfterJob(interfaceId, messageResponseDocument);
    }

    /**
     * 인터페이스 전문 처리를 위한 서비스 호출
     * @param interfaceId
     * @param messageDocument
     * @return
     * @throws Exception
     */
    public Object execServiceMethod(String interfaceId, MessageDocument messageDocument) throws Exception {
        // 해당 전문의 서비스 처리 method 정보 조회
        String [] arrMethodInfo = Constants.SERVICE_FUNCTION_MAP.get(interfaceId).split(":");
        String methodClassPackage = arrMethodInfo[0].trim();
        String methodClassName = arrMethodInfo[1].trim();
        String methodName = arrMethodInfo[2].trim();
        // 전문 처리 메소드가 있는 서비스 클래스의 인스턴스
        Object targetObject = ApplicationContextProvider.getApplicationContext().getBean(methodClassName);
        String serviceClassPackage = methodClassPackage + "." + methodClassName.substring(0, 1).toUpperCase() + methodClassName.substring(1);
        Class clazz = null;
        try {
            clazz = Class.forName(serviceClassPackage);
        } catch (ClassNotFoundException e) {
//			throw new ServicePreparatoryWorkException("인터페이스를 처리할 클래스 (" + serviceClassPackage + ") 가 존재 하지 않습니다.", "2010");
            throw new ServicePreparatoryWorkException("Invalid Interface ID", "2000");
        }
        // 전문 처리 메소드 정보
        Method paramMethod = this.getTargetMethod(clazz, methodName);
        if(paramMethod == null) {
//			throw new ServicePreparatoryWorkException("인터페이스를 처리할 메소드 (" + methodName + ") 가 존재 하지 않습니다.", "2010");
            throw new ServicePreparatoryWorkException("Interface Process Error", "2010");
        }

        // 전문 처리 메소드의 파라미터 정보 조회
        Object [] parameterObject = this.convertMessageToParameter(messageDocument, paramMethod);
        // 전문 처리 서비스의 실행
        Object resultObject = this.executeMethod(targetObject, parameterObject, interfaceId, methodName);

        // Error 는 Map 형태로 리턴 받음.
        if(resultObject instanceof Map) {
            LinkedHashMap<String, Object> resultMap = (LinkedHashMap<String, Object>) resultObject;
            if(resultMap.containsKey("message")) {
                // 결과 값에 message 가 있으면 실패 상태로 봄.
                // 실패시 resultObject 에 넘어오는 키값 (message, detailMsg, status, code)
                throw new ServicePreparatoryWorkException((String) resultMap.get("detailMsg"), "2021");
            }
        }

        return resultObject;
    }

    /**
     * 전문 메세지를 전문 서리 서비스 메소드의 파라미터 타입으로 변경
     * @param messageDocument - 전문
     * @param paramMethod - 전문을 처리하는 메소드
     * @return
     */
    public Object [] convertMessageToParameter(MessageDocument messageDocument, Method paramMethod) throws Exception {
        Object [] arrParameterObj = null;

        Parameter [] arrParameter = paramMethod.getParameters();
        Class [] arrParamClass = paramMethod.getParameterTypes();

        if(arrParamClass != null && arrParamClass.length > 0) {
            arrParameterObj = new Object[arrParamClass.length];

            int index = 0;
            for(Class paramClass : arrParamClass) {
                if(paramClass.isAssignableFrom(String.class)) {

                    MessageMapper messageMapper = (MessageMapper) arrParameter[index].getAnnotation(MessageMapper.class);
                    Object messageObject = null;
                    if(MessageMapper.HEADER.equals(messageMapper.dataLoc())) {
                        messageObject = messageDocument.getEgovplatformMsgHeader();
                    } else if(MessageMapper.BODY.equals(messageMapper.dataLoc())) {
                        messageObject = messageDocument.getEgovplatformMsgBody();
                    }

                    // Parameter 에 걸린 Message Annotation 의 field 값으로 해당하는 값 조회
                    arrParameterObj[index] = BeanUtil.getObjectValue(messageObject, messageMapper.field());

                } else if(paramClass.isAssignableFrom(QueueServiceBroker.class)) {
                    arrParameterObj[index] = BeanUtil.convertMessageToParameter(messageDocument.getEgovplatformMsgBody(), QueueServiceBroker.builder().build());
                } else if(paramClass.isAssignableFrom(QueueServiceInstance.class)) {
                    arrParameterObj[index] = BeanUtil.convertMessageToParameter(messageDocument.getEgovplatformMsgBody(), QueueServiceInstance.builder().build());
                } else if(paramClass.isAssignableFrom(StatisticsUseService.class)) {
                    arrParameterObj[index] = BeanUtil.convertMessageToParameter(messageDocument.getEgovplatformMsgBody(), new StatisticsUseService());
                }else if(paramClass.isAssignableFrom(QueueServiceTemplate.class)) {
                    arrParameterObj[index] = BeanUtil.convertMessageToParameter(messageDocument.getEgovplatformMsgBody(), QueueServiceTemplate.builder().build());
                }else {
                    throw new ServicePreparatoryWorkException("Not Apply Parameter Class (" + paramClass.getName() + ")", "2030");
                }

                index ++;
            }
        }

        return arrParameterObj;
    }

    /**
     * 서비스 클래스 메소드의 파라미터 클래스 타입 조회
     * @param targetClazz
     * @param methodName
     * @return
     */
    public Method getTargetMethod(Class targetClazz, String methodName) throws Exception {
        Method paramMethod = Arrays.asList(targetClazz.getDeclaredMethods()).stream()
                .filter(method -> method.getName().equals(methodName))
                .collect(Collectors.toList()).get(0);
        return paramMethod;
    }

    /**
     * 서비스 클래스의 메소드 실행.
     * @param targetObject
     * @param parameterObject
     * @param requestType
     * @param methodName
     * @return Object - 처리 결과
     * @throws Exception
     */
    public Object executeMethod(Object targetObject, Object [] parameterObject, String requestType, String methodName) throws Exception {
        Method reqMethod = null;
        try {
            if(parameterObject == null || parameterObject.length == 0) {
                reqMethod = targetObject.getClass().getDeclaredMethod(methodName, (Class<?>)null);
            } else {
                // Object 로 넘어온 파라미터 배열을 Class 배열로 변환(파라미터 타입에 맞는 메소드 찾기 용도.)
                Class [] arrParamType = new Class[parameterObject.length];
                for(int paramIdx = 0; paramIdx < parameterObject.length; paramIdx ++) {
                    arrParamType[paramIdx] = parameterObject[paramIdx].getClass();
                }
                reqMethod = targetObject.getClass().getDeclaredMethod(methodName, arrParamType);
            }
        } catch (NoSuchMethodException e) {
            throw new ServicePreparatoryWorkException("Not InterFace Method (" + methodName + ")", "2040");
        } catch (SecurityException e) {
            logger.error(e.getMessage());
            throw new ServicePreparatoryWorkException(e.getMessage(), "2041", e);
        }

        Object resultObject = null;
        try {
            resultObject = reqMethod.invoke(targetObject, parameterObject);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new ServicePreparatoryWorkException(e.getMessage(), "2042", e);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new ServicePreparatoryWorkException("Method Parameter Error (" + methodName + ":::" + e.getMessage()+")", "2043", e);
        } catch (InvocationTargetException e) {
            logger.error(e.getTargetException().getMessage());
            if(e.getTargetException() instanceof BaseBizException) {
                throw (BaseBizException)e.getTargetException();
            } else {
                throw new ServicePreparatoryWorkException("Method Error (" + methodName + "::" + e.getMessage() + " )", "2044", e);
            }
        }
        return resultObject;
    }

    /**
     * 처리 결과를 JSON String 형태로 생성.
     * @param resultObject
     * @param messageDocument
     * @param interfaceId
     */
    public MessageResponseDocument responseResult(Object resultObject, MessageDocument messageDocument, String interfaceId) throws Exception {
        MessageResponseBody messageResponseBody = new MessageResponseBody(interfaceId, resultObject);

        MessageResponseDocument messageResponseDocument = new MessageResponseDocument(messageDocument.getEgovplatformMsgHeader(), messageResponseBody);
        return messageResponseDocument;
    }

    /**
     * 통합 포털에 요청에 대한 응답 회신
     * @param messageResponseDocument
     * @throws ServiceBrokerException
     */
    public void sendResponse(MessageResponseDocument messageResponseDocument) throws Exception {

        try {
            ObjectMapper mapper = new ObjectMapper();
            if(!(messageResponseDocument.getEgovplatformMsgHeader().getInterfaceId().equals("egovplatform.mp.sc.1062") || messageResponseDocument.getEgovplatformMsgHeader().getInterfaceId().equals("egovplatform.mp.sc.1073")))
            {
                logger.info("sendMessage ::::" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageResponseDocument));
            }
            if(messageResponseDocument.getEgovplatformMsgHeader().getFromPartyId().toUpperCase().equals("CF")){
                return;
            }
            messageResponseDocument.createMessageHeader();
            rabbitMQProducer.sendResponseWithJson(messageResponseDocument, messageResponseDocument.getEgovplatformMsgHeader().getToPartyId());
        } catch (ServiceBrokerException e) {
            logger.error(e.getMessage());
            throw new ServicePreparatoryWorkException("Json Parsing Error", "2050", e);
        }
    }

    /**
     * 작업 처리 요청에 대한 응답 후 잔여 처리 작업.
     * - 서비스 생성, 수정 과 같이 작업 요청 후 완료까지 시간이 소요되는 작업에 대한 상태 체크
     * @param interfaceId
     * @param messageResponseDocument
     */
    public void procAfterJob(String interfaceId, MessageResponseDocument messageResponseDocument) throws Exception {
//		switch (interfaceId){
//			case Constants.SERVICE_REQUEST_1001 :
//			case Constants.SERVICE_REQUEST_1004 :
//			case Constants.SERVICE_REQUEST_1005 :
//			case Constants.SERVICE_REQUEST_1006 :
//			case Constants.SERVICE_REQUEST_1018 :
//			case Constants.SERVICE_REQUEST_1019 : {
//				MessageDocument messageDocument = new MessageDocument();
//				messageDocument.settingMessageDocument(messageResponseDocument);
//				rabbitMQProducer.sendResponseWithJson(messageDocument, messageDocument.getMessageHeader().getToPartyId());
//				break;
//			}
//			default: break;
//		}
    }
}
