package tools.gnzlz.templete.instruction.base;

import tools.gnzlz.templete.Template.Template;

import java.awt.event.KeyEvent;
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

    /**********************************
     * selectContent
     **********************************/
    public static String selectContent(InstructionSimple instruction, Template template, String content, int start) {
        return selectContent(instruction,template, content,start, false);
    }

    public static String selectContent(InstructionSimple instruction, Template template, String content, int start, boolean isFirst) {
        String resultContent = "";
        Object execute = instruction.execute(content, template);
        int end = template.prepareStart(instruction);
        if(isFirst || execute.toString().isEmpty()){
            String contentTemp = content.substring(start, end);
            if(contentTemp.charAt(0) == '\r'){
                start = start + "\r".length();
            }
            if(contentTemp.length() > 1 && contentTemp.charAt(1) == '\n'){
                start = start + "\n".length();
            }
        }
        if(execute.toString().isEmpty()){
            end = template.prepareStartBefore(instruction);
            if(end < start){
                end = start;
            }
        }
        //System.out.println("Select");
        //System.out.println(content.substring(start, end));
        //System.out.println("Execute");
        resultContent = resultContent + content.substring(start, end);
        resultContent = resultContent + execute;
        //System.out.println(execute);
        return resultContent;
    }

    public static String selectContentFor(InstructionSimple instruction, Template template, String content, int start, boolean isFirst) {
        String resultContent = "";
        Object execute = instruction.execute(content, template);
        int end = template.prepareStart(instruction);
        /*if(isFirst || execute.toString().isEmpty()){
            String contentTemp = content.substring(start, end);
            if(contentTemp.charAt(0) == '\r'){
                start = start + "\r".length();
            }
            if(contentTemp.length() > 1 && contentTemp.charAt(1) == '\n'){
                start = start + "\n".length();
            }
        }*/
        //if(execute.toString().isEmpty()){
        //    end = template.prepareStartBefore(instruction);
        //    if(end < start){
        //        end = start;
        //    }
        //}
        //System.out.println("Select");
        //System.out.println(content.substring(start, end));
        //System.out.println("Execute");
        //if(isFirst){
            //resultContent = resultContent + content.substring(start, end).replaceFirst("    ", "");
        //}else{
            resultContent = resultContent + content.substring(start, end);
        //}

        resultContent = resultContent + execute;
        //System.out.println(execute);
        return resultContent;
    }
}
