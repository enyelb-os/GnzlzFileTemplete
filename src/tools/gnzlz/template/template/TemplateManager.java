package tools.gnzlz.template.template;

import java.util.ArrayList;

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
    protected ArrayList<TemplateLoader> templates;

    /**
     * TemplateManager
     * @param path p
     * @param out o
     */
    protected TemplateManager(String path, String out){
        templates = new ArrayList<TemplateLoader>();
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
    public TemplateManager add(boolean addPathAndOut, TemplateLoader template){
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
    public TemplateManager add(TemplateLoader template){
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
    public ArrayList<TemplateLoader> templates(){
        return templates;
    }

    /**
     * test
     */
    public TemplateManager test(){
        templates.forEach(templates->{
            templates.templates();
        });
        return this;
    }
}