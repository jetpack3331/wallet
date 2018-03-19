package hut34.wallet.controller.dto;

import hut34.wallet.util.ValueObject;

public class IndexMeta extends ValueObject {

    private String url;
    private String title;
    private String description;
    private String image;

    public String getUrl() {
        return url;
    }

    public IndexMeta setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public IndexMeta setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IndexMeta setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public IndexMeta setImage(String image) {
        this.image = image;
        return this;
    }

}
