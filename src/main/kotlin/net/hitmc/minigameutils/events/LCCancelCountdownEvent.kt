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

package net.hitmc.minigameutils.events

import net.hitmc.minigameutils.LobbyCountdown
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