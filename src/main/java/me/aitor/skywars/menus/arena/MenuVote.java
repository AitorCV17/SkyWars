package me.aitor.skywars.menus.arena;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.menus.Menu;
import me.aitor.skywars.menus.MenuListener;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuVote extends Menu {
   public MenuVote(Player var1) {
      super(var1, "vote", SkyWars.getMessage(MSG.VOTE_MENU_TITLE), 3);
      new MenuVoteChest(var1);
      new MenuVoteTime(var1);
   }

   public void onOpen(InventoryOpenEvent var1) {
      this.update();
   }

   public void onClose(InventoryCloseEvent var1) {
   }

   public void onClick(InventoryClickEvent var1) {
      if (var1.getCurrentItem().getType() == Material.CHEST) {
         this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "voteChest").getInventory());
      }

      if (var1.getCurrentItem().getType() == Material.WATCH) {
         this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "voteTime").getInventory());
      }

   }

   public void update() {
      SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
      if (var1.isInArena()) {
         Arena var2 = var1.getArena();
         if (var2.getConfig().getBoolean("options.vote.chest")) {
            this.setItem(11, (new ItemBuilder(Material.CHEST)).setTitle(SkyWars.getMessage(MSG.VOTE_CHESTS_NAME)).addLore(SkyWars.getMessage(MSG.VOTE_CHESTS_LORE)));
         }

         if (var2.getConfig().getBoolean("options.vote.time")) {
            this.setItem(15, (new ItemBuilder(Material.WATCH)).setTitle(SkyWars.getMessage(MSG.VOTE_TIME_NAME)).addLore(SkyWars.getMessage(MSG.VOTE_TIME_LORE)));
         }
      }

   }
}
