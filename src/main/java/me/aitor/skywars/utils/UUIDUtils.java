package me.aitor.skywars.utils;

import me.aitor.skywars.SkyWars;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UUIDUtils {
   public static JSONParser jsonParser = new JSONParser();

   public static UUID getUUID(String var0) {
      Iterator var1 = SkyWars.getPlugin().getServer().getOnlinePlayers().iterator();
      Player var2;
      do {
         if (!var1.hasNext()) {
            if (SkyWars.getPlugin().getServer().getOnlineMode()) {
               return getOnlineUUID(var0);
            }
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + var0).getBytes(Charset.forName("UTF-8")));
         }
         var2 = (Player)var1.next();
      } while(!var2.getName().equalsIgnoreCase(var0));
      return var2.getUniqueId();
   }

   public static UUID getOnlineUUID(String var0) {
      try {
         URL var1 = new URL("https://api.mojang.com/users/profiles/minecraft/" + var0);
         BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.openStream()));
         String var3;
         String var4;
         for(var3 = ""; (var4 = var2.readLine()) != null; var3 = var4 + "\n") {
         }
         var2.close();
         JSONObject var5 = (JSONObject)jsonParser.parse(var3);
         return UUID.fromString(((String)var5.get("id")).replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
      } catch (Exception var6) {
         return null;
      }
   }

   public static String getOnlineName(UUID var0) {
      try {
         String var2 = var0.toString().replace("-", "");
         URL var3 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + var2);
         BufferedReader var4 = new BufferedReader(new InputStreamReader(var3.openStream()));
         String var5;
         String var6;
         for(var5 = ""; (var6 = var4.readLine()) != null; var5 = var6 + "\n") {
         }
         var4.close();
         JSONObject var7 = (JSONObject)jsonParser.parse(var5);
         String var1 = (String)var7.get("name");
         return var1;
      } catch (Exception var8) {
         return null;
      }
   }
}
