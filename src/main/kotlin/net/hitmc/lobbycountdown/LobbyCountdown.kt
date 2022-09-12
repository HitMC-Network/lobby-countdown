package net.hitmc.lobbycountdown

import mu.KotlinLogging
import net.hitmc.lobbycountdown.events.LCCancelCountdownEvent
import net.hitmc.lobbycountdown.events.LCCountdownStartEvent
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority
import org.bukkit.scheduler.BukkitTask

val logger = KotlinLogging.logger("LobbyCountdownLogger")

class LobbyCountdown(
    val plugin: Plugin,
    val items: List<HotbarItem>,
    val handler: CountdownHandler,
    val startValue: Int
) {

    var countdown: Int = startValue
        private set

    private lateinit var task: BukkitTask

    init {
        val servicesManager = Bukkit.getServicesManager()
        if (servicesManager.getRegistration(LobbyCountdown::class.java)?.provider != null) {
            error("There was already registered an instance of LobbyCount")
        }
        servicesManager.register(LobbyCountdown::class.java, this, plugin, ServicePriority.High)
        Bukkit.getPluginManager().registerEvents(Listeners(), plugin)
    }

    fun start() {
        val event = LCCountdownStartEvent()
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            return
        }
        task = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            handler.tick(countdown)
            if (countdown < 1) {
                cancel(LCCancelCountdownEvent.Reason.COUNTDOWN_ZERO)
                return@Runnable
            }
            countdown--
        }, 0L, 1L)
    }

    fun cancel(reason: LCCancelCountdownEvent.Reason = LCCancelCountdownEvent.Reason.CUSTOM) {
        val event = LCCancelCountdownEvent(reason)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            return
        }
        task.cancel()
    }
}

interface CountdownHandler {

    fun tick(value: Int): Boolean
}