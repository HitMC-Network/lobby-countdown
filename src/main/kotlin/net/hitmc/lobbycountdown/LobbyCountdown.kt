package net.hitmc.lobbycountdown

import net.axay.kspigot.chat.literalText
import net.axay.kspigot.extensions.bukkit.title
import net.hitmc.lobbycountdown.events.LCCancelCountdownEvent
import net.hitmc.lobbycountdown.events.LCCountdownStartEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority
import org.bukkit.scheduler.BukkitTask

class LobbyCountdown(
    val plugin: Plugin,
    val items: List<HotbarItem>,
    val countdownHandler: CountdownHandler? = null,
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
            countdownHandler?.tick(countdown)
            if (countdownHandler != null) {
                if (countdownHandler.messageValues.contains(countdown)) {
                    for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                        countdownHandler.message(countdown, onlinePlayer)
                    }
                }
                if (countdownHandler.titleValues.contains(countdown)) {
                    for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                        countdownHandler.title(countdown, onlinePlayer)
                    }
                }
            }
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

    val messageValues: MutableList<Int>
        get() = mutableListOf(60, 50, 40, 30, 20, 15, 10, 5, 4, 3, 2, 1)

    val titleValues: MutableList<Int>
        get() = mutableListOf(60, 30, 15, 5, 4, 3, 2, 1)

    fun tick(value: Int): Boolean = true

    fun message(value: Int, player: Player) =
        player.sendMessage(literalText("The game starts in $value seconds") { color = NamedTextColor.GREEN })

    fun title(value: Int, player: Player) = player.title(literalText(value.toString()) { color = NamedTextColor.GREEN })
}