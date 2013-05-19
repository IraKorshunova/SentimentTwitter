package states;

import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The enum {@code State} represents 50 USA states plus Undefined state.
 * 
 * @author Ira Korshunova
 * 
 */
public enum State {
	Alaska, Alabama, Arkansas, Arizona, California, Colorado, Connecticut, Delaware, Florida, Georgia, Hawaii, Iowa, Idaho, Illinois, Indiana, Kansas, Kentucky, Louisiana, Massachusetts, Maryland, Maine, Michigan, Minnesota, Missouri, Mississippi, Montana, NorthCarolina, NorthDakota, Nebraska, NewHampshire, NewJersey, NewMexico, Nevada, NewYork, Ohio, Oklahoma, Oregon, Pennsylvania, RhodeIsland, SouthCarolina, SouthDakota, Tennessee, Texas, Utah, Virginia, Vermont, Washington, Wisconsin, WestVirginia, Wyoming, Undefined;

	/** Boundary of the state */
	private Polygon boundary;
	
	/** Abbreviation of the state */
	private String abbreviation;
	
	/**Mapping from abbreviation to state*/
	private static Map<String, State> abbreviationToState;
	
	/**Scaling factor for boundary coordinates*/
	private final static int coordinatesScaleFactor = 10000;

	public Polygon getBoundary() {
		return boundary;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Determines the state to which the point belongs
	 * @param lat point's latitude
	 * @param lng point's longitude
	 * @return state of the  given point
	 */
	public static State getStateOfGeoLocation(double lat, double lng) {
		lat *= coordinatesScaleFactor;
		lng *= coordinatesScaleFactor;
		for (State state : State.values()) {
			if (state.boundary.contains(lat, lng))
				return state;
		}
		return State.Undefined;
	}

	/**
	 * Returns the state, which corresponds to the given abbreviation
	 * @param abbreviation state's abbreviation
	 * @return state associated with abbreviation
	 */
	public static State abbreviationToState(String abbreviation) {
		return abbreviationToState.get(abbreviation);
	}

	static {
		try {
			initializeStateCoordinates();
			initailizeStateAbbreviation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads file with state boundaries and builds corresponding polygon
	 * for each state.
	 * 
	 * @throws Exception if XML parser doesn't work
	 */
	private static void initializeStateCoordinates() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		builder = factory.newDocumentBuilder();
		File input = new File("states.xml");
		Document doc = builder.parse(input);
		NodeList nodeStates = doc.getElementsByTagName("state");
		for (int i = 0; i < nodeStates.getLength(); i++) {
			Node nodeState = nodeStates.item(i);
			String name = nodeState.getAttributes().getNamedItem("name").getNodeValue()
					.replace(" ", "");
			State state = State.valueOf(name);
			Polygon boundary = new Polygon();
			NodeList points = ((Element) nodeState).getElementsByTagName("point");
			for (int j = 0; j < points.getLength(); j++) {
				Node point = points.item(j);
				int lat = (int) (coordinatesScaleFactor * Double.parseDouble(point.getAttributes()
						.getNamedItem("lat").getNodeValue()));
				int lng = (int) (coordinatesScaleFactor * Double.parseDouble(point.getAttributes()
						.getNamedItem("lng").getNodeValue()));
				boundary.addPoint(lat, lng);
			}
			state.boundary = boundary;
		}
		State.Undefined.boundary = new Polygon(new int[] { 0 }, new int[] { 0 }, 1);
	}

	/**
	 * Builds correspondence between state and its abbreviation 
	 * @throws FileNotFoundException
	 */
	private static void initailizeStateAbbreviation() throws FileNotFoundException  {
		abbreviationToState = new HashMap<String, State>();
		File input = new File("state-abbrev.txt");
		Scanner scn = new Scanner(input);
		while (scn.hasNext()) {
			StringTokenizer t = new StringTokenizer(scn.nextLine());
			StringBuilder stateName = new StringBuilder();
			int numberOfTokens = t.countTokens() - 1;
			for (int i = 0; i < numberOfTokens; i++) {
				stateName.append(t.nextToken());
			}
			String abbrev = t.nextToken();
			State state = State.valueOf(stateName.toString());
			state.abbreviation = abbrev;
			abbreviationToState.put(abbrev, state);
		}
		State.Undefined.abbreviation = "Undefined";
		scn.close();
	}

	@Override
	public String toString() {
		String[] nameParts = super.toString().split("(?=[A-Z])");
		StringBuilder sb = new StringBuilder(nameParts[0]);
		for (int i = 1; i < nameParts.length; i++) {
			sb.append(nameParts[i]);
			if (i != nameParts.length - 1)
				sb.append(" ");
		}
		return sb.toString();
	}
}