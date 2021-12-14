package co.africanwolf.hiddenwords.gameplay

import android.animation.AnimatorInflater
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import androidx.activity.viewModels
import co.africanwolf.hiddenwords.FullscreenActivity
import co.africanwolf.hiddenwords.R
import co.africanwolf.hiddenwords.SoundPlayer
import co.africanwolf.hiddenwords.commons.DurationFormatter
import co.africanwolf.hiddenwords.commons.Util
import co.africanwolf.hiddenwords.commons.viewBinding
import co.africanwolf.hiddenwords.custom.LetterBoard.OnLetterSelectionListener
import co.africanwolf.hiddenwords.custom.StreakView.StreakLine
import co.africanwolf.hiddenwords.data.entity.Game
import co.africanwolf.hiddenwords.data.entity.GameType
import co.africanwolf.hiddenwords.databinding.ActivityGamePlayBinding
import co.africanwolf.hiddenwords.gameover.GameoverDialog
import co.africanwolf.hiddenwords.gameover.GameoverDialog.Companion.newInstance
import co.africanwolf.hiddenwords.gameover.GameoverDialog.GameOverDialogInputListener
import co.africanwolf.hiddenwords.gameplay.GamePlayViewModel.AnswerResult
import co.africanwolf.hiddenwords.gameplay.GamePlayViewModel.GameState
import co.africanwolf.hiddenwords.gameplay.GamePlayViewModel.Generating
import co.africanwolf.hiddenwords.gameplay.GamePlayViewModel.Paused
import co.africanwolf.hiddenwords.gameplay.GamePlayViewModel.Playing
import co.africanwolf.hiddenwords.gameplay.GamePlayViewModel.Update
import co.africanwolf.hiddenwords.mainmenu.MainMenuActivity
import co.africanwolf.hiddenwords.model.GameData
import co.africanwolf.hiddenwords.model.UsedWord
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class GamePlayActivity : FullscreenActivity(), GameOverDialogInputListener {

    @Inject
    lateinit var mSoundPlayer: SoundPlayer

    private val mViewModel: GamePlayViewModel by viewModels()

    private val binding by viewBinding(ActivityGamePlayBinding::inflate)
    private var mLetterAdapter: ArrayLetterGridDataAdapter? = null
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.letterBoard.streakView.setEnableOverrideStreakLineColor(preferences.grayscale())
        binding.letterBoard.streakView.setOverrideStreakLineColor(R.color.gray)
        binding.letterBoard.setOnLetterSelectionListener(object : OnLetterSelectionListener {
            override fun onSelectionBegin(streakLine: StreakLine, str: String) {
                streakLine.color = Util.getRandomColorWithAlpha(170)
                binding.textSelection.visibility = View.VISIBLE
                binding.textSelection.text = str
            }

            override fun onSelectionDrag(streakLine: StreakLine, str: String) {
                if (str.isEmpty()) {
                    binding.textSelection.text = "..."
                } else {
                    binding.textSelection.text = str
                }
            }

            override fun onSelectionEnd(streakLine: StreakLine, str: String) {
                mViewModel.answerWord(
                    str,
                    STREAK_LINE_MAPPER.revMap(streakLine),
                    preferences.reverseMatching()
                )
                binding.textSelection.text = ""
            }
        })

        mViewModel.onTimer.observe(this) { duration: Long -> showDuration(duration) }
        mViewModel.onGameState.observe(this) { gameState: GameState ->
            onGameStateChanged(
                gameState
            )
        }
        mViewModel.onAnswerResult.observe(this) { answerResult: AnswerResult ->
            onAnswerResult(
                answerResult
            )
        }
        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey(EXTRA_GAME_ROUND_ID)) {
                val gid = extras.getInt(EXTRA_GAME_ROUND_ID)
                mViewModel.loadGameRound(gid)
            } else {
                val rowCount = extras.getInt(EXTRA_ROW_COUNT)
                val colCount = extras.getInt(EXTRA_COL_COUNT)
                val gameData = extras.getString(EXTRA_GAME_DATA)
                game = gameData?.let { Json.decodeFromString(Game.serializer(), it) }!!
                mViewModel.generateNewGameRound(rowCount, colCount, game)
                updateUI()
            }
        }
        if (!preferences.showGridLine()) {
            binding.letterBoard.gridLineBackground.visibility = View.INVISIBLE
        } else {
            binding.letterBoard.gridLineBackground.visibility = View.VISIBLE
        }
        binding.letterBoard.streakView.isSnapToGrid = preferences.snapToGrid
    }

    private fun updateUI() {
        if (game.type == GameType.BLIND_FOLD) {
            binding.flowLayout.visibility = GONE
        }

        binding.gameType.text = game.type.type
    }

    override fun onStart() {
        super.onStart()
        mViewModel.resumeGame()
    }

    override fun onStop() {
        super.onStop()
        mViewModel.pauseGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.stopGame()
    }

    private fun onAnswerResult(answerResult: AnswerResult) {
        if (answerResult.correct) {
            val textView = findUsedWordTextViewByUsedWordId(answerResult.usedWordId)
            if (textView != null) {
                val uw = textView.tag as UsedWord
                if (preferences.grayscale()) {
                    uw.answerLine.color = R.color.gray
                }
                textView.setBackgroundColor(uw.answerLine.color)
                textView.text = uw.string
                textView.setTextColor(Color.WHITE)
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                val anim = AnimatorInflater.loadAnimator(this, R.animator.zoom_in_out)
                anim.setTarget(textView)
                anim.start()
            }
            mSoundPlayer.play(SoundPlayer.Sound.Correct)
        } else {
            binding.letterBoard.popStreakLine()
            mSoundPlayer.play(SoundPlayer.Sound.Wrong)
        }
    }

    private fun onGameStateChanged(gameState: GameState) {
        showLoading(false, null)
        if (gameState is Generating) {
            val state = gameState
            val text = "Generating " + state.rowCount + "x" + state.colCount + " grid"
            showLoading(true, text)
            refresh()
        } else if (gameState is GamePlayViewModel.Finished) {
            showFinishGame(gameState.mGameData.id)
        } else if (gameState is Paused) {
        } else if (gameState is Playing) {
            onGameRoundLoaded(gameState.mGameData)
        } else if (gameState is Update) {
            showAnsweredWordsCount(gameState.mGameData)
        }
    }

    private fun refresh() {
        binding.flowLayout.removeAllViews()
        if (mLetterAdapter != null) {
            mLetterAdapter!!.clear()
        }
        binding.letterBoard.removeAllStreakLine()
    }

    private fun onGameRoundLoaded(gameData: GameData) {
        if (gameData.isFinished) {
            setGameAsAlreadyFinished()
        }
        showLetterGrid(gameData.grid.array)
        showDuration(gameData.duration)
        showUsedWords(gameData.usedWords)
        showWordsCount(gameData.usedWords.size)
        doneLoadingContent()
    }

    private fun tryScale() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val boardWidth = binding.letterBoard.width
        val screenWidth = metrics.widthPixels
        if (preferences.autoScaleGrid() || boardWidth > screenWidth) {
            val scale = screenWidth.toFloat() / boardWidth.toFloat()
            binding.letterBoard.scale(scale, scale)
        }
    }

    private fun doneLoadingContent() {
        // call tryScale() on the next render frame
        Handler().postDelayed({ tryScale() }, 100)
    }

    private fun showLoading(enable: Boolean, text: String?) {
        if (enable) {
            binding.loading.visibility = View.VISIBLE
            binding.loadingText.visibility = View.VISIBLE
            binding.contentLayout.visibility = View.GONE
            binding.loadingText.text = text
        } else {
            binding.loading.visibility = View.GONE
            binding.loadingText.visibility = View.GONE
            binding.contentLayout.visibility = View.VISIBLE
        }
    }

    private fun showLetterGrid(grid: Array<CharArray>) {
        if (mLetterAdapter == null) {
            mLetterAdapter = ArrayLetterGridDataAdapter(grid)
            binding.letterBoard.dataAdapter = mLetterAdapter
        } else {
            mLetterAdapter!!.grid = grid
        }
    }

    private fun showDuration(duration: Long) {
        binding.textDuration.text = DurationFormatter.fromInteger(duration)
    }

    private fun showUsedWords(usedWords: List<UsedWord>) {
        for (uw in usedWords) {
            binding.flowLayout.addView(createUsedWordTextView(uw))
        }
    }

    private fun showAnsweredWordsCount(gameData: GameData) {
        binding.foundWordsCount.text = getString(
            R.string.word_count, gameData.answeredWordsCount,
            gameData.usedWords.size
        )
    }

    private fun showWordsCount(count: Int) {
        binding.foundWordsCount.text = getString(R.string.word_count, 0, count)
    }

    private fun showFinishGame(gameId: Int) {
        /*    Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra(GameOverActivity.EXTRA_GAME_ROUND_ID, gameId);
        startActivity(intent);
        finish();*/
        val fm = supportFragmentManager
        val editNameDialogFragment = newInstance("Some Title")
        editNameDialogFragment.show(fm, "fragment_edit_name")
    }

    private fun setGameAsAlreadyFinished() {
        binding.letterBoard.streakView.isInteractive = false
    }

    //
    private fun createUsedWordTextView(uw: UsedWord): TextView {
        val tv = TextView(this)
        tv.setPadding(10, 5, 10, 5)
        if (uw.isAnswered) {
            if (preferences.grayscale()) {
                uw.answerLine.color = R.color.gray
            }
            tv.setBackgroundColor(uw.answerLine.color)
            tv.text = uw.string
            tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.letterBoard.addStreakLine(STREAK_LINE_MAPPER.map(uw.answerLine))
        } else {
            var str = uw.string
            if (uw.isMystery) {
                var revealCount = uw.revealCount
                val uwString = uw.string
                str = ""
                for (element in uwString) {
                    if (revealCount > 0) {
                        str += element
                        revealCount--
                    } else {
                        str += " _"
                    }
                }
            }
            tv.text = "$str "
        }
        tv.setTextColor(Color.WHITE)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        tv.tag = uw
        return tv
    }

    private fun findUsedWordTextViewByUsedWordId(usedWordId: Int): TextView? {
        for (i in 0 until binding.flowLayout.childCount) {
            val tv = binding.flowLayout.getChildAt(i) as TextView
            val uw = tv.tag as UsedWord
            if (uw != null && uw.id == usedWordId) {
                return tv
            }
        }
        return null
    }

    override fun sendInput(input: String?) {
        if (input == GameoverDialog.ACTION_MAIN_MENU) {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            newGame()
        }
    }

    private fun newGame() {
        val extras = intent.extras
        val rowCount = extras!!.getInt(EXTRA_ROW_COUNT)
        val colCount = extras.getInt(EXTRA_COL_COUNT)
        mViewModel.generateNewGameRound(rowCount, colCount, game)
    }

    companion object {
        const val EXTRA_GAME_ROUND_ID = "co.africanwolf.hiddenwords.gameplay.GamePlayActivity.ID"
        const val EXTRA_ROW_COUNT = "co.africanwolf.hiddenwords.gameplay.GamePlayActivity.ROW"
        const val EXTRA_COL_COUNT = "co.africanwolf.hiddenwords.gameplay.GamePlayActivity.COL"
        const val EXTRA_GAME_DATA =
            "co.africanwolf.hiddenwords.gameplay.GamePlayActivity.GAME_DATA"
        private val STREAK_LINE_MAPPER = StreakLineMapper()
    }
}
