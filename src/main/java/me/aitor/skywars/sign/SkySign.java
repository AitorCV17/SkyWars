package me.aitor.skywars.sign;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.server.ServerManager;
import me.aitor.skywars.utils.Game;
import me.aitor.skywars.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SkySign {
   private final String location;
   private boolean rotation;
   private boolean updating;
   private Game game;

   public SkySign(String var1) {
      this.location = var1;
      this.rotation = false;
   }

   public Sign getSign() {
      Location var1 = LocationUtil.getLocation(this.location);
      if (var1 == null) {
         SkyWars.logError("Intentando usar ubicación nula para ArenaSign: " + this.location);
         return null;
      } else if (var1.getWorld() == null) {
         SkyWars.logError("Intentando usar ubicación de mundo nula para ArenaSign: " + this.location);
         return null;
      } else {
         Block var2 = var1.getWorld().getBlockAt(var1);
         return var2 == null || var2.getType() != Material.WALL_SIGN && var2.getType() != Material.SIGN_POST ? null : (Sign)var2.getState();
      }
   }

   public Location getLocation() {
      return LocationUtil.getLocation(this.location);
   }

   public Game getGame() {
      return this.game;
   }

   public void setGame(String var1) {
      if (SkyWars.isMultiArenaMode()) {
         this.game = ArenaManager.getGame(var1);
      }

      if (SkyWars.isLobbyMode()) {
         this.game = ServerManager.getServer(var1);
      }
   }

   public boolean isRotation() {
      return this.rotation;
   }

   public void setRotation(boolean var1) {
      this.rotation = var1;
   }

   public boolean isUpdating() {
      return this.updating;
   }

   public void setUpdating(boolean var1) {
      this.updating = var1;
   }
}
