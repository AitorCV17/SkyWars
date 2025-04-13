package me.aitor.skywars.commands.user;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.events.enums.ArenaLeaveCause;
import me.aitor.skywars.listener.DamageListener;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.BungeeUtils;
import me.aitor.skywars.utils.MSG;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdOthers implements CommandExecutor {
   public CmdOthers(SkyWars var1) {
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if (!(var1 instanceof Player)) {
         return true;
      } else {
         Player var5 = (Player)var1;
         if (!var3.equalsIgnoreCase("leave") && !var3.equalsIgnoreCase("salir")) {
            return false;
         } else {
            if (var4.length == 0) {
               SkyPlayer var6 = SkyWars.getSkyPlayer(var5);
               if (var6 == null) {
                  return false;
               }

               if (var6.isInArena()) {
                  Arena var7 = var6.getArena();
                  if (var6.isInArena()) {
                     if (DamageListener.lastDamage.containsKey(var5.getUniqueId())) {
                        Player var8 = Bukkit.getPlayer((UUID)DamageListener.lastDamage.get(var5.getUniqueId()));
                        var5.damage(1000.0D, var8);
                        var6.addDeaths(1);
                     }

                     var7.removePlayer(var6, ArenaLeaveCause.COMMAND);
                     SkyWars.log("CmdOther.onCommand - " + var6.getName() + " removed using command");
                  }
               }

               if (SkyWars.isBungeeMode()) {
                  BungeeUtils.teleToServer(var5, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
               }
            }

            return true;
         }
      }
   }
}
