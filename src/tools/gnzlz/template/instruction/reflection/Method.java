package tools.gnzlz.template.instruction.reflection;

import tools.gnzlz.template.exceptions.TemplateObjectNotFoundException;

import java.lang.reflect.InvocationTargetException;

public class Method {

    /**
     * reflection
     * @param object d
     * @param level c
     * @param objects o
     */
    public static Object reflection(Object object, String level, Object ... objects) throws TemplateObjectNotFoundException {
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
            throw new TemplateObjectNotFoundException(name + " is null");
        }

        return object;
    }

    /**
     * reflection
     * @param object d
     * @param level c
     */
    public static Object reflection(Object object, String level){
        return "";
    }

    /**
     * isMethod
     * @param name d
     */
    public static boolean isMethod(String name){
        return name.contains("(") && name.contains(")");
    }

    /**
     * validParameters
     * @param method d
     * @param parameters c
     */
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

    /**
     * parameters
     * @param content c
     * @param objects o
     */
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
