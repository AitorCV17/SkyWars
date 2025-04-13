package me.aitor.skywars.abilities;

import me.aitor.skywars.config.ConfigManager;
import java.util.HashMap;
import java.util.Iterator;

public class Ability {
   private String name;
   private AbilityType type;
   private boolean enabled;
   private HashMap<Integer, AbilityLevel> levels = new HashMap();

   public Ability(String var1, AbilityType var2) {
      this.name = var1;
      this.type = var2;
      this.enabled = ConfigManager.abilities.getBoolean("abilities." + var1 + ".enabled");
      Iterator var3 = ConfigManager.abilities.getConfigurationSection("abilities." + var1 + ".level").getKeys(false).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         int var5 = Integer.parseInt(var4);
         int var6 = ConfigManager.abilities.getInt("abilities." + var1 + ".level." + var4 + ".chance");
         int var7 = ConfigManager.abilities.getInt("abilities." + var1 + ".level." + var4 + ".price");
         boolean var9 = false;
         int var8;
         if (ConfigManager.abilities.isSet("abilities." + var1 + ".level." + var4 + ".value")) {
            var9 = true;
            var8 = ConfigManager.abilities.getInt("abilities." + var1 + ".level." + var4 + ".value");
         } else {
            var8 = 0;
         }

         AbilityLevel var10 = new AbilityLevel(var5, var6, var7, var9, var8);
         this.addLevel(var5, var10);
      }

      AbilityManager.abilities.put(var1, this);
      AbilityManager.abilitiesbyType.put(var2, this);
   }

   public String getName() {
      return this.name;
   }

   public AbilityType getType() {
      return this.type;
   }

   public AbilityLevel getLevel(int var1) {
      return this.levels.containsKey(var1) ? (AbilityLevel)this.levels.get(var1) : null;
   }

   public HashMap<Integer, AbilityLevel> getLevels() {
      return this.levels;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setType(AbilityType var1) {
      this.type = var1;
   }

   public void setLevels(HashMap<Integer, AbilityLevel> var1) {
      this.levels = var1;
   }

   public void addLevel(int var1, AbilityLevel var2) {
      this.levels.put(var1, var2);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }
}
