package me.aitor.skywars.menus.lobby;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.abilities.AbilityManager;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.config.SkyConfiguration;
import me.aitor.skywars.menus.Menu;
import me.aitor.skywars.menus.MenuListener;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.Utils;
import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuShop extends Menu {
   public MenuShop(Player var1) {
      super(var1, "shop", ConfigManager.shop.getString("main.main_name"), ConfigManager.shop.getInt("shop_size"));

      for(int var2 = 1; var2 <= this.getAbilitiesPages(); ++var2) {
         new MenuAbilities(var1, var2, this.getAbilitiesPages());
      }

      Iterator var4 = ConfigManager.shop.getConfigurationSection("submenus").getKeys(false).iterator();

      while(var4.hasNext()) {
         String var3 = (String)var4.next();
         new MenuShopCategory(var1, var3);
      }

   }

   public void onOpen(InventoryOpenEvent var1) {
      this.update();
   }

   public void onClose(InventoryCloseEvent var1) {
   }

   public void onClick(InventoryClickEvent var1) {
      int var2 = var1.getSlot();
      if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
         Iterator var3 = ConfigManager.shop.getConfigurationSection("all").getKeys(false).iterator();

         String var4;
         int var5;
         String var6;
         String var7;
         byte var8;
         do {
            if (!var3.hasNext()) {
               var3 = ConfigManager.shop.getConfigurationSection("main.items").getKeys(false).iterator();

               do {
                  if (!var3.hasNext()) {
                     var3 = ConfigManager.shop.getConfigurationSection("submenus").getKeys(false).iterator();

                     do {
                        if (!var3.hasNext()) {
                           if (ConfigManager.shop.getInt("submenu.abilities.slot") - 1 == var2) {
                              this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "abilities1").getInventory());
                              return;
                           }

                           return;
                        }

                        var4 = (String)var3.next();
                     } while(Integer.parseInt(var4) - 1 != var2);

                     this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "shopCategory-" + var4).getInventory());
                     return;
                  }

                  var4 = (String)var3.next();
                  var5 = Integer.parseInt(var4) - 1;
               } while(var2 != var5);

               var6 = ConfigManager.shop.getString("main.items." + var4 + ".action");
               var7 = var6.toUpperCase();
               var8 = -1;
               switch(var7.hashCode()) {
               case 2402104:
                  if (var7.equals("NONE")) {
                     var8 = 0;
                  }
                  break;
               case 64218584:
                  if (var7.equals("CLOSE")) {
                     var8 = 1;
                  }
               }

               switch(var8) {
               case 0:
                  return;
               case 1:
                  this.getPlayer().closeInventory();
                  return;
               default:
                  return;
               }
            }

            var4 = (String)var3.next();
            var5 = Integer.parseInt(var4) - 1;
         } while(var2 != var5);

         var6 = ConfigManager.shop.getString("all." + var4 + ".action");
         var7 = var6.toUpperCase();
         var8 = -1;
         switch(var7.hashCode()) {
         case 2402104:
            if (var7.equals("NONE")) {
               var8 = 0;
            }
            break;
         case 64218584:
            if (var7.equals("CLOSE")) {
               var8 = 1;
            }
         }

         switch(var8) {
         case 0:
            return;
         case 1:
            this.getPlayer().closeInventory();
            return;
         default:
         }
      }
   }

   public void update() {
      SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
      Iterator var2 = ConfigManager.shop.getConfigurationSection("main.items").getKeys(false).iterator();

      String var3;
      int var4;
      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var4 = Integer.parseInt(var3) - 1;
         this.setItem(var4, getItemRead("main.items." + var3, ConfigManager.shop, true, var1));
      }

      var2 = ConfigManager.shop.getConfigurationSection("all").getKeys(false).iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var4 = Integer.parseInt(var3) - 1;
         this.setItem(var4, getItemRead("all." + var3, ConfigManager.shop, true, var1));
      }

      var2 = ConfigManager.shop.getConfigurationSection("submenus").getKeys(false).iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var4 = Integer.parseInt(var3) - 1;
         this.setItem(var4, getItemRead("submenus." + var3, ConfigManager.shop, true, var1));
      }

      if (ConfigManager.shop.getBoolean("submenu.abilities.enabled")) {
         int var5 = ConfigManager.shop.getInt("submenu.abilities.slot");
         this.setItem(var5 - 1, getItemRead("submenu.abilities.item", ConfigManager.shop, true, var1));
      }

   }

   private int getAbilitiesPages() {
      int var1 = AbilityManager.getEnabledAbilities().length;
      int var2 = var1 % 5;
      int var3 = var1 - var2;
      int var4 = var3 / 5;
      if (var2 != 0 && var2 < 5) {
         ++var4;
      }

      return var4;
   }

   public static boolean executeSubMenuButtons(int var0, SkyPlayer var1) {
      Player var2 = var1.getPlayer();
      Iterator var3 = ConfigManager.shop.getConfigurationSection("all").getKeys(false).iterator();

      String var4;
      int var5;
      String var6;
      String var7;
      byte var8;
      do {
         if (!var3.hasNext()) {
            var3 = ConfigManager.shop.getConfigurationSection("allsubmenus").getKeys(false).iterator();

            do {
               if (!var3.hasNext()) {
                  return false;
               }

               var4 = (String)var3.next();
               var5 = Integer.parseInt(var4) - 1;
            } while(var0 != var5);

            var6 = ConfigManager.shop.getString("allsubmenus." + var4 + ".action");
            var7 = var6.toUpperCase();
            var8 = -1;
            switch(var7.hashCode()) {
            case 2358713:
               if (var7.equals("MAIN")) {
                  var8 = 2;
               }
               break;
            case 2402104:
               if (var7.equals("NONE")) {
                  var8 = 0;
               }
               break;
            case 64218584:
               if (var7.equals("CLOSE")) {
                  var8 = 1;
               }
            }

            switch(var8) {
            case 0:
               return true;
            case 1:
               var2.closeInventory();
               return true;
            case 2:
               if (var1.isInArena()) {
                  var2.closeInventory();
               } else {
                  var2.openInventory(MenuListener.getPlayerMenu(var1.getPlayer(), "shop").getInventory());
               }

               return true;
            default:
               return true;
            }
         }

         var4 = (String)var3.next();
         var5 = Integer.parseInt(var4) - 1;
      } while(var0 != var5);

      var6 = ConfigManager.shop.getString("all." + var4 + ".action");
      var7 = var6.toUpperCase();
      var8 = -1;
      switch(var7.hashCode()) {
      case 2402104:
         if (var7.equals("NONE")) {
            var8 = 0;
         }
         break;
      case 64218584:
         if (var7.equals("CLOSE")) {
            var8 = 1;
         }
      }

      switch(var8) {
      case 0:
         return true;
      case 1:
         var2.closeInventory();
         return true;
      default:
         return true;
      }
   }

   public static ItemBuilder getItemRead(String var0, SkyConfiguration var1, boolean var2, SkyPlayer var3) {
      ItemBuilder var4 = Utils.readItem(var1.getString(var0 + ".item"));
      if (var3 != null) {
         var4.setTitle(var1.getString(var0 + ".name").replace("%coins%", "" + var3.getCoins()));
         if (var2) {
            var4.setLore(var1.getStringList(var0 + ".lore"));
         }
      } else {
         var4.setTitle(var1.getString(var0 + ".name"));
         if (var2) {
            var4.setLore(var1.getStringList(var0 + ".lore"));
         }
      }

      return var4;
   }
}
