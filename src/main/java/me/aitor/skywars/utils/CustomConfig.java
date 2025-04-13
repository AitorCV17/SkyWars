package me.aitor.skywars.utils;

import me.aitor.skywars.SkyWars;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomConfig {
   SkyWars plugin;
   private String name;
   private File file;
   private FileConfiguration fileConfig;

   public CustomConfig(String var1) {
      this.name = var1;
   }

   public CustomConfig(SkyWars var1) {
      this.plugin = var1;
   }

   public FileConfiguration getCustomConfig(CustomConfig var1) {
      if (var1.fileConfig == null) {
         this.reloadCustomConfig(var1);
      }
      return var1.fileConfig;
   }

   public void reloadCustomConfig(CustomConfig var1) {
      if (var1.fileConfig == null) {
         var1.file = new File(this.plugin.getDataFolder(), var1.name + ".properties");
      }
      var1.fileConfig = YamlConfiguration.loadConfiguration(var1.file);
      if (var1.fileConfig != null) {
         YamlConfiguration var2 = YamlConfiguration.loadConfiguration(var1.file);
         var1.fileConfig.setDefaults(var2);
      }
   }

   public void saveCustomConfig(CustomConfig var1) {
      if (var1.fileConfig != null && var1.file != null) {
         try {
            this.getCustomConfig(var1).save(var1.file);
         } catch (IOException var3) {
            this.plugin.getLogger().log(Level.SEVERE, "No se pudo guardar la configuración en " + var1.file, var3);
         }
      }
   }

   public void saveDefaultConfig(CustomConfig var1) {
      if (var1.file == null) {
         var1.file = new File(this.plugin.getDataFolder(), var1.name + ".properties");
      }
      if (!var1.file.exists()) {
         this.plugin.saveResource(var1.name + ".properties", false);
      }
   }
}
