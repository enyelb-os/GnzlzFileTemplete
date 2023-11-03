package tools.gnzlz.template.instruction.base;

import tools.gnzlz.template.instruction.Type;

import java.util.ArrayList;

public abstract class InstructionMultiple extends InstructionSimple {

    /**
     * internals
     */
    protected ArrayList<InstructionSimple> internals;

    /**
     * endInstruction
     */
    protected InstructionSimple endInstruction;

    /**
     * InstructionMultiple
     * @param type t
     * @param start s
     * @param end e
     */
    public InstructionMultiple(Type type, int start, int end) {
        super(type,start,end);
        this.internals = new ArrayList<InstructionSimple>();
    }

    /**
     * addInternal
     * @param instruction i
     */
    public void addInternal(InstructionSimple instruction) {
        internals.add(instruction);
    }

    /**
     * addEnd
     * @param instruction i
     */
    public void addEnd(InstructionSimple instruction){
        endInstruction = instruction;
    }

    /**
     * end
     */
    public InstructionSimple end(){
        if(endInstruction instanceof InstructionMultiple){
           return ((InstructionMultiple) endInstruction).end();
        }
        return endInstruction;
    }
}
