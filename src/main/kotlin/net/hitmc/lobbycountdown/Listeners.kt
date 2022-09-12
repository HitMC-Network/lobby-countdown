package net.hitmc.lobbycountdown

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

class Listeners : Listener {

    private val servicesManager = Bukkit.getServicesManager()

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val provider = servicesManager.getRegistration(LobbyCountdown::class.java)?.provider ?: return
        val inventory = event.player.inventory
        for (item in provider.items) {
            inventory.setItem(item.slot, item.itemStack)
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val provider = servicesManager.getRegistration(LobbyCountdown::class.java)?.provider ?: return
        val hotbarItem = provider.items.firstOrNull { event.item == it.itemStack } ?: return
        hotbarItem.gui.show(event.player)
    }
}