package me.aitor.skywars.player;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.abilities.Ability;
import me.aitor.skywars.abilities.AbilityLevel;
import me.aitor.skywars.abilities.AbilityManager;
import me.aitor.skywars.abilities.AbilityType;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaBox;
import me.aitor.skywars.arena.chest.ChestType;
import me.aitor.skywars.arena.chest.ChestTypeManager;
import me.aitor.skywars.database.DatabaseHandler;
import me.aitor.skywars.events.SkyPlayerSpectatorEvent;
import me.aitor.skywars.events.enums.SpectatorReason;
import me.aitor.skywars.kit.Kit;
import me.aitor.skywars.utils.Utils;
import me.aitor.skywars.utils.Utils19;
import me.aitor.skywars.utils.economy.SkyEconomyManager;
import me.aitor.skywars.utils.economy.skyeconomy.CustomEconomy;
import me.aitor.skywars.utils.sky.SkyData;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyPlayer extends SkyData {
   private String name;
   private String uid = "382269";
   private UUID uniqueId;
   private String url = "-1492943149";
   private Arena arena;
   private Kit kit;
   private ArenaBox box;
   private String trail;
   private String boxsection;
   private final HashSet<String> owned_kits = new HashSet();
   private final HashMap<AbilityType, AbilityLevel> ownedAbilityByType = new HashMap();
   private final HashSet<AbilityType> disabledAbilities = new HashSet();
   private Location arena_spawn;
   private boolean spectating;
   private int wins = 0;
   private int kills = 0;
   private int deaths = 0;
   private int played = 0;
   private int arrow_shot = 0;
   private int arrow_hit = 0;
   private int blocks_broken = 0;
   private int blocks_placed = 0;
   private int distance_walked = 0;
   private int time_played = 0;
   private double coins = 0.0D;
   private Date local_time_played = null;
   private ItemStack[] armourContents = null;
   private ItemStack[] inventoryContents = null;
   private Integer xplevel = 0;
   private float exp = 0.0F;

   public SkyPlayer(String var1, UUID var2) {
      this.name = var1;
      this.uniqueId = var2;
      this.addData("upload_data", false);
      this.load();
   }

   public Player getPlayer() {
      return Bukkit.getPlayer(this.uniqueId) != null ? Bukkit.getPlayer(this.uniqueId) : Bukkit.getPlayer(this.name);
   }

   public String getName() {
      return this.name;
   }

   public String getUID() {
      return this.uid;
   }

   public UUID getUniqueId() {
      return this.uniqueId;
   }

   public Arena getArena() {
      return this.arena;
   }

   public Kit getKit() {
      return this.kit;
   }

   public ArenaBox getBox() {
      return this.box;
   }

   public String getTrail() {
      return this.trail;
   }

   public int getWins() {
      return this.wins;
   }

   public void setWins(int var1) {
      this.wins = var1;
   }

   public int getKills() {
      return this.kills;
   }

   public void setKills(int var1) {
      this.kills = var1;
   }

   public int getDeaths() {
      return this.deaths;
   }

   public void setDeaths(int var1) {
      this.deaths = var1;
   }

   public int getPlayed() {
      return this.played;
   }

   public void setPlayed(int var1) {
      this.played = var1;
   }

   public int getArrowShot() {
      return this.arrow_shot;
   }

   public void setArrowShot(int var1) {
      this.arrow_shot = var1;
   }

   public int getArrowHit() {
      return this.arrow_hit;
   }

   public void setArrowHit(int var1) {
      this.arrow_hit = var1;
   }

   public int getBlocksBroken() {
      return this.blocks_broken;
   }

   public void setBlocksBroken(int var1) {
      this.blocks_broken = var1;
   }

   public int getBlocksPlaced() {
      return this.blocks_placed;
   }

   public void setBlocksPlaced(int var1) {
      this.blocks_placed = var1;
   }

   public int getTimePlayed() {
      return this.time_played;
   }

   public void setTimePlayed(int var1) {
      this.time_played = var1;
   }

   public int getDistanceWalked() {
      return this.distance_walked;
   }

   public void setDistanceWalked(int var1) {
      this.distance_walked = var1;
   }

   public Location getArenaSpawn() {
      return this.getArena() != null ? this.arena_spawn : null;
   }

   public void addWins(int var1) {
      this.setWins(this.getWins() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addKills(int var1) {
      this.setKills(this.getKills() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addDeaths(int var1) {
      this.setDeaths(this.getDeaths() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addPlayed(int var1) {
      this.setPlayed(this.getPlayed() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addArrowShot(int var1) {
      this.setArrowShot(this.getArrowShot() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addArrowHit(int var1) {
      this.setArrowHit(this.getArrowHit() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addBlocksBroken(int var1) {
      this.setBlocksBroken(this.getBlocksBroken() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addBlocksPlaced(int var1) {
      this.setBlocksPlaced(this.getBlocksPlaced() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addDistanceWalked(int var1) {
      this.setDistanceWalked(this.getDistanceWalked() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void addTimePlayed(int var1) {
      this.setTimePlayed(this.getTimePlayed() + var1);
      if (var1 > 0) {
         this.addData("upload_data", true);
      }

   }

   public void setArena(Arena var1) {
      this.arena = var1;
   }

   public void setKit(Kit var1) {
      this.kit = var1;
   }

   public void setBox(ArenaBox var1) {
      this.box = var1;
   }

   public void setTrail(String var1) {
      this.trail = var1;
   }

   public void setSpectating(boolean enable, SpectatorReason reason) {
      boolean isManual = reason != SpectatorReason.DEATH && reason != SpectatorReason.LEAVE;
      this.spectating = enable && !isManual;

      if (enable && reason == SpectatorReason.DEATH && (this.getPlayer() == null || !this.getPlayer().isDead())) {
         reason = SpectatorReason.JOIN;
      }

      if ((enable && (reason == SpectatorReason.DEATH || reason == SpectatorReason.JOIN))
              || (!enable && reason == SpectatorReason.LEAVE)) {
         SkyPlayerSpectatorEvent event = new SkyPlayerSpectatorEvent(this, this.getArena(), enable, reason);
         Bukkit.getServer().getPluginManager().callEvent(event);
      }
   }

   public void setArenaSpawn(Location var1) {
      this.arena_spawn = var1;
   }

   public boolean isSpectating() {
      return this.spectating;
   }

   public boolean isInArena() {
      return this.arena != null;
   }

   public boolean hasKit() {
      return this.kit != null;
   }

   public boolean hasBox() {
      return this.box != null;
   }

   public boolean hasTrail() {
      return this.trail != null && !this.trail.isEmpty() && !this.trail.equals("none");
   }

   public void teleport(Location var1) {
      Player var2 = this.getPlayer();
      if (var2 != null) {
         this.getPlayer().teleport(var1);
      }

   }

   public boolean hasPermissions(String var1) {
      return this.getPlayer().hasPermission(var1);
   }

   public void sendMessage(String var1) {
      if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
         var1 = PlaceholderAPI.setPlaceholders(this.getPlayer(), var1);
      }

      if (var1 != null && !var1.isEmpty()) {
         Player var2 = this.getPlayer();
         if (var2 != null) {
            var2.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.color(var1)));
         }
      }

   }

   public void sendMessage(String var1, Object... var2) {
      this.sendMessage(String.format(var1, var2));
   }

   public void clearInventory(boolean var1) {
      Player var2 = this.getPlayer();
      if (var2 != null) {
         if (SkyWars.getPlugin().getConfig().getBoolean("options.saveInventory") && var1) {
            this.armourContents = var2.getInventory().getArmorContents();
            this.inventoryContents = var2.getInventory().getContents();
            this.xplevel = var2.getLevel();
            this.exp = var2.getExp();
         }

         var2.getInventory().setArmorContents((ItemStack[])null);
         var2.getInventory().clear();
         var2.getInventory().setContents(new ItemStack[0]);
         Iterator var3 = var2.getActivePotionEffects().iterator();

         while(var3.hasNext()) {
            PotionEffect var4 = (PotionEffect)var3.next();
            var2.removePotionEffect(var4.getType());
         }

         if (SkyWars.is19orHigher()) {
            var2.setHealth(Utils19.getMaxHealth(var2));
         } else {
            var2.setHealth(var2.getMaxHealth());
         }

         var2.setFoodLevel(20);
         var2.setExp(0.0F);
         var2.setLevel(0);
         var2.setFlying(false);
         var2.setAllowFlight(false);
         this.updateInventory();
      }
   }

   public void resetInventory() {
      this.getPlayer().getInventory().setArmorContents((ItemStack[])null);
      this.getPlayer().getInventory().clear();
      if (SkyWars.getPlugin().getConfig().getBoolean("options.saveInventory")) {
         if (this.inventoryContents != null && this.inventoryContents.length > 0 && this.inventoryContents.length <= this.getPlayer().getInventory().getSize() + 1) {
            this.getPlayer().getInventory().setContents(this.inventoryContents);
         }

         if (this.armourContents != null) {
            this.getPlayer().getInventory().setArmorContents(this.armourContents);
         }

         this.getPlayer().setLevel(this.xplevel);
         this.getPlayer().setExp(this.exp);
         this.updateInventory();
         this.armourContents = null;
         this.inventoryContents = null;
         this.xplevel = 0;
         this.exp = 0.0F;
      }

   }

   public void resetVotes() {
      if (this.hasData("voted_chest")) {
         ChestType[] var1 = ChestTypeManager.getChestTypes();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ChestType var4 = var1[var3];
            if (this.hasData("voted_chest_" + var4.getName())) {
               this.getArena().addData("vote_chest_" + var4.getName(), this.getArena().getInt("vote_chest_" + var4.getName()) - 1);
               this.removeData("voted_chest_" + var4.getName());
            }
         }

         this.removeData("voted_chest");
      }

      if (this.hasData("voted_time")) {
         if (this.hasData("voted_time_day")) {
            this.getArena().addData("vote_time_day", this.getArena().getInt("vote_time_day") - 1);
            this.removeData("voted_time_day");
         }

         if (this.hasData("voted_time_night")) {
            this.getArena().addData("vote_time_night", this.getArena().getInt("vote_time_night") - 1);
            this.removeData("voted_time_night");
         }

         if (this.hasData("voted_time_sunset")) {
            this.getArena().addData("vote_time_sunset", this.getArena().getInt("vote_time_sunset") - 1);
            this.removeData("voted_time_sunset");
         }

         this.removeData("voted_time");
      }

   }

   public void updateInventory() {
      this.getPlayer().updateInventory();
   }

   public double getCoins() {
      return SkyEconomyManager.getCoins(this.getPlayer());
   }

   public double getCoins2() {
      return this.coins;
   }

   public void setCoins(double var1) {
      this.coins = var1;
   }

   public void load() {
      this.loadData();
   }

   public void loadData() {
      DatabaseHandler.getDS().loadPlayerData(this);
      if (CustomEconomy.isCustom()) {
         this.coins = DatabaseHandler.getDS().getCoins(this);
      }

   }

   public void upload(boolean var1) {
      if (var1) {
         this.uploadData();
      } else {
         this.uploadAsyncData();
      }

   }

   public void uploadAsyncData() {
      if (this.hasData("upload_data") && this.getBoolean("upload_data")) {
         (new BukkitRunnable() {
            public void run() {
               DatabaseHandler.getDS().uploadPlayerData(SkyPlayer.this);
            }
         }).runTaskAsynchronously(SkyWars.getPlugin());
      }

   }

   public void uploadData() {
      if (this.hasData("upload_data") && this.getBoolean("upload_data")) {
         DatabaseHandler.getDS().uploadPlayerData(this);
      }

   }

   public String convertKitsToString() {
      String var1 = "";

      String var3;
      for(Iterator var2 = this.owned_kits.iterator(); var2.hasNext(); var1 = var1 + var3 + ",") {
         var3 = (String)var2.next();
      }

      if (var1 != null && var1.endsWith(",")) {
         var1.substring(0, var1.length() - 1);
      }

      return var1;
   }

   public void setBoxSection(String var1, boolean var2) {
      this.boxsection = var1;
   }

   public String getBoxSection() {
      return this.boxsection;
   }

   public int getBoxItem(String var1) {
      return SkyWars.boxes.getInt("boxes." + var1 + ".item");
   }

   public int getBoxData(String var1) {
      return SkyWars.boxes.getInt("boxes." + var1 + ".data");
   }

   public boolean hasKit(Kit var1) {
      return var1 != null && this.owned_kits != null && this.owned_kits.contains(var1.getName());
   }

   public void addKit(Kit var1) {
      if (!this.hasKit(var1)) {
         this.owned_kits.add(var1.getName());
      }

   }

   public void distanceWalkedConvert() {
      double var1 = this.getDouble("local_distance");
      int var3 = (int)Math.round(var1);
      this.addDistanceWalked(var3);
      this.removeData("local_distance");
   }

   public void playedTimeStart() {
      this.local_time_played = new Date();
   }

   public void playedTimeEnd() {
      if (this.local_time_played != null) {
         Date var1 = new Date();
         long var2 = var1.getTime() - this.local_time_played.getTime();
         int var4 = (int)(var2 / 1000L);
         this.addTimePlayed(var4);
         this.local_time_played = null;
      }

   }

   public boolean isAbilityDisabled(AbilityType var1) {
      return this.disabledAbilities.contains(var1);
   }

   public void addAbilityDisabled(AbilityType var1) {
      if (!this.disabledAbilities.contains(var1)) {
         this.disabledAbilities.add(var1);
      }

      if (!this.hasAbility(var1)) {
         this.ownedAbilityByType.put(var1, (AbilityLevel) null);
      }

      this.addData("upload_data", true);
   }

   public void removeAbilityDisabled(AbilityType var1) {
      if (this.disabledAbilities.contains(var1)) {
         this.disabledAbilities.remove(var1);
      }

      this.addData("upload_data", true);
   }

   public boolean hasAbility(AbilityType var1) {
      return this.ownedAbilityByType.get(var1) == null ? false : this.ownedAbilityByType.containsKey(var1);
   }

   public AbilityLevel getAbilityLevel(AbilityType var1) {
      return !this.ownedAbilityByType.containsKey(var1) ? null : (AbilityLevel)this.ownedAbilityByType.get(var1);
   }

   public void setAbility(AbilityType var1, AbilityLevel var2) {
      this.ownedAbilityByType.put(var1, var2);
   }

   public void addAbilityLevel(AbilityType var1) {
      if (this.hasAbility(var1)) {
         if (((AbilityLevel)this.ownedAbilityByType.get(var1)).getLevel() == 8) {
            return;
         }

         this.ownedAbilityByType.put(var1, AbilityManager.getAbilityByType(var1).getLevel(((AbilityLevel)this.ownedAbilityByType.get(var1)).getLevel() + 1));
      } else {
         this.ownedAbilityByType.put(var1, AbilityManager.getAbilityByType(var1).getLevel(1));
      }

   }

   public void serializeAbilities(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         String[] var2 = var1.split(";");
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            String[] var7 = var6.split(",");
            Ability var8 = AbilityManager.getAbility(var7[0]);
            AbilityLevel var9 = null;
            if (var7[1] != "0") {
               var9 = var8.getLevel(Integer.parseInt(var7[1]));
            }

            if (var7[2].equals("1")) {
               this.addAbilityDisabled(var8.getType());
            }

            this.setAbility(var8.getType(), var9);
         }
      }

   }

   public String deserializeAbilities() {
      String var1 = "";
      Iterator var2 = this.ownedAbilityByType.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (var3.getValue() == null) {
            var1 = var1 + AbilityManager.getAbilityByType((AbilityType)var3.getKey()).getName() + ",0," + (this.isAbilityDisabled((AbilityType)var3.getKey()) ? "1" : "0") + ";";
         } else {
            var1 = var1 + AbilityManager.getAbilityByType((AbilityType)var3.getKey()).getName() + "," + ((AbilityLevel)var3.getValue()).getLevel() + "," + (this.isAbilityDisabled((AbilityType)var3.getKey()) ? "1" : "0") + ";";
         }
      }

      if (var1 != null && var1.endsWith(";")) {
         var1.substring(0, var1.length() - 1);
      }

      return var1;
   }
}
