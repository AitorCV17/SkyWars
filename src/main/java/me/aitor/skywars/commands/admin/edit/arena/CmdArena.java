package me.aitor.skywars.commands.admin.edit.arena;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.commands.BaseCommand;
import me.aitor.skywars.utils.LocationUtil;
import me.aitor.skywars.utils.ZipDir;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CmdArena implements BaseCommand {
   public boolean onCommand(CommandSender var1, String[] var2) {
      Player var3 = null;
      if (!(var1 instanceof Player)) {
         var1.sendMessage("¡No eres un jugador!");
         return true;
      } else {
         var3 = (Player)var1;
         if (!var3.hasPermission(this.getPermission())) {
            var3.sendMessage("§c¡No tienes permisos!");
            return true;
         } else if (var2.length == 0) {
            this.helpDefault(var1);
            return true;
         } else {
            if (var2.length >= 1) {
               String var4 = var2[0].toLowerCase();
               byte var5 = -1;
               switch(var4.hashCode()) {
                  case -1352294148:
                     if (var4.equals("create")) {
                        var5 = 1;
                     }
                     break;
                  case -934641255:
                     if (var4.equals("reload")) {
                        var5 = 5;
                     }
                     break;
                  case 113762:
                     if (var4.equals("set")) {
                        var5 = 3;
                     }
                     break;
                  case 3327206:
                     if (var4.equals("load")) {
                        var5 = 0;
                     }
                     break;
                  case 3522941:
                     if (var4.equals("save")) {
                        var5 = 6;
                     }
                     break;
                  case 109638523:
                     if (var4.equals("spawn")) {
                        var5 = 2;
                     }
                     break;
                  case 1671308008:
                     if (var4.equals("disable")) {
                        var5 = 4;
                     }
               }

               String var6;
               Arena var7;
               String var8;
               int var10;
               switch(var5) {
                  case 0:
                     if (!var1.hasPermission("skywars.admin.arena.load")) {
                        return false;
                     }

                     if (var2.length == 1) {
                        var3.sendMessage("§cUso: /sw arena load <mundo>");
                        StringBuilder var17 = new StringBuilder();
                        Iterator var20 = Bukkit.getWorlds().iterator();

                        while(var20.hasNext()) {
                           World var21 = (World)var20.next();
                           var17.append(", ").append(var21.getName());
                        }

                        var3.sendMessage(String.format("§cLista de mundos cargados: %s", var17.toString().replaceFirst(", ", "")));
                     }

                     if (var2.length == 2) {
                        File var18 = new File(SkyWars.maps);
                        String var22 = var2[1];
                        if (var18.exists() && var18.isDirectory()) {
                           File[] var23 = var18.listFiles();
                           int var27 = var23.length;

                           for(var10 = 0; var10 < var27; ++var10) {
                              File var28 = var23[var10];
                              if (var28.getName().contains(var22) && var28.isDirectory()) {
                                 try {
                                    ArenaManager.delete(new File(var28.getName()));
                                    ArenaManager.copyFolder(var28, new File(var28.getName()));
                                    WorldCreator var12 = new WorldCreator(var22);
                                    var12.generateStructures(false);
                                    World var13 = var12.createWorld();
                                    var13.setAutoSave(false);
                                    var13.setKeepSpawnInMemory(false);
                                    var13.setGameRuleValue("doMobSpawning", "false");
                                    var13.setGameRuleValue("doDaylightCycle", "false");
                                    var13.setGameRuleValue("mobGriefing", "false");
                                    var13.setGameRuleValue("commandBlockOutput", "false");
                                    var13.setTime(0L);
                                    var3.sendMessage("§a" + var22 + " cargado");
                                 } catch (Exception var16) {
                                    var16.printStackTrace();
                                 }
                              }
                           }
                        }
                     }
                     break;
                  case 1:
                     if (!var1.hasPermission("skywars.admin.arena.create")) {
                        return false;
                     }

                     if (var2.length == 1) {
                        var3.sendMessage("§cUso: /sw arena create <nombre_arena>");
                     }

                     if (var2.length == 2) {
                        var6 = var2[1];
                        var7 = ArenaManager.getGame(var6);
                        if (var7 != null) {
                           var3.sendMessage("§c¡Esta arena ya existe!");
                           return false;
                        }

                        new Arena(var6, true);
                        var3.sendMessage("§a" + var6 + " ha sido creada");
                     }
                     break;
                  case 2:
                     if (!var1.hasPermission("skywars.admin.arena.spawn")) {
                        return false;
                     }

                     if (var2.length == 1) {
                        var3.sendMessage("§a/sw arena §espawn §dadd §a- §bAgregar punto de aparición");
                        var3.sendMessage("§a/sw arena §espawn §dremove §9[#] §a- §bEliminar el último o un punto de aparición específico");
                        var3.sendMessage("§a/sw arena §espawn §dspect §a- §bEstablecer punto de aparición de espectadores");
                     }

                     if (var2.length >= 2) {
                        var6 = var3.getWorld().getName();
                        var7 = ArenaManager.getGame(var6);
                        if (var7 == null) {
                           var3.sendMessage("§cPrimero necesitas crear la arena (/sw arena create <nombre>)");
                           return false;
                        }

                        if (!var7.isDisabled()) {
                           var3.sendMessage("§cNo puedes editar una arena si no está deshabilitada");
                           return false;
                        }

                        List var19 = var7.getConfig().getStringList("spawnpoints");
                        String var25 = var2[1].toLowerCase();
                        byte var26 = -1;
                        switch(var25.hashCode()) {
                           case -934610812:
                              if (var25.equals("remove")) {
                                 var26 = 1;
                              }
                              break;
                           case 96417:
                              if (var25.equals("add")) {
                                 var26 = 0;
                              }
                              break;
                           case 109641753:
                              if (var25.equals("spect")) {
                                 var26 = 2;
                              }
                        }

                        switch(var26) {
                           case 0:
                              var19.add(LocationUtil.getString(var3.getLocation(), true));
                              var7.getConfig().set("spawnpoints", var19);
                              var7.getConfig().save();
                              var3.sendMessage("§aPunto de aparición agregado (" + var19.size() + ")");
                              return true;
                           case 1:
                              if (var19.isEmpty()) {
                                 var3.sendMessage("§cEsta arena no tiene puntos de aparición");
                                 return false;
                              }

                              int var11 = var19.size();
                              if (var2.length >= 3) {
                                 var11 = Integer.parseInt(var2[2]);
                              }

                              var19.remove(var11 - 1);
                              var7.getConfig().set("spawnpoints", var19);
                              var7.getConfig().save();
                              var3.sendMessage("§aPunto de aparición #" + var11 + " eliminado");
                              return true;
                           case 2:
                              var7.getConfig().set("spectator_spawn", LocationUtil.getString(var3.getLocation(), true));
                              var7.getConfig().save();
                              var3.sendMessage("§aPunto de aparición de espectadores establecido");
                              return true;
                           default:
                              var3.sendMessage("§a/sw arena §espawn §dadd §a- §bAgregar punto de aparición");
                              var3.sendMessage("§a/sw arena §espawn §dremove §9[#] §a- §bEliminar el último o un punto de aparición específico");
                              var3.sendMessage("§a/sw arena §espawn §dspect §a- §bEstablecer punto de aparición de espectadores");
                        }
                     }
                     break;
                  case 3:
                     if (!var1.hasPermission("skywars.admin.arena.set")) {
                        return false;
                     }

                     if (var2.length == 1) {
                        var3.sendMessage("§a/sw arena §eset §dmax §9<cantidad> §a- §bEstablecer jugadores máximos en la arena");
                        var3.sendMessage("§a/sw arena §eset §dmin §9<cantidad> §a- §bEstablecer jugadores mínimos en la arena");
                     }

                     if (var2.length >= 2) {
                        var6 = var3.getWorld().getName();
                        var7 = ArenaManager.getGame(var6);
                        if (var7 == null) {
                           var3.sendMessage("§cPrimero necesitas crear la arena (/sw arena create <nombre>)");
                           return false;
                        }

                        if (!var7.isDisabled()) {
                           var3.sendMessage("§cNo puedes editar una arena si no está deshabilitada");
                           return false;
                        }

                        var8 = var2[1].toLowerCase();
                        byte var24 = -1;
                        switch(var8.hashCode()) {
                           case 107876:
                              if (var8.equals("max")) {
                                 var24 = 0;
                              }
                              break;
                           case 108114:
                              if (var8.equals("min")) {
                                 var24 = 1;
                              }
                        }

                        switch(var24) {
                           case 0:
                              if (var2.length == 2) {
                                 var3.sendMessage("§a/sw arena §eset §dmax §9<cantidad> §a- §bEstablecer jugadores máximos en la arena");
                              }

                              if (var2.length == 3) {
                                 var10 = Integer.parseInt(var2[2]);
                                 var7.getConfig().set("max_players", var10);
                                 var7.getConfig().save();
                                 var3.sendMessage("§aJugadores máximos establecidos a " + var10 + " en " + var6);
                              }

                              return true;
                           case 1:
                              if (var2.length == 2) {
                                 var3.sendMessage("§a/sw arena §eset §dmin §9<cantidad> §a- §bEstablecer jugadores mínimos en la arena");
                              }

                              if (var2.length == 3) {
                                 var10 = Integer.parseInt(var2[2]);
                                 if (var10 <= 1) {
                                    var3.sendMessage("§cNo se recomienda establecer el mínimo de jugadores en " + var10 + ", esto podría causar que el juego inicie con un solo jugador (y si está solo, ganará)");
                                 }

                                 var7.getConfig().set("min_players", var10);
                                 var7.getConfig().save();
                                 var3.sendMessage("§aJugadores mínimos establecidos a " + var10 + " en " + var6);
                                 return true;
                              }

                              return false;
                           default:
                              var3.sendMessage("§a/sw arena §eset §dmax §9<cantidad> §a- §bEstablecer jugadores máximos en la arena");
                              var3.sendMessage("§a/sw arena §eset §dmin §9<cantidad> §a- §bEstablecer jugadores mínimos en la arena");
                              return true;
                        }
                     }
                     break;
                  case 4:
                     if (!var1.hasPermission("skywars.admin.arena.disable")) {
                        return false;
                     }

                     if (var2.length >= 2) {
                        var6 = var2[1];
                        var7 = ArenaManager.getGame(var6);
                        if (var7 != null) {
                           if (var7.isDisabled()) {
                              var3.sendMessage("§cLa arena ya está deshabilitada");
                              return false;
                           }

                           var7.setDisabled(true);
                           var7.restart();
                           var3.sendMessage("§a" + var6 + " ha sido deshabilitada y ahora puedes editarla");
                           return true;
                        }

                        var3.sendMessage("§cLa arena no existe");
                        return false;
                     }

                     var3.sendMessage("§a/sw arena §edisable §d<nombre_arena> §a- §bDeshabilitar una arena para editarla");
                     return false;
                  case 5:
                     if (!var1.hasPermission("skywars.admin.arena.reload")) {
                        return false;
                     }

                     if (var2.length >= 2) {
                        var6 = var2[1];
                        var7 = ArenaManager.getGame(var6);
                        if (var7 != null) {
                           var7.setDisabled(false);
                           var7.restart();
                           var3.sendMessage("§a" + var6 + " ha sido recargada" + (var7.isDisabled() ? " §a y ahora está habilitada" : ""));
                           return true;
                        }

                        var3.sendMessage("§cLa arena no existe");
                        return false;
                     }

                     var3.sendMessage("§a/sw arena §ereload §d<nombre_arena> §a- §bRecargar una arena y habilitarla");
                     return false;
                  case 6:
                     if (!var1.hasPermission("skywars.admin.arena.save")) {
                        return false;
                     }

                     if (var2.length >= 2) {
                        var6 = var2[1];
                        var7 = ArenaManager.getGame(var6);
                        if (var7 == null) {
                           var3.sendMessage("§cPrimero necesitas crear la arena (/sw arena create <nombre>)");
                           return false;
                        }

                        if (!var7.isDisabled()) {
                           var3.sendMessage("§cNo puedes guardar una arena si no está deshabilitada");
                           return false;
                        }

                        var7.getWorld().save();
                        var8 = SkyWars.maps + File.separator + var6;
                        File folder = new File(var8);
                        if (!folder.exists()) {
                           folder.mkdirs();
                        }
                        ZipDir.zipFile(var8);
                        var3.sendMessage("§aCopia de seguridad creada para " + var6);
                        File var9 = new File(var8);

                        ArenaManager.delete(var9);
                        try {
                           ArenaManager.copyFolder(new File(var6), var9);
                        } catch (IOException e) {
                           var3.sendMessage("§cError al copiar el mundo de la arena: " + e.getMessage());
                           e.printStackTrace();
                           return false;
                        }

                        var3.sendMessage("§a" + var6 + " ha sido guardada en la carpeta maps");
                        return true;
                     }

                     var3.sendMessage("§a/sw arena §esave §d<nombre_arena> §a- §bGuardar el mundo de una arena");
                     return false;
                  default:
                     this.helpDefault(var1);
               }
            }

            return true;
         }
      }
   }

   public String help(CommandSender var1) {
      String var2 = "&a/sw arena - &bPara obtener más ayuda sobre los comandos de arena";
      return var1.hasPermission(this.getPermission()) ? var2 : "";
   }

   public String getPermission() {
      return "skywars.admin.arena";
   }

   public boolean console() {
      return false;
   }

   public void helpDefault(CommandSender var1) {
      HashMap var2 = new HashMap();
      var2.put("load", "&a/sw arena &eload &a- &bCargar nuevo mundo de arena");
      var2.put("create", "&a/sw arena &ecreate &a- &bCrear nueva arena");
      var2.put("spawn", "&a/sw arena &espawn &a- &bAgregar o eliminar puntos de aparición");
      var2.put("set", "&a/sw arena &eset &a- &bConfigurar la arena");
      var2.put("disable", "&a/sw arena &edisable &a- &bDeshabilitar una arena para editarla");
      var2.put("reload", "&a/sw arena &ereload &a- &bRecargar una arena y habilitarla");
      var2.put("save", "&a/sw arena &esave &a- &bGuardar el mundo de una arena");
      var1.sendMessage("------------ §a[Ayuda de Arena SkyWars] §f------------");
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (var1.hasPermission("skywars.admin.arena." + (String)var4.getKey())) {
            var1.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)var4.getValue()));
         }
      }

      var1.sendMessage("--------------------------------------------");
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      if (!var1.hasPermission(this.getPermission())) {
         return null;
      } else {
         Iterator var7;
         Entry var8;
         HashMap var10;
         String[] var12;
         ArrayList var13;
         ArrayList var14;
         if (var2.length == 1) {
            var10 = new HashMap();
            var10.put("load", "&a/sw arena &eload &a- &bPara obtener más ayuda sobre cargar mundos");
            var10.put("create", "&a/sw arena &ecreate &a- &bPara obtener más ayuda sobre crear arena");
            var10.put("spawn", "&a/sw arena &espawn &a- &bPara obtener más ayuda sobre establecer puntos de aparición");
            var10.put("set", "&a/sw arena &eset &a- &bPara obtener más ayuda sobre configurar la arena");
            var10.put("disable", "&a/sw arena &edisable &a- &bDeshabilitar una arena para editarla");
            var10.put("reload", "&a/sw arena &ereload &a- &bRecargar una arena y habilitarla");
            var10.put("save", "&a/sw arena &esave &a- &bGuardar el mundo de una arena");
            var12 = new String[]{"load", "create", "spawn", "set", "disable", "reload", "save"};
            var13 = new ArrayList(Arrays.asList(var12));
            var14 = new ArrayList();
            StringUtil.copyPartialMatches(var2[0], var13, var14);
            Collections.sort(var14);
            var1.sendMessage("--------------------------------------------");
            var7 = var10.entrySet().iterator();

            while(var7.hasNext()) {
               var8 = (Entry)var7.next();
               if (var14.contains(var8.getKey()) && var1.hasPermission("skywars.admin.arena." + (String)var8.getKey())) {
                  var1.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)var8.getValue()));
               }
            }

            var1.sendMessage("--------------------------------------------");
            return var14;
         } else {
            if (var2.length > 1 && var2.length < 4) {
               ArrayList var4;
               if (var2[0].equalsIgnoreCase("load")) {
                  if (!var1.hasPermission("skywars.admin.arena.load")) {
                     return null;
                  }
                  File var11 = new File(SkyWars.maps);
                  var4 = new ArrayList();
                  var13 = new ArrayList();
                  if (var11.exists() && var11.isDirectory()) {
                     File[] var16 = var11.listFiles();
                     int var17 = var16.length;

                     for(int var20 = 0; var20 < var17; ++var20) {
                        File var9 = var16[var20];
                        if (var9.isDirectory()) {
                           var4.add(var9.getName());
                        }
                     }
                  }

                  Iterator var18 = Bukkit.getWorlds().iterator();

                  while(var18.hasNext()) {
                     World var19 = (World)var18.next();
                     if (var4.contains(var19.getName())) {
                        var4.remove(var19.getName());
                     }
                  }

                  var1.sendMessage("--------------------------------------------");
                  var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aMundos disponibles para cargar (&b" + var4.size() + "&a):"));
                  var1.sendMessage("--------------------------------------------");
                  StringUtil.copyPartialMatches(var2[1], var4, var13);
                  Collections.sort(var13);
                  return var13;
               }

               ArrayList var3;
               Iterator var5;
               Arena var6;
               if (var2[0].equalsIgnoreCase("create")) {
                  if (!var1.hasPermission("skywars.admin.arena.create")) {
                     return null;
                  }
                  var3 = new ArrayList();
                  var4 = new ArrayList();
                  var5 = Bukkit.getWorlds().iterator();

                  while(var5.hasNext()) {
                     World var15 = (World)var5.next();
                     var3.add(var15.getName());
                  }

                  var5 = ArenaManager.getGames().iterator();

                  while(var5.hasNext()) {
                     var6 = (Arena)var5.next();
                     if (var3.contains(var6.getName())) {
                        var3.remove(var6.getName());
                     }
                  }

                  var1.sendMessage("--------------------------------------------");
                  var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPosibles arenas disponibles para crear (&b" + var3.size() + "&a):"));
                  var1.sendMessage("--------------------------------------------");
                  StringUtil.copyPartialMatches(var2[1], var3, var4);
                  Collections.sort(var4);
                  return var4;
               }

               if (var2[0].equalsIgnoreCase("spawn")) {
                  if (!var1.hasPermission("skywars.admin.arena.spawn")) {
                     return null;
                  } else {
                     var10 = new HashMap();
                     var10.put("add", "&a/sw arena &espawn &dadd &a- &bAgregar punto de aparición");
                     var10.put("remove", "&a/sw arena &espawn &dremove &9[#] &a- &bEliminar el último o un punto de aparición específico");
                     var10.put("spect", "&a/sw arena &espawn &dspect &a- &bEstablecer punto de aparición de espectadores");
                     var12 = new String[]{"add", "remove", "spect"};
                     var13 = new ArrayList(Arrays.asList(var12));
                     var14 = new ArrayList();
                     StringUtil.copyPartialMatches(var2[1], var13, var14);
                     Collections.sort(var14);
                     var1.sendMessage("--------------------------------------------");
                     var7 = var10.entrySet().iterator();

                     while(var7.hasNext()) {
                        var8 = (Entry)var7.next();
                        if (var14.contains(var8.getKey())) {
                           var1.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)var8.getValue()));
                        }
                     }

                     var1.sendMessage("--------------------------------------------");
                     return var14;
                  }
               }

               if (var2[0].equalsIgnoreCase("set")) {
                  if (!var1.hasPermission("skywars.admin.arena.set")) {
                     return null;
                  }
                  var10 = new HashMap();
                  var10.put("max", "&a/sw arena &eset &dmax &9<cantidad> &a- &bEstablecer jugadores máximos en la arena");
                  var10.put("min", "&a/sw arena &eset &dmin &9<cantidad> &a- &bEstablecer jugadores mínimos en la arena");
                  var12 = new String[]{"max", "min"};
                  var13 = new ArrayList(Arrays.asList(var12));
                  var14 = new ArrayList();
                  StringUtil.copyPartialMatches(var2[1], var13, var14);
                  Collections.sort(var14);
                  var1.sendMessage("--------------------------------------------");
                  var7 = var10.entrySet().iterator();

                  while(var7.hasNext()) {
                     var8 = (Entry)var7.next();
                     if (var14.contains(var8.getKey())) {
                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)var8.getValue()));
                     }
                  }

                  var1.sendMessage("--------------------------------------------");
                  return var14;
               }

               if (var2[0].equalsIgnoreCase("disable") || var2[0].equalsIgnoreCase("reload") || var2[0].equalsIgnoreCase("save")) {
                  if (!var1.hasPermission("skywars.admin.arena." + var2[0])) {
                     return null;
                  }
                  var3 = new ArrayList();
                  var4 = new ArrayList();
                  var5 = ArenaManager.getGames().iterator();

                  while(var5.hasNext()) {
                     var6 = (Arena)var5.next();
                     var3.add(var6.getName());
                  }

                  var1.sendMessage("--------------------------------------------");
                  StringUtil.copyPartialMatches(var2[1], var3, var4);
                  Collections.sort(var4);
                  return var4;
               }
            }

            return null;
         }
      }
   }
}
