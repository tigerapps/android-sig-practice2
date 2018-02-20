package edu.missouri.mca.android.practice2.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import edu.missouri.mca.android.practice2.BR;

/**
 * Created by samuel on 2/19/18.
 */

public class Model extends BaseObservable {
    private final ObservableList<Repo> repos = new ObservableArrayList<>();
    private String query;

    @Bindable
    public String getQuery() {
        return query;
    }

    public ObservableList<Repo> getRepos() {
        return repos;
    }

    public void setQuery(final String query) {
        this.query = query;
        notifyPropertyChanged(BR.query);
    }
}
