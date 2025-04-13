package me.aitor.skywars.abilities;

public class AbilityLevel {
   private int level;
   private int chance;
   private int price;
   private int value;
   private boolean hasvalue;

   public AbilityLevel(int var1, int var2, int var3, boolean var4, int var5) {
      this.setLevel(var1);
      this.setChance(var2);
      this.setPrice(var3);
      this.setHasvalue(var4);
      this.setValue(var5);
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int var1) {
      this.level = var1;
   }

   public int getChance() {
      return this.chance;
   }

   public void setChance(int var1) {
      this.chance = var1;
   }

   public int getPrice() {
      return this.price;
   }

   public void setPrice(int var1) {
      this.price = var1;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int var1) {
      this.value = var1;
   }

   public boolean isHasvalue() {
      return this.hasvalue;
   }

   public void setHasvalue(boolean var1) {
      this.hasvalue = var1;
   }
}
