/*
NAME: Henry Unruh
CLASS: CS317
FUNCTION: generateNFA
SYNOPSIS: the generateNFA.java class houses all of the functions that will create the various NFAs from the
regular expressions.
 */

//Import statement(s)
import java.util.ArrayList;

//CLASS - generateNFA, creates an NFA based on the regex given.
public class generateNFA{
    //Initial or start state.
    private int initialState;

    //Final or accept state.
    private int finalState;

    //Identifier for states.
    private int nextSID;

    //Transition between states.
    private ArrayList<stateTrans> stateChangeList;

    //generateNFA, Initializes a blank NFA
    private generateNFA(){
        initialState = 0;
        finalState = 0;
        nextSID = 0;
        stateChangeList = new ArrayList<stateTrans>();
    }

    //Generates a new NFA node that receives a new input symbol.
    public generateNFA(char s){
        initialState = 1;
        finalState = 2;
        nextSID = 3;
        stateChangeList = new ArrayList<stateTrans>();
        stateChangeList.add(new stateTrans(initialState,s, finalState));
    }

    //Creates an existing NFA node.
    public generateNFA(generateNFA NFA){
        initialState = NFA.initialState;
        finalState = NFA.finalState;
        nextSID = NFA.nextSID;
        stateChangeList = NFA.getStateList();
    }

    public int getInitState(){
        return initialState;
    }
    public int getFinalState(){
        return finalState;
    }
    public ArrayList<stateTrans> getStateList(){
        return new ArrayList<stateTrans>(stateChangeList);
    }
    public int getNextStateID(){
        return nextSID;
    }

     //Generates the union of two automata; will not change the originals.
     //Returns an NFA that represents the union.
    public generateNFA union(generateNFA newNFA){
        generateNFA unionNFA = new generateNFA();
        int next = nextSID - 1;
        ArrayList<stateTrans> trList2 = incrStates(newNFA,next);

        //Setting up the unionNFA.
        unionNFA.initialState = newNFA.nextSID + next;
        unionNFA.finalState = unionNFA.initialState + 1;
        unionNFA.nextSID = unionNFA.finalState + 1;

        //Adding the transition states.
        unionNFA.stateChangeList.add(new stateTrans(unionNFA.initialState,'E',initialState));
        unionNFA.stateChangeList.add(new stateTrans(unionNFA.initialState,'E',newNFA.initialState+next));
        unionNFA.stateChangeList.addAll(stateChangeList);
        unionNFA.stateChangeList.addAll(trList2);
        unionNFA.stateChangeList.add(new stateTrans(finalState,'E',unionNFA.finalState));
        unionNFA.stateChangeList.add(new stateTrans(newNFA.finalState+next,'E',unionNFA.finalState));
        return unionNFA;
    }

    //Concatenates 2 NFA
    //Will generate a new NFA that represents the connected NFAs.
    public generateNFA concat(generateNFA newNFA){
        generateNFA concatNFA = new generateNFA();
        int val = nextSID - 1;
        ArrayList<stateTrans> transitionList2 = incrStates(newNFA,val);

        //Setting up concatinated NFA.
        concatNFA.initialState = initialState;
        concatNFA.finalState = newNFA.finalState + val;
        concatNFA.nextSID = newNFA.nextSID + val;

        concatNFA.stateChangeList.addAll(stateChangeList);
        concatNFA.stateChangeList.add(new stateTrans(finalState,'E',newNFA.initialState+val));
        concatNFA.stateChangeList.addAll(transitionList2);
        return concatNFA;
    }

    //Adds a new state to take the kleene star.
    public void kStar(){
        int newState = nextSID++;
        stateTrans tr1 = new stateTrans(newState,'E',initialState);
        stateChangeList.add(tr1);

        stateTrans tr2 = new stateTrans(finalState,'E',newState);
        stateChangeList.add(tr2);
        initialState = finalState = newState;
    }

     //CLASS: stateTrans, creates a data structure.
     //Structure has nested transitions which holds info about state changes.
    public class stateTrans{
        private int fromState;
        private char symbol;
        private int toState;

        private stateTrans(int fromState, char symbol, int toState){
            this.fromState = fromState;
            this.symbol = symbol;
            this.toState = toState;
        }

        public int getFromState() {
            return fromState;
        }
        public char getSymbol() {
            return symbol;
        }
        public int getToState() {
            return toState;
        }
    }

    //Increments all the state values from a NFA's state list.
    //Returns new state list of transitions with the incremented state values.
    private ArrayList<stateTrans> incrStates(generateNFA NFA, int val){
        ArrayList<stateTrans> incrSTList = NFA.getStateList();
        for (stateTrans tr : incrSTList) {
            tr.fromState += val;
            tr.toState += val;
        }
        return incrSTList;
    }
}