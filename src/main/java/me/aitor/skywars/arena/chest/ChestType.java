package me.aitor.skywars.arena.chest;

import me.aitor.skywars.SkyWars;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestType {
   private String name;
   private String title;
   private int slot;
   private Material item;
   private short item_data;
   private File config_file = null;
   private FileConfiguration config = null;
   private List<String> description = new ArrayList();
   private List<RandomItem> items = new ArrayList();

   public ChestType(String var1) {
      this.name = var1;
      this.config_file = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.chests + File.separator + var1 + ".yml");
      this.config = YamlConfiguration.loadConfiguration(this.config_file);
      ChestTypeManager.chesttypes.put(this.getName(), this);
   }

   public void setTitle(String var1) {
      this.title = ChatColor.translateAlternateColorCodes('&', var1);
   }

   public void setSlot(int var1) {
      this.slot = var1;
   }

   public void setDescription(List<String> var1) {
      this.description = var1;
   }

   public void setItem(String var1, short var2) {
      if (ChestTypeManager.isNumeric(var1)) {
         this.item = Material.getMaterial(Integer.parseInt(var1));
      } else {
         this.item = Material.getMaterial(var1.toUpperCase());
      }

      this.item_data = var2;
   }

   public void setItems(List<RandomItem> var1) {
      this.items = var1;
   }

   public void addDescription(String var1) {
      this.description.add(var1);
   }

   public void addItem(RandomItem var1) {
      this.items.add(var1);
   }

   public FileConfiguration getConfig() {
      return this.config;
   }

   public Material getItem() {
      return this.item;
   }

   public short getItemData() {
      return this.item_data;
   }

   public String getName() {
      return this.name;
   }

   public String getShortName() {
      return this.config.getString("name") == null ? this.name : this.config.getString("name");
   }

   public String getTitle() {
      return this.title;
   }

   public int getSlot() {
      return this.slot;
   }

   public List<String> getDescription() {
      return this.description;
   }

   public List<RandomItem> getItems() {
      return this.items;
   }

   private int countItems(Inventory var1) {
      int var2 = 0;
      ItemStack[] var3 = var1.getContents();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack var6 = var3[var5];
         if (var6 != null && var6.getType() != Material.AIR) {
            ++var2;
         }
      }

      return var2;
   }

   public void fillChest(Inventory var1) {
      if (var1.getHolder() instanceof DoubleChest) {
         this.fillChest(var1, true);
      } else {
         this.fillChest(var1, false);
      }

   }

   private void fillChest(Inventory var1, boolean var2) {
      var1.clear();
      if (this.getItems().size() > 0) {
         int var3 = SkyWars.getPlugin().getConfig().getInt("max_items_types_chest");

         while(this.countItems(var1) < var3) {
            Collections.shuffle(this.getItems(), new Random());
            Iterator var4 = this.getItems().iterator();

            while(var4.hasNext()) {
               RandomItem var5 = (RandomItem)var4.next();
               if (this.countItems(var1) < var3 && var5.hasChance()) {
                  var1.setItem((new Random()).nextInt(var1.getSize()), var5.getItem().build());
               }
            }
         }
      }

   }
}
