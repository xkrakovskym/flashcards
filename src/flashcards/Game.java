package flashcards;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Game {
    private List<String> logList;
    private StateMachine state;
    private Deck deck;
    private Map<String, Integer> cardStatistics;
    private String exportFileName;

    public Game(String inputFileName, String exportFileName) {
        this.cardStatistics = new HashMap<>();
        this.logList = new ArrayList<>();
        this.deck = new Deck();
        state = StateMachine.MAIN_MENU;
        this.exportFileName = exportFileName;

        if (inputFileName != null){
            state.IMPORT_FILE.moveOn(this ,inputFileName); // this is ugly, but trying not to use logger in game
        }
    }

    public Pair<Integer, List<String>> hardestCards() {
        int maxMistakes = 0;
        for (var entry : cardStatistics.entrySet()) {
            if (entry.getValue() > maxMistakes) {
                maxMistakes = entry.getValue();
            }
        }
        List<String> hardestCards = new ArrayList<>();
        for (var entry : cardStatistics.entrySet()) {
            if (entry.getValue().equals(maxMistakes)) {
                hardestCards.add("\"" + entry.getKey() + "\"");
            }
        }
        return new Pair<>(maxMistakes, hardestCards);
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
        Integer previousValue = cardStatistics.putIfAbsent(currentTerm, 1);
        if (previousValue != null) {
            cardStatistics.put(currentTerm, ++previousValue);
        }
    }

    public void resetStats() {
        cardStatistics.clear();
    }

    public Integer importFromObject(String input) {
        Map<String, String> termMapToImport;
        Integer numOfCardsImported = null;
        try {
            FileInputStream fileIn = new FileInputStream(input);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cardStatistics = (HashMap) in.readObject();
            termMapToImport = (LinkedHashMap) in.readObject();
            in.close();
            fileIn.close();
            deck.add(termMapToImport);
            numOfCardsImported =termMapToImport.entrySet().size();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return numOfCardsImported;
    }

    public Integer exportToObject(String input) {
        Integer numOfCardsExported = null;
        if (input == null){
            input = exportFileName;
        }
        if (input != null) {
            try {
                FileOutputStream fileOut = new FileOutputStream(input);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(cardStatistics);
                deck.saveTermMap(out); // is this OKAY?!?!?
                out.close();
                fileOut.close();
                numOfCardsExported = deck.getDeckSize();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return numOfCardsExported;
    }

    public boolean processRemove(String input){
        if (deck.removeCard(input)) {
            cardStatistics.remove(input);
            return true;
        } else {
            return false;
        }
    }

    public boolean processTerm(String input) {
        return deck.addTerm(input);
    }

    public String processDefinition(String input) {
        String termAdded = null;
        if (!deck.definitionExists(input)){
            termAdded = deck.addCard(input);
        }
        return termAdded;
    }

    public void ask(String input) throws NumberFormatException {
        int timesToAsk = Integer.parseInt(input);
        if (timesToAsk > 0) {
            deck.setTimesToAsk(timesToAsk);
            deck.initDealing();
            transitionToState(StateMachine.ASK_DEFINITION);
        } else {
            transitionToState(StateMachine.MAIN_MENU);
        }
    }

    public String askDefinition() {
        String askedTerm = null;
        int timesToAsk = deck.getTimesToAsk();
        if (timesToAsk > 0 && !deck.getTermMap().isEmpty()) {
            if (!deck.getDeckIterator().hasNext()) {
                deck.initDealing();
            }
            Map.Entry<String, String> entry = deck.getDeckIterator().next();
            askedTerm = entry.getKey();
            deck.setCurrentTerm(entry.getKey());
            deck.setTimesToAsk(--timesToAsk);
        }
        return askedTerm;
    }

    public Pair<String, String> getAskedCard() {
        return deck.giveCurrentCard();
    }

    public boolean definitionExists(String input) {
        return deck.definitionExists(input);
    }


    public String getTermForDefinition(String input) {
        return deck.getTermForDefinition(input);
    }
}