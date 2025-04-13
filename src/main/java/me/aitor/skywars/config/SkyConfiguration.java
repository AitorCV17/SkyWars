package me.aitor.skywars.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class SkyConfiguration extends YamlConfiguration implements IConfiguration {
   private EConfiguration econfig = new EConfiguration();
   private File file;

   public SkyConfiguration(File file) {
      this.file = file;

      // Si no existe el archivo, lo crea vacío
      if (!file.exists()) {
         try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            // Puedes escribir valores por defecto si quieres aquí también
            Bukkit.getLogger().log(Level.INFO, "[SkyWars] Config \"{0}\" creada automáticamente.", file.getName());
         } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "No se pudo crear el archivo " + file, e);
         }
      }

      try {
         this.load(file);
      } catch (InvalidConfigurationException | IOException e) {
         Bukkit.getLogger().log(Level.SEVERE, "No se pudo cargar " + file, e);
      }
   }


   public EConfiguration getEConfig() {
      return this.econfig;
   }

   public File getFile() {
      return this.file;
   }

   public void load(File file) throws IOException, InvalidConfigurationException {
      this.file = file;
      super.load(file);
      super.options().header("");

      List<String> lines = new ArrayList<>();
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new FileReader(file));
         String line;
         while ((line = reader.readLine()) != null) {
            lines.add(line);
         }
      } finally {
         if (reader != null) reader.close();
      }

      if (lines.isEmpty()) {
         // Archivo vacío, no hay contenido que procesar (sin mostrar error)
         return;
      }

      boolean firstLineNotEmpty = !this.econfig.trim(lines.get(0)).isEmpty();
      LinkedHashMap<String, List<String>> comments = new LinkedHashMap<>();

      for (int i = 0; i < lines.size(); i++) {
         String line = lines.get(i);
         String trimmed = this.econfig.trimPrefixSpaces(line);
         if (trimmed.startsWith("#") && (i > 0 || !firstLineNotEmpty)) {
            String path = this.econfig.getPathToComment(lines, i, line);
            if (path != null) {
               List<String> list = comments.getOrDefault(path, new ArrayList<>());
               list.add(trimmed.startsWith("# ") ? trimmed.substring(2) : trimmed.substring(1));
               comments.put(path, list);
            }
         }
      }

      this.econfig.getComments().putAll(comments);
   }

   public void save() {
      try {
         this.save(this.file);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void save(File file) throws IOException {
      super.save(file);

      List<String> lines = new ArrayList<>();
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new FileReader(file));
         String line;
         while ((line = reader.readLine()) != null) {
            lines.add(line);
         }
      } finally {
         if (reader != null) reader.close();
      }

      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new FileWriter(file));
         for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String path = null;

            if (!line.startsWith("#") && line.contains(":")) {
               path = this.econfig.getPathToKey(lines, i, line);
            }

            if (path != null && this.econfig.getComments().containsKey(path)) {
               int spaces = this.econfig.getPrefixSpaceCount(line);
               StringBuilder indent = new StringBuilder();
               for (int s = 0; s < spaces; s++) indent.append(" ");

               List<String> commentList = this.econfig.getComments().get(path);
               for (String comment : commentList) {
                  writer.append(indent).append("# ").append(comment);
                  writer.newLine();
               }
            }

            if (!line.startsWith("-") && !line.startsWith("  -") && !line.startsWith("    -") && !line.startsWith("      -")) {
               writer.append(line);
            } else {
               writer.append("  ").append(line);
            }

            writer.newLine();

            if (this.econfig.shouldAddNewLinePerKey() && i < lines.size() - 1 && !line.startsWith("#")) {
               String nextLine = lines.get(i + 1);
               if (nextLine != null && !nextLine.startsWith(" ") && !nextLine.startsWith("'") && !nextLine.startsWith("-")) {
                  writer.newLine();
               }
            }
         }
      } finally {
         if (writer != null) writer.close();
      }
   }

   public void set(String path, Object value) {
      if (value != null) {
         if (this.econfig.getComments(path).size() > 0) {
            this.econfig.getComments().put(path, this.econfig.getComments(path));
         } else {
            this.econfig.getComments().remove(path);
         }
      } else {
         this.econfig.getComments().remove(path);
      }

      super.set(path, value);
   }

   public void set(String path, Object value, String... comments) {
      if (value != null) {
         if (comments != null && comments.length > 0) {
            List<String> list = new ArrayList<>();
            for (String comment : comments) {
               list.add(comment != null ? comment : "");
            }
            this.econfig.getComments().put(path, list);
         } else {
            this.econfig.getComments().remove(path);
         }
      } else {
         this.econfig.getComments().remove(path);
      }

      super.set(path, value);
   }

   public void addDefault(String path, Object value, String... comments) {
      if (value != null && comments != null && comments.length > 0 && !this.econfig.getComments().containsKey(path)) {
         List<String> list = new ArrayList<>();
         for (String comment : comments) {
            list.add(comment != null ? comment : "");
         }
         this.econfig.getComments().put(path, list);
      }

      super.addDefault(path, value);
   }

   public void createSection(String path, String... comments) {
      if (path != null && comments != null && comments.length > 0) {
         List<String> list = new ArrayList<>();
         for (String comment : comments) {
            list.add(comment != null ? comment : "");
         }
         this.econfig.getComments().put(path, list);
      }

      super.createSection(path);
   }

   public void setHeader(String... lines) {
      StringBuilder header = new StringBuilder();
      for (String line : lines) {
         header.append(line).append("\n");
      }

      super.options().header(header.toString());
   }
}
