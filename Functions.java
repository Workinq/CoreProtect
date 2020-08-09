package net.coreprotect;

import net.coreprotect.consumer.Queue;
import net.coreprotect.model.BlockInfo;
import net.coreprotect.model.Config;
import net.coreprotect.worldedit.CoreProtectEditSessionEvent;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.*;

public class Functions extends Queue
{

    // One line methods
    public static Material getType(Block block) { return block.getType(); }
    public static int getMaterialId(Material material) { return block_id(material.name(), true); }
    public static int getArtId(Art art) { return art.getId(); }
    public static byte getData(Block block) { return block.getData(); }
    public static byte getData(BlockState block) { return block.getData().getData(); }
    public static byte getRawData(BlockState block) { return block.getRawData(); }
    public static void setData(Block block, byte data) { block.setData(data); }
    public static Enchantment getEnchantmentFromId(int id) { return Enchantment.getById(id); }
    public static void setTypeId1(Block block, int type) { block.setTypeId(type); }
    public static void setTypeId1(Block block, int type, boolean update) { block.setTypeId(type, update); }
    public static void setTypeIdAndData1(Block block, int type, byte data, boolean update) { block.setTypeIdAndData(type, data, update); }
    public static ItemStack newItemStack1(int type, int amount) { return new ItemStack(type, amount); }
    public static ItemStack newItemStack1(int type, int amount, short data) { return new ItemStack(type, amount, data); }
    public static void updateInventory(Player player) { player.updateInventory(); }
    public static void sendBlockChange(Player player, Location location, Material type, byte data) { player.sendBlockChange(location, type, data); }
    public static int getEntityId(EntityType type) { return Functions.getEntityId(type.name(), true); }
    public static boolean worldEditLoaded(Plugin plugin) { return plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null; }

    public static void setTypeAndData(Block block, Material type, byte data, boolean update)
    {
        // Apply
        block.setType(type, update);
        block.setData(data);
    }

    public static BlockState setRawData(BlockState block, byte data)
    {
        // Apply
        block.setRawData(data);

        // Return
        return block;
    }

    public static int checkConfig(World world, String option)
    {
        // Args
        int result = -1;

        // Verify
        if (Config.config.get(world.getName() + "-" + option) != null) result = Config.config.get(world.getName() + "-" + option);
        else if (Config.config.get(option) != null) result = Config.config.get(option);

        // Return
        return result;
    }

    public static boolean listContains(List<Material> list, Material value)
    {
        // Args
        boolean result = false;

        // Loop
        for (Material list_value : list)
        {
            // Verify
            if (list_value != value) continue;

            result = true;
            break;
        }

        // Return
        return result;
    }

    public static boolean newVersion(int[] old_version, int[] current_version)
    {
        // Args
        boolean result = false;

        // Verify
        if (old_version[0] < current_version[0]) result = true;
        else if (old_version[0] == current_version[0] && old_version[1] < current_version[1]) result = true;
        else if (old_version[0] == current_version[0] && old_version[1] == current_version[1] && old_version[2] < current_version[2]) result = true;

        // Return
        return result;
    }

    public static void loadWorldEdit(CoreProtect plugin)
    {
        try 
        {
        	// Args
            boolean validVersion = true;
            String version = plugin.getServer().getPluginManager().getPlugin("WorldEdit").getDescription().getVersion();
            int value = Integer.parseInt(version.split("-")[0].replaceAll("[^0-9]", ""));

            // Verify
            if (version.contains("."))
            {
                String[] version_split = version.replaceAll("[^0-9.]", "").split("\\.");
                double we_version = Double.parseDouble(version_split[0] + "." + version_split[1]);
                if (we_version > 0.0 && we_version < 6.0) validVersion = false;
            } 
            else if (version.contains("-") && value > 0 && value < 3122)
            {
                validVersion = false;
            }

            if (validVersion)
            {
                // Register
                CoreProtectEditSessionEvent.register();
            }
            else
            {
                // Inform
                System.out.println("[CoreProtect] Invalid WorldEdit version found.");
            }
        }
        catch (Exception ignored)
        {
        }
    }

    public static String block_name_lookup(int id)
    {
        // Args
        String name = "";

        // Verify
        if (Config.materials_reversed.get(id) != null) name = Config.materials_reversed.get(id);
        else if (BlockInfo.legacy_block_names.get(id) != null) name = BlockInfo.legacy_block_names.get(id);

        // Return
        return name;
    }

    public static String block_name_short(int id)
    {
        // Args
        String name = Functions.block_name_lookup(id);

        // Verify
        if (name.contains(":"))
        {
            String[] block_name_split = name.split(":");
            name = block_name_split[1];
        }

        // Return
        return name;
    }

    public static String nameFilter(String name, int data)
    {
        // Verify
        if (name.equals("stone"))
        {
            switch (data)
            {
                case 1: return "granite";
                case 2: return "polished_granite";
                case 3: return "diorite";
                case 4: return "polished_diorite";
                case 5: return "andesite";
                case 6: return "polished_andesite";
                default: return "stone";
            }
        }
        else if (name.equals("mob_spawner"))
        {
            switch (data)
            {
                case 1: return "zombie_spawner";
                case 2: return "skeleton_spawner";
                case 3: return "spider_spawner";
                case 4: return "cavespider_spawner";
                case 5: return "silverfish_spawner";
                case 6: return "blaze_spawner";
                case 7: return "creeper_spawner";
                case 8: return "giant_spawner";
                case 9: return "slime_spawner";
                case 10: return "ghast_spawner";
                case 11: return "pigzombie_spawner";
                case 12: return "enderman_spawner";
                case 13: return "magmacube_spawner";
                case 14: return "enderdragon_spawner";
                case 15: return "wither_spawner";
                case 16: return "bat_spawner";
                case 17: return "witch_spawner";
                case 18: return "endermite_spawner";
                case 19: return "guardian_spawner";
                case 20: return "sheep_spawner";
                case 21: return "cow_spawner";
                case 22: return "chicken_spawner";
                case 23: return "squid_spawner";
                case 24: return "wolf_spawner";
                case 25: return "mushroomcow_spawner";
                case 26: return "snowman_spawner";
                case 27: return "ocelot_spawner";
                case 28: return "irongolem_spawner";
                case 29: return "horse_spawner";
                case 30: return "rabbit_spawner";
                case 31: return "villager_spawner";
                default: return "pig_spawner";
            }
        }

        // Return
        return name;
    }

    public static int block_id(Material material) 
    {
        // Verify
        if (material == null) material = Material.AIR;

        // Return
        return Functions.block_id(material.name(), true);
    }

    public static Material getType(int id) 
    {
        // Args
        Material material = null;

        // Verify
        if (Config.materials_reversed.get(id) != null && id > 0)
        {
            String name = Config.materials_reversed.get(id);
            if (name.contains("minecraft:")) 
            {
                String[] block_name_split = name.split(":");
                name = block_name_split[1];
            }
            material = Material.getMaterial(name.toUpperCase());
        }

        // Return
        return material;
    }

    public static EntityType getEntityType(int id) 
    {
        // Args
        EntityType entitytype = null;

        // Verify
        if (Config.entities_reversed.get(id) != null)
        {
            String name = Config.entities_reversed.get(id);
            if (name.contains("minecraft:")) 
            {
                String[] block_name_split = name.split(":");
                name = block_name_split[1];
            }
            entitytype = EntityType.valueOf(name.toUpperCase());
        }

        // Return
        return entitytype;
    }

    public static String getArtName(int id) 
    {
        // Args
        String artname = "";

        // Verify
        if (Config.art_reversed.get(id) != null) artname = Config.art_reversed.get(id);

        // Return
        return artname;
    }

    public static String getEntityName(int id)
    {
        // Args
        String entityName = "";

        // Verify
        if (Config.entities_reversed.get(id) != null) entityName = Config.entities_reversed.get(id);

        // Return
        return entityName;
    }

    public static EntityType getEntityType(String name)
    {
        // Args
        EntityType type = null;
        name = name.toLowerCase().trim();

        // Verify
        if (name.contains("minecraft:")) name = name.split(":")[1];
        if (Config.entities.get(name) != null) type = EntityType.valueOf(name.toUpperCase());

        // Return
        return type;
    }

    public static Material getType(String name)
    {
        // Args
        Material material;
        name = name.toLowerCase().trim();

        // Verify
        if (!name.contains(":")) name = "minecraft:" + name;
        if (BlockInfo.legacy_block_ids.get(name) != null)
        {
            int legacy_id = BlockInfo.legacy_block_ids.get(name);
            material = Material.getMaterial(legacy_id);
        }
        else
        {
            if (name.contains("minecraft:")) name = name.split(":")[1];
            material = Material.getMaterial(name = name.toUpperCase());

            if (material == null)
            {
                List<String> stone_map = Arrays.asList("granite", "polished_granite", "diorite", "polished_diorite", "andesite", "polished_andesite");
                if (stone_map.contains(name.toLowerCase())) material = Material.getMaterial("STONE");
            }
        }

        // Return
        return material;
    }

    public static int getEntityId(String name, boolean internal)
    {
        // Args
        int id = -1;

        // Verify
        if (Config.entities.get(name = name.toLowerCase().trim()) != null)
        {
            id = Config.entities.get(name);
        }
        else if (internal)
        {
            int entityID = Config.entity_id + 1;
            Config.entities.put(name, entityID);
            Config.entities_reversed.put(entityID, name);
            Config.entity_id = entityID;
            Queue.queueEntityInsert(entityID, name);
            id = Config.entities.get(name);
        }

        // Return
        return id;
    }

