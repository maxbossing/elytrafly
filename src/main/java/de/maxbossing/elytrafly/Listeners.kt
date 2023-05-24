package de.maxbossing.elytrafly

import de.maxbossing.elytrafly.utils.Cube
import de.maxbossing.elytrafly.utils.MXItem
import de.maxbossing.elytrafly.utils.MXLocale
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object Listeners: Listener {

    private val mn = MiniMessage.miniMessage()

    private val chestPlates: MutableMap<Player, ItemStack> = mutableMapOf()

    private val elytra = MXItem(Material.ELYTRA, 1)
        .displayname(mn.deserialize("<rainbow>Elytra</rainbow>"))
        .flag(ItemFlag.HIDE_ATTRIBUTES)
        .enchant(Enchantment.BINDING_CURSE, 1)
        .build()

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {

        val player = event.player
        val location = player.location

        if (ElytraFly.mainConfig.getLocation("locations.1") == null || ElytraFly.mainConfig.getLocation("locations.2") == null) return

        val definedLocations = listOf(ElytraFly.mainConfig.getLocation("locations.1")!!, ElytraFly.mainConfig.getLocation("locations.2")!!)


        if (Cube(definedLocations[0], definedLocations[1]).containsLocation(location)) {

            if (player.inventory.chestplate != null && player.inventory.chestplate != elytra) {
                chestPlates[player] = player.inventory.chestplate!!
            }

            if (player.inventory.chestplate == elytra) {
                return
            }

            player.inventory.chestplate = elytra
            player.sendMessage(MXLocale.getComponent("elytra.recieved"))

        }

    }

    @EventHandler
    fun onGlideToggle(event: EntityToggleGlideEvent) {
        if (event.entity !is Player)return
        val player = event.entity as Player
        if(!player.isGliding)return
        if (player.inventory.chestplate != elytra)return
        player.inventory.chestplate = if(chestPlates.containsKey(player)) chestPlates[player] else ItemStack(Material.AIR)
        player.sendMessage(MXLocale.getComponent("elytra.removed"))
    }
}