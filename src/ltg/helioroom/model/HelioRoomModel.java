package ltg.helioroom.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ltg.commons.phenomena_handler.PhenomenaXMLUtils;

import org.dom4j.DocumentException;
import org.dom4j.Element;

public class HelioRoomModel {

	// Planets representation constants
	public final static String REP_IMAGE 		= "image";
	public final static String REP_SPHERE 		= "sphere";

	// Planets names constants
	public final static String LABEL_NONE 		= "none";
	public final static String LABEL_NAME 		= "name";
	public final static String LABEL_COLOR 		= "color";
	
	// State constants
	public final static String STATE_RUNNING	= "running";
	public final static String STATE_PAUSED 	= "paused";

	// Simulation data
	private long startTime = -1;
	private String state = null;
	private int viewAngleBegin = -1;
	private int viewAngleEnd = -1;
	private List<Planet> planets = null;
	
	// Other data
	private boolean initialized = false;
	
	
	public synchronized double getStartTime() {
		return startTime;
	}

	public synchronized String getState() {
		return state;
	}

	public synchronized double getViewAngleBegin() {
		return viewAngleBegin;
	}

	public synchronized double getViewAngleEnd() {
		return viewAngleEnd;
	}

	public synchronized List<Planet> getPlanets() {
		return planets;
	}


	public synchronized void init(Element e) {
		try {
			startTime = PhenomenaXMLUtils.parseIntElement(e, "startTime");
			state = parseStateElement(e, "state");
			viewAngleBegin = PhenomenaXMLUtils.parseIntElement(e, "viewAngleBegin");
			viewAngleEnd = PhenomenaXMLUtils.parseIntElement(e, "viewAngleEnd");
			planets = parsePlanets(e, "planets");
		} catch (DocumentException ex) {
			System.err.println("Errors initializing HelioRoom model");
			System.exit(-1);
		}
		initialized = true;
	}
	
	
	public synchronized boolean isInitialized() {
		return initialized;
	}
	
	
	public synchronized double getViewAngle() {
		int w = viewAngleEnd - viewAngleBegin;
		if (w > 0)
			return w;
		else
			return (360 + w);
	}


	private String parseStateElement(Element e, String element) throws DocumentException {
		String state = PhenomenaXMLUtils.parseStringElement(e, element);
		if (state.equals(STATE_RUNNING))
			return STATE_RUNNING;
		if (state.equals(STATE_PAUSED))
			return STATE_PAUSED;
		throw new DocumentException();
	}


	private List<Planet> parsePlanets(Element e, String element) throws DocumentException {
		List<Planet> plans = new ArrayList<Planet>();
		List<Element> planetsElements = PhenomenaXMLUtils.parseListElement(e, element);
		for (Element pl : planetsElements)
			plans.add(new Planet(pl));
		Collections.reverse(plans);
		return plans;
	}
	

}
