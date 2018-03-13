package edu.missouri.mca.android.practice2.model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Handler;
import android.util.Log;

import java.util.Objects;

import javax.inject.Inject;

import edu.missouri.mca.android.practice2.Application.ApplicationHandler;
import edu.missouri.mca.android.practice2.BR;
import edu.missouri.mca.android.practice2.api.GitHubService;
import edu.missouri.mca.android.practice2.api.GitHubService.SearchResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by samuel on 2/19/18.
 */

public class Model extends BaseObservable {
    private static final String KEY_QUERY = "query";

    private final GitHubService gitHubService;
    private final Handler handler;
    private final SharedPreferences preferences;
    private final ObservableList<Repo> repos = new ObservableArrayList<>();
    private final Runnable runner = new SearchRunner();
    private String query;

    @Inject
    public Model(final GitHubService gitHubService, @ApplicationHandler final Handler handler,
                 final SharedPreferences preferences) {
        this.gitHubService = gitHubService;
        this.handler = handler;
        this.preferences = preferences;
        query = preferences.getString(KEY_QUERY, "");
    }

    @Bindable
    public String getQuery() {
        return query;
    }

    public ObservableList<Repo> getRepos() {
        return repos;
    }

    public void setQuery(final String query) {
        if (Objects.equals(this.query, query))
            return;
        this.query = query;
        notifyPropertyChanged(BR.query);
        Log.i("Model", "Set query string to '" + query + '\'');
        preferences.edit().putString(KEY_QUERY, query).apply();
        handler.removeCallbacks(runner);
        handler.postDelayed(runner, 500);
    }

    private class SearchCallback implements Callback<SearchResults> {
        @Override
        public void onFailure(final Call<SearchResults> call, final Throwable t) {
            Log.e("Model", "Update failed: ", t);
        }

        @Override
        public void onResponse(final Call<SearchResults> call,
                               final Response<SearchResults> response) {
            final SearchResults results = response.body();
            if (results != null) {
                repos.clear();
                repos.addAll(results.getItems());
            }
        }
    }

    private class SearchRunner implements Runnable {
        @Override
        public void run() {
            Log.i("Model", "Performing update");
            if (query != null && !query.isEmpty()) {
                final Call<SearchResults> results = gitHubService.searchRepositories(query);
                results.enqueue(new SearchCallback());
            } else {
                repos.clear();
            }
        }
    }
}
