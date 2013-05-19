package tweets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.gson.Gson;

/**
 * The {@code TweetStreamParser} class represents parser of the twitter stream.
 * 
 * @author Ira Korshunova
 * 
 */
public class TweetStreamParser {
	/** List of raw tweets in English */
	private List<RawTweet> rawTweets;

	/**
	 * Parses twitter stream file into a list of English tweets
	 * 
	 * @param twitterStreamFile
	 *            file with Twitter's data
	 * @throws LangDetectException
	 *             if language profiles can't be loaded
	 * @throws FileNotFoundException
	 *             if twitterStream file can't be found
	 */
	public TweetStreamParser(File twitterStreamFile) throws LangDetectException,
			FileNotFoundException {
		
		DetectorFactory.loadProfile(new String("profiles"));
		rawTweets = new ArrayList<RawTweet>();
		Scanner scn = new Scanner(twitterStreamFile).useDelimiter("\r\n");
		Gson gson = new Gson();
		while (scn.hasNext()) {
			String line = scn.nextLine();
			if (line.startsWith("{\"delete\""))
				continue;
			RawTweet rawTweet = gson.fromJson(line, RawTweet.class);
			if (isInEnglish(rawTweet)) {
				rawTweet.setLanguage("en");
				rawTweets.add(rawTweet);
			}
		}
		scn.close();
	}

	public List<RawTweet> getListOfTweets() {
		return rawTweets;
	}

	/**
	 * Checks whether the specified tweet is in English. Uses
	 * {@link RawTweet#lang} to identify language, but if it's {@code null}
	 * language is detected with the use of library.
	 * 
	 * @see <a
	 *      href="https://code.google.com/p/language-detection/">Language_Detection_Library_for_Java</a>
	 * 
	 * @param tweet
	 *            tweet which language is to be detected
	 * @return true if the tweet's text is in English else return false
	 */
	private boolean isInEnglish(RawTweet tweet) {
		if (tweet.getLanguage() != null && tweet.getLanguage().equals("en")) {
			return true;
		}
		String filteredText = filterText(tweet);
		try {
			Detector detector = DetectorFactory.create();
			detector.append(filteredText);
			String lang = detector.detect();
			if (lang.equals("en"))
				return true;
		} catch (LangDetectException e) {
			return false;
		}
		return false;
	}

	/**
	 * Filters tweets text from urls, user mentions and hashtags.
	 * 
	 * @param rawTweet
	 *            tweet which text is filtered
	 * @return filtered text
	 */
	private String filterText(RawTweet rawTweet) {
		String filteredText = rawTweet.getText();

		for (String url : rawTweet.getEntities().getUrls()) {
			filteredText = filteredText.replace(url, "");
		}
		for (String user : rawTweet.getEntities().getUserMentions()) {
			filteredText = filteredText.replace(user, "");
		}

		for (String hashTag : rawTweet.getEntities().getHashtags()) {
			filteredText = filteredText.replace(hashTag, "");
		}
		filteredText = filteredText.replaceAll("\\p{Punct}|\\d", "");
		return filteredText;
	}
}