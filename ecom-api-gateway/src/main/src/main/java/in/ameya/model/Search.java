package in.ameya.model;

public class Search{
	
	private Integer loggedInKey;
	private Integer pageNo;
	private Integer pageSize;
	private String orderByColumn;
	private String orderAscDesc;
	private Integer approvedFlag;
	private Integer status;
	
	public Integer count;
	
	public Integer getLoggedInKey() {
		return loggedInKey;
	}
	public void setLoggedInKey(Integer loggedInKey) {
		this.loggedInKey = loggedInKey;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getOrderByColumn() {
		return orderByColumn;
	}
	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}
	public String getOrderAscDesc() {
		return orderAscDesc;
	}
	public void setOrderAscDesc(String orderAscDesc) {
		this.orderAscDesc = orderAscDesc;
	}
	public Integer getApprovedFlag() {
		return approvedFlag;
	}
	public void setApprovedFlag(Integer approvedFlag) {
		this.approvedFlag = approvedFlag;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	protected String searchToString() {
		return "loggedInKey=" + loggedInKey + ", pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", orderByColumn=" + orderByColumn + ", orderAscDesc=" + orderAscDesc + ", approvedFlag="
				+ approvedFlag + ", status=" + status + ", count=" + count;
	}
	
	

}
