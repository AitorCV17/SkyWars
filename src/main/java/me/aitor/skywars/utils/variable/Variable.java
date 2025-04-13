package me.aitor.skywars.utils.variable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Variable {
   String[] vars();
}
