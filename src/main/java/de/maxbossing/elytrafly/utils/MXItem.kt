package de.maxbossing.elytrafly.utils

import net.kyori.adventure.text.Component
import org.apache.commons.lang3.Validate
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.material.MaterialData
import org.bukkit.persistence.PersistentDataType
import java.lang.reflect.InvocationTargetException

/**
 * A utility class to create ItemStacks.
 *
 * @since 1.0.0
 * @author Max
 */


class MXItem {
    /*
    TODO: Add descriptive variable names
    TODO: organise into correlative blocks
     */
    private var item: ItemStack?

    /**
     * Returns the item meta of the item stack.
     *
     * @return the item meta of the item stack
     */
    var meta: ItemMeta? = null
        private set

    /**
     * Returns the material of the item stack.
     *
     * @return the material of the item stack
     */
    var material = Material.STONE
        private set

    /**
     * Returns the stack size of the item stack.
     *
     * @return the stack size of the item stack
     */
    var amount = 1
        private set

    /**
     * Deprecated. MaterialData is subject to removal instead.
     *
     * @return the material data of the item stack
     */
    @get:Deprecated("")
    var data: MaterialData? = null
        private set
    /**
     * Deprecated. Use [.getDurability] instead.
     *
     * @return the durability of the item stack
     */
    /**
     * Returns the durability of the item stack.
     *
     * @return the durability of the item stack
     */
    @get:Deprecated("")
    var durability: Short = 0
        private set
        /**
         * Returns the durability of the item stack.
         *
         * @return the durability of the item stack
         */
        get() = field
    private var enchantments: MutableMap<Enchantment, Int> = HashMap()

    /**
     * Returns the display name of the item stack.
     *
     * @return the display name of the item stack
     */
    var displayname: Component? = null
        private set
    private var lore: MutableList<Component>? = ArrayList()
    private var flags: MutableList<ItemFlag> = ArrayList()
    private var customModelData = 0

    private var key: NamespacedKey? = null
    private var value: String? = null

    /**
     * Returns whether the and symbol is replaced with the section symbol (§).
     *
     * @return true if the and symbol is replaced, false otherwise
     */
    var andSymbol = true
        private set
    private var unsafeStackSize = false

    /**
     * Constructs a new ItemStackBuilder with the specified material.
     *
     * If the material is null, it will default to Material.AIR.
     *
     * @param material the material for the ItemStack
     */
    constructor(material: Material?) {
        var material = material
        if (material == null) material = Material.AIR
        item = ItemStack(material)
        this.material = material
    }

    /**
     * Constructs a new ItemStackBuilder with the specified material and amount.
     *
     * If the material is null, it will default to Material.AIR. If the amount is greater
     * than the material's maximum stack size or less than or equal to zero, the amount
     * will default to 1 unless `unsafeStackSize` is set to true.
     *
     * @param material the material for the ItemStack
     * @param amount the amount of the material for the ItemStack
     */
    constructor(material: Material?, amount: Int) {
        var material = material
        var amount = amount
        if (material == null) material = Material.AIR
        if ((amount > material.maxStackSize || amount <= 0) && !unsafeStackSize) amount = 1
        this.amount = amount
        item = ItemStack(material, amount)
        this.material = material
    }

    /**
     * Constructs a new ItemStackBuilder with the specified material, amount, and display name.
     *
     * If the material is null, it will default to Material.AIR. If the amount is greater
     * than the material's maximum stack size or less than or equal to zero, the amount
     * will default to 1 unless `unsafeStackSize` is set to true. The display name must
     * not be null.
     *
     * @param material the material for the ItemStack
     * @param amount the amount of the material for the ItemStack
     * @param displayname the display name for the ItemStack
     * @throws NullPointerException if the display name is null
     */
    constructor(material: Material?, amount: Int, displayname: Component) {
        var material = material
        var amount = amount
        if (material == null) material = Material.AIR
        Validate.notNull(displayname, "The Displayname is null.")
        item = ItemStack(material, amount)
        this.material = material
        if ((amount > material.maxStackSize || amount <= 0) && !unsafeStackSize) amount = 1
        this.amount = amount
        this.displayname = displayname
    }

