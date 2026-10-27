package in.ameya.model.nostroReconciliation;

import java.io.Serializable;
import java.util.ArrayList;

public class MT940MessageViewVo implements Serializable
{
	private String creditId;
	private String inOutFlag;
	private String creditType;
	private String tranRefNo;
	private String valueDate;
	private String currId;
	private String msgAmount;
	private String tranType;
	private String bankDetails;
	private String senderCode;
	private String branchId;
	private String bCategoryBranchId;
	private String templateId;
	private String isCorporate;
	private String uploadId;
	private String creditStatus;
	private String createdOn;
	private String modifiedBy;
	private String modifiedOn;
	private String checkedBy;
	private String checkedOn;
	private String isArchived;
	private String accountNoFrom103;
	private String field86Data;
	private String accountId;
	private String recBic;
	private String currCode;
	private String varPurposeId;
	private String msgAmountSum;
	private String siId;
	private String murNumber;
	private String referenceNo;
	private String benReferenceNo;
	private String transTypeIdentificationCode; 
	
	private String statementNumber;
	private String sequenceNumber;
	private String opDebitCreditIndicator;
	private String opDate;
	private String opCurrCode;
	private String opAmount;
	private String clDebitCreditIndicator;
	private String clDate;
	private String clCurrCode;
	private String clAmount;
	private String clAvailDebitCreditIndicator;
	private String clAvailDate;
	private String clAvailCurrCode;
	private String clAvailAmount;
	private String forwardAvailableBalance;
	
