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

package net.hitmc.minigameutils

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