package net.hitmc.lobbycountdown.team

import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

interface Team {

    val color: TextColor

    fun players(): Set<Player>

    fun join(player: Player)
}