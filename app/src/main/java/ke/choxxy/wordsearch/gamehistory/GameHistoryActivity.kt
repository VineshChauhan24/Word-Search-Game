package ke.choxxy.wordsearch.gamehistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import ke.choxxy.wordsearch.FullscreenActivity
import ke.choxxy.wordsearch.R
import ke.choxxy.wordsearch.commons.viewBinding
import ke.choxxy.wordsearch.databinding.ActivityGameHistoryBinding
import ke.choxxy.wordsearch.easyadapter.MultiTypeAdapter
import ke.choxxy.wordsearch.gameplay.GamePlayActivity
import ke.choxxy.wordsearch.model.GameDataInfo

class GameHistoryActivity : FullscreenActivity() {

    private val binding by viewBinding(ActivityGameHistoryBinding::inflate)
    private val mViewModel: GameHistoryViewModel by viewModels()
    private lateinit var mAdapter: MultiTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_history)
        ButterKnife.bind(this)
        initRecyclerView()
        mViewModel.onGameDataInfoLoaded.observe(this) { gameDataInfos: List<GameDataInfo> ->
            onGameDataInfoLoaded(
                gameDataInfos
            )
        }

        binding.btnClear.setOnClickListener {
            mViewModel.clear()
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.loadGameHistory()
    }

    private fun onGameDataInfoLoaded(gameDataInfos: List<GameDataInfo>) {
        if (gameDataInfos.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.textEmpty.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.textEmpty.visibility = View.GONE
            mAdapter.setItems(gameDataInfos)
        }
    }

    private fun initRecyclerView() {
        val gameDataAdapterDelegate = GameDataAdapterDelegate()
        gameDataAdapterDelegate.setOnClickListener(object :
            GameDataAdapterDelegate.OnClickListener {
            override fun onClick(gameDataInfo: GameDataInfo) {
                val intent = Intent(this@GameHistoryActivity, GamePlayActivity::class.java)
                intent.putExtra(GamePlayActivity.EXTRA_GAME_ROUND_ID, gameDataInfo.id)
                startActivity(intent)
            }

            override fun onDeleteClick(gameDataInfo: GameDataInfo) {
                    mViewModel.deleteGameData(gameDataInfo)
            }
        })
        mAdapter = MultiTypeAdapter()
        mAdapter.addDelegate(gameDataAdapterDelegate)
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}