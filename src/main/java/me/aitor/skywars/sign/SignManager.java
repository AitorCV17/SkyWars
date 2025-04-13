package me.aitor.skywars.sign;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.events.SkySignUpdateEvent;
import me.aitor.skywars.events.enums.ArenaJoinCause;
import me.aitor.skywars.events.enums.SkySignUpdateCause;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.server.ServerManager;
import me.aitor.skywars.utils.BungeeUtils;
import me.aitor.skywars.utils.Game;
import me.aitor.skywars.utils.LocationUtil;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.SignUtils;
import me.aitor.skywars.utils.Utils;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SignManager implements Listener {
   private static final HashMap<String, SkySign> locationSign = new HashMap();

   public static void loadSigns() {
      List var0 = ConfigManager.signs.getStringList("signs");
      var0.sort(Comparator.comparing(String::length).reversed());
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         String[] var3 = var2.split(";");
         String var4 = var3[0];
         String var5 = null;
         if (var3.length > 1 && var3[1] != null) {
            var5 = var3[1];
         }

         if (var5 == null) {
            var5 = "";
         }

         SkySign var6 = new SkySign(var4);
         initSign(var4, var5, var6);
      }

      SkyWars.log("SignManager#loadSigns - Carteles cargados");
   }

   private static List<SkySign> getArenaSigns(String var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = locationSign.values().iterator();

      while(var2.hasNext()) {
         SkySign var3 = (SkySign)var2.next();
         if (var3.getGame() != null && var3.getGame().getName().equals(var0)) {
            var1.add(var3);
         }
      }

      return var1;
   }

   private static List<SkySign> getSearchingSigns() {
      ArrayList var0 = new ArrayList();
      Iterator var1 = locationSign.values().iterator();

      while(var1.hasNext()) {
         SkySign var2 = (SkySign)var1.next();
         if (var2.getGame() == null) {
            var0.add(var2);
         }
      }

      return var0;
   }

   private static void initSign(String var0, String var1, SkySign var2) {
      var2.setRotation(ConfigManager.signs.getBoolean("rotation"));
      if (var1 != null && !var1.isEmpty()) {
         var2.setGame(var1);
      }

      locationSign.put(var0, var2);
      Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
         Bukkit.getServer().getPluginManager().callEvent(new SkySignUpdateEvent(var1, SkySignUpdateCause.STATE));
      }, 5L);
   }

   public static Set<SkySign> getSigns() {
      return Collections.unmodifiableSet(Sets.newHashSet(locationSign.values()));
   }

   public static SkySign getSign(Location var0) {
      String var1 = LocationUtil.getString(var0, false);
      return (SkySign)locationSign.get(var1);
   }

   private static Game[] getAvailableGames() {
      ArrayList var0 = new ArrayList();
      Set var1 = SkyWars.isLobbyMode() ? ServerManager.getServers() : ArenaManager.getGames();
      Iterator var2 = var1.iterator();

      while(true) {
         Game var3;
         do {
            do {
               do {
                  do {
                     if (!var2.hasNext()) {
                        if (var0.isEmpty()) {
                           return new Game[]{null};
                        }

                        return (Game[])var0.toArray(new Game[0]);
                     }

                     var3 = (Game)var2.next();
                  } while(var3.getState() != ArenaState.WAITING && var3.getState() != ArenaState.STARTING);
               } while(var3.isLoading());
            } while(var3.isFull());
         } while(var3.isDisabled());

         int var4 = 0;
         Iterator var5 = getArenaSigns(var3.getName()).iterator();

         while(var5.hasNext()) {
            SkySign var6 = (SkySign)var5.next();
            if (var6.isRotation()) {
               ++var4;
            }
         }

         if (var4 == 0) {
            var0.add(var3);
         }
      }
   }

   public static void updateSign(SkySign var0) {
      if (var0 != null) {
         if (!var0.isUpdating()) {
            var0.setUpdating(true);
            if (var0.isRotation()) {
               if (var0.getGame() == null) {
                  Game[] var1 = getAvailableGames();
                  Game var2 = var1[(new Random()).nextInt(var1.length)];
                  if (var2 == null) {
                     signFormatSearch(var0);
                     var0.setUpdating(false);
                     return;
                  }

                  var0.setGame(var2.getName());
               }

               int var4 = 0;
               Iterator var5 = locationSign.values().iterator();

               while(var5.hasNext()) {
                  SkySign var3 = (SkySign)var5.next();
                  if (var3 != var0 && var0.getGame() != null && var3.getGame() == var0.getGame()) {
                     ++var4;
                  }
               }

               if (var4 >= 1) {
                  signFormatSearch(var0);
                  var0.setUpdating(false);
               }

               if (var0.getGame() != null) {
                  if (var0.getGame().isDisabled()) {
                     signFormatSearch(var0);
                     var0.setUpdating(false);
                     Bukkit.getServer().getPluginManager().callEvent(new SkySignUpdateEvent("", SkySignUpdateCause.STATE));
                  } else if (var0.getGame().getState() != ArenaState.ENDING && !var0.getGame().isLoading()) {
                     if (var0.getGame().getState() == ArenaState.INGAME) {
                        signFormat(var0);
                        Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
                           signFormatSearch(var0);
                           var0.setUpdating(false);
                           Bukkit.getServer().getPluginManager().callEvent(new SkySignUpdateEvent("", SkySignUpdateCause.STATE));
                        }, ConfigManager.signs.getLong("rotationDelay"));
                     } else if ((var0.getGame().getState() != ArenaState.INGAME || var0.getGame().getState() != ArenaState.ENDING) && !var0.getGame().isLoading()) {
                        signFormat(var0);
                        var0.setUpdating(false);
                     } else {
                        var0.setUpdating(false);
                     }
                  } else {
                     signFormatSearch(var0);
                     var0.setUpdating(false);
                  }
               }
            } else {
               if (var0.getGame() != null && (var0.getGame().isLoading() || var0.getGame().isDisabled())) {
                  signFormatSearch(var0);
                  var0.setUpdating(false);
                  return;
               }

               signFormat(var0);
               var0.setUpdating(false);
            }

         }
      }
   }

   private static void signFormat(SkySign var0) {
      Game var1 = var0.getGame();
      Sign var2 = var0.getSign();
      if (var2 != null) {
         org.bukkit.material.Sign var3 = null;
         if (var2.getType() == Material.WALL_SIGN) {
            var3 = (org.bukkit.material.Sign)var2.getData();
         }

         if (var3 == null) {
            SkyWars.log("SignManager.signFormat - debe ser un cartel de pared (colocado en la pared)");
         } else if (var1 == null) {
            signFormatError(var2);
            SkyWars.log("SignManager.signFormat - intentando cargar un juego nulo");
         } else {
            Block var4 = var2.getBlock().getRelative(var3.getAttachedFace());
            ItemStack var5 = null;
            String var6 = "";
            if (var1.getState() == ArenaState.WAITING) {
               var6 = SkyWars.getMessage(MSG.MOTD_WAITING);
               var5 = Utils.readItem(ConfigManager.signs.getString("state.waiting")).build();
            }

            if (var1.getState() == ArenaState.STARTING) {
               var6 = SkyWars.getMessage(MSG.MOTD_STARTING);
               var5 = Utils.readItem(ConfigManager.signs.getString("state.starting")).build();
            }

            if (var1.isFull()) {
               var6 = SkyWars.getMessage(MSG.MOTD_FULL);
               var5 = Utils.readItem(ConfigManager.signs.getString("state.full")).build();
            }

            if (var1.getState() == ArenaState.INGAME || var1.getState() == ArenaState.ENDING) {
               var6 = SkyWars.getMessage(MSG.MOTD_INGAME);
               var5 = Utils.readItem(ConfigManager.signs.getString("state.ingame")).build();
            }

            List var7 = ConfigManager.signs.getStringList("format.game");

            for(int var8 = 0; var8 < var7.size(); ++var8) {
               SignUtils.setSignText(var2, var8, ChatColor.translateAlternateColorCodes('&', ((String)var7.get(var8)).replace("%map%", var1.getDisplayName()).replace("%state%", var6).replace("%players%", var1.getAlivePlayers() + "").replace("%maxplayers%", var1.getMaxPlayers() + "")));
            }

            ItemStack finalVar = var5;
            Bukkit.getServer().getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
               if (ConfigManager.signs.getBoolean("change_block") && finalVar != null) {
                  var4.setType(finalVar.getType());
                  var4.setData((byte) finalVar.getDurability());
               }

               var2.update();
            }, 5L);
         }
      }
   }

   private static void signFormatSearch(SkySign var0) {
      Sign var1 = var0.getSign();
      if (var1 != null) {
         org.bukkit.material.Sign var2 = null;
         if (var1.getType() == Material.WALL_SIGN) {
            var2 = (org.bukkit.material.Sign)var1.getData();
         }

         if (var2 != null) {
            Block var3 = var1.getBlock().getRelative(var2.getAttachedFace());
            List var4 = ConfigManager.signs.getStringList("format.searching");

            for(int var5 = 0; var5 < var4.size(); ++var5) {
               SignUtils.setSignText(var1, var5, ChatColor.translateAlternateColorCodes('&', (String)var4.get(var5)));
            }

            if (var0.isRotation()) {
               var0.setGame((String)null);
            }

            ItemStack var6 = Utils.readItem(ConfigManager.signs.getString("state.searching")).build();
            Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
               if (ConfigManager.signs.getBoolean("change_block") && var6 != null) {
                  var3.setType(var6.getType());
                  var3.setData((byte)var6.getDurability());
               }

               var1.update();
            }, 5L);
         }
      }
   }

   private static void signFormatError(Sign var0) {
      ArrayList var1 = new ArrayList();
      var1.add("&lSkyWars");
      var1.add("&cERROR");
      var1.add("&cArena no");
      var1.add("&cencontrada");

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         SignUtils.setSignText(var0, var2, ChatColor.translateAlternateColorCodes('&', (String)var1.get(var2)));
      }

      var0.update();
      Material var6 = var0.getBlock().getType();
      org.bukkit.material.Sign var3 = null;
      if (var6 == null) {
         SkyWars.log("SignManager.signFormatError - intentando obtener un cartel pero este no existe");
      } else {
         if (var6 == Material.WALL_SIGN) {
            var3 = (org.bukkit.material.Sign)var0.getBlock().getState().getData();
         }

         if (var3 != null) {
            Block var4 = var0.getBlock().getRelative(var3.getAttachedFace());
            Material var5 = Material.BEDROCK;
            var4.setType(var5);
         }
      }
   }

   private void addSign(Location var1, String var2) {
      String var3 = LocationUtil.getString(var1, false);
      SkySign var4 = new SkySign(var3);
      var4.setRotation(ConfigManager.signs.getBoolean("rotation"));
      if (var2 != null && !var2.isEmpty()) {
         var4.setGame(var2);
      }

      locationSign.put(var3, var4);
      updateSign(var4);
   }

   private void removeSign(Location var1) {
      String var2 = LocationUtil.getString(var1, false);
      locationSign.remove(var2);
      Bukkit.getServer().getPluginManager().callEvent(new SkySignUpdateEvent("", SkySignUpdateCause.STATE));
   }

   @EventHandler
   public void onPlace(SignChangeEvent var1) {
      Block var2 = var1.getBlock();
      if ((var1.getPlayer().isOp() || var1.getPlayer().hasPermission("skywars.admin")) && var1.getLine(0).equalsIgnoreCase("[SW]")) {
         if (var2.getType() == Material.WALL_SIGN) {
            String var3 = var1.getLine(1);
            String var4 = LocationUtil.getString(var1.getBlock().getLocation(), false);
            if (var3 == null) {
               var3 = "";
            }

            Iterator var5 = locationSign.keySet().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               if (var6.equals(var4)) {
                  var1.getPlayer().sendMessage("§cYa existe un cartel en esta ubicación");
                  return;
               }
            }

            List var7 = ConfigManager.signs.getStringList("signs");
            var7.add(var4 + ";" + var3);
            ConfigManager.signs.set("signs", var7);
            ConfigManager.signs.save();
            this.addSign(var1.getBlock().getLocation(), var3);
            var1.getPlayer().sendMessage("§aCartel agregado");
         } else {
            var1.getPlayer().sendMessage("§cEste cartel debe colocarse en una pared");
         }
      }

   }

   @EventHandler
   public void onSignClick(PlayerInteractEvent var1) {
      if ((var1.getAction() == Action.RIGHT_CLICK_BLOCK || var1.getAction() == Action.RIGHT_CLICK_AIR) && var1.hasBlock() && var1.getClickedBlock().getState() instanceof Sign) {
         Sign var2 = (Sign)var1.getClickedBlock().getState();
         Location var3 = var2.getLocation();
         SkySign var4 = getSign(var3);
         if (var4 == null) {
            return;
         }

         Game var5 = var4.getGame();
         SkyPlayer var6 = SkyWars.getSkyPlayer(var1.getPlayer());
         if (var6 == null) {
            return;
         }

         if (var5 == null) {
            return;
         }

         if (var5.getState() == null) {
            return;
         }

         if (var5.isDisabled()) {
            return;
         }

         if (var5.getState() == ArenaState.INGAME && !var1.getPlayer().hasPermission("skywars.admin.spectate")) {
            var6.sendMessage(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
            return;
         }

         if (var5.getState() == ArenaState.ENDING) {
            var6.sendMessage(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
            return;
         }

         if (var5.getAlivePlayers() >= var5.getMaxPlayers() && !var1.getPlayer().hasPermission("skywars.admin.spectate")) {
            var6.sendMessage(SkyWars.getMessage(MSG.GAME_FULL_MESSAGE));
            return;
         }

         if (var5.isLoading()) {
            var6.sendMessage(SkyWars.getMessage(MSG.GAME_LOADING));
            return;
         }

         if (SkyWars.isMultiArenaMode()) {
            Arena var7 = (Arena)var5;
            var7.addPlayer(var6, ArenaJoinCause.SIGN);
         } else if (SkyWars.isLobbyMode()) {
            BungeeUtils.teleToServer(var6.getPlayer(), "", var5.getName());
         }
      }

   }

   @EventHandler
   public void onSignBreak(BlockBreakEvent var1) {
      if (var1.getBlock().getType() == Material.WALL_SIGN) {
         Iterator var2 = getSigns().iterator();

         while(true) {
            while(true) {
               SkySign var3;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var3 = (SkySign)var2.next();
               } while(!var1.getBlock().getLocation().equals(var3.getLocation()));

               if (!var1.getPlayer().isOp() && !var1.getPlayer().hasPermission("skywars.admin")) {
                  var1.setCancelled(true);
               } else {
                  this.removeSign(var3.getLocation());
                  int var4 = 0;
                  List var5 = ConfigManager.signs.getStringList("signs");

                  for(Iterator var6 = var5.iterator(); var6.hasNext(); ++var4) {
                     String var7 = (String)var6.next();
                     String var8 = var7.split(";")[0];
                     String var9 = LocationUtil.getString(var3.getLocation(), false);
                     if (var8.equals(var9)) {
                        break;
                     }
                  }

                  var5.remove(var4);
                  ConfigManager.signs.set("signs", var5);
                  ConfigManager.signs.save();
                  var1.getPlayer().sendMessage("§cCartel eliminado");
               }
            }
         }
      }
   }

   @EventHandler
   public void onSignUpdate(SkySignUpdateEvent var1) {
      Iterator var2;
      SkySign var3;
      if (var1.getArena() != null && !var1.getArena().isEmpty() && !getArenaSigns(var1.getArena()).isEmpty()) {
         var2 = getArenaSigns(var1.getArena()).iterator();

         while(var2.hasNext()) {
            var3 = (SkySign)var2.next();
            updateSign(var3);
         }
      } else {
         var2 = getSearchingSigns().iterator();

         while(var2.hasNext()) {
            var3 = (SkySign)var2.next();
            if (var3.isRotation()) {
               updateSign(var3);
               break;
            }
         }
      }

   }
}
