package in.ameya.model;

public class ExportResponse {
	private String base64Data;
    private String filename;

    public ExportResponse(String base64Data, String filename) {
        this.base64Data = base64Data;
        this.filename = filename;
    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
