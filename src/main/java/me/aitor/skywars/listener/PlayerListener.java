package me.aitor.skywars.listener;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaMode;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.arena.chest.ChestType;
import me.aitor.skywars.arena.chest.ChestTypeManager;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.Utils;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
   @EventHandler
   public void onBlockBreak(BlockBreakEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onBlockBreak - jugador nulo");
      } else {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var3.isSpectating()) {
               var1.setCancelled(true);
            }
            if (var4.getState() == ArenaState.WAITING || var4.getState() == ArenaState.STARTING || var4.getState() == ArenaState.ENDING) {
               var1.setCancelled(true);
            }
            if (var4.getState() == ArenaState.INGAME) {
               Block var5 = var1.getBlock();
               Location var6 = var5.getLocation();
               if (var5.getState() instanceof Chest) {
                  Chest var7 = (Chest)var5.getState();
                  if (!var4.isFilled(var6) && !var4.getDontFill().contains(var6)) {
                     Inventory var8 = var7.getInventory();
                     ChestType var9 = ChestTypeManager.getChestType(var4.getChest());
                     var9.fillChest(var8);
                  }
                  var4.removeFilled(var6);
               }
            }
         }
      }
   }

   @EventHandler
   public void onChestOpen(PlayerInteractEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onChestOpen - jugador nulo");
      } else {
         if (var1.getAction().equals(Action.RIGHT_CLICK_BLOCK) || var1.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block var4 = var1.getClickedBlock();
            Location var5 = var4.getLocation();
            if (var4.getState() instanceof Chest && var3.isInArena()) {
               Arena var6 = var3.getArena();
               Chest var7 = (Chest)var4.getState();
               if (var6.getState() == ArenaState.INGAME && !var6.isFilled(var5) && !var6.getDontFill().contains(var5)) {
                  var6.addFilled(var5);
                  Inventory var8 = var7.getInventory();
                  ChestType var9 = ChestTypeManager.getChestType(var6.getChest());
                  var9.fillChest(var8);
               }
            }
         }
      }
   }

   @EventHandler
   public void onPlaceBlock(BlockPlaceEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onPlaceBlock - jugador nulo");
      } else {
         if (var3.isInArena()) {
            if (var3.isSpectating()) {
               var1.setCancelled(true);
            }
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.INGAME) {
               Block var5 = var1.getBlock();
               Location var6 = var5.getLocation();
               if (var5.getState() instanceof Chest) {
                  var4.getDontFill().add(var6);
                  var4.removeFilled(var6);
               }
            }
         }
      }
   }

   @EventHandler
   public void onHunger(FoodLevelChangeEvent var1) {
      Player var2 = (Player)var1.getEntity();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onHunger - jugador nulo");
      } else {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.WAITING || var4.getState() == ArenaState.STARTING || var4.getState() == ArenaState.ENDING) {
               var1.setCancelled(true);
            }
            if (var3.isSpectating()) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @EventHandler
   public void onMove(PlayerMoveEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onMove - jugador nulo");
      } else {
         if (var3.isInArena() && !var2.isDead()) {
            Arena var4 = var3.getArena();
            if (var4 == null) {
               return;
            }
            if (var3.getPlayer().getWorld() == Bukkit.getWorlds().get(0)) {
               var3.teleport(var4.getSpawn());
            }
            if (var4.getState() != ArenaState.INGAME && var4.getState() != ArenaState.ENDING) {
               if (var4.getArenaMode() == ArenaMode.TEAM) {
                  if (var4.getState() == ArenaState.WAITING && (var2.getLocation().getY() <= 0.0D || !var2.getWorld().equals(var4.getTeamLobby().getWorld()))) {
                     var3.teleport(var4.getTeamLobby());
                  }
                  if (var4.getState() == ArenaState.STARTING && var2.getWorld().equals(var4.getWorld()) && var2.getLocation().distanceSquared(var3.getArenaSpawn()) >= 2.0D) {
                     var3.teleport(var3.getArenaSpawn());
                  }
                  return;
               }
               if (var2.getWorld().equals(var4.getWorld()) && var2.getLocation().distanceSquared(var3.getArenaSpawn()) >= 2.0D && (var4.getState() == ArenaState.WAITING || var4.getState() == ArenaState.STARTING)) {
                  var3.teleport(var3.getArenaSpawn());
               }
            } else {
               if (!var2.getWorld().equals(var4.getWorld())) {
                  var3.teleport(var3.getArenaSpawn());
               }
               if (var2.getLocation().getY() <= 0.0D && (var3.isSpectating() || var4.getState() == ArenaState.ENDING)) {
                  var3.teleport(var4.getSpawn());
               }
               if (var2.getLocation().getY() <= -6.0D) {
                  if (DamageListener.lastDamage.containsKey(var2.getUniqueId())) {
                     Player var5 = Bukkit.getPlayer((UUID)DamageListener.lastDamage.get(var2.getUniqueId()));
                     var2.damage(1000.0D, var5);
                  } else {
                     var2.setHealth(0.0D);
                  }
                  var3.teleport(var4.getSpawn());
               }
            }
         }
      }
   }

   @EventHandler
   public void onDrop(PlayerDropItemEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onDrop - jugador nulo");
      } else {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.WAITING || var4.getState() == ArenaState.STARTING) {
               var1.setCancelled(true);
            }
            if (var3.isSpectating()) {
               var1.setCancelled(true);
            }
         } else if (ConfigManager.shop.getBoolean("item.enabled")) {
            ItemStack var6 = var1.getItemDrop().getItemStack();
            ItemBuilder var5 = Utils.readItem(ConfigManager.shop.getString("item.item"));
            var5.setTitle(ConfigManager.shop.getString("item.name")).setLore(ConfigManager.shop.getStringList("item.lore"));
            if (var6.isSimilar(var5.build())) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @EventHandler
   public void onPickUp(PlayerPickupItemEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onPickUp - jugador nulo");
      } else {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.WAITING || var4.getState() == ArenaState.STARTING || var4.getState() == ArenaState.ENDING) {
               var1.setCancelled(true);
            }
            if (var3.isSpectating()) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @EventHandler
   public void onAnimation(BlockDamageEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onAnimation - jugador nulo");
      } else {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.WAITING || var4.getState() == ArenaState.STARTING) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @EventHandler
   public void onChat(AsyncPlayerChatEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 == null) {
         SkyWars.log("PlayerListener.onChat - jugador nulo");
      } else {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if ((var4.getState() == ArenaState.WAITING || var4.getState() == ArenaState.STARTING) && !var2.hasPermission("skywars.vip.talk")) {
               var1.setCancelled(true);
               var3.sendMessage(SkyWars.getMessage(MSG.GAME_PLAYER_TALK));
            }
         }
         if (SkyWars.isMultiArenaMode() && !ConfigManager.main.getBoolean("options.disablePerWorldChat")) {
            Iterator var6 = Bukkit.getServer().getOnlinePlayers().iterator();
            while(var6.hasNext()) {
               Player var5 = (Player)var6.next();
               if (var5.getWorld() != var2.getWorld() && var1.getRecipients().contains(var5)) {
                  var1.getRecipients().remove(var5);
               }
            }
         }
      }
   }

   @EventHandler
   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 != null) {
         if (var3.isInArena()) {
            String[] var4 = var1.getMessage().split(" ");
            var4[0] = var4[0].replace("/", "");
            if (ConfigManager.main.getString("block.commands.mode").equalsIgnoreCase("blacklist")) {
               if (ConfigManager.main.getStringList("block.commands.ingame").contains(var4[0])) {
                  var3.sendMessage(SkyWars.getMessage(MSG.GAME_PLAYER_BLOCKEDCOMMANDS));
                  var1.setCancelled(true);
               }
            } else {
               if (var4[0].equalsIgnoreCase("salir") || var4[0].equalsIgnoreCase("leave")) {
                  return;
               }
               if (!ConfigManager.main.getStringList("block.commands.ingame").contains(var4[0])) {
                  var3.sendMessage(SkyWars.getMessage(MSG.GAME_PLAYER_BLOCKEDCOMMANDS));
                  var1.setCancelled(true);
               }
            }
         }
      }
   }
}
