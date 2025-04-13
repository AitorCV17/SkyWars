package me.aitor.skywars;

import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class SkyWarsBungee extends Plugin implements Listener {
   public static SkyWarsBungee plugin;
   public static Configuration configuration;

   public void onEnable() {
      plugin = this;

      try {
         configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource(this, "config.yml"));
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      if (!configuration.contains("skywarslobbies_servers")) {
         ArrayList var1 = new ArrayList();
         var1.add("SkyWarsLobby1");
         var1.add("SkyWarsLobby2");
         configuration.set("skywarslobbies_servers", var1);
         saveFile(this, configuration, "config.yml");
      }

      PluginManager var3 = this.getProxy().getPluginManager();
      var3.registerListener(this, this);
      ProxyServer.getInstance().registerChannel("SkyWars-Sign-Send");
      ProxyServer.getInstance().registerChannel("SkyWars-Sign-Update");
   }

   public static SkyWarsBungee getPlugin() {
      return plugin;
   }

   public void sendUpdate(String var1) {
      ServerInfo var4;
      ByteArrayOutputStream var5;
      for(Iterator var2 = configuration.getStringList("skywarslobbies_servers").iterator(); var2.hasNext(); var4.sendData("SkyWars-Sign-Update", var5.toByteArray())) {
         String var3 = (String)var2.next();
         var4 = ProxyServer.getInstance().getServerInfo(var3);
         var5 = new ByteArrayOutputStream();
         DataOutputStream var6 = new DataOutputStream(var5);

         try {
            var6.writeUTF(var1);
         } catch (IOException var8) {
            ProxyServer.getInstance().getLogger().severe("¡Ocurrió un error de E/S!");
         }
      }

   }

   @EventHandler
   public void onQueryReceive(PluginMessageEvent var1) {
      if (var1.getTag().equalsIgnoreCase("SkyWars-Sign-Send")) {
         DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(var1.getData()));

         try {
            String var3 = var2.readUTF();
            this.sendUpdate(var3);
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

   }

   public static File loadResource(SkyWarsBungee var0, String var1) {
      File var2 = var0.getDataFolder();
      if (!var2.exists()) {
         var2.mkdir();
      }

      File var3 = new File(var2, var1);

      try {
         if (!var3.exists()) {
            var3.createNewFile();
            InputStream var4 = var0.getResourceAsStream(var1);
            Throwable var5 = null;

            try {
               FileOutputStream var6 = new FileOutputStream(var3);
               Throwable var7 = null;

               try {
                  ByteStreams.copy(var4, var6);
               } catch (Throwable var32) {
                  var7 = var32;
                  throw var32;
               } finally {
                  if (var6 != null) {
                     if (var7 != null) {
                        try {
                           var6.close();
                        } catch (Throwable var31) {
                           var7.addSuppressed(var31);
                        }
                     } else {
                        var6.close();
                     }
                  }

               }
            } catch (Throwable var34) {
               var5 = var34;
               throw var34;
            } finally {
               if (var4 != null) {
                  if (var5 != null) {
                     try {
                        var4.close();
                     } catch (Throwable var30) {
                        var5.addSuppressed(var30);
                     }
                  } else {
                     var4.close();
                  }
               }

            }
         }
      } catch (Exception var36) {
         var36.printStackTrace();
      }

      return var3;
   }

   public static void saveFile(SkyWarsBungee var0, Configuration var1, String var2) {
      try {
         ConfigurationProvider.getProvider(YamlConfiguration.class).save(var1, loadResource(var0, var2));
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }
}
