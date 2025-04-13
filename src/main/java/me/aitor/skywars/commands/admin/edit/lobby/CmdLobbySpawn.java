package me.aitor.skywars.commands.admin.edit.lobby;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.commands.BaseCommand;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.utils.LocationUtil;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdLobbySpawn implements BaseCommand {
   public boolean onCommand(CommandSender var1, String[] var2) {
      Player var3 = null;
      if (!(var1 instanceof Player)) {
         var1.sendMessage("¡No eres un jugador!");
         return true;
      } else {
         var3 = (Player)var1;
         if (!var3.hasPermission(this.getPermission())) {
            var3.sendMessage("§c¡No tienes permiso!");
            return true;
         } else if (var2.length == 0) {
            SkyWars.getPlugin().getConfig().set("spawn", LocationUtil.getString(var3.getLocation(), true));
            ConfigManager.main.set("spawn", LocationUtil.getString(var3.getLocation(), true));
            ConfigManager.main.save();
            SkyWars.spawn = var3.getLocation();
            var3.sendMessage("§aSpawn del lobby establecido");
            return true;
         } else {
            return true;
         }
      }
   }

   public String help(CommandSender var1) {
      String var2 = "&a/sw lobbyspawn &a- &bEstablecer spawn del lobby";
      return var1.hasPermission(this.getPermission()) ? var2 : "";
   }

   public String getPermission() {
      return "skywars.admin";
   }

   public boolean console() {
      return false;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return null;
   }
}
