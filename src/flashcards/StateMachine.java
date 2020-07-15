package flashcards;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public enum StateMachine {
    MAIN_MENU{
        @Override
        public void menuText(Deck deck){
            DisplayUtils.mainMenuText();
        }

        @Override
        public void moveOn(Deck deck, String input) {
            switch (input)
            {
                case "add":
                    deck.setState(StateMachine.ADD_TERM);
                    break;
                case "remove":
                    deck.setState(StateMachine.REMOVE_CARD);
                    break;
                case "import":
                    deck.setState(StateMachine.IMPORT_FILE);
                    break;
                case "export":
                    deck.setState(StateMachine.EXPORT_FILE);
                    break;
                case "ask":
                    deck.setState(StateMachine.ASK);
                    break;
                case "exit":
                    deck.setState(StateMachine.EXIT);
                    break;
                default:
                    DisplayUtils.incorrectInputText();
            }
        }
    },
    ADD_TERM {
        @Override
        public void menuText(Deck deck){
            DisplayUtils.cardInputText();
        }

        @Override
        public void moveOn(Deck deck, String input) {
            if (!deck.termExists(input)) {
                deck.setCurrentTerm(input);
                deck.setState(StateMachine.ADD_DEFINITION);
            } else {
                DisplayUtils.termAlreadyExistsText(input);
                deck.setState(StateMachine.MAIN_MENU);
            }
        }
    },
    ADD_DEFINITION {
        @Override
        public void menuText(Deck deck){
            DisplayUtils.cardDefinitionInputText();
        }

        @Override
        public void moveOn(Deck deck, String input) {
            if (!deck.definitionExists(input)) {
                deck.addCard(deck.getCurrentTerm(),input);
                DisplayUtils.cardAddedText(deck.getCurrentTerm(), input);
            } else {
                DisplayUtils.definitionAlreadyExistsText(input);
            }
            deck.setState(StateMachine.MAIN_MENU);
        }
    },
    REMOVE_CARD {
        @Override
        public void menuText(Deck deck){
            DisplayUtils.cardInputText();
        }

        @Override
        public void moveOn(Deck deck, String input) {
            if (deck.removeCard(input)){
                DisplayUtils.cardWasRemovedText();
            } else {
                DisplayUtils.cardWasNotRemovedText(input);
            }
            deck.setState(StateMachine.MAIN_MENU);
        }
    },
    IMPORT_FILE {
        @Override
        public void menuText(Deck deck){
            DisplayUtils.fileNameText();
        }

        @Override
        public void moveOn(Deck deck, String input) {
            if (Deck.isSavedAsObject()){
                importFromObject(deck, input);
            } else {
                importFromText(deck, input);
            }
            deck.setState(StateMachine.MAIN_MENU);
        }

        public void importFromText(Deck deck, String input) {
        }

        public void importFromObject(Deck deck, String input){
            Map <String, String> termMapToImport;
            try {
                FileInputStream fileIn = new FileInputStream(input);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                termMapToImport = (LinkedHashMap) in.readObject();
                in.close();
                fileIn.close();
                deck.add(termMapToImport);
                DisplayUtils.loadedCardsText(termMapToImport.entrySet().size());
            } catch (IOException | ClassNotFoundException exception) {
                DisplayUtils.fileErrorText();
                exception.printStackTrace();
            }
        }
    },
    EXPORT_FILE {
        @Override
        public void menuText(Deck deck){
            DisplayUtils.fileNameText();
        }

        @Override
        public void moveOn(Deck deck, String input) {
            if (Deck.isSavedAsObject()){
                exportToObject(deck, input);
            } else {
                exportToText(deck, input);
            }
            deck.setState(StateMachine.MAIN_MENU);
        }

        public void exportToText(Deck deck, String input) {
        }

        public void exportToObject(Deck deck, String input) {
            try {
                FileOutputStream fileOut = new FileOutputStream(input);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(deck.getTermMap());
                out.close();
                fileOut.close();
                DisplayUtils.savedCardsText(deck.getDeckSize());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

    },
    ASK {
        @Override
        public void menuText(Deck deck){
            DisplayUtils.askHowManyTimeText();
        }

        @Override
        public void moveOn(Deck deck, String input) {
            int timesToAsk = 0;
            try{
                timesToAsk = Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                DisplayUtils.wrongFormatInputText();
            }

            if (timesToAsk > 0){
                deck.setTimesToAsk(timesToAsk);
                deck.initDealing();
                deck.setState(StateMachine.ASK_DEFINITION);
            } else {
                deck.setState(StateMachine.MAIN_MENU);
            }
        }
    },
    ASK_DEFINITION {
        @Override
        public void menuText(Deck deck){
            int timesToAsk = deck.getTimesToAsk();
            if (timesToAsk > 0 && !deck.getTermMap().isEmpty()){
                if (!deck.getDeckIterator().hasNext()){
                    deck.initDealing();
                }
                Map.Entry<String, String> entry = deck.getDeckIterator().next();
                DisplayUtils.cardAskText(entry.getKey());
                deck.setCurrentTerm(entry.getKey());
                deck.setTimesToAsk(--timesToAsk);
            } else {
                deck.setState(StateMachine.MAIN_MENU);
                DisplayUtils.mainMenuText();
            }
        }

        @Override
        public void moveOn(Deck deck, String input) {
            String definition = deck.getTermMap().get(deck.getCurrentTerm());
            if (definition.equalsIgnoreCase(input)) {
                DisplayUtils.correctAnswerText();
            } else {
                if (deck.definitionExists(input)) {
                    DisplayUtils.incorrectAnswerDefinitionExistsText(
                            definition,
                            deck.getDefinitionMap().get(input));
                } else {
                    DisplayUtils.incorrectAnswerText(definition);
                }
            }
        }
    },

    EXIT {
        @Override
        public void menuText(Deck deck){
            DisplayUtils.exitText();
        }

        @Override
        public void moveOn(Deck deck, String input) {
        }

        public boolean isRunning(){
            StateMachine.EXIT.menuText(null);
            return false;

        }
    };

    public abstract void menuText(Deck deck);
    public abstract void moveOn(Deck deck, String input);

    public boolean isRunning(){
        return true;
    }
}
