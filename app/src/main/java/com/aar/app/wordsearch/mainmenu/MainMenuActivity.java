package com.aar.app.wordsearch.mainmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ListView;
import android.widget.Spinner;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.ViewModelFactory;
import com.aar.app.wordsearch.WordSearchApp;
import com.aar.app.wordsearch.FullscreenActivity;
import com.aar.app.wordsearch.gameplay.GamePlayActivity;
import com.aar.app.wordsearch.model.GameDataInfo;
import com.aar.app.wordsearch.settings.SettingsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends FullscreenActivity {

    @BindView(R.id.game_round_list)
    ListView mListView;
    @BindView(R.id.game_template_spinner)
    Spinner mGameTempSpinner;

    @BindView(R.id.layout_saved_game)
    View mLayoutSavedGame;

    @BindArray(R.array.game_round_dimension_values)
    int[] mGameRoundDimVals;

    @Inject
    ViewModelFactory mViewModelFactory;
    MainMenuViewModel mViewModel;
    GameDataInfoAdapter mInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);
        ((WordSearchApp) getApplication()).getAppComponent().inject(this);

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainMenuViewModel.class);
        mViewModel.getOnGameRoundInfoLoaded().observe(this, this::showGameInfoList);

        mInfoAdapter = new GameDataInfoAdapter(this, R.layout.game_round_item);
        mListView.setAdapter(mInfoAdapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            showGameRound(mInfoAdapter.getItem(position).getId());
        });

        mInfoAdapter.setOnDeleteItemClickListener(info -> {
            deleteInfo(info);
            mViewModel.deleteGameRound(info);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.loadData();
    }

    @OnClick(R.id.new_game_btn)
    public void onNewGameClick() {
        int dim = mGameRoundDimVals[ mGameTempSpinner.getSelectedItemPosition() ];
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra(GamePlayActivity.EXTRA_ROW_COUNT, dim);
        intent.putExtra(GamePlayActivity.EXTRA_COL_COUNT, dim);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @OnClick(R.id.clear_all_btn)
    public void onClearClick() {
        clearInfoList();
        mViewModel.clearAll();
    }

    public void showGameInfoList(List<GameDataInfo> infoList) {
        mInfoAdapter.clear();
        mInfoAdapter.addAll(infoList);
        if (infoList.size() <= 0) {
            mLayoutSavedGame.setVisibility(View.INVISIBLE);
        } else {
            mLayoutSavedGame.setVisibility(View.VISIBLE);
        }
    }

    public void showGameRound(int gid) {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra(GamePlayActivity.EXTRA_GAME_ROUND_ID, gid);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void clearInfoList() {
        final float initXPos = mLayoutSavedGame.getX();
        mLayoutSavedGame.animate()
                .alpha(0.0f)
                .translationX(-mLayoutSavedGame.getWidth())
                .setInterpolator(new AccelerateInterpolator(2.0f))
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLayoutSavedGame.setVisibility(View.INVISIBLE);
                        mLayoutSavedGame.setX(initXPos);
                        mLayoutSavedGame.setAlpha(1.0f);
                        mInfoAdapter.clear();
                    }
                });
    }

    public void deleteInfo(GameDataInfo info) {
        mInfoAdapter.remove(info);
        if (mInfoAdapter.getCount() <= 0) {
            mLayoutSavedGame.animate()
                    .alpha(0.0f)
                    .setDuration(150)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLayoutSavedGame.setVisibility(View.INVISIBLE);
                            mLayoutSavedGame.setAlpha(1.0f);
                        }
                    });
        }
    }

    @OnClick(R.id.settings_button)
    public void onSettingsClick() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
