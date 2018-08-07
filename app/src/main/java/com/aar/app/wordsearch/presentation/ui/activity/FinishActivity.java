package com.aar.app.wordsearch.presentation.ui.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.widget.TextView;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.WordSearchApp;
import com.aar.app.wordsearch.commons.DurationFormatter;
import com.aar.app.wordsearch.domain.model.GameRoundStat;
import com.aar.app.wordsearch.presentation.presenters.GameOverPresenter;
import com.aar.app.wordsearch.presentation.views.GameOverView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinishActivity extends FullscreenActivity implements GameOverView {
    public static final String EXTRA_GAME_ROUND_ID =
            "com.paperplanes.wordsearch.presentation.ui.activity.FinishActivity";

    @Inject
    GameOverPresenter mPresenter;

    @BindView(R.id.game_stat_text)
    TextView mGameStatText;

    private int mGameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        ButterKnife.bind(this);
        ((WordSearchApp) getApplication()).getAppComponent().inject(this);

        mPresenter.setView(this);

        if (getIntent().getExtras() != null) {
            mGameId = getIntent().getExtras().getInt(EXTRA_GAME_ROUND_ID);
            mPresenter.loadData(mGameId);
        }
    }

    @OnClick(R.id.main_menu_btn)
    public void onMainMenuClick() {
        goToMainMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainMenu();
    }

    private void goToMainMenu() {
        if (mPreferences.deleteAfterFinish()) {
            mPresenter.deleteGameRound(mGameId);
        }
        NavUtils.navigateUpTo(this, new Intent());
        finish();

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void showGameStat(GameRoundStat stat) {
        String strGridSize = stat.getGridRowCount() + " x " + stat.getGridColCount();

        String str = getString(R.string.finish_text);
        str = str.replaceAll(":gridSize", strGridSize);
        str = str.replaceAll(":uwCount", String.valueOf(stat.getUsedWordCount()));
        str = str.replaceAll(":duration", DurationFormatter.fromInteger(stat.getDuration()));

        mGameStatText.setText(str);
    }
}
