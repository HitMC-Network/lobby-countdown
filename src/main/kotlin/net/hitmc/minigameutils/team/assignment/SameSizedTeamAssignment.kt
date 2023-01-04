/*
 * minigame-utils is an collection for commonly needed functionalities when developing minigames for PaperMC.
 *     Copyright (C) 2022-2023 JvstvsHD
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

package net.hitmc.minigameutils.team.assignment

import net.hitmc.minigameutils.team.SameSizedTeam
import net.hitmc.minigameutils.team.TeamRegistry
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object SameSizedTeamAssignment : TeamAssignment {
    
    override fun assignTeams() {
        val registryData = TeamRegistry.copyData()
        val registeredTeams = registryData.registeredTeams.filterIsInstance<SameSizedTeam>().toMutableList()
        val maxTeamSize = registryData.maxTeamSize
        val assignedPlayers = registeredTeams.flatMap { it.players }
        val unassignedPlayers = (Bukkit.getOnlinePlayers() - assignedPlayers.toSet()).toMutableList()
        val teamAssignment = when (unassignedPlayers.size) {
            0 -> return
            1 -> mapOf(Pair(unassignedPlayers.first(), registeredTeams.minByOrNull { it.players.size }!!))
            else -> {
                when (Bukkit.getOnlinePlayers().size) {
                    registeredTeams.size * registryData.maxTeamSize -> {
                        val map = mutableMapOf<Player, SameSizedTeam>()
                        val players = unassignedPlayers.toMutableList()
                        for (registeredTeam in registeredTeams) {
                            val count = maxTeamSize - registeredTeam.players.size
                            for (i in 0 until count) {
                                val player = players.removeAt(i)
                                map[player] = registeredTeam
                            }
                        }
                        map
                    }

                    else -> {
                        val players = Bukkit.getOnlinePlayers()
                        val filledTeams = registeredTeams.filter { it.players.isNotEmpty() }.toMutableList()
                        val emptyTeams = (registeredTeams - filledTeams.toSet()).toMutableList()
                        while (players.size > filledTeams.size * maxTeamSize && emptyTeams.isNotEmpty()) {
                            filledTeams.add(emptyTeams.removeFirst())
                        }
                        val map = mutableMapOf<Player, SameSizedTeam>()
                        val averageLevel =
                            players.size / (filledTeams.size * maxTeamSize).toDouble()
                        val averageTeamSize = maxTeamSize * averageLevel
                        val biggerTeams = filledTeams.filter { it.players.size > averageTeamSize }
                        val averageTeamSizeSmallerTeams = averageTeamSize - (biggerTeams.map { it.players.size }
                            .sumOf { it - averageTeamSize }) / (filledTeams.size - biggerTeams.size).toDouble()

                        for (registeredTeam in filledTeams) {
                            if (registeredTeam.players.size < averageTeamSize) {
                                val count = averageTeamSizeSmallerTeams.toInt() - registeredTeam.players.size
                                if (count <= 0) continue
                                for (i in 0 until count.coerceAtMost(unassignedPlayers.size)) {
                                    val player = unassignedPlayers.removeFirst()
                                    map[player] = registeredTeam
                                }
                            }
                        }
                        while (unassignedPlayers.isNotEmpty()) {
                            val player = unassignedPlayers.removeFirst()
                            map[player] = filledTeams.minByOrNull { it.players.size }!!
                        }
                        map
                    }
                }
            }
        }
        for ((player, team) in teamAssignment) {
            team.players.add(player)
        }
        val regData = TeamRegistry.TeamRegistryData(registryData.maxTeamSize, registeredTeams.toSet())
        TeamRegistry.insertData(regData)
    }
}