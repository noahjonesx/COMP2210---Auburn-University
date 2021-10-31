import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;
import java.io.File;
import java.io.FileReader;
import java.util.SortedSet;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.lang.Math;



/**
 * Engine for Word Search Game
 * @author Noah Jones (nhj0004@auburn.edu)
 */
 
public class WordSearchEngine implements WordSearchGame {
   //Create TreeSet for lexicon 
   private TreeSet<String> lexicon; 
   //Create multidimensional array to serve as game board
   private String[][] board;
   //sets final / max board size
   private static final int MAX_NEIGHBORS = 8;
   //width of board
   private int width;
   //height of board 
   private int height;
   //marks positions that have already been visited
   private boolean[][] visited;
   //defines how to stay on path for game
   private ArrayList<Integer> path;
   //Keeps track of word as it is being built
   private String wordSoFar;
   //Set of all words on board
   private SortedSet<String> allWords;
   private ArrayList<Position> path2; 

   //Constructor 
   //Initializes board

   public WordSearchEngine() {
      //lexicon begins at null
      lexicon = null;
      //Default playing board
      board = new String[4][4];
      //mark all positions
      board[0][0] = "F";
      board[0][1] = "I";
      board[0][2] = "N";
      board[0][3] = "D";
      board[1][0] = "W";
      board[1][1] = "O";
      board[1][2] = "R";
      board[1][3] = "D";
      board[2][0] = "S";
      board[2][1] = "O";
      board[2][2] = "N";
      board[2][3] = "B";
      board[3][0] = "O";
      board[3][0] = "A";
      board[3][2] = "R";
      board[3][3] = "D";    
      width = board.length;
      height = board[0].length;
      //remark all spots as unvisited
      markAllUnvisited();
   }
       
    /**
    * Loads the lexicon into a data structure for later use.
    *
    * @param fileName A string containing the name of the file to be opened.
    * @throws IllegalArgumentException if fileName is null
    * @throws IllegalArgumentException if fileName cannot be opened.
    */
   public void loadLexicon(String fileName) {
      lexicon = new TreeSet<String>();
   //Checks if file is real, and reads it in.
      if (fileName == null) {
         throw new IllegalArgumentException();
      }
      try {
         Scanner s = new Scanner(new BufferedReader(new FileReader(new File(fileName))));
         while (s.hasNext()) {
            String str = s.next();
            str = str.toUpperCase();
            lexicon.add(str); //Adds it to the lexicon
            s.nextLine();
         }
      }
      catch (Exception e) {
         throw new IllegalArgumentException("Error loading word list: " + fileName + ": " + e);
      }
   }
   
   /**
    * @param letterArray stores contents of game board
    * @throws IllegalArgumentException if letterArray is null / not square
    */

