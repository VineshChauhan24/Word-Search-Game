package co.africanwolf.hiddenwords;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import dagger.hilt.android.AndroidEntryPoint;
import co.africanwolf.hiddenwords.settings.Preferences;

import javax.inject.Inject;

/**
 * Created by abdularis on 21/04/17.
 *
 * Extend this class to make a fullscreen activity
 */

@SuppressLint("Registered")
@AndroidEntryPoint
public class FullscreenActivity extends AppCompatActivity {

    @Inject
    Preferences mPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPreferences.enableFullscreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    protected Preferences getPreferences() {
        return mPreferences;
    }
}
