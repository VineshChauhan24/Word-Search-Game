package com.aar.app.wordsearch.presentation.mainmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.WordSearchApp;
import com.aar.app.wordsearch.domain.model.GameRound;
import com.aar.app.wordsearch.presentation.FullscreenActivity;
import com.aar.app.wordsearch.presentation.gameplay.GamePlayActivity;
import com.aar.app.wordsearch.presentation.settings.SettingsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends FullscreenActivity implements MainMenuView {

    @Inject
    MainMenuPresenter mPresenter;

    @BindView(R.id.game_round_list)
    ListView mListView;
    @BindView(R.id.game_template_spinner)
    Spinner mGameTempSpinner;
    @BindView(R.id.new_game_loading_layout)
    View mNewGameProgress;
    @BindView(R.id.new_game_content_layout)
    View mNewGameContent;

    @BindView(R.id.layout_saved_game)
    View mLayoutSavedGame;

    @BindArray(R.array.game_round_dimension_values)
    int[] mGameRoundDimVals;

    GameRoundInfoAdapter mInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);
        ((WordSearchApp) getApplication()).getAppComponent().inject(this);

        mInfoAdapter = new GameRoundInfoAdapter(this, R.layout.game_round_item);
        mListView.setAdapter(mInfoAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.gameRoundSelected(mInfoAdapter.getItem(position));
            }
        });

        mInfoAdapter.setOnDeleteItemClickListener(new GameRoundInfoAdapter.OnDeleteItemClickListener() {
            @Override
            public void onDeleteItemClick(GameRound.Info info) {
                mPresenter.deleteGameRound(info);
            }
        });

        mPresenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadData();
    }

    @OnClick(R.id.new_game_btn)
    public void onNewGameClick() {
        int dim = mGameRoundDimVals[ mGameTempSpinner.getSelectedItemPosition() ];

        mPresenter.newGameRound(dim, dim);
    }

    @OnClick(R.id.clear_all_btn)
    public void onClearClick() {
        mPresenter.clearAll();
    }

    @Override
    public void setNewGameLoading(boolean enable) {
        if (enable) {
            mNewGameProgress.setVisibility(View.VISIBLE);
            mNewGameContent.setVisibility(View.INVISIBLE);
        }
        else {
            mNewGameProgress.setVisibility(View.INVISIBLE);
            mNewGameContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showGameInfoList(List<GameRound.Info> infoList) {
        mInfoAdapter.clear();
        mInfoAdapter.addAll(infoList);
        if (infoList.size() <= 0) {
            mLayoutSavedGame.setVisibility(View.INVISIBLE);
        } else {
            mLayoutSavedGame.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNewlyCreatedGameRound(GameRound gameRound) {
        showGameRound(gameRound.getInfo().getId());
    }

    @Override
    public void showGameRound(int gid) {
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra(GamePlayActivity.EXTRA_GAME_ROUND_ID, gid);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
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

    @Override
    public void deleteInfo(GameRound.Info info) {
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
