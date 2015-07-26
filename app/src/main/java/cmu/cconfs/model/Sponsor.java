package cmu.cconfs.model;

/**
 * Created by zmhbh on 8/21/15.
 */
public class Sponsor {
    private String name;
    private String link;
    private String imageUrl;

    public Sponsor(String name, String link, String imageUrl) {
        this.name = name;
        this.link = link;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
