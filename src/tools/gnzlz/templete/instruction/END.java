package tools.gnzlz.templete.instruction;

import tools.gnzlz.templete.Template.Template;
import tools.gnzlz.templete.instruction.base.InstructionSimple;

public class END extends InstructionSimple {

    public END(Type type , int start, int end){
        super(type, start,end);
    }

    @Override
    public Object execute(String content, Template template) {
        return "";
    }
}
