package ir.s.s.bs1;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class G extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
