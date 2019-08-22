/**
 * Name: Jeromey Klein
 * File: WordDestroyer.java
 * Date: December 24, 2018
 */

import java.util.*;
import java.io.*;

/**
 * This program find all words from a given letter combination.
 */
public class WordDestroyer {
  
  // Constants
  private static final int DICT_SIZE = 466544, MIN_LENGTH = 3;
  private static final char SPACE_CHAR = ' ';
  private static final String DICT_FILE = "dict.txt",
    
    // Usage message
    USAGE_MSG = "Usage:\n  Enter a series of characters to generate words,\n"
      + "  Enter key characters with spaces to find particular words:\n",

    // Dictionary message
    WORDS_MSG = "\nYour dictionary has eliminated %d possibilitie(s):\n\n",
    
    // Search message
    SEARCH_MSG = "\nYou have found %d word(s):\n\n";

  /**
   * Takes input and prints results.
   *
   * @param args (not used)
   */
  public static void main(String[]args) {
    
    // Fill dictionary
    String[] dict = getDict();
    Arrays.sort(dict);
    
    // Declare word array and scanner
    ArrayList<String> words = new ArrayList<String>();
    Scanner input = new Scanner(System.in);

    // Print usage
    System.out.println(USAGE_MSG);

    // Main loop
    while (input.hasNextLine()) {

      // Get input
      String chars = input.nextLine();

      // Check search
      if (!chars.contains("" + SPACE_CHAR)) {
        
        // Clear words
        words.clear();
        
        // Generate combinations
        getCombos("",chars, words);

        // Check length and repeats
        for (int i = words.size() - 1; i >= 0; i--) {
          if (words.get(i).length() < MIN_LENGTH ||
              words.subList(0, i).contains(words.get(i))) {
            words.remove(i);
          }
        }
        // Save initial size
        int size = words.size();

        // Remove non-words
        for (int i = words.size() - 1; i >= 0; i--) {
          if (!binarySearch(0, dict.length, dict, words.get(i))) {
            words.remove(i);
          }
        }
        // Sort alphabetically and by length
        Collections.sort(words, (w1, w2) -> w1.compareTo(w2));
        Collections.sort(words, (w1, w2) -> w1.length() - w2.length());
        
        // Print results
        for (int i = 0; i < words.size(); i++) {
          System.out.println(words.get(i));
        }
        System.out.format(WORDS_MSG, size - words.size());
      } else {
        
        // Instantiate counter
        int total = 0;

        // For each word
        for (int i = 0; i < words.size(); i++) {
          
          // Check length
          if (words.get(i).length() == chars.length()) {
            
            // Check equal
            boolean same = true;
            int j = 0;
            while (same && j < chars.length()) {
              if (chars.charAt(j) != SPACE_CHAR &&
                  words.get(i).charAt(j) != chars.charAt(j)) {
                same = false;
              }
              j++;
            }
            // Print word
            if (same) {
              System.out.println(words.get(i));
              total++;
            }
          }
        }
        // Print message
        System.out.format(SEARCH_MSG, total);
      }
    }
  }

  /**
   * Returns the dictionary to be used to find words.
   *
   * @return array of words
   */
  private static String[] getDict() {
    
    // Instantiate array
    String[] dict = new String[DICT_SIZE];
    
    // Instantiate scanner
    Scanner dictScan = null;
    try {
      dictScan = new Scanner(new FileReader(DICT_FILE));
    } catch (IOException e) {}

    // Read file
    for (int i = 0; i < dict.length; i++) {
      dict[i] = dictScan.nextLine().toLowerCase();
    }

    // Return array
    return dict;
  }
  
  /**
   * Fills a list with all the possible combinations of a string of chars.
   *
   * @param root used in all of the following combinations
   * @param chars used to generate combinations
   * @param combos list to fill with combinations
   */
  private static void getCombos(String root, String chars, 
    ArrayList<String> combos) {
    
    // For each character in chars
    for (int i = 0; i < chars.length(); i++) {
      
      // Add character to root
      String newRoot = root + chars.charAt(i);
      
      // Remove character from chars
      String newChars = chars.replaceFirst("" + chars.charAt(i), "");
      
      // Add root to combos
      combos.add(newRoot);

      // Use root to create new branch
      getCombos(newRoot, newChars, combos);
    }
  }
  
  /**
   * Implements binary search algorith to check if a sorted array contains a
   * perticular element.
   *
   * @param start start of array
   * @param end end of array
   * @param words array to seach
   * @param word element for which to search
   * @return true if array contains element
   */
  private static boolean binarySearch(int start, int end, String[] words,
      String word) {

    // Base cases
    if (start == end) {
      return false;
    } else if (end - start == 1) {
      return word.equals(words[start]);
    }
    // Calculate mid point
    int mid = start + ((end - start) / 2);
    
    // Get equality
    int equal = word.compareTo(words[mid]);
    
    // Check upper half
    if (equal < 0) {
      return binarySearch(start, mid, words, word);
    
    // Check lower half
    } else if (equal > 0) {
      return binarySearch(mid + 1, end, words, word);
    
    // If midpoint happens to be equal
    } else {
      return true;
    }
  }
}
