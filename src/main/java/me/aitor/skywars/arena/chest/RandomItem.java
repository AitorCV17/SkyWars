package me.aitor.skywars.arena.chest;

import me.aitor.skywars.utils.ItemBuilder;
import java.util.Random;

public class RandomItem {
   private double chance;
   private int min;
   private int max;
   private ItemBuilder item;

   public RandomItem(double var1, int var3, int var4, ItemBuilder var5) {
      this.chance = var1;
      this.min = var3;
      this.max = var4;
      this.item = var5;
   }

   public ItemBuilder getItem() {
      int var1 = (new Random()).nextInt(this.max - this.min + 1) + this.min;
      return this.item.setAmount(var1);
   }

   public boolean hasChance() {
      return (double)(new Random()).nextInt(10000) < this.chance * 100.0D;
   }
}
