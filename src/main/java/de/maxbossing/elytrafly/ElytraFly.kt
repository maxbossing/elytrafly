package de.maxbossing.elytrafly

import de.maxbossing.elytrafly.commands.ElytraFlyCommand
import de.maxbossing.elytrafly.commands.ElytraFlyTabCompleter
import de.maxbossing.elytrafly.enums.LogLevel
import de.maxbossing.elytrafly.utils.ELogger
import de.maxbossing.elytrafly.utils.MXLocale
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class ElytraFly : JavaPlugin() {
    companion object {
        lateinit var instance: ElytraFly
        var elogger = ELogger

        lateinit var mainConfig: FileConfiguration

        lateinit var prefix: Component
        lateinit var loglevel: LogLevel
    }

    override fun onEnable() {
        // Instance
        instance = this

        // Config
        saveDefaultConfig()
        mainConfig = config

        // LogLevel
        loglevel = LogLevel.valueOf(mainConfig.getString("loglevel")!!.toUpperCase())
        elogger.log(LogLevel.DEBUG, "loglevel: $loglevel")

        // Prefix
        prefix = MiniMessage.miniMessage().deserialize(mainConfig.getString("prefix")!!)
        elogger.log(LogLevel.DEBUG, "prefix: ${MiniMessage.miniMessage().serialize(prefix)})}")

        // Command
        getCommand("elytrafly")!!.setExecutor(ElytraFlyCommand)
        getCommand("elytrafly")!!.tabCompleter = ElytraFlyTabCompleter

        // Events
        server.pluginManager.registerEvents(Listeners, this)
        elogger.log(LogLevel.DEBUG, "registered events")

        // Version Check
        version()

        elogger.log(LogLevel.INFO, MXLocale.getString("startup.finished").replace("%version%", description.version))
    }

    override fun onDisable() {
        saveConfig()
        elogger.log(LogLevel.INFO, MXLocale.getString("shutdown.finished").replace("%version%", description.version))
    }


    fun version() {
        fun getRemoteVersion(url: String): String? {
            try {
                val connection = URL(url).openConnection()
                val reader = BufferedReader(connection.getInputStream().reader())
                val remoteVersion = reader.readLine()
                reader.close()
                return remoteVersion
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
        fun compareVersions(localVersion: String, remoteVersion: String): Int {
            val localParts = localVersion.split(".")
            val remoteParts = remoteVersion.split(".")

            val maxLength = maxOf(localParts.size, remoteParts.size)
            for (i in 0 until maxLength) {
                val local = localParts.getOrNull(i)?.toIntOrNull() ?: 0
                val remote = remoteParts.getOrNull(i)?.toIntOrNull() ?: 0
                if (local < remote) {
                    return -1
                } else if (local > remote) {
                    return 1
                }
            }

            return 0
        }

        val url = "https://maxbossing.de/api/elytrafly/version.txt"

        val localversion = description.version

        val remoteVersion = getRemoteVersion(url)

        if (remoteVersion != null) {
            if (compareVersions(localversion, remoteVersion) == -1) {
                elogger.log(LogLevel.WARNING, MXLocale.getString("version.outdated").replace("%version%", remoteVersion))
            }else {
                elogger.log(LogLevel.INFO, MXLocale.getString("version.latest").replace("%version%", remoteVersion))
            }
        } else {
            elogger.log(LogLevel.ERROR, MXLocale.getString("version.error"))
        }
    }
}
