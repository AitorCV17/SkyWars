package me.aitor.skywars.utils;

import me.aitor.skywars.SkyWars;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileResClassLoader extends ClassLoader {
   private final transient File dataFolder = SkyWars.getPlugin().getDataFolder();

   public FileResClassLoader(ClassLoader var1) {
      super(var1);
   }

   public URL getResource(String var1) {
      File var2 = new File(this.dataFolder, var1);
      if (var2.exists()) {
         try {
            return var2.toURI().toURL();
         } catch (MalformedURLException var4) {
         }
      }

      return null;
   }

   public InputStream getResourceAsStream(String var1) {
      File var2 = new File(this.dataFolder, var1);
      if (var2.exists()) {
         try {
            return new FileInputStream(var2);
         } catch (FileNotFoundException var4) {
         }
      }

      return null;
   }
}
