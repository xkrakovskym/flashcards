package flashcards;


import java.io.*;
import java.util.*;

public class Deck {

    private Map<String, String> termMap;
    private Map<String, String> definitionMap;
    private Map<String, Integer> cardStatistics;
    private Iterator<Map.Entry<String, String>> deckIterator;

    public Deck() {
        termMap = new LinkedHashMap<>();
        definitionMap = new LinkedHashMap<>();
        this.cardStatistics = new HashMap<>();
    }

    public void addDeck(Map<String, String> termMapToAdd) {
        termMap.putAll(termMapToAdd);
        definitionMap.clear();
        for (var entry : termMap.entrySet()) {
            definitionMap.put(entry.getValue(), entry.getKey());
        }
    }

    public void addCard(String term, String definition) {
        termMap.put(term, definition);
        definitionMap.put(definition, term);
    }

    public boolean removeCard(String term) {
        String deleteTerm = null;
        for (var entry : termMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(term)) {
                deleteTerm = termMap.remove(entry.getKey());
                definitionMap.remove(deleteTerm);
                cardStatistics.remove(deleteTerm);
                break;
            }
        }
        return deleteTerm != null;
    }

    public boolean termExists(String term) {
        return termMap.containsKey(term);
    }

    public boolean definitionExists(String definition) {
        return definitionMap.containsKey(definition);
    }


    public void initDealing() {
        deckIterator = termMap.entrySet().iterator();
    }

    public Iterator<Map.Entry<String, String>> getDeckIterator() {
        return deckIterator;
    }

    public int getDeckSize() {
        return termMap.size();
    }

    public boolean addTerm(String input) {
        return !termExists(input);
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

    public Pair<Integer, List<String>> getHardestCards() {
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
            cardStatistics = (HashMap<String, Integer>) in.readObject();
            termMapToImport = (LinkedHashMap<String, String>) in.readObject();
            in.close();
            fileIn.close();
            addDeck(termMapToImport);
            numOfCardsImported = termMapToImport.entrySet().size();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return numOfCardsImported;
    }

    public Integer exportToObject(String input) {
        Integer numOfCardsExported = null;
        try {
            FileOutputStream fileOut = new FileOutputStream(input);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cardStatistics);
            out.writeObject(termMap);
            out.close();
            fileOut.close();
            numOfCardsExported = getDeckSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numOfCardsExported;
    }
}