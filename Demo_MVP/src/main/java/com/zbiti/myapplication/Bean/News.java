package com.zbiti.myapplication.Bean;

/**
 * Created by admin on 2016/3/29.
 */
public class News {
    private String id;
    private String title;
    private String icon;
    private String createAt;

    public News() {
        super();
    }

    public News(String id, String title, String icon, String createAt) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", createAt='" + createAt + '\'' +
                '}';
    }
}
