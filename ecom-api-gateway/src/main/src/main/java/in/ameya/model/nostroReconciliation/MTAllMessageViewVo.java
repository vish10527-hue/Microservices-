package in.ameya.model.nostroReconciliation;

import java.io.Serializable;

public class MTAllMessageViewVo implements Serializable{

	private String msgTypeId;
	private String msgTypeCode;
	private String msgId;
	
	public String getMsgTypeId() {
		return msgTypeId;
	}
	public void setMsgTypeId(String msgTypeId) {
		this.msgTypeId = msgTypeId;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getMsgTypeCode() {
		return msgTypeCode;
	}
	public void setMsgTypeCode(String msgTypeCode) {
		this.msgTypeCode = msgTypeCode;
	}
	
	
}
