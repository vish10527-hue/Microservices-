package in.ameya.model.master;

import java.io.Serializable;

public class MenuVoList implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String url;
	private String icon;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	

		
}
