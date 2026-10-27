package in.ameya.model;

public class Menu {

	private Integer key;
	private Integer parentKey;
	
	private String name;
	private String path;
	
	private Integer type;
	private Integer order;
	
	
	public Integer getKey() {
		return key;
	}
	public void setKey(Integer key) {
		this.key = key;
	}
	public Integer getParentKey() {
		return parentKey;
	}
	public void setParentKey(Integer parentKey) {
		this.parentKey = parentKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	@Override
	public String toString() {
		return "Menu [key=" + key + ", name=" + name + ", path=" + path + "]";
	}

}
