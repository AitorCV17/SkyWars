package me.aitor.skywars.listener;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.events.enums.ArenaLeaveCause;
import me.aitor.skywars.kit.KitManager;
import me.aitor.skywars.menus.Menu;
import me.aitor.skywars.menus.MenuListener;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.sign.SignManager;
import me.aitor.skywars.sign.SkySign;
import me.aitor.skywars.utils.BungeeUtils;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {
   @EventHandler
   public void onInteract(PlayerInteractEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("InteractListener.onInteract - jugador nulo");
      } else {
         if (var1.getAction() == Action.RIGHT_CLICK_AIR || var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (var1.hasBlock() && var1.getClickedBlock().getState() instanceof Sign) {
               Sign var4 = (Sign)var1.getClickedBlock().getState();
               Location var5 = var4.getLocation();
               SkySign var6 = SignManager.getSign(var5);
               if (var6 != null) {
                  return;
               }
            }

            if (var3.isInArena()) {
               Arena var20 = var3.getArena();
               ItemStack var23 = var2.getItemInHand();
               Material var7;
               ItemStack var8;
               String[] var25;
               if ((var20.getState() == ArenaState.WAITING || var20.getState() == ArenaState.STARTING) && !var3.isSpectating()) {
                  var25 = ConfigManager.main.getString("item.vote").split(" ");
                  var7 = Material.EMPTY_MAP;
                  if (var25.length >= 2) {
                     try {
                        var7 = Material.matchMaterial(var25[1]);
                     } catch (NumberFormatException var19) {
                        SkyWars.logError("Formato de ítem incorrecto en config.yml (item.vote)");
                     }
                  }

                  var8 = (new ItemBuilder(var7)).setTitle(SkyWars.getMessage(MSG.ITEM_VOTE_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_VOTE_LORE)).build();
                  if (var23.equals(var8)) {
                     var1.setCancelled(true);
                     Menu var9 = MenuListener.getPlayerMenu(var2, "vote");
                     if (var9 == null) {
                        SkyWars.logError("Algo anda mal con el menú de votación para " + var2.getName());
                        return;
                     }

                     var2.openInventory(var9.getInventory());
                  }

                  String[] var26 = ConfigManager.main.getString("item.kits").split(" ");
                  Material var10 = Material.PAPER;
                  if (var26.length >= 2) {
                     try {
                        var10 = Material.matchMaterial(var26[1]);
                     } catch (NumberFormatException var18) {
                        SkyWars.logError("Formato de ítem incorrecto en config.yml (item.kits)");
                     }
                  }

                  ItemStack var11 = (new ItemBuilder(var10)).setTitle(SkyWars.getMessage(MSG.ITEM_KITS_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_KITS_LORE)).build();
                  if (var23.equals(var11)) {
                     if (KitManager.getKits().length == 0) {
                        var3.sendMessage(SkyWars.getMessage(MSG.KITS_NONE));
                        return;
                     }

                     var1.setCancelled(true);
                     Menu var12 = MenuListener.getPlayerMenu(var2, "kitSelector");
                     if (var12 == null) {
                        SkyWars.logError("Algo anda mal con el menú de kits para " + var2.getName());
                        return;
                     }

                     var2.openInventory(var12.getInventory());
                  }

                  String[] var27 = ConfigManager.main.getString("item.settings").split(" ");
                  Material var13 = Material.DIAMOND;
                  if (var27.length >= 2) {
                     try {
                        var13 = Material.matchMaterial(var27[1]);
                     } catch (NumberFormatException var17) {
                        SkyWars.logError("Formato de ítem incorrecto en config.yml (item.settings)");
                     }
                  }

                  ItemStack var14 = (new ItemBuilder(var13)).setTitle(SkyWars.getMessage(MSG.ITEM_SETTINGS_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_SETTINGS_LORE)).build();
                  if (var23.equals(var14)) {
                     Menu var15 = MenuListener.getPlayerMenu(var2, "settings");
                     if (var15 == null) {
                        SkyWars.logError("Algo anda mal con el menú de configuración para " + var2.getName());
                        return;
                     }

                     var2.openInventory(var15.getInventory());
                     var1.setCancelled(true);
                  }
               }

               if (var3.isSpectating() && var23.getType() == Material.COMPASS) {
                  var2.openInventory(MenuListener.getPlayerMenu(var2, "tracker").getInventory());
               }

               if (var20.getState() == ArenaState.WAITING || var20.getState() == ArenaState.STARTING) {
                  var25 = ConfigManager.main.getString("item.exit").split(" ");
                  var7 = Material.BED;
                  if (var25.length >= 2) {
                     try {
                        var7 = Material.matchMaterial(var25[1]);
                     } catch (NumberFormatException var16) {
                        SkyWars.logError("Formato de ítem incorrecto en config.yml (item.exit)");
                     }
                  }

                  var8 = (new ItemBuilder(var7)).setTitle(SkyWars.getMessage(MSG.ITEM_SPECTATOR_EXIT_NAME)).build();
                  if (var23.equals(var8)) {
                     if (SkyWars.isBungeeMode()) {
                        if (var3.isInArena()) {
                           var20.removePlayer(var3, ArenaLeaveCause.ITEM);
                           SkyWars.log("InteractListener.onInteract - " + var3.getName() + " eliminado usando cama");
                        }

                        BungeeUtils.teleToServer(var2, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
                     } else if (var3.isInArena()) {
                        var20.removePlayer(var3, ArenaLeaveCause.ITEM);
                        SkyWars.log("InteractListener.onInteract - " + var3.getName() + " eliminado usando cama");
                        var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY));
                     }
                  }

                  var1.setCancelled(true);
               }
            }

            if ((SkyWars.isMultiArenaMode() && !var3.isInArena() || SkyWars.isLobbyMode()) && ConfigManager.shop.getBoolean("item.enabled")) {
               ItemStack var21 = var2.getItemInHand();
               ItemBuilder var24 = Utils.readItem(ConfigManager.shop.getString("item.item"));
               var24.setTitle(ConfigManager.shop.getString("item.name")).setLore(ConfigManager.shop.getStringList("item.lore"));
               if (var21.isSimilar(var24.build())) {
                  var2.openInventory(MenuListener.getPlayerMenu(var2, "shop").getInventory());
               }
            }
         }

         if (var1.getAction() == Action.PHYSICAL && var3.isInArena() && var3.isSpectating()) {
            Material var22 = var1.getClickedBlock().getType();
            if (var22 == Material.SOIL || var22 == Material.WOOD_PLATE || var22 == Material.STONE_PLATE || var22 == Material.IRON_PLATE || var22 == Material.GOLD_PLATE) {
               var1.setCancelled(true);
            }
         }

      }
   }
}
