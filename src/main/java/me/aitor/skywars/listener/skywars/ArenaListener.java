package me.aitor.skywars.listener.skywars;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaBox;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.arena.ArenaMode;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.arena.GameQueue;
import me.aitor.skywars.arena.event.ArenaEvent;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.events.ArenaJoinEvent;
import me.aitor.skywars.events.ArenaLeaveEvent;
import me.aitor.skywars.events.ArenaTickEvent;
import me.aitor.skywars.events.enums.ArenaLeaveCause;
import me.aitor.skywars.events.enums.SpectatorReason;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.server.SkyServer;
import me.aitor.skywars.utils.BungeeUtils;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.sky.SkyHologram;
import me.aitor.skywars.utils.title.Title;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaListener implements Listener {
   public static boolean checkWinner(Arena var0) {
      if (var0.getAlivePlayers() <= 1) {
         if (var0.getAlivePlayers() == 0) {
            var0.end(true);
            return true;
         } else {
            SkyPlayer var1 = (SkyPlayer)var0.getAlivePlayer().get(0);
            if (var1 != null) {
               Player var2 = var1.getPlayer();
               if (var2 != null) {
                  Location var3 = var2.getLocation();
                  if (var3 != null) {
                     if (!SkyWars.is19orHigher()) {
                        var2.playSound(var3, Sound.valueOf("ORB_PICKUP"), 1.0F, 1.0F);
                     } else {
                        var2.playSound(var3, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                     }
                  }
               }

               var0.end(var1);
            } else {
               var0.end(true);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   @EventHandler
   public void onSkyPlayerArenaJoinEvent(ArenaJoinEvent var1) {
      SkyPlayer var2 = var1.getPlayer();
      Arena var3 = var1.getGame();
      if (Bukkit.getPluginManager().isPluginEnabled("FeatherBoard")) {
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb off " + var2.getName() + " -s");
      }

      if (var3.getState() == ArenaState.INGAME && var2.getPlayer().hasPermission("skywars.admin.spectate")) {
         var2.clearInventory(true);
         var2.setArena(var3);
         var2.teleport(var3.getSpawn());
         var2.setSpectating(true, SpectatorReason.JOIN);
         SkyHologram.removeHologram(var2);
      } else {
         if (var3.getArenaMode() == ArenaMode.SOLO) {
            Location var4 = var3.getSpawnPoint();
            SkyWars.log("Arena.addPlayer - Obtener punto de aparición " + var4);
            if (var4 == null) {
               SkyWars.log("Arena.addPlayer - Intentando añadir un jugador en un punto de aparición en uso");
               if (SkyWars.isBungeeMode()) {
                  var2.getPlayer().kickPlayer(SkyWars.getMessage(MSG.GAME_SPAWN_USED));
                  return;
               }

               var2.sendMessage(SkyWars.getMessage(MSG.GAME_SPAWN_USED));
               return;
            }

            Iterator var5 = var3.getGlassBoxes().iterator();

            while(var5.hasNext()) {
               ArenaBox var6 = (ArenaBox)var5.next();
               Location var7 = var6.getLocation();
               if (var7.equals(var4)) {
                  SkyWars.log("Arena.addPlayer - Caja seleccionada - " + var7);
                  var2.setBox(var6);
               }
            }

            SkyWars.log("Arena.addPlayer - " + var2.getName() + " se está teletransportando a " + var4.toString());
            var3.setUsed(var4, true);
            var2.setArenaSpawn(var4);
            var2.teleport(var4);
            String var11 = var2.getBoxSection();
            if (var2.getBoxSection() != null && !var11.equalsIgnoreCase(SkyWars.boxes.getString("default"))) {
               int var8;
               ArenaBox var9;
               String var13;
               int var15;
               if (var2.getBoxItem(var2.getBoxSection()) != 0) {
                  var13 = var2.getBoxSection();
                  var15 = var2.getBoxItem(var13);
                  var8 = var2.getBoxData(var13);
                  var9 = var2.getBox();
                  SkyWars.log("Arena.addPlayer - Sección de caja=" + var13 + ", Ítem de caja=" + var15 + ", Datos de caja=" + var8 + ", Caja=" + var9);
                  var9.setBox(var15, var8);
               } else {
                  var2.getPlayer().setMetadata("upload_me", new FixedMetadataValue(SkyWars.getPlugin(), true));
                  var13 = SkyWars.boxes.getString("default");
                  var2.setBoxSection(var13, true);
                  var15 = var2.getBoxItem(var13);
                  var8 = var2.getBoxData(var13);
                  var9 = var2.getBox();
                  SkyWars.log("Arena.addPlayer - Sección de caja=" + var13 + ", Ítem de caja=" + var15 + ", Datos de caja=" + var8 + ", Caja=" + var9);
                  var9.setBox(var15, var8);
               }
            }
         } else {
            var2.teleport(var3.getTeamLobby());
         }

         var2.clearInventory(true);
         var2.setArena(var3);
         SkyWars.log("Arena.addPlayer - Jugador ya en la lista: " + var3.getPlayers().contains(var2));
         if (!var3.getPlayers().contains(var2)) {
            var3.getPlayers().add(var2);
            SkyWars.log("Arena.addPlayer - Jugador añadido a la lista");
         }

         if (var2.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            var2.getPlayer().setGameMode(GameMode.SURVIVAL);
         }

         Iterator var10 = var3.getPlayers().iterator();

         while(var10.hasNext()) {
            SkyPlayer var12 = (SkyPlayer)var10.next();
            Player var14 = var12.getPlayer();
            Player var16 = var2.getPlayer();
            if (var14 != null && var16 != null && var12 != var2) {
               var14.showPlayer(var16);
            }
         }

         this.setInventoryItems(var2, var3);
         SkyWars.log("Arena.addPlayer - Se añadió correctamente a " + var2.getName() + " a " + var2.getArena().getName());
         var3.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_JOIN), var2.getName(), var3.getAlivePlayers(), var3.getMaxPlayers()));
         var2.setSpectating(false, SpectatorReason.JOIN);
         SkyHologram.removeHologram(var2);
         GameQueue.removePlayer(var2);
      }
   }

   private void setInventoryItems(SkyPlayer var1, Arena var2) {
      PlayerInventory var3 = var1.getPlayer().getInventory();
      var3.clear();
      String[] var4 = ConfigManager.main.getString("item.kits").split(" ");
      Material var5 = Material.PAPER;
      int var6 = 1;
      if (var4.length >= 2) {
         try {
            var6 = Integer.parseInt(var4[0]);
            var5 = Material.matchMaterial(var4[1]);
         } catch (NumberFormatException var20) {
            SkyWars.logError("Formato de ítem incorrecto en config.yml (item.kits)");
         }
      }

      String[] var7 = ConfigManager.main.getString("item.settings").split(" ");
      Material var8 = Material.DIAMOND;
      int var9 = 2;
      if (var7.length >= 2) {
         try {
            var9 = Integer.parseInt(var7[0]);
            var8 = Material.matchMaterial(var7[1]);
         } catch (NumberFormatException var19) {
            SkyWars.logError("Formato de ítem incorrecto en config.yml (item.settings)");
         }
      }

      String[] var10 = ConfigManager.main.getString("item.vote").split(" ");
      Material var11 = Material.EMPTY_MAP;
      int var12 = 3;
      if (var10.length >= 2) {
         try {
            var12 = Integer.parseInt(var10[0]);
            var11 = Material.matchMaterial(var10[1]);
         } catch (NumberFormatException var18) {
            SkyWars.logError("Formato de ítem incorrecto en config.yml (item.vote)");
         }
      }

      String[] var13 = ConfigManager.main.getString("item.exit").split(" ");
      Material var14 = Material.BED;
      int var15 = 9;
      if (var13.length >= 2) {
         try {
            var15 = Integer.parseInt(var13[0]);
            var14 = Material.matchMaterial(var13[1]);
         } catch (NumberFormatException var17) {
            SkyWars.logError("Formato de ítem incorrecto en config.yml (item.exit)");
         }
      }

      var3.setItem(var6 - 1, (new ItemBuilder(var5)).setTitle(SkyWars.getMessage(MSG.ITEM_KITS_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_KITS_LORE)).build());
      var3.setItem(var9 - 1, (new ItemBuilder(var8)).setTitle(SkyWars.getMessage(MSG.ITEM_SETTINGS_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_SETTINGS_LORE)).build());
      if ((var2.getConfig().getBoolean("options.vote.chest") || var2.getConfig().getBoolean("options.vote.time")) && var2.getArenaMode() == ArenaMode.SOLO) {
         var3.setItem(var12 - 1, (new ItemBuilder(var11)).setTitle(SkyWars.getMessage(MSG.ITEM_VOTE_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_VOTE_LORE)).build());
      }

      if (!SkyWars.getPlugin().getConfig().getBoolean("options.disableLeaveItem")) {
         var3.setItem(var15 - 1, (new ItemBuilder(var14)).setTitle(SkyWars.getMessage(MSG.ITEM_SPECTATOR_EXIT_NAME)).build());
      }

      var1.updateInventory();
   }

   @EventHandler
   public void onSkyPlayerArenaLeaveEvent(ArenaLeaveEvent var1) {
      SkyPlayer var2 = var1.getPlayer();
      Arena var3 = var1.getGame();
      Player var4 = var2.getPlayer();
      if (!var2.isSpectating() && var1.getCause() != ArenaLeaveCause.RESTART) {
         var3.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_QUIT), var2.getName(), var3.getAlivePlayers(), var3.getMaxPlayers()));
      }

      var2.setSpectating(false, SpectatorReason.LEAVE);
      SkyWars.log("Arena.removePlayer - Eliminando a " + var2.getName() + " de " + var3.getName() + " por causa: " + var1.getCause());
      if (var4.isOnline()) {
         var3.getPlayers().remove(var2);
         SkyWars.log("Arena.removePlayer - Eliminación exitosa de " + var2.getName() + " de " + var3.getName());
      }

      String var5 = SkyWars.boxes.getString("default");
      if (var2.getBox() != null) {
         var2.getBox().setBox(var2.getBoxItem(var5), var2.getBoxData(var5));
      }

      var3.resetPlayer(var2);
      if (!SkyWars.isBungeeMode()) {
         var2.upload(false);
         SkyWars.goToSpawn(var2);
         var4.setFallDistance(0.0F);
         if (Bukkit.getPluginManager().isPluginEnabled("FeatherBoard")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb on " + var2.getName() + " -s");
         }
      }

   }

   @EventHandler
   public void onArenaTick(ArenaTickEvent var1) {
      Arena var2 = var1.getArena();
      SkyServer.setValues(var2);
      if (var2.getState() == ArenaState.INGAME) {
         if (checkWinner(var2)) {
            return;
         }

         if (!var2.isFallDamage()) {
            if (var2.getStartCountdown() == -5) {
               var2.setFallDamage(true);
            }

            var2.setStartCountdown(var2.getStartCountdown() - 1);
         }

         this.countEvents(var2);
         this.countMaxTime(var2);
      }

      if (var2.getState() == ArenaState.WAITING || var2.getState() == ArenaState.STARTING) {
         int var3 = var2.getStartCountdown();
         if (this.checkEmpty(var2)) {
            return;
         }

         if (var3 == 0) {
            if (var2.getPlayers().size() < var2.getMinPlayers() && !var2.isForceStart()) {
               var2.setStartCountdown(var2.getStartFullCountdown());
               var2.broadcast(SkyWars.getMessage(MSG.GAME_START_NOREQUIREDPLAYERS));
               return;
            }

            if (var2.getState() == ArenaState.STARTING) {
               var2.start();
               Iterator var4 = var2.getPlayers().iterator();

               while(var4.hasNext()) {
                  SkyPlayer var5 = (SkyPlayer)var4.next();
                  var5.getPlayer().setLevel(0);
               }

               return;
            }
         }

         if (var2.getState() == ArenaState.WAITING) {
            if (var2.getPlayers().size() < var2.getMinPlayers() && !var2.isForceStart()) {
               return;
            }

            if (var2.isForceStart()) {
               var2.setStartCountdown(var2.getStartFullCountdown());
               var3 = var2.getStartCountdown();
            } else if (var2.getPlayers().size() >= var2.getMaxPlayers()) {
               if (var3 > var2.getStartFullCountdown()) {
                  var2.setStartCountdown(var2.getStartFullCountdown());
                  var3 = var2.getStartCountdown();
               }

               var2.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_NOWFULL), var3));
            }

            if (var3 <= var2.getStartFullCountdown()) {
               var2.setState(ArenaState.STARTING);
            }
         }

         this.countStart(var2);
      }

      if (var2.getState() == ArenaState.ENDING) {
         this.countEnd(var2);
      }

   }

   private void countMaxTime(Arena var1) {
      int var2 = var1.getMaxTimeCountdown();
      if (var2 % 60 == 0 && var2 <= 300 && var2 > 0) {
         var1.broadcast(String.format(SkyWars.getMessage(MSG.GAME_TIME_LEFT_MINUTES), var2 % 3600 / 60));
      }

      if (var2 % 1 == 0 && (var2 == 10 || var2 <= 5) && var2 > 0) {
         var1.broadcast(String.format(SkyWars.getMessage(MSG.GAME_TIME_LEFT_SECONDS), var2));
      }

      if (var2 == 0) {
         var1.broadcast(SkyWars.getMessage(MSG.GAME_TIME_LIMIT));
         var1.setState(ArenaState.ENDING);
         var1.end(true);
      }

      var1.setMaxTimeCountdown(var1.getMaxTimeCountdown() - 1);
   }

   private void countStart(Arena var1) {
      int var2 = var1.getStartCountdown();
      Iterator var3 = var1.getPlayers().iterator();

      while(var3.hasNext()) {
         SkyPlayer var4 = (SkyPlayer)var3.next();
         if (var2 >= 0) {
            Player var5 = var4.getPlayer();
            if (var5 != null) {
               var5.setLevel(var2);
            }
         }
      }

      if (var1.getStartingCounts().contains(var2)) {
         if (var2 >= 60) {
            int var6 = var2 % 3600 / 60;
            int var7 = var2 % 60;
            if (var7 == 0) {
               var1.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN_MINUTES), var6));
            } else {
               var1.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN_MINUTES_SECONDS), var6, var7));
            }
         } else {
            var1.getPlayers().forEach((var1x) -> {
               Player var2x = var1x.getPlayer();
               if (var2x != null) {
                  if (!SkyWars.is19orHigher()) {
                     var2x.playSound(var2x.getLocation(), Sound.valueOf("CLICK"), 1.0F, 1.0F);
                  } else {
                     var2x.playSound(var2x.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
                  }

                  if (var2 <= 5 && SkyWars.is18orHigher()) {
                     Title title = new Title(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN_ALERT), var2), 0, 25, 0);
                     title.send(var2x);
                  }

               }
            });
            if (var2 == 1) {
               var1.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN_ONE), var2));
            } else {
               var1.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN), var2));
            }
         }
      }

      var1.setStartCountdown(var2 - 1);
   }

   private void countEnd(Arena var1) {
      int var2 = var1.getEndCountdown();
      Iterator var3;
      Player var4;
      if (var2 == 0) {
         if (SkyWars.isBungeeMode()) {
            var3 = Bukkit.getOnlinePlayers().iterator();

            while(var3.hasNext()) {
               var4 = (Player)var3.next();
               BungeeUtils.teleToServer(var4, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
            }
         } else {
            var1.restart();
         }
      }

      if (var2 == -3 && SkyWars.isBungeeMode()) {
         if (SkyWars.isAutoStart()) {
            var3 = Bukkit.getOnlinePlayers().iterator();

            while(var3.hasNext()) {
               var4 = (Player)var3.next();
               var4.kickPlayer(SkyWars.getMessage(MSG.GAME_RESTART));
            }

            if (SkyWars.isRandomMap()) {
               var3 = var1.getTimers().iterator();

               while(var3.hasNext()) {
                  BukkitRunnable var5 = (BukkitRunnable)var3.next();
                  var5.cancel();
               }

               var1.getTicks().cancel();
               ArenaManager.initGames();
            } else {
               var1.restart();
            }
         } else {
            Bukkit.shutdown();
         }
      }

      var1.setEndCountdown(var1.getEndCountdown() - 1);
   }

   private void countEvents(Arena var1) {
      if (var1.getEvents().size() != 0 && var1.getConfig().getBoolean("options.events") && var1.getState() == ArenaState.INGAME) {
         ArenaEvent var2 = (ArenaEvent)var1.getEvents().get(0);
         var2.setSeconds(var2.getSeconds() - 1);
         if (var2.getSeconds() <= 0) {
            var2.playEvent(var1);
            var1.getEvents().remove(0);
         }

      }
   }

   private boolean checkEmpty(Arena var1) {
      if (var1.getPlayers().size() <= 1) {
         var1.setStartCountdown(var1.getConfig().getInt("countdown.starting"));
         var1.setForceStart(false);
         if (var1.getState() != ArenaState.WAITING) {
            var1.setState(ArenaState.WAITING);
         }

         return true;
      } else {
         return false;
      }
   }
}
