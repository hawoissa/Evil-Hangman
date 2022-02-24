// Hawo Issa
// CSE 143 EG with Khushi Chaudhari
// Homework42
// HangmanManager is a class that manages the game by keeping track of letters, updating the
// pattern of dashes, and tracking the amount of guesses.
// This manager is "evil" as it is upadating the secret word constantly until it is forced 
// in hopes of the player running out of guesses. 

import java.util.*;
public class HangmanManager {
   
   private int guessCount;
   private String pattern;
   private Set<String> gettableWords;
   private SortedSet<Character> charTrack;
   
   // pre: must be length > 1 and max > 0 (throws IllegalArgumentException otherwise)
   // post: Initliaze the set of words that will be used throughout the game. Each
   //       word will be gathered through the dictionary and must be the desired
   //       length. There will be no duplicates. Each word will be all lowercase.
   // param: Collection<String> dictionary - dictionary of words
   //        int length - length of word, int max - max amount of guesses
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      guessCount = max;
      pattern = "";
      gettableWords = new TreeSet<String>();
      charTrack = new TreeSet<Character>();
      for (String word : dictionary) {
         if (word.length() == length) {
            gettableWords.add(word); 
         }
      }
      for (int i = 0; i < length; i++) {
         pattern += "- ";
      }
   }
   
   // post: returns the set of available words
   public Set<String> words() {
      return gettableWords;
   }
   
   // post: returns the number of guesses left
   public int guessesLeft() {
      return guessCount;
   }
   
   // post: returns the set of letters that have been guessed
   public SortedSet<Character> guesses() {
      return charTrack;
   }
   
   // pre: set of words must not be empty (throw IllegalStateException otherwise)
   // post: returns the current pattern that will be displayed and will acknowledge
   //       the letters that have been guessed.If the correct letters have not been 
   //       guessed, they will be displayed as a dash. There will be spaces seperating each 
   //       letter or dash.
   public String pattern() {
      if (gettableWords.isEmpty()) {
         throw new IllegalStateException();
      }
      return pattern;
   }
   
   // pre: char guess > 1 and set of words are not empty (throws IllegalStateException otherwise)
   //      character cannot be guessed more than once (throws IllegalArgumentException otherwise)
   // post: updates the set of words that will be used by manager.
   //       Records the character guessed by user. Updates the amount of guesses user has.
   //       Returns the number of occurences of the guessed letter in the new pattern.
   // param: char guess - letter guessed by player
   public int record(char guess) {
      if (gettableWords.isEmpty() || guessCount < 1) {
         throw new IllegalStateException();
      }
      if (charTrack.contains(guess)) {
         throw new IllegalArgumentException();
      }
      charTrack.add(guess);
      Map <String, Set<String>> mapWords = new TreeMap<String, Set<String>>();
      for (String word : gettableWords) {
         String newPattern = createPattern(guess, word);
         if (mapWords.containsKey(newPattern)) {
            mapWords.get(newPattern).add(word);         
         } else {
            Set<String> setNot = new TreeSet<String>();
            setNot.add(word);
            mapWords.put(newPattern, setNot);
         }   
      }
      String largestSet = largestSet(mapWords);
      gettableWords = mapWords.get(largestSet);
      pattern = largestSet;  
      return occurences(largestSet, guess);
   } 
   
   // post: returns the new pattern that will be updated using the 
   //       current guess and the new word
   // param: char guess - cleint's guessed letter,
   //        String word - word from set
   private String createPattern(char guess, String word) {
      String newPattern = "";
      for (int i = 0; i < word.length(); i++) {
         int index = i * 2;
         if (guess == word.charAt(i)) {
            newPattern += guess;
         } else { 
            newPattern += pattern.substring(index, index + 1);
         }
         newPattern += " ";
      }
      return newPattern; 
   }
   
   // post: returns the word from the map of words that is the largest
   // param: Map<String, Set<String>> mapWords - map of words that will be used
   private String largestSet(Map<String, Set<String>> mapWords) {
      int max = 0;
      String largestWord = "";
      for (String word : mapWords.keySet()) {
         if (max < mapWords.get(word).size()) {
            max = mapWords.get(word).size();
            largestWord = word;
         }
      }
      return largestWord;
   }
   
   // post: returns the amount of times the guessed letter appears in the word.
   //       Decreases the number of guesses left if the guessed 
   //       letter never appears.
   // param: String word - word from set,
   //        char guess - cleint's guessed letter
   private int occurences(String word, char guess) {
      int count = 0;
      for (int i = 0; i < pattern.length(); i++) {
         if (pattern.charAt(i) == guess) {
            count++;
         }
      }
      if (count == 0) {
         guessCount--;
      }
      return count;
   }
   
}