package me.aitor.skywars.arena.event;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.chest.ChestType;
import me.aitor.skywars.arena.chest.ChestTypeManager;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.title.Title;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

public class ArenaEvent {
   private EventType event;
   private String argument;
   private int seconds;
   private String title;
   private boolean executed = false;

   public ArenaEvent(EventType var1, String var2, int var3, String var4) {
      this.event = var1;
      this.argument = var2;
      this.seconds = var3;
      this.title = var4;
   }

   public EventType getEvent() {
      return this.event;
   }

   public void setEvent(EventType var1) {
      this.event = var1;
   }

   public String getArgument() {
      return this.argument;
   }

   public void setArgument(String var1) {
      this.argument = var1;
   }

   public int getSeconds() {
      return this.seconds;
   }

   public void setSeconds(int var1) {
      this.seconds = var1;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public boolean isExecuted() {
      return this.executed;
   }

   public void setExecuted(boolean var1) {
      this.executed = var1;
   }

   public void playEvent(Arena var1) {
      if (this.event == EventType.REFILL) {
         ChestType var2 = ChestTypeManager.getChestType(this.argument == null ? var1.getChest() : (this.argument.equalsIgnoreCase("selected") ? var1.getChest() : this.argument));
         Iterator var3 = var1.getChestFilled().iterator();

         while(var3.hasNext()) {
            Location var4 = (Location)var3.next();
            Block var5 = var4.getBlock();
            if (var5.getState() instanceof Chest) {
               Chest var6 = (Chest)var5.getState();
               var2.fillChest(var6.getInventory());
            }
         }

         var3 = var1.getAlivePlayer().iterator();

         while(var3.hasNext()) {
            SkyPlayer var7 = (SkyPlayer)var3.next();
            var7.sendMessage(SkyWars.getMessage(MSG.GAME_EVENT_REFILL));
            if (SkyWars.is18orHigher()) {
               Title var8 = new Title(SkyWars.getMessage(MSG.GAME_EVENT_REFILL), 20, 80, 20);
               var8.send(var7.getPlayer());
            }
         }
      }

      this.executed = true;
   }

   public String getTime() {
      int var1 = this.seconds / 3600;
      int var2 = this.seconds % 3600 / 60;
      int var3 = this.seconds % 60;
      String var4;
      if (var1 >= 1) {
         var4 = String.format("%02d:%02d:%02d", var1, var2, var3);
      } else {
         var4 = String.format("%02d:%02d", var2, var3);
      }

      return var4;
   }
}
