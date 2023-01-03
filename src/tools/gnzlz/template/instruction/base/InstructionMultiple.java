package tools.gnzlz.template.instruction.base;

import tools.gnzlz.template.instruction.Type;

import java.util.ArrayList;

public abstract class InstructionMultiple extends InstructionSimple {

    protected ArrayList<InstructionSimple> internals;
    protected InstructionSimple endInstruction;

    public InstructionMultiple(Type type, int start, int end) {
        super(type,start,end);
        this.internals = new ArrayList<InstructionSimple>();
    }

    public void addInternal(InstructionSimple instruction) {
        internals.add(instruction);
    }

    public void addEnd(InstructionSimple instruction){
        endInstruction = instruction;
    }

    public InstructionSimple end(){
        if(endInstruction instanceof InstructionMultiple){
           return ((InstructionMultiple) endInstruction).end();
        }
        return endInstruction;
    }
}