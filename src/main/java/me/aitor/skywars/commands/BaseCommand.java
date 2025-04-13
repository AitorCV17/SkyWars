package me.aitor.skywars.commands;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface BaseCommand {
   boolean onCommand(CommandSender var1, String[] var2);

   List<String> onTabComplete(CommandSender var1, String[] var2);

   String help(CommandSender var1);

   String getPermission();

   boolean console();
}
