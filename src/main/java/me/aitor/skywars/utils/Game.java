package me.aitor.skywars.utils;

import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.utils.sky.SkyData;

public class Game extends SkyData {
   protected String name;
   protected String displayName;
   protected int alivePlayers;
   protected int maxPlayers;
   protected boolean loading;
   protected ArenaState state;

   public Game(String var1, String var2, int var3, boolean var4, ArenaState var5) {
      this.name = var1;
      this.displayName = var2;
      this.maxPlayers = var3;
      this.loading = var4;
      this.state = var5;
      if (var2 == null) {
         this.displayName = var1;
      }

   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
   }

   public int getAlivePlayers() {
      return this.alivePlayers;
   }

   public void setAlivePlayers(int var1) {
      this.alivePlayers = var1;
   }

   public int getMaxPlayers() {
      return this.maxPlayers;
   }

   public void setMaxPlayers(int var1) {
      this.maxPlayers = var1;
   }

   public boolean isLoading() {
      return this.loading;
   }

   public void setLoading(boolean var1) {
      this.loading = var1;
   }

   public ArenaState getState() {
      return this.state;
   }

   public void setState(ArenaState var1) {
      this.state = var1;
   }

   public boolean isDisabled() {
      return false;
   }

   public boolean isFull() {
      return this.alivePlayers >= this.maxPlayers;
   }
}
