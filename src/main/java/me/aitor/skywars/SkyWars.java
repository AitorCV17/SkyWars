package me.aitor.skywars;

import me.aitor.skywars.abilities.AbilityManager;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.arena.chest.ChestTypeManager;
import me.aitor.skywars.box.BoxManager;
import me.aitor.skywars.commands.CmdExecutor;
import me.aitor.skywars.commands.user.CmdOthers;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.database.DatabaseHandler;
import me.aitor.skywars.events.EventsManager;
import me.aitor.skywars.events.enums.ArenaLeaveCause;
import me.aitor.skywars.kit.KitManager;
import me.aitor.skywars.listener.AbilitiesListener;
import me.aitor.skywars.listener.BungeeRecieveListener;
import me.aitor.skywars.listener.DamageListener;
import me.aitor.skywars.listener.InteractListener;
import me.aitor.skywars.listener.LoginListener;
import me.aitor.skywars.listener.PlayerListener;
import me.aitor.skywars.listener.StatsListener;
import me.aitor.skywars.listener.TrailListener;
import me.aitor.skywars.listener.WorldListener;
import me.aitor.skywars.listener.WorldTabListener;
import me.aitor.skywars.listener.skywars.ArenaListener;
import me.aitor.skywars.listener.skywars.DeathListener;
import me.aitor.skywars.listener.skywars.SpectateListener;
import me.aitor.skywars.menus.MenuListener;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.server.ServerManager;
import me.aitor.skywars.server.SkyServer;
import me.aitor.skywars.sign.SignManager;
import me.aitor.skywars.utils.CustomConfig;
import me.aitor.skywars.utils.FileResClassLoader;
import me.aitor.skywars.utils.Glow;
import me.aitor.skywars.utils.LocationUtil;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.Metrics;
import me.aitor.skywars.utils.RandomFirework;
import me.aitor.skywars.utils.UTF8Control;
import me.aitor.skywars.utils.Utils;
import me.aitor.skywars.utils.VoidUtil;
import me.aitor.skywars.utils.economy.SkyEconomyManager;
import me.aitor.skywars.utils.leaderheads.LeaderHeadsManager;
import me.aitor.skywars.utils.sky.SkyHologram;
import me.aitor.skywars.utils.sky.SkyScoreboard;
import me.aitor.skywars.utils.variable.VariableManager;
import me.aitor.skywars.utils.variable.VariablesDefault;
import me.aitor.skywars.utils.variable.VariablesPlaceholder;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class SkyWars extends JavaPlugin implements Listener {
   private static final ResourceBundle NULL_BUNDLE = null;
   public static SkyWars plugin;
   public static HashMap<UUID, SkyPlayer> skyPlayersUUID = new HashMap();
   public static HashMap<String, SkyPlayer> skyPlayers = new HashMap();
   public static Location spawn;
   public static List<Location> hologram = new ArrayList();
   public static String arenas = "games";
   public static String kits = "kits";
   public static String chests = "chests";
   public static String maps = "maps";
   public static String vupdate = "382269";
   public static String URL_KEY = "1380755095";
   public static FileConfiguration boxes = null;
   public static boolean holo;
   public static boolean update;
   public static boolean disabling;
   public static boolean login;
   public static boolean firstJoin;
   public static String prefix = "[SkyWars] ";
   public static long seconds = 0L;
   public static VariableManager variableManager;
   private static ResourceBundle messageBundle;
   private static ResourceBundle customBundle;
   private static DatabaseHandler databaseHandler;
   private static String server_version;
   private final int[] protocolvalues = new int[]{2696, 93163, 84421, 119900, 258017, 119467, 317546, 119900, 263085};

   public static void reloadMessages() {
      CustomConfig var0 = new CustomConfig(getPlugin());
      CustomConfig var1 = new CustomConfig("messages_en");
      CustomConfig var2 = new CustomConfig("messages_es");
      CustomConfig var3 = new CustomConfig("messages_nl");
      var0.saveDefaultConfig(var1);
      var0.saveDefaultConfig(var2);
      var0.saveDefaultConfig(var3);

      String locale = ConfigManager.main.getString("locale", "en");

      try {
         messageBundle = ResourceBundle.getBundle("messages_" + locale, new UTF8Control());
      } catch (MissingResourceException e) {
         messageBundle = NULL_BUNDLE;
      }

      try {
         customBundle = ResourceBundle.getBundle("messages_" + locale, Locale.ROOT, new FileResClassLoader(SkyWars.class.getClassLoader()), new UTF8Control());
      } catch (MissingResourceException e) {
         customBundle = NULL_BUNDLE;
      }
   }

   public static void reloadAbilities() {
      AbilityManager.initAbilities();
   }

   public static void reloadConfigMain() {
      ConfigManager.mainConfig();
      if (!ConfigManager.main.getString("spawn").isEmpty()) {
         String var0 = ConfigManager.main.getString("spawn");
         spawn = LocationUtil.getLocation(var0);
      } else {
         spawn = ((World)Bukkit.getWorlds().get(0)).getSpawnLocation();
      }

   }

   public static void reloadConfigScoreboard() {
      ConfigManager.scoreboardConfig();
      SkyHologram.reloadHolograms();
   }

   public static void reloadConfigAbilities() {
      ConfigManager.abilitiesConfig();
   }

   public static void reloadConfigShop() {
      ConfigManager.shopConfig();
   }

   public static SkyWars getPlugin() {
      return plugin;
   }

   private static String translate(String var0) {
      try {
         try {
            return customBundle.getString(var0);
         } catch (MissingResourceException var2) {
            return messageBundle.getString(var0);
         }
      } catch (MissingResourceException var3) {
         return customBundle.getString(var0);
      }
   }

   public static String getMessage(MSG var0) {
      if (messageBundle.containsKey(var0.toString())) {
         String var1 = translate(var0.toString());
         return var1.equalsIgnoreCase("null") ? "" : ChatColor.translateAlternateColorCodes('&', var1);
      } else {
         return var0.toString();
      }
   }

   public static String getMapSet() {
      return ConfigManager.main.getString("mode.bungeemapset");
   }

   @Nullable
   public static SkyPlayer getSkyPlayer(Player var0) {
      if (var0 == null) {
         logError("Intentando obtener un jugador nulo");
         return null;
      }

      SkyPlayer player = skyPlayersUUID.get(var0.getUniqueId());
      if (player == null) {
         player = skyPlayers.get(var0.getName());
      }

      return player;
   }

   public static Location getSpawn() {
      return spawn;
   }

   public static List<Location> getHoloLocations() {
      return hologram;
   }

   public static void log(String var0) {
      if (isDebug()) {
         System.out.println("[SkyWars] " + var0);
      }

   }

   public static void logError(String var0) {
      System.out.println("[SkyWars] ERROR: " + var0);
   }

   public static boolean isLobbyMode() {
      String var0 = ConfigManager.main.getString("mode.plugin");
      return var0.equalsIgnoreCase("Lobby") || var0.equalsIgnoreCase("SkyWarsLobby") || var0.startsWith("L");
   }

   public static boolean isBungeeMode() {
      String var0 = ConfigManager.main.getString("mode.plugin");
      return var0.equalsIgnoreCase("Bungee") || var0.equalsIgnoreCase("BungeeMode") || var0.startsWith("B");
   }

   public static boolean isMultiArenaMode() {
      String var0 = ConfigManager.main.getString("mode.plugin");
      return var0.equalsIgnoreCase("Multi") || var0.equalsIgnoreCase("MultiArena") || var0.startsWith("M");
   }

   public static boolean isAutoStart() {
      return ConfigManager.main.getBoolean("mode.bungee-autostart");
   }

   public static boolean isRandomMap() {
      return ConfigManager.main.getBoolean("mode.bungeerandom");
   }

   public static boolean is18orHigher() {
      return server_version.contains("v1_8") || is19orHigher();
   }

   public static boolean is19orHigher() {
      return server_version.contains("v1_9") || server_version.contains("v1_10") || server_version.contains("v1_11") || server_version.contains("v1_12");
   }

   public static boolean isDebug() {
      return ConfigManager.main.getBoolean("debug");
   }

   public static void goToSpawn(SkyPlayer var0) {
      try {
         var0.teleport(getSpawn());
      } catch (Exception var2) {
         logError("El Spawn del Lobby no existe, por favor agrega un Spawn de Lobby con: /sw lobbyspawn");
      }

      if (holo && !getHoloLocations().isEmpty()) {
         SkyHologram.createHologram(var0);
      }

   }

   public static void console(String var0) {
      ConsoleCommandSender var1 = Bukkit.getServer().getConsoleSender();
      var1.sendMessage(ChatColor.translateAlternateColorCodes('&', var0));
   }

   public static ChunkGenerator getVoidGenerator() {
      return new VoidUtil();
   }

   public static String getRandomLobby() {
      List var0 = ConfigManager.main.getStringList("lobbies_servers");
      SecureRandom var1 = new SecureRandom();
      int var2 = var1.nextInt(var0.size());
      return (String)var0.get(var2);
   }

   public static boolean getMysql() {
      return ConfigManager.main.getString("data.type").equalsIgnoreCase("MySQL");
   }

   public static boolean isServerEnabled() {
      return isBungeeMode();
   }

   public static boolean getUpdate() {
      return update;
   }

   public static String checkUpdate(String var0) {
      if (ConfigManager.main.getBoolean("check_updates")) {
         try {
            HttpsURLConnection var1 = (HttpsURLConnection)(new URL("https://api.spigotmc.org/legacy/update.php?resource=" + var0)).openConnection();
            short var2 = 1250;
            var1.setConnectTimeout(var2);
            var1.setReadTimeout(var2);
            String var3 = getPlugin().getDescription().getVersion();
            String[] var4 = var3.split("\\.");
            String var5 = (new BufferedReader(new InputStreamReader(var1.getInputStream()))).readLine();
            String[] var6 = var5.split("\\.");
            if (!var5.equals(var3)) {
               if (!var6[0].equals(var4[0])) {
                  return "§8[§7SkyWars§8] §aHay una actualización §4MAYOR IMPORTANTE §a(§e" + var5 + "§a) Descárgala aquí: §ehttps://spigotmc.org/resources/6525/";
               }

               if (!var6[1].equals(var4[1])) {
                  return "§8[§7SkyWars§8] §aHay una §cACTUALIZACIÓN IMPORTANTE §a(§e" + var5 + "§a) Descárgala aquí: §ehttps://spigotmc.org/resources/6525/";
               }

               if (var6.length > 2 && !var6[2].equals(var4[2])) {
                  return "§8[§7SkyWars§8] §aHay una §6ACTUALIZACIÓN MENOR §a(§e" + var5 + "§a) Descárgala aquí: §ehttps://spigotmc.org/resources/6525/";
               }

               return null;
            }

            var1.disconnect();
         } catch (Exception var7) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "¡Error al verificar actualizaciones en SpigotMC.org! - " + var7.getLocalizedMessage());
            return null;
         }
      }

      return null;
   }

   public void onEnable() {
      plugin = this;
      login = false;
      disabling = false;
      console(prefix + "&aCargando todos los archivos de configuración");
      ConfigManager.mainConfig();
      ConfigManager.scoreboardConfig();
      ConfigManager.abilitiesConfig();
      ConfigManager.shopConfig();
      ConfigManager.signsConfig();
      variableManager = new VariableManager();
      variableManager.registerVariableReplacer(new VariablesDefault());
      File var1 = new File(getPlugin().getDataFolder(), "boxes.yml");
      if (!var1.exists()) {
         this.saveResource("boxes.yml", false);
      }

      boxes = YamlConfiguration.loadConfiguration(var1);
      File var2 = new File(this.getDataFolder(), arenas);
      File var3 = new File(this.getDataFolder(), kits);
      File var4 = new File(this.getDataFolder(), chests);
      File var5 = new File(maps);
      if (!var2.exists()) {
         var2.mkdirs();
      }

      if (!var5.exists()) {
         var5.mkdirs();
      }

      if (!var3.exists()) {
         var3.mkdirs();
         this.saveResource("kits/Archer.yml", false);
         this.saveResource("kits/Blacksmith.yml", false);
         this.saveResource("kits/Bomber.yml", false);
         this.saveResource("kits/Builder.yml", false);
         this.saveResource("kits/Chicken.yml", false);
         this.saveResource("kits/Digger.yml", false);
         this.saveResource("kits/Enchanter.yml", false);
         this.saveResource("kits/Enderman.yml", false);
         this.saveResource("kits/Farmer.yml", false);
         this.saveResource("kits/Fisherman.yml", false);
         this.saveResource("kits/Healer.yml", false);
         this.saveResource("kits/Iron_golem.yml", false);
         this.saveResource("kits/Joker.yml", false);
         this.saveResource("kits/Lumberjack.yml", false);
         this.saveResource("kits/Noobly.yml", false);
         this.saveResource("kits/Pyromaniac.yml", false);
         this.saveResource("kits/Redstone_master.yml", false);
         this.saveResource("kits/Scout.yml", false);
         this.saveResource("kits/Spiderman.yml", false);
         this.saveResource("kits/Swordsman.yml", false);
      }

      if (!var4.exists()) {
         var4.mkdir();
         this.saveResource("chests/Basic.yml", false);
         this.saveResource("chests/Normal.yml", false);
         this.saveResource("chests/Overpowered.yml", false);
      }

      try {
         if (this.loadUpdate()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      console(prefix + "&aEconomía: &e" + ConfigManager.main.getString("economy.mode"));
      this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
      if (!isLobbyMode()) {
         if (!ConfigManager.main.getBoolean("options.disablePerWorldTab")) {
            this.getServer().getPluginManager().registerEvents(new WorldTabListener(), this);
         }

         this.getServer().getPluginManager().registerEvents(new TrailListener(), this);
      }

      PluginManager var6 = this.getServer().getPluginManager();
      var6.registerEvents(new SignManager(), this);
      var6.registerEvents(this, this);
      var6.registerEvents(new PlayerListener(), this);
      var6.registerEvents(new InteractListener(), this);
      var6.registerEvents(new DamageListener(), this);
      var6.registerEvents(new WorldListener(), this);
      var6.registerEvents(new StatsListener(), this);
      var6.registerEvents(new AbilitiesListener(), this);
      var6.registerEvents(new EventsManager(), this);
      var6.registerEvents(new DeathListener(), this);
      var6.registerEvents(new SpectateListener(), this);
      var6.registerEvents(new ArenaListener(), this);
      var6.registerEvents(new MenuListener(), this);
      this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
      reloadMessages();
      console(prefix + "&aCargando archivos de idioma");
      SkyEconomyManager.load();

      try {
         databaseHandler = new DatabaseHandler();
      } catch (Exception ex) {
         ex.printStackTrace();
         log(ex.getMessage());
         Bukkit.getPluginManager().disablePlugin(this);
         return;
      }

      if (isLobbyMode()) {
         Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "SkyWars-Sign-Update", new BungeeRecieveListener());
         ServerManager.initServers();
      }

      if (isBungeeMode()) {
         Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "SkyWars-Sign-Send");
      }

      if (isServerEnabled()) {
         SkyServer.load();
      }

      KitManager.initKits();
      AbilityManager.initAbilities();
      if (!isLobbyMode()) {
         BoxManager.initBoxes();
         ChestTypeManager.loadChests();
         console(prefix + "&aCargando arenas (Partidas)");
         ArenaManager.initGames();
         console(prefix + "&e" + ArenaManager.getGames().size() + " arenas &ahan sido habilitadas");
         RandomFirework.loadFireworks();
         this.getCommand("leave").setExecutor(new CmdOthers(this));
         this.getCommand("salir").setExecutor(new CmdOthers(this));
      }

      if (!ConfigManager.main.getString("spawn").isEmpty()) {
         String var7 = ConfigManager.main.getString("spawn");
         spawn = LocationUtil.getLocation(var7);
      } else {
         spawn = ((World)Bukkit.getWorlds().get(0)).getSpawnLocation();
      }

      holo = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
      if (holo) {
         Iterator var11 = ConfigManager.score.getStringList("hologram.locations").iterator();

         while(var11.hasNext()) {
            String var8 = (String)var11.next();
            hologram.add(LocationUtil.getLocation(var8));
         }

         console(prefix + "&aHook de HolographicDisplays habilitado (&e" + ConfigManager.score.getStringList("hologram.locations").size() + " &aHolograma(s))");
      }

      this.getCommand("sw").setExecutor(new CmdExecutor());
      this.getCommand("sw").setTabCompleter(new CmdExecutor());
      Bukkit.setSpawnRadius(0);
      Metrics var12 = new Metrics(this);
      if (isBungeeMode()) {
         var12.addCustomChart(new Metrics.SimplePie("mode", () -> {
            return "Bungee";
         }));
      }

      if (isMultiArenaMode()) {
         var12.addCustomChart(new Metrics.SimplePie("mode", () -> {
            return "MultiArena";
         }));
      }

      if (isLobbyMode()) {
         var12.addCustomChart(new Metrics.SimplePie("type", () -> {
            return "Lobby";
         }));
      } else {
         var12.addCustomChart(new Metrics.SimplePie("type", () -> {
            return "Partida";
         }));
      }

      var12.addCustomChart(new Metrics.SimplePie("language", () -> {
         return ConfigManager.main.getString("locale", "en");
      }));
      console(prefix + "&aMétricas (bStats) habilitadas");
      server_version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
      log("Versión del servidor: " + server_version);
      update = checkUpdate("6525") != null;
      login = true;
      seconds = (new Date()).getTime();
      firstJoin = false;
      (new BukkitRunnable() {
         public void run() {
            Iterator var1 = Bukkit.getOnlinePlayers().iterator();

            while(var1.hasNext()) {
               Player var2 = (Player)var1.next();
               SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
               if (var3 != null) {
                  SkyScoreboard.contentBoard(var3);
               }
            }

         }
      }).runTaskTimerAsynchronously(this, 0L, 15L);
      if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
         console(prefix + "&aHabilitando PlaceholderAPI...");
         (new VariablesPlaceholder(this)).register();
         console(prefix + "&aPlaceholderAPI habilitado, registrado exitosamente");
      }

      this.registerGlow();
      if (isMultiArenaMode() || isLobbyMode()) {
         SignManager.loadSigns();
      }

   }

   public void onDisable() {
      disabling = true;

      SkyPlayer var2;
      for(Iterator var1 = skyPlayersUUID.values().iterator(); var1.hasNext(); var2.upload(true)) {
         var2 = (SkyPlayer)var1.next();
         if (var2.isInArena()) {
            Arena var3 = var2.getArena();
            var3.removePlayer(var2, ArenaLeaveCause.RESTART);
         }
      }

      if (databaseHandler != null) {
         console(prefix + "&cDeshabilitando todos los datos");
         DatabaseHandler.getDS().close();
      }

   }

   @EventHandler
   public void onPluginEnable(PluginEnableEvent var1) {
      Plugin var2 = var1.getPlugin();
      if (var2.getName().equals("LeaderHeads")) {
         LeaderHeadsManager.load();
      }

   }

   public void reloadSigns() {
      SignManager.loadSigns();
   }

   public void reloadKits() {
      KitManager.initKits();
   }

   public void reloadBoxes() {
      BoxManager.initBoxes();
   }

   public void reloadChests() {
      ChestTypeManager.loadChests();
   }

   public void reloadArenas() {
      Iterator var1 = ArenaManager.getGames().iterator();

      while(var1.hasNext()) {
         Arena var2 = (Arena)var1.next();
         var2.restart();
      }

      ArenaManager.initGames();
   }

   @EventHandler
   public void ping(ServerListPingEvent var1) {
      if (isBungeeMode()) {
         Iterator var2 = ArenaManager.getGames().iterator();

         while(var2.hasNext()) {
            Arena var3 = (Arena)var2.next();
            var1.setMaxPlayers(var3.getMaxPlayers());
            if (var3.isLoading()) {
               var1.setMotd(getMessage(MSG.MOTD_LOADING).replace("%map%", var3.getName()));
               return;
            }

            if (var3.getState() == ArenaState.WAITING) {
               var1.setMotd(getMessage(MSG.MOTD_WAITING).replace("%map%", var3.getName()));
            }

            if (var3.getState() == ArenaState.STARTING) {
               var1.setMotd(getMessage(MSG.MOTD_STARTING).replace("%map%", var3.getName()));
            }

            if (var3.getState() == ArenaState.INGAME) {
               var1.setMotd(getMessage(MSG.MOTD_INGAME).replace("%map%", var3.getName()));
            }

            if (var3.getState() == ArenaState.ENDING) {
               var1.setMotd(getMessage(MSG.MOTD_ENDING).replace("%map%", var3.getName()));
            }
         }
      }

   }

   private boolean loadUpdate() {
      String var1 = vupdate;
      if (!Utils.isNumeric(var1)) {
         return true;
      } else {
         int[] var2 = this.protocolvalues;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = var2[var4];
            if (Integer.parseInt(var1) == var5) {
               return true;
            }
         }

         return false;
      }
   }

   private void registerGlow() {
      try {
         Field var1 = Enchantment.class.getDeclaredField("acceptingNew");
         var1.setAccessible(true);
         var1.set((Object)null, true);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      try {
         Glow var5 = new Glow(120);
         Enchantment.registerEnchantment(var5);
      } catch (IllegalArgumentException var2) {
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
