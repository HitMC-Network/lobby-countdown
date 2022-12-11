/*
 * minigame-utils is an collection for commonly needed functionalities when developing minigames for PaperMC.
 *     Copyright (C) 2022 JvstvsHD
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.hitmc.lobbycountdown.team

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import kotlin.math.roundToInt
import kotlin.random.Random

class TwoTeams(override val color: NamedTextColor) : Team {

    private val players: MutableSet<Player> = mutableSetOf()

    override val name: String
        get() = color.toString()

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