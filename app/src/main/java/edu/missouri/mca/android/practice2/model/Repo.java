package edu.missouri.mca.android.practice2.model;

/**
 * Created by samuel on 2/19/18.
 */

public class Repo {
    private String description;
    private String html_url;
    private String name;
    private GitHubUser owner;

    public String getDescription() {
        return description;
    }

    public String getHtmlUrl() {
        return html_url;
    }

    public String getName() {
        return name;
    }

    public GitHubUser getOwner() {
        return owner;
    }
}
