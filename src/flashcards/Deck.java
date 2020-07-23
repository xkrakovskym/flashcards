package flashcards;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Deck {

    private Map<String, String> termMap;
    private Map<String, String> definitionMap;
    private transient Iterator<Map.Entry<String, String>> deckIterator; // transient to exclude from Serialization
    private int deckSize;

    public Deck() {
        termMap = new LinkedHashMap<>();
        definitionMap = new LinkedHashMap<>();
        deckSize = 0;
    }

    public void addDeck(Map<String, String> termMapToAdd){
        termMap.putAll(termMapToAdd);
        definitionMap.clear();
        for (var entry : termMap.entrySet()){
            definitionMap.put(entry.getValue(), entry.getKey());
        }
        deckSize = termMap.entrySet().size();
    }

    public void addCard(String term,String definition){
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

    public boolean termExists(String term){
        return termMap.containsKey(term);
    }

    public boolean definitionExists(String definition){
        return definitionMap.containsKey(definition);
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
        return !termExists(input);
    }

    public void saveTermMap(ObjectOutputStream out) throws IOException {
        out.writeObject(termMap);
    }

    public String getTermForDefinition(String input) {
        return definitionMap.get(input);
    }

    public String getDefinitionForTerm(String term) {
        return termMap.get(term);
    }

    public boolean isEmpty() {
        return termMap.isEmpty();
    }
}
