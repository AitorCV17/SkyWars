package me.aitor.skywars.utils;

import me.aitor.skywars.SkyWars;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BungeeUtils {
   public static void teleToServer(Player var0, String var1, String var2) {
      if (!var1.equalsIgnoreCase("")) {
         var0.sendMessage(var1);
      }

      try {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         DataOutputStream var4 = new DataOutputStream(var3);
         var4.writeUTF("Connect");
         var4.writeUTF(var2);
         var0.sendPluginMessage(SkyWars.getPlugin(), "BungeeCord", var3.toByteArray());
         var3.close();
         var4.close();
      } catch (Exception var5) {
         var0.sendMessage(ChatColor.GOLD + "Error: No se pudo enviarte a " + ChatColor.RED + var2);
      }

   }
}
