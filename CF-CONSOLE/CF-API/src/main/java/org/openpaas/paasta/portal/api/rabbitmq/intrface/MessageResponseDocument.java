package org.openpaas.paasta.portal.api.rabbitmq.intrface;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openpaas.paasta.portal.api.rabbitmq.annotation.NecessaryParam;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDocument {

	@NecessaryParam
	private EgovplatformMsgHeader egovplatformMsgHeader;
	
	@NecessaryParam
	private MessageResponseBody egovplatformMsgBody;



	public void createMessageHeader(){
		String str = this.egovplatformMsgHeader.getInterfaceId();
		int fi=str.indexOf(".");
		int li=str.lastIndexOf(".");
		String ToPartyId=this.egovplatformMsgHeader.getToPartyId();
		String FromPartyId=this.egovplatformMsgHeader.getFromPartyId();
		String header=str.substring(0,fi);
		String number=str.substring(li+1);
		String interfaceID = header + "." + ToPartyId + "." + FromPartyId + "."+number;
		this.egovplatformMsgHeader.setSeq(ToPartyId.toUpperCase() + this.egovplatformMsgHeader.getSeq().substring(2,19));                                                           //일련번호
		this.egovplatformMsgHeader.setFromPartyId(ToPartyId);                                                   //송신
		this.egovplatformMsgHeader.setToPartyId(FromPartyId);                                                     //수신
		this.egovplatformMsgHeader.setInterfaceId(interfaceID);
		SimpleDateFormat format = new SimpleDateFormat( "yyyyMMddHHmmss");
		Date time = new Date();
		String time1 = format.format(time);
		this.egovplatformMsgHeader.setCreateDtm(time1);
	}
}
