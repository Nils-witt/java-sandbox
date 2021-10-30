package de.nilswitt.sqlite;

public class DemoComponent extends GUIComponent {

    private String header;
    private String footer;

    public DemoComponent(String header, String footer, int id) {
        super(id);
        this.header = header;
        this.footer = footer;
    }

    public DemoComponent(int id) {
        super(id);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

}
