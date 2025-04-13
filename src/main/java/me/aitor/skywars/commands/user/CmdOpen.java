package me.aitor.skywars.commands.user;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.commands.BaseCommand;
import me.aitor.skywars.menus.MenuListener;
import me.aitor.skywars.player.SkyPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdOpen implements BaseCommand {
   public boolean onCommand(CommandSender var1, String[] var2) {
      if (!(var1 instanceof Player)) {
         var1.sendMessage("Solo para jugadores");
         return true;
      } else {
         Player var3 = (Player)var1;
         if (var2.length != 1) {
            var3.sendMessage("§cUso: /sw open lshop");
            return true;
         } else {
            String var4 = var2[0];
            byte var5 = -1;
            switch(var4.hashCode()) {
               case 103269730:
                  if (var4.equals("lshop")) {
                     var5 = 0;
                  }
            }

            switch(var5) {
               case 0:
                  if (var3.hasPermission("skywars.cmd.open.lshop")) {
                     SkyPlayer var6 = SkyWars.getSkyPlayer(var3);
                     if (var6 == null) {
                        return false;
                     } else if (SkyWars.isMultiArenaMode() && !var6.isInArena() || SkyWars.isLobbyMode()) {
                        var3.openInventory(MenuListener.getPlayerMenu(var6.getPlayer(), "shop").getInventory());
                     }
                  }
               default:
                  return true;
            }
         }
      }
   }

   public String help(CommandSender var1) {
      String var2 = "&a/sw &eopen lshop &a- &bAbrir inventario de la tienda de kits del lobby";
      return var1.hasPermission(this.getPermission()) ? var2 : "";
   }

   public String getPermission() {
      return "skywars.cmd.open";
   }

   public boolean console() {
      return false;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      if (var2.length >= 1) {
         ArrayList var3 = new ArrayList();
         var3.add("lshop");
         return var3;
      } else {
         return null;
      }
   }
}