    public static int getArtId(String name, boolean internal)
    {
        // Args
        int id = -1;

        // Verify
        if (Config.art.get(name = name.toLowerCase().trim()) != null)
        {
            id = Config.art.get(name);
        }
        else if (internal)
        {
            int artID = Config.art_id + 1;
            Config.art.put(name, artID);
            Config.art_reversed.put(artID, name);
            Config.art_id = artID;
            Queue.queueArtInsert(artID, name);
            id = Config.art.get(name);
        }

        // Return
        return id;
    }

    public static int block_id(String name, boolean internal)
    {
        // Args
        int id = -1;
        name = name.toLowerCase().trim();

        // Verify
        if (!name.contains(":")) name = "minecraft:" + name;
        if (Config.materials.get(name) != null)
        {
            id = Config.materials.get(name);
        }
        else if (internal)
        {
            int mid = Config.material_id + 1;
            Config.materials.put(name, mid);
            Config.materials_reversed.put(mid, name);
            Config.material_id = mid;
            Queue.queueMaterialInsert(mid, name);
            id = Config.materials.get(name);
        }

        // Return
        return id;
    }

    public static void messageOwner(String string)
    {
        // Verify
        if (string.startsWith("-"))
        {
            // Log
            CoreProtect.getInstance().getServer().getConsoleSender().sendMessage(string);

            // Loop
            for (Player player : CoreProtect.getInstance().getServer().getOnlinePlayers())
            {
                // Verify
                if (!player.isOp()) continue;

                // Inform
                player.sendMessage(string);
            }
        }
        else
        {
            // Log
            CoreProtect.getInstance().getServer().getConsoleSender().sendMessage("[CoreProtect] " + string);

            // Loop
            for (Player player : CoreProtect.getInstance().getServer().getOnlinePlayers())
            {
                // Verify
                if (!player.isOp()) continue;

                // Inform
                player.sendMessage("\u00a73CoreProtect \u00a7f- " + string);
            }
        }
    }

    public static void messageOwnerAndUser(CommandSender user, String string)
    {
        // Log
        CoreProtect.getInstance().getServer().getConsoleSender().sendMessage("[CoreProtect] " + string);

        // Loop
        for (Player player : CoreProtect.getInstance().getServer().getOnlinePlayers())
        {
            // Verify
            if (!player.isOp() || player.getName().equals(user.getName())) continue;

            // Inform
            player.sendMessage("\u00a73CoreProtect \u00a7f- " + string);
        }
        if (user instanceof Player && ((Player)user).isOnline())
        {
            // Inform
            user.sendMessage("\u00a73CoreProtect \u00a7f- " + string);
        }
    }

