package tools.gnzlz.template;

import tools.gnzlz.template.loader.controller.FileController;
import tools.gnzlz.template.loader.data.ObjectDataLoad;

import java.util.ArrayList;
import java.util.function.Predicate;

public class TemplateManager {

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
    protected boolean internal;

    /**
     * templates
     */
    protected ArrayList<TemplateLoader<?>> templates;

    /**
     * TemplateManager
     * @param path p
     * @param out o
     */
    protected TemplateManager(String path, String out){
        templates = new ArrayList<>();
        this.path = path;
        this.out = out;
        this.internal = false;
    }

    /**
     * TemplateManager
     * @param path p
     */
    protected TemplateManager(String path){
       this(path, "");
    }

    /**
     * TemplateManager
     */
    protected TemplateManager(){
        this("", "");

    }

    /**
     * create
     */
    public static TemplateManager create(){
        return new TemplateManager();
    }

    /**
     * create
     * @param path p
     */
    public static TemplateManager create(String path){
        return new TemplateManager(path);
    }

    /**
     * create
     * @param path p
     * @param out o
     */
    public static TemplateManager create(String path, String out){
        return new TemplateManager(path, out);
    }

    /**
     * add
     * @param template name
     * @param addPathAndOut a
     */
    public TemplateManager add(boolean addPathAndOut, TemplateLoader<?> template){
        if (addPathAndOut) {
            if (template.path.isEmpty() && !path.isEmpty()) {
                template.path = path;
            }
            if (template.out.isEmpty() && !out.isEmpty()) {
                template.out = out;
            }
        }
        template.internal = internal;
        templates.add(template);
        return this;
    }

    /**
     * add
     * @param template name
     */
    public TemplateManager add(TemplateLoader<?> template){
        this.add(true, template);
        return this;
    }

    /**
     * internal
     * @param internal internal
     */
    public TemplateManager internal(boolean internal){
        this.internal = internal;
        return this;
    }

    /**
     * path
     * @param path path
     */
    public TemplateManager path(String path){
        this.path = path;
        return this;
    }

    /**
     * out
     * @param out out
     */
    public TemplateManager out(String out){
        this.out = out;
        return this;
    }

    /**
     * templates
     */
    public ArrayList<TemplateLoader<?>> templates(){
        return templates;
    }

    /**
     * objects
     * @param templates t
     * @param name name
     * @param object o
     */
    public TemplateManager object(String templates, String name, Object object) {
        for (var template : this.templates) {
            template.object(templates, name, object);
        }
        return this;
    }

    /**
     * objects
     * @param name name
     * @param object o
     */
    public TemplateManager object(String name, Object object) {
        return this.object("", name, object);
    }

    /**
     * test
     */
    public TemplateManager test(){
        templates.forEach(TemplateLoader::templates);
        return this;
    }

    /**
     * processObjects
     * @param templates t
     * @param objects o
     */
    public static void processObjects(ArrayList<TemplateLoader<?>> templates, Object ... objects) {
        templates.forEach(template -> {
            template.addObjects(objects);
        });
    }

    /**
     * processTemplate
     * @param names n
     * @param templates t
     * @param predicate p
     * @param objects o
     */
    public static void processTemplate(StringBuilder names, boolean process, ArrayList<TemplateLoader<?>> templates, Predicate<? super TemplateLoader<?>> predicate, Object ... objects) {
        if (process) {
            templates.stream().filter(predicate).forEach(template -> {
                template.process(names.toString(), objects);
            });
        }
    }
}