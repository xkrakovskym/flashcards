package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String inputFileName = null;
        String exportFileName = null;
        for (int i = 0; i < args.length; ++i){
            try {
                if (args[i].equalsIgnoreCase("-import") && i+1 < args.length) {
                    inputFileName = args[i + 1];
                } else if (args[i].equalsIgnoreCase("-export") && i+1 < args.length){
                    exportFileName = args[i + 1];
                }
            } catch (Exception ex) {
                DisplayUtils.wrongInputArgumentsText();
            }
        }

        Scanner reader = new Scanner(System.in);
        Game game = new Game(inputFileName, exportFileName);
        while (game.isRunning()){
            game.menuText();
            game.moveOn(reader.nextLine());
        }
    }
}