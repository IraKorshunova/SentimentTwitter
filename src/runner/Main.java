package runner;
import java.io.File;
import java.io.FileNotFoundException;

import tweets.TweetStreamParser;
import words.WordsToSentimentBuilder;
import data.StateToTweetsBuilder;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {

			try {
				File wordsSentiment = new File(args[0]);
				WordsToSentimentBuilder.loadWordsSentiment(wordsSentiment);
			
				File twitterStream = new File(args[1]);
				TweetStreamParser tweetParser = new TweetStreamParser(twitterStream);
				StateToTweetsBuilder datasetBuilder = new StateToTweetsBuilder(tweetParser.getListOfTweets());
				String abbrev =  HappyCalculator.getHappiestStateAbbreviation(datasetBuilder.getStateToTweetsMap());
				System.out.println(abbrev);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}