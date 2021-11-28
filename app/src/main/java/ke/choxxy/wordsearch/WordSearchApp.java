package ke.choxxy.wordsearch;

import android.app.Application;

import ke.choxxy.wordsearch.di.component.AppComponent;
import ke.choxxy.wordsearch.di.component.DaggerAppComponent;
import ke.choxxy.wordsearch.di.modules.AppModule;

/**
 * Created by abdularis on 18/07/17.
 */

public class WordSearchApp extends Application {

    AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
