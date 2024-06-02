package tools.gnzlz.template;

public class TemplatesNone extends TemplateLoader<TemplatesNone> {

    /**
     * TemplatesNone
     */
    protected TemplatesNone(){
        this("","");
    }

    /**
     * TemplatesNone
     * @param path p
     */
    protected TemplatesNone(String path){
        this(path, "");
    }

    /**
     * TemplatesNone
     * @param path p
     * @param out o
     */
    protected TemplatesNone(String path, String out){
        this(path, out, null);
    }

    /**
     * TemplatesNone
     * @param path p
     * @param out o
     * @param templateObjects t
     */
    protected TemplatesNone(String path, String out, TemplateObjects templateObjects){
        super(path, out, templateObjects);
    }

    /**
     * create
     */
    public static TemplatesNone create(){
        return new TemplatesNone();
    }

    /**
     * create
     * @param path p
     */
    public static TemplatesNone create(String path){
        return new TemplatesNone(path);
    }

    /**
     * create
     * @param path p
     * @param out o
     */
    public static TemplatesNone create(String path, String out){
        return new TemplatesNone(path, out);
    }

    /**
     * create
     * @param path p
     * @param out o
     * @param templateObjects t
     */
    public static TemplatesNone create(String path, String out, TemplateObjects templateObjects){
        return new TemplatesNone(path, out, templateObjects);
    }

    /**
     * create
     * @param templateObjects t
     */
    public static TemplatesNone create(TemplateObjects templateObjects){
        return new TemplatesNone("", "", templateObjects);
    }
}
