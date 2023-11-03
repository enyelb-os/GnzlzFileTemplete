package tools.gnzlz.template.instruction;

import tools.gnzlz.template.instruction.base.InstructionSimple;
import tools.gnzlz.template.template.Template;

public class END extends InstructionSimple {

    /**
     * END
     * @param type t
     * @param start s
     * @param end e
     */
    public END(Type type , int start, int end){
        super(type, start,end);
    }

    /**
     * execute
     * @param content c
     * @param template t
     */
    @Override
    public Object execute(String content, Template template) {
        return "";
    }
}
