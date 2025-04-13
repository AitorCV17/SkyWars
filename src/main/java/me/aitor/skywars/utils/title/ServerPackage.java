package me.aitor.skywars.utils.title;

import org.bukkit.Bukkit;

public enum ServerPackage {
   MINECRAFT("net.minecraft.server." + getServerVersion()),
   CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion());

   private final String path;

   private ServerPackage(String path) {
      this.path = path;
   }

   public static String getServerVersion() {
      return Bukkit.getServer().getClass().getPackage().getName().substring(23);
   }

   @Override
   public String toString() {
      return this.path;
   }

   public Class<?> getClass(String name) {
      try {
         return Class.forName(this.toString() + "." + name);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException("No se puede encontrar la clase: " + this + "." + name, e);
      }
   }
}
