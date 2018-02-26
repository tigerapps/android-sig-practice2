package edu.missouri.mca.android.practice2.api;

import java.util.List;

import edu.missouri.mca.android.practice2.model.Repo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by samuel on 2/19/18.
 */

public interface GitHubService {
    @GET("/search/repositories")
    Call<SearchResults> searchRepositories(@Query("q") String query);

    class SearchResults {
        private List<Repo> items;

        public List<Repo> getItems() {
            return items;
        }
    }
}
