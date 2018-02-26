package edu.missouri.mca.android.practice2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.gabrielittner.threetenbp.LazyThreeTen;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Qualifier;
import javax.inject.Scope;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import edu.missouri.mca.android.practice2.api.GitHubService;
import edu.missouri.mca.android.practice2.model.Model;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by samuel on 2/19/18.
 */

public class Application extends android.app.Application {
    private static ApplicationComponent component;

    public static ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LazyThreeTen.init(this);
        component = DaggerApplication_ApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    @ApplicationScope
    @Component(modules = ApplicationModule.class)
    public interface ApplicationComponent {
        Model getModel();

        Picasso getPicasso();
    }

    @Qualifier
    public @interface ApplicationContext {
    }

    @Qualifier
    public @interface ApplicationHandler {
    }

    @Scope
    public @interface ApplicationScope {
    }

    @Qualifier
    public @interface BackgroundHandler {
    }

    @Module
    public static final class ApplicationModule {
        private final Context context;

        private ApplicationModule(final Application application) {
            context = application.getApplicationContext();
        }

        @ApplicationScope
        @Provides
        public static Config getAppConfig() {
            return new Config("https://api.github.com", 1000000);
        }

        @ApplicationScope
        @Provides
        public static Cache getCache(final Config config,
                                     @ApplicationContext final Context context) {
            return new Cache(context.getCacheDir(), config.cacheSize);
        }

        @ApplicationScope
        @Provides
        public static GitHubService getGitHubService(final Config config,
                                                     final OkHttpClient okHttpClient) {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(config.baseUrl)
                    .client(okHttpClient)
                    .build()
                    .create(GitHubService.class);
        }

        @ApplicationHandler
        @ApplicationScope
        @Provides
        public static Handler getHandler(@ApplicationContext final Context context) {
            return new Handler(context.getMainLooper());
        }

        @ApplicationScope
        @Provides
        public static OkHttpClient getOkHttpClient(final Cache cache) {
            return new OkHttpClient.Builder()
                    .cache(cache)
                    .build();
        }

        @ApplicationScope
        @Provides
        public static Picasso getPicasso(@ApplicationContext final Context context,
                                         final OkHttpClient okHttpClient) {
            return new Picasso.Builder(context)
                    .downloader(new OkHttp3Downloader(okHttpClient))
                    .build();
        }

        @ApplicationScope
        @Provides
        public static SharedPreferences getPreferences(@ApplicationContext final Context context) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }

        @ApplicationContext
        @ApplicationScope
        @Provides
        public Context getContext() {
            return context;
        }
    }

    public static final class Config {
        private final String baseUrl;
        private final long cacheSize;

        private Config(final String baseUrl, final long cacheSize) {
            this.baseUrl = baseUrl;
            this.cacheSize = cacheSize;
        }
    }
}
