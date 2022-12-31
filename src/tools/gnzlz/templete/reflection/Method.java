package tools.gnzlz.templete.reflection;

import java.lang.reflect.InvocationTargetException;

public class Method {

    /**********************************
     * reflection
     **********************************/

    public static Object reflection(Object object, String level, Object ... objects){
        int start = level.indexOf("(");
        int end = level.indexOf(")");
        String name = level.substring(0, start);
        String content = level.substring(start+1, end);
        try {
            Class<?> c = object instanceof Class ? ((Class)object) : object.getClass();
            for (java.lang.reflect.Method method : c.getDeclaredMethods()){
                Object[] parameters = parameters(content, objects);
                if(method.getName().equals(name) && validParameters(method, parameters)){
                    return method.invoke(object, parameters);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(name + " is null");
        }

        return object;
    }

    /**********************************
     * isMethod
     **********************************/

    public static boolean isMathod(String name){
        return name.contains("(") && name.contains(")");
    }

    /**********************************
     * validParameters
     **********************************/

    private static boolean validParameters(java.lang.reflect.Method method, Object[] parameters){
        if(parameters.length == method.getParameterCount()){
            for (int i = 0; i < parameters.length ; i++) {
                Class<?> c = parameters[i] instanceof Class ? ((Class)parameters[i]) : parameters[i].getClass();
                if(method.getParameterTypes()[i] != c){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**********************************
     * parameters
     **********************************/

    private static Object[] parameters(String content, Object ... objects){
        Object[] parameters = new Object[0];
        if(!content.trim().isEmpty()){
            String[] arg = content.trim().split(",");
            parameters = new Object[arg.length];
            for (int i = 0; i < arg.length ; i++) {
                parameters[i] = objects[Integer.parseInt(arg[i].trim())];
            }
        }
        return parameters;
    }
}
