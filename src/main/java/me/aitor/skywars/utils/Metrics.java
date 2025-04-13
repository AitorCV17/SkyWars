package me.aitor.skywars.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Metrics {
   public static final int B_STATS_VERSION = 1;
   private static final String URL = "https://bStats.org/submitData/bukkit";
   private static boolean logFailedRequests;
   private static String serverUUID;
   private final JavaPlugin plugin;
   private final List<Metrics.CustomChart> charts = new ArrayList<>();

   public Metrics(JavaPlugin plugin) {
      if (plugin == null) {
         throw new IllegalArgumentException("¡El plugin no puede ser nulo!");
      }
      this.plugin = plugin;
      File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
      File configFile = new File(bStatsFolder, "config.yml");
      YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
      if (!config.isSet("serverUuid")) {
         config.addDefault("enabled", true);
         config.addDefault("serverUuid", UUID.randomUUID().toString());
         config.addDefault("logFailedRequests", false);
         config.options().header("bStats recoge algunos datos para los autores de plugins, como cuántos servidores utilizan sus plugins.\n" +
                 "Para honrar su trabajo, no deberías desactivarlo.\n" +
                 "¡Esto tiene casi ningún efecto en el rendimiento del servidor!\n" +
                 "Visita https://bStats.org/ para saber más :)").copyDefaults(true);

         try {
            config.save(configFile);
         } catch (IOException ignored) {
         }
      }
      serverUUID = config.getString("serverUuid");
      logFailedRequests = config.getBoolean("logFailedRequests", false);
      if (config.getBoolean("enabled", true)) {
         boolean found = false;
         Iterator<Class<?>> it = Bukkit.getServicesManager().getKnownServices().iterator();
         while (it.hasNext()) {
            Class<?> service = it.next();
            try {
               service.getField("B_STATS_VERSION");
               found = true;
               break;
            } catch (NoSuchFieldException ignored) {
            }
         }
         Bukkit.getServicesManager().register(Metrics.class, this, plugin, ServicePriority.Normal);
         if (!found) {
            this.startSubmitting();
         }
      }
   }

   public void addCustomChart(Metrics.CustomChart chart) {
      if (chart == null) {
         throw new IllegalArgumentException("¡El gráfico no puede ser nulo!");
      }
      this.charts.add(chart);
   }

   private void startSubmitting() {
      final Timer timer = new Timer(true);
      timer.scheduleAtFixedRate(new TimerTask() {
         public void run() {
            if (!Metrics.this.plugin.isEnabled()) {
               timer.cancel();
            } else {
               Bukkit.getScheduler().runTask(Metrics.this.plugin, new Runnable() {
                  public void run() {
                     Metrics.this.submitData();
                  }
               });
            }
         }
      }, 300000L, 1800000L);
   }

   public JSONObject getPluginData() {
      JSONObject data = new JSONObject();
      String pluginName = this.plugin.getDescription().getName();
      String pluginVersion = this.plugin.getDescription().getVersion();
      data.put("pluginName", pluginName);
      data.put("pluginVersion", pluginVersion);
      JSONArray chartData = new JSONArray();
      Iterator<Metrics.CustomChart> it = this.charts.iterator();
      while (it.hasNext()) {
         Metrics.CustomChart chart = it.next();
         JSONObject chartObj = chart.getRequestJsonObject();
         if (chartObj != null) {
            chartData.add(chartObj);
         }
      }
      data.put("customCharts", chartData);
      return data;
   }

   private JSONObject getServerData() {
      int playerCount;
      try {
         Method method = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");
         if (method.getReturnType().equals(Collection.class)) {
            playerCount = ((Collection<?>) method.invoke(Bukkit.getServer())).size();
         } else {
            playerCount = ((Player[]) method.invoke(Bukkit.getServer())).length;
         }
      } catch (Exception e) {
         playerCount = Bukkit.getOnlinePlayers().size();
      }
      int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
      String bukkitVersion = Bukkit.getVersion();
      bukkitVersion = bukkitVersion.substring(bukkitVersion.indexOf("MC: ") + 4, bukkitVersion.length() - 1);
      String javaVersion = System.getProperty("java.version");
      String osName = System.getProperty("os.name");
      String osArch = System.getProperty("os.arch");
      String osVersion = System.getProperty("os.version");
      int coreCount = Runtime.getRuntime().availableProcessors();

      JSONObject serverData = new JSONObject();
      serverData.put("serverUUID", serverUUID);
      serverData.put("playerAmount", playerCount);
      serverData.put("onlineMode", onlineMode);
      serverData.put("bukkitVersion", bukkitVersion);
      serverData.put("javaVersion", javaVersion);
      serverData.put("osName", osName);
      serverData.put("osArch", osArch);
      serverData.put("osVersion", osVersion);
      serverData.put("coreCount", coreCount);
      return serverData;
   }

   private void submitData() {
      final JSONObject serverData = this.getServerData();
      JSONArray pluginsArray = new JSONArray();

      for (Class<?> service : Bukkit.getServicesManager().getKnownServices()) {
         try {
            service.getField("B_STATS_VERSION");
            for (RegisteredServiceProvider<?> rsp : Bukkit.getServicesManager().getRegistrations(service)) {
               try {
                  Method m = rsp.getService().getMethod("getPluginData");
                  Object result = m.invoke(rsp.getProvider());
                  if (result instanceof JSONObject) {
                     pluginsArray.add(result);
                  }
               } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException ignored) {
               }
            }
         } catch (NoSuchFieldException ignored) {
         }
      }

      serverData.put("plugins", pluginsArray);

      new Thread(new Runnable() {
         public void run() {
            try {
               Metrics.sendData(serverData);
            } catch (Exception e) {
               if (Metrics.logFailedRequests) {
                  Metrics.this.plugin.getLogger().log(Level.WARNING, "No se pudieron enviar las estadísticas del plugin " + Metrics.this.plugin.getName(), e);
               }
            }
         }
      }).start();
   }

   private static void sendData(JSONObject data) throws Exception {
      if (data == null) {
         throw new IllegalArgumentException("¡Los datos no pueden ser nulos!");
      }
      if (Bukkit.isPrimaryThread()) {
         throw new IllegalAccessException("¡Este método no debe ser llamado desde el hilo principal!");
      }
      URL url = new URL("https://bStats.org/submitData/bukkit");
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      byte[] compressed = compress(data.toString());
      connection.setRequestMethod("POST");
      connection.addRequestProperty("Accept", "application/json");
      connection.addRequestProperty("Connection", "close");
      connection.addRequestProperty("Content-Encoding", "gzip");
      connection.addRequestProperty("Content-Length", String.valueOf(compressed.length));
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("User-Agent", "MC-Server/1");
      connection.setDoOutput(true);
      DataOutputStream out = new DataOutputStream(connection.getOutputStream());
      out.write(compressed);
      out.flush();
      out.close();
      connection.getInputStream().close();
   }

   private static byte[] compress(String str) throws IOException, UnsupportedEncodingException {
      if (str == null) {
         return null;
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      GZIPOutputStream gzip = new GZIPOutputStream(baos);
      gzip.write(str.getBytes("UTF-8"));
      gzip.close();
      return baos.toByteArray();
   }

   static {
      if (System.getProperty("bstats.relocatecheck") == null ||
              !System.getProperty("bstats.relocatecheck").equals("false")) {
         String originalPackage = new String(new byte[]{111, 114, 103, 46, 98, 115, 116, 97, 116, 115, 46, 98, 117, 107, 107, 105, 116});
         String yourPackage = new String(new byte[]{121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101});
         if (Metrics.class.getPackage().getName().equals(originalPackage) ||
                 Metrics.class.getPackage().getName().equals(yourPackage)) {
            throw new IllegalStateException("¡La clase Metrics de bStats no ha sido reubicada correctamente!");
         }
      }
   }

   // ----------------- Inner classes for charts -----------------

   public static class AdvancedBarChart extends Metrics.CustomChart {
      private final Callable<Map<String, int[]>> callable;

      public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JSONObject getChartData() {
         JSONObject json = new JSONObject();
         JSONObject values = new JSONObject();
         Map<String, int[]> data;
         try {
            data = callable.call();
         } catch (Exception e) {
            return null;
         }
         if (data != null && !data.isEmpty()) {
            boolean empty = true;
            for (Entry<String, int[]> entry : data.entrySet()) {
               int[] arr = entry.getValue();
               if (arr.length == 0) continue;
               empty = false;
               JSONArray arrJson = new JSONArray();
               for (int i : arr) {
                  arrJson.add(i);
               }
               values.put(entry.getKey(), arrJson);
            }
            if (empty) return null;
            json.put("values", values);
            return json;
         } else {
            return null;
         }
      }
   }

   public static class SimpleBarChart extends Metrics.CustomChart {
      private final Callable<Map<String, Integer>> callable;

      public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JSONObject getChartData() {
         JSONObject json = new JSONObject();
         JSONObject values = new JSONObject();
         Map<String, Integer> data;
         try {
            data = callable.call();
         } catch (Exception e) {
            return null;
         }
         if (data != null && !data.isEmpty()) {
            for (Entry<String, Integer> entry : data.entrySet()) {
               JSONArray arr = new JSONArray();
               arr.add(entry.getValue());
               values.put(entry.getKey(), arr);
            }
            json.put("values", values);
            return json;
         } else {
            return null;
         }
      }
   }

   public static class MultiLineChart extends Metrics.CustomChart {
      private final Callable<Map<String, Integer>> callable;

      public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JSONObject getChartData() {
         JSONObject json = new JSONObject();
         JSONObject values = new JSONObject();
         Map<String, Integer> data;
         try {
            data = callable.call();
         } catch (Exception e) {
            return null;
         }
         if (data != null && !data.isEmpty()) {
            boolean allZero = true;
            for (Entry<String, Integer> entry : data.entrySet()) {
               if (entry.getValue() != 0) {
                  allZero = false;
                  values.put(entry.getKey(), entry.getValue());
               }
            }
            if (allZero) return null;
            json.put("values", values);
            return json;
         } else {
            return null;
         }
      }
   }

   public static class SingleLineChart extends Metrics.CustomChart {
      private final Callable<Integer> callable;

      public SingleLineChart(String chartId, Callable<Integer> callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JSONObject getChartData() {
         JSONObject json = new JSONObject();
         int value;
         try {
            value = callable.call();
         } catch (Exception e) {
            return null;
         }
         if (value == 0) {
            return null;
         } else {
            json.put("value", value);
            return json;
         }
      }
   }

   public static class DrilldownPie extends Metrics.CustomChart {
      private final Callable<Map<String, Map<String, Integer>>> callable;

      public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
         super(chartId);
         this.callable = callable;
      }

      public JSONObject getChartData() {
         JSONObject json = new JSONObject();
         JSONObject values = new JSONObject();
         Map<String, Map<String, Integer>> data;
         try {
            data = callable.call();
         } catch (Exception e) {
            return null;
         }
         if (data != null && !data.isEmpty()) {
            boolean allEmpty = true;
            for (Entry<String, Map<String, Integer>> entry : data.entrySet()) {
               JSONObject subJson = new JSONObject();
               boolean emptySub = true;
               for (Entry<String, Integer> subEntry : entry.getValue().entrySet()) {
                  subJson.put(subEntry.getKey(), subEntry.getValue());
                  emptySub = false;
               }
               if (!emptySub) {
                  allEmpty = false;
                  values.put(entry.getKey(), subJson);
               }
            }
            if (allEmpty) return null;
            json.put("values", values);
            return json;
         } else {
            return null;
         }
      }
   }

   public static class AdvancedPie extends Metrics.CustomChart {
      private final Callable<Map<String, Integer>> callable;

      public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JSONObject getChartData() {
         JSONObject json = new JSONObject();
         JSONObject values = new JSONObject();
         Map<String, Integer> data;
         try {
            data = callable.call();
         } catch (Exception e) {
            return null;
         }
         if (data != null && !data.isEmpty()) {
            boolean allZero = true;
            for (Entry<String, Integer> entry : data.entrySet()) {
               if (entry.getValue() != 0) {
                  allZero = false;
                  values.put(entry.getKey(), entry.getValue());
               }
            }
            if (allZero) return null;
            json.put("values", values);
            return json;
         } else {
            return null;
         }
      }
   }

   public static class SimplePie extends Metrics.CustomChart {
      private final Callable<String> callable;

      public SimplePie(String chartId, Callable<String> callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JSONObject getChartData() {
         JSONObject json = new JSONObject();
         String value;
         try {
            value = callable.call();
         } catch (Exception e) {
            return null;
         }
         if (value != null && !value.isEmpty()) {
            json.put("value", value);
            return json;
         } else {
            return null;
         }
      }
   }

   public abstract static class CustomChart {
      final String chartId;

      CustomChart(String chartId) {
         if (chartId != null && !chartId.isEmpty()) {
            this.chartId = chartId;
         } else {
            throw new IllegalArgumentException("¡El chartId no puede ser nulo o vacío!");
         }
      }

      private JSONObject getRequestJsonObject() {
         JSONObject request = new JSONObject();
         request.put("chartId", this.chartId);
         try {
            JSONObject data = this.getChartData();
            if (data == null) {
               return null;
            } else {
               request.put("data", data);
               return request;
            }
         } catch (Throwable t) {
            if (Metrics.logFailedRequests) {
               Bukkit.getLogger().log(Level.WARNING, "No se pudieron obtener los datos para el gráfico personalizado con id " + this.chartId, t);
            }
            return null;
         }
      }

      protected abstract JSONObject getChartData();
   }
}
