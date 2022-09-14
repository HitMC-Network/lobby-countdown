package net.hitmc.lobbycountdown

import net.axay.kspigot.gui.openGUI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

class Listeners(private val lobbyCountdown: LobbyCountdown) : Listener {

    private val servicesManager = Bukkit.getServicesManager()

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val provider = servicesManager.getRegistration(LobbyCountdown::class.java)?.provider ?: return
        val inventory = event.player.inventory
        for (item in provider.items) {
            inventory.setItem(item.slot, item.itemStack)
        }
        event.player.teleportAsync(lobbyCountdown.spawnLocation)
        if (lobbyCountdown.startCountdown(lobbyCountdown, Bukkit.getOnlinePlayers().size)) {
            lobbyCountdown.start()
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val provider = servicesManager.getRegistration(LobbyCountdown::class.java)?.provider ?: return
        val hotbarItem = provider.items.firstOrNull { event.item == it.itemStack } ?: return
        event.player.openGUI(hotbarItem.gui)
    }
}