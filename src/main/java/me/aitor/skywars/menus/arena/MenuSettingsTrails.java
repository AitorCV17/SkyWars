package me.aitor.skywars.menus.arena;

import me.aitor.skywars.SkyWars;
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

public class MenuSettingsTrails extends Menu {
   private ItemBuilder none;
   private ItemBuilder slime;
   private ItemBuilder flame;
   private ItemBuilder water;
   private ItemBuilder lava;
   private ItemBuilder potion;
   private ItemBuilder notes;

   public MenuSettingsTrails(Player var1) {
      super(var1, "settingsTrails", SkyWars.getMessage(MSG.SETTINGS_MENU_TRAILS_TITLE), 1);
   }

   public void onOpen(InventoryOpenEvent var1) {
      this.update();
   }

   public void onClose(InventoryCloseEvent var1) {
   }

   public void onClick(InventoryClickEvent var1) {
      ItemStack var2 = var1.getCurrentItem();
      SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
      if (var2.isSimilar(this.none.build())) {
         var3.setTrail((String)null);
         var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), "Default"));
         this.getPlayer().closeInventory();
      }

      if (var2.isSimilar(this.slime.build())) {
         if (!var3.hasPermissions("skywars.settings.trail.slime")) {
            var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_SETTINGS_TRAIL));
            this.getPlayer().closeInventory();
            return;
         }

         var3.setTrail("SLIME");
         var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), ChatColor.stripColor(SkyWars.getMessage(MSG.SETTINGS_TRAILS_SLIME_NAME))));
         this.getPlayer().closeInventory();
      }

      if (var2.isSimilar(this.flame.build())) {
         if (!var3.hasPermissions("skywars.settings.trail.flame")) {
            var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_SETTINGS_TRAIL));
            this.getPlayer().closeInventory();
            return;
         }

         var3.setTrail("FLAME");
         var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), ChatColor.stripColor(SkyWars.getMessage(MSG.SETTINGS_TRAILS_FLAME_NAME))));
         this.getPlayer().closeInventory();
      }

      if (var2.isSimilar(this.water.build())) {
         if (!var3.hasPermissions("skywars.settings.trail.water")) {
            var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_SETTINGS_TRAIL));
            this.getPlayer().closeInventory();
            return;
         }

         var3.setTrail("WATER_SPLASH");
         var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), ChatColor.stripColor(SkyWars.getMessage(MSG.SETTINGS_TRAILS_WATER_NAME))));
         this.getPlayer().closeInventory();
      }

      if (var2.isSimilar(this.lava.build())) {
         if (!var3.hasPermissions("skywars.settings.trail.lava")) {
            var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_SETTINGS_TRAIL));
            this.getPlayer().closeInventory();
            return;
         }

         var3.setTrail("DRIP_LAVA");
         var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), ChatColor.stripColor(SkyWars.getMessage(MSG.SETTINGS_TRAILS_LAVA_NAME))));
         this.getPlayer().closeInventory();
      }

      if (var2.isSimilar(this.potion.build())) {
         if (!var3.hasPermissions("skywars.settings.trail.potion")) {
            var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_SETTINGS_TRAIL));
            this.getPlayer().closeInventory();
            return;
         }

         var3.setTrail("REDSTONE");
         var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), ChatColor.stripColor(SkyWars.getMessage(MSG.SETTINGS_TRAILS_POTION_NAME))));
         this.getPlayer().closeInventory();
      }

      if (var2.isSimilar(this.notes.build())) {
         if (!var3.hasPermissions("skywars.settings.trail.notes")) {
            var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_SETTINGS_TRAIL));
            this.getPlayer().closeInventory();
            return;
         }

         var3.setTrail("NOTE");
         var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), ChatColor.stripColor(SkyWars.getMessage(MSG.SETTINGS_TRAILS_NOTES_NAME))));
         this.getPlayer().closeInventory();
      }

   }

   public void update() {
      this.none = (new ItemBuilder(Material.GLASS)).setTitle(SkyWars.getMessage(MSG.SETTINGS_TRAILS_DEFAULT_NAME));
      this.slime = (new ItemBuilder(Material.SLIME_BALL)).setTitle(SkyWars.getMessage(MSG.SETTINGS_TRAILS_SLIME_NAME)).addLore(SkyWars.getMessage(MSG.SETTINGS_TRAILS_SLIME_LORE));
      this.flame = (new ItemBuilder(Material.FLINT_AND_STEEL)).setTitle(SkyWars.getMessage(MSG.SETTINGS_TRAILS_FLAME_NAME)).addLore(SkyWars.getMessage(MSG.SETTINGS_TRAILS_FLAME_LORE));
      this.water = (new ItemBuilder(Material.WATER_BUCKET)).setTitle(SkyWars.getMessage(MSG.SETTINGS_TRAILS_WATER_NAME)).addLore(SkyWars.getMessage(MSG.SETTINGS_TRAILS_WATER_LORE));
      this.lava = (new ItemBuilder(Material.LAVA_BUCKET)).setTitle(SkyWars.getMessage(MSG.SETTINGS_TRAILS_LAVA_NAME)).addLore(SkyWars.getMessage(MSG.SETTINGS_TRAILS_LAVA_LORE));
      this.potion = (new ItemBuilder(Material.POTION)).setTitle(SkyWars.getMessage(MSG.SETTINGS_TRAILS_POTION_NAME)).addLore(SkyWars.getMessage(MSG.SETTINGS_TRAILS_POTION_LORE));
      this.notes = (new ItemBuilder(Material.JUKEBOX)).setTitle(SkyWars.getMessage(MSG.SETTINGS_TRAILS_NOTES_NAME)).addLore(SkyWars.getMessage(MSG.SETTINGS_TRAILS_NOTES_LORE));
      this.setItem(0, this.none);
      this.setItem(1, this.slime);
      this.setItem(2, this.flame);
      this.setItem(3, this.water);
      this.setItem(4, this.lava);
      this.setItem(5, this.potion);
      this.setItem(6, this.notes);
   }
}
