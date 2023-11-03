package tools.gnzlz.template.template.type;

import java.util.ArrayList;

public class TemplatesManager {

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
    protected ArrayList<TemplatesBase> templates;

    /**
     * TemplatesManager
     * @param path p
     * @param out o
     */
    protected TemplatesManager(String path, String out){
        templates = new ArrayList<TemplatesBase>();
        this.path = path;
        this.out = out;
        this.internal = false;
    }

    /**
     * TemplatesManager
     * @param path p
     */
    protected TemplatesManager(String path){
       this(path, "");
    }

    /**
     * TemplatesManager
     */
    protected TemplatesManager(){
        this("", "");

    }

    /**
     * create
     */
    public static TemplatesManager create(){
        return new TemplatesManager();
    }

    /**
     * create
     * @param path p
     */
    public static TemplatesManager create(String path){
        return new TemplatesManager(path);
    }

    /**
     * create
     * @param path p
     * @param out o
     */
    public static TemplatesManager create(String path, String out){
        return new TemplatesManager(path, out);
    }

    /**
     * add
     * @param template name
     * @param addPathAndOut a
     */
    public TemplatesManager add(boolean addPathAndOut, TemplatesBase template){
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
    public TemplatesManager add(TemplatesBase template){
        this.add(true, template);
        return this;
    }

    /**
     * internal
     * @param internal internal
     */
    public TemplatesManager internal(boolean internal){
        this.internal = internal;
        return this;
    }

    /**
     * path
     * @param path path
     */
    public TemplatesManager path(String path){
        this.path = path;
        return this;
    }

    /**
     * out
     * @param out out
     */
    public TemplatesManager out(String out){
        this.out = out;
        return this;
    }

    /**
     * templates
     */
    public ArrayList<TemplatesBase> templates(){
        return templates;
    }

    /**
     * test
     */
    public TemplatesManager test(){
        templates.forEach(templates->{
            templates.templates();
        });
        return this;
    }
}