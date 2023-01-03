package tools.gnzlz.template.instruction.base;

import tools.gnzlz.template.Template.Template;

import java.util.regex.MatchResult;

public class Utils {

    /**********************************
     * endInstruction
     **********************************/

    public static int end(InstructionSimple instruction, Template template) {
        if (instruction instanceof InstructionMultiple) {
            return ((InstructionMultiple) instruction).end().end;
        } else {
            return instruction.end + template.symbolEnd().length();
        }
    }

    /**********************************
     * endInstruction
     **********************************/

    public static int positionEndCharacterValid(String content, int startPos, int endPos) {
        int k = endPos - 1;
        while (k != startPos){
            char c = content.charAt(k);
            if(c != '\r' && c != '\n' && c != ' '){
                return k + 1;
            }
            k--;
            if(startPos > k){
                return startPos+1;
            }
        }
        return k+1;
    }

    public static int positionEndCharacterValid(String content,InstructionSimple instruction, int i){
        if(instruction instanceof InstructionMultiple){
            return Utils.positionStartCharacterValid(content, i, instruction.start);
        }
        return instruction.start;
    }

    public static int positionStartCharacterValid(String content, int startPos, int endPos) {
        int k = startPos;
        while (k != endPos){
            char c = content.charAt(k);
            if(c == '\r' || c == '\n' || c != ' '){
                if(c == '\r' || c == '\n'){
                    return k + 1;
                }
                return k;
            }
            k++;
        }
        return k;
    }
    public static boolean containsCharacterValid(String content, int startPos, int endPos) {
        int k = startPos;
        while (k != endPos){
            char c = content.charAt(k);
            if(c != '\r' && c != '\n' && c != ' '){
                return true;
            }
            k++;
        }
        return false;
    }

    /**********************************
     * endInstruction
     **********************************/

    public static boolean valid(Template template, MatchResult match, String type) {
        return match.group().equals(template.symbol() + type + template.symbolStart());
    }

    public static boolean valid2(Template template, MatchResult match, String type) {
        return match.group().equals(template.symbol() + type);
    }
}
