package me.aitor.skywars.events;

import me.aitor.skywars.events.enums.SkySignUpdateCause;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkySignUpdateEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private String arena;
   private SkySignUpdateCause cause;

   public SkySignUpdateEvent(String var1, SkySignUpdateCause var2) {
      this.arena = var1;
      this.cause = var2;
   }

   public String getArena() {
      return this.arena;
   }

   public SkySignUpdateCause getCause() {
      return this.cause;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
