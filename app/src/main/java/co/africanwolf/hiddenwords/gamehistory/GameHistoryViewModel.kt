package co.africanwolf.hiddenwords.gamehistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.africanwolf.hiddenwords.data.sqlite.GameDataSQLiteDataSource
import co.africanwolf.hiddenwords.model.GameDataInfo
import dagger.hilt.android.lifecycle.HiltViewModel
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
