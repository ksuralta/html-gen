package model;

/**
 * Model class for a single work.
 * 
 * @author Kenneth Suralta
 */
public class Work {
    private String id = "";
    private String filename = "";
    private String urlSmall = "";
    private String urlMedium = "";
    private String urlLarge = "";
    private String cameraMake = "";
    private String cameraModel = "";

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getUrlSmall() {
        return urlSmall;
    }
    public void setUrlSmall(String urlSmall) {
        this.urlSmall = urlSmall;
    }
    public String getUrlMedium() {
        return urlMedium;
    }
    public void setUrlMedium(String urlMedium) {
        this.urlMedium = urlMedium;
    }
    public String getUrlLarge() {
        return urlLarge;
    }
    public void setUrlLarge(String urlLarge) {
        this.urlLarge = urlLarge;
    }
    public String getCameraMake() {
        return cameraMake;
    }
    public void setCameraMake(String cameraMake) {
        this.cameraMake = cameraMake;
    }
    public String getCameraModel() {
        return cameraModel;
    }
    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }
}
