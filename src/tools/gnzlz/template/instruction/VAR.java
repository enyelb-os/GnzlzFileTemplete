package tools.gnzlz.template.instruction;

import tools.gnzlz.template.template.Template;
import tools.gnzlz.template.instruction.base.InstructionSimple;
import tools.gnzlz.template.instruction.base.Utils;
import tools.gnzlz.template.reflection.Field;

public class VAR extends InstructionSimple {

    /**
     * constructor
     */
    public VAR(int start, int end) {
        super(Type.VAR, start, end);
    }

    @Override
    public Object execute(String content, Template template) {
        /**
         * vars
         */
        String resultContent = "";
        Object[] objects = new Object[arguments.size()];
        int i = start + start(template);

        /**
         * make arguments and prepare content
         */
        for (int k = 0; k < arguments.size() ; k++) {
            /**
             * select the content, for the first round "i" is equal to the beginning of the instruction
             * index the arguments
             */
            resultContent = resultContent + content.substring(i, arguments.get(k).start) + k;
            /**
             * make arguments and prepare content
             */
            objects[k] = arguments.get(k).execute(content, template);
            /**
             * assign "i" for the next round
             */
            i = Utils.end(arguments.get(k), template);
        }
        /**
         * rest of the content
         */
        resultContent = resultContent + content.substring(i, end);
        /**
         * run instuccion
         */
        //System.out.println(content.substring(start, end + template.symbolEnd().length()) + " : "+Field.reflection(template.data(), resultContent, objects));
        return Field.reflection(template.data(), resultContent, objects);
    }
}
