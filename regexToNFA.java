/*
NAME: Henry Unruh
CLASS: CS317
FUNCTION: regexToNFA
SYNOPSIS: the regexToNFA.java class has code that will generate a non-deterministic, finite automata based on
the regular expression given through user input.
 */

//Import statements;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Stack;
import java.io.IOException;

//CLASS: regexToNFA, takes in regular expression and converts using generateNFA.java
public class regexToNFA{
    public static void main(String[] args){
        for (String filename:args){
            //Check to ignore the command line arguments.
            if((filename=="java") || (filename=="regexToNFA")){
                continue;
            }
            FileReader input;
            try {
                input = new FileReader(filename);
            } catch (FileNotFoundException e){
                System.out.println("Error: File \"" + filename + "\" not found");
                continue;
            }
            System.out.println("NFAs from \"" + filename + "\" ");
            Stack<generateNFA> automataStack = new Stack<>();
            int x;
            int nfaVal = 1;
            try {
                while ((x=input.read()) != -1) {
                    if (x=='&') {
                        generateNFA nfa2 = automataStack.pop();
                        generateNFA nfa1 = automataStack.pop();
                        automataStack.push(nfa1.concat(nfa2));
                    } else if (x=='|') {
                        generateNFA nfa2 = automataStack.pop();
                        generateNFA nfa1 = automataStack.pop();
                        automataStack.push(nfa1.union(nfa2));
                    } else if (x=='*') {
                        automataStack.peek().kStar();
                    } else if (x=='\n') {
                        System.out.println("NFA " + nfaVal++ + " ");
                        printNFA(automataStack.pop());
                        automataStack.clear();
                    } else {
                        automataStack.push(new generateNFA((char) x));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading character in file \"" + filename + "\"");
                System.exit(1);
            }
        }
    }

    private static void printNFA(generateNFA NFA) {
        System.out.println("Start: q" + NFA.getInitState() +
                "\nAccept: q" + NFA.getFinalState()
        );
        ArrayList<generateNFA.stateTrans> transitionList = NFA.getStateList();
        for (generateNFA.stateTrans tr : transitionList) {
            System.out.println("(q" +
                    tr.getFromState() +
                    ", " +
                    tr.getSymbol() +
                    ") -> q" +
                    tr.getToState()
            );
        }
    }
}