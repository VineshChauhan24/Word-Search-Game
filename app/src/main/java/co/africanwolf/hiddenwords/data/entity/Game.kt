package co.africanwolf.hiddenwords.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    var id: Int,
    var type: GameType,
    var color: String,
    var levels: Int = 100,
)

@Serializable
data class Player(
    var id: String,
    var name: String,
    var curGameId: Int = -1,
    var gameProgress: List<GameProgress>
)

@Serializable
data class GameProgress(
    var gameId: Int,
    var curLevels: Int,
)
