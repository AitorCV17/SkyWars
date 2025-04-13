package me.aitor.skywars.menus.arena;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.menus.Menu;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuVoteTime extends Menu {
   public MenuVoteTime(Player var1) {
      super(var1, "voteTime", SkyWars.getMessage(MSG.VOTE_TIME_TITLE), 3);
   }

   public void onOpen(InventoryOpenEvent var1) {
      this.update();
   }

   public void onClose(InventoryCloseEvent var1) {
   }

   public void onClick(InventoryClickEvent var1) {
      ItemStack var2 = var1.getCurrentItem();
      SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
      if (var3.isInArena()) {
         Arena var4 = var3.getArena();
         if (var2.getType() == Material.STAINED_CLAY && var2.getDurability() == 4) {
            if (!var3.hasPermissions("skywars.vote.time.day")) {
               var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_VOTE_TIME));
               this.getPlayer().closeInventory();
               return;
            }

            if (var3.hasData("voted_time")) {
               var3.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
               this.getPlayer().closeInventory();
               return;
            }

            var3.addData("voted_time", true);
            var3.addData("voted_time_day", true);
            var4.addData("vote_time_day", var4.getInt("vote_time_day") + 1);
            var3.sendMessage(String.format(SkyWars.getMessage(MSG.VOTE_TIME_SUCCESSFUL), ChatColor.stripColor(SkyWars.getMessage(MSG.VOTE_TIME_DAY_NAME))));
            var4.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_VOTE_TIME), this.getPlayer().getName(), SkyWars.getMessage(MSG.SELECTED_TIME_DAY), var4.getInt("vote_time_day")));
            this.getPlayer().closeInventory();
         }

         if (var2.getType() == Material.STAINED_CLAY && var2.getDurability() == 15) {
            if (!var3.hasPermissions("skywars.vote.time.night")) {
               var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_VOTE_TIME));
               this.getPlayer().closeInventory();
               return;
            }

            if (var3.hasData("voted_time")) {
               var3.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
               this.getPlayer().closeInventory();
               return;
            }

            var3.addData("voted_time", true);
            var3.addData("voted_time_night", true);
            var4.addData("vote_time_night", var4.getInt("vote_time_night") + 1);
            var3.sendMessage(String.format(SkyWars.getMessage(MSG.VOTE_TIME_SUCCESSFUL), ChatColor.stripColor(SkyWars.getMessage(MSG.VOTE_TIME_NIGHT_NAME))));
            var4.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_VOTE_TIME), this.getPlayer().getName(), SkyWars.getMessage(MSG.SELECTED_TIME_NIGHT), var4.getInt("vote_time_night")));
            this.getPlayer().closeInventory();
         }

         if (var2.getType() == Material.STAINED_CLAY && var2.getDurability() == 14) {
            if (!var3.hasPermissions("skywars.vote.time.sunset")) {
               var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_VOTE_TIME));
               this.getPlayer().closeInventory();
               return;
            }

            if (var3.hasData("voted_time")) {
               var3.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
               this.getPlayer().closeInventory();
               return;
            }

            var3.addData("voted_time", true);
            var3.addData("voted_time_sunset", true);
            var4.addData("vote_time_sunset", var4.getInt("vote_time_sunset") + 1);
            var3.sendMessage(String.format(SkyWars.getMessage(MSG.VOTE_TIME_SUCCESSFUL), ChatColor.stripColor(SkyWars.getMessage(MSG.VOTE_TIME_SUNSET_NAME))));
            var4.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_VOTE_TIME), this.getPlayer().getName(), SkyWars.getMessage(MSG.SELECTED_TIME_SUNSET), var4.getInt("vote_time_sunset")));
            this.getPlayer().closeInventory();
         }
      }

   }

   public void update() {
      SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
      if (var1.isInArena()) {
         Arena var2 = var1.getArena();
         this.setItem(10, (new ItemBuilder(Material.STAINED_CLAY, (short)4)).setTitle(SkyWars.getMessage(MSG.VOTE_TIME_DAY_NAME)).addLore(SkyWars.getMessage(MSG.VOTE_TIME_DAY_LORE1)).addLore(String.format(SkyWars.getMessage(MSG.VOTE_TIME_DAY_LORE2), var2.getInt("vote_time_day"))));
         this.setItem(13, (new ItemBuilder(Material.STAINED_CLAY, (short)15)).setTitle(SkyWars.getMessage(MSG.VOTE_TIME_NIGHT_NAME)).addLore(SkyWars.getMessage(MSG.VOTE_TIME_NIGHT_LORE1)).addLore(String.format(SkyWars.getMessage(MSG.VOTE_TIME_NIGHT_LORE2), var2.getInt("vote_time_night"))));
         this.setItem(16, (new ItemBuilder(Material.STAINED_CLAY, (short)14)).setTitle(SkyWars.getMessage(MSG.VOTE_TIME_SUNSET_NAME)).addLore(SkyWars.getMessage(MSG.VOTE_TIME_SUNSET_LORE1)).addLore(String.format(SkyWars.getMessage(MSG.VOTE_TIME_SUNSET_LORE2), var2.getInt("vote_time_sunset"))));
      }

   }
}
