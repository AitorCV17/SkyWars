package me.aitor.skywars.menus.lobby;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.abilities.Ability;
import me.aitor.skywars.abilities.AbilityLevel;
import me.aitor.skywars.abilities.AbilityManager;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.menus.Menu;
import me.aitor.skywars.menus.MenuListener;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.Utils;
import me.aitor.skywars.utils.economy.SkyEconomyManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuAbilities extends Menu {
   private int page;
   private int pages;

   public MenuAbilities(Player var1, int var2, int var3) {
      super(var1, "abilities" + var2, ConfigManager.shop.getString("submenu.abilities.name").replace("%number%", var2 + ""), 6);
      this.page = var2;
      this.pages = var3;
   }

   public void onOpen(InventoryOpenEvent var1) {
      this.update();
   }

   public void onClose(InventoryCloseEvent var1) {
   }

   public void onClick(InventoryClickEvent var1) {
      int var2 = var1.getSlot();
      if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
         if (var2 == ConfigManager.shop.getInt("submenu.abilities.next.slot") - 1) {
            this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "abilities" + (this.page + 1)).getInventory());
         } else if (var2 == ConfigManager.shop.getInt("submenu.abilities.previous.slot") - 1) {
            this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "abilities" + (this.page - 1)).getInventory());
         } else {
            SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
            if (!MenuShop.executeSubMenuButtons(var2, var3)) {
               Ability[] var4 = AbilityManager.getEnabledAbilities();
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  Ability var7 = var4[var6];
                  String var8 = ChatColor.translateAlternateColorCodes('&', ConfigManager.abilities.getString("abilities." + var7.getName() + ".name"));
                  String var9 = var1.getCurrentItem().getItemMeta().getDisplayName();
                  String var10 = ConfigManager.abilities.getString("abilities." + var7.getName() + ".name");
                  if (var9.equals(var8)) {
                     if (var3.isAbilityDisabled(var7.getType())) {
                        var3.removeAbilityDisabled(var7.getType());
                     } else {
                        var3.addAbilityDisabled(var7.getType());
                     }

                     this.executeAbilityIconBuild(var2, var10, var7, var3);
                     this.executeAbilitiesLevelsBuild(var7, var3, var10, var2);
                     var3.uploadAsyncData();
                     return;
                  }

                  if (var9.contains(var8)) {
                     ItemStack var11 = var1.getCurrentItem();
                     if (this.itemCompare(var11, "purchase")) {
                        if (!var3.hasAbility(var7.getType())) {
                           this.buyAbility(var3, 1, var7, var10, var2);
                        } else {
                           int var12 = var3.getAbilityLevel(var7.getType()).getLevel();
                           this.buyAbility(var3, var12 + 1, var7, var10, var2);
                        }

                        return;
                     }

                     if (this.itemCompare(var11, "afford")) {
                        var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_ABILITY_AFFORD));
                        return;
                     }

                     if (this.itemCompare(var11, "purchased")) {
                        var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_ABILITY_PURCHASED));
                        return;
                     }

                     if (this.itemCompare(var11, "unavailable")) {
                        var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_ABILITY_UNAVAILABLE));
                        return;
                     }

                     if (this.itemCompare(var11, "disabled")) {
                        var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_ABILITY_DISABLED));
                        return;
                     }
                  }
               }

            }
         }
      }
   }

   public void update() {
      boolean var1 = this.page < this.pages;
      boolean var2 = this.page > 1;
      SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
      this.updateAbilities(var3);
      Iterator var4 = ConfigManager.shop.getConfigurationSection("all").getKeys(false).iterator();

      String var5;
      int var6;
      while(var4.hasNext()) {
         var5 = (String)var4.next();
         var6 = Integer.parseInt(var5) - 1;
         this.setItem(var6, MenuShop.getItemRead("all." + var5, ConfigManager.shop, true, var3));
      }

      var4 = ConfigManager.shop.getConfigurationSection("allsubmenus").getKeys(false).iterator();

      while(var4.hasNext()) {
         var5 = (String)var4.next();
         var6 = Integer.parseInt(var5) - 1;
         this.setItem(var6, MenuShop.getItemRead("allsubmenus." + var5, ConfigManager.shop, true, (SkyPlayer)null));
      }

      int var7;
      if (var1) {
         var7 = ConfigManager.shop.getInt("submenu.abilities.next.slot");
         this.setItem(var7 - 1, MenuShop.getItemRead("submenu.abilities.next", ConfigManager.shop, false, (SkyPlayer)null));
      }

      if (var2) {
         var7 = ConfigManager.shop.getInt("submenu.abilities.previous.slot");
         this.setItem(var7 - 1, MenuShop.getItemRead("submenu.abilities.previous", ConfigManager.shop, false, (SkyPlayer)null));
      }

   }

   private void updateAbilities(SkyPlayer var1) {
      int var2 = 0;
      int var3 = 1;
      Ability[] var4 = AbilityManager.getEnabledAbilities();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Ability var7 = var4[var6];
         if (this.page == 2 && var3 < 6) {
            ++var3;
         } else {
            if (var2 >= 45) {
               break;
            }

            String var8 = ConfigManager.abilities.getString("abilities." + var7.getName() + ".name");
            this.executeAbilityIconBuild(var2, var8, var7, var1);
            var2 = this.executeAbilitiesLevelsBuild(var7, var1, var8, var2);
            ++var2;
            ++var3;
         }
      }

   }

   public int getPage() {
      return this.page;
   }

   public int getPages() {
      return this.pages;
   }

   private ItemBuilder getAbilityLevelItem(String var1) {
      return Utils.readItem(ConfigManager.abilities.getString("menu.item.status." + var1));
   }

   private List<String> getLoreFormated(Ability var1, AbilityLevel var2, String var3, String var4) {
      int var5 = var2.getChance();
      int var6 = var2.getPrice();
      ArrayList var7 = new ArrayList();
      Iterator var8 = ConfigManager.abilities.getStringList("menu.lore.levels." + var3).iterator();

      while(true) {
         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            if (var9.equals("%level-desc%")) {
               Iterator var10 = ConfigManager.abilities.getStringList("abilities." + var1.getName() + ".levels_desc").iterator();

               while(var10.hasNext()) {
                  String var11 = (String)var10.next();
                  var7.add(var11.replace("%chance%", var5 + "").replace("%price%", var6 + "").replace("%value%", var2.getValue() + ""));
               }
            } else if (var9.equals("%price-format%")) {
               var7.add(ConfigManager.abilities.getString("menu.lore.price").replace("%price%", var6 + ""));
            } else if (var9.equals("%status-format%")) {
               var7.add(ConfigManager.abilities.getString("menu.lore.status." + var4));
            } else if (var9.equals("%disabled%")) {
               var7.add(ConfigManager.abilities.getString("menu.lore.disabled"));
            } else {
               var7.add(var9);
            }
         }

         return var7;
      }
   }

   private void executeAbilityIconBuild(int var1, String var2, Ability var3, SkyPlayer var4) {
      ItemBuilder var5 = Utils.readItem(ConfigManager.abilities.getString("abilities." + var3.getName() + ".item"));
      ArrayList var6 = new ArrayList();
      var6.addAll(ConfigManager.abilities.getStringList("abilities." + var3.getName() + ".desc"));
      var6.add("");
      var6.add(ConfigManager.abilities.getString("menu.lore.clickto." + (var4.isAbilityDisabled(var3.getType()) ? "enable" : "disable")));
      this.setItem(var1, var5.setTitle(var2).setLore(var6).setHideFlags(true));
   }

   private int executeAbilitiesLevelsBuild(Ability var1, SkyPlayer var2, String var3, int var4) {
      int var5 = var4;
      Iterator var6 = var1.getLevels().values().iterator();

      while(var6.hasNext()) {
         AbilityLevel var7 = (AbilityLevel)var6.next();
         String var8 = ConfigManager.abilities.getString("menu.item.level").replace("%number%", var7.getLevel() + "");
         ItemBuilder var9;
         if (var2.isAbilityDisabled(var1.getType())) {
            var9 = this.getAbilityLevelItem("disabled").setTitle(var3 + " " + var8).setLore(this.getLoreFormated(var1, var7, "disabled", "disabled"));
            ++var5;
            this.setItem(var5, var9);
         } else {
            AbilityLevel var10 = var2.getAbilityLevel(var1.getType());
            if (var10 == null) {
               if (var7.getLevel() == 1) {
                  if (var2.getCoins() >= (double)var1.getLevel(1).getPrice()) {
                     var9 = this.getAbilityLevelItem("purchase").setTitle(var3 + " " + var8).setLore(this.getLoreFormated(var1, var7, "enabled", "purchase"));
                  } else {
                     var9 = this.getAbilityLevelItem("afford").setTitle(var3 + " " + var8).setLore(this.getLoreFormated(var1, var7, "enabled", "afford"));
                  }
               } else {
                  var9 = this.getAbilityLevelItem("unavailable").setTitle(var3 + " " + var8).setLore(this.getLoreFormated(var1, var7, "enabled", "unavailable"));
               }

               ++var5;
               this.setItem(var5, var9);
            } else {
               int var11 = var10.getLevel();
               if (var7.getLevel() <= var11) {
                  var9 = this.getAbilityLevelItem("purchased").setTitle(var3 + " " + var8).setLore(this.getLoreFormated(var1, var7, "enabled", "purchased"));
                  ++var5;
                  this.setItem(var5, var9);
               } else if (var7.getLevel() == var11 + 1) {
                  if (var2.getCoins() >= (double)var7.getPrice()) {
                     var9 = this.getAbilityLevelItem("purchase").setTitle(var3 + " " + var8).setLore(this.getLoreFormated(var1, var7, "enabled", "purchase"));
                  } else {
                     var9 = this.getAbilityLevelItem("afford").setTitle(var3 + " " + var8).setLore(this.getLoreFormated(var1, var7, "enabled", "afford"));
                  }

                  ++var5;
                  this.setItem(var5, var9);
               } else {
                  var9 = this.getAbilityLevelItem("unavailable").setTitle(var3 + " " + var8).setLore(this.getLoreFormated(var1, var7, "enabled", "unavailable"));
                  ++var5;
                  this.setItem(var5, var9);
               }
            }
         }
      }

      return var5;
   }

   private boolean buyAbility(SkyPlayer var1, int var2, Ability var3, String var4, int var5) {
      if (var1.getCoins() >= (double)var3.getLevel(var2).getPrice()) {
         SkyEconomyManager.removeCoins(var1.getPlayer(), var3.getLevel(var2).getPrice());
         String var6 = ConfigManager.abilities.getString("menu.item.level").replace("%number%", var2 + "");
         var1.sendMessage(SkyWars.getMessage(MSG.PLAYER_ABILITY_PURCHASE).replace("%ability%", var4 + " " + var6));
         var1.addData("upload_data", true);
         var1.addAbilityLevel(var3.getType());
         ItemBuilder var7 = this.getAbilityLevelItem("purchased");
         var7.setTitle(var4 + " " + var6).setLore(this.getLoreFormated(var3, var3.getLevel(var2), "enabled", "purchased"));
         this.setItem(var5, var7);
         if (var2 < 8) {
            ItemBuilder var8;
            String var9;
            if (var1.getCoins() >= (double)var3.getLevel(var2 + 1).getPrice()) {
               var8 = this.getAbilityLevelItem("purchase");
               var9 = ConfigManager.abilities.getString("menu.item.level").replace("%number%", var2 + 1 + "");
               var8.setTitle(var4 + " " + var9).setLore(this.getLoreFormated(var3, var3.getLevel(var2 + 1), "enabled", "purchase"));
               this.setItem(var5 + 1, var8);
            } else {
               var8 = this.getAbilityLevelItem("afford");
               var9 = ConfigManager.abilities.getString("menu.item.level").replace("%number%", var2 + 1 + "");
               var8.setTitle(var4 + " " + var9).setLore(this.getLoreFormated(var3, var3.getLevel(var2 + 1), "enabled", "afford"));
               this.setItem(var5 + 1, var8);
            }
         }

         this.updateAbilities(var1);
         return true;
      } else {
         var1.sendMessage(SkyWars.getMessage(MSG.PLAYER_ABILITY_AFFORD));
         return false;
      }
   }

   private boolean itemCompare(ItemStack var1, String var2) {
      String var3 = ChatColor.translateAlternateColorCodes('&', ConfigManager.abilities.getString("menu.lore.status." + var2));
      ItemStack var4 = this.getAbilityLevelItem(var2).build();
      if (var4.getType() == var1.getType() && var4.getDurability() == var1.getDurability()) {
         Iterator var5 = var1.getItemMeta().getLore().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (var6.equals(var3)) {
               return true;
            }
         }
      }

      return false;
   }
}
