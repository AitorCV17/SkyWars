package me.aitor.skywars.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class UTF8Control extends Control {
   @Override
   public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                   ClassLoader loader, boolean reload) {
      String bundleName = this.toBundleName(baseName, locale);
      ResourceBundle bundle = null;

      if ("java.class".equals(format)) {
         try {
            Class<?> clazz = loader.loadClass(bundleName);
            if (!ResourceBundle.class.isAssignableFrom(clazz)) {
               throw new ClassCastException(clazz.getName() + " no se puede convertir a ResourceBundle");
            }
            bundle = (ResourceBundle) clazz.getDeclaredConstructor().newInstance();
         } catch (ClassNotFoundException ignored) {
            // Silenciamos esta excepción, porque es normal si no hay clase
         } catch (Exception e) {
            e.printStackTrace(); // Otras excepciones sí las mostramos
         }
      } else if ("java.properties".equals(format)) {
         final String resourceName = this.toResourceName(bundleName, "properties");
         InputStream stream = null;

         try {
            stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
               public InputStream run() throws IOException {
                  InputStream is;
                  if (reload) {
                     URL url = loader.getResource(resourceName);
                     if (url != null) {
                        URLConnection connection = url.openConnection();
                        if (connection != null) {
                           connection.setUseCaches(false);
                           is = connection.getInputStream();
                        } else {
                           is = null;
                        }
                     } else {
                        is = null;
                     }
                  } else {
                     is = loader.getResourceAsStream(resourceName);
                  }
                  return is;
               }
            });
         } catch (PrivilegedActionException e) {
            e.printStackTrace();
         }

         if (stream != null) {
            try {
               bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
            } catch (IOException e) {
               e.printStackTrace();
            } finally {
               try {
                  stream.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Formato desconocido: " + format);
      }

      return bundle;
   }
}