    /**
     * Constructs a new ItemStackBuilder with the specified material and display name.
     *
     * If the material is null, it will default to Material.AIR. The display name must
     * not be null.
     *
     * @param material the material for the ItemStack
     * @param displayname the display name for the ItemStack
     * @throws NullPointerException if the display name is null
     */
    constructor(material: Material?, displayname: Component) {
        var material = material
        if (material == null) material = Material.AIR
        Validate.notNull(displayname, "The Displayname is null.")
        item = ItemStack(material)
        this.material = material
        this.displayname = displayname
    }


    /**
     * Constructs a new ItemStackBuilder with the specified ItemStack.
     *
     * The ItemStack must not be null. The new ItemStackBuilder will have the same
     * material, amount, data, damage, enchantments, display name, lore, and flags
     * as the original ItemStack.
     *
     * @param item the ItemStack to create a new ItemStackBuilder from
     * @throws NullPointerException if the ItemStack is null
     */
    constructor(item: ItemStack) {
        Validate.notNull(item, "The Item is null.")
        this.item = item
        if (item.hasItemMeta()) meta = item.itemMeta
        material = item.type
        amount = item.amount
        data = item.data
        durability = item.durability
        enchantments = item.enchantments
        if (item.hasItemMeta()) displayname = Component.text(item.itemMeta.displayName)
        if (item.hasItemMeta()) {
            val list: MutableList<Component> = mutableListOf()
            item.itemMeta.lore?.forEach { list.add(Component.text(it)) }
            lore = list
        }
        if (item.hasItemMeta()) for (f in item.itemMeta.itemFlags) {
            flags.add(f)
        }
        if (item.hasItemMeta()) {
            customModelData = item.itemMeta.customModelData
        }
    }

    /**
     * Constructs a new ItemStackBuilder with the ItemStack from the specified path in the
     * given FileConfiguration.
     *
     * The FileConfiguration must not be null. If the path is invalid or the ItemStack
     * at the specified path is null, the ItemStackBuilder will default to a new
     * ItemStack with Material.AIR.
     *
     * @param cfg the FileConfiguration to retrieve the ItemStack from
     * @param path the path in the FileConfiguration to the ItemStack
     * @throws NullPointerException if the FileConfiguration is null
     */
    constructor(cfg: FileConfiguration, path: String?) : this(
        cfg.getItemStack(
            path!!
        )!!
    )

    /**
     * Constructs a new ItemStackBuilder with the properties of the specified ItemStackBuilder.
     *
     * The ItemStackBuilder must not be null.
     *
     * @param builder the ItemStackBuilder to copy the properties from
     * @throws NullPointerException if the ItemStackBuilder is null
     */
    @Deprecated(
        """This constructor is deprecated and will be removed in a future version.
                  Use the copy constructor instead."""
    )
    constructor(builder: MXItem) {
        Validate.notNull(builder, "The ItemBuilder is null.")
        item = builder.item
        meta = builder.meta
        material = builder.material
        amount = builder.amount
        durability = builder.durability
        data = builder.data
        durability = builder.durability
        enchantments = builder.enchantments
        displayname = builder.displayname
        lore = builder.lore
        flags = builder.flags
        customModelData = builder.customModelData
    }

    /**
     * Sets the amount of the material for the ItemStack.
     *
     * If the amount is greater than the material's maximum stack size or less than or
     * equal to zero, the amount will default to 1 unless `unsafeStackSize` is set to true.
     *
     * @param amount the amount of the material for the ItemStack
     * @return the current ItemStackBuilder
     */
    fun amount(amount: Int): MXItem {
        var amount = amount
        if ((amount > material.maxStackSize || amount <= 0) && !unsafeStackSize) amount = 1
        this.amount = amount
        return this
    }

    /**
     * Sets the data for the ItemStack.
     *
     * The data must not be null.
     *
     * @param data the data for the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the data is null
     */
    fun data(data: MaterialData): MXItem {
        Validate.notNull(data, "The Data is null.")
        this.data = data
        return this
    }

    /**
     * Sets the damage (durability) for the ItemStack.
     *
     * @param damage the damage for the ItemStack
     * @return the current ItemStackBuilder
     */
    @Deprecated(
        """This method is deprecated and will be removed in a future version.
                  Use {@link #durability(short)} instead."""
    )
    fun damage(damage: Short): MXItem {
        durability = damage
        return this
    }

    /**
     * Sets the durability (damage) for the ItemStack.
     *
     * @param damage the durability for the ItemStack
     * @return the current ItemStackBuilder
     */
    fun durability(damage: Short): MXItem {
        durability = damage
        return this
    }

    /**
     * Sets the material for the ItemStack.
     *
     * The material must not be null.
     *
     * @param material the material for the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the material is null
     */
    fun material(material: Material): MXItem {
        Validate.notNull(material, "The Material is null.")
        this.material = material
        return this
    }

    /**
     * Sets the meta data for the ItemStack.
     *
     * The meta data must not be null.
     *
     * @param meta the meta data for the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the meta data is null
     */
    fun meta(meta: ItemMeta): MXItem {
        Validate.notNull(meta, "The Meta is null.")
        this.meta = meta
        return this
    }

    /**
     * Adds an Enchantment to the ItemStack.
     *
     * @param enchant the Enchantment to add to the ItemStack
     * @param level the level of the Enchantment
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the Enchantment is null
     */
    fun enchant(enchant: Enchantment, level: Int): MXItem {
        Validate.notNull(enchant, "The Enchantment is null.")
        enchantments[enchant] = level
        return this
    }

    /**
     * Sets the [org.bukkit.enchantments.Enchantment]s for the ItemStack.
     *
     * The enchantments must not be null.
     *
     * @param enchantments the enchantments for the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the enchantments are null
     */
    fun enchant(enchantments: MutableMap<Enchantment, Int>): MXItem {
        Validate.notNull<Map<Enchantment, Int>>(enchantments, "enchantments cant be null")
        this.enchantments = enchantments
        return this
    }

    /**
     * Sets the display name for the ItemStack.
     *
     * The display name must not be null. If `andSymbol` is set to true, the display name
     * will be formatted
     *
     * @param displayname the display name for the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the display name is null
     */
    fun displayname(displayname: Component): MXItem {
        Validate.notNull(displayname, "The Displayname is null.")
        this.displayname = displayname
        return this
    }


    fun nameSpacedKey(key: NamespacedKey, value: String?): MXItem {
        Validate.notNull(key, "The Key is null.")
        Validate.notNull(value, "The Value is null.")
        this.key = key
        this.value = value
        return this
    }


    /**
     * Adds a line of lore to the ItemStack.
     *
     * The line of lore must not be null. If `andSymbol` is set to true, the line of lore
     * will be formatted.
     *
     * @param line the line of lore to add to the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the line of lore is null
     */
    fun lore(line: Component): MXItem {
        Validate.notNull(line, "The Line is null.")
        lore!!.add(line)
        return this
    }

    /**
     * Sets the lore for the ItemStack.
     *
     * The lore must not be null. If `andSymbol` is set to true, the lines of lore
     * will be formatted.
     *
     * @param lore the lore for the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the lore is null
     */
    fun lore(lore: MutableList<Component>): MXItem {
        Validate.notNull<List<Component>>(lore, "The Lores are null.")
        this.lore = lore
        return this
    }

    /**
     * Adds one or more Lines to the Lore of the ItemStack
     * @param lines One or more Strings for the ItemStack Lore
     * @return the current ItemStackBuilder
     */
    @Deprecated(
        """Use {@code ItemBuilder#lore}
      """
    )
    fun lores(vararg lines: String): MXItem {
        for (line in lines) {
            lore((if (andSymbol) ChatColor.translateAlternateColorCodes('&', line) else line))
        }
        return this
    }

    /**
     * Adds lines of lore to the ItemStack.
     *
     * The lines of lore must not be null. If `andSymbol` is set to true, the lines of lore
     * will be formatted.
     *
     * @param lines the lines of lore to add to the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the lines of lore are null
     */
    @Deprecated("use {@link #lore(String)} instead")
    fun lore(vararg lines: String): MXItem {
        for (line in lines) {
            this.lore!!.add(Component.text(line))
        }
        return this
    }

    /**
     * Sets a line of lore at a specific index in the ItemStack.
     *
     * The line of lore must not be null. If `andSymbol` is set to true, the line of lore
     * will be formatted.
     *
     * @param line the line of lore to set in the ItemStack
     * @param index the index in the lore list to set the line of lore
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the line of lore is null
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    fun lore(line: Component, index: Int): MXItem {
        Validate.notNull(line, "The Line is null.")
        lore!![index] = line
        return this
    }

    fun lore(vararg lines: Component): MXItem {
        for (line in lines) {
            lore(line)
        }
        return this
    }

    /**
     * Sets the Custom Model Data of the ItemStack. The param must be not null.
     * @param customModelData the CustomModelData the Item should have
     * @return the current itemStackBuilder
     * @throws NullPointerException is the param is null
     */
    fun customModelData(customModelData: Int): MXItem {
        Validate.notNull(customModelData, "The CustomModelData is null")
        this.customModelData = customModelData
        return this
    }

    /**
     * Adds an [org.bukkit.inventory.ItemFlag] to the ItemStack.
     *
     * The item flag must not be null.
     *
     * @param flag the item flag to add to the ItemStack
     * @return the current ItemStackBuilder
     * @throws NullPointerException if the item flag is null
     */
    fun flag(flag: ItemFlag): MXItem {
        Validate.notNull(flag, "The Flag is null.")
        flags.add(flag)
        return this
    }

    /**
     * Sets the flags for the item stack.
     *
     * @param flags the flags to set for the item stack
     * @return the current [MXItem] instance
     * @throws NullPointerException if the flags list is null
     */
    fun flag(flags: MutableList<ItemFlag>): MXItem {
        Validate.notNull<List<ItemFlag>>(flags, "The Flags are null.")
        this.flags = flags
        return this
    }

    /**
     * Sets the unbreakable status for the item stack.
     *
     * @param unbreakable whether the item stack should be unbreakable or not
     * @return the current [MXItem] instance
     */
    fun unbreakable(): MXItem {
        meta!!.isUnbreakable = true
        return this
    }

    /**
     * Returns an Unsafe instance for the item stack builder, which contains the nbt methods.
     *
     * @return an Unsafe instance for the item stack builder
     */
    fun unsafe(): Unsafe {
        return Unsafe(this)
    }

    /**
     * Deprecated. Use [.replaceAndSymbol] instead.
     *
     * @return the current [MXItem] instance
     */
    @Deprecated("")
    fun replaceAndSymbol(): MXItem {
        replaceAndSymbol(!andSymbol)
        return this
    }

    /**
     * Sets whether to replace the and symbol with the section symbol (§).
     *
     * @param replace whether to replace the and symbol or not
     * @return the current [MXItem] instance
     */
    fun replaceAndSymbol(replace: Boolean): MXItem {
        andSymbol = replace
        return this
    }

    /**
     * Toggles the replacement of the and symbol with the section symbol (§).
     *
     * @return the current [MXItem] instance
     */
    fun toggleReplaceAndSymbol(): MXItem {
        replaceAndSymbol(!andSymbol)
        return this
    }

    /**
     * Enables or disables the use of stack sizes larger than the maximum stack size for the item.
     *
     * @param allow whether to allow unsafe stack sizes or not
     * @return the current [MXItem] instance
     */
    fun unsafeStackSize(allow: Boolean): MXItem {
        unsafeStackSize = allow
        return this
    }

    /**
     * Toggles the use of stack sizes larger than the maximum stack size for the item.
     *
     * @return the current [MXItem] instance
     */
    fun toggleUnsafeStackSize(): MXItem {
        unsafeStackSize(!unsafeStackSize)
        return this
    }

    /**
     * Returns the enchantments and their levels for the item stack.
     *
     * @return the enchantments and their levels for the item stack
     */
    fun getEnchantments(): Map<Enchantment, Int> {
        return enchantments
    }

    val lores: List<Component>?
        /**
         * Returns the lore lines for the item stack.
         *
         * @return the lore lines for the item stack
         */
        get() = lore

    /**
     * Returns the flags for the item stack.
     *
     * @return the flags for the item stack
     */
    fun getFlags(): List<ItemFlag> {
        return flags
    }

    /**
     * Deprecated. Use [.getLores] instead.
     *
     * @return the lore lines for the item stack
     */
    @Deprecated("")
    fun getLore(): List<Component>? {
        return lore
    }

    /**
     * Saves the item stack to a configuration file at the specified path.
     *
     * @param cfg the configuration file to save to
     * @param path the path to save the item stack at
     * @return the current [MXItem] instance
     */
    fun toConfig(cfg: FileConfiguration, path: String?): MXItem {
        cfg[path!!] = build()
        return this
    }

    /**
     * Loads the item stack from a configuration file at the specified path.
     *
     * @param cfg the configuration file to load from
     * @param path the path to load the item stack from
     * @return a new [MXItem] instance with the loaded item stack
     */
    fun fromConfig(cfg: FileConfiguration, path: String?): MXItem {
        return MXItem(cfg, path)
    }

    /**
     * Builds the item stack using the current builder's properties.
     *
     * @return the built item stack
     */
    fun build(): ItemStack? {
        item!!.type = material
        item!!.amount = amount
        item!!.durability = durability
        meta = item!!.itemMeta
        if (data != null) {
            item!!.data = data
        }
        if (enchantments.size > 0) {
            item!!.addUnsafeEnchantments(enchantments)
        }
        if (displayname != null) {
            meta!!.displayName(displayname)
        }
        if (lore!!.size > 0) {
            meta!!.lore(lore)
        }
        if (flags.size > 0) {
            for (f in flags) {
                meta!!.addItemFlags(f)
            }
        }
        if (key != null) {
            item!!.itemMeta.persistentDataContainer.set(key!!, PersistentDataType.STRING, this.value!!)
        }
        item!!.setItemMeta(meta)
        return item
    }

