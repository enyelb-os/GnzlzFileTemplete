package tools.gnzlz.templete;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

public class Decoder {

    public static String decoder(Hashtable<String, Object> data, String content, String start, String end){
        parse(data,content,start,end);
        return content;
    }

    private static void parse(Hashtable<String, Object> data, String content, String start, String end){
        int i = content.indexOf(start);
        int f = 0;
        while (i>-1){
            f = content.indexOf(end, i + 1);
            if(f > i){
                String key = content.substring(i + start.length(), f).trim();
            }
            i = content.indexOf(start, f + 1);
        }
    }

    private static Object reflection(Hashtable<String, Object> data, String content){
        String levels[] = content.split("[.]");
        Object object = null;
        for(int i = 0; i < levels.length ; i++) {
            if(i == 0){
                object = data.get(levels[i]);
                if(object == null){
                    throw new RuntimeException(levels[i] + " is null");
                }
            } else if(levels[i].contains("(")){
                try {
                    for (Method method : object.getClass().getMethods()){
                        if(method.getName().equals(levels[i].substring(0,levels[i].indexOf("("))) && method.getParameterCount() == 0){
                            object = method.invoke(object);
                        }
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(levels[i] + " is null");
                }
            } else {
                try {
                    object = object.getClass().getField(levels[i]).get(object);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(levels[i] + " is null");
                }
            }
        }
        return object;
    }



}
