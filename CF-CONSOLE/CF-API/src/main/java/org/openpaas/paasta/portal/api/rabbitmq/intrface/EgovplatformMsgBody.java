package org.openpaas.paasta.portal.api.rabbitmq.intrface;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.rabbitmq.annotation.NecessaryParam;

/**
 * 인터페이스 Body
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EgovplatformMsgBody {

	@NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
            + "," + Constants.SERVICE_REQUEST_1012
            + "," + Constants.SERVICE_REQUEST_1013)
	private String user_id;

    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1002
            + "," + Constants.SERVICE_REQUEST_1004)
    private String organization_name;
    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1002
            + "," + Constants.SERVICE_REQUEST_1004)
    private String quota_name;
    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
            + "," + Constants.SERVICE_REQUEST_1002
            + "," + Constants.SERVICE_REQUEST_1004
            + "," + Constants.SERVICE_REQUEST_1011
            + "," + Constants.SERVICE_REQUEST_1012)
    private String project_id;

    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1011)
    private String build_pack_name;

    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1011)
    private String application_name;

    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1011)
    private String disk_size;

    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1011)
    private String memory_size;

    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1011)
    private String host_name;

    @NecessaryParam(useDocument = Constants.SERVICE_REQUEST_1001
            + "," + Constants.SERVICE_REQUEST_1012)
    private String user_type;



}
