package tools.gnzlz.template.Template;

import tools.gnzlz.template.instruction.*;
import tools.gnzlz.template.instruction.base.InstructionMultiple;
import tools.gnzlz.template.instruction.base.InstructionSimple;
import tools.gnzlz.template.instruction.base.Utils;
import tools.gnzlz.template.reflection.ObjectCustom;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {

    /**********************************
     * Vars
     **********************************/

    public static String path = "";

    protected Hashtable<String, Object> data;
    protected String content;
    protected String symbol;
    protected String symbolStart;
    protected String symbolEnd;
    protected Pattern pattern;
    protected ArrayList<InstructionSimple> instructions;
    protected ArrayList<Integer> lns;

    /**********************************
     * constructor
     **********************************/

    public Template(String content, String symbol, String start, String end) {

        this.content = content;
        this.symbol = symbol;
        this.symbolStart = start;
        this.symbolEnd = end;
        this.data = new Hashtable<String, Object>();
        this.pattern = Pattern.compile(regex());
        this.instructions = createInstructions();
        this.lns = new ArrayList<Integer>();

        lns.add(content.indexOf("\n"));
        while (lns.get(lns.size()-1) >= 0) {
            lns.add(content.indexOf("\n", lns.get(lns.size()-1) + 1));
        }
    }

    /**********************************
     * constructor
     **********************************/

    public Template(String content) {
        this(content,"@","{", "}");
    }

    /**********************************
     * build
     **********************************/

    public String build(){
        int i = 0;
        String resultContent = "";
        for (InstructionSimple instruction: instructions) {
            Object execute = instruction.execute(content, this);
            /**
             * is not empty
             */

            if(!execute.toString().isEmpty()){
                /**
                 * select the content, for the first round "i" is equal to the beginning of the instruction
                 */
                resultContent += content.substring(i, this.prepareStart(instruction));
                resultContent += execute;
            } else if(Utils.containsCharacterValid(content, i, this.prepareStart(instruction))) {
                /**
                 * select the content, for the first round "i" is equal to the beginning of the instruction
                 */
                resultContent += content.substring(i, Utils.positionEndCharacterValid(content, i, instruction.start));
            }
            i = Utils.end(instruction, this);
        }
        resultContent = resultContent + content.substring(i);

        return resultContent;
    }

    /**********************************
     * prepareInstructions
     **********************************/

    public ArrayList<InstructionSimple> createInstructions(){
        /**
         * matchers in string content
         */
        Matcher matcher = pattern.matcher(content);
        /**
         * new list instructions
         */
        ArrayList instructions = new ArrayList<InstructionSimple>();
        /**
         * waiting to complete internal instructions
         */
        final ArrayList<InstructionMultiple> onhold = new ArrayList<InstructionMultiple>();
        /**
         * waiting for arguments
         */
        final AtomicReference<InstructionSimple> waitingForArgumnet = new AtomicReference<InstructionSimple>();
        /**
         * init for
         */
        matcher.results().forEach(match -> {
            InstructionSimple instruction = null;
            /**
             * end of instruction
             */
            if(Utils.valid(this, match,"VAR")){
                /**
                 * create instruction type VAR
                 */
                instruction = new VAR(matcher.start(), seacrhNextEndSymbol(content, match.end()));

            }else if(Utils.valid(this, match,"FOR")){
                /**
                 * create instruction type FOR
                 */
                instruction = new FOR(matcher.start(), seacrhNextEndSymbol(content, match.end()));

            }else if(Utils.valid(this, match,"IF")){
                /**
                 * create instruction type IF
                 */
                instruction = new IF(matcher.start(), seacrhNextEndSymbol(content, match.end()));

            }else if(Utils.valid2(this, match,"ENDFOR")){
                /**
                 * create instruction type END, add in multiple (select last on hold)
                 */
                onhold.get(onhold.size() - 1).addEnd(new END(Type.ENDFOR, matcher.start(), matcher.end()));
                /**
                 * remove last element the onhold
                 */
                onhold.remove(onhold.size() - 1);

            }else if(Utils.valid2(this, match,"ENDIF")){
                /**
                 * create instruction type END, add in multiple (select last on hold)
                 */
                onhold.get(onhold.size() - 1).addEnd(new END(Type.ENDIF, matcher.start(), matcher.end()));
                /**
                 * remove last element the onhold
                 */
                onhold.remove(onhold.size() - 1);

            }else if(Utils.valid2(this, match,"ELSE")){
                /**
                 * create instruction type ELSE add in multiple (select last on hold)
                 */
                instruction = IF.ELSE(matcher.start(), matcher.end());
                onhold.get(onhold.size() - 1).addEnd(instruction);

            }else if(Utils.valid(this, match,"ELSEIF")){
                /**
                 * create instruction type ELSEIF add in multiple (select last on hold)
                 */
                instruction = IF.ELSEIF(matcher.start(), seacrhNextEndSymbol(content, match.end()));
                onhold.get(onhold.size() - 1).addEnd(instruction);
            }

            /**
             * if "nextEndSymbol" is equal to "waitingForArguments.end"
             * means "waitingForArguments.end" not found
             */

            if(waitingForArgumnet.get() != null && instruction != null && waitingForArgumnet.get().end > instruction.end){
                /**
                 * add argument
                 */
                waitingForArgumnet.get().addArgument(instruction);

            }else if(waitingForArgumnet.get() != null){
                /**
                 * clean up
                 */
                waitingForArgumnet.set(null);
            }

            if(waitingForArgumnet.get() == null && instruction != null){
                /**
                 * if waitingForArgument is null add actual instruction
                 */
                waitingForArgumnet.set(instruction);

                if(onhold.isEmpty()){
                    /**
                     * add to instructions
                     */
                    instructions.add(instruction);
                } else {
                    /**
                     * add in internal (select last on hold)
                     */
                    InstructionSimple onholdInstructionSimple = onhold.get(onhold.size() - 1);
                    if((onholdInstructionSimple.type == Type.IF || onholdInstructionSimple.type == Type.ELSEIF) &&
                            (instruction.type == Type.ELSEIF || instruction.type == Type.ELSE)){
                        onhold.remove(onhold.size() - 1);
                        onhold.add((InstructionMultiple) instruction);
                    } else{
                        onhold.get(onhold.size() - 1).addInternal(instruction);
                    }
                }
                /**
                 * add onhold (because they can contain internal instructions)
                 */
                if(instruction.type == Type.IF || instruction.type == Type.FOR){
                    onhold.add((InstructionMultiple) instruction);
                }
            }
        });
        /**
         * returns instructions
         */
        return instructions;
    }

    /**********************************
     * prepareStart
     **********************************/

    public int prepareStart(InstructionSimple instruction){
        if(instruction instanceof InstructionMultiple){
            return ln(indexLnFirst(instruction.start, ((InstructionMultiple) instruction).end().end) - 1, instruction.start);
        }
        return instruction.start;
    }

    /**********************************
     * regex
     **********************************/

    private String regex(){
        StringBuilder regex = new StringBuilder();
        regex.append("(\\").append(symbol);
        regex.append("(VAR|FOR|IF|ELSEIF)");
        regex.append("\\").append(symbolStart).append(")|");
        regex.append("(\\").append(symbol);
        regex.append("(ENDIF|ENDFOR|ELSE))");
        return regex.toString();
    }

    /**********************************
     * Index Ln First
     **********************************/

    public int indexLnFirst(int start, int end){
        for (int i = 0; i < lns.size(); i++){
            if(lns.get(i) > start && lns.get(i) < end){
                return i;
            }
        }
        return -1;
    }

    /**********************************
     * Index Ln
     **********************************/

    public int ln(int index, int temp){
        if(index >= 0 && index < lns.size()){
            return lns.get(index);
        }
        return temp;
    }

    /**********************************
     * getters
     **********************************/

    public Template object(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Template object(String key, ObjectCustom value) {
        data.put(key, value);
        return this;
    }

    public Hashtable<String, Object> data() {
        return data;
    }

    public String symbol() {
        return symbol;
    }

    public String symbolStart() {
        return symbolStart;
    }

    public String symbolEnd() {
        return symbolEnd;
    }


    /**********************************
     * static
     **********************************/

    public static Template template(String content, String symbol, String start, String end){
        return new Template(content, symbol, start, end);
    }

    /**********************************
     * static
     **********************************/

    public static Template template(String content){
        return new Template(content);
    }

    /**********************************
     * static
     **********************************/

    public static Template file(String url, boolean internal, String symbol, String start, String end){
        return new Template(File.read(path+url, internal), symbol, start, end);
    }

    public static Template file(String url, String symbol, String start, String end){
        return Template.file(url, true, symbol, start, end);
    }

    /**********************************
     * static
     **********************************/

    public static Template file(String url, boolean internal){
        return new Template(File.read(path+url, internal));
    }

    public static Template file(String url){
        return Template.file(url, true);
    }

    /**********************************
     * static
     **********************************/

    public boolean create(String url, String name, String ext, boolean replace){
        return File.create(url, name, ext, this.build(), replace);
    }

    /**********************************
     * static
     **********************************/

    public boolean create(){
        String contentProps = this.build();
        AtomicInteger integer = new AtomicInteger(0);
        AtomicReference<String> url = new AtomicReference<String>("");
        AtomicReference<String> name = new AtomicReference<String>("");
        AtomicBoolean replace = new AtomicBoolean(false);
        AtomicBoolean create = new AtomicBoolean(true);
        contentProps.lines().forEach((line)->{
            if(line.contains("PATH:") || line.contains("NAME:") || line.contains("REPLACE:")){
                integer.set(integer.get() + line.length() + System.lineSeparator().length());
                String result[] = line.split(":");
                if(result.length > 1){
                    if(line.contains("PATH:")){
                        url.set(result[1].trim());
                    }else if(line.contains("NAME:")){
                        name.set(result[1].trim());
                    }else if(line.contains("REPLACE:")){
                        replace.set(result[1].trim().equalsIgnoreCase("true"));
                    }else if(line.contains("CREATE:")){
                        create.set(result[1].trim().equalsIgnoreCase("true"));
                    }
                }
            }
        });
        if(!url.get().isEmpty() && !name.get().isEmpty() && create.get()){
            return File.create(url.get(), name.get(), contentProps.substring(integer.get()), replace.get());
        }
        return false;
    }

    private int seacrhNextEndSymbol(String content, int i){
        int countFind = 0;
        int end = content.indexOf(symbolEnd, i-1);
        int start = content.indexOf(symbolStart, i+1);
        do {
            if(countFind > 0){
                end = content.indexOf(symbolEnd, end + 1);
                countFind--;
                start = content.indexOf(symbolStart, start+1);
            }
            if(start < end && start != -1){
                countFind++;
            }
        } while (countFind != 0);
        return end;
    };

}
