package data;

import java.util.Map;
import java.util.StringTokenizer;

import states.State;
import tweets.RawTweet;
import words.WordsToSentimentBuilder;

/**
 * The class {@code Tweet} represents geotagged tweet with sentiment score
 * 
 * @author Ira Korshunova
 * 
 */
public class Tweet {

	/** State that tweet originates from */
	private State state;

	/** Sentiment of the tweet */
	private int sentiment;

	/** Indicates if this tweet is meaningful */
	private boolean isMeaningful;

	/** Instance of raw tweet */
	private RawTweet tweet;

	/**
	 * Initializes a newly created {@code Tweet} object
	 * 
	 * @param state
	 *            state of the tweet
	 * @param tweet
	 *            raw tweet
	 */
	public Tweet(State state, RawTweet tweet) {
		this.state = state;
		this.tweet = tweet;
		this.sentiment = calculateSentiment(tweet.getText());
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public void setTweet(RawTweet tweet) {
		this.tweet = tweet;
	}

	public RawTweet getTweet() {
		return tweet;
	}

	public double getSentiment() {
		return sentiment;
	}

	public boolean isMeaningful() {
		return isMeaningful;
	}

	@Override
	public String toString() {
		return new String("state = " + state.name() + " " + sentiment + "   " + tweet.toString());
	}

	/**
	 * Calculates sentiment of the given string as a sum of sentiment scores of
	 * tokens in the string.
	 * 
	 * @param s
	 *            string for which sentiment score is computed
	 * @return sentiment scores
	 */
	private int calculateSentiment(String s) {
		Map<String, Integer> wordsToSentiment = WordsToSentimentBuilder.getWordsSentimentMap();
		String text = s.replaceAll("\\p{Punct}|\\d", "").toLowerCase();
		int sentiment = 0;
		int arousal = 0;
		int currentSentiment = 0;
		StringTokenizer tokenizer = new StringTokenizer(text);

		while (tokenizer.hasMoreTokens()) {
			String firstToken = tokenizer.nextToken();

			if (wordsToSentiment.containsKey(firstToken)) {
				currentSentiment = wordsToSentiment.get(firstToken).intValue();
				sentiment += currentSentiment;
				arousal += Math.abs(currentSentiment);
			}

			if (tokenizer.hasMoreTokens()) {
				String secondToken = tokenizer.nextToken();
				String phrase = firstToken.concat(" " + secondToken);

				if (wordsToSentiment.containsKey(phrase)) {
					currentSentiment = wordsToSentiment.get(phrase).intValue();
					sentiment += currentSentiment;
					arousal += Math.abs(currentSentiment);
				}
				if (wordsToSentiment.containsKey(secondToken)) {
					currentSentiment = wordsToSentiment.get(secondToken).intValue();
					sentiment += currentSentiment;
					arousal += Math.abs(currentSentiment);
				}
			}
			if (arousal == 0)
				isMeaningful = false;
			else
				isMeaningful = true;
		}

		return sentiment;
	}
}
