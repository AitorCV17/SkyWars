package me.aitor.skywars.utils.title;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class Title {
   /** @deprecated */
   @Deprecated
   public static boolean DEBUG;
   private JSONObject title;
   private JSONObject subtitle;
   private int fadeIn;
   private int fadeOut;
   private int stay;

   public Title(String var1, int var2, int var3, int var4) {
      String[] var5 = var1.split("\n");
      this.title = convert(var5[0]);
      this.subtitle = convert(var5.length > 1 ? var5[1] : "");
      this.fadeIn = var2;
      this.fadeOut = var4;
      this.stay = var3;
   }

   public Title(String var1, String var2, int var3, int var4, int var5) {
      this.title = convert(var1);
      this.subtitle = convert(var2);
      this.fadeIn = var3;
      this.fadeOut = var5;
      this.stay = var4;
   }

   public Title(JSONObject var1, JSONObject var2, int var3, int var4, int var5) {
      this.title = var1;
      this.subtitle = var2;
      this.fadeIn = var3;
      this.fadeOut = var4;
      this.stay = var5;
   }

   static JSONObject convert(String var0) {
      JSONObject var1 = new JSONObject();
      var1.put("text", var0);
      return var1;
   }

   public void send(Player var1) {
      Preconditions.checkNotNull(var1);

      try {
         Object var2 = var1.getClass().getMethod("getHandle").invoke(var1);
         Object var3 = var2.getClass().getField("playerConnection").get(var2);
         Class var4 = ServerPackage.MINECRAFT.getClass("PacketPlayOutTitle");
         Class var5 = ServerPackage.MINECRAFT.getClass("Packet");
         Class var6 = ServerPackage.MINECRAFT.getClass("IChatBaseComponent");
         Class var7 = ServerPackage.MINECRAFT.getClass("IChatBaseComponent$ChatSerializer");
         Class var8 = ServerPackage.MINECRAFT.getClass("PacketPlayOutTitle$EnumTitleAction");
         Object var9 = var4.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(this.fadeIn, this.stay, this.fadeOut);
         var3.getClass().getMethod("sendPacket", var5).invoke(var3, var9);
         Object var10;
         Object var11;
         if (this.title != null && !this.title.isEmpty()) {
            var10 = var7.getMethod("a", String.class).invoke((Object)null, this.title.toString());
            var11 = var4.getConstructor(var8, var6).newInstance(var8.getField("TITLE").get((Object)null), var10);
            var3.getClass().getMethod("sendPacket", var5).invoke(var3, var11);
         }

         if (this.subtitle != null && !this.subtitle.isEmpty()) {
            var10 = var7.getMethod("a", String.class).invoke((Object)null, this.subtitle.toString());
            var11 = var4.getConstructor(var8, var6).newInstance(var8.getField("SUBTITLE").get((Object)null), var10);
            var3.getClass().getMethod("sendPacket", var5).invoke(var3, var11);
         }

      } catch (Throwable var12) {
         throw new RuntimeException(var12);
      }
   }

   public void sendToAll() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player var2 = (Player)var1.next();
         this.send(var2);
      }

   }

   public JSONObject getTitle() {
      return this.title;
   }

   public void setTitle(String var1) {
      this.title = convert(var1);
   }

   public void setTitle(JSONObject var1) {
      this.title = var1;
   }

   public JSONObject getSubtitle() {
      return this.subtitle;
   }

   public void setSubtitle(String var1) {
      this.subtitle = convert(var1);
   }

   public void setSubtitle(JSONObject var1) {
      this.subtitle = var1;
   }

   public int getFadeIn() {
      return this.fadeIn;
   }

   public void setFadeIn(int var1) {
      this.fadeIn = var1;
   }

   public int getFadeOut() {
      return this.fadeOut;
   }

   public void setFadeOut(int var1) {
      this.fadeOut = var1;
   }

   public int getStay() {
      return this.stay;
   }

   public void setStay(int var1) {
      this.stay = var1;
   }
}
