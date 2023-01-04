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

package net.hitmc.minigameutils.team

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.jetbrains.annotations.Nullable

/**
 * A team consisting of a group of players which play for the same goal and are immune to each other.
 * A color is always assigned to a team.
 */
interface Team {

    companion object {

        @JvmStatic
        @Nullable
        fun getTeamOrNull(player: Player) = player.teamOrNull()

        @JvmStatic
        fun getTeam(player: Player) = player.team()
    }

    /**
     * The color of the team to represent it in chat and other places.
     * There is no guarantee that exactly this color is used. For example, [a NamedTextColor](NamedTextColor.nearestTo) may be used.
     */
    val color: TextColor

    /**
     * The name of the team. This is used to identify the team in the scoreboard or other places.
     */
    val name: String
        get() = (if (color is NamedTextColor) color else NamedTextColor.nearestTo(color)).examinableName()

    /**
     * A set containing all players belonging to this team.
     */
    val players: Set<Player>

    /**
     * Adds a player to this team.
     * @param player The player to add.
     */
    fun join(player: Player)

    /**
     * Removes a player from this team.
     * @param player The player to remove.
     */
    fun leave(player: Player)
}