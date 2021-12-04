package ke.choxxy.wordsearch.gamehistory

import ke.choxxy.wordsearch.data.GameDataSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import ke.choxxy.wordsearch.model.GameDataInfo
import ke.choxxy.wordsearch.data.GameDataSource.InfosCallback
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.choxxy.wordsearch.data.sqlite.GameDataSQLiteDataSource
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class GameHistoryViewModel @Inject constructor(private val mGameDataSource: GameDataSQLiteDataSource) : ViewModel() {

    private val mOnGameDataInfoLoaded: MutableLiveData<List<GameDataInfo>> = MutableLiveData()

    fun loadGameHistory() {
        mGameDataSource.getGameDataInfos { infoList: List<GameDataInfo> ->
            mOnGameDataInfoLoaded.setValue(
                infoList
            )
        }
    }

    fun deleteGameData(gameDataInfo: GameDataInfo) {
        mGameDataSource.deleteGameData(gameDataInfo.id)
        loadGameHistory()
    }

    fun clear() {
        mGameDataSource.deleteGameDatas()
        mOnGameDataInfoLoaded.value = ArrayList()
    }

    val onGameDataInfoLoaded: LiveData<List<GameDataInfo>>
        get() = mOnGameDataInfoLoaded

}