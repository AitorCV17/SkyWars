package me.aitor.skywars.box;

import me.aitor.skywars.SkyWars;
import java.util.HashMap;
import java.util.Iterator;

public class BoxManager {
   public static HashMap<String, Box> boxes = new HashMap();

   public static void initBoxes() {
      boxes.clear();
      Iterator var0 = SkyWars.boxes.getConfigurationSection("boxes").getKeys(false).iterator();

      while(var0.hasNext()) {
         String var1 = (String)var0.next();
         Box var2 = new Box(SkyWars.boxes.getString("boxes." + var1 + ".name"), SkyWars.boxes.getString("boxes." + var1 + ".desc"));
         var2.setItem(SkyWars.boxes.getInt("boxes." + var1 + ".item"));
         var2.setData(SkyWars.boxes.getInt("boxes." + var1 + ".data"));
         var2.setSlot(SkyWars.boxes.getInt("boxes." + var1 + ".slot"));
         var2.setSection(var1);
         boxes.put(var1, var2);
      }

   }

   public static Box getDefaultBox() {
      return (Box)boxes.get(SkyWars.boxes.getString("default"));
   }

   public static Box[] getBoxes() {
      return (Box[])boxes.values().toArray(new Box[boxes.values().size()]);
   }

   public static Box getBox(String var0) {
      return (Box)boxes.get(var0);
   }
}
