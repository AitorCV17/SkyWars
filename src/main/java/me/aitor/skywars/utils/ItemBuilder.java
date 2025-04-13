package me.aitor.skywars.utils;

import me.aitor.skywars.SkyWars;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ItemBuilder {
   private Material mat;
   private int amount;
   private short data;
   private String title;
   private List<String> lore;
   private Map<Enchantment, Integer> enchants;
   private Color color;
   private PotionType potion;
   private boolean potionUpgraded;
   private boolean potionExtended;
   private boolean potionSplash;
   private boolean hideFlags;
   private boolean glow;
   private String skull;

   public ItemBuilder(Material var1) {
      this(var1, (int)1);
   }

   public ItemBuilder(Material var1, int var2) {
      this(var1, var2, (short)0);
   }

   public ItemBuilder(Material var1, short var2) {
      this(var1, 1, var2);
   }

   public ItemBuilder(Material var1, int var2, short var3) {
      this.title = null;
      this.lore = new ArrayList();
      this.enchants = new HashMap();
      this.mat = var1;
      if (this.mat == null) {
         this.mat = Material.BEDROCK;
      }

      this.amount = var2;
      this.data = var3;
      this.hideFlags = false;
   }

   public ItemBuilder(ItemStack var1) {
      this.title = null;
      this.lore = new ArrayList();
      this.enchants = new HashMap();
      this.mat = var1.getType();
      this.amount = var1.getAmount();
      this.data = var1.getDurability();
      ItemMeta var2 = var1.getItemMeta();
      this.title = var2.getDisplayName();
      this.lore = var2.getLore();
      if (var2 instanceof LeatherArmorMeta) {
         this.color = ((LeatherArmorMeta)var2).getColor();
      }

      if (var2 instanceof PotionMeta) {
         if (SkyWars.is19orHigher()) {
            this.potion = Utils19.getPotionData(var1).getType();
            this.potionUpgraded = Utils19.getPotionData(var1).isUpgraded();
            this.potionSplash = false;
            this.potionExtended = Utils19.getPotionData(var1).isExtended();
         } else {
            Potion var3 = Potion.fromItemStack(var1);
            this.potion = var3.getType();
            this.potionUpgraded = var3.getLevel() > 1;
            this.potionSplash = var3.isSplash();
            this.potionExtended = var3.hasExtendedDuration();
         }
      }

      this.enchants.putAll(var1.getEnchantments());
   }

   public ItemBuilder setType(Material var1) {
      this.mat = var1;
      return this;
   }

   public ItemBuilder setData(short var1) {
      this.data = var1;
      return this;
   }

   public ItemBuilder setTitle(String var1) {
      this.title = ChatColor.translateAlternateColorCodes('&', var1);
      return this;
   }

   public ItemBuilder addLore(String var1) {
      this.lore.add(ChatColor.translateAlternateColorCodes('&', var1));
      return this;
   }

   public ItemBuilder addLore(List<String> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.lore.add(ChatColor.translateAlternateColorCodes('&', var3));
      }

      return this;
   }

   public ItemBuilder removeLastLoreLine() {
      this.lore.remove(this.lore.size() - 1);
      return this;
   }

   public ItemBuilder setLore(List<String> var1) {
      this.lore.clear();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.lore.add(ChatColor.translateAlternateColorCodes('&', var3));
      }

      return this;
   }

   public ItemBuilder addEnchantment(Enchantment var1, int var2) {
      if (this.enchants.containsKey(var1)) {
         this.enchants.remove(var1);
      }

      this.enchants.put(var1, var2);
      return this;
   }

   public ItemBuilder setColor(Color var1) {
      if (this.mat.name().contains("LEATHER_")) {
         this.color = var1;
      }

      return this;
   }

   public ItemBuilder setHideFlags(boolean var1) {
      this.hideFlags = var1;
      return this;
   }

   public boolean isHideFlags() {
      return this.hideFlags;
   }

   public boolean isGlow() {
      return this.glow;
   }

   public void setGlow(boolean var1) {
      this.glow = var1;
   }

   public ItemBuilder setPotion(String var1, Material var2, boolean var3, boolean var4) {
      this.mat = var2;

      try {
         if (var2 == Material.SPLASH_POTION) {
            this.potionSplash = true;
         }
      } catch (NoSuchFieldError var6) {
         this.mat = Material.POTION;
         this.potionSplash = true;
      }

      this.potion = PotionType.valueOf(var1);
      this.potionUpgraded = var3;
      this.potionExtended = var4;
      return this;
   }

   public ItemBuilder setAmount(int var1) {
      this.amount = var1;
      return this;
   }

   public ItemBuilder setSkullOwner(String var1) {
      if (this.mat != Material.SKULL_ITEM) {
         this.mat = Material.SKULL_ITEM;
         this.data = 3;
      }

      this.skull = var1;
      return this;
   }

   public ItemStack build() {
      if (this.mat == null) {
         this.mat = Material.AIR;
      }

      ItemStack var1 = new ItemStack(this.mat, this.amount, this.data);
      Object var2 = var1.getItemMeta();
      if (var2 instanceof LeatherArmorMeta && this.color != null) {
         ((LeatherArmorMeta)var2).setColor(this.color);
      }

      if (var2 instanceof SkullMeta && this.skull != null) {
         ((SkullMeta)var2).setOwner(this.skull);
      }

      if (var2 instanceof PotionMeta && this.potion != null) {
         if (SkyWars.is19orHigher()) {
            var2 = Utils19.getPotion(this.mat, this.potion, this.potionExtended, this.potionUpgraded);
         } else {
            Potion var3 = new Potion(this.potion, this.potionUpgraded ? 2 : 1, this.potionSplash, this.potionExtended);
            var3.apply(var1);
         }
      }

      if (this.title != null) {
         ((ItemMeta)var2).setDisplayName(this.title);
      }

      if (!this.lore.isEmpty()) {
         ((ItemMeta)var2).setLore(this.lore);
      }

      if (this.hideFlags) {
         ((ItemMeta)var2).addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS});
      }

      if (this.glow) {
         Glow var4 = new Glow(120);
         ((ItemMeta)var2).addEnchant(var4, 0, true);
      }

      var1.setItemMeta((ItemMeta)var2);
      var1.addUnsafeEnchantments(this.enchants);
      return var1;
   }

   public ItemBuilder clone() {
      ItemBuilder var1 = new ItemBuilder(this.mat, this.amount, this.data);
      var1.setTitle(this.title);
      var1.setLore(this.lore);
      Iterator var2 = this.enchants.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.addEnchantment((Enchantment)var3.getKey(), (Integer)var3.getValue());
      }

      var1.setColor(this.color);
      var1.potion = this.potion;
      var1.potionExtended = this.potionExtended;
      var1.potionUpgraded = this.potionUpgraded;
      var1.potionSplash = this.potionSplash;
      return var1;
   }

   public Material getType() {
      return this.mat;
   }

   public short getData() {
      return this.data;
   }

   public String getTitle() {
      return this.title;
   }

   public List<String> getLore() {
      return this.lore;
   }

   public Color getColor() {
      return this.color;
   }

   public boolean hasEnchantment(Enchantment var1) {
      return this.enchants.containsKey(var1);
   }

   public int getEnchantmentLevel(Enchantment var1) {
      return (Integer)this.enchants.get(var1);
   }

   public Map<Enchantment, Integer> getAllEnchantments() {
      return this.enchants;
   }

   public boolean isItem(ItemStack var1) {
      if (var1 == null) {
         return false;
      } else {
         ItemMeta var2 = var1.getItemMeta();
         if (var1.getType() != this.getType()) {
            return false;
         } else if (!var2.hasDisplayName() && this.getTitle() != null) {
            return false;
         } else if (!var2.getDisplayName().equals(this.getTitle())) {
            return false;
         } else if (!var2.hasLore() && !this.getLore().isEmpty()) {
            return false;
         } else {
            Iterator var3;
            if (var2.hasLore()) {
               var3 = var2.getLore().iterator();

               while(var3.hasNext()) {
                  String var4 = (String)var3.next();
                  if (!this.getLore().contains(var4)) {
                     return false;
                  }
               }
            }

            var3 = var1.getEnchantments().keySet().iterator();

            Enchantment var5;
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               var5 = (Enchantment)var3.next();
            } while(this.hasEnchantment(var5));

            return false;
         }
      }
   }

   public String toString() {
      String var1 = "";
      var1 = var1 + this.mat.toString();
      if (this.data != 0) {
         var1 = var1 + ":" + this.data;
      }

      if (this.amount > 1) {
         var1 = var1 + "," + this.amount;
      }

      if (this.title != null) {
         var1 = var1 + ",name:" + this.title;
      }

      Iterator var2;
      String var3;
      if (!this.lore.isEmpty()) {
         for(var2 = this.lore.iterator(); var2.hasNext(); var1 = var1 + ",lore:" + var3) {
            var3 = (String)var2.next();
         }
      }

      Entry var4;
      for(var2 = this.getAllEnchantments().entrySet().iterator(); var2.hasNext(); var1 = var1 + "," + ((Enchantment)var4.getKey()).getName() + ((Integer)var4.getValue() > 1 ? ":" + var4.getValue() : "")) {
         var4 = (Entry)var2.next();
      }

      if (this.color != null) {
         var1 = var1 + ",leather_color:" + this.color.getRed() + "-" + this.color.getGreen() + "-" + this.color.getBlue();
      }

      if (this.potion != null) {
         var1 = var1 + ",potion:" + this.potion.toString() + ":" + this.potionUpgraded + ":" + this.potionExtended;
      }

      if (this.glow) {
         var1 = var1 + ",glowing";
      }

      return var1;
   }
}
