package net.hitmc.lobbycountdown.events

import net.hitmc.lobbycountdown.LobbyCountdown
import org.bukkit.event.Event

abstract class LCEvent : Event() {

    abstract val lobbyCountdown: LobbyCountdown?
}