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