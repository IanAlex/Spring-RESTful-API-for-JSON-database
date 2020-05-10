package com.ija.tacosproject;

import org.springframework.stereotype.Service;

import org.json.JSONObject;
import org.json.JSONException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * This service class was written for Challenge 2.
 * The public methods in this service class are invoked by the methods in the
 *  StringParsingController REST controller class.
 *  Please see comments in each method as to how the method works.
 */

@Service
public class StringParsingService {
	
	// the string of eligible characters as described in Challenge 2
	private final static String ELIGIBLECHARSTRING =  "RSTLNAEIOU";
	
	// textSample text was supplied in the _notTacos folder of the taco-master folder
	// and was copied to the folder of the PWVTacos project for use in Challenge 2
	public final static String TEXTSAMPLEFILE = "textSample.txt"; 
	
	// JSON output file as specified in Challenge 2.   
	public final static String JSONOUTPUTFILE = " textSampleResults.json"; 
	
	public String generateJsonBlockFromInputText(String inputText) {
		// this method is invoked in the StringParsingController REST Controller where the input 
		// text is in the Request Body.
		// Logic: loop in the lines, populate the wordList for each line populateListFromLine()
		// and then generate the Json result using getWordStatsFromList().
		if (inputText != null && inputText.length() > 0) {
			List<String> wordList = new ArrayList<>();
			String[] lines = inputText.split("\\n");
			for (String line : lines) {
				populateListFromLine(line, wordList);
			}
			String jsonStr = getWordStatsFromList(wordList);
			return jsonStr;
		}
		return "";
	}
	
	public String generateJsonFileFromParsedTextFileInApp()  {
		// this method is invoked in the StringParsingController REST Controller where no request body nor input
		// file is supplied.   In this case we use the supplied "textSample.txt" 
		// (from _notTacos folder of the taco-master folder) which is under the main folder for this
		// project.
		// The method will call generateJsonFileFromParsedTextFile() using the supplied file above
		return generateJsonFileFromParsedTextFile(TEXTSAMPLEFILE);
	}
	
