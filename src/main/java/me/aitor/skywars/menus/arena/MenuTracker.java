package me.aitor.skywars.menus.arena;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.menus.Menu;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MenuTracker extends Menu {
   public MenuTracker(Player var1) {
      super(var1, "tracker", SkyWars.getMessage(MSG.TRACKER_MENU_TITLE), 3);
   }

   public void onOpen(InventoryOpenEvent var1) {
      this.update();
   }

   public void onClose(InventoryCloseEvent var1) {
   }

   public void onClick(InventoryClickEvent var1) {
      ItemStack var2 = var1.getCurrentItem();
      if (var2.getType() == Material.SKULL_ITEM) {
         SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
         Player var4 = Bukkit.getPlayer(ChatColor.stripColor(var2.getItemMeta().getDisplayName()));
         var3.teleport(var4.getLocation());
         this.getPlayer().closeInventory();
      }

   }

   public void update() {
      SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
      if (var1.isInArena() && var1.isSpectating()) {
         Arena var2 = var1.getArena();
         this.getInventory().clear();
         Iterator var3 = var2.getAlivePlayer().iterator();

         while(var3.hasNext()) {
            SkyPlayer var4 = (SkyPlayer)var3.next();
            ItemBuilder var5 = (new ItemBuilder(Material.SKULL_ITEM, (short)3)).setTitle("§a" + var4.getName());
            SkullMeta var6 = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
            var6.setOwner(var4.getName());
            var5.build().setItemMeta(var6);
            this.addItem(var5);
         }
      }

   }
}
