package me.aitor.skywars.kit;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.config.SkyConfiguration;
import me.aitor.skywars.utils.Console;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;

public class Kit {
   private String name;
   private int price;
   private List<ItemBuilder> items = new ArrayList();
   private int slot;
   private List<String> item_lore = new ArrayList();
   private List<String> contents = new ArrayList();
   private boolean free = false;
   private ItemBuilder item;
   private SkyConfiguration config;

   public Kit(String var1) {
      this.name = var1;
      File var2 = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.kits + File.separator + var1 + ".yml");
      this.config = new SkyConfiguration(var2);
      this.loadConfig(var2);
      this.price = this.config.getInt("price");
      if (this.price <= 0) {
         this.free = true;
      }

      this.slot = this.config.getInt("icon.slot");
      this.items.clear();
      SkyWars.log("Kit.load - Cargando: " + var1);
      Iterator var3 = this.config.getStringList("items").iterator();

      String var4;
      while(var3.hasNext()) {
         var4 = (String)var3.next();

         try {
            this.items.add(Utils.readItem(var4));
         } catch (NullPointerException var6) {
            Console.severe("El kit '" + var1 + "' ha omitido el ítem \"" + var4.toString() + "\" debido a un error de sintaxis");
         }
      }

      SkyWars.log("Kit.load - Cargado: " + var1);
      var3 = this.config.getStringList("contents").iterator();

      while(var3.hasNext()) {
         var4 = (String)var3.next();
         this.contents.add(var4.toString());
      }

      this.item_lore.add(SkyWars.getMessage(MSG.KIT_CONTENTS));
      var3 = this.contents.iterator();

      while(var3.hasNext()) {
         var4 = (String)var3.next();
         this.item_lore.add(String.format(ChatColor.translateAlternateColorCodes('&', SkyWars.getMessage(MSG.KIT_CONTENTS_FORMAT)), var4));
      }

      this.item = Utils.readItem(this.config.getString("icon.item"));
      this.updateConfig(var2);
      this.item.setTitle(this.isFree() ? String.format(SkyWars.getMessage(MSG.KIT_NAME_FREE), var1) : String.format(SkyWars.getMessage(MSG.KIT_NAME_NOTPURCHASED), var1));
      if (!this.isFree()) {
         this.item.addLore(String.format(SkyWars.getMessage(MSG.KIT_COST), this.price));
      }

      this.item.addLore(this.item_lore);
      KitManager.kits.put(var1, this);
   }

   public String getName() {
      return this.name;
   }

   public int getPrice() {
      return this.price;
   }

   public List<ItemBuilder> getItems() {
      return this.items;
   }

   public int getSlot() {
      return this.slot;
   }

   public ItemBuilder getIcon() {
      return this.item;
   }

   public boolean isFree() {
      return this.free;
   }

   public List<String> getContents() {
      return this.item_lore;
   }

   public SkyConfiguration getConfig() {
      return this.config;
   }

   private void loadConfig(File var1) {
      this.config.addDefault("price", 0, "Precio del kit (si el precio es 0, el kit será gratuito)");
      this.config.addDefault("icon.slot", 0, "Ranura en el Selector de Kits (En espera)");
      this.config.addDefault("icon.item", "1:0", "Ítem en el Selector de Kits (ID:Data)");
      this.config.addDefault("contents", new ArrayList(), "Descripción del kit");
      this.config.addDefault("items", new ArrayList(), "Ítems a otorgar en el kit", "Formato: ID:Data,Cantidad (Si Data es igual a 0 o la Cantidad es igual a 1, no es necesario escribirlo)", "Formato: NOMBRE_DEL_ÍTEM:Data,Cantidad", "Formato: ID:Data,Cantidad,Valor,Valor,Valor,...", "Valores disponibles:", "    name:&3Nombre del Ítem", "    lore:&7Línea de descripción del ítem", "    ENCHANTMENT:LEVEL  (Lista de encantamientos aquí: https://goo.gl/KKBDiH)", "    potion:POTION_NAME:Upgraded:Extended  (Lista de pociones aquí: https://goo.gl/aBGNSw) (Ejemplo: potion:JUMP:true:false para Salto II)", "    leather_color:R-G-B  (Valores R,G,B de 0 a 255)", "    glowing - Agrega efecto Brillante/Encantamiento al ítem");
      this.config.options().copyDefaults(true);
      this.config.getEConfig().setNewLinePerKey(true);
      this.config.save();
   }

   private void updateConfig(File var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.items.iterator();

      while(var3.hasNext()) {
         ItemBuilder var4 = (ItemBuilder)var3.next();
         var2.add(var4.toString());
      }

      this.config.set("icon.item", this.item.toString());
      this.config.set("items", var2);
      this.config.save();
   }
}
