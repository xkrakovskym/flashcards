package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Deck deck = new Deck();
        while (deck.getState().isRunning()){
            deck.getState().menuText(deck);
            deck.getState().moveOn(deck, reader.nextLine());
        }
    }
}
