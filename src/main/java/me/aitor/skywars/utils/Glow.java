package me.aitor.skywars.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Glow extends Enchantment {
   public Glow(int var1) {
      super(var1);
   }

   public String getName() {
      return "Glowing";
   }

   public int getMaxLevel() {
      return 10;
   }

   public int getStartLevel() {
      return 1;
   }

   public EnchantmentTarget getItemTarget() {
      return null;
   }

   public boolean isTreasure() {
      return false;
   }

   public boolean isCursed() {
      return false;
   }

   public boolean conflictsWith(Enchantment var1) {
      return false;
   }

   public boolean canEnchantItem(ItemStack var1) {
      return false;
   }
}
