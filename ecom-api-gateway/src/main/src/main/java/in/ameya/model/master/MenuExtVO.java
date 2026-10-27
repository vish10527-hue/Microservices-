package in.ameya.model.master;

import java.io.Serializable;

public class MenuExtVO implements Serializable{

	
		/**
		 * 
		 */
	    private static final long serialVersionUID = 1L;
		private String privilegeId;
	    private String actionsInfo;	 
	    
	    private boolean actionAddEdit;
	    private boolean actionView;
	    private boolean actionAuthorize;
	    
		public String getPrivilegeId() {
			return privilegeId;
		}
		public void setPrivilegeId(String privilegeId) {
			this.privilegeId = privilegeId;
		}
		public String getActionsInfo() {
			return actionsInfo;
		}
		public void setActionsInfo(String actionsInfo) {
			this.actionsInfo = actionsInfo;
		}
		public boolean isActionAddEdit() {
			return actionAddEdit;
		}
		public void setActionAddEdit(boolean actionAddEdit) {
			this.actionAddEdit = actionAddEdit;
		}
		public boolean isActionView() {
			return actionView;
		}
		public void setActionView(boolean actionView) {
			this.actionView = actionView;
		}
		public boolean isActionAuthorize() {
			return actionAuthorize;
		}
		public void setActionAuthorize(boolean actionAuthorize) {
			this.actionAuthorize = actionAuthorize;
		}
	    
	    
	    
	    
}
