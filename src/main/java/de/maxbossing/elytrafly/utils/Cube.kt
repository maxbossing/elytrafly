package de.maxbossing.elytrafly.utils

import org.bukkit.Location

data class Cube(val corner1: Location, val corner2: Location) {
    fun containsLocation(location: Location): Boolean {

        if (corner1.world != location.world || corner2.world != location.world) {
            return false // Player and cube are not in the same world
        }

        val minX = Math.min(corner1.x, corner2.x)
        val maxX = Math.max(corner1.x, corner2.x)
        val minY = Math.min(corner1.y, corner2.y)
        val maxY = Math.max(corner1.y, corner2.y)
        val minZ = Math.min(corner1.z, corner2.z)
        val maxZ = Math.max(corner1.z, corner2.z)

        return location.x >= minX && location.x <= maxX &&
                location.y >= minY && location.y <= maxY &&
                location.z >= minZ && location.z <= maxZ
    }
}