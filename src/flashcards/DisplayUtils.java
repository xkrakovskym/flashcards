package flashcards;

import java.util.List;

public class DisplayUtils {

    public static String displayAndReturn(String toOutput){
        System.out.println(toOutput);
        return toOutput;
    }

    public static String  mainMenuText() {
        return displayAndReturn(
                "Input the action (add, remove, import, export, " +
                        "ask, exit, log, hardest card, reset stats):");
    }

    public static String cardInputText() {
        return displayAndReturn("The card:");
    }

    public static String cardDefinitionInputText() {
        return displayAndReturn("The definition of the card:");
    }

    public static String cardAskText(String definition) {
        return displayAndReturn(String.format("Print the definition of \"%s\":", definition));
    }

    public static String correctAnswerText() {
        return displayAndReturn("Correct answer!");
    }

    public static String incorrectAnswerText(String definition) {
        return displayAndReturn(String.format("Wrong answer. The correct one is \"%s\".\n",
                definition));
    }

    public static String termAlreadyExistsText(String term) {
        return displayAndReturn(String.format("The card \"%s\" already exists.\n",
                term));
    }

    public static String definitionAlreadyExistsText(String definition) {
        return displayAndReturn(String.format("The definition \"%s\" already exists.\n",
                definition));
    }

    public static String incorrectAnswerDefinitionExistsText(String correctDefinition, String guessedTerm) {
        return displayAndReturn(String.format("Wrong answer. The correct one is \"%s\", you've just written the" +
                " definition of \"%s\".", correctDefinition, guessedTerm));
    }

    public static String incorrectInputText() {
        return displayAndReturn("Wrong input, please use one of these commands: add, remove, import, ask, exit");
    }

    public static String exitText() {
        return displayAndReturn("Bye bye!");
    }

    public static String cardWasRemovedText(){
        return displayAndReturn("The card has been removed.\n");
    }

    public static String cardWasNotRemovedText(String term){
        return displayAndReturn(String.format("Can't remove \"%s\": there is no such card.\n",
                term));
    }

    public static String askHowManyTimeText(){
        return displayAndReturn("How many times to ask?");
    }

    public static String fileNameText(){
        return displayAndReturn("File name:");
    }

    public static String fileErrorText(){
        return displayAndReturn("File not found.");
    }

    public static String cardAddedText(String term, String definition) {
        return displayAndReturn(String.format("The pair (\"%s\":\"%s\") has been added.\n", term, definition));
    }

    public static String exportCardsText(Integer numOfCards){
        return displayAndReturn(String.format("%d cards have been saved", numOfCards));
    }

    public static String importCardsText(Integer numOfCards){
        return displayAndReturn(String.format("%d cards have been loaded", numOfCards));
    }

    public static String savedLogText(){
        return displayAndReturn("The log has been saved.");
    }

    public static String noHardestCardText() {
        return displayAndReturn("There are no cards with errors.\n");
    }

    public static String hardestCardText(String term, Integer numOfMistakes) {
        return displayAndReturn(String.format("The hardest card is %s. You have %d errors answering it.", term, numOfMistakes));
    }

    public static String hardestCardsText(List<String> hardestCards, Integer numOfMistakes){
        String joinedTerms = String.join(", ", hardestCards);
        return displayAndReturn(String.format("The hardest cards are %s. You have %d errors answering them.", joinedTerms, numOfMistakes));
    }

    public static String resetStatsText() {
        return displayAndReturn("Card statistics has been reset.\n");
    }

    public static void wrongInputArgumentsText() {
        System.out.println("Wrong input arguments provided");
    }
}