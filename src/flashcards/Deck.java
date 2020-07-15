package flashcards;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.Serializable;

public class Deck implements Serializable {

    private Map<String, String> termMap;
    private Map<String, String> definitionMap;
    private transient Iterator<Map.Entry<String, String>> deckIterator; // transient to exclude from Serialization
    private int timesToAsk;
    private int deckSize;
    private flashcards.StateMachine state;
    private String currentTerm;
    private static final long serialVersionUID = 1452525600079957398L;
    private static final boolean savedAsObject = true;

    public Deck() {
        this.termMap = new HashMap<>();
        this.definitionMap = new HashMap<>();
        this.timesToAsk = 0;
        termMap = new LinkedHashMap<>();
        definitionMap = new LinkedHashMap<>();
        state = flashcards.StateMachine.MAIN_MENU;
    }

    public void add(Map<String, String> termMapToAdd){
        termMap.putAll(termMapToAdd);
        definitionMap.clear();
        for (var entry : termMap.entrySet()){
            definitionMap.put(entry.getValue(), entry.getKey());
        }
        deckSize = termMap.entrySet().size();
    }

    public boolean termExists(String term){
        return termMap.containsKey(term);
    }

    public boolean definitionExists(String definition){
        return definitionMap.containsKey(definition);
    }

    public void addCard(String term, String definition){
        termMap.put(term, definition);
        definitionMap.put(definition, term);
        ++deckSize;
    }

    public boolean removeCard(String term){
        String deleteTerm = null;
        for (var entry : termMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(term)) {
                deleteTerm = termMap.remove(entry.getKey());
                definitionMap.remove(deleteTerm);
                --deckSize;
                break;
            }
        }
        return deleteTerm != null;
    }

    public Map<String, String> getTermMap() {
        return termMap;
    }
    public Map<String, String> getDefinitionMap() {
        return definitionMap;
    }

    public void setState(flashcards.StateMachine state) {
        this.state = state;
    }

    public flashcards.StateMachine getState() {
        return state;
    }

    public void setCurrentTerm(String currentTerm) {
        this.currentTerm = currentTerm;
    }

    public String getCurrentTerm() {
        return currentTerm;
    }

    public void setTimesToAsk(int timesToAsk) {
        this.timesToAsk = timesToAsk;
    }

    public int getTimesToAsk() {
        return timesToAsk;
    }

    public void initDealing() {
        deckIterator = termMap.entrySet().iterator();
    }

    public Iterator<Map.Entry<String, String>> getDeckIterator() {
        return deckIterator;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public static boolean isSavedAsObject() {
        return savedAsObject;
    }
}