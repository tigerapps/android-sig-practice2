package edu.missouri.mca.android.practice2;

import com.gabrielittner.threetenbp.LazyThreeTen;

/**
 * Created by samuel on 2/19/18.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LazyThreeTen.init(this);
    }
}
