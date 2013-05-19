package data;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import states.State;
import tweets.RawTweet;

/**
 * The {@code StateToTweetsMapBuilder} class contains methods for creating data
 * set of geotagged tweets from raw tweets.
 * 
 * @author Ira Korshunova
 * 
 */
public class StateToTweetsBuilder {
	/** Mapping from US state to the list of tweets associated with it */
	private Map<State, List<Tweet>> stateToTweets;

	/**
	 * Creates mapping from state to the list of tweets associated with it based
	 * on raw twitter data
	 * 
	 * @param rawTweetsList
	 *            list of raw tweets
	 */
	public StateToTweetsBuilder(List<RawTweet> rawTweetsList) {
		stateToTweets = new EnumMap<State, List<Tweet>>(State.class);

		for (RawTweet tweet : rawTweetsList) {
			State state = determineState(tweet);
			Tweet instance = new Tweet(state, tweet);
			if (instance.isMeaningful()) {
				if (!stateToTweets.containsKey(state)) {
					ArrayList<Tweet> tweetsList = new ArrayList<Tweet>();
					tweetsList.add(instance);
					stateToTweets.put(state, tweetsList);
				} else {
					stateToTweets.get(state).add(instance);
				}
			}
		}
	}

	/**
	 * Returns mapping from state to the list of tweets, which probably were
	 * originated from it
	 * 
	 * @return map from state to the tweets
	 */
	public Map<State, List<Tweet>> getStateToTweetsMap() {
		return stateToTweets;
	}

	/**
	 * Determines the state of tweet based on coordinates, places full name and
	 * user's location fields. The result may be inaccurate due to:
	 * <ul>
	 * <li>approximate state boundaries</li>
	 * <li>place does not necessarily define the originated place of the tweet</li>
	 * <li>fuzziness of user-defined location for the account's profile</li>
	 * </ul>
	 * If no such information is given then state is undefined
	 * 
	 * @param tweet
	 *            raw tweet from which its origin is determined
	 * @return state associated with the given tweet
	 */
	private State determineState(RawTweet tweet) {
		if (tweet.getCoordinates() != null) {
			double lng = tweet.getCoordinates().getCoordinates()[0]; // y
			double lat = tweet.getCoordinates().getCoordinates()[1]; // x
			return State.getStateOfGeoLocation(lat, lng);
		}

		if (tweet.getPlace() != null) {
			String text = tweet.getPlace().getFullName();
			State state = State.abbreviationToState(text.substring(text.length() - 2));
			if (state == null)
				return State.Undefined;
			else
				return state;
		}

		if (tweet.getUser().getLocation() != null) {
			String location = tweet.getUser().getLocation();
			return getProbableState(location);
		}

		return State.Undefined;
	}

	/**
	 * Searches the state name and abbreviation in the given string
	 * 
	 * @param s
	 *            the string to be examined
	 * @return state which is mentioned in the given string
	 */
	private State getProbableState(String s) {
		for (State state : State.values()) {
			if (s.contains(state.name()) || s.contains(state.getAbbreviation().toUpperCase()))
				return state;
		}
		return State.Undefined;
	}
}