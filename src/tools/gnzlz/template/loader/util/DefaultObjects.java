package tools.gnzlz.template.loader.util;

import tools.gnzlz.template.Template;

import java.util.ArrayList;

public class DefaultObjects {

    /**
     * objectDefault
     * @param template t
     */
    public static void objectDefault(Template template){
        template.object("camelcase", o -> beginValidNumber(camelCaseClass(o.toString())));
        template.object("lowercamelcase", o -> beginValidNumber(camelCaseMethod(o.toString())));
        template.object("uppercase", o -> beginValidNumber(o.toString()).toUpperCase());
        template.object("lowercase", o -> beginValidNumber(o.toString()).toLowerCase());

        template.object("classname", o -> {
            if(o instanceof Class) {
                return ((Class<?>)o).getSimpleName();
            }else if(o instanceof Object){
                return o.getClass().getSimpleName();
            }
            return "";
        });

        template.object("package", o -> {
            if(o instanceof Class) {
                return ((Class<?>)o).getPackage().getName();
            }else if(o instanceof Object){
                return o.getClass().getPackage().getName();
            }
            return "";
        });

        template.object("empty", o -> {
            if(o instanceof String) {
                return ((String) o).isEmpty();
            } else if(o instanceof ArrayList){
                return ((ArrayList) o).isEmpty();
            }
            return true;
        });

        template.object("path", o -> {
            if (o instanceof String) {
                return ((String) o).replaceAll("[.]", "/");
            }
            return o;
        });
    }

    /**
     * beginValidNumber
     * @param s s
     */
    private static String beginValidNumber(String s) {
        boolean digit = false;
        if(s.equals("package") || s.equals("query")){
            return s + "1";
        }
        for (int i = 0 ; i < s.length(); i++){
            if(Character.isDigit(s.charAt(i))){
                digit = true;
                if(i == s.length()-1)
                    return "n"+s;
            }else{
                if(digit){
                    return s.substring(i) + s.substring(0,i);
                }
                break;
            }
        }
        return s;
    }

    /**
     * camelCaseClass
     * @param s s
     */
    private static String camelCaseClass(String s) {
        s = s.toLowerCase();
        String temp[] = s.split("_");
        String newString = "";
        for (String string : temp) {
            newString = newString + Character.toUpperCase(string.charAt(0)) + string.substring(1);
        }

        if(s.substring(s.length()-1).equalsIgnoreCase("_")){
            newString = newString + "_";
        }

        return newString;
    }

    /**
     * camelCaseMethod
     * @param s s
     */
    private static String camelCaseMethod(String s) {
        s = s.toLowerCase();
        String temp[] = s.split("_");
        String newString = "";
        for (int i = 0; i < temp.length; i++) {
            if(i==0){
                newString = newString + Character.toLowerCase(temp[i].charAt(0)) + temp[i].substring(1);
            } else {
                newString = newString + Character.toUpperCase(temp[i].charAt(0)) + temp[i].substring(1);
            }
        }

        if(s.substring(s.length()-1).equalsIgnoreCase("_")){
            newString = newString + "_";
        }

        return newString;
    }

}
