package tools.gnzlz.template.instruction;

import tools.gnzlz.template.instruction.base.InstructionMultiple;
import tools.gnzlz.template.instruction.base.InstructionSimple;
import tools.gnzlz.template.instruction.base.Utils;
import tools.gnzlz.template.Template;
import tools.gnzlz.template.instruction.expression.Resolver;
import tools.gnzlz.template.exceptions.TemplateObjectNotFoundException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IF extends InstructionMultiple {

    /**
     * IF
     * @param start s
     * @param end e
     */
    public static IF IF(int start, int end) {
        return new IF(Type.IF, start, end);
    }

    /**
     * ELSEIF
     * @param start s
     * @param end e
     */
    public static IF ELSEIF(int start, int end) {
        return new IF(Type.ELSEIF, start, end);
    }

    /**
     * ELSE
     * @param start s
     * @param end e
     */
    public static IF ELSE(int start, int end) {
        return new IF(Type.ELSE, start, end);
    }

    /**
     * IF
     * @param start s
     * @param end e
     */
    public IF(int start, int end) {
        this(Type.IF, start, end);
    }

    /**
     * IF
     * @param type t
     * @param start s
     * @param end e
     */
    private IF(Type type, int start, int end) {
        super(type, start, end);
    }

    /**
     * execute
     * @param content c
     * @param template t
     */
    @Override
    public Object execute(String content, Template template) throws TemplateObjectNotFoundException {
        String contentIf = content.substring(start + start(template), end);
        boolean[] results = prepareData(contentIf, content, template);
        if(type == Type.ELSE || validateResults(results, contentIf)){
            String resultContent = "";
            int i = Utils.positionStartCharacterValid(content, end + template.symbolEnd().length(), endInstruction.start);
            for (InstructionSimple instruction: internals) {
                Object execute = instruction.execute(content, template);
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
                i = Utils.end(instruction, template);

            }
            resultContent = resultContent + content.substring(i, Utils.positionEndCharacterValid(content, i, endInstruction.start));
            return resultContent
                    .replaceAll("[\r\n]    ", System.lineSeparator())
                    .replaceFirst("[\r\n]","")
                    .replaceFirst("(?s)[\r\n](?!.*?[\r\n])", "").trim();

        } else if(endInstruction instanceof InstructionMultiple){
            return endInstruction.execute(content, template);
        }
        return "";
    }

    /**
     * prepareData
     * @param contentIf c
     * @param content c
     * @param template t
     */
    private boolean[] prepareData(String contentIf, String content, Template template) throws TemplateObjectNotFoundException {
        if (contentIf == null || contentIf.isEmpty()) {
            return new boolean[1];
        }
        String[] split = contentIf.split("(AND|OR)");
        boolean[] results = new boolean[split.length];
        int var = 0;
        for (int i = 0; i < split.length; i++) {
            String[] operations = split[i].split("(==|!=|>|>=|<|<=)");
            Object[] values = new Object[operations.length];
            for (int j = 0; j<operations.length; j++) {
                if(operations[j].contains("VAR")){
                    InstructionSimple instruction = arguments.get(var);
                    String contentOperation = content.substring(instruction.start, instruction.end + template.symbolEnd().length());
                    values[j] = instruction.execute(content, template);
                    if(values[j] instanceof Boolean bool && operations[j].contains("!")){
                        values[j] = !(bool);
                    }else if(contentOperation.length() < operations[j].length()){
                        values[j] += operations[j].substring(contentOperation.length() + 1, operations[j].length());
                    }
                    var++;
                } else if(operations[j].trim().equalsIgnoreCase("true")){
                    values[j] = true;
                } else if(operations[j].trim().equalsIgnoreCase("false")){
                    values[j] = false;
                }else {
                    values[j] = operations[j].trim();
                }
            }
            if(operations.length > 1){
                results[i] = result(Resolver.number(values[0]), Resolver.number(values[1]), operator(split[i]));
            } else {
                if(values[0] instanceof Boolean bool){
                    results[i] = bool;
                } else if(values[0] instanceof String str) {
                    results[i] = str.trim().equalsIgnoreCase("true");
                }
            }
        }
        return results;
    }

    /**
     * operator
     * @param value v
     */
    private String operator(String value){
        if(value.contains("!=")){
            return "!=";
        } else if(value.contains(">")){
            return ">";
        } else if(value.contains(">=")){
            return ">=";
        } else if(value.contains("<")){
            return "<";
        } else if(value.contains("<=")){
            return "<=";
        } else{
            return "==";
        }
    }

    /**
     * result
     * @param value1 v
     * @param value2 v
     * @param operator o
     */
    private boolean result(Object value1, Object value2, String operator){
        if(operator.equals("!=")){
            if(value1 instanceof Number || value2 instanceof Number || value1 instanceof String || value2 instanceof String){
                return !value1.toString().equals(value2.toString());
            } else {
                return !value1.equals(value2);
            }
        } else if(operator.equals(">")){
            if(value1 instanceof Number || value2 instanceof Number){
                return ((Number)value1).doubleValue() > ((Number)value2).doubleValue();
            } else {
                return false;
            }
        } else if(operator.equals(">=")){
            if(value1 instanceof Number || value2 instanceof Number){
                return ((Number)value1).doubleValue() >= ((Number)value2).doubleValue();
            } else {
                return false;
            }
        } else if(operator.equals("<")){
            if(value1 instanceof Number || value2 instanceof Number){
                return ((Number)value1).doubleValue() < ((Number)value2).doubleValue();
            } else {
                return false;
            }
        } else if(operator.equals("<=")){
            if(value1 instanceof Number || value2 instanceof Number){
                return ((Number)value1).doubleValue() <= ((Number)value2).doubleValue();
            } else {
                return false;
            }
        } else {
            if(value1 instanceof Number || value2 instanceof Number || value1 instanceof String || value2 instanceof String){
                return value1.toString().equals(value2.toString());
            } else {
                return value1.equals(value2);
            }
        }
    }

    /**
     * validateResults
     * @param results r
     * @param expression e
     */
    private boolean validateResults(boolean[] results, String expression){
        Pattern pattern = Pattern.compile("(AND|OR)");
        Matcher matcher = pattern.matcher(expression);
        ArrayList<String> types = new ArrayList<>();
        matcher.results().forEach(match1 -> types.add(match1.group()));

        for (int i = 0 ; i < types.size(); i++) {
            if(types.get(i).equals("AND")){
                if(!results[i] || !results[i+1]){
                    return false;
                }
            }else{
                if(!results[i] && !results[i+1]){
                    return false;
                }
            }
        }
        if(results.length == 1){
            return results[0];
        }
        return true;
    }
}
