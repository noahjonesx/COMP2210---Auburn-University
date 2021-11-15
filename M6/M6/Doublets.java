import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import java.util.stream.Collectors;

/**
 * Provides an implementation of the WordLadderGame interface.
 *
 * @author Noah jones @nhj0004@auburn.edu)
 */
 
public class Doublets implements WordLadderGame {

   // The word list used to validate words.
   // Must be instantiated and populated in the constructor.
   /////////////////////////////////////////////////////////////////////////////
   // DECLARE A FIELD NAMED lexicon HERE. THIS FIELD IS USED TO STORE ALL THE //
   // WORDS IN THE WORD LIST. YOU CAN CREATE YOUR OWN COLLECTION FOR THIS     //
   // PURPOSE OF YOU CAN USE ONE OF THE JCF COLLECTIONS. SUGGESTED CHOICES    //
   // ARE TreeSet (a red-black tree) OR HashSet (a closed addressed hash      //
   // table with chaining).
   /////////////////////////////////////////////////////////////////////////////

   TreeSet<String> lexicon;
   static List<String> EMPTY_LADDER = new ArrayList<>();
   
   //// // // // // // // // // // // // // // // // // // // // // // // // //
   /**
    * Instantiates a new instance of Doublets with the lexicon populated with
    * the strings in the provided InputStream. The InputStream can be formatted
    * in different ways as long as the first string on each line is a word to be
    * stored in the lexicon.
    */
   public Doublets(InputStream in) {
      try {
         lexicon = new TreeSet<String>();
         Scanner s =
            new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            lexicon.add(str.toLowerCase());
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }

//retrieve hamming distance
   public int getHammingDistance(String str1, String str2) {
      if (str1.length() != str2.length()) {
         return -1;
      }
      char[] char1 = str1.toLowerCase().toCharArray();
      char[] char2 = str2.toLowerCase().toCharArray();
      int x = 0;
      for (int i = 0; i < str1.length(); i++) { 
         if (char1[i] != char2[i]) {
            x++;
         }
      }
      return x;
   }

//gets the ladder
   public List<String> getLadder(String start, String end) {
      List<String> result = new ArrayList<String>();
   
      if (start.equals(end)) {
         result.add(start);
         return result;
      }
      else if (start.length() != end.length()) {
         return EMPTY_LADDER;
      }
      else if (!isWord(start) || !isWord(end)) {
         return EMPTY_LADDER;
      }
      TreeSet<String> one = new TreeSet<>();
      Deque<String> stack = new ArrayDeque<>();
   
      stack.addLast(start);
      one.add(start);
      while(!stack.isEmpty()) {
         String current = stack.peekLast();
         if (current.equals(end)) {
            break;
         }
         List<String> neighbors1 = getNeighbors(current);
         List<String> neighbors = new ArrayList<>();
         for (String word : neighbors1) {
            if (!one.contains(word)) {
               neighbors.add(word);
            }
         }
         if (!neighbors.isEmpty()) {
            stack.addLast(neighbors.get(0));
            one.add(neighbors.get(0));
         }
         else {
            stack.removeLast();
         }
      }
      result.addAll(stack);
      return result;
   }

//Return minimum length word ladder, returns empty list if no words found
   public List<String> getMinLadder(String start, String end) {
      List<String> ladder = new ArrayList<String>();
      if (start.equals(end)) {
         ladder.add(start);
         return ladder;
      }
      else if (start.length() != end.length()) {
         return EMPTY_LADDER;
      }
      else if (!isWord(start) || !isWord(end)) {
         return EMPTY_LADDER;
      }
      Deque<Node> q = new ArrayDeque<>();
      TreeSet<String> f = new TreeSet<>();
      f.add(start);
      q.addLast(new Node(start,null));
      while (!q.isEmpty()) {
         Node n = q.removeFirst();
         String position = n.position;
         for (String neighbor1 : getNeighbors(position)) {
            if (!f.contains(neighbor1)) {
               f.add(neighbor1);
               q.addLast(new Node(neighbor1, n));
            }
            if (neighbor1.equals(end)) {
               Node m = q.removeLast();
               while(m != null) {
                  ladder.add(0, m.position);
                  m = m.predecessor;
               }
               return ladder;
            }
         }
      }
      return EMPTY_LADDER;
   }

//Retrieve neighbors
//Iterate thru lexicon
   @Override
   public List<String> getNeighbors(String word) {
      List<String> neighbors = new ArrayList<String>();
      TreeSet<String> set = new TreeSet<String>();
      
      if (word == null)
         return EMPTY_LADDER;
         
      for (int x = 0; x < word.length(); x++) {
         for (char ch = 'a'; ch <= 'z'; ch++) {
            StringBuilder newWord = new StringBuilder(word);
            newWord.setCharAt(x, ch);
            String newWord1 = newWord.toString();
            if ((isWord(newWord1) == true) & (!newWord1.equals(word)))
               neighbors.add(newWord1);
         }
      }
       
       
      return neighbors;
   }

//return total number of word in the current lexicon
   public int getWordCount() {
      return lexicon.size();
   }

//confirms that word is valid
   public boolean isWord(String str) {
      if (lexicon.contains(str.toLowerCase())) {
         return true;
      }
   //else
      return false;
   }

//confirms that word is valid in word ladder
   public boolean isWordLadder(List<String> sequence) {
      String w = "";
      String ww = "";
   
      if (sequence.isEmpty()) {
         return false;
      }
      for (int i = 0; i < sequence.size() - 1; i ++) {
         w = sequence.get(i);
         ww = sequence.get(i + 1);
         if (!isWord(w) || !isWord(ww)) {
            return false;
         }
         if (getHammingDistance(w, ww) != 1) {
            return false;
         }
      }
      return true;
   }

//constructs a node

   private class Node {
      String position;
      Node predecessor;
   
      public Node(String s, Node pre) {
         position = s;
         predecessor = pre;
      
      }
   }



}

