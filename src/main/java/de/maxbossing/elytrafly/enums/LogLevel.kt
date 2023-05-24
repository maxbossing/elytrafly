package de.maxbossing.elytrafly.enums


/**
 * LogLevel
 *
 * This Enum is used to define the LogLevel of a message
 *
 * This defines the importance of a message, the higher the LogLevel the more important the message is
 *
 * Messages will be differently displayed based on the LogLevel (Errors Red, Warnings Yellow...)
 *
 * [LogLevel.FATAL] Messages disable the Plugin if they are logged
 *
 * Also defines if a Message is shown by checking the ordinal of the LogLevel and comparing it to the ordinal of the [de.maxbossing.elytrafly.ElytraFly.loglevel]
 *
 * If the ordinal of the LogLevel is lower than the ordinal of the [de.maxbossing.elytrafly.ElytraFly.loglevel] the message will not be shown
 *
 * It is used in the [de.maxbossing.elytrafly.utils.ELogger] Object
 *
 * @since 1.0
 * @author Max
 * @see de.maxbossing.elytrafly.enums.LogLevel
 */

enum class LogLevel {
    /**
     * Debug LogLevel
     *
     * Used for Debugging
     */
    DEBUG,
    /**
     * Info LogLevel
     *
     * Used for general Information
     */
    INFO,

    /**
     * Warn LogLevel
     *
     * Used for Warnings
     */
    WARNING,

    /**
     * Error LogLevel
     *
     * Used for Errors
     */
    ERROR,

    /**
     * Fatal LogLevel
     *
     * Used for Fatal Errors
     *
     * Will shutdown the Plugin if a message is logged with this LogLevel
     */
    FATAL
}