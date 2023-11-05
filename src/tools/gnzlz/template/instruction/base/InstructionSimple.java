package tools.gnzlz.template.instruction.base;

import tools.gnzlz.template.instruction.Type;
import tools.gnzlz.template.template.Template;
import tools.gnzlz.template.exceptions.TemplateObjectNotFoundException;

import java.util.ArrayList;

public abstract class InstructionSimple{

    /**
     * arguments
     */
    protected ArrayList<InstructionSimple> arguments;

    /**
     * type
     */
    public final Type type;

    /**
     * start
     */
    public int start;

    /**
     * end
     */
    public int end;

    /**
     * InstructionSimple
     * @param type t
     * @param start s
     * @param end e
     */
    public InstructionSimple(Type type, int start, int end) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.arguments = new ArrayList<InstructionSimple>();
    }

    /**
     * start
     * @param template t
     */
    protected int start(Template template) {
        if(type == Type.ELSE){
            return template.symbol().length() + type.toString().length();
        }else{
            return template.symbol().length() + type.toString().length() + template.symbolStart().length();
        }
    }

    /**
     * addArgument
     * @param instruction i
     */
    public void addArgument(InstructionSimple instruction) {
        arguments.add(instruction);
    }

    /**
     * execute
     * @param content c
     * @param template t
     */
    public abstract Object execute(String content, Template template) throws TemplateObjectNotFoundException;
}
