package ke.choxxy.wordsearch.features.gameover;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import androidx.core.app.NavUtils;
import android.os.Bundle;
import android.widget.TextView;

import ke.choxxy.wordsearch.R;
import ke.choxxy.wordsearch.features.ViewModelFactory;
import ke.choxxy.wordsearch.WordSearchApp;
import ke.choxxy.wordsearch.commons.DurationFormatter;
import ke.choxxy.wordsearch.model.GameDataInfo;
import ke.choxxy.wordsearch.features.FullscreenActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameOverActivity extends FullscreenActivity {
    public static final String EXTRA_GAME_ROUND_ID =
            "com.paperplanes.wordsearch.presentation.ui.activity.GameOverActivity";

    @Inject
    ViewModelFactory mViewModelFactory;

    @BindView(R.id.game_stat_text)
    TextView mGameStatText;

    private int mGameId;
    private GameOverViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        ButterKnife.bind(this);
        ((WordSearchApp) getApplication()).getAppComponent().inject(this);

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(GameOverViewModel.class);
        mViewModel.getOnGameDataInfoLoaded().observe(this, this::showGameStat);

        if (getIntent().getExtras() != null) {
            mGameId = getIntent().getExtras().getInt(EXTRA_GAME_ROUND_ID);
            mViewModel.loadData(mGameId);
        }
    }

    @OnClick(R.id.main_menu_btn)
    public void onMainMenuClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainMenu();
    }

    private void goToMainMenu() {
        if (getPreferences().deleteAfterFinish()) {
            mViewModel.deleteGameRound(mGameId);
        }
        NavUtils.navigateUpTo(this, new Intent());
        finish();
    }

    public void showGameStat(GameDataInfo info) {
        String strGridSize = info.getGridRowCount() + " x " + info.getGridColCount();

        String str = getString(R.string.finish_text);
        str = str.replaceAll(":gridSize", strGridSize);
        str = str.replaceAll(":uwCount", String.valueOf(info.getUsedWordsCount()));
        str = str.replaceAll(":duration", DurationFormatter.fromInteger(info.getDuration()));

        mGameStatText.setText(str);
    }
}
