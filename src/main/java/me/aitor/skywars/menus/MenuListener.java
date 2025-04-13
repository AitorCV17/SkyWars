package me.aitor.skywars.menus;

import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener implements Listener {
   public static HashMap<String, HashMap<String, Menu>> menus = new HashMap<>();

   public static HashMap<String, Menu> getPlayerMenus(Player player) {
      return menus.computeIfAbsent(player.getName(), k -> new HashMap<>());
   }

   public static Menu getPlayerMenu(Player player, String id) {
      return getPlayerMenus(player).get(id);
   }

   @EventHandler
   public void onPlayerLeaveInvRemove(PlayerQuitEvent event) {
      menus.remove(event.getPlayer().getName());
   }

   @EventHandler
   public void onPlayerKickInvRemove(PlayerKickEvent event) {
      menus.remove(event.getPlayer().getName());
   }

   @EventHandler
   public void onInventoryOpen(InventoryOpenEvent event) {
      Iterator<Menu> it = getPlayerMenus((Player) event.getPlayer()).values().iterator();
      while (it.hasNext()) {
         Menu menu = it.next();
         if (event.getInventory().getTitle().equals(menu.getInventory().getTitle())) {
            menu.onOpen(event);
         }
      }
   }

   @EventHandler
   public void onInventoryClose(InventoryCloseEvent event) {
      Iterator<Menu> it = getPlayerMenus((Player) event.getPlayer()).values().iterator();
      while (it.hasNext()) {
         Menu menu = it.next();
         if (event.getInventory().getTitle().equals(menu.getInventory().getTitle())) {
            menu.onClose(event);
         }
      }
   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      Iterator<Menu> it = getPlayerMenus((Player) event.getWhoClicked()).values().iterator();
      while (it.hasNext()) {
         Menu menu = it.next();
         if (event.getInventory().getTitle().equals(menu.getInventory().getTitle()) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            menu.onClick(event);
         }
      }
   }
}
