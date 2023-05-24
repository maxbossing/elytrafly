package de.maxbossing.elytrafly.utils

import de.maxbossing.elytrafly.ElytraFly
import de.maxbossing.elytrafly.enums.LogLevel

/**
 * Logger Object
 *
 * This Object is used to log messages to the console
 *
 * It utilizes the [ElytraFly] logger
 *
 * @see de.maxbossing.elytrafly.ElytraFly
 * @author Max
 * @since 1.0
 */
object ELogger {

    /**
     * Logs a message to the console
     *
     * [LogLevel.FATAL] Messages disable the Plugin if they are logged
     *
     * @param level The LogLevel of the message
     * @param message The message to log
     * @see LogLevel
     */
    fun log(level: LogLevel, message: String) {
        //Check if the Message should be shown based on the LogLevel
        if (level.ordinal < ElytraFly.loglevel.ordinal) return

        val instance = ElytraFly.instance

        // Log based on the LogLevel
        when (level) {
            LogLevel.DEBUG -> {
                instance.logger.info("[DEBUG] $message")
            }
            LogLevel.INFO -> {
                instance.logger.info(message)
            }
            LogLevel.WARNING -> {
                instance.logger.warning(message)
            }
            LogLevel.ERROR -> {
                instance.logger.severe(message)
            }
            LogLevel.FATAL -> {
                instance.logger.severe("[FATAL] $message")
                instance.logger.severe("[FATAL] Disabling Plugin...")
                instance.server.pluginManager.disablePlugin(instance)
            }
        }
    }

}
