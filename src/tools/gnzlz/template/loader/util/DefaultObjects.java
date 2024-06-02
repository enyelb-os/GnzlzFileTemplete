package tools.gnzlz.template.loader.util;

import tools.gnzlz.template.Template;
import tools.gnzlz.template.instruction.reflection.functional.FunctionObjectCustom;

import java.util.ArrayList;

public class DefaultObjects {

    /**
     * objectDefault
     * @param template t
     */
    public static void objectDefault(Template template){

        FunctionObjectCustom camelCase = o -> o != null && !o.toString().isEmpty() ? camelCase(o.toString()) : "";
        FunctionObjectCustom pascalCase = o -> o != null && !o.toString().isEmpty() ? pascalCase(o.toString()) : "";
        FunctionObjectCustom snakeCase = o -> o != null && !o.toString().isEmpty() ? snakeCase(o.toString()) : "";
        FunctionObjectCustom kebabCase = o -> o != null && !o.toString().isEmpty() ? kebabCase(o.toString()) : "";
        FunctionObjectCustom upperCase = o -> o.toString().toUpperCase();
        FunctionObjectCustom lowerCase = o -> o.toString().toLowerCase();
        FunctionObjectCustom className = o -> o instanceof Class<?> c ? c.getSimpleName() : (o != null ? o.getClass().getSimpleName() : "");
        FunctionObjectCustom packageName = o -> o instanceof Class<?> c ? c.getPackage().getName() : (o != null ? o.getClass().getPackage().getName() : "");
        FunctionObjectCustom pathName = o -> o instanceof String s ? s.replaceAll("[.]", "/") : o;

        template.object("empty", o -> o instanceof String s ? s.isEmpty() : (!(o instanceof ArrayList<?> a) || a.isEmpty()));
        template.object("path", pathName);
        template.object("camelcase", camelCase);
        template.object("pascalcase", pascalCase);
        template.object("snakecase", snakeCase);
        template.object("kebabcase", kebabCase);
        template.object("uppercase", upperCase);
        template.object("lowercase", lowerCase);
        template.object("classname", className);
        template.object("cc", camelCase);
        template.object("pc", pascalCase);
        template.object("sc", snakeCase);
        template.object("kc", kebabCase);
        template.object("uc", upperCase);
        template.object("lc", lowerCase);
        template.object("cn", className);
        template.object("package", packageName);

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
     * pascalCase
     * @param s s
     */
    private static String pascalCase(String s) {
        s = s.toLowerCase();
        String[] temp = s.split("[_\\-]");
        StringBuilder newString = new StringBuilder();
        for (String string : temp) {
            newString.append(Character.toUpperCase(string.charAt(0))).append(string.substring(1));
        }

        if(s.substring(s.length()-1).equalsIgnoreCase("_")){
            newString.append("_");
        }

        return newString.toString();
    }

    /**
     * camelCase
     * @param s s
     */
    private static String camelCase(String s) {
        s = s.toLowerCase();
        String[] temp = s.split("_");
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < temp.length; i++) {
            if(i==0){
                newString.append(Character.toLowerCase(temp[i].charAt(0))).append(temp[i].substring(1));
            } else {
                newString.append(Character.toUpperCase(temp[i].charAt(0))).append(temp[i].substring(1));
            }
        }

        if(s.substring(s.length()-1).equalsIgnoreCase("_")){
            newString.append("_");
        }

        return newString.toString();
    }

    /**
     * kebabCase
     * @param s s
     */
    private static String kebabCase(String s) {
        return s.toLowerCase().replaceAll(" |[.]|/|\\|_|-", "-");
    }

    /**
     * snakeCase
     * @param s s
     */
    private static String snakeCase(String s) {
        return s.toLowerCase().replaceAll(" |[.]|/|\\|_|-", "_");
    }

}
