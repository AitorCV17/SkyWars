package me.aitor.skywars.menus.lobby;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.kit.Kit;
import me.aitor.skywars.kit.KitManager;
import me.aitor.skywars.menus.Menu;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.economy.SkyEconomyManager;
import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuShopCategory extends Menu {
   private String section;

   public MenuShopCategory(Player var1, String var2) {
      super(var1, "shopCategory-" + var2, ConfigManager.shop.getString("submenus." + var2 + ".sub_name"), ConfigManager.shop.getInt("shop_size"));
      this.section = var2;
   }

   public void onOpen(InventoryOpenEvent var1) {
      this.update();
   }

   public void onClose(InventoryCloseEvent var1) {
   }

   public void onClick(InventoryClickEvent var1) {
      int var2 = var1.getSlot();
      if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
         SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
         if (!MenuShop.executeSubMenuButtons(var2, var3)) {
            Iterator var4 = ConfigManager.shop.getConfigurationSection("submenus." + this.section + ".content").getKeys(false).iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               if (var2 == Integer.parseInt(var5) - 1) {
                  String[] var6 = ConfigManager.shop.getString("submenus." + this.section + ".content." + var5 + ".item").split(",");
                  Kit var7 = KitManager.getKit(var6[1]);
                  if (var7 == null) {
                     return;
                  }

                  if (!var3.hasKit(var7)) {
                     if (SkyWars.getPlugin().getConfig().getBoolean("kit_permission") && !var3.hasPermissions("skywars.kit." + var7.getName().toLowerCase())) {
                        var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_KIT));
                        return;
                     }

                     if (var7.isFree()) {
                        var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_PURCHASE_KIT), var7.getName().toLowerCase()));
                        return;
                     }

                     if (var3.getCoins() >= (double)var7.getPrice()) {
                        SkyEconomyManager.removeCoins(var3.getPlayer(), var7.getPrice());
                        var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_PURCHASE_KIT), var7.getName().toLowerCase()));
                        var3.addData("upload_data", true);
                        var3.addKit(var7);
                        var3.getPlayer().closeInventory();
                        return;
                     }

                     var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDMONEY_KIT));
                     return;
                  }

                  var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_ALREADY_KIT), var7.getName().toLowerCase()));
               }
            }

         }
      }
   }

   public void update() {
      SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
      Iterator var2 = ConfigManager.shop.getConfigurationSection("submenus." + this.section + ".content").getKeys(false).iterator();

      String var3;
      int var4;
      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var4 = Integer.parseInt(var3) - 1;
         String[] var5 = ConfigManager.shop.getString("submenus." + this.section + ".content." + var3 + ".item").split(",");
         if (var5[0].equalsIgnoreCase("KIT")) {
            Kit var6 = KitManager.getKit(var5[1]);
            if (var6 != null) {
               ItemBuilder var7 = var6.getIcon().setHideFlags(true);
               if (var1.hasKit(var6)) {
                  var7.setTitle(String.format(SkyWars.getMessage(MSG.KIT_NAME_PURCHASED), var6.getName()));
               }

               this.setItem(var4, var7);
            }
         }
      }

      var2 = ConfigManager.shop.getConfigurationSection("all").getKeys(false).iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var4 = Integer.parseInt(var3) - 1;
         this.setItem(var4, MenuShop.getItemRead("all." + var3, ConfigManager.shop, true, var1));
      }

      var2 = ConfigManager.shop.getConfigurationSection("allsubmenus").getKeys(false).iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         var4 = Integer.parseInt(var3) - 1;
         this.setItem(var4, MenuShop.getItemRead("allsubmenus." + var3, ConfigManager.shop, true, (SkyPlayer)null));
      }

   }
}
