package me.aitor.skywars.utils.title;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class ActionBar {
   /** @deprecated */
   @Deprecated
   public static boolean DEBUG;
   private JSONObject json;

   public ActionBar(String var1) {
      Preconditions.checkNotNull(var1);
      this.json = Title.convert(var1);
   }

   public ActionBar(JSONObject var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(!var1.isEmpty());
      this.json = var1;
   }

   /** @deprecated */
   @Deprecated
   public static void send(Player var0, String var1) {
      (new ActionBar(var1)).send(var0);
   }

   /** @deprecated */
   @Deprecated
   public static void sendToAll(String var0) {
      (new ActionBar(var0)).sendToAll();
   }

   public void send(Player var1) {
      Preconditions.checkNotNull(var1);

      try {
         Class var2 = ServerPackage.MINECRAFT.getClass("IChatBaseComponent");
         Class var3 = ServerPackage.MINECRAFT.getClass("ChatMessageType");
         Object var4 = var1.getClass().getMethod("getHandle").invoke(var1);
         Object var5 = var4.getClass().getField("playerConnection").get(var4);
         Object var6 = ServerPackage.MINECRAFT.getClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke((Object)null, this.json.toString());
         Object var7 = var3.getMethod("valueOf", String.class).invoke((Object)null, "GAME_INFO");
         Object var8 = ServerPackage.MINECRAFT.getClass("PacketPlayOutChat").getConstructor(var2, var3).newInstance(var6, var7);
         var5.getClass().getMethod("sendPacket", ServerPackage.MINECRAFT.getClass("Packet")).invoke(var5, var8);
      } catch (Throwable var9) {
         throw new RuntimeException(var9);
      }
   }

   public void sendToAll() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player var2 = (Player)var1.next();
         this.send(var2);
      }

   }

   public void setText(String var1) {
      Preconditions.checkNotNull(var1);
      this.json = Title.convert(var1);
   }

   public void setJsonText(JSONObject var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(!var1.isEmpty());
      this.json = var1;
   }
}
