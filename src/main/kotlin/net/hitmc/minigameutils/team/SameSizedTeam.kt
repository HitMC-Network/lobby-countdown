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

import net.hitmc.minigameutils.team.assignment.SameSizedTeamAssignment
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import java.util.*

/**
 * A class representing a constellation where multiple teams exist, in the best case with the equal amount of players.
 */
class SameSizedTeam(override val color: TextColor) : Team {

    override val players: MutableSet<Player> = Collections.synchronizedSet(mutableSetOf())

    override val name: String
        get() = color.toString()

    companion object {

        @JvmStatic
        fun assignTeam() = SameSizedTeamAssignment.assignTeams()
    }

    override fun join(player: Player) {
        players.add(player)
    }

    override fun leave(player: Player) {
        players.remove(player)
    }
}