package me.aitor.skywars.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDir extends SimpleFileVisitor<Path> {
   private static ZipOutputStream zos;
   private Path sourceDir;

   public ZipDir(Path var1) {
      this.sourceDir = var1;
   }

   public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) {
      try {
         Path var3 = this.sourceDir.relativize(var1);
         zos.putNextEntry(new ZipEntry(var3.toString()));
         byte[] var4 = Files.readAllBytes(var1);
         zos.write(var4, 0, var4.length);
         zos.closeEntry();
      } catch (IOException var5) {
         System.err.println(var5);
      }
      return FileVisitResult.CONTINUE;
   }

   public static void zipFile(String var0) {
      String var1 = var0;
      Path var2 = Paths.get(var0);
      try {
         String var3 = var1.concat("-Respaldo.zip");
         zos = new ZipOutputStream(new FileOutputStream(var3));
         Files.walkFileTree(var2, new ZipDir(var2));
         zos.close();
      } catch (IOException var4) {
         System.err.println("Error de I/O: " + var4);
      }
   }
}
