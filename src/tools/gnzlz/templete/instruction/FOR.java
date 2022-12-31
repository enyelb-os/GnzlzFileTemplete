package tools.gnzlz.templete.instruction;

import tools.gnzlz.templete.Template.Template;
import tools.gnzlz.templete.instruction.base.InstructionMultiple;
import tools.gnzlz.templete.instruction.base.InstructionSimple;
import tools.gnzlz.templete.instruction.base.Utils;
import tools.gnzlz.templete.reflection.Field;

public class FOR extends InstructionMultiple {

    /**
     * constructor
     */
    public FOR(int start, int end) {
        super(Type.FOR, start, end);
    }

    @Override
    public String execute(String content, Template template) {
        /**
         * get arguments to "FOR"
         */
        String split[] = content.substring(start + start(template), end).split("[|]");
        String nameVariableData = split[0].trim();
        String nameVariableIterable = split[1].trim();
        String nameVariableIndex = split[2].trim();
        /**
         * get data variable by name with reflection
         */
        Object reflection = Field.reflection(template.data(), nameVariableData);
        /**
         * result content
         */
        String resultContent = "";
        if(reflection instanceof Iterable){
            /**
             * Cast object iterable
             */
            Iterable<Object> iterable = (Iterable<Object>) reflection;
            int index = 0;
            /**
             * for iterable object in the template
             */
            for (Object object: iterable) {
                /**
                 * add objects to template
                 */
                template.object(nameVariableIterable, object);
                template.object(nameVariableIndex, index);
                index++;
                int i = Utils.positionStartCharacterValid(content, end + template.symbolEnd().length(), endInstruction.start);
                /**
                 * for internals instructions
                 */
                for (InstructionSimple instruction: internals) {
                    /**
                     * make instruction and prepare content
                     */
                    Object execute = instruction.execute(content, template);
                    /**
                     * is not empty
                     */

                    if(!execute.toString().isEmpty()){
                        /**
                         * select the content, for the first round "i" is equal to the beginning of the instruction
                         */
                        resultContent += content.substring(i, template.prepareStart(instruction));
                        resultContent += execute;
                    } else if(Utils.containsCharacterValid(content, i, template.prepareStart(instruction))) {
                        /**
                         * select the content, for the first round "i" is equal to the beginning of the instruction
                         */
                        resultContent += content.substring(i, Utils.positionEndCharacterValid(content, i, instruction.start));
                    }
                    /**
                     * assign "i" for the next round
                     */
                    i = Utils.end(instruction, template);

                }
                /**
                 * add objects to template
                 */
                resultContent = resultContent + content.substring(i,Utils.positionEndCharacterValid(content, i, endInstruction.start))
                   .replaceFirst("(?s)[\r\n](?!.*?[\r\n])", "").trim();
            }
        }
        return resultContent.replaceAll("[\r\n]    ", "\n");
    }
}
