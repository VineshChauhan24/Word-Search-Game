package co.africanwolf.hiddenwords.gamehistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.africanwolf.hiddenwords.FullscreenActivity
import co.africanwolf.hiddenwords.R
import co.africanwolf.hiddenwords.commons.viewBinding
import co.africanwolf.hiddenwords.databinding.ActivityGameHistoryBinding
import co.africanwolf.hiddenwords.easyadapter.MultiTypeAdapter
import co.africanwolf.hiddenwords.gameplay.GamePlayActivity
import co.africanwolf.hiddenwords.model.GameDataInfo

class GameHistoryActivity : FullscreenActivity() {

    private val binding by viewBinding(ActivityGameHistoryBinding::inflate)
    private val mViewModel: GameHistoryViewModel by viewModels()
    private lateinit var mAdapter: MultiTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_history)
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
        val gameDataAdapterDelegate = GameDataAdapterDelegate(object :
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
