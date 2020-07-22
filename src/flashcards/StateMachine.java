package flashcards;

import javafx.util.Pair;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public enum StateMachine {
    MAIN_MENU {
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.mainMenuText());
        }

        @Override
        public void moveOn(Game game, String input) {
            switch (input) {
                case "add" -> game.transitionToState(StateMachine.ADD_TERM);
                case "remove" -> game.transitionToState(StateMachine.REMOVE_CARD);
                case "import" -> game.transitionToState(StateMachine.IMPORT_FILE);
                case "export" -> game.transitionToState(StateMachine.EXPORT_FILE);
                case "ask" -> game.transitionToState(StateMachine.ASK);
                case "exit" -> game.transitionToState(StateMachine.EXIT);
                case "log" -> game.transitionToState(StateMachine.LOG);
                case "hardest card" -> game.transitionToState(StateMachine.HARDEST_CARD);
                case "reset stats" -> game.transitionToState(StateMachine.RESET_STATS);
                default -> Logger.log(DisplayUtils.incorrectInputText());
            }
        }
    },
    ADD_TERM {
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.cardInputText());
        }

        @Override
        public void moveOn(Game game, String input) {
            if (game.processTerm(input)) {
                game.transitionToState(StateMachine.ADD_DEFINITION);
            } else {
                Logger.log(DisplayUtils.termAlreadyExistsText(input));
                game.transitionToState(StateMachine.MAIN_MENU);
            }
        }
    },
    ADD_DEFINITION {
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.cardDefinitionInputText());
        }

        @Override
        public void moveOn(Game game, String input) {
            String termAdded = game.processDefinition(input);
            if (termAdded != null) {
                Logger.log(DisplayUtils.cardAddedText(termAdded, input));
            } else {
                Logger.log(DisplayUtils.definitionAlreadyExistsText(input));
            }
            game.transitionToState(StateMachine.MAIN_MENU);
        }
    },
    REMOVE_CARD {
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.cardInputText());
        }

        @Override
        public void moveOn(Game game, String input) {
            if (game.processRemove(input)) {
                Logger.log(DisplayUtils.cardWasRemovedText());
            } else {
                Logger.log(DisplayUtils.cardWasNotRemovedText(input));
            }
            game.transitionToState(StateMachine.MAIN_MENU);
        }
    },
    IMPORT_FILE { // Have to import also card statistics
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.fileNameText());
        }

        @Override
        public void moveOn(Game game, String input) {
            Integer numOfCardsImported = game.importFromObject(input);
            if (numOfCardsImported != null){
                Logger.log(DisplayUtils.importCardsText(numOfCardsImported));
            } else {
                Logger.log(DisplayUtils.fileErrorText());
            }
            game.transitionToState(StateMachine.MAIN_MENU);
        }
    },
    EXPORT_FILE {
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.fileNameText());
        }

        @Override
        public void moveOn(Game game, String input) {
            Logger.log(DisplayUtils.exportCardsText(game.exportToObject(input)));
            game.transitionToState(StateMachine.MAIN_MENU);
        }
    },
    ASK {
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.askHowManyTimeText());
        }

        @Override
        public void moveOn(Game game, String input) {
            game.ask(input);
        }
    },
    ASK_DEFINITION {
        @Override
        public void menuText(Game game) {
            String askedTerm = game.askDefinition();
            if (askedTerm != null){
                Logger.log(DisplayUtils.cardAskText(askedTerm));
            } else {
                game.transitionToState(StateMachine.MAIN_MENU);
                Logger.log(DisplayUtils.mainMenuText());
            }
        }

        @Override
        public void moveOn(Game game, String input) {
            Pair<String, String> askedCard = game.getAskedCard();
            String correctDefinition = askedCard.getValue();
            if (correctDefinition.equalsIgnoreCase(input)) {
                Logger.log(DisplayUtils.correctAnswerText());
            } else {
                if (game.definitionExists(input)) {
                    Logger.log(DisplayUtils.incorrectAnswerDefinitionExistsText(
                            correctDefinition,
                            game.getTermForDefinition(input)));
                } else {
                    Logger.log(DisplayUtils.incorrectAnswerText(correctDefinition));
                    game.addMistake(askedCard.getKey());
                }
            }
        }
    },

    EXIT {
        @Override
        public void menuText(Game game) {
        }

        @Override
        public void moveOn(Game game, String input) {
        }
        @Override
        public boolean isRunning(Game game) {
            Logger.log(DisplayUtils.exitText());
            Integer numOfCardsExported = game.exportToObject(null);
            if (numOfCardsExported != null){
                Logger.log(DisplayUtils.exportCardsText(numOfCardsExported));
            }
            return false;
        }
    },
    LOG {
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.fileNameText());
        }

        @Override
        public void moveOn(Game game, String input) {
            try {
                Logger.saveLogToFile(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.log(DisplayUtils.savedLogText());
            game.transitionToState(StateMachine.MAIN_MENU);
        }

    },
    HARDEST_CARD {
        @Override
        public void menuText(Game game) {
            Pair<Integer, List<String>> hardestCardsStats = game.hardestCards();
            List<String> hardestCards = hardestCardsStats.getValue();
            Integer numOfMistakes = hardestCardsStats.getKey();

            if (numOfMistakes.equals(0)) {
                DisplayUtils.noHardestCardText();
            } else if (hardestCards.size() == 1){
                DisplayUtils.hardestCardText(hardestCards.get(0), numOfMistakes);
            }
            if (hardestCards.size() > 1){
                DisplayUtils.hardestCardsText(hardestCards, numOfMistakes);
            }
            game.transitionToState(StateMachine.MAIN_MENU);
            Logger.log(DisplayUtils.mainMenuText());
        }

        @Override
        public void moveOn(Game game, String input) {
        }
    },
    RESET_STATS {
        @Override
        public void menuText(Game game) {
            Logger.log(DisplayUtils.resetStatsText());
            game.resetStats();
            game.transitionToState(StateMachine.MAIN_MENU);
            Logger.log(DisplayUtils.mainMenuText());
        }

        @Override
        public void moveOn(Game game, String input) {
        }
    };

    public abstract void menuText(Game game);
    public abstract void moveOn(Game game, String input);

    public boolean isRunning(Game game){
        return true;
    }
}
