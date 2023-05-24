package de.maxbossing.elytrafly.commands

import de.maxbossing.elytrafly.ElytraFly
import de.maxbossing.elytrafly.utils.ELogger
import de.maxbossing.elytrafly.utils.MXLocale
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

object ElytraFlyCommand: CommandExecutor {

    val prefix = ElytraFly.prefix
    val mn = MiniMessage.miniMessage()
    val config = ElytraFly.mainConfig


    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(MXLocale.getComponent("command.error.player"))
            return true
        }

        if (args == null) return true

        if (args.size != 1) return true

        if (args[0] == "1") {
            if (sender.getTargetBlock(5) == null) {
                sender.sendMessage(MXLocale.getComponent("command.error.looking"))
                return true
            }
            config.set("locations.1", sender.getTargetBlock(5)!!.location)
            sender.sendMessage(MXLocale.getComponent("command.success").replaceText(
                TextReplacementConfig
                    .builder()
                    .match("%location%")
                    .replacement("1")
                    .build()
            ))

            ElytraFly.instance.saveConfig()

            return true
        }

        if (args[0] == "2") {
            if (sender.getTargetBlock(5) == null) {
                sender.sendMessage(MXLocale.getComponent("command.error.looking"))
                return true
            }
            config.set("locations.2", sender.getTargetBlock(5)!!.location)

            sender.sendMessage(MXLocale.getComponent("command.success").replaceText(
                TextReplacementConfig
                    .builder()
                    .match("%location%")
                    .replacement("2")
                    .build()
            ))

            ElytraFly.instance.saveConfig()
            return true
        }



        return false
    }
}

object ElytraFlyTabCompleter: TabCompleter {
    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?
    ): MutableList<String>? {
        if (p3 == null) return null

        if (p3.size == 1)  {
            return mutableListOf("1", "2")
        }

        return null
    }

}
