package edu.missouri.mca.android.practice2.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import org.threeten.bp.Instant;

import java.io.IOException;

import edu.missouri.mca.android.practice2.BR;
import edu.missouri.mca.android.practice2.api.GitHubService;
import edu.missouri.mca.android.practice2.api.GitHubService.SearchResults;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by samuel on 2/19/18.
 */

public class Model extends BaseObservable {
    private final GitHubService gitHubService;
    private final ObservableList<Repo> repos = new ObservableArrayList<>();
    private Instant lastUpdate = Instant.EPOCH;
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
        this.query = query;
        notifyPropertyChanged(BR.query);
        Log.i("Model", "Set query string to '" + query + '\'');
        final Instant now = Instant.now();
        if (now.minusMillis(1000).compareTo(lastUpdate) > 0) {
            Log.i("Model", "Performing update");
            try {
                final SearchResults results = gitHubService.searchRepositories(query)
                        .execute()
                        .body();
                if (results != null) {
                    repos.clear();
                    repos.addAll(results.getItems());
                }
            } catch (final IOException e) {
                Log.i("Model", "API call failed:", e);
            }
            lastUpdate = now;
        } else {
            Log.i("Model", "Skipping update");
        }
    }
}
