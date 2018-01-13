package tk.ckknight.productqueue;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by sauyee on 13/1/18.
 */

public class ProductQueue extends Application {
    private static ProductQueue INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Stetho.initializeWithDefaults(this);
    }

    public static ProductQueue getInstance() {
        return INSTANCE;
    }
}
