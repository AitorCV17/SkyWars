package me.aitor.skywars.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;

public final class ReflectionUtils {
   private ReflectionUtils() {}

   public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) throws NoSuchMethodException {
      Class<?>[] primitiveParams = DataType.getPrimitive(params);
      Constructor<?>[] constructors = clazz.getConstructors();
      for (Constructor<?> constructor : constructors) {
         if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveParams)) {
            return constructor;
         }
      }
      throw new NoSuchMethodException("No existe ningún constructor en esta clase con los tipos de parámetros especificados");
   }

   public static Constructor<?> getConstructor(String className, PackageType pkg, Class<?>... params) throws ClassNotFoundException, NoSuchMethodException {
      return getConstructor(pkg.getClass(className), params);
   }

   public static Object instantiateObject(Class<?> clazz, Object... args) {
      try {
         return getConstructor(clazz, DataType.getPrimitive(args)).newInstance(args);
      } catch (Exception e) {
         throw new RuntimeException("Error al instanciar el objeto", e);
      }
   }

   public static Object instantiateObject(String className, PackageType pkg, Object... args) {
      try {
         return instantiateObject(pkg.getClass(className), args);
      } catch (Exception e) {
         throw new RuntimeException("Error al instanciar el objeto", e);
      }
   }

   public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) throws NoSuchMethodException {
      Class<?>[] primitiveParams = DataType.getPrimitive(params);
      for (Method method : clazz.getMethods()) {
         if (method.getName().equals(methodName)
                 && DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveParams)) {
            return method;
         }
      }
      throw new NoSuchMethodException("No existe ningún método en esta clase con el nombre y los tipos de parámetros especificados");
   }

   public static Method getMethod(String className, PackageType pkg, String methodName, Class<?>... params) throws ClassNotFoundException, NoSuchMethodException {
      return getMethod(pkg.getClass(className), methodName, params);
   }

   public static Object invokeMethod(Object instance, String methodName, Object... args) {
      try {
         return getMethod(instance.getClass(), methodName, DataType.getPrimitive(args)).invoke(instance, args);
      } catch (Exception e) {
         throw new RuntimeException("Error al invocar el método", e);
      }
   }

   public static Object invokeMethod(Object instance, Class<?> clazz, String methodName, Object... args) {
      try {
         return getMethod(clazz, methodName, DataType.getPrimitive(args)).invoke(instance, args);
      } catch (Exception e) {
         throw new RuntimeException("Error al invocar el método", e);
      }
   }

   public static Object invokeMethod(Object instance, String className, PackageType pkg, String methodName, Object... args) {
      try {
         return invokeMethod(instance, pkg.getClass(className), methodName, args);
      } catch (Exception e) {
         throw new RuntimeException("Error al invocar el método", e);
      }
   }

   public static Field getField(Class<?> clazz, boolean declared, String name) {
      try {
         Field field = declared ? clazz.getDeclaredField(name) : clazz.getField(name);
         field.setAccessible(true);
         return field;
      } catch (Exception e) {
         throw new RuntimeException("Error accediendo al campo", e);
      }
   }

   public static Field getField(String className, PackageType pkg, boolean declared, String fieldName) {
      try {
         return getField(pkg.getClass(className), declared, fieldName);
      } catch (Exception e) {
         throw new RuntimeException("Error accediendo al campo", e);
      }
   }

   public static Object getValue(Object instance, Class<?> clazz, boolean declared, String fieldName) {
      try {
         return getField(clazz, declared, fieldName).get(instance);
      } catch (Exception e) {
         throw new RuntimeException("Error al obtener el valor del campo", e);
      }
   }

   public static Object getValue(Object instance, String className, PackageType pkg, boolean declared, String fieldName) {
      try {
         return getValue(instance, pkg.getClass(className), declared, fieldName);
      } catch (Exception e) {
         throw new RuntimeException("Error al obtener el valor del campo", e);
      }
   }

   public static Object getValue(Object instance, boolean declared, String fieldName) {
      return getValue(instance, instance.getClass(), declared, fieldName);
   }

   public static void setValue(Object instance, Class<?> clazz, boolean declared, String fieldName, Object value) {
      try {
         getField(clazz, declared, fieldName).set(instance, value);
      } catch (Exception e) {
         throw new RuntimeException("Error al establecer el valor del campo", e);
      }
   }

   public static void setValue(Object instance, String className, PackageType pkg, boolean declared, String fieldName, Object value) {
      try {
         setValue(instance, pkg.getClass(className), declared, fieldName, value);
      } catch (Exception e) {
         throw new RuntimeException("Error al establecer el valor del campo", e);
      }
   }

   public static void setValue(Object instance, boolean declared, String fieldName, Object value) {
      setValue(instance, instance.getClass(), declared, fieldName, value);
   }

   public enum DataType {
      BYTE(Byte.TYPE, Byte.class),
      SHORT(Short.TYPE, Short.class),
      INTEGER(Integer.TYPE, Integer.class),
      LONG(Long.TYPE, Long.class),
      CHARACTER(Character.TYPE, Character.class),
      FLOAT(Float.TYPE, Float.class),
      DOUBLE(Double.TYPE, Double.class),
      BOOLEAN(Boolean.TYPE, Boolean.class);

      private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<>();
      private final Class<?> primitive;
      private final Class<?> reference;

      DataType(Class<?> primitive, Class<?> reference) {
         this.primitive = primitive;
         this.reference = reference;
      }

      public Class<?> getPrimitive() {
         return primitive;
      }

      public Class<?> getReference() {
         return reference;
      }

      public static DataType fromClass(Class<?> clazz) {
         return CLASS_MAP.get(clazz);
      }

      public static Class<?> getPrimitive(Class<?> clazz) {
         DataType type = fromClass(clazz);
         return type == null ? clazz : type.getPrimitive();
      }

      public static Class<?> getReference(Class<?> clazz) {
         DataType type = fromClass(clazz);
         return type == null ? clazz : type.getReference();
      }

      public static Class<?>[] getPrimitive(Class<?>[] classes) {
         Class<?>[] result = new Class[classes.length];
         for (int i = 0; i < classes.length; i++) {
            result[i] = getPrimitive(classes[i]);
         }
         return result;
      }

      public static Class<?>[] getReference(Class<?>[] classes) {
         Class<?>[] result = new Class[classes.length];
         for (int i = 0; i < classes.length; i++) {
            result[i] = getReference(classes[i]);
         }
         return result;
      }

      public static Class<?>[] getPrimitive(Object[] objects) {
         Class<?>[] result = new Class[objects.length];
         for (int i = 0; i < objects.length; i++) {
            result[i] = getPrimitive(objects[i].getClass());
         }
         return result;
      }

      public static Class<?>[] getReference(Object[] objects) {
         Class<?>[] result = new Class[objects.length];
         for (int i = 0; i < objects.length; i++) {
            result[i] = getReference(objects[i].getClass());
         }
         return result;
      }

      public static boolean compare(Class<?>[] a, Class<?>[] b) {
         if (a.length != b.length) return false;
         for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i]) && !a[i].isAssignableFrom(b[i])) return false;
         }
         return true;
      }

      static {
         for (DataType type : values()) {
            CLASS_MAP.put(type.primitive, type);
            CLASS_MAP.put(type.reference, type);
         }
      }
   }

   public enum PackageType {
      MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
      CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),
      CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"),
      CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT, "chunkio"),
      CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"),
      CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT, "conversations"),
      CRAFTBUKKIT_ENCHANTMENS(CRAFTBUKKIT, "enchantments"),
      CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"),
      CRAFTBUKKIT_EVENT(CRAFTBUKKIT, "event"),
      CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT, "generator"),
      CRAFTBUKKIT_HELP(CRAFTBUKKIT, "help"),
      CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT, "inventory"),
      CRAFTBUKKIT_MAP(CRAFTBUKKIT, "map"),
      CRAFTBUKKIT_METADATA(CRAFTBUKKIT, "metadata"),
      CRAFTBUKKIT_POTION(CRAFTBUKKIT, "potion"),
      CRAFTBUKKIT_PROJECTILES(CRAFTBUKKIT, "projectiles"),
      CRAFTBUKKIT_SCHEDULER(CRAFTBUKKIT, "scheduler"),
      CRAFTBUKKIT_SCOREBOARD(CRAFTBUKKIT, "scoreboard"),
      CRAFTBUKKIT_UPDATER(CRAFTBUKKIT, "updater"),
      CRAFTBUKKIT_UTIL(CRAFTBUKKIT, "util");

      private final String path;

      PackageType(String path) {
         this.path = path;
      }

      PackageType(PackageType parent, String sub) {
         this(parent + "." + sub);
      }

      public String getPath() {
         return path;
      }

      public Class<?> getClass(String name) throws ClassNotFoundException {
         return Class.forName(this + "." + name);
      }

      @Override
      public String toString() {
         return path;
      }

      public static String getServerVersion() {
         return Bukkit.getServer().getClass().getPackage().getName().substring(23);
      }
   }
}
