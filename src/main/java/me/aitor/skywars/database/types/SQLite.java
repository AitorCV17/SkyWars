package me.aitor.skywars.database.types;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.database.DataSource;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.economy.skyeconomy.CustomEconomy;

import java.sql.*;
import java.util.List;
import java.util.Map.Entry;

public class SQLite extends DataSource {
   private static Connection con;

   public SQLite() {
      try {
         this.connect();
         this.setup();
      } catch (SQLException | ClassNotFoundException ex) {
         SkyWars.logError("Error durante la inicialización de SQLite: " + ex.getMessage());
         ex.printStackTrace();
      }
   }

   private synchronized void connect() throws SQLException, ClassNotFoundException {
      Class.forName("org.sqlite.JDBC");
      SkyWars.log("Controlador de SQLite cargado");
      con = DriverManager.getConnection("jdbc:sqlite:plugins/SkyWars/Database.db");
      SkyWars.log("SQLite.connect: está cerrado = " + con.isClosed());
   }

   private synchronized void setup() {
      Statement stmt = null;
      try {
         stmt = con.createStatement();
         stmt.executeUpdate(String.format(
                 "CREATE TABLE IF NOT EXISTS '%s' ('id' INTEGER PRIMARY KEY, 'uuid' TEXT(40), 'username' TEXT(32), 'kits' TEXT, 'abilities' TEXT, 'last_colour' TEXT(60) DEFAULT NULL, " +
                         "'wins' INT(12) DEFAULT '0', 'kills' INT(12) DEFAULT '0', 'deaths' INT(12) DEFAULT '0', 'played' INT(12) DEFAULT '0', 'arrow_shot' INT(12) DEFAULT '0', " +
                         "'arrow_hit' INT(12) DEFAULT '0', 'blocks_broken' INT(12) DEFAULT '0', 'blocks_placed' INT(12) DEFAULT '0', 'time_played' INT(12) DEFAULT '0', 'distance_walked' INT(12) DEFAULT '0');" +
                         "CREATE INDEX IF NOT EXISTS swdata_username ON %s(username);" +
                         "CREATE INDEX IF NOT EXISTS swdata_uuid ON %s(uuid);",
                 this.TABLE_DATA, this.TABLE_DATA, this.TABLE_DATA));

         this.addColumn(this.TABLE_DATA, "uuid", "VARCHAR(255) NOT NULL UNIQUE");
         this.addColumn(this.TABLE_DATA, "username", "VARCHAR(255) DEFAULT NULL");
         this.addColumn(this.TABLE_DATA, "kits", "TEXT");
         this.addColumn(this.TABLE_DATA, "abilities", "TEXT");
         this.addColumn(this.TABLE_DATA, "last_colour", "VARCHAR(40)");
         this.addColumn(this.TABLE_DATA, "wins", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "kills", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "deaths", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "played", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "arrow_shot", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "arrow_hit", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "blocks_broken", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "blocks_placed", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "time_played", "INT(12) DEFAULT 0");
         this.addColumn(this.TABLE_DATA, "distance_walked", "INT(12) DEFAULT 0");

         if (CustomEconomy.isCustom()) {
            stmt.executeUpdate(String.format(
                    "CREATE TABLE IF NOT EXISTS '%s' ('id' INTEGER PRIMARY KEY, 'uuid' TEXT(40), 'username' TEXT(32), 'money' DOUBLE(20,2) DEFAULT '0');" +
                            "CREATE INDEX IF NOT EXISTS sweconomy_username ON %s(username);" +
                            "CREATE INDEX IF NOT EXISTS sweconomy_uuid ON %s(uuid);",
                    this.TABLE_ECONOMY, this.TABLE_ECONOMY, this.TABLE_ECONOMY));
         }
      } catch (SQLException ex) {
         ex.printStackTrace();
      } finally {
         this.close(stmt);
      }

      SkyWars.log("Configuración de SQLite finalizada");
   }

   private void addColumn(String table, String column, String type) {
      ResultSet rs = null;
      Statement stmt = null;

      try {
         stmt = con.createStatement();
         DatabaseMetaData meta = con.getMetaData();
         rs = meta.getColumns(null, null, table, column);
         if (!rs.next()) {
            stmt.executeUpdate(String.format("ALTER TABLE %s ADD COLUMN %s %s;", table, column, type));
         }
      } catch (SQLException ex) {
         ex.printStackTrace();
      } finally {
         this.close(rs);
         this.close(stmt);
      }
   }

   public synchronized Connection getConnection() {
      return con;
   }

   public void close() {
      try {
         if (con != null && !con.isClosed()) {
            con.close();
         }
      } catch (SQLException ex) {
         ex.printStackTrace();
      }
   }

   public synchronized void loadPlayerData(SkyPlayer player) {
      this.loadPlayerData(con, player);
   }

   public void uploadPlayerData(SkyPlayer player) {
      this.uploadPlayerData(con, player);
   }

   public double getCoins(SkyPlayer player) {
      return this.getCoins(con, player);
   }

   public void modifyCoins(SkyPlayer player, double amount) {
      this.modifyCoins(con, player, amount);
   }

   public void loadServer() {
      System.err.println("[SkyWars] Intentando cargar servidores desde SQLite, por favor cambia el tipo de datos a MySQL");
   }

   public void getServers() {
      System.err.println("[SkyWars] Intentando obtener servidores desde SQLite, por favor cambia el tipo de datos a MySQL");
   }

   public void setServerData(Arena arena) {
      System.err.println("[SkyWars] Intentando establecer datos del servidor desde SQLite, por favor cambia el tipo de datos a MySQL");
   }

   public List<Entry<String, Integer>> getTopStats(String stat, int limit) {
      return this.getTopStats(con, stat, limit);
   }
}
