package tools.gnzlz.templete.reflection;


import java.util.Hashtable;

public class Field {

    /**********************************
     * Reflection
     **********************************/

    public static Object reflection(Hashtable<String, Object> data, String content, Object ... objects){
        String levels[] = content.split("[.]");
        Object object = null;
        for(int i = 0; i < levels.length ; i++) {
            if(i == 0){
                object = data.get(levels[i]);
                if(object == null){
                    throw new RuntimeException(levels[i] + " is null");
                }
            } else if(Method.isMathod(levels[i])){
                object = Method.reflection(object, levels[i], objects);
            } else {
                object = Field.reflection(object, levels[i]);
            }
        }
        return object;
    }

    /**********************************
     * Reflection
     **********************************/

    private static Object reflection(Object object, String name){
        Class<?> c = object instanceof Class ? ((Class)object) : object.getClass();
        try {
            object = c.getField(name).get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(name + " is null");
        }
        return object;
    }
}
