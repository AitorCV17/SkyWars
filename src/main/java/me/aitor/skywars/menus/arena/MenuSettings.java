package me.aitor.skywars.menus.arena;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.menus.Menu;
import me.aitor.skywars.menus.MenuListener;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuSettings extends Menu {
   public MenuSettings(Player var1) {
      super(var1, "settings", SkyWars.getMessage(MSG.SETTINGS_MENU_TITLE), 1);
      new MenuSettingsBoxes(var1);
      new MenuSettingsTrails(var1);
   }

   public void onOpen(InventoryOpenEvent var1) {
      this.update();
   }

   public void onClose(InventoryCloseEvent var1) {
   }

   public void onClick(InventoryClickEvent var1) {
      if (var1.getCurrentItem().getType() == Material.GLASS) {
         this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "settingsBoxes").getInventory());
      }

      if (var1.getCurrentItem().getType() == Material.ARROW) {
         this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "settingsTrails").getInventory());
      }

   }

   public void update() {
      this.setItem(2, (new ItemBuilder(Material.GLASS)).setTitle(SkyWars.getMessage(MSG.SETTINGS_MENU_BOXES_NAME)).addLore(SkyWars.getMessage(MSG.SETTINGS_MENU_BOXES_LORE)));
      this.setItem(6, (new ItemBuilder(Material.ARROW)).setTitle(SkyWars.getMessage(MSG.SETTINGS_MENU_TRAILS_NAME)).addLore(SkyWars.getMessage(MSG.SETTINGS_MENU_TRAILS_LORE)));
   }
}
