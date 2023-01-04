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

package net.hitmc.minigameutils.team

import org.bukkit.entity.Player

/**
 * A static instance for registering teams and their general properties.
 */
object TeamRegistry {

    /**
     * All currently registered teams. Any team not in this set won't be considered when using built-in assignment methods.
     */
    private val registeredTeams = mutableSetOf<Team>()

    /**
     * The maximum amount of players a team can have.
     */
    var maxTeamSize: Int = 1

    /**
     * Whether new teams can be registered or not. The TeamRegistry gets locked as soon as the built-in assignment methods are called.
     */
    var locked: Boolean = false
        private set

    /**
     * Registers a team to this registry.
     * This is not possible if the registry [is locked](locked)
     */
    fun registerTeam(team: Team) {
        if (locked) error("TeamRegistry is locked")
        registeredTeams.add(team)
    }

    /**
     * Retrieves a copy of the current registered teams.
     */
    fun registeredTeams() = registeredTeams.toSet()

    /**
     * Copies all data from this registry.
     */
    internal fun copyData(): TeamRegistryData {
        return TeamRegistryData(maxTeamSize, registeredTeams)
    }

    /**
     * Inserts registry data. This will [lock](locked) the registry.
     * @param data The data to insert.
     * @param clear Whether to clear the list of teams before insertion.
     */
    internal fun insertData(data: TeamRegistryData, clear: Boolean = false) {
        maxTeamSize = data.maxTeamSize
        if (clear) registeredTeams.clear()
        registeredTeams.addAll(data.registeredTeams)
        locked = true
    }

    /**
     * A data class for storing registry data (max team size and registered teams).
     * @param maxTeamSize The maximum team size.
     * @param registeredTeams The registered teams.
     */
    internal data class TeamRegistryData(val maxTeamSize: Int, val registeredTeams: Set<Team>)
}

/**
 * Retrieves the team of this player.
 * @return The team of this player or null if the player is not assigned to a team.
 */
fun Player.teamOrNull(): Team? = kotlin.runCatching { team() }.getOrNull()

/**
 * Retrieves the team of this player.
 * @return The team of this player.
 * @throws IllegalStateException If the player is not assigned to a team.
 */
fun Player.team(): Team {
    return TeamRegistry.registeredTeams().firstOrNull { it.players.contains(this) }
        ?: error("Player is not assigned to a team")
}