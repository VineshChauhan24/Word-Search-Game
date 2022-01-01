package co.africanwolf.hiddenwords.gameover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.africanwolf.hiddenwords.data.sqlite.GameDataSQLiteDataSource
import co.africanwolf.hiddenwords.model.GameDataInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameOverViewModel @Inject constructor(private val mGameDataSource: GameDataSQLiteDataSource) : ViewModel() {
    private val mOnGameDataInfoLoaded = MutableLiveData<GameDataInfo>()
    fun loadData(gid: Int) {
        mGameDataSource.getGameDataInfo(gid) { value: GameDataInfo -> mOnGameDataInfoLoaded.setValue(value) }
    }

    fun deleteGameRound(gid: Int) {
        mGameDataSource.deleteGameData(gid)
    }

    val onGameDataInfoLoaded: LiveData<GameDataInfo>
        get() = mOnGameDataInfoLoaded
}
