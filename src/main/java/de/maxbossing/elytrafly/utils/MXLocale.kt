package de.maxbossing.elytrafly.utils

import de.maxbossing.elytrafly.ElytraFly
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import java.util.*

object MXLocale {

    private var locale: ResourceBundle = ResourceBundle.getBundle("locale", Locale.getDefault())

    /**
     * Get a string from the locale
     * @param node The node to get the string from
     * @return The string
      */
    fun getString(node: String): String {
        return locale.getString(node)
    }

    /**
     * Get a component from the locale
     * Components are MiniMessage strings
     * @param node The node to get the component from
     * @return The component
     */
    fun getComponent(node: String): Component {

        val msg = MiniMessage.miniMessage().deserialize(getString(node))

        return ElytraFly.prefix.append(msg)
    }
}