public class ExampleGameClient {

   /** Drives execution. */
   public static void main(String[] args) {
      WordSearchGame game = WordSearchGameFactory.createGame();
      game.loadLexicon("Lexicon/CSW12.txt");
      game.setBoard(new String[]{"F", "I", "N", "D", "W", "O", "R", "D", "S",
                                 "O", "N", "B", "O", "A", "R", "D"});
      System.out.print("LENT is on the board at the following positions: ");
      System.out.println(game.isOnBoard("LENT"));
      System.out.print("POPE is not on the board: ");
      System.out.println(game.isOnBoard("POPE"));
      System.out.println("All words of length 4 or more: ");
      System.out.println(game.getAllScorableWords(4));
   }
}