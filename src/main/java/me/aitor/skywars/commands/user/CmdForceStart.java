package me.aitor.skywars.commands.user;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.commands.BaseCommand;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.MSG;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdForceStart implements BaseCommand {
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
         } else {
            if (var2.length < 1) {
               SkyPlayer var4 = SkyWars.getSkyPlayer(var3);
               if (var4.isInArena()) {
                  if (var4.getArena().getPlayers().size() <= 1) {
                     var4.sendMessage("&cNecesitas al menos dos (2) jugadores para forzar el juego");
                     return true;
                  }

                  Arena var5 = var4.getArena();
                  var5.setForceStart();
                  var5.broadcast(SkyWars.getMessage(MSG.GAME_FORCESTART));
                  return true;
               }
            }

            return true;
         }
      }
   }

   public String help(CommandSender var1) {
      String var2 = "&a/sw &eforcestart &a- &bForzar el inicio del juego";
      return var1.hasPermission(this.getPermission()) ? var2 : "";
   }

   public String getPermission() {
      return "skywars.admin.forcestart";
   }

   public boolean console() {
      return false;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return null;
   }
}
