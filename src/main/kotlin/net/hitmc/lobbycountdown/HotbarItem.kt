package net.hitmc.lobbycountdown

import com.github.stefvanschie.inventoryframework.gui.type.util.NamedGui
import org.bukkit.inventory.ItemStack

data class HotbarItem(val slot: Int, val itemStack: ItemStack, val gui: NamedGui)