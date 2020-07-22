package flashcards;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Deck {

    private Map<String, String> termMap;
    private Map<String, String> definitionMap;
    private transient Iterator<Map.Entry<String, String>> deckIterator; // transient to exclude from Serialization
    private int timesToAsk;
    private int deckSize;
    private String currentTerm;

    public Deck() {
        this.timesToAsk = 0;
        termMap = new LinkedHashMap<>();
        definitionMap = new LinkedHashMap<>();
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

    public String addCard(String definition){
        termMap.put(currentTerm, definition);
        definitionMap.put(definition, currentTerm);
        ++deckSize;
        return currentTerm;
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

    public boolean addTerm(String input) {
        currentTerm = input;
        return !termExists(input);
    }

    public void saveTermMap(ObjectOutputStream out) throws IOException {
        out.writeObject(termMap);
    }

    public Pair<String, String> giveCurrentCard() {
        return new Pair<>(currentTerm, termMap.get(currentTerm));
    }

    public String getTermForDefinition(String input) {
        return definitionMap.get(input);
    }
}
