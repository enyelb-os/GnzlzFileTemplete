package tools.gnzlz.template;

import tools.gnzlz.system.ansi.Color;
import tools.gnzlz.system.io.SystemIO;
import tools.gnzlz.template.exceptions.TemplateObjectNotFoundException;
import tools.gnzlz.template.loader.data.ObjectDataLoad;
import tools.gnzlz.template.loader.data.ObjectFileLoad;
import tools.gnzlz.template.loader.data.ObjectFunctionAddObjects;
import tools.gnzlz.template.loader.data.ObjectTemplate;
import tools.gnzlz.template.loader.functional.FunctionAddObjects;
import tools.gnzlz.template.loader.util.DefaultObjects;
import tools.gnzlz.template.loader.controller.FileController;

import java.util.ArrayList;

public class TemplateLoader<T extends TemplateLoader<?>> {

    /**
     * out
     */
    protected String out;

    /**
     * path
     */
    protected String path;

    /**
     * internal
     */
    protected boolean internal = false;

    /**
     * templates
     */
    private final ArrayList<ObjectTemplate> templates;

    /**
     * functionsAddObjects
     */
    private final ArrayList<ObjectFunctionAddObjects<Object>> functionsAddObjects;

    /**
     * files
     */
    private final ArrayList<ObjectFileLoad> files;

    /**
     * objects
     */
    private final ArrayList<ObjectDataLoad> objects;

    /**
     * TemplateLoader
     * @param path p
     * @param out o
     */
    protected TemplateLoader(String path, String out){
        this.templates = new ArrayList<>();
        this.functionsAddObjects = new ArrayList<>();
        this.files = new ArrayList<>();
        this.objects = new ArrayList<>();
        this.path = path;
        this.out = out;
    }

    /**
     * TemplateLoader
     * @param path p
     */
    protected TemplateLoader(String path){
       this(path, "");
    }

    /**
     * TemplateLoader
     */
    protected TemplateLoader(){
        this("", "");
    }

    /**
     * create
     */
    public static TemplateLoader<?> create(){
        return new TemplateLoader<>();
    }

    /**
     * create
     * @param path p
     */
    public static TemplateLoader<?> create(String path){
        return new TemplateLoader<>(path);
    }

    /**
     * create
     * @param path p
     * @param out o
     */
    public static TemplateLoader<?> create(String path, String out){
        return new TemplateLoader<>(path, out);
    }

    /**
     * objects
     * @param <Type> t
     * @param type t
     * @param functionAddObjects t
     */
    public <Type> void addObjects(Class<Type> type, FunctionAddObjects<Type> functionAddObjects) {
        functionsAddObjects.add((ObjectFunctionAddObjects<Object>) new ObjectFunctionAddObjects<>(functionAddObjects, type));
    }

    /**
     * objects
     * @param objects o
     */
    public void addObjects(Object ... objects) {
        if (objects != null) {
            for (Object o: objects) {
                for (ObjectFunctionAddObjects<Object> objectFunctionAddObjects: functionsAddObjects) {
                    if (objectFunctionAddObjects.type() == o.getClass()) {
                        for (ObjectTemplate objectTemplate: templates()) {
                            objectFunctionAddObjects.functionAddObjects().process(objectTemplate.template(), o);
                        }
                    }
                }
            }
        }
    }

    /**
     * object
     * @param name name
     * @param object o
     */
    public T object(String name, Object object) {
        return this.object("", name, object);
    }

    /**
     * objects
     * @param templates t
     * @param name name
     * @param object o
     */
    public T object(String templates, String name, Object object) {
        objects.add(new ObjectDataLoad(templates, name, object));
        for (var template : this.templates) {
            if (FileController.containsKey(templates, template.name())) {
                template.template().object(name, object);
            }
        }
        return (T) this;
    }

    /**
     * objects
     * @param names names
     * @param objects o
     */
    public T process(String names, Object ... objects) {
        if (names == null || names.isEmpty()) {
            process(objects);
        } else {
            for (ObjectTemplate o: templates()) {
                if (FileController.containsKey(names, o.name())) {
                    try {
                        this.addObjects(objects);
                        FileController.create(out, o.template());
                    } catch (TemplateObjectNotFoundException e) {
                        SystemIO.OUT.println(Color.RED.print(o.name() + ": " + e.getMessage()));
                        break;
                    }
                }
            }
        }
        return (T) this;
    }

    /**
     * objects
     * @param objects o
     */
    public T process(Object ... objects) {
        for (ObjectTemplate o: templates()) {
            try {
                this.addObjects(objects);
                FileController.create(out, o.template());
            } catch (TemplateObjectNotFoundException e) {
                SystemIO.OUT.println(Color.RED.print(o.name() + ": " + e.getMessage()));
                break;
            }
        }
        return (T) this;
    }

    /**
     * existsUrlTemplates
     * @param url url
     */
    private boolean existsUrlTemplates(String url){
        for (ObjectTemplate template: templates) {
            if (template.url().equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * existsUrl
     * @param url url
     */
    private boolean existsUrlFiles(String url){
        for (ObjectFileLoad file: files) {
            if (file.url().equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * load
     * @param name name
     * @param url url
     * @param internal internal
     */
    public T load(String name, String url, boolean internal) {
        if (!existsUrlFiles(url)) {
            files.add(new ObjectFileLoad(name, url, internal));
        }
        return (T) this;
    }

    /**
     * load
     * @param name name
     * @param url url
     */
    public T load(String name, String url) {
        return load(name, url, false);
    }

    /**
     * template
     * @param template template
     */
    protected TemplateLoader<?> template(ObjectTemplate template) {
        if (template != null && !existsUrlTemplates(template.url())) {
            defaultObjects(template.template());
            for (ObjectDataLoad data: objects) {
                if (FileController.containsKey(data.templates(), template.name())) {
                    template.template().object(data.name(), data.value());
                }
            }
            templates.add(template);
        }
        return this;
    }

    /**
     * templates
     */
    public ArrayList<ObjectTemplate> templates() {
        if (!files.isEmpty()) {
            for (ObjectFileLoad file : files) {
                ObjectTemplate template = FileController.load(file.name(), path, file.url(), file.internal() || internal);
                template(template);
            }
            files.clear();
        }
        return templates;
    }

    /**
     * defaultObjects
     * @param template template
     */
    protected void defaultObjects(Template template) {
        DefaultObjects.objectDefault(template);
    }
}
