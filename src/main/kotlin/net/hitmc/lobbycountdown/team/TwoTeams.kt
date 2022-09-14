package net.hitmc.lobbycountdown.team

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import kotlin.math.roundToInt
import kotlin.random.Random

class TwoTeams(override val color: NamedTextColor) : Team {

    private val players: MutableSet<Player> = mutableSetOf()

    companion object {
        val RED = TwoTeams(NamedTextColor.RED)
        val BLUE = TwoTeams(NamedTextColor.BLUE)

        fun assignTeams() {
            val assignedPlayers = (RED.players + BLUE.players).toMutableList()
            val unassignedPlayers = (Bukkit.getOnlinePlayers() - assignedPlayers.toSet()).toMutableList()
            val teamAssignment = when (unassignedPlayers.size) {
                0 -> return
                1 -> mapOf(
                    Pair(
                        unassignedPlayers.first(), when (RED.players.size) {
                            BLUE.players.size -> randomTeam()
                            in 0 until BLUE.players.size -> RED
                            else -> BLUE
                        }
                    )
                )

                else -> {
                    val blueCount = BLUE.players.size
                    when (RED.players.size) {
                        blueCount -> unassignedPlayers.halfPlayers(randomTeam())
                        in 0 until blueCount -> unassignedPlayers.halfPlayers(RED)
                        else -> unassignedPlayers.halfPlayers(BLUE)
                    }
                }
            }
            for ((player, team) in teamAssignment) {
                team.players.add(player)
            }
        }

        private fun List<Player>.halfPlayers(majorityTeam: TwoTeams): Map<Player, TwoTeams> {
            val (red, blue) = chunked((size / 2.0).roundToInt())
            return red.associateWith { majorityTeam } + blue.associateWith { majorityTeam.opposite() }
        }

        private fun randomTeam() = if (Random.nextBoolean()) RED else BLUE
    }

    override fun players() = players.toSet()

    override fun join(player: Player) {

    }

    fun opposite() = if (this == RED) BLUE else if (this == BLUE) RED else error("Unknown team")
}