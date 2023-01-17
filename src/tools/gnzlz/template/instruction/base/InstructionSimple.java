package tools.gnzlz.template.instruction.base;

import tools.gnzlz.template.instruction.Type;
import tools.gnzlz.template.template.Template;

import java.util.ArrayList;

public abstract class InstructionSimple{

    protected ArrayList<InstructionSimple> arguments;
    public final Type type;
    public int start;
    public int end;

    public InstructionSimple(Type type, int start, int end) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.arguments = new ArrayList<InstructionSimple>();
    }

    protected int start(Template template) {
        if(type == Type.ELSE){
            return template.symbol().length() + type.toString().length();
        }else{
            return template.symbol().length() + type.toString().length() + template.symbolStart().length();
        }
    }

    public void addArgument(InstructionSimple instruction) {
        arguments.add(instruction);
    }

    public abstract Object execute(String content, Template template);

}
