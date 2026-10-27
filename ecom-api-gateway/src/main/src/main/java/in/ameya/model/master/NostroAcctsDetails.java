package in.ameya.model.master;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Detail;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NostroAcctsDetails  extends Detail {
	
	private Integer nostroAcctsKey;
	private String nostroAcctsNo;
	private String nostroName;
	private String bankCode;
	private String currKey;
	private String currCode;
	private String senderBic;
	private String receiverBic;
	private Integer status;
	private Integer refNostroAcctsKey;
	
	private Integer outFileType;
	private String finNostroMirror;
	
	public Integer getNostroAcctsKey() {
		return nostroAcctsKey;
	}

	public void setNostroAcctsKey(Integer nostroAcctsKey) {
		this.nostroAcctsKey = nostroAcctsKey;
	}

	public String getNostroAcctsNo() {
		return nostroAcctsNo;
	}

	public void setNostroAcctsNo(String nostroAcctsNo) {
		this.nostroAcctsNo = nostroAcctsNo;
	}

	public String getNostroName() {
		return nostroName;
	}

	public void setNostroName(String nostroName) {
		this.nostroName = nostroName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCurrKey() {
		return currKey;
	}

	public void setCurrKey(String currKey) {
		this.currKey = currKey;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public String getSenderBic() {
		return senderBic;
	}

	public void setSenderBic(String senderBic) {
		this.senderBic = senderBic;
	}

	public String getReceiverBic() {
		return receiverBic;
	}

	public void setReceiverBic(String receiverBic) {
		this.receiverBic = receiverBic;
	}

	
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRefNostroAcctsKey() {
		return refNostroAcctsKey;
	}

	public void setRefNostroAcctsKey(Integer refNostroAcctsKey) {
		this.refNostroAcctsKey = refNostroAcctsKey;
	}

	
	
	@Override
	public String toString() {
		return "MasterNostroAcctsDetail [nostroAcctsKey=" + nostroAcctsKey + ", nostroAcctsNo=" + nostroAcctsNo + ", nostroName="
				+ nostroName + ", status=" + status + ", refNostroAcctsKey=" + refNostroAcctsKey + ", " + detailToString() + "]";
	}

	public Integer getOutFileType() {
		return outFileType;
	}

	public void setOutFileType(Integer outFileType) {
		this.outFileType = outFileType;
	}

	public String getFinNostroMirror() {
		return finNostroMirror;
	}

	public void setFinNostroMirror(String finNostroMirror) {
		this.finNostroMirror = finNostroMirror;
	}

	

}
