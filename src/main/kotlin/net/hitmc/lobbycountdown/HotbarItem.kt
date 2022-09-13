package net.hitmc.lobbycountdown

import net.axay.kspigot.gui.GUI
import org.bukkit.inventory.ItemStack

data class HotbarItem(val slot: Int, val itemStack: ItemStack, val gui: GUI<*>)