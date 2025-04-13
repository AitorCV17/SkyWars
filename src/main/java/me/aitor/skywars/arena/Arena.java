package me.aitor.skywars.arena;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.chest.ChestType;
import me.aitor.skywars.arena.chest.ChestTypeManager;
import me.aitor.skywars.arena.event.ArenaEvent;
import me.aitor.skywars.arena.event.ArenaEventManager;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.config.SkyConfiguration;
import me.aitor.skywars.events.ArenaFinishEvent;
import me.aitor.skywars.events.ArenaJoinEvent;
import me.aitor.skywars.events.ArenaLeaveEvent;
import me.aitor.skywars.events.ArenaTickEvent;
import me.aitor.skywars.events.SkySignUpdateEvent;
import me.aitor.skywars.events.enums.ArenaJoinCause;
import me.aitor.skywars.events.enums.ArenaLeaveCause;
import me.aitor.skywars.events.enums.SkySignUpdateCause;
import me.aitor.skywars.events.enums.SpectatorReason;
import me.aitor.skywars.kit.Kit;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.BungeeUtils;
import me.aitor.skywars.utils.Console;
import me.aitor.skywars.utils.Game;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.LocationUtil;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.RandomFirework;
import me.aitor.skywars.utils.economy.SkyEconomyManager;
import me.aitor.skywars.utils.title.Title;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class Arena extends Game {
   private final List<SkyPlayer> players = new ArrayList();
   private final LinkedHashMap<Location, Boolean> spawnPoints = new LinkedHashMap();
   private final List<ArenaBox> glassBoxes = new ArrayList();
   private final List<String> selectedChest = new ArrayList();
   private final List<String> selectedTime = new ArrayList();
   private final List<Location> dontFill = new ArrayList();
   private final List<BukkitRunnable> tickers = new ArrayList();
   private final List<Integer> startingCounts = new ArrayList();
   private final HashMap<Integer, ArenaTeam> teams = new HashMap();
   private int minPlayers;
   private boolean forceStart;
   private boolean fallDamage;
   private boolean abilities;
   private boolean chestSelected;
   private boolean hardReset;
   private boolean disabled;
   private int startCountdown;
   private int startFullCountdown;
   private int endCountdown;
   private int maxTimeCountdown;
   private ArenaMode mode;
   private File configFile = null;
   private SkyConfiguration config;
   private List<Location> chestFilled = new ArrayList();
   private LinkedList<ArenaEvent> events = new LinkedList();
   private String chest = "";
   private HashMap<SkyPlayer, Integer> killStreak = new HashMap();
   private BukkitRunnable ticks;
   private HashMap<SkyPlayer, ArenaTeam> playerTeam = new HashMap();
   private Location teamLobby;
   private int teamCountdown;
   private int loadWorldTries = 0;

   public Arena(String var1) {
      super(var1, var1, 0, false, ArenaState.WAITING);
      this.createConfig(var1);
      this.forceStart = false;
      this.fallDamage = true;
      this.abilities = this.config.getBoolean("options.abilities");
      this.chestSelected = false;
      this.hardReset = false;
      this.displayName = this.config.getString("name");
      if (this.getWorld() == null) {
         this.loadFirstWorld();
      }

      if (this.config.getString("options.mode").equalsIgnoreCase("TEAM")) {
         this.mode = ArenaMode.SOLO;
      } else {
         this.mode = ArenaMode.SOLO;
      }

      if (this.mode == ArenaMode.SOLO) {
         this.minPlayers = this.config.getInt("min_players");
         this.maxPlayers = this.config.getInt("max_players");
         this.loadSpawnPoints();
         this.loadGlassBoxes();
      } else {
         for(int var2 = 1; var2 <= this.config.getInt("team.teams"); ++var2) {
            this.teams.put(var2, new ArenaTeam(var2, this.config.getInt("team.teams_size"), LocationUtil.getLocation((String)this.config.getStringList("spawnpoints").get(var2 - 1))));
         }

         this.minPlayers = this.config.getInt("min_players");
         this.maxPlayers = this.teams.size() * this.config.getInt("team.teams_size");
         String var7 = this.config.getString("team.waiting_lobby");
         if (var7 != null && !var7.isEmpty()) {
            this.teamLobby = LocationUtil.getLocation(this.config.getString("team.waiting_lobby"));
         } else {
            this.teamLobby = this.getSpawn();
         }

         this.teamCountdown = this.config.getInt("team.start_countdown");
      }

      this.startCountdown = this.config.getInt("countdown.starting");
      this.startFullCountdown = this.config.getInt("countdown.starting_full");
      this.endCountdown = this.config.getInt("countdown.end");
      this.maxTimeCountdown = ConfigManager.main.getInt("maxtime");
      String[] var8 = this.config.getString("countdown.starting_message").split(",");
      int var3 = var8.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         String var5 = var8[var4];
         int var6 = Integer.parseInt(var5);
         this.startingCounts.add(var6);
      }

      this.clearData();
      ChestType[] var9 = ChestTypeManager.getChestTypes();
      var3 = var9.length;

      for(var4 = 0; var4 < var3; ++var4) {
         ChestType var11 = var9[var4];
         this.addData("vote_chest_" + var11.getName(), 0);
      }

      this.addData("vote_time_day", 0);
      this.addData("vote_time_night", 0);
      this.addData("vote_time_sunset", 0);
      ArenaEvent[] var10 = ArenaEventManager.getArenaEvents(this);
      var3 = var10.length;

      for(var4 = 0; var4 < var3; ++var4) {
         ArenaEvent var12 = var10[var4];
         this.events.add(var12);
      }

      ArenaManager.games.put(var1, this);
      this.startTicks();
   }

   public Arena(String var1, boolean var2) {
      super(var1, var1, 0, true, ArenaState.WAITING);
      this.disabled = var2;
      this.createConfig(var1);
      if (this.getWorld() == null) {
         this.loadFirstWorld();
      }

      ArenaManager.games.put(var1, this);
      this.playSignUpdate(SkySignUpdateCause.ALL);
   }

   public void addFilled(Location var1) {
      if (!this.chestFilled.contains(var1)) {
         this.chestFilled.add(var1);
      }

   }

   public void removeFilled(Location var1) {
      if (this.chestFilled.contains(var1)) {
         this.chestFilled.remove(var1);
      }

   }

   public void addPlayer(SkyPlayer var1, ArenaJoinCause var2) {
      if (null == var1) {
         SkyWars.log("Arena.addPlayer - Intentando agregar un jugador NULO");
      } else if (this.disabled) {
         if (var1.getPlayer().hasPermission("skywars.admin")) {
            var1.teleport(this.getWorld().getSpawnLocation());
         } else if (SkyWars.isBungeeMode()) {
            var1.getPlayer().kickPlayer("No tienes permisos para entrar y editar este juego");
         } else {
            var1.sendMessage("&cNo tienes permisos para entrar y editar este juego");
         }

      } else if (this.isLoading()) {
         SkyWars.log("Arena.addPlayer - Intentando unir jugador cuando el juego se está recargando");
         if (SkyWars.isBungeeMode()) {
            var1.getPlayer().kickPlayer(SkyWars.getMessage(MSG.GAME_LOADING));
         } else {
            var1.sendMessage(SkyWars.getMessage(MSG.GAME_LOADING));
         }

      } else {
         if (!var1.getPlayer().hasPermission("skywars.admin.spectate")) {
            if (this.state == ArenaState.INGAME) {
               if (SkyWars.isBungeeMode()) {
                  var1.getPlayer().kickPlayer(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
               } else {
                  var1.sendMessage(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
               }

               return;
            }

            if (this.getAlivePlayers() >= this.maxPlayers) {
               if (SkyWars.isBungeeMode()) {
                  var1.getPlayer().kickPlayer(SkyWars.getMessage(MSG.GAME_FULL_MESSAGE));
               } else {
                  var1.sendMessage(SkyWars.getMessage(MSG.GAME_FULL_MESSAGE));
               }

               return;
            }
         }

         ArenaJoinEvent var3 = new ArenaJoinEvent(var1, this, var2);
         Bukkit.getServer().getPluginManager().callEvent(var3);
         this.playSignUpdate(SkySignUpdateCause.PLAYERS);
      }
   }

   public void addTimer(BukkitRunnable var1, long var2, long var4) {
      this.tickers.add(var1);
      var1.runTaskTimer(SkyWars.getPlugin(), var2, var4);
   }

   public List<BukkitRunnable> getTimers() {
      return this.tickers;
   }

   public BukkitRunnable getTicks() {
      return this.ticks;
   }

   public void broadcast(String var1) {
      Iterator var2 = this.players.iterator();

      while(var2.hasNext()) {
         SkyPlayer var3 = (SkyPlayer)var2.next();
         if (var3.getPlayer() != null && var3.getPlayer().isOnline()) {
            var3.sendMessage(var1);
         }
      }

   }

   private void createConfig(String var1) {
      this.configFile = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.arenas + File.separator + var1 + ".yml");
      this.config = new SkyConfiguration(this.configFile);
      this.config.addDefault("name", var1, "nombre mostrado en el servidor");
      this.config.addDefault("min_players", 2, "jugadores requeridos para iniciar el juego");
      this.config.addDefault("max_players", 6, "cantidad máxima de jugadores que pueden unirse a la arena");
      this.config.addDefault("spawnpoints", new ArrayList(), "punto de aparición donde el jugador aparecerá en el juego (cajas)");
      this.config.addDefault("spectator_spawn", "", "punto de aparición donde aparecerán los espectadores");
      this.config.addDefault("countdown.starting", 90, "tiempo en segundos para iniciar el juego");
      this.config.addDefault("countdown.starting_message", "90,60,30,10,5,4,3,2,1", "lista de segundos en los que se mostrará el mensaje de tiempo");
      this.config.addDefault("countdown.starting_full", 10, "si el juego está lleno o no puede iniciarse por falta de jugadores, la cuenta regresiva será este valor");
      this.config.addDefault("countdown.end", 10, "tiempo en segundos para la duración del final (para efectos de victoria)");
      this.config.addDefault("options.abilities", true, "habilitar o deshabilitar habilidades en esta arena");
      this.config.addDefault("options.mode", ArenaMode.SOLO.toString(), "modo en el que se jugará esta arena", "Modos disponibles: SOLO");
      this.config.addDefault("options.events", true, "habilitar o deshabilitar eventos de arena");
      this.config.addDefault("options.vote.chest", true, "habilitar o deshabilitar votación de cofres");
      this.config.addDefault("options.vote.time", true, "habilitar o deshabilitar votación de tiempo");
      String var2 = ChestTypeManager.getChestType("Normal").getName();
      if (var2 == null || var2.isEmpty()) {
         var2 = ChestTypeManager.getChestTypes()[0].getName();
      }

      this.config.addDefault("chests.default", var2, "tipo de cofre que se seleccionará por defecto en esta arena");
      ArrayList var3 = new ArrayList();
      ChestType[] var4 = ChestTypeManager.getChestTypes();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ChestType var7 = var4[var6];
         var3.add(var7.getName());
      }

      this.config.addDefault("chests.selectable", var3, "lista de tipos de cofres que se pueden seleccionar para votar en esta arena");
      ArrayList var8 = new ArrayList();
      var8.add("REFILL:" + var2 + ",300,Evento de recarga");
      this.config.addDefault("events", var8, "lista de eventos que se ejecutarán en el juego en el orden establecido", "Eventos disponibles:", "    REFILL - Argumento: Tipo de cofre (Ejemplo: Overpowered), también puede ser \"Selected\" para recargar con el tipo de cofre seleccionado", "Formato de uso: EVENT:Argumento,Segundos,Título", "    Argumento: El argumento del evento; si no se especifica, el evento tomará el valor por defecto o un argumento aleatorio", "    Segundos: Tiempo en segundos para ejecutar el evento después del inicio del juego o desde el evento anterior", "    Título: El nombre del evento que se mostrará en el juego", "Ejemplo de formato: REFILL:Normal,300,Evento de recarga");
      this.config.options().copyDefaults(true);
      this.config.getEConfig().setNewLinePerKey(true);
      this.config.save();
   }

   public void clearItems() {
      Iterator var1 = this.getWorld().getEntities().iterator();

      while(var1.hasNext()) {
         Entity var2 = (Entity)var1.next();
         if (var2 instanceof Item) {
            var2.remove();
         }
      }

   }

   public void clearMobs() {
      Iterator var1 = this.getWorld().getEntities().iterator();

      while(true) {
         Entity var2;
         do {
            if (!var1.hasNext()) {
               return;
            }

            var2 = (Entity)var1.next();
         } while(!(var2 instanceof Animals) && !(var2 instanceof Monster));

         var2.remove();
      }
   }

   public void end(boolean var1) {
      this.state = ArenaState.ENDING;
      this.playSignUpdate(SkySignUpdateCause.STATE);
      Iterator var2 = this.getWorld().getPlayers().iterator();

      while(var2.hasNext()) {
         final Player var3 = (Player)var2.next();
         if (var3.isDead()) {
            (new BukkitRunnable() {
               public void run() {
                  var3.spigot().respawn();
               }
            }).runTaskLater(SkyWars.getPlugin(), 10L);
         }
      }

      if (var1) {
         this.endCountdown = 2;
      }

   }

   public void end(final SkyPlayer var1) {
      if (this.getState() != ArenaState.ENDING) {
         this.clearItems();
         this.broadcast(String.format(SkyWars.getMessage(MSG.GAME_FINISH_BROADCAST_WINNER), var1.getName(), this.name));
         SkyEconomyManager.addCoins(var1.getPlayer(), (double)SkyWars.getPlugin().getConfig().getInt("reward.win"), true);
         this.executeWinnerCommands(ConfigManager.main.getBoolean("reward.wincmd.enabled"), var1);
         var1.addWins(1);
         var1.clearInventory(false);
         this.addTimer(new BukkitRunnable() {
            public void run() {
               if (var1 != null && var1.getPlayer() != null && var1.getPlayer().getWorld() != null && var1.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                  Arena.this.launchFirework(var1);
               }
            }
         }, 0L, 10L);
         this.end(false);
         Bukkit.getPluginManager().callEvent(new ArenaFinishEvent(this, var1));
      }
   }

   public void executeWinnerCommands(boolean var1, SkyPlayer var2) {
      if (var1) {
         Iterator var3 = ConfigManager.main.getStringList("reward.wincmd.list").iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String[] var5 = var4.split("/");
            int var6 = Integer.parseInt(var5[0]);
            String var7 = var5[1].replace("%winner%", var2.getName()).replace("%map%", this.getName());
            if (this.getChance() < (double)var6) {
               SkyWars.getPlugin().getServer().dispatchCommand(SkyWars.getPlugin().getServer().getConsoleSender(), var7);
            }
         }
      }
   }

   public List<SkyPlayer> getAlivePlayer() {
      ArrayList<SkyPlayer> alive = new ArrayList<>();
      for (SkyPlayer sp : this.players) {
         if (!(sp.isSpectating() && sp.getPlayer() != null && sp.getPlayer().getGameMode() == GameMode.SPECTATOR)) {
            alive.add(sp);
         }
      }
      return alive;
   }

   public int getAlivePlayers() {
      int alive = 0;

      for (SkyPlayer skyPlayer : this.players) {
         Player bukkitPlayer = skyPlayer.getPlayer();

         if (skyPlayer.isSpectating()) continue;
         if (bukkitPlayer == null) continue;
         if (SkyWars.is18orHigher() && bukkitPlayer.getGameMode() == GameMode.SPECTATOR) {
            if (!bukkitPlayer.hasPermission("skywars.admin.gamemode")) {
               skyPlayer.setSpectating(true, SpectatorReason.DEATH);
               continue;
            }
         }
         alive++;
      }

      return alive;
   }

   public ArenaMode getArenaMode() {
      return this.mode;
   }

   public int getAvailableSlots() {
      return this.getMaxPlayers() - this.getAlivePlayers();
   }

   private double getChance() {
      double var1 = Math.random() * 100.0D;
      return var1;
   }

   public String getChest() {
      return this.getSelectedChest();
   }

   public SkyConfiguration getConfig() {
      return this.config;
   }

   public LinkedList<ArenaEvent> getEvents() {
      return this.events;
   }

   public void setEvents(LinkedList<ArenaEvent> var1) {
      this.events = var1;
   }

   public int getEndCountdown() {
      return this.endCountdown;
   }

   public void setEndCountdown(int var1) {
      this.endCountdown = var1;
   }

   public List<ArenaBox> getGlassBoxes() {
      if (this.mode != ArenaMode.TEAM) {
         return this.glassBoxes;
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var2 = this.teams.values().iterator();

         while(var2.hasNext()) {
            ArenaTeam var3 = (ArenaTeam)var2.next();
            var1.addAll(var3.getCages());
         }

         return var1;
      }
   }

   public int getMaxTimeCountdown() {
      return this.maxTimeCountdown;
   }

   public void setMaxTimeCountdown(int var1) {
      this.maxTimeCountdown = var1;
   }

   public int getMinPlayers() {
      return this.minPlayers;
   }

   public List<SkyPlayer> getPlayers() {
      return this.players;
   }

   public String getSelectedChest() {
      if (this.chestSelected) {
         return this.chest;
      } else {
         this.selectedChest.clear();
         ChestType[] var1 = ChestTypeManager.getChestTypes();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ChestType var4 = var1[var3];
            this.selectedChest.add("vote_chest_" + var4.getName());
         }

         int var5 = -1;
         String var6 = null;
         Iterator var7 = this.selectedChest.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            if (this.getInt(var8) > var5) {
               var5 = this.getInt(var8);
               var6 = var8.replace("vote_chest_", "");
            }
         }

         if (var5 <= 0) {
            var6 = this.config.getString("chests.default");
         }

         this.chest = var6;
         this.chestSelected = true;
         return var6;
      }
   }

   public String getSelectedTime() {
      this.selectedTime.clear();
      this.selectedTime.add("vote_time_day");
      this.selectedTime.add("vote_time_night");
      this.selectedTime.add("vote_time_sunset");
      int var1 = -1;
      String var2 = null;
      Iterator var3 = this.selectedTime.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (this.getInt(var4) > var1) {
            var1 = this.getInt(var4);
            var2 = var4.split("_")[2];
         }
      }

      if (var1 <= 0) {
         return "default";
      } else {
         return var2;
      }
   }

   public final Location getSpawn() {
      Location var1 = null;
      if (this.hasSpectSpawn()) {
         var1 = LocationUtil.getLocation(this.config.getString("spectator_spawn"));
      } else {
         try {
            throw new IllegalAccessException("El spawn de espectadores de (" + this.getName() + ") no ha sido encontrado");
         } catch (IllegalAccessException var3) {
            var3.printStackTrace();
         }
      }

      return var1 != null ? var1 : this.getWorld().getSpawnLocation();
   }

   public Location getSpawnPoint() {
      if (SkyWars.getPlugin().getConfig().getBoolean("options.orderedSpawnPoints")) {
         Iterator var1 = this.spawnPoints.keySet().iterator();

         while(var1.hasNext()) {
            Location var2 = (Location)var1.next();
            if (!(Boolean)this.spawnPoints.get(var2)) {
               return var2;
            }
         }
      } else {
         ArrayList var4 = new ArrayList(this.spawnPoints.keySet());
         Collections.shuffle(var4);
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Location var3 = (Location)var5.next();
            if (!(Boolean)this.spawnPoints.get(var3)) {
               return var3;
            }
         }
      }

      return null;
   }

   public LinkedHashMap<Location, Boolean> getSpawnPoints() {
      return this.spawnPoints;
   }

   public int getStartCountdown() {
      return this.startCountdown;
   }

   public void setStartCountdown(int var1) {
      this.startCountdown = var1;
   }

   public int getStartFullCountdown() {
      return this.startFullCountdown;
   }

   public void setStartFullCountdown(int var1) {
      this.startFullCountdown = var1;
   }

   public List<Integer> getStartingCounts() {
      return this.startingCounts;
   }

   public long getTime() {
      String var1 = this.getSelectedTime();
      if (var1.equalsIgnoreCase("day")) {
         return 0L;
      } else if (var1.equalsIgnoreCase("night")) {
         return 18000L;
      } else {
         return var1.equalsIgnoreCase("sunset") ? 12000L : 24000L;
      }
   }

   public final World getWorld() {
      return Bukkit.getWorld(this.name);
   }

   public List<Location> getChestFilled() {
      return this.chestFilled;
   }

   public void setChestFilled(List<Location> var1) {
      this.chestFilled = var1;
   }

   public int getKillStreak(SkyPlayer var1) {
      return this.killStreak.containsKey(var1) ? (Integer)this.killStreak.get(var1) : 0;
   }

   public void addKillStreak(SkyPlayer var1) {
      if (this.killStreak.containsKey(var1)) {
         this.killStreak.put(var1, (Integer)this.killStreak.get(var1) + 1);
      } else {
         this.killStreak.put(var1, 1);
      }
   }

   public void setKillStreak(HashMap<SkyPlayer, Integer> var1) {
      this.killStreak = var1;
   }

   public void goToSpawn(SkyPlayer var1) {
      var1.teleport(this.getSpawn());
   }

   public boolean hasSpectSpawn() {
      return this.config.getString("spectator_spawn") != null || !this.config.getString("spectator_spawn").isEmpty();
   }

   public boolean isHardReset() {
      return this.hardReset;
   }

   public void setHardReset(boolean var1) {
      this.hardReset = var1;
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean var1) {
      this.disabled = var1;
   }

   public HashMap<SkyPlayer, ArenaTeam> getPlayerTeam() {
      return this.playerTeam;
   }

   public void setPlayerTeam(HashMap<SkyPlayer, ArenaTeam> var1) {
      this.playerTeam = var1;
   }

   public Location getTeamLobby() {
      return this.teamLobby;
   }

   public void setTeamLobby(Location var1) {
      this.teamLobby = var1;
   }

   public boolean isAbilitiesEnabled() {
      return this.abilities;
   }

   public boolean isFallDamage() {
      return this.fallDamage;
   }

   public void setFallDamage(boolean var1) {
      this.fallDamage = var1;
   }

   public boolean isFilled(Location var1) {
      return this.chestFilled.contains(var1);
   }

   public boolean isForceStart() {
      return this.forceStart;
   }

   public void setForceStart(boolean var1) {
      this.forceStart = var1;
   }

   public boolean isFull() {
      return this.players.size() >= this.getMaxPlayers();
   }

   public boolean isUsed(Location var1) {
      return (Boolean)this.spawnPoints.get(var1);
   }

   public void launchFirework(SkyPlayer var1) {
      Location var2 = var1.getPlayer().getLocation();
      RandomFirework.launchRandomFirework(var2);
   }

   public final void loadGlassBoxes() {
      this.glassBoxes.clear();
      Iterator var1 = this.spawnPoints.keySet().iterator();

      while(var1.hasNext()) {
         Location var2 = (Location)var1.next();
         ArenaBox var3 = new ArenaBox(var2);
         var3.setBox(SkyWars.boxes.getInt("boxes." + SkyWars.boxes.getString("default") + ".item"), SkyWars.boxes.getInt("boxes." + SkyWars.boxes.getString("default") + ".data"));
         this.glassBoxes.add(var3);
      }
   }

   public final void loadSpawnPoints() {
      this.spawnPoints.clear();
      Iterator var1 = this.config.getList("spawnpoints").iterator();

      while(var1.hasNext()) {
         Object var2 = var1.next();
         this.spawnPoints.put(LocationUtil.getLocation(var2.toString()), false);
      }
   }

   public final World loadFirstWorld() {
      if (this.getWorld() != null) {
         if (!Bukkit.unloadWorld(this.getWorld(), false)) {
            Console.debugWarn(this.name + " is already loaded but SkyWars is trying to unload the world for resetting (something is keeping the world loaded)");
            ++this.loadWorldTries;
            if (this.loadWorldTries >= 10) {
               SkyWars.logError(this.name + " ha ocurrido un error al intentar descargar el mundo, SkyWars lo intentó 10 veces pero otra instancia mantiene el mundo cargado");
               this.loadWorldTries = 0;
               return this.getWorld();
            }
         }
         return this.loadFirstWorld();
      } else {
         WorldCreator var1 = new WorldCreator(this.name);
         var1.generateStructures(false);
         var1.generator(SkyWars.getVoidGenerator());
         World var2 = var1.createWorld();
         var2.setAutoSave(false);
         var2.setGameRuleValue("doMobSpawning", "false");
         var2.setGameRuleValue("doDaylightCycle", "false");
         var2.setGameRuleValue("commandBlockOutput", "false");
         var2.setTime(0L);
         var2.setDifficulty(Difficulty.NORMAL);
         try {
            var2.setKeepSpawnInMemory(false);
         } catch (Exception var4) {
            SkyWars.logError("Ha ocurrido un error al intentar cargar el mundo: " + this.name);
            SkyWars.logError("Mensaje de error: " + var4.getMessage());
         }
         this.loadWorldTries = 0;
         return var2;
      }
   }

   public void reloadWorld() {
      Iterator var1 = this.getWorld().getPlayers().iterator();

      while(var1.hasNext()) {
         Player var2 = (Player)var1.next();
         if (SkyWars.isBungeeMode()) {
            BungeeUtils.teleToServer(var2, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
         } else {
            SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
            if (var3 == null) {
               var2.kickPlayer("¿Tienes lag?\nNecesitamos reiniciar el mundo :)");
            } else {
               SkyWars.goToSpawn(var3);
               var2.setFallDistance(0.0F);
            }
         }
      }
      if (!Bukkit.unloadWorld(this.getWorld(), false)) {
         SkyWars.logError(this.name + " no se pudo descargar correctamente antes del reinicio del mundo (esto puede causar algunos problemas y no es un problema de SkyWars)");
      }
      if (this.hardReset) {
         File var9 = new File(SkyWars.maps);
         File[] var10 = var9.listFiles();
         int var4 = var10.length;
         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var10[var5];
            if (var6.getName().equals(this.getName()) && var6.isDirectory()) {
               try {
                  ArenaManager.delete(new File(var6.getName()));
                  ArenaManager.copyFolder(var6, new File(var6.getName()));
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            }
         }
      }
      this.loadFirstWorld();
      this.loadSpawnPoints();
      this.loadGlassBoxes();
      this.loading = false;
      this.hardReset = false;
   }

   public String getSpectatorSpawnRaw() {
      return this.config.getString("spectator_spawn");
   }

   public void removePlayer(SkyPlayer var1, ArenaLeaveCause var2) {
      if (var2 == ArenaLeaveCause.SPECTATOR_DISABLED_ON_DEATH) {
         System.out.println("[DEBUG] Se intentó remover al jugador por muerte, ignorado para espectador.");
         return;
      }
      ArenaLeaveEvent var3 = new ArenaLeaveEvent(var1, this, var2);
      Bukkit.getServer().getPluginManager().callEvent(var3);
      if (var2 != ArenaLeaveCause.RESTART) {
         this.playSignUpdate(SkySignUpdateCause.PLAYERS);
      }
   }

   public void removeTimer(BukkitRunnable var1) {
      this.tickers.remove(var1);
   }

   public void resetPlayer(SkyPlayer var1) {
      this.setUsed(var1.getArenaSpawn(), false);
      var1.playedTimeEnd();
      var1.distanceWalkedConvert();
      var1.setBox((ArenaBox)null);
      var1.setArenaSpawn((Location)null);
      var1.clearInventory(false);
      var1.resetInventory();
      var1.resetVotes();
      var1.setArena((Arena)null);
      var1.getPlayer().updateInventory();
   }

   public void restart() {
      Iterator var1 = this.tickers.iterator();

      while(var1.hasNext()) {
         BukkitRunnable var2 = (BukkitRunnable)var1.next();
         var2.cancel();
      }
      this.tickers.clear();
      this.loading = true;
      this.state = ArenaState.WAITING;
      this.forceStart = false;
      this.fallDamage = true;
      this.chestSelected = false;
      this.clearData();
      ChestType[] var5 = ChestTypeManager.getChestTypes();
      int var6 = var5.length;
      for(int var3 = 0; var3 < var6; ++var3) {
         ChestType var4 = var5[var3];
         this.addData("vote_chest_" + var4.getName(), 0);
      }
      this.addData("vote_time_day", 0);
      this.addData("vote_time_night", 0);
      this.addData("vote_time_sunset", 0);
      var1 = this.getWorld().getPlayers().iterator();
      while(var1.hasNext()) {
         Player var7 = (Player)var1.next();
         SkyPlayer var8 = SkyWars.getSkyPlayer(var7);
         if (this.getPlayers().contains(var8)) {
            if (!SkyWars.isBungeeMode()) {
               this.removePlayer(var8, ArenaLeaveCause.RESTART);
            }
         } else if (SkyWars.isBungeeMode()) {
            BungeeUtils.teleToServer(var7, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
         } else {
            SkyWars.goToSpawn(var8);
            var8.getPlayer().setFallDistance(0.0F);
         }
      }
      this.players.clear();
      this.glassBoxes.clear();
      this.selectedChest.clear();
      this.selectedTime.clear();
      this.chestFilled.clear();
      this.dontFill.clear();
      this.events.clear();
      this.killStreak.clear();
      this.events.addAll(Arrays.asList(ArenaEventManager.getArenaEvents(this)));
      this.startCountdown = this.config.getInt("countdown.starting");
      this.startFullCountdown = this.config.getInt("countdown.starting_full");
      this.endCountdown = this.config.getInt("countdown.end");
      this.maxTimeCountdown = ConfigManager.main.getInt("maxtime");
      this.reloadWorld();
      this.playSignUpdate(SkySignUpdateCause.ALL);
   }

   public void setForceStart() {
      if (!this.forceStart) {
         this.forceStart = true;
      }
   }

   public void setState(ArenaState var1) {
      this.state = var1;
      this.playSignUpdate(SkySignUpdateCause.STATE);
   }

   public void setUsed(Location var1, boolean var2) {
      this.spawnPoints.put(var1, var2);
   }

   public void start() {
      if (!ConfigManager.main.getBoolean("options.creaturespawn")) {
         this.clearMobs();
      }
      this.state = ArenaState.INGAME;
      if (this.mode == ArenaMode.SOLO) {
         this.startGo();
      } else {
         Iterator var1 = this.players.iterator();
         while(var1.hasNext()) {
            SkyPlayer var2 = (SkyPlayer)var1.next();
            this.setTeam(var2);
            ArenaTeam var3 = (ArenaTeam)this.playerTeam.get(var2);
            Location var4 = var3.getSpawnUsable();
            var2.teleport(var4);
            var2.setArenaSpawn(var4);
            Iterator var5 = var3.getCages().iterator();
            while(var5.hasNext()) {
               ArenaBox var6 = (ArenaBox)var5.next();
               if (var6.getLocation().equals(var4)) {
                  var2.setBox(var6);
               }
            }
            String var11 = var2.getBoxSection();
            if (var2.getBoxSection() != null && !var11.equalsIgnoreCase(SkyWars.boxes.getString("default"))) {
               int var7;
               int var8;
               ArenaBox var9;
               String var12;
               if (var2.getBoxItem(var2.getBoxSection()) != 0) {
                  var12 = var2.getBoxSection();
                  var7 = var2.getBoxItem(var12);
                  var8 = var2.getBoxData(var12);
                  var9 = var2.getBox();
                  SkyWars.log("Arena.start - Box Section=" + var12 + ", Box Item=" + var7 + ", Box Data=" + var8 + ", Box=" + var9);
                  var9.setBox(var7, var8);
               } else {
                  var2.getPlayer().setMetadata("upload_me", new FixedMetadataValue(SkyWars.getPlugin(), true));
                  var12 = SkyWars.boxes.getString("default");
                  var2.setBoxSection(var12, true);
                  var7 = var2.getBoxItem(var12);
                  var8 = var2.getBoxData(var12);
                  var9 = var2.getBox();
                  SkyWars.log("Arena.start - Box Section=" + var12 + ", Box Item=" + var7 + ", Box Data=" + var8 + ", Box=" + var9);
                  var9.setBox(var7, var8);
               }
            }
         }
         BukkitRunnable var10 = new BukkitRunnable() {
            public void run() {
               if (Arena.this.teamCountdown == 0) {
                  Arena.this.startGo();
                  this.cancel();
               }
               Arena.this.teamCountdown--;
            }
         };
         this.addTimer(var10, 0L, 20L);
      }
      this.playSignUpdate(SkySignUpdateCause.STATE);
   }

   public void startGo() {
      this.broadcast(SkyWars.getMessage(MSG.GAME_START_GO_ALERT_CHAT));
      this.broadcast(SkyWars.getMessage(MSG.GAME_START_GO));
      Iterator var1 = this.getGlassBoxes().iterator();
      while(var1.hasNext()) {
         ArenaBox var2 = (ArenaBox)var1.next();
         if (ConfigManager.main.getBoolean("options.removeAllCageOnStart")) {
            var2.removeAll();
         } else {
            var2.removeBase();
         }
      }
      this.fallDamage = false;
      this.broadcast(String.format(SkyWars.getMessage(MSG.SELECTED_CHEST), this.getChest()));
      long var8 = this.getTime();
      if (var8 == 0L) {
         this.broadcast(String.format(SkyWars.getMessage(MSG.SELECTED_TIME), SkyWars.getMessage(MSG.SELECTED_TIME_DAY)));
      }
      if (var8 == 18000L) {
         this.broadcast(String.format(SkyWars.getMessage(MSG.SELECTED_TIME), SkyWars.getMessage(MSG.SELECTED_TIME_NIGHT)));
      }
      if (var8 == 12000L) {
         this.broadcast(String.format(SkyWars.getMessage(MSG.SELECTED_TIME), SkyWars.getMessage(MSG.SELECTED_TIME_SUNSET)));
      }
      if (var8 == 24000L) {
         this.broadcast(SkyWars.getMessage(MSG.SELECTED_TIME_DEFAULT));
      }
      this.getWorld().setTime(var8);
      Iterator var3 = this.players.iterator();
      while(var3.hasNext()) {
         SkyPlayer var4 = (SkyPlayer)var3.next();
         if (SkyWars.is18orHigher()) {
            Title var5 = new Title(SkyWars.getMessage(MSG.GAME_START_GO_ALERT), 10, 40, 20);
            var5.send(var4.getPlayer());
         }
         var4.getPlayer().getInventory().clear();
         var4.getPlayer().closeInventory();
         if (var4.hasKit()) {
            Kit var9 = var4.getKit();
            Iterator var6 = var9.getItems().iterator();
            while(var6.hasNext()) {
               ItemBuilder var7 = (ItemBuilder)var6.next();
               var4.getPlayer().getInventory().addItem(new ItemStack[]{var7.build()});
            }
         }
         var4.resetVotes();
         var4.addPlayed(1);
         var4.playedTimeStart();
      }
   }

   private void playSignUpdate(SkySignUpdateCause var1) {
      Bukkit.getServer().getPluginManager().callEvent(new SkySignUpdateEvent(this.name, var1));
   }

   private void startTicks() {
      this.ticks = new BukkitRunnable() {
         public void run() {
            if (!Arena.this.disabled) {
               Bukkit.getServer().getPluginManager().callEvent(new ArenaTickEvent(Arena.this));
            }
         }
      };
      this.ticks.runTaskTimer(SkyWars.getPlugin(), 0L, 20L);
   }

   private void setTeam(SkyPlayer var1) {
      if (!this.playerTeam.containsKey(var1)) {
         int var2 = this.config.getInt("team.teams_size");
         int var3 = 0;
         Iterator var4 = this.teams.values().iterator();
         while(var4.hasNext()) {
            ArenaTeam var5 = (ArenaTeam)var4.next();
            if (var5.getPlayers().size() < var2) {
               var2 = var5.getPlayers().size();
               var3 = var5.getNumber();
            }
         }
         ArenaTeam var6 = (ArenaTeam)this.teams.get(var3);
         if (var6 != null) {
            if (!var6.getPlayers().contains(var1)) {
               var6.getPlayers().add(var1);
               this.playerTeam.put(var1, var6);
            }
         }
      }
   }

   public List<Location> getDontFill() {
      return this.dontFill;
   }
}
