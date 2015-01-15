package model;

/**
 * Navigation Item Model.
 * 
 * @author Kenneth Suralta
 */
public class NavItem {
    private String name;
    private String filename;

    public NavItem(String name, String filename) {
        this.name = name;
        this.filename = filename;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
