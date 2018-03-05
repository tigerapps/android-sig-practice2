package edu.missouri.mca.android.practice2.model;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

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

    public CharSequence getPath() {
        final SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(owner.getLogin());
        ssb.append(" / ", new StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(name);
        return ssb;
    }
}
