package tools.gnzlz.template.reflection;


import java.util.Hashtable;

public class Field {

    /**********************************
     * Reflection
     **********************************/

    public static Object reflection(Hashtable<String, Object> data, String content, Object ... objects){
        Object object = data.get(content);
        if(object != null){
            return object;
        }
        String levels[] = content.split("[.]");
        for(int i = 0; i < levels.length ; i++) {
            if(i == 0){
                object = data.get(levels[i]);
                if(object == null){
                    throw new RuntimeException(levels[i] + " is null");
                }
            } else if(Method.isMathod(levels[i])){
                object = Method.reflection(object, levels[i], objects);
            } else {
                object = Field.reflection(object, levels[i], data);
            }
        }
        return object;
    }

    /**********************************
     * Reflection
     **********************************/

    private static Object reflection(Object object, String name, Hashtable<String, Object> data){
        Class<?> c = object instanceof Class ? ((Class)object) : object.getClass();
        try {
            object = c.getField(name).get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Object objectCustom = data.get(name);
            if(objectCustom != null && objectCustom instanceof ObjectCustom){
                return ((ObjectCustom) objectCustom).run(object);
            } else {
                throw new RuntimeException(name + " is null");
            }
        }
        return object;
    }
}
