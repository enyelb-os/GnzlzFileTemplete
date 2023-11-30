package tools.gnzlz.template.loader.controller;

import tools.gnzlz.system.ansi.Color;
import tools.gnzlz.system.io.SystemIO;
import tools.gnzlz.template.Template;
import tools.gnzlz.template.exceptions.TemplateObjectNotFoundException;
import tools.gnzlz.template.file.File;
import tools.gnzlz.template.file.data.ObjectFile;
import tools.gnzlz.template.loader.data.ObjectTemplate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FileController {

    /**
     * load
     * @param name name
     * @param path path
     * @param url url
     * @param internal internal
     */
    public static ObjectTemplate load(String name, String path, String url, boolean internal){
        ObjectFile objectFile = File.read(path+url, internal);
        if (objectFile.exist()) {
            return new ObjectTemplate(name, path+url, Template.template(objectFile.content()));
        } else {
            SystemIO.OUT.println(Color.RED.print("Error" + objectFile.content()));
        }
        return null;
    }

    /**
     * create
     * @param template template
     */
    public static ObjectFile create(String out, Template template) throws TemplateObjectNotFoundException {
        String contentProps = template.build();
        AtomicInteger integer = new AtomicInteger(0);
        AtomicReference<String> url = new AtomicReference<String>("");
        AtomicReference<String> name = new AtomicReference<String>("");
        AtomicBoolean replace = new AtomicBoolean(false);
        AtomicBoolean create = new AtomicBoolean(true);

        contentProps.lines().forEach((line)->{
            if(line.contains("PATH:") || line.contains("NAME:") || line.contains("REPLACE:")){
                integer.set(integer.get() + line.length() + System.lineSeparator().length());
                String result[] = line.split(":");
                if(result.length > 1){
                    if(line.contains("PATH:")){
                        url.set(result[1].trim());
                    }else if(line.contains("NAME:")){
                        name.set(result[1].trim());
                    }else if(line.contains("REPLACE:")){
                        replace.set(result[1].trim().equalsIgnoreCase("true"));
                    }else if(line.contains("CREATE:")){
                        create.set(result[1].trim().equalsIgnoreCase("true"));
                    }
                }
            }
        });
        if(!url.get().isEmpty() && !name.get().isEmpty() && create.get()){
            return File.create(out + url.get(), name.get(), contentProps.substring(integer.get()), replace.get());
        }
        return new ObjectFile(false, "");
    }

    /**
     * existsFile
     * @param keys keys
     * @param str str
     */
    public static boolean containsKey(String keys, String str){
        if (keys == null || keys.isEmpty()) {
            return true;
        }
        String[] keysArray = keys.split(",");
        for (String key: keysArray) {
            if (str.equals(key)) {
                return true;
            }
        }
        return false;
    }
}
