package com.einsteiny.einsteiny;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.interceptors.ParseLogInterceptor;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by lukas on 4/3/17.
 */

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        FlowManager.init(new FlowConfig.Builder(this).build());

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("einsteiny-client") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://einsteiny.herokuapp.com/parse/").build());

        ParseFacebookUtils.initialize(this);

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

       // ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}