package me.aitor.skywars.config;

import java.io.File;

public interface IConfiguration {
   File getFile();

   void save();

   void addDefault(String var1, Object var2, String... var3);

   void createSection(String var1, String... var2);

   void setHeader(String... var1);

   void set(String var1, Object var2, String... var3);
}
