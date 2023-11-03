package tools.gnzlz.template.template.type.util;

import tools.gnzlz.template.template.Template;
import tools.gnzlz.template.template.exceptions.TemplateObjectNotFoundException;
import tools.gnzlz.template.template.file.File;
import tools.gnzlz.template.template.file.data.ObjectFile;
import tools.gnzlz.template.template.type.data.ObjectDataLoad;
import tools.gnzlz.template.template.type.data.ObjectTemplate;
import tools.gnzlz.template.template.type.functional.FunctionAddDefaultObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FileController {

    /**
     * load
     * @param name name
     * @param templates templates
     * @param path path
     * @param url url
     * @param internal internal
     */
    public static boolean load(String name, String path, String url, boolean internal, ArrayList<ObjectTemplate> templates, ArrayList<ObjectDataLoad> objects, FunctionAddDefaultObject functionAddDefaultObjects){
        if(!FileController.existsFile(url, templates)) {
            ObjectFile objectFile = File.read(path+url, internal);
            if (objectFile.exist()) {
                Template template = Template.template(objectFile.content());
                templates.add(new ObjectTemplate(name, url, template));
                functionAddDefaultObjects.process(template);
                for (ObjectDataLoad data: objects) {
                    if (data.templates().isEmpty() || FileController.existsFileName(data.templates(), name)) {
                        template.object(data.name(), data.value());
                    }
                }
                return true;
            } else {
                System.out.println("Error" + objectFile.content());
            }
        }
        return false;
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
     * @param url url
     * @param templates templates
     */
    private static boolean existsFile(String url, ArrayList<ObjectTemplate> templates){
        for (ObjectTemplate template: templates) {
            if (template.url().equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * existsFile
     * @param names names
     * @param namee namee
     */
    public static boolean existsFileName(String names, String namee){
        if (names == null || names.isEmpty()) {
            return false;
        }
        String[] namesTemp = names.split(",");
        for (String name: namesTemp) {
            if (namee.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
