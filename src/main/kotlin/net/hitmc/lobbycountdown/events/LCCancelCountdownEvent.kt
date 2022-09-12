package net.hitmc.lobbycountdown.events

import net.hitmc.lobbycountdown.LobbyCountdown
import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList

class LCCancelCountdownEvent(var reason: Reason) : LCEvent(), Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean = cancelled

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    companion object {
        @JvmStatic
        val handlerList: HandlerList = HandlerList()
    }

    override val lobbyCountdown: LobbyCountdown?
        get() = Bukkit.getServicesManager().getRegistration(LobbyCountdown::class.java)?.provider

    override fun getHandlers(): HandlerList = handlerList

    enum class Reason {
        COUNTDOWN_ZERO,
        CUSTOM;
    }
}