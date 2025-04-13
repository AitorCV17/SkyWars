package me.aitor.skywars.listener;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.abilities.Ability;
import me.aitor.skywars.abilities.AbilityManager;
import me.aitor.skywars.abilities.AbilityType;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.arena.chest.RandomItem;
import me.aitor.skywars.player.SkyPlayer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class AbilitiesListener implements Listener {
   @EventHandler
   public void onTripleArrowShot(EntityShootBowEvent var1) {
      AbilityType var2 = AbilityType.TRIPLE_ARROW;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         if (var1.getEntity() instanceof Player) {
            Player var4 = (Player)var1.getEntity();
            SkyPlayer var5 = SkyWars.getSkyPlayer(var4);
            if (var5 == null) {
               return;
            }

            if (var5.isInArena() && !var5.isSpectating()) {
               Arena var6 = var5.getArena();
               if (var6.getState() == ArenaState.INGAME && var6.isAbilitiesEnabled() && var5.hasAbility(var2) && !var5.isAbilityDisabled(var2) && this.getChance() < (double)var5.getAbilityLevel(var2).getChance()) {
                  Arrow var7 = (Arrow)var4.launchProjectile(Arrow.class);
                  Arrow var8 = (Arrow)var4.launchProjectile(Arrow.class);
                  var7.setCustomName("ArrowKeyName-1920");
                  var8.setCustomName("ArrowKeyName-1920");
                  Arrow var9 = (Arrow)var1.getProjectile();
                  if (var9.isCritical()) {
                     var7.setCritical(true);
                     var8.setCritical(true);
                  }

                  if (!this.getDirection(var4.getLocation().getYaw()).equals("EAST") && !this.getDirection(var4.getLocation().getYaw()).equals("WEST")) {
                     var7.setVelocity(var9.getVelocity().add(new Vector(-0.2D, 0.0D, 0.0D)));
                     var8.setVelocity(var9.getVelocity().add(new Vector(0.2D, 0.0D, 0.0D)));
                  } else {
                     var7.setVelocity(var9.getVelocity().add(new Vector(0.0D, 0.0D, -0.2D)));
                     var8.setVelocity(var9.getVelocity().add(new Vector(0.0D, 0.0D, 0.2D)));
                  }
               }
            }
         }

      }
   }

   @EventHandler
   public void onTripleArrowHit(ProjectileHitEvent var1) {
      if (var1.getEntity() instanceof Arrow) {
         Arrow var2 = (Arrow)var1.getEntity();
         if (var2.getCustomName() == null || var2.getCustomName().isEmpty()) {
            return;
         }

         if (var2.getCustomName().equals("ArrowKeyName-1920")) {
            var2.remove();
         }
      }

   }

   @EventHandler
   public void onAdrenalineBoost(EntityDamageByEntityEvent var1) {
      AbilityType var2 = AbilityType.ADRENALINE_BOOST;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         if (var1.getDamager() instanceof Player && var1.getEntity() instanceof Player) {
            Player var4 = (Player)var1.getEntity();
            SkyPlayer var5 = SkyWars.getSkyPlayer(var4);
            if (var5 == null) {
               return;
            }

            if (var5.isInArena() && !var5.isSpectating()) {
               Arena var6 = var5.getArena();
               if (var6.getState() == ArenaState.INGAME && var6.isAbilitiesEnabled() && var5.hasAbility(var2) && !var5.isAbilityDisabled(var2) && this.getChance() < (double)var5.getAbilityLevel(var2).getChance()) {
                  var4.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
               }
            }
         }

      }
   }

   @EventHandler
   public void onFeatherWeight(EntityDamageEvent var1) {
      AbilityType var2 = AbilityType.FEATHER_WEIGHT;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         if (var1.getCause() == DamageCause.FALL && var1.getEntity() instanceof Player) {
            Player var4 = (Player)var1.getEntity();
            SkyPlayer var5 = SkyWars.getSkyPlayer(var4);
            if (var5 == null) {
               return;
            }

            if (var5.isInArena() && !var5.isSpectating()) {
               Arena var6 = var5.getArena();
               if (var6.getState() == ArenaState.INGAME && var6.isAbilitiesEnabled() && var5.hasAbility(var2) && !var5.isAbilityDisabled(var2) && this.getChance() < (double)var5.getAbilityLevel(var2).getChance()) {
                  var1.setDamage(this.getDamageReduction(var1.getDamage(), var5.getAbilityLevel(var2).getValue()));
               }
            }
         }

      }
   }

   @EventHandler
   public void onAggressiveMode(EntityDamageByEntityEvent var1) {
      AbilityType var2 = AbilityType.AGGRESSIVE_MODE;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         if (var1.getDamager() instanceof Player && var1.getEntity() instanceof Player) {
            Player var4 = (Player)var1.getDamager();
            SkyPlayer var5 = SkyWars.getSkyPlayer(var4);
            if (var5 == null) {
               return;
            }

            if (var4.getHealth() < 10.0D && var5.isInArena() && !var5.isSpectating()) {
               Arena var6 = var5.getArena();
               if (var6.getState() == ArenaState.INGAME && var6.isAbilitiesEnabled() && var5.hasAbility(var2) && !var5.isAbilityDisabled(var2) && this.getChance() < (double)var5.getAbilityLevel(var2).getChance()) {
                  var1.setDamage(this.getDamageIncrement(var1.getDamage(), var5.getAbilityLevel(var2).getValue()));
               }
            }
         }

      }
   }

   @EventHandler
   public void onArrowTank(EntityDamageByEntityEvent var1) {
      AbilityType var2 = AbilityType.ARROW_TANK;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         if (var1.getCause() == DamageCause.PROJECTILE && var1.getEntity() instanceof Player && var1.getDamager() instanceof Arrow) {
            Player var4 = (Player)var1.getEntity();
            SkyPlayer var5 = SkyWars.getSkyPlayer(var4);
            if (var5 == null) {
               return;
            }

            if (var5.isInArena() && !var5.isSpectating()) {
               Arena var6 = var5.getArena();
               if (var6.getState() == ArenaState.INGAME && var6.isAbilitiesEnabled() && var5.hasAbility(var2) && !var5.isAbilityDisabled(var2) && this.getChance() < (double)var5.getAbilityLevel(var2).getChance()) {
                  var1.setDamage(this.getDamageReduction(var1.getDamage(), var5.getAbilityLevel(var2).getValue()));
               }
            }
         }

      }
   }

   @EventHandler
   public void onMiningLuck(BlockBreakEvent var1) {
      AbilityType var2 = AbilityType.MINING_LUCK;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         Player var4 = var1.getPlayer();
         Material var5;
         if (SkyWars.is19orHigher()) {
            var5 = var4.getInventory().getItemInMainHand().getType();
         } else {
            var5 = var4.getItemInHand().getType();
         }

         if (var5 == Material.WOOD_PICKAXE || var5 == Material.STONE_PICKAXE || var5 == Material.IRON_PICKAXE || var5 == Material.GOLD_PICKAXE || var5 == Material.DIAMOND_PICKAXE) {
            SkyPlayer var6 = SkyWars.getSkyPlayer(var4);
            if (var6 == null) {
               return;
            }

            if (var6.isInArena() && !var6.isSpectating()) {
               Arena var7 = var6.getArena();
               if (var7.getState() == ArenaState.INGAME && var7.isAbilitiesEnabled() && var6.hasAbility(var2) && !var6.isAbilityDisabled(var2) && this.getChance() < (double)var6.getAbilityLevel(var2).getChance()) {
                  Iterator var8 = this.getOres().iterator();

                  while(var8.hasNext()) {
                     Material var9 = (Material)var8.next();
                     if (var1.getBlock().getType() == var9) {
                        if (var9 == Material.LAPIS_ORE) {
                           var1.getBlock().getWorld().dropItemNaturally(var1.getBlock().getLocation(), new ItemStack(this.getOreDrop(var9), 1, (short)4));
                        } else {
                           var1.getBlock().getWorld().dropItemNaturally(var1.getBlock().getLocation(), new ItemStack(this.getOreDrop(var9), 1));
                        }
                     }
                  }
               }
            }
         }

      }
   }

   @EventHandler
   public void onWoodchoppingLuck(BlockBreakEvent var1) {
      AbilityType var2 = AbilityType.WOODCHOPPING_LUCK;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         Player var4 = var1.getPlayer();
         Material var5;
         if (SkyWars.is19orHigher()) {
            var5 = var4.getInventory().getItemInMainHand().getType();
         } else {
            var5 = var4.getItemInHand().getType();
         }

         if (var5 == Material.WOOD_AXE || var5 == Material.STONE_AXE || var5 == Material.IRON_AXE || var5 == Material.GOLD_AXE || var5 == Material.DIAMOND_AXE) {
            SkyPlayer var6 = SkyWars.getSkyPlayer(var4);
            if (var6 == null) {
               return;
            }

            if (var6.isInArena() && !var6.isSpectating()) {
               Arena var7 = var6.getArena();
               if (var7.getState() == ArenaState.INGAME && var7.isAbilitiesEnabled() && var6.hasAbility(var2) && !var6.isAbilityDisabled(var2) && this.getChance() < (double)var6.getAbilityLevel(var2).getChance() && var1.getBlock().getType() == Material.LOG) {
                  var1.getBlock().getWorld().dropItemNaturally(var1.getBlock().getLocation(), new ItemStack(Material.LOG, 1));
               }
            }
         }

      }
   }

   @EventHandler
   public void onPirate(BlockBreakEvent var1) {
      AbilityType var2 = AbilityType.PIRATE;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         Player var4 = var1.getPlayer();
         SkyPlayer var5 = SkyWars.getSkyPlayer(var4);
         if (var5 != null) {
            if (var5.isInArena() && !var5.isSpectating()) {
               Arena var6 = var5.getArena();
               if (var6.getState() == ArenaState.INGAME && var6.isAbilitiesEnabled() && var5.hasAbility(var2) && !var5.isAbilityDisabled(var2) && this.getChance() < (double)var5.getAbilityLevel(var2).getChance() && (var1.getBlock().getType() == Material.DIRT || var1.getBlock().getType() == Material.GRASS)) {
                  var1.getBlock().getWorld().dropItemNaturally(var1.getBlock().getLocation(), this.getRandomTreasure());
               }
            }

         }
      }
   }

   @EventHandler
   public void onFireResistance(EntityDamageEvent var1) {
      AbilityType var2 = AbilityType.FIRE_RESISTANCE;
      Ability var3 = AbilityManager.getAbilityByType(var2);
      if (var3 == null) {
         SkyWars.logError("No se pudo obtener la habilidad, algo está mal con " + var2 + " (quizás fue eliminada)");
      } else if (AbilityManager.getEnabledAbilitiesList().contains(var3.getName())) {
         if ((var1.getCause() == DamageCause.FIRE || var1.getCause() == DamageCause.FIRE_TICK || var1.getCause() == DamageCause.LAVA) && var1.getEntity() instanceof Player) {
            Player var4 = (Player)var1.getEntity();
            SkyPlayer var5 = SkyWars.getSkyPlayer(var4);
            if (var5 == null) {
               return;
            }

            if (var5.isInArena() && !var5.isSpectating()) {
               Arena var6 = var5.getArena();
               if (var6.getState() == ArenaState.INGAME && var6.isAbilitiesEnabled() && var5.hasAbility(var2) && !var5.isAbilityDisabled(var2) && this.getChance() < (double)var5.getAbilityLevel(var2).getChance()) {
                  var1.setDamage(this.getDamageReduction(var1.getDamage(), var5.getAbilityLevel(var2).getValue()));
               }
            }
         }

      }
   }

   private double getChance() {
      return Math.random() * 100.0D;
   }

   private double getDamageReduction(double var1, int var3) {
      return var1 - var1 * ((double)var3 / 100.0D);
   }

   private double getDamageIncrement(double var1, int var3) {
      return var1 + var1 * ((double)var3 / 100.0D);
   }

   private String getDirection(Float var1) {
      var1 = var1 / 90.0F;
      var1 = (float)Math.round(var1);
      if (var1 != -4.0F && var1 != 0.0F && var1 != 4.0F) {
         if (var1 != -1.0F && var1 != 3.0F) {
            if (var1 != -2.0F && var1 != 2.0F) {
               return var1 != -3.0F && var1 != 1.0F ? "" : "WEST";
            } else {
               return "NORTH";
            }
         } else {
            return "EAST";
         }
      } else {
         return "SOUTH";
      }
   }

   private List<Material> getOres() {
      ArrayList var1 = new ArrayList();
      var1.add(Material.STONE);
      var1.add(Material.COAL_ORE);
      var1.add(Material.IRON_ORE);
      var1.add(Material.GOLD_ORE);
      var1.add(Material.DIAMOND_ORE);
      var1.add(Material.EMERALD_ORE);
      var1.add(Material.REDSTONE_ORE);
      var1.add(Material.LAPIS_ORE);
      return var1;
   }

   private Material getOreDrop(Material var1) {
      if (var1 == Material.STONE) {
         return Material.COBBLESTONE;
      } else if (var1 == Material.COAL_ORE) {
         return Material.COAL;
      } else if (var1 == Material.IRON_ORE) {
         return Material.IRON_ORE;
      } else if (var1 == Material.GOLD_ORE) {
         return Material.GOLD_ORE;
      } else if (var1 == Material.DIAMOND_ORE) {
         return Material.DIAMOND;
      } else if (var1 == Material.EMERALD_ORE) {
         return Material.EMERALD;
      } else if (var1 == Material.REDSTONE_ORE) {
         return Material.REDSTONE;
      } else {
         return var1 == Material.LAPIS_ORE ? Material.INK_SACK : null;
      }
   }

   private ItemStack getRandomTreasure() {
      int var1 = (new Random()).nextInt(AbilityManager.treasureItems.size());
      return ((RandomItem)AbilityManager.treasureItems.get(var1)).getItem().build();
   }
}
