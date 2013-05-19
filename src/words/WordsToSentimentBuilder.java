package words;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The class {@code WordsToSentimentBuilder} contains methods for dealing with
 * words' sentiments data
 * 
 * @author Ira Korshunova
 * 
 */
public class WordsToSentimentBuilder {
	/** Mapping from words to their sentiments */
	private static Map<String, Integer> wordsToSentiment;

	/**
	 * Reads file with words(phrases)-sentiments pairs and creates the
	 * corresponding map
	 * 
	 * @param wordsSentimentFile
	 *            file to be read
	 * @throws FileNotFoundException
	 *             if the file can't be found
	 */
	public static void loadWordsSentiment(File wordsSentimentFile) throws FileNotFoundException {
		wordsToSentiment = new HashMap<String, Integer>();
		Scanner scn = null;
		scn = new Scanner(wordsSentimentFile);
		while (scn.hasNext()) {
			String line = scn.nextLine();
			String[] s = line.split("\\s+");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < s.length - 1; i++) {
				sb.append(s[i]);
				if (i != s.length - 2)
					sb.append(" ");
			}
			Integer number = Integer.valueOf(s[s.length - 1]);
			wordsToSentiment.put(sb.toString(), number);
		}
		scn.close();
	}

	public static Map<String, Integer> getWordsSentimentMap() {
		if (wordsToSentiment == null) {
			throw new IllegalStateException("File with words was not loaded!");
		}
		return wordsToSentiment;
	}

}
