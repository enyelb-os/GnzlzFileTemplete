package tools.gnzlz.template.instruction;

import tools.gnzlz.template.Template.Template;
import tools.gnzlz.template.instruction.base.InstructionSimple;

public class END extends InstructionSimple {

    public END(Type type , int start, int end){
        super(type, start,end);
    }

    @Override
    public Object execute(String content, Template template) {
        return "";
    }
}