    public static int matchWorld(String name)
    {
        // Args
        int id = -1;
        try
        {
            String result = "";
            name = name.replaceFirst("#", "").toLowerCase().trim();

            // Loop
            for (World world : CoreProtect.getInstance().getServer().getWorlds())
            {
                String world_name = world.getName();

                // Verify
                if (world_name.toLowerCase().equals(name))
                {
                    result = world.getName();
                    break;
                }
                if (world_name.toLowerCase().endsWith(name))
                {
                    result = world.getName();
                    continue;
                }
                if (!world_name.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").endsWith(name)) continue;
                result = world.getName();
            }
            if (result.length() > 0) id = Functions.getWorldId(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }

    public static int getWorldId(String name)
    {
        // Args
        int id = -1;
        try
        {
            // Verify
            if (Config.worlds.get(name) == null)
            {
                int wid = Config.world_id + 1;
                Config.worlds.put(name, wid);
                Config.worlds_reversed.put(wid, name);
                Config.world_id = wid;
                Queue.queueWorldInsert(wid, name);
            }
            id = Config.worlds.get(name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Return
        return id;
    }

    public static String getWorldName(int id)
    {
        // Args
        String name = "";
        try
        {
            // Verify
            if (Config.worlds_reversed.get(id) != null) name = Config.worlds_reversed.get(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Return
        return name;
    }

    public static List<Object> processMeta(BlockState block)
    {
        // Args
        ArrayList<Object> meta = new ArrayList<>();
        try
        {
            // Verify
            if (block instanceof CommandBlock)
            {
                CommandBlock command_block = (CommandBlock)block;
                String command = command_block.getCommand();
                if (command.length() > 0) meta.add(command);
            }
            else if (block instanceof Banner)
            {
                Banner banner = (Banner)block;
                meta.add(banner.getBaseColor());
                List<Pattern> patterns = banner.getPatterns();
                for (Pattern pattern : patterns) meta.add(pattern.serialize());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (meta.size() == 0) meta = null;

        // Return
        return meta;
    }

    public static byte[] convertByteData(Object data)
    {
        // Args
        byte[] result = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos))
        {
            oos.writeObject(data);
            oos.flush();
            result = bos.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static int getHangingDelay(Map<String, Integer> hanging_delay, int row_wid, int row_x, int row_y, int row_z)
    {
        // Args
        String token = row_wid + "." + row_x + "." + row_y + "." + row_z;
        int delay = 0;

        // Verify
        if (hanging_delay.get(token) != null) delay = hanging_delay.get(token) + 1;

        // Put
        hanging_delay.put(token, delay);

        // Return
        return delay;
    }

    public static void iceBreakCheck(BlockState state, String user, Material type)
    {
        // Verify
        if (type == Material.ICE)
        {
            // Args
            int unixtimestamp = (int)(System.currentTimeMillis() / 1000L);
            int wid = Functions.getWorldId(state.getWorld().getName());

            // Put
            Config.lookup_cache.put(state.getX() + "." + state.getY() + "." + state.getZ() + "." + wid, new Object[]{unixtimestamp, user, Material.WATER});
        }
    }

    public static Block fallingSand(Block block, BlockState state, String player)
    {
        // Args
        Block bl = block;
        int timestamp = (int) (System.currentTimeMillis() / 1000L);
        Material type = block.getType();
        if (state != null) type = state.getType();
        int x = block.getX(), y = block.getY(), z = block.getZ();
        World world = block.getWorld();
        int wid = Functions.getWorldId(world.getName());
        int yc = y - 1;

        // Verify
        if (Config.falling_block_types.contains(type))
        {
            boolean bottomfound = false;

            // Loop
            while (!bottomfound)
            {
                if (yc < 0)
                {
                    bl = world.getBlockAt(x, yc + 1, z);
                    bottomfound = true;
                    continue;
                }
                Block block_down = world.getBlockAt(x, yc, z);
                Material down = block_down.getType();
                if (down != Material.AIR && down != Material.WATER && down != Material.STATIONARY_WATER && down != Material.LAVA && down != Material.STATIONARY_LAVA && down != Material.SNOW)
                {
                    bl = world.getBlockAt(x, yc + 1, z);
                    bottomfound = true;
                }
                else
                {
                    String cords = x + "." + yc + "." + z + "." + wid;
                    Object[] data = Config.lookup_cache.get(cords);
                    if (data != null && type.equals(data[2]))
                    {
                        bl = world.getBlockAt(x, yc + 1, z);
                        bottomfound = true;
                    }
                }
                --yc;
            }

            // Put
            Config.lookup_cache.put(x + "." + bl.getY() + "." + z + "." + wid, new Object[]{timestamp, player, type});
        }

        // Return
        return bl;
    }

    public static ItemStack[] get_container_state(ItemStack[] array)
    {
        // Args
        ItemStack[] result = array.clone();
        int counter = 0;

        // Loop
        for (ItemStack item : array)
        {
            ItemStack clone = null;

            // Verify
            if (item != null) clone = item.clone();

            result[counter] = clone;
            counter++;
        }

        // Return
        return result;
    }

    public static void combine_items(Material material, ItemStack[] items)
    {
        // Verify
        if (material == Material.ARMOR_STAND) return;

        try
        {
            // Args
            int counter_1 = 0;

            // Loop
            for (ItemStack item_1 : items)
            {
                int counter_2 = 0;

                // Nested Loop
                for (ItemStack item_2 : items)
                {
                    if (item_1 != null && item_2 != null && item_1.isSimilar(item_2) && counter_2 > counter_1)
                    {
                        int item_amount = item_1.getAmount() + item_2.getAmount();
                        item_1.setAmount(item_amount);
                        item_2.setAmount(0);
                    }
                    counter_2++;
                }
                counter_1++;
            }
        }
        catch (Exception ignored)
        {
        }
    }

    public static EntityEquipment getEntityEquipment(LivingEntity entity)
    {
        // Args
        EntityEquipment equipment = null;

        try
        {
            equipment = entity.getEquipment();
        }
        catch (Exception ignored)
        {
        }

        // Return
        return equipment;
    }

    public static Inventory getContainerInventory(BlockState block_state, boolean singleBlock)
    {
        // Args
        Inventory inventory = null;

        try
        {
            // Verify
            if (block_state instanceof InventoryHolder)
            {
                if (singleBlock)
                {
                    List<Material> chests = Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST);
                    if (chests.contains(block_state.getType())) inventory = ((Chest) block_state).getBlockInventory();
                }
                if (inventory == null) inventory = ((InventoryHolder) block_state).getInventory();
            }
        }
        catch (Exception ignored)
        {
        }

        // Return
        return inventory;
    }

    public static ItemStack[] getContainerContents(Material type, Object container, Location location)
    {
        // Args
        ItemStack[] contents = null;

        // Verify
        if (Functions.checkConfig(location.getWorld(), "item-transactions") == 1 && Config.containers.contains(type))
        {
            try
            {
                if (type == Material.ARMOR_STAND)
                {
                    LivingEntity entity = (LivingEntity)container;
                    EntityEquipment equipment = Functions.getEntityEquipment(entity);
                    if (equipment != null) contents = equipment.getArmorContents();
                }
                else
                {
                    Block block = (Block)container;
                    Inventory inventory = Functions.getContainerInventory(block.getState(), false);
                    if (inventory != null) contents = inventory.getContents();
                }
                if (contents != null) contents = Functions.get_container_state(contents);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        // Return
        return contents;
    }

    public static void updateBlock(BlockState block)
    {
        // Run
        CoreProtect.getInstance().getServer().getScheduler().runTask(CoreProtect.getInstance(), block::update);
    }

    public static void removeHanging(BlockState block, int delay)
    {
        // Run (delayed)
        CoreProtect.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(CoreProtect.getInstance(), () ->
        {
            try
            {
                // Loop
                for (Entity entity : block.getChunk().getEntities())
                {
                    // Args
                    Location location = entity.getLocation();

                    // Verify
                    if ( ! (entity instanceof ItemFrame) && !(entity instanceof Painting) || location.getBlockX() != block.getX() || location.getBlockY() != block.getY() || location.getBlockZ() != block.getZ()) continue;

                    // Remove
                    entity.remove();
                }
            }
            catch (Exception ignored)
            {
            }
        }, delay);
    }

    public static void spawnHanging(BlockState blockstate, Material row_type, int row_data, int delay)
    {
        CoreProtect.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(CoreProtect.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // Args
                    Block block = blockstate.getBlock();
                    int row_x = block.getX();
                    int row_y = block.getY();
                    int row_z = block.getZ();

                    // Loop
                    for (Entity entity : block.getChunk().getEntities())
                    {
                        if ((row_type == Material.ITEM_FRAME && entity instanceof ItemFrame) || (row_type == Material.PAINTING && entity instanceof Painting))
                        {
                            Location entityLocation = entity.getLocation();
                            if (entityLocation.getBlockX() == row_x && entityLocation.getBlockY() == row_y && entityLocation.getBlockZ() == row_z) {
                                entity.remove();
                                break;
                            }
                        }
                    }

                    // More args
                    int dx1 = row_x + 1;
                    int dx2 = row_x - 1;
                    int dz1 = row_z + 1;
                    int dz2 = row_z - 1;
                    Block c1 = block.getWorld().getBlockAt(dx1, row_y, row_z);
                    Block c2 = block.getWorld().getBlockAt(dx2, row_y, row_z);
                    Block c3 = block.getWorld().getBlockAt(row_x, row_y, dz1);
                    Block c4 = block.getWorld().getBlockAt(row_x, row_y, dz2);
                    BlockFace face_set = null;

                    // Verify
                    if (!Config.non_attachable.contains(c1.getType()))
                    {
                        face_set = BlockFace.WEST;
                        block = c1;
                    }
                    else if (!Config.non_attachable.contains(c2.getType()))
                    {
                        face_set = BlockFace.EAST;
                        block = c2;
                    }
                    else if (!Config.non_attachable.contains(c3.getType()))
                    {
                        face_set = BlockFace.NORTH;
                        block = c3;
                    }
                    else if (!Config.non_attachable.contains(c4.getType()))
                    {
                        face_set = BlockFace.SOUTH;
                        block = c4;
                    }

                    BlockFace face = null;
                    if (Config.non_solid_entity_blocks.contains(Functions.getType(block.getRelative(BlockFace.EAST)))) face = BlockFace.EAST;
                    else if (Config.non_solid_entity_blocks.contains(Functions.getType(block.getRelative(BlockFace.NORTH)))) face = BlockFace.NORTH;
                    else if (Config.non_solid_entity_blocks.contains(Functions.getType(block.getRelative(BlockFace.WEST)))) face = BlockFace.WEST;
                    else if (Config.non_solid_entity_blocks.contains(Functions.getType(block.getRelative(BlockFace.SOUTH)))) face = BlockFace.SOUTH;

                    if (face_set != null && face != null)
                    {
                        if (row_type == Material.PAINTING)
                        {
                            String art_name = Functions.getArtName(row_data);
                            Art painting = Art.getByName(art_name.toUpperCase());
                            int height = painting.getBlockHeight();
                            int width = painting.getBlockWidth();
                            int painting_x = row_x;
                            int painting_y = row_y;
                            int painting_z = row_z;
                            if (height != 1 || width != 1)
                            {
                                if (height > 1 && height != 3) --painting_y;
                                if (width > 1)
                                {
                                    if (face_set.equals(BlockFace.WEST)) --painting_z;
                                    else if (face_set.equals(BlockFace.SOUTH)) --painting_x;
                                }
                            }
                            Block spawn_block = block.getRelative(face);
                            Material current_type = spawn_block.getType();
                            int current_data = Functions.getData(spawn_block);
                            Functions.setTypeAndData(spawn_block, Material.AIR, (byte)0, true);
                            Painting hanging = null;
                            try
                            {
                                hanging = block.getWorld().spawn(spawn_block.getLocation(), Painting.class);
                            }
                            catch (Exception ignored) {}
                            if (hanging != null)
                            {
                                Functions.setTypeAndData(spawn_block, current_type, (byte)current_data, true);
                                hanging.teleport(block.getWorld().getBlockAt(painting_x, painting_y, painting_z).getLocation());
                                hanging.setFacingDirection(face_set, true);
                                hanging.setArt(painting, true);
                            }
                        }
                        else if (row_type == Material.ITEM_FRAME)
                        {
                            Block spawn_block = block.getRelative(face);
                            Material current_type = spawn_block.getType();
                            int current_data = Functions.getData(spawn_block);
                            Functions.setTypeAndData(spawn_block, Material.AIR, (byte)0, true);
                            ItemFrame hanging = null;
                            try
                            {
                                hanging = block.getWorld().spawn(spawn_block.getLocation(), ItemFrame.class);
                            }
                            catch (Exception ignored) {}
                            if (hanging != null)
                            {
                                Functions.setTypeAndData(spawn_block, current_type, (byte)current_data, true);
                                hanging.teleport(block.getWorld().getBlockAt(row_x, row_y, row_z).getLocation());
                                hanging.setFacingDirection(face_set, true);
                                Material row_data_material = Functions.getType(row_data);
                                if (row_data_material != null)
                                {
                                    ItemStack istack = new ItemStack(row_data_material, 1);
                                    hanging.setItem(istack);
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, delay);
    }

    public static void spawnEntity(BlockState block, EntityType type, List<Object> list)
    {
        // Run
        CoreProtect.getInstance().getServer().getScheduler().runTask(CoreProtect.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // Args
                    Location location = block.getLocation();
                    location.setX(location.getX() + 0.5);
                    location.setZ(location.getZ() + 0.5);
                    Entity entity = block.getLocation().getWorld().spawnEntity(location, type);
                    if (list.size() == 0) return;
                    List<Object> age = (List<Object>) list.get(0);
                    List<Object> tame = (List<Object>) list.get(1);
                    List<Object> data = (List<Object>) list.get(2);
                    if (list.size() >= 5)
                    {
                        entity.setCustomNameVisible((boolean) list.get(3));
                        entity.setCustomName((String) list.get(4));
                    }
                    int unixtimestamp = (int) (System.currentTimeMillis() / 1000L);
                    int wid = Functions.getWorldId(block.getWorld().getName());
                    String token = block.getX() + "." + block.getY() + "." + block.getZ() + "." + wid + "." + type.name();

                    // Put
                    Config.entity_cache.put(token, new Object[] { unixtimestamp, entity.getEntityId() });

                    // Verify - Ageable
                    if (entity instanceof Ageable)
                    {
                        int count = 0;
                        Ageable ageable = (Ageable)entity;
                        for (Object value : age)
                        {
                            if (count == 0)
                            {
                                int set = (int) value;
                                ageable.setAge(set);
                            }
                            else if (count == 1)
                            {
                                boolean set = (boolean) value;
                                ageable.setAgeLock(set);
                            }
                            else if (count == 2)
                            {
                                boolean set = (boolean) value;
                                if (set) ageable.setAdult();
                                else ageable.setBaby();
                            }
                            else if (count == 3)
                            {
                                boolean set = (boolean) value;
                                ageable.setBreed(set);
                            }
                            else if (count == 4)
                            {
                                double set = (double) value;
                                ageable.setMaxHealth(set);
                            }
                            count++;
                        }
                    }

                    // Verify - Tameable
                    if (entity instanceof Tameable)
                    {
                        int count = 0;
                        Tameable tameable = (Tameable) entity;
                        for (Object value : tame)
                        {
                            if (count == 0)
                            {
                                boolean set = (boolean)value;
                                tameable.setTamed(set);
                            }
                            else if (count == 1)
                            {
                                String set = (String) value;
                                if (set.length() > 0)
                                {
                                    Player owner = CoreProtect.getInstance().getServer().getPlayer(set);
                                    if (owner == null)
                                    {
                                        OfflinePlayer offline_player = CoreProtect.getInstance().getServer().getOfflinePlayer(set);
                                        if (offline_player != null) tameable.setOwner(offline_player);
                                    }
                                    else
                                    {
                                        tameable.setOwner(owner);
                                    }
                                }
                            }
                            count++;
                        }
                    }

                    int count = 0;
                    for (Object value : data)
                    {
                        if (entity instanceof Creeper)
                        {
                            Creeper creeper = (Creeper) entity;
                            if (count == 0) creeper.setPowered((boolean) value);
                        }
                        else if (entity instanceof Enderman)
                        {
                            Enderman enderman = (Enderman) entity;
                            if (count == 0) enderman.setCarriedMaterial(ItemStack.deserialize((Map<String, Object>) value).getData());
                        }
                        else if (entity instanceof IronGolem)
                        {
                            IronGolem irongolem = (IronGolem) entity;
                            if (count == 0) irongolem.setPlayerCreated((boolean) value);
                        }
                        else if (entity instanceof Ocelot)
                        {
                            Ocelot ocelot = (Ocelot) entity;
                            if (count == 0) ocelot.setCatType((Ocelot.Type) value);
                            else if (count == 1) ocelot.setSitting((boolean) value);
                        }
                        else if (entity instanceof Pig)
                        {
                            Pig pig = (Pig) entity;
                            if (count == 0) pig.setSaddle((boolean) value);
                        }
                        else if (entity instanceof Sheep)
                        {
                            Sheep sheep = (Sheep) entity;
                            if (count == 0) sheep.setSheared((boolean) value);
                            else if (count == 1) sheep.setColor((DyeColor) value);
                        }
                        else if (entity instanceof Skeleton)
                        {
                            Skeleton skeleton = (Skeleton) entity;
                            if (count == 0) skeleton.setSkeletonType((Skeleton.SkeletonType) value);
                        }
                        else if (entity instanceof Slime)
                        {
                            Slime slime = (Slime) entity;
                            if (count == 0) slime.setSize((int) value);
                        }
                        else if (entity instanceof Villager)
                        {
                            Villager villager = (Villager) entity;
                            if (count == 0) villager.setProfession((Villager.Profession) value);
                        }
                        else if (entity instanceof Wolf)
                        {
                            Wolf wolf = (Wolf) entity;
                            if (count == 0) wolf.setSitting((boolean) value);
                            else if (count == 1) wolf.setCollarColor((DyeColor) value);
                        }
                        else if (entity instanceof Zombie)
                        {
                            Zombie zombie = (Zombie) entity;
                            if (count == 0) zombie.setBaby((boolean) value);
                            else if (count == 1) zombie.setVillager((boolean) value);
                        }
                        else if (entity instanceof Horse)
                        {
                            Horse horse = (Horse) entity;
                            if (count == 0) horse.setCarryingChest((boolean) value);
                            else if (count == 1) horse.setColor((Horse.Color) value);
                            else if (count == 2) horse.setDomestication((int) value);
                            else if (count == 3) horse.setJumpStrength((double) value);
                            else if (count == 4) horse.setMaxDomestication((int) value);
                            else if (count == 5) horse.setStyle((Horse.Style) value);
                            else if (count == 6) horse.setVariant((Horse.Variant) value);
                            else if (count == 7 && value != null) horse.getInventory().setArmor(ItemStack.deserialize((Map<String, Object>) value));
                            else if (count == 8 && value != null) horse.getInventory().setSaddle(ItemStack.deserialize((Map<String, Object>) value));
                        }
                        count++;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String[] toStringArray(String input)
    {
        // Args
        int commaCount = input.replaceAll("[^,]", "").length();

        // Verify
        if (commaCount == 8)
        {
            String[] data = input.split(",");
            String action_time = data[0];
            String action_player = data[1];
            String action_cords = data[2];
            String[] data_cords = action_cords.split("\\.");
            String action_x = data_cords[0];
            String action_y = data_cords[1];
            String action_z = data_cords[2];
            String action_type = data[3];
            String action_data = data[4];
            String action_action = data[5];
            String action_rb = data[6];
            String action_wid = data[7].trim();

            // Return
            return new String[]{action_time, action_player, action_x, action_y, action_z, action_type, action_data, action_action, action_rb, action_wid};
        }

        // Return
        return null;
    }

    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(final Map<K, V> map)
    {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>(new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2)
            {
                int res = entry1.getValue().compareTo(entry2.getValue());
                return (res != 0) ? res : 1;
            }
        });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public static ItemMeta deserializeItemMeta(Class<? extends ItemMeta> itemMetaClass, Map<String, Object> args)
    {
        DelegateDeserialization delegate = itemMetaClass.getAnnotation(DelegateDeserialization.class);
        return (ItemMeta)ConfigurationSerialization.deserializeObject(args, delegate.value());
    }

    public static int getSpawnerType(EntityType type)
    {
        switch (type)
        {
            case ZOMBIE: return 1;
            case SKELETON: return 2;
            case SPIDER: return 3;
            case CAVE_SPIDER: return 4;
            case SILVERFISH: return 5;
            case BLAZE: return 6;
            case CREEPER: return 7;
            case GIANT: return 8;
            case SLIME: return 9;
            case GHAST: return 10;
            case PIG_ZOMBIE: return 11;
            case ENDERMAN: return 12;
            case MAGMA_CUBE: return 13;
            case ENDER_DRAGON: return 14;
            case WITHER: return 15;
            case BAT: return 16;
            case WITCH: return 17;
            case ENDERMITE: return 18;
            case GUARDIAN: return 19;
            case SHEEP: return 20;
            case COW: return 21;
            case CHICKEN: return 22;
            case SQUID: return 23;
            case WOLF: return 24;
            case MUSHROOM_COW: return 25;
            case SNOWMAN: return 26;
            case OCELOT: return 27;
            case IRON_GOLEM: return 28;
            case HORSE: return 29;
            case RABBIT: return 30;
            case VILLAGER: return 31;
            default: return 0;
        }
    }

    public static EntityType getSpawnerType(int type)
    {
        switch (type)
        {
            case 1: return EntityType.ZOMBIE;
            case 2: return EntityType.SKELETON;
            case 3: return EntityType.SPIDER;
            case 4: return EntityType.CAVE_SPIDER;
            case 5: return EntityType.SILVERFISH;
            case 6: return EntityType.BLAZE;
            case 7: return EntityType.CREEPER;
            case 8: return EntityType.GIANT;
            case 9: return EntityType.SLIME;
            case 10: return EntityType.GHAST;
            case 11: return EntityType.PIG_ZOMBIE;
            case 12: return EntityType.ENDERMAN;
            case 13: return EntityType.MAGMA_CUBE;
            case 14: return EntityType.ENDER_DRAGON;
            case 15: return EntityType.WITHER;
            case 16: return EntityType.BAT;
            case 17: return EntityType.WITCH;
            case 18: return EntityType.ENDERMITE;
            case 19: return EntityType.GUARDIAN;
            case 20: return EntityType.SHEEP;
            case 21: return EntityType.COW;
            case 22: return EntityType.CHICKEN;
            case 23: return EntityType.SQUID;
            case 24: return EntityType.WOLF;
            case 25: return EntityType.MUSHROOM_COW;
            case 26: return EntityType.SNOWMAN;
            case 27: return EntityType.OCELOT;
            case 28: return EntityType.IRON_GOLEM;
            case 29: return EntityType.HORSE;
            case 30: return EntityType.RABBIT;
            case 31: return EntityType.VILLAGER;
            default: return EntityType.PIG;
        }
    }

    public static int getSkullType(SkullType type)
    {
        switch (type)
        {
            case WITHER: return 1;
            case ZOMBIE: return 2;
            case PLAYER: return 3;
            case CREEPER: return 4;
            default: return 0;
        }
    }

    public static SkullType getSkullType(int type)
    {
        switch (type)
        {
            case 1: return SkullType.WITHER;
            case 2: return SkullType.ZOMBIE;
            case 3: return SkullType.PLAYER;
            case 4: return SkullType.CREEPER;
            default: return SkullType.SKELETON;
        }
    }

    public static int getBlockFace(BlockFace rotation)
    {
        switch (rotation)
        {
            case NORTH: return 0;
            case NORTH_NORTH_EAST: return 1;
            case NORTH_EAST: return 2;
            case EAST_NORTH_EAST: return 3;
            case EAST: return 4;
            case EAST_SOUTH_EAST: return 5;
            case SOUTH_EAST: return 6;
            case SOUTH_SOUTH_EAST: return 7;
            case SOUTH: return 8;
            case SOUTH_SOUTH_WEST: return 9;
            case SOUTH_WEST: return 10;
            case WEST_SOUTH_WEST: return 11;
            case WEST: return 12;
            case WEST_NORTH_WEST: return 13;
            case NORTH_WEST: return 14;
            case NORTH_NORTH_WEST: return 15;
        }
        throw new IllegalArgumentException("Invalid BlockFace rotation: " + rotation);
    }

    public static BlockFace getBlockFace(int rotation)
    {
        switch (rotation)
        {
            case 0: return BlockFace.NORTH;
            case 1: return BlockFace.NORTH_NORTH_EAST;
            case 2: return BlockFace.NORTH_EAST;
            case 3: return BlockFace.EAST_NORTH_EAST;
            case 4: return BlockFace.EAST;
            case 5: return BlockFace.EAST_SOUTH_EAST;
            case 6: return BlockFace.SOUTH_EAST;
            case 7: return BlockFace.SOUTH_SOUTH_EAST;
            case 8: return BlockFace.SOUTH;
            case 9: return BlockFace.SOUTH_SOUTH_WEST;
            case 10: return BlockFace.SOUTH_WEST;
            case 11: return BlockFace.WEST_SOUTH_WEST;
            case 12: return BlockFace.WEST;
            case 13: return BlockFace.WEST_NORTH_WEST;
            case 14: return BlockFace.NORTH_WEST;
            case 15: return BlockFace.NORTH_NORTH_WEST;
        }
        throw new AssertionError(rotation);
    }

}

