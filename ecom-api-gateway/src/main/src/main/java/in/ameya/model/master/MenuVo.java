package in.ameya.model.master;

import java.io.Serializable;

public class MenuVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String menuId;
	private long menuParentId;
	private String displayName;
	private String folderPath;
	private String menuType;
    private String privilegeId;
    private String actionsInfo;	
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public long getMenuParentId() {
		return menuParentId;
	}

	public void setMenuParentId(long menuParentId) {
		this.menuParentId = menuParentId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	
	
	
}
