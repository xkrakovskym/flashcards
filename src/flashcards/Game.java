package flashcards;

import java.util.List;
import java.util.Map;

public class Game {
    private Deck deck;
    private StateMachine state;
    private String currentTerm;
    private int timesToAsk;
    private String exportFileName;

    public Game(String inputFileName, String exportFileName) {
        this.deck = new Deck();
        this.state = StateMachine.MAIN_MENU;
        this.exportFileName = exportFileName;
        this.timesToAsk = 0;

        if (inputFileName != null){
            StateMachine.IMPORT_FILE.moveOn(this ,inputFileName);
        }
    }

    public Pair<Integer, List<String>> hardestCards() {
        return deck.getHardestCards();
    }

    public void transitionToState(StateMachine state) {
        this.state = state;
    }

    public boolean isRunning() {
        return state.isRunning(this);
    }

    public void menuText() {
        state.menuText(this);
    }

    public void moveOn(String input) {
        state.moveOn(this, input);
    }

    public void addMistake(String currentTerm) {
        deck.addMistake(currentTerm);
    }

    public void resetStats() {
        deck.resetStats();
    }

    public Integer importFromObject(String input) {
        return deck.importFromObject(input);
    }

    public Integer exportToObject(String input) {
        Integer numOfCardsExported = null;
        if (input == null){
            input = exportFileName;
        }
        if (input != null) {
            numOfCardsExported = deck.exportToObject(input);
        }
        return numOfCardsExported;
    }

    public boolean processRemove(String input){
        return deck.removeCard(input);
    }

    public boolean processTerm(String input) {
        currentTerm = input;
        return deck.addTerm(input);
    }

    public String processDefinition(String input) {
        if (!deck.definitionExists(input)){
            deck.addCard(currentTerm, input);
        } else {
            currentTerm = null;
        }
        return currentTerm;
    }

    public void ask(String input) throws NumberFormatException {
        timesToAsk = Integer.parseInt(input);
        if (timesToAsk > 0) {
            deck.initDealing();
            transitionToState(StateMachine.ASK_DEFINITION);
        } else {
            transitionToState(StateMachine.MAIN_MENU);
        }
    }

    public String askDefinition() {
        currentTerm = null;
        if (timesToAsk > 0 && !deck.isEmpty()) {
            if (!deck.getDeckIterator().hasNext()) {
                deck.initDealing();
            }
            Map.Entry<String, String> entry = deck.getDeckIterator().next();
            currentTerm = entry.getKey();
            --timesToAsk;
        }
        return currentTerm;
    }

    public Pair<String, String> getCurrentCard() {
        return new Pair<> (currentTerm, deck.getDefinitionForTerm(currentTerm));
    }

    public boolean definitionExists(String input) {
        return deck.definitionExists(input);
    }


    public String getTermForDefinition(String input) {
        return deck.getTermForDefinition(input);
    }
}