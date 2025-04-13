package me.aitor.skywars.box;

import me.aitor.skywars.utils.ItemBuilder;
import java.util.Arrays;
import org.bukkit.Material;

public class Box {
   private final String name;
   private String desc;
   private String section;
   private int item;
   private int data;
   private int slot;

   public Box(String var1, String var2) {
      this.name = var1;
      this.desc = var2;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.desc;
   }

   public int getItem() {
      return this.item;
   }

   public int getData() {
      return this.data;
   }

   public ItemBuilder getItemBuilder() {
      return (new ItemBuilder(Material.getMaterial(this.getItem()), (short)this.getData())).setTitle(this.getName()).addLore(Arrays.asList(this.getDescription().split("\n")));
   }

   public int getSlot() {
      return this.slot;
   }

   public String getSection() {
      return this.section;
   }

   public void setItem(int var1) {
      this.item = var1;
   }

   public void setData(int var1) {
      this.data = var1;
   }

   public void setDescription(String var1) {
      this.desc = var1;
   }

   public void setSlot(int var1) {
      this.slot = var1;
   }

   public void setSection(String var1) {
      this.section = var1;
   }
}
