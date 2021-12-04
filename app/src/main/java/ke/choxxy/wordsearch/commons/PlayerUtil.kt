package ke.choxxy.wordsearch.commons

import ke.choxxy.wordsearch.data.entity.Game
import ke.choxxy.wordsearch.data.entity.GameProgress
import ke.choxxy.wordsearch.data.entity.Player
import kotlinx.coroutines.flow.Flow
import java.util.UUID

object PlayerUtil {

    suspend fun createPlayer(dataStoreUtil: DataStoreUtil, games: List<Game>) {
        val player = Player(
            id = UUID.randomUUID().toString(),
            name = "",
            gameProgress = initProgress(games)
        )

        dataStoreUtil.setPlayerInfo(player)
    }

    suspend fun Player.incrementLevel(dataStoreUtil: DataStoreUtil) {
        this.gameProgress.forEach { gameProgress ->
            if (gameProgress.gameId == curGameId) {
                gameProgress.curLevels++
            }
        }

        dataStoreUtil.setPlayerInfo(this)
    }

    suspend fun getPlayer(dataStoreUtil: DataStoreUtil): Flow<Player> {
        return dataStoreUtil.getPlayerInfo()
    }

    private fun initProgress(games: List<Game>): List<GameProgress> {
        val gameList = mutableListOf<GameProgress>()

        for (game in games) {
            val progress = GameProgress(game.id, 0)
            gameList.add(progress)
        }

        return gameList
    }
}