	public String generateJsonFileFromParsedTextFile(String textFilePathStr)  {
		// This method is invoked from the StringParsingController REST Controller
		// If the argument textFilePathStr represents a path to a valid file then
		// we read in each line and invoke populateListFromLine() to add to the wordList
		// from that line.
		// After the wordList is populated we invoke getWordStatsFromList() to get the 
		// stats as a Json string.
		// Finally we write the Json string to a textSampleResults.json file on the SAME PATH
		// as the input file.
		// If the textFilePathStr does not represent a valid path to an input file then
		// return "not found".
		BufferedReader br = null;
		List<String> wordList = new ArrayList<>();
		File file = new File (textFilePathStr);
		if (!file.exists() || file.isDirectory()) {
			System.out.println("Text file " + file.getAbsolutePath() + " does not exist");
			return "not found";
		}
		try {
		   
	       br = new BufferedReader(new FileReader(textFilePathStr));
	       String line;
           while ((line = br.readLine()) != null) {
        	   populateListFromLine(line, wordList);
           }
		} catch (IOException e) {
			e.printStackTrace();
        } finally {
        	if (br != null) {
        		try {
        		  br.close();
        		}
        		catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
		String jsonStr = getWordStatsFromList(wordList);
		File outFile = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf(file.getName()) ) + "//" + JSONOUTPUTFILE);
		try (PrintWriter out = new PrintWriter(outFile)) {
		    out.println(jsonStr);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "done";
	}
	
	private void populateListFromLine(String line, List<String> wordList) {
		// from a line of text we split it into words and add to the word list.
		// words are split by whitespace, question mark, period, comma, exclamation mark,
		// colon, semi-colon and hyphen
		if (line != null && line.length() > 0) {
			String[] words = line.split("\\s|\\?|\\.|\\!|\\:|\\;|\\-");
			for (String word : words) {
				wordList.add(word);
			}
		}
	}
	
	private String getWordStatsFromList(List<String> wordList) {
		// Using the above wordList, we iteratively examine words in the
		// list to see if they're retained or not
		// if the word (ignoring case) consists of only characters in "RSTLNAEIOU" (ELIGIBLECHARSTRING variable)
		// and at most 1 other letter then it is retained.  If the word was previously used or removed
		// then we discard it from the list (no duplicates as specified).  If the same word
		// uses caps (non caps) different to an earlier version of that word then the earlier
		// version stays in the word list while the later version is discarded. We then
		// order the remaining words in the word list by length of word.
		// Also: keep stats of word usage to find most-used words
		// How this is implemented:
		// (1) retain lists of used and exclude words in caps
		// (2) strip the current word of quotes (stripQuotes() call)
		// (3) if capitalized version of current word is in either list in (1) then remove it
		// (4) run checkIfEligible() on current word (if not in either of lists in (1)
		//  to see if it qualifies and if it does not then discard from the list
		// (5) keep a wordToFreqMap which maps word to frequency.
		// (6) if a word was previously used (discard from list) then we increment its 
		//     frequency in wordToFreqMap; if it is a new word then set its frequency to 1 in wordToFreqMap
		// (7) order the words remaining in the wordList by length of word
		// (8) obtain the word used most frequently along with number of times used
		// (9) return the ordered (by length) word list and most frequently used word and number of
		//     times used as a JSON string
		if (wordList != null && !wordList.isEmpty()) {
			List<String> wordsUsedCaps = new ArrayList<>(); 
			List<String> wordsExcludedCaps = new ArrayList<>(); 
			ListIterator<String> iterator = wordList.listIterator();
			Map<String, Integer> wordToFreqMap = new HashMap<>();
			// iterate on word List using listIterator to enable edits and removals
			while (iterator.hasNext()) {
				String origWord = iterator.next();
				// remove quotes from word e.g. change parents' to parents; 
				// change children's to children; change mark'd to mark
				String curWord = stripQuotes(origWord);
				if (!curWord.equals(origWord)) {
					iterator.set(curWord);
				}
				String curWordCaps = curWord.toUpperCase();
				// remove words previously used (to prevent duplicates) or previously
				// excluded (compare capitalized version)
				if (wordsExcludedCaps.contains(curWordCaps)) {
					iterator.remove();
				}
				else if (wordsUsedCaps.contains(curWordCaps)) {
					// if word was previously used then update wordToFreqMap to increment
					// its usage frequency
					Set<String> wordKeys = wordToFreqMap.keySet();
					for (String word : wordKeys) {
						if (curWord.equalsIgnoreCase(word)) {
							wordToFreqMap.put(word, wordToFreqMap.get(word).intValue() + 1);
							break;
						}
					}
					iterator.remove();
				}
				else {
					// invoke checkIfEligible() with algorithm described in Challenge 2 to see if word not
					// previously used/excluded should be kept.  If kept in list 
					// then add to wordsUsedCaps list; if not qualified then add to
					// wordsExcludedCaps list to prevent checkIfEligible() having to be 
					// called again
					if (checkIfEligible(curWordCaps)) {
						wordsUsedCaps.add(curWordCaps);
						wordToFreqMap.put(curWord, 1);
					}
					else {
						wordsExcludedCaps.add(curWordCaps);
						iterator.remove();
					}
				}
			}
			// sort words in list in order of length
			wordList.sort(Comparator.comparingInt(String::length));
			// sort wordToFreqMap by value (frequency) and choose the last sorted map element
			// to get most frequently used word
			List<Map.Entry<String, Integer>> mapEntryWtfList = new ArrayList<>(wordToFreqMap.entrySet());
			mapEntryWtfList.sort(Map.Entry.comparingByValue());
			Map<String, Integer> sortedWordToFreqMap = new LinkedHashMap<>();
			sortedWordToFreqMap.put(mapEntryWtfList.get(mapEntryWtfList.size()-1).getKey(), mapEntryWtfList.get(mapEntryWtfList.size()-1).getValue());	
			// set up Json object to be returned as string
			// the code below is self-explaining
			JSONObject json = new JSONObject();
			try {
				json.put("remaining words ordered by length", wordList);
				json.put("most used word", mapEntryWtfList.get(mapEntryWtfList.size()-1).getKey());
				json.put("number of uses", mapEntryWtfList.get(mapEntryWtfList.size()-1).getValue());
			} catch(JSONException je) {
				je.printStackTrace();
			}
			return json.toString();
			
		}
		return "";
	}
	
	private String stripQuotes(String word) {
		// remove quotes from word e.g. change parents' to parents; 
		// change children's to children; change mark'd to mark
		String workWord = new String (word);
		if (workWord != null && workWord.length() > 1) {
			if (workWord.endsWith(new String("\'")) || workWord.endsWith(new String("\""))) {
				workWord = workWord.substring(0, workWord.length()-1);
			}
			else if (workWord.endsWith(new String("\'s")) || workWord.endsWith(new String("\'d")) ) {
				workWord = workWord.substring(0, workWord.length()-2);
			}
			if (workWord.startsWith(new String("\'")) || workWord.startsWith(new String("\"")) && workWord.length() > 1) {
			    workWord = workWord.substring(1);
			}
		}
	    return workWord;
	}
	
	private boolean checkIfEligible(String word) {
		// Use algorithm described in Challenge 2 to see if word is eligible and return true or false
		// if the word (ignoring case) consists of only characters in "RSTLNAEIOU" (ELIGIBLECHARSTRING variable)
		// and at most 1 other letter then its eligible (return true.  Otherwise return false
		if (word != null && word.length() > 0) {
			char otherChar = Character.MIN_VALUE;
			for (int i = 0; i < word.length(); i++) {
				char curChar = word.charAt(i);
				if (curChar != '\'' && curChar != '\"'  && ELIGIBLECHARSTRING.indexOf(curChar) < 0) {
					if (otherChar != Character.MIN_VALUE && curChar != otherChar) {
						return false;
					}
					else if (otherChar == Character.MIN_VALUE ) {
						otherChar = curChar;
					}
				}
			}
		}
		return true;
		
	}

}
