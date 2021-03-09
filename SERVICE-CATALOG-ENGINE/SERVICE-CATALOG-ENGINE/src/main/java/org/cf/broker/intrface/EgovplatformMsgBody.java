package org.cf.broker.intrface;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.broker.common.Constants;
import org.cf.broker.common.annotation.NecessaryParam;

/**
 * 인터페이스 Body
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EgovplatformMsgBody {

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
			+ "," + Constants.SERVICE_REQUEST_1002
			+ "," + Constants.SERVICE_REQUEST_1003
			+ "," + Constants.SERVICE_REQUEST_1004
			+ "," + Constants.SERVICE_REQUEST_1005
			+ "," + Constants.SERVICE_REQUEST_1006
			+ "," + Constants.SERVICE_REQUEST_1020
			+ "," + Constants.SERVICE_REQUEST_1021
			+ "," + Constants.SERVICE_REQUEST_1022)
	private String service_broker_id;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
			+ "," + Constants.SERVICE_REQUEST_1002
			+ "," + Constants.SERVICE_REQUEST_1003
			+ "," + Constants.SERVICE_REQUEST_1004
			+ "," + Constants.SERVICE_REQUEST_1051)
	private String service_instance_id;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
			+ "," + Constants.SERVICE_REQUEST_1002)
	private String service_instance_nm;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
			+ "," + Constants.SERVICE_REQUEST_1051)
	private String service_id;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
			+ "," + Constants.SERVICE_REQUEST_1002
			+ "," + Constants.SERVICE_REQUEST_1051)
	private String plan_id;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
			+ "," + Constants.SERVICE_REQUEST_1005
			+ "," + Constants.SERVICE_REQUEST_1051
			+ "," + Constants.SERVICE_REQUEST_1053)
	private String project_id;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001)
	private String is_singleton;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001)
	private String service_metering_type;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1020)
	private String service_broker_type;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001)
	private Object parameter;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1020
			+ "," + Constants.SERVICE_REQUEST_1021)
	private String service_broker_url;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1003)
	private String operation_type;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1030
			+ "," + Constants.SERVICE_REQUEST_1031
			+ "," + Constants.SERVICE_REQUEST_1032)
	private String platform_type;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1030
			+ "," + Constants.SERVICE_REQUEST_1031
			+ "," + Constants.SERVICE_REQUEST_1032)
	private String service_type;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1030
			+ "," + Constants.SERVICE_REQUEST_1031
			+ "," + Constants.SERVICE_REQUEST_1032)
	private String image_type;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1031
			+ "," + Constants.SERVICE_REQUEST_1032)
	private String image_nm;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1031
			+ "," + Constants.SERVICE_REQUEST_1032
			+ "," + Constants.SERVICE_REQUEST_1040
			+ "," + Constants.SERVICE_REQUEST_1041
			+ "," + Constants.SERVICE_REQUEST_1042)
	private String use_at;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1031
			+ "," + Constants.SERVICE_REQUEST_1032
			+ "," + Constants.SERVICE_REQUEST_1041
			+ "," + Constants.SERVICE_REQUEST_1042)
	private String dc;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1032
			+ "," + Constants.SERVICE_REQUEST_1033)
	private String service_image_id;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1042
			+ "," + Constants.SERVICE_REQUEST_1043)
	private String service_template_id;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1041
			+ "," + Constants.SERVICE_REQUEST_1042)
	private String template_nm;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1041
			+ "," + Constants.SERVICE_REQUEST_1042)
	private String tag;

	/*@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_2001)
	private String space_id;*/

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1041
			+ "," + Constants.SERVICE_REQUEST_1042)
	private Object template_services;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1041
			+ "," + Constants.SERVICE_REQUEST_1042)
	private Object template_file;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1050)
	private Object search_type;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1050)
	private Object start_date;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1050)
	private Object end_date;

	//미터링

	/*@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_2001)
	private String service_id;*/

	/*@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_2001)
	private String service_instance_id;*/

	/*@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_2001)
	private String svc_plan_id;*/


	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1053)
	private String use_month;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1052)
	private String unit_value;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1052
	//+ "," + Constants.SERVICE_REQUEST_1054
	+ "," + Constants.SERVICE_REQUEST_1055
	+ "," + Constants.SERVICE_REQUEST_1056)
	private String unit_type;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1054
			+ "," + Constants.SERVICE_REQUEST_1055)
	private String unit_info;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1054
			+ "," + Constants.SERVICE_REQUEST_1055)
	private String unit_nm;

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1054
			+ "," + Constants.SERVICE_REQUEST_1055)
	private String description;

}
