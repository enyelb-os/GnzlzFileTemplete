package tools.gnzlz.template.reflection;


import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

public class Field {

    /**********************************
     * Reflection
     **********************************/

    public static Object reflection(Hashtable<String, Object> data, String content, Object ... objects){
        Object object = null;
        String levels[] = content.split("[.]");
        int k = 0;
        String current = "";
        for(int i = 0; i < levels.length ; i++) {
            current += levels[i];
            Object objectCurrent = data.get(current);
            if(objectCurrent != null){
                object = objectCurrent;
                k = i + 1;
            }
            current+=".";
        }
        for(int i = k; i < levels.length ; i++) {
            if(i == 0){
                object = data.get(levels[i]);
                if(object == null){
                    throw new RuntimeException(levels[i] + " is null");
                }
            } else if(Method.isMathod(levels[i])){
                object = Method.reflection(object, levels[i], objects);
            } else {
                object = Field.reflection(object, levels[i], data, content);
            }
        }
        return object;
    }

    /**********************************
     * Reflection
     **********************************/

    private static Object reflection(Object object, String name, Hashtable<String, Object> data, String name_complete){
        Class<?> c = object instanceof Class ? ((Class)object) : object.getClass();
        Object current = object;
        try {
            object = c.getField(name).get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            try {
                for (java.lang.reflect.Method method : c.getDeclaredMethods()){
                    if(method.getName().equals(name)){
                        return method.invoke(object);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {}

            Object objectCustom = data.get(name);
            if(objectCustom != null && objectCustom instanceof ObjectCustom){
                return ((ObjectCustom) objectCustom).run(current);
            } else {
                throw new RuntimeException(name_complete + " is null [" + name + "]");
            }

        }
        return object;
    }
}
