package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import states.State;
import data.Tweet;

public class HappyCalculator {

	/** Mapping from state to its sentiment */
	private static Map<State, Double> stateToHappiness;

	/**
	 * Calculates overall sentiment of each state based on the sentiments of all
	 * tweets associated with the state and returns the abbreviation of the
	 * happiest state in USA. In parallel it writes data(state-happiness) to
	 * file, which subsequently can be processed in order to obtain distribution
	 * of happiness across states.
	 * 
	 * @param stateToTweets
	 *            mapping from state to its list of tweets
	 * @return abbreviation of the happiest state
	 * @throws FileNotFoundException
	 *             if some error occurs while creating file
	 */
	public static String getHappiestStateAbbreviation(Map<State, List<Tweet>> stateToTweets)
			throws FileNotFoundException {
		stateToHappiness = new EnumMap<State, Double>(State.class);
		PrintWriter writer = new PrintWriter(new File("sentiments.txt"));

		for (Entry<State, List<Tweet>> s : stateToTweets.entrySet()) {
			double sentiment = 0;
			for (Tweet t : s.getValue()) {
				sentiment += t.getSentiment();
			}
			sentiment /= s.getValue().size();

			if (s.getKey() != State.Undefined) {
				stateToHappiness.put(s.getKey(), sentiment);
				writer.println(s.getKey().toString() + "," + sentiment);
			}
		}
		writer.close();

		Map.Entry<State, Double> maxEntry = null;
		for (Entry<State, Double> entry : stateToHappiness.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}

		return maxEntry.getKey().getAbbreviation();
	}

}
