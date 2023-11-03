package tools.gnzlz.template.template.type;

import tools.gnzlz.template.template.Template;
import tools.gnzlz.template.template.exceptions.TemplateObjectNotFoundException;
import tools.gnzlz.template.template.file.File;
import tools.gnzlz.template.template.file.data.ObjectFile;
import tools.gnzlz.template.template.type.data.ObjectDataLoad;
import tools.gnzlz.template.template.type.data.ObjectFileLoad;
import tools.gnzlz.template.template.type.data.ObjectFunctionAddObjects;
import tools.gnzlz.template.template.type.data.ObjectTemplate;
import tools.gnzlz.template.template.type.functional.FunctionAddObjects;
import tools.gnzlz.template.template.type.util.DefaultObjects;
import tools.gnzlz.template.template.type.util.FileController;

import java.util.ArrayList;

public class TemplatesBase<T extends TemplatesBase<?>> {

    /**
     * out
     */
    protected String out = "";

    /**
     * path
     */
    protected String path = "";

    /**
     * internal
     */
    protected boolean internal = false;

    /**
     * templates
     */
    private ArrayList<ObjectTemplate> templates;

    /**
     * functionsAddObjects
     */
    private ArrayList<ObjectFunctionAddObjects<Object>> functionsAddObjects;

    /**
     * files
     */
    private ArrayList<ObjectFileLoad> files;

    /**
     * objects
     */
    private ArrayList<ObjectDataLoad> objects;

    /**
     * TemplatesBase
     * @param path p
     * @param out o
     */
    protected TemplatesBase(String path, String out){
        this.templates = new ArrayList<ObjectTemplate>();
        this.functionsAddObjects = new ArrayList<ObjectFunctionAddObjects<Object>>();
        this.files = new ArrayList<ObjectFileLoad>();
        this.objects = new ArrayList<ObjectDataLoad>();
        this.path = path;
        this.out = out;
    }

    /**
     * TemplatesBase
     * @param path p
     */
    protected TemplatesBase(String path){
       this(path, "");
    }

    /**
     * TemplatesBase
     */
    protected TemplatesBase(){
        this("", "");
    }

    /**
     * create
     */
    public static TemplatesBase create(){
        return new TemplatesBase();
    }

    /**
     * create
     * @param path p
     */
    public static TemplatesBase create(String path){
        return new TemplatesBase(path);
    }

    /**
     * create
     * @param path p
     * @param out o
     */
    public static TemplatesBase create(String path, String out){
        return new TemplatesBase(path, out);
    }

    /**
     * objects
     * @param <Type> t
     * @param type t
     * @param functionAddObjects t
     */
    public <Type> T objects(Class<Type> type, FunctionAddObjects<Type> functionAddObjects) {
        functionsAddObjects.add((ObjectFunctionAddObjects<Object>) new ObjectFunctionAddObjects<Type>(functionAddObjects, type));
        return (T) this;
    }

    /**
     * objects
     * @param name name
     * @param object o
     */
    public T object(String name, Object object) {
        objects.add(new ObjectDataLoad("", name, object));
        return (T) this;
    }

    /**
     * objects
     * @param templates t
     * @param name name
     * @param object o
     */
    public T objectTemplate(String templates, String name, Object object) {
        objects.add(new ObjectDataLoad(templates, name, object));
        return (T) this;
    }

    /**
     * objects
     * @param objects o
     */
    public T objects(Object ... objects) {
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
                if (FileController.existsFileName(names, o.name())) {
                    try {
                        objects(objects);
                        FileController.create(out, o.template());
                    } catch (TemplateObjectNotFoundException e) {
                        System.out.println(o.name() + ": " + e.getMessage());
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
                objects(objects);
                FileController.create(out, o.template());
            } catch (TemplateObjectNotFoundException e) {
                System.out.println(o.name() + ": " + e.getMessage());
                break;
            }
        }
        return (T) this;
    }

    /**
     * load
     * @param name name
     * @param url url
     * @param internal internal
     */
    public T load(String name, String url, boolean internal) {
        this.files.add(new ObjectFileLoad(name, url, internal));
        return (T) this;
    }

    /**
     * load
     * @param name name
     * @param url url
     */
    public T load(String name, String url) {
        this.load(name, url, false);
        return (T) this;
    }

    /**
     * templates
     */
    public ArrayList<ObjectTemplate> templates() {
        if (!files.isEmpty()) {
            for (ObjectFileLoad file : files) {
                FileController.load(file.name(), path, file.url(), file.internal() ? true : internal, templates, objects, this::defaultObjects);
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