	private ArrayList<MT940MessageViewExtVo> mT940MessageViewExtVoList;
	 
	 
	public String getOpDebitCreditIndicator() {
		return opDebitCreditIndicator;
	}
	public void setOpDebitCreditIndicator(String opDebitCreditIndicator) {
		this.opDebitCreditIndicator = opDebitCreditIndicator;
	}
	public String getOpDate() {
		return opDate;
	}
	public void setOpDate(String opDate) {
		this.opDate = opDate;
	}
	public String getOpCurrCode() {
		return opCurrCode;
	}
	public void setOpCurrCode(String opCurrCode) {
		this.opCurrCode = opCurrCode;
	}
	public String getClDebitCreditIndicator() {
		return clDebitCreditIndicator;
	}
	public void setClDebitCreditIndicator(String clDebitCreditIndicator) {
		this.clDebitCreditIndicator = clDebitCreditIndicator;
	}
	public String getClDate() {
		return clDate;
	}
	public void setClDate(String clDate) {
		this.clDate = clDate;
	}
	public String getClCurrCode() {
		return clCurrCode;
	}
	public void setClCurrCode(String clCurrCode) {
		this.clCurrCode = clCurrCode;
	}
	public String getClAvailDebitCreditIndicator() {
		return clAvailDebitCreditIndicator;
	}
	public void setClAvailDebitCreditIndicator(String clAvailDebitCreditIndicator) {
		this.clAvailDebitCreditIndicator = clAvailDebitCreditIndicator;
	}
	public String getClAvailDate() {
		return clAvailDate;
	}
	public void setClAvailDate(String clAvailDate) {
		this.clAvailDate = clAvailDate;
	}
	public String getClAvailCurrCode() {
		return clAvailCurrCode;
	}
	public void setClAvailCurrCode(String clAvailCurrCode) {
		this.clAvailCurrCode = clAvailCurrCode;
	}
	public String getMurNumber() {
		return murNumber;
	}
	public void setMurNumber(String murNumber) {
		this.murNumber = murNumber;
	}
	public String getSiId() {
		return siId;
	}
	public void setSiId(String siId) {
		this.siId = siId;
	}
	public String getMsgAmountSum() {
		return msgAmountSum;
	}
	public void setMsgAmountSum(String msgAmountSum) {
		this.msgAmountSum = msgAmountSum;
	}
	public String getbCategoryBranchId() {
		return bCategoryBranchId;
	}
	public void setbCategoryBranchId(String bCategoryBranchId) {
		this.bCategoryBranchId = bCategoryBranchId;
	}
	public String getVarPurposeId() {
		return varPurposeId;
	}
	public void setVarPurposeId(String varPurposeId) {
		this.varPurposeId = varPurposeId;
	}
	public String getCurrCode() {
		return currCode;
	}
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	public String getCreditId() {
		return creditId;
	}
	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}
	public String getCreditType() {
		return creditType;
	}
	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	public String getTranRefNo() {
		return tranRefNo;
	}
	public void setTranRefNo(String tranRefNo) {
		this.tranRefNo = tranRefNo;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
	public String getCurrId() {
		return currId;
	}
	public void setCurrId(String currId) {
		this.currId = currId;
	}
	public String getMsgAmount() {
		return msgAmount;
	}
	public void setMsgAmount(String msgAmount) {
		this.msgAmount = msgAmount;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getBankDetails() {
		return bankDetails;
	}
	public void setBankDetails(String bankDetails) {
		this.bankDetails = bankDetails;
	}
	public String getSenderCode() {
		return senderCode;
	}
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getIsCorporate() {
		return isCorporate;
	}
	public void setIsCorporate(String isCorporate) {
		this.isCorporate = isCorporate;
	}
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public String getCreditStatus() {
		return creditStatus;
	}
	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getCheckedBy() {
		return checkedBy;
	}
	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}
	public String getCheckedOn() {
		return checkedOn;
	}
	public void setCheckedOn(String checkedOn) {
		this.checkedOn = checkedOn;
	}
	public String getIsArchived() {
		return isArchived;
	}
	public void setIsArchived(String isArchived) {
		this.isArchived = isArchived;
	}
	public String getAccountNoFrom103() {
		return accountNoFrom103;
	}
	public void setAccountNoFrom103(String accountNoFrom103) {
		this.accountNoFrom103 = accountNoFrom103;
	}
	public String getField86Data() {
		return field86Data;
	}
	public void setField86Data(String field86Data) {
		this.field86Data = field86Data;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getRecBic() {
		return recBic;
	}
	public void setRecBic(String recBic) {
		this.recBic = recBic;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getBenReferenceNo() {
		return benReferenceNo;
	}
	public void setBenReferenceNo(String benReferenceNo) {
		this.benReferenceNo = benReferenceNo;
	}
	public String getTransTypeIdentificationCode() {
		return transTypeIdentificationCode;
	}
	public void setTransTypeIdentificationCode(
			String transTypeIdentificationCode) {
		this.transTypeIdentificationCode = transTypeIdentificationCode;
	}
	public ArrayList<MT940MessageViewExtVo> getmT940MessageViewExtVoList() {
		return mT940MessageViewExtVoList;
	}
	public void setmT940MessageViewExtVoList(
			ArrayList<MT940MessageViewExtVo> mT940MessageViewExtVoList) {
		this.mT940MessageViewExtVoList = mT940MessageViewExtVoList;
	}
	public String getStatementNumber() {
		return statementNumber;
	}
	public void setStatementNumber(String statementNumber) {
		this.statementNumber = statementNumber;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getOpAmount() {
		return opAmount;
	}
	public void setOpAmount(String opAmount) {
		this.opAmount = opAmount;
	}
	public String getClAmount() {
		return clAmount;
	}
	public void setClAmount(String clAmount) {
		this.clAmount = clAmount;
	}
	public String getClAvailAmount() {
		return clAvailAmount;
	}
	public void setClAvailAmount(String clAvailAmount) {
		this.clAvailAmount = clAvailAmount;
	}
	public String getInOutFlag() {
		return inOutFlag;
	}
	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}
	public String getForwardAvailableBalance() {
		return forwardAvailableBalance;
	}
	public void setForwardAvailableBalance(String forwardAvailableBalance) {
		this.forwardAvailableBalance = forwardAvailableBalance;
	}
	

}
