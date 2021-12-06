package ke.choxxy.wordsearch.gameover

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.NavUtils
import dagger.hilt.android.AndroidEntryPoint
import ke.choxxy.wordsearch.FullscreenActivity
import ke.choxxy.wordsearch.R
import ke.choxxy.wordsearch.commons.DurationFormatter
import ke.choxxy.wordsearch.commons.viewBinding
import ke.choxxy.wordsearch.databinding.ActivityGameOverBinding
import ke.choxxy.wordsearch.model.GameDataInfo

@AndroidEntryPoint
class GameOverActivity : FullscreenActivity() {

    private val binding by viewBinding(ActivityGameOverBinding::inflate)
    private var mGameId = 0
    private val mViewModel: GameOverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        mViewModel.onGameDataInfoLoaded.observe(this) { info: GameDataInfo -> showGameStat(info) }
        if (intent.extras != null) {
            mGameId = intent.extras!!.getInt(EXTRA_GAME_ROUND_ID)
            mViewModel.loadData(mGameId)
        }

        binding.mainMenuBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToMainMenu()
    }

    private fun goToMainMenu() {
        if (preferences.deleteAfterFinish()) {
            mViewModel.deleteGameRound(mGameId)
        }
        NavUtils.navigateUpTo(this, Intent())
        finish()
    }

    fun showGameStat(info: GameDataInfo) {
        val strGridSize = info.gridRowCount.toString() + " x " + info.gridColCount
        var str = getString(R.string.finish_text)
        str = str.replace(":gridSize".toRegex(), strGridSize)
        str = str.replace(":uwCount".toRegex(), info.usedWordsCount.toString())
        str = str.replace(
            ":duration".toRegex(),
            DurationFormatter.fromInteger(info.duration.toLong())
        )
        binding.gameStatText.text = str
    }

    companion object {
        const val EXTRA_GAME_ROUND_ID =
            "com.paperplanes.wordsearch.presentation.ui.activity.GameOverActivity"
    }
}
