package flashcards;

import java.text.MessageFormat;

public class DisplayUtils {

    public static void mainMenuText() {
        System.out.println("Input the action (add, remove, import, export, ask, exit):");
    }

    public static void cardInputText() {
        System.out.println("The card:");
    }

    public static void cardDefinitionInputText() {
        System.out.println("The definition of the card:");
    }

    public static void cardAskText(String definition) {
        System.out.print(String.format("Print the definition of \"%s\": \n> ", definition));
    }

    public static void correctAnswerText() {
        System.out.println("Correct answer!");
    }

    public static void incorrectAnswerText(String definition) {
        System.out.print(MessageFormat.format("Wrong answer. The correct one is \"{0}\".\n",
                definition));
    }

    public static void termAlreadyExistsText(String term) {
        System.out.println(String.format("The card \"%s\" already exists.\n",
                term));
    }

    public static void definitionAlreadyExistsText(String definition) {
        System.out.println(String.format("The definition \"%s\" already exists.\n",
                definition));
    }

    public static void incorrectAnswerDefinitionExistsText(String correctDefinition, String guessedTerm) {
        System.out.println(MessageFormat.format("Wrong answer. The correct one is \"{0}\", you''ve just written the" +
                " definition of \"{1}\".", correctDefinition, guessedTerm));
    }

    public static void incorrectInputText() {
        System.out.println("Wrong input, please use one of these commands: add, remove, import, ask, exit");
    }

    public static void exitText() {
        System.out.println("Bye bye!");
    }

    public static void cardWasRemovedText(){
        System.out.println("The card has been removed.\n");
    }

    public static void cardWasNotRemovedText(String term){
        System.out.println(String.format("Can\'t remove \"%s\": there is no such card.\n",
                term));
    }

    public static void askHowManyTimeText(){
        System.out.println("How many times to ask?");
    }

    public static void wrongFormatInputText(){
        System.out.println("Wrong input!");
    }

    public static void fileNameText(){
        System.out.println("File name:");
    }

    public static void fileErrorText(){
        System.out.println("File not found.");
    }

    public static void cardAddedText(String term, String definition) {
        System.out.println(String.format("The pair (\"%s\":\"%s\") has been added.\n", term, definition));
    }

    public static void savedCardsText(int numOfCards){
        System.out.println(String.format("%d cards have been saved", numOfCards));
    }

    public static void loadedCardsText(int numOfCards){
        System.out.println(String.format("%d cards have been loaded", numOfCards));
    }
}