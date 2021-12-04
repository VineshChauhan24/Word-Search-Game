package ke.choxxy.wordsearch.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.choxxy.wordsearch.data.GameThemeRepository
import ke.choxxy.wordsearch.model.GameTheme
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(private val mGameThemeRepository: GameThemeRepository) : ViewModel() {

    private val mOnGameThemeLoaded: MutableLiveData<List<GameTheme>> = MutableLiveData()

    fun loadData() {
        mOnGameThemeLoaded.value = mGameThemeRepository.gameThemes
    }

    val onGameThemeLoaded: LiveData<List<GameTheme>>
        get() = mOnGameThemeLoaded
}
