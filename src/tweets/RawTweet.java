package tweets;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * The class {@code RawTweet} represents incomplete tweet (some of the fields
 * are omitted in comparison to the original tweets from twitter stream)
 * 
 * @see <a
 *      href="https://dev.twitter.com/docs/platform-objects/tweets">Twitter_documentation:Tweets</a>
 * 
 * @author Ira Korshunova
 * 
 */
public class RawTweet {
	/**
	 * {@code Gson} object for serialization of {@code RawTweet} object into its
	 * equivalent Json representation
	 */
	private static Gson gson = new Gson();

	/** Text of the tweet */
	private String text;

	/** Language of the tweet's text */
	private String lang;

	/** The user who posted this tweet */
	private Users user = new Users();

	/** Geographic location of this tweet */
	private Coordinates coordinates = new Coordinates();

	/** Place which is associated with this tweet */
	private Places place = new Places();

	/** Entity which includes urls, hashtags and user mentions*/
	 private Entities entities = new Entities();

	/**
	 * Doesn't allow to instantiate this class without Gson
	 */
	private RawTweet() {

	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public Places getPlace() {
		return place;
	}

	public Users getUser() {
		return user;
	}

	public String getLanguage() {
		return lang;
	}

	public void setLanguage(String lang) {
		this.lang = lang;
	}

	public String getText() {
		return text;
	}

	public Entities getEntities() {
		return entities;
	}

	@Override
	public String toString() {
		return gson.toJson(this);
	}

	/**
	 * The class {@code Users} represents reduced Twitter's Users class
	 * 
	 * @see <a
	 *      href="https://dev.twitter.com/docs/platform-objects/users">Twitter_documentation:Users</a>
	 * 
	 */
	public class Users {
		/** The user-defined location for the account's profile */
		private String location;

		public void setLocation(String location) {
			this.location = location;
		}

		public String getLocation() {
			return location;
		}

	}

	/**
	 * The class {@code Coordinates} represents geographic location of this
	 * Tweet
	 * 
	 */
	public class Coordinates {
		/**
		 * Coordinates in geoJSON format. First coordinate is logitude, second
		 * is latitude.
		 */
		private double[] coordinates;

		public void setCoordinates(double[] coordinates) {
			this.coordinates = coordinates;
		}

		public double[] getCoordinates() {
			return coordinates;
		}

	}

	/**
	 * The class {@code Places} represents reduced Twitter's Places class
	 * 
	 * @see <a
	 *      href="https://dev.twitter.com/docs/platform-objects/places">Twitter_documentation:Places</a>
	 * 
	 */
	public class Places {
		/**
		 * Full name of the place which might include name of the city and
		 * state's abbreviation
		 */
		private String full_name;

		public void setFullName(String full_name) {
			this.full_name = full_name;
		}

		public String getFullName() {
			return full_name;
		}
	}
	/**
	 * The class {@code Entities} represents modified Twitter's Entity class
	 * 
	 * @see <a
	 *      href="https://https://dev.twitter.com/docs/platform-objects/entities">Twitter_documentation:Entities</a>
	 * 
	 */
	
	public class Entities {
		private Hashtag[] hashtags;
		private Url[] urls;
		private UserMention[] user_mentions;

		public String[] getHashtags() {
			List<String> list = new ArrayList<String>();
			for (Hashtag Hashtag : hashtags) {
				list.add(Hashtag.text);
			}
			return list.toArray(new String[]{});
		}

		public String[] getUrls() {
			List<String> list = new ArrayList<String>();
			for (Url url : urls) {
				list.add(url.url);
			}
			return list.toArray(new String[]{});
		}

		public String[] getUserMentions() {
			List<String> list = new ArrayList<String>();
			for (UserMention userMention : user_mentions) {
				list.add(userMention.screen_name);
			}
			return list.toArray(new String[]{});
		}

		public class Hashtag {
			private String text;
		}

		public class Url {
			private String url;
		}

		public class UserMention {
			private String screen_name;
		}
	}
}