   public void setBoard(String[] letterArray) {
     //Throw IAE if letterArray null
      if (letterArray == null) {
         throw new IllegalArgumentException();
      }
      int n = (int)Math.sqrt(letterArray.length);
      //Throw IAE if not square.
      if ((n * n) != letterArray.length) {
         throw new IllegalArgumentException();
      }
      //Put into 2D array

      board = new String[n][n];
      width = n;
      height = n;
      int index = 0;
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            board[i][j] = letterArray[index];
            index++;
         }
      }
      //remarks all visited positions
      markAllUnvisited(); 
   }
   
   // creates string representation of board  

    /** 
     * @param strBoard for string representation of game board
     * @return board for strBoard
     */

   public String getBoard() {
      String strBoard = "";
      for (int i = 0; i < height; i ++) {
         if (i > 0) {
            strBoard += "\n";
         }
         for (int j = 0; j < width; j++) {
            strBoard += board[i][j] + " ";
         }
      }
      return strBoard;
   }
   
   /**
    * @param minimumWordLength defined min length for what counts as a word.
    * @throws IllegalArgumentException if minimumWordLength is less than 1
    * @throws IllegalStateException if lexicon is not there
    */
   public SortedSet<String> getAllScorableWords(int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      path2 = new ArrayList<Position>(); //second to keep track of path
      allWords = new TreeSet<String>();
      wordSoFar = "";
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j ++) {
            wordSoFar = board[i][j];
            if (isValidWord(wordSoFar) && wordSoFar.length() >= minimumWordLength) {
               allWords.add(wordSoFar);
            }
            if (isValidPrefix(wordSoFar)) {
               Position temp = new Position(i,j);
               path2.add(temp);
               dfs2(i, j, minimumWordLength);
               //remove last from temp upon failure
               path2.remove(temp);
            }
         }
      }
      return allWords;
   }
   
   private void dfs2(int x, int y, int min) {
      Position start = new Position(x, y);
      //mark all unvisited spots
      markAllUnvisited();
      //Marks path of current word
      markPathVisited(); 
      for (Position p : start.neighbors()) {
         if (!isVisited(p)) {
            visit(p);
            if (isValidPrefix(wordSoFar + board[p.x][p.y])) {
               wordSoFar += board[p.x][p.y];
               path2.add(p);
               if (isValidWord(wordSoFar) && wordSoFar.length() >= min) {
                  allWords.add(wordSoFar);
               }
               dfs2(p.x, p.y, min);
               //Backtracking, remove last added word
               path2.remove(p);
               int endIndex = wordSoFar.length() - board[p.x][p.y].length();
               wordSoFar = wordSoFar.substring(0, endIndex);
            }
         }
      }
      markAllUnvisited(); 
      markPathVisited(); 
   }
   
  /**
   * @param words set of completed words
   * @param minimumWordLength min required characters for word
   * @throws IllegalArgumentException if minimumWordLength less than 1
   * @throws IllegalStateException if lexicon not there
   */  
   public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      int score = 0;
      Iterator<String> itr = words.iterator();
      while (itr.hasNext()) {
         String word = itr.next();
         //if word is atleast min length, is in lexicon, and is on the board
         if (word.length() >= minimumWordLength && isValidWord(word)
             && !isOnBoard(word).isEmpty()) {
            //+1 score for meeting min and +1 for each extra character
            score += (word.length() - minimumWordLength) + 1;
         }
      }
      return score;
   }
   
   /**
    * Determines if the completed word is valid
    * @param wordToCheck  word being validated
    * @throws IllegalArgumentException if wordToCheck is null
    * @throws IllegalStateException if lexicon not there
    */
   public boolean isValidWord(String wordToCheck) {
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      //Checks if word exists in lexicon 
      wordToCheck = wordToCheck.toUpperCase();
      return lexicon.contains(wordToCheck);
   }
   
   /**
    * determines if prefix is valid in lexicon 
    * @param prefixToCheck prefix being validated
    * @throws IllegalArgumentException if prefixToCheck is null.
    * @throws IllegalStateException if lexicon is not there
    */
   public boolean isValidPrefix(String prefixToCheck) {
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      if (prefixToCheck == null) {
         throw new IllegalArgumentException();
      }
      prefixToCheck = prefixToCheck.toUpperCase();
      //Checks if in lexicon
      String word = lexicon.ceiling(prefixToCheck);
      if (word != null) {
         return word.startsWith(prefixToCheck);
      }
      return false;
   }
     
   /**
    * determines if lexicon word is on game board
    * @throws IllegalArgumentException if wordToCheck is null.
    * @throws IllegalStateException if lexicon is not there.
    */
   public List<Integer> isOnBoard(String wordToCheck) {
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      path2 = new ArrayList<Position>();
      wordToCheck = wordToCheck.toUpperCase();
      wordSoFar = "";
      path = new ArrayList<Integer>();
   //mark starting position
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j ++) {
         //If first spot = word, add to list and return.
            if (wordToCheck.equals(board[i][j])) {
               //row-major position 
               path.add(i * width + j);
               return path;
            }
            if (wordToCheck.startsWith(board[i][j])) {
               Position pos = new Position(i, j);
               path2.add(pos);
               //regular position
               //add to wordSoFar
               wordSoFar = board[i][j];
               //Start search
               dfs(i, j, wordToCheck);
               //remove upon failure
               if (!wordToCheck.equals(wordSoFar)) {
                  path2.remove(pos);
               }
               else {
               //row-major position
                  for (Position p: path2) {
                     path.add((p.x * width) + p.y);
                  }
                  return path;
               }
            }
         }
      }
      return path;
   }
   
   /**
   * Depth-first search 
   * @param x for x value
   * @param y for y value
   * @param wordToCheck the word to check for.
   */
   private void dfs(int x, int y, String wordToCheck) {
      Position start = new Position(x, y);
      markAllUnvisited(); 
      markPathVisited();
      for (Position p: start.neighbors()) {
         if (!isVisited(p)) {
            visit(p);
            if (wordToCheck.startsWith(wordSoFar + board[p.x][p.y])) {
               //add words to wordSoFar
               wordSoFar += board[p.x][p.y]; 
               path2.add(p);
               dfs(p.x, p.y, wordToCheck);
               if (wordToCheck.equals(wordSoFar)) {
                  return;
               }
               else {
                  path2.remove(p);
               //backtracking
                  int endIndex = wordSoFar.length() - board[p.x][p.y].length();
                  wordSoFar = wordSoFar.substring(0, endIndex);
               }
            }
         }
      }
      markAllUnvisited();
      markPathVisited();

   }

   /**
   * Marks all unvisited spots.
   */
   private void markAllUnvisited() {
      visited = new boolean[width][height];
      for (boolean[] row : visited) {
         Arrays.fill(row, false);
      }
   }
   
   /**
   * Mark as visited.
   */
   private void markPathVisited() {
      for (int i = 0; i < path2.size(); i ++) {
         visit(path2.get(i));
      }
   }
   

   //position class 

   private class Position {
      int x;
      int y;
   
      /** position with x,y coordinates */
      public Position(int x, int y) {
         this.x = x;
         this.y = y;
      }
   
      /** Returns position as string */
      @Override
      public String toString() {
         return "(" + x + ", " + y + ")";
      }
   
      // Returns neighbor positions

      public Position[] neighbors() {
         Position[] nbrs = new Position[MAX_NEIGHBORS];
         int count = 0;
         Position p;
         // get all neighbor positions
         // add to return if valid
         for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
               if (!((i == 0) && (j == 0))) {
                  p = new Position(x + i, y + j);
                  if (isValid(p)) {
                     nbrs[count++] = p;
                  }
               }
            }
         }
         return Arrays.copyOf(nbrs, count);
      }
   }

   /**
    * checks if a position is valid
    * @param p for position
    */
   private boolean isValid(Position p) {
      return (p.x >= 0) && (p.x < width) && (p.y >= 0) && (p.y < height);
   }

   /**
    * Checks if a position has been visited.
    * @param p for position
    */

   private boolean isVisited(Position p) {
      return visited[p.x][p.y];
   }

   /**
    * Mark valid position as visited.
    */

   private void visit(Position p) {
      visited[p.x][p.y] = true;
   }

}