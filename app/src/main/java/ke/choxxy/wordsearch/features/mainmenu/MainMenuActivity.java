package ke.choxxy.wordsearch.features.mainmenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ke.choxxy.wordsearch.R;
import ke.choxxy.wordsearch.data.entity.MenuItem;
import ke.choxxy.wordsearch.easyadapter.MenuItemAdapter;
import ke.choxxy.wordsearch.features.ViewModelFactory;
import ke.choxxy.wordsearch.WordSearchApp;
import ke.choxxy.wordsearch.features.FullscreenActivity;
import ke.choxxy.wordsearch.features.gameplay.GamePlayActivity;
import ke.choxxy.wordsearch.features.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends FullscreenActivity {

    @BindView(R.id.list) RecyclerView mRv;

    @BindArray(R.array.game_round_dimension_values)
    int[] mGameRoundDimVals;

    @Inject
    ViewModelFactory mViewModelFactory;
    MainMenuViewModel mViewModel;
    MenuItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);
        ((WordSearchApp) getApplication()).getAppComponent().inject(this);

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainMenuViewModel.class);
        // mViewModel.getOnGameThemeLoaded().observe(this, this::showGameThemeList);

        mAdapter = new MenuItemAdapter(new MenuItemAdapter.OnClickListener() {
            @Override
            public void onClick(@NonNull MenuItem menuItem) {
                newGame();
            }
        });

        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter.submitList(dummyList());
        mViewModel.loadData();
    }

    public List<MenuItem> dummyList(){

        String [] colors = new String[]{
                "#17DEEE",
                "#FF4162",
                "#F2E50B",
                "#21B20C",
                "#FF7F50",
                "#fdc675",
                "#d3fda1",
                "#96e3a5",
                "#41bec2"
        };

        String [] games = getResources().getStringArray(R.array.game_modes);

        List<MenuItem> dummy = new ArrayList<>();
        for(int i= 0; i < games.length;i++){
            dummy.add(new MenuItem(i, games[i], colors[i]));
        }
        return dummy;
    }

    @OnClick(R.id.settings_button)
    public void onSettingsClick() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    public void newGame() {
        int dim = mGameRoundDimVals[(int) (Math.random()*mGameRoundDimVals.length)];
        Intent intent = new Intent(MainMenuActivity.this, GamePlayActivity.class);
        intent.putExtra(GamePlayActivity.EXTRA_ROW_COUNT, dim);
        intent.putExtra(GamePlayActivity.EXTRA_COL_COUNT, dim);
        startActivity(intent);
    }


}