    /**
     * A class containing sensitive NMS code for manipulating NBT data on an item stack.
     * Use with caution as this code is highly sensitive and can break the ItemStackBuilder if used improperly.
     */
    inner class Unsafe
    /**
     * Initializes the Unsafe class with an ItemStackBuilder.
     *
     * @param builder the ItemStackBuilder to initialize the Unsafe class with
     */(
        /** Do not access using this Field  */
        protected val builder: MXItem
    ) {
        /** Do not access using this Field */
        protected val utils: ReflectionUtils = ReflectionUtils()

        /**
         * Sets a NBT tag string into the NBT tag compound of the item.
         *
         * @param key the name to save the NBT tag under
         * @param value the value to save
         * @return the current Unsafe instance
         */
        fun setString(key: String?, value: String?): Unsafe {
            builder.item = utils.setString(builder.item, key, value)
            return this
        }

        /**
         * Returns the string saved under the specified key.
         *
         * @param key the key to retrieve the string from
         * @return the string saved under the key, or null if not found
         */
        fun getString(key: String?): String? {
            return utils.getString(builder.item, key)
        }

        /**
         * Sets a NBT tag integer into the NBT tag compound of the item.
         *
         * @param key the name to save the NBT tag under
         * @param value the value to save
         * @return the current Unsafe instance
         */
        fun setInt(key: String?, value: Int): Unsafe {
            builder.item = utils.setInt(builder.item, key, value)
            return this
        }

        /**
         * returns the Integer under the current Key
         * @param key the nbt tag name
         * @return int
         */
        fun getInt(key: String?): Int {
            return utils.getInt(builder.item, key)
        }

        /**
         * Sets a NBT tag double into the NBT tag compound of the item.
         *
         * @param key the name to save the NBT tag under
         * @param value the value to save
         * @return the current Unsafe instance
         */
        fun setDouble(key: String?, value: Double): Unsafe {
            builder.item = utils.setDouble(builder.item, key, value)
            return this
        }

        /**
         * Returns the double saved under the specified key.
         *
         * @param key the key to retrieve the double from
         * @return the double saved under the key, or 0 if not found
         */
        fun getDouble(key: String?): Double {
            return utils.getDouble(builder.item, key)
        }

        /**
         * Sets a NBT tag boolean into the NBT tag compound of the item.
         *
         * @param key the name to save the NBT tag under
         * @param value the value to save
         * @return the current Unsafe instance
         */
        fun setBoolean(key: String?, value: Boolean): Unsafe {
            builder.item = utils.setBoolean(builder.item, key, value)
            return this
        }

        /**
         * Returns the boolean saved under the specified key.
         *
         * @param key the key to retrieve the boolean from
         * @return the boolean saved under the key, or false if not found
         */
        fun getBoolean(key: String?): Boolean {
            return utils.getBoolean(builder.item, key)
        }

        /**
         * Returns whether the item has an NBT tag saved under the specified key.
         *
         * @param key the key to check
         * @return true if the item has an NBT tag saved under the key, false otherwise
         */
        fun containsKey(key: String?): Boolean {
            return utils.hasKey(builder.item, key)
        }

        /**
         * Returns the ItemStackBuilder instance associated with this Unsafe instance.
         *
         * @return the ItemStackBuilder instance
         */
        fun builder(): MXItem {
            return builder
        }

        /** This Class contains highly sensitive NMS Code that should not be touched unless you want to break the ItemBuilder  */
        inner class ReflectionUtils {
            /**
             * Returns a String saved in the nbt data of the item at the given key
             * If the key does not exist, it will return null
             *
             * @param item item
             * @param key key
             * @return the String if successfull, null if not
             */
            fun getString(item: ItemStack?, key: String?): String? {
                var compound = getNBTTagCompound(getItemAsNMSStack(item))
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    return compound!!.javaClass.getMethod("getString", String::class.java)
                        .invoke(compound, key) as String
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return null
            }

            /**
             * Sets a String at the nbt key of the nbt data of item
             *
             * @param item item
             * @param key key
             * @param value value
             * @return the ItemStack
             */
            fun setString(item: ItemStack?, key: String?, value: String?): ItemStack? {
                var nmsItem = getItemAsNMSStack(item)
                var compound = getNBTTagCompound(nmsItem)
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    compound!!.javaClass.getMethod("setString", String::class.java, String::class.java)
                        .invoke(compound, key, value)
                    nmsItem = setNBTTag(compound, nmsItem)
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return getItemAsBukkitStack(nmsItem)
            }

            /**
             * Returns an int saved in the nbt data of the item at the given key
             * If the key does not exist, it will return -1
             * @param item item
             * @param key key
             * @return int if success, -1 if key does not exist
             */
            fun getInt(item: ItemStack?, key: String?): Int {
                var compound = getNBTTagCompound(getItemAsNMSStack(item))
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    return compound!!.javaClass.getMethod("getInt", String::class.java).invoke(compound, key) as Int
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return -1
            }

            /**
             * Sets an int at the nbt key of the nbt data of the ItemStack
             * @param item item
             * @param key key
             * @param value value
             * @return the ItemStack
             */
            fun setInt(item: ItemStack?, key: String?, value: Int): ItemStack? {
                var nmsItem = getItemAsNMSStack(item)
                var compound = getNBTTagCompound(nmsItem)
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    compound!!.javaClass.getMethod("setInt", String::class.java, Int::class.java)
                        .invoke(compound, key, value)
                    nmsItem = setNBTTag(compound, nmsItem)
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return getItemAsBukkitStack(nmsItem)
            }

            /**
             * Returns a Double saved in the nbt data of item at given nbt key
             * Returns NaN if key does not exist
             * @param item item
             * @param key key
             * @return Double at Success, NaN if key does not exist
             */
            fun getDouble(item: ItemStack?, key: String?): Double {
                var compound = getNBTTagCompound(getItemAsNMSStack(item))
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    return compound!!.javaClass.getMethod("getDouble", String::class.java)
                        .invoke(compound, key) as Double
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return Double.NaN
            }

            /**
             * sets a double at the nbt key of the nbt data of the item
             * @param item item
             * @param key key
             * @param value value
             * @return the ItemStack
             */
            fun setDouble(item: ItemStack?, key: String?, value: Double): ItemStack? {
                var nmsItem = getItemAsNMSStack(item)
                var compound = getNBTTagCompound(nmsItem)
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    compound!!.javaClass.getMethod("setDouble", String::class.java, Double::class.java)
                        .invoke(compound, key, value)
                    nmsItem = setNBTTag(compound, nmsItem)
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return getItemAsBukkitStack(nmsItem)
            }

            /**
             * Gets a boolean at given key of nbt data of given item.
             * returns false if key is not existing
             * @param item item
             * @param key key
             * @return boolean at success, false if key does not exist
             */
            fun getBoolean(item: ItemStack?, key: String?): Boolean {
                var compound = getNBTTagCompound(getItemAsNMSStack(item))
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    return compound!!.javaClass.getMethod("getBoolean", String::class.java)
                        .invoke(compound, key) as Boolean
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return false
            }

            /**
             * sets a boolean at nbt key of nbt data of item
             * @param item item
             * @param key key
             * @param value value
             * @return the ItemStack
             */
            fun setBoolean(item: ItemStack?, key: String?, value: Boolean): ItemStack? {
                var nmsItem = getItemAsNMSStack(item)
                var compound = getNBTTagCompound(nmsItem)
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    compound!!.javaClass.getMethod("setBoolean", String::class.java, Boolean::class.java)
                        .invoke(compound, key, value)
                    nmsItem = setNBTTag(compound, nmsItem)
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return getItemAsBukkitStack(nmsItem)
            }

            /**
             * checks if items nbt data contains a key
             * @param item item
             * @param key key
             * @return true if key does exist, false if not
             */
            fun hasKey(item: ItemStack?, key: String?): Boolean {
                var compound = getNBTTagCompound(getItemAsNMSStack(item))
                if (compound == null) {
                    compound = newNBTTagCompound
                }
                try {
                    return compound!!.javaClass.getMethod("hasKey", String::class.java).invoke(compound, key) as Boolean
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                }
                return false
            }

            val newNBTTagCompound: Any?
                /**
                 * get New NBT Tag Compound
                 * @return Object
                 */
                get() {
                    val ver = Bukkit.getServer().javaClass.getPackage().name.split(".".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()[3]
                    try {
                        return Class.forName("net.minecraft.server.$ver.NBTTagCompound").newInstance()
                    } catch (ex: ClassNotFoundException) {
                        ex.printStackTrace()
                    } catch (ex: IllegalAccessException) {
                        ex.printStackTrace()
                    } catch (ex: InstantiationException) {
                        ex.printStackTrace()
                    }
                    return null
                }

            /**
             * set NBT Tag of Item
             * @param tag tag
             * @param item item
             * @return item at success, null if not
             */
            fun setNBTTag(tag: Any?, item: Any?): Any? {
                try {
                    item!!.javaClass.getMethod("setTag", item.javaClass).invoke(item, tag)
                    return item
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return null
            }

            /**
             * get NBT Tag Compound
             * @param nmsStack nmsStack
             * @return Object
             */
            fun getNBTTagCompound(nmsStack: Any?): Any? {
                try {
                    return nmsStack!!.javaClass.getMethod("getTag").invoke(nmsStack)
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                }
                return null
            }

            /**
             * get ItemStack as nmsStack object
             * @param item item
             * @return object
             */
            fun getItemAsNMSStack(item: ItemStack?): Any? {
                try {
                    val m = craftItemStackClass!!.getMethod("asNMSCopy", ItemStack::class.java)
                    return m.invoke(craftItemStackClass, item)
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                }
                return null
            }

            /**
             * get nmsStack object as Bukkit Stack
             * @param nmsStack nmsStack
             * @return ItemStack
             */
            fun getItemAsBukkitStack(nmsStack: Any?): ItemStack? {
                try {
                    val m = craftItemStackClass!!.getMethod("asCraftMirror", nmsStack!!.javaClass)
                    return m.invoke(craftItemStackClass, nmsStack) as ItemStack
                } catch (ex: NoSuchMethodException) {
                    ex.printStackTrace()
                } catch (ex: InvocationTargetException) {
                    ex.printStackTrace()
                } catch (ex: IllegalAccessException) {
                    ex.printStackTrace()
                }
                return null
            }

            val craftItemStackClass: Class<*>?
                /**
                 * get Craft Item Stack Class
                 * @return Class
                 */
                get() {
                    val ver = Bukkit.getServer().javaClass.getPackage().name.split(".".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()[3]
                    try {
                        return Class.forName("org.bukkit.craftbukkit.$ver.inventory.CraftItemStack")
                    } catch (ex: ClassNotFoundException) {
                        ex.printStackTrace()
                    }
                    return null
                }
        }
    }

    companion object {
        /**
         * Saves the item stack to a configuration file at the specified path.
         *
         * @param cfg the configuration file to save to
         * @param path the path to save the item stack at
         * @param builder the item stack builder to save
         */
        fun toConfig(cfg: FileConfiguration, path: String?, builder: MXItem) {
            cfg[path!!] = builder.build()
        }
    }
}
