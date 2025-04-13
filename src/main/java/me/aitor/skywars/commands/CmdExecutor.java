package me.aitor.skywars.commands;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.commands.admin.CmdHide;
import me.aitor.skywars.commands.admin.CmdReload;
import me.aitor.skywars.commands.admin.edit.arena.CmdArena;
import me.aitor.skywars.commands.admin.edit.arena.CmdTp;
import me.aitor.skywars.commands.admin.edit.lobby.CmdHologram;
import me.aitor.skywars.commands.admin.edit.lobby.CmdLobbySpawn;
import me.aitor.skywars.commands.user.CmdCoins;
import me.aitor.skywars.commands.user.CmdForceStart;
import me.aitor.skywars.commands.user.CmdJoin;
import me.aitor.skywars.commands.user.CmdOpen;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CmdExecutor implements CommandExecutor, TabCompleter {
   private final HashMap<String, BaseCommand> commands = new HashMap();
   private final List<String> cmds = new ArrayList();

   public CmdExecutor() {
      this.loadCommands();
      this.cmds.clear();
      Iterator var1 = this.commands.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         if (!var2.equals("hide")) {
            this.cmds.add(var2);
         }
      }

   }

   private void loadCommands() {
      this.commands.clear();
      if (!SkyWars.isLobbyMode()) {
         this.commands.put("arena", new CmdArena());
         this.commands.put("tp", new CmdTp());
         this.commands.put("forcestart", new CmdForceStart());
      }

      if (!SkyWars.isBungeeMode()) {
         if (SkyWars.holo) {
            this.commands.put("hologram", new CmdHologram());
         }

         this.commands.put("lobbyspawn", new CmdLobbySpawn());
         this.commands.put("open", new CmdOpen());
         this.commands.put("join", new CmdJoin());
      }

      this.commands.put("coins", new CmdCoins());
      this.commands.put("reload", new CmdReload());
      this.commands.put("hide", new CmdHide());
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var2.getName().equalsIgnoreCase("sw")) {
         if (var4 != null && var4.length >= 1) {
            if (var4[0].equalsIgnoreCase("help")) {
               this.help(var1);
               return true;
            } else {
               String var5 = var4[0];
               Vector var6 = new Vector();
               var6.addAll(Arrays.asList(var4));
               var6.remove(0);
               var4 = (String[])((String[])var6.toArray(new String[0]));
               if (!this.commands.containsKey(var5)) {
                  var1.sendMessage("Este comando no existe");
                  return true;
               } else {
                  try {
                     ((BaseCommand)this.commands.get(var5)).onCommand(var1, var4);
                  } catch (Exception var8) {
                     var8.printStackTrace();
                     var1.sendMessage("Ocurrió un error al ejecutar el comando. Revisa la consola");
                     var1.sendMessage("Escribe /sw help para obtener ayuda");
                  }

                  return true;
               }
            }
         } else {
            this.help(var1);
            return true;
         }
      } else {
         return true;
      }
   }

   private void help(CommandSender var1) {
      String var2 = "---------- §8[§7SkyWars§8]§a " + SkyWars.getPlugin().getDescription().getVersion() + " §r----------";
      var1.sendMessage(var2);
      Iterator var3 = this.commands.values().iterator();

      while(var3.hasNext()) {
         BaseCommand var4 = (BaseCommand)var3.next();
         if (!var4.help(var1).isEmpty()) {
            if (!(var1 instanceof Player)) {
               if (var4.console()) {
                  var1.sendMessage(ChatColor.translateAlternateColorCodes('&', var4.help(var1)));
               }
            } else {
               var1.sendMessage(ChatColor.translateAlternateColorCodes('&', var4.help(var1)));
            }
         }
      }

      var1.sendMessage("-----------------------------------");
   }

   public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var2.getName().equalsIgnoreCase("sw") && var1 instanceof Player) {
         if (var4.length == 1) {
            ArrayList var8 = new ArrayList();
            StringUtil.copyPartialMatches(var4[0], this.cmds, var8);
            Collections.sort(var8);
            return var8;
         }

         if (var4.length >= 2) {
            String var5 = var4[0];
            Vector var6 = new Vector(Arrays.asList(var4));
            var6.remove(0);
            var4 = (String[])var6.toArray(new String[0]);
            if (!this.commands.containsKey(var5)) {
               var1.sendMessage("Este comando no existe");
               return null;
            }

            Object var7 = ((BaseCommand)this.commands.get(var5)).onTabComplete(var1, var4);
            if (var7 == null) {
               var7 = new ArrayList();
            }

            return (List)var7;
         }
      }

      return null;
   }
}
