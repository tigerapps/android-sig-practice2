package edu.missouri.mca.android.practice2.model;

import android.text.TextUtils;

/**
 * Created by samuel on 2/19/18.
 */

public class Repo {
    private static final String URL_BASE = "https://github.com";

    private final String description;
    private final String name;
    private final String owner;

    public Repo(final String description, final String name, final String owner) {
        this.description = description;
        this.name = name;
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getUrl() {
        return TextUtils.join("/", new Object[]{URL_BASE, owner, name});
    }
}
