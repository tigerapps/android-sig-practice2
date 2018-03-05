package edu.missouri.mca.android.practice2.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Objects;

import edu.missouri.mca.android.practice2.BR;
import edu.missouri.mca.android.practice2.api.GitHubService;
import edu.missouri.mca.android.practice2.api.GitHubService.SearchResults;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by samuel on 2/19/18.
 */

public class Model extends BaseObservable {
    private final GitHubService gitHubService;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ObservableList<Repo> repos = new ObservableArrayList<>();
    private final Runnable runner = new SearchRunner();
    private String query;

    public Model() {
        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .client(httpClient)
                .build();
        gitHubService = retrofit.create(GitHubService.class);
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
