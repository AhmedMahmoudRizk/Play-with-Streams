package play_java_websocket_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

public class DataHandler extends TimerTask {

	private int MONTH_LENGTH = 32;
	private int YEAR_LENGTH = 13;
	private int[] avrgMinutePerTrip = new int[3];
	private int[] dropOff = new int[3];
	private int[][][] madBro = new int[YEAR_LENGTH][MONTH_LENGTH][3];
	private int[][] avrgvendPerDay = new int[YEAR_LENGTH][MONTH_LENGTH];
	private int[][] tripsPerDay = new int[YEAR_LENGTH][MONTH_LENGTH];
	private ArrayList<Trip>[][] trips;
	private ArrayList<Location> locations;
	private ResultHandler resultHandler;
	private int totalRecords;
	private int givenMonth = 0;
	private static DataHandler dataHandler;

	private DataHandler() {
		initData();
		resultHandler = new ResultHandler();
		// TODO Auto-generated constructor stub
	}

	/**
	 * use singleton design pattern for this class cause it doesn't need to create
	 * more than one object of this class
	 */

	public static DataHandler getInstance() {

		if (dataHandler == null)
			dataHandler = new DataHandler();

		return dataHandler;
	}

	private void initData() {
		readLocations();
		totalRecords = 0;
		trips = new ArrayList[YEAR_LENGTH][MONTH_LENGTH];
		for (int i = 0; i < YEAR_LENGTH; i++) {
			for (int j = 0; j < MONTH_LENGTH; j++)
				trips[i][j] = new ArrayList<Trip>();
		}
		for (int i = 0; i < YEAR_LENGTH; i++) {
			for (int j = 0; j < MONTH_LENGTH; j++) {
				tripsPerDay[i][j] = 0;
				avrgvendPerDay[i][j] = 0;

			}
		}

	}

	/**
	 * read the taxi zones and store them in location data structure
	 */
	private void readLocations() {
		File file = new File("taxi_zones_simple.csv");

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			locations = new ArrayList<>();
			try {
				while ((st = br.readLine()) != null) {
					String[] stSplited = st.split(",");
					Location location = new Location(stSplited[0], stSplited[1], stSplited[2]);
					locations.add(location);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * parse the incoming record and extract the used data and store them in Trips
	 * array list
	 * 
	 * assume that the number of received records equal the number of trips
	 */
	public void SetTrip(String message) {
		JSONObject obj;
		Trip trip = new Trip();
		try {
			obj = new JSONObject(message);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				// System.out.println(message);
				totalRecords++;
				trip.setTripType(obj.getString("taxiType"));
				String vendor = obj.getString("vendorId").replaceAll("\"", "");
				trip.setVendorId(vendor);
				String pickupDateTime = obj.getString("pickupDateTime").replaceAll("\"", "");
				Date pickDate = formatter.parse(pickupDateTime);
				trip.setPickupDateTime(pickDate);
				String dropOffDatetime = obj.getString("dropOffDatetime").replaceAll("\"", "");
				Date dropDate = formatter.parse(dropOffDatetime);
				trip.setDropOffDatetime(dropDate);
				String pickupLocationId = obj.getString("pickupLocationId").replaceAll("\"", "");
				trip.setPickupLocationId(pickupLocationId);
				String dropOffLocationId = obj.getString("dropOffLocationId").replaceAll("\"", "");
				trip.setDropOffLocationId(dropOffLocationId);
				trip.setType(obj.getString("type"));

				// get the day and month of this trip through drop off time
				int d = trip.getDropOffDatetime().getDate();
				int m = trip.getDropOffDatetime().getMonth() + 1;
				trips[m][d].add(trip);
				givenMonth = m;

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	/**
	 * calculate the number of trips per each day in the year
	 */

	private void tripsNmberPerDay() {

		for (int i = 0; i < YEAR_LENGTH; i++) {
			for (int j = 0; j < MONTH_LENGTH; j++) {
				tripsPerDay[i][j] = trips[i][j].size();
			}
		}
	}

	/**
	 * 
	 * calculate the average number of distinct vehicles per day in the year
	 * 
	 */
	private void averegeVehiclesPerDay() {
		avrgvendPerDay = new int[YEAR_LENGTH][MONTH_LENGTH];
		for (int i = 1; i < YEAR_LENGTH; i++) {
			for (int j = 1; j < MONTH_LENGTH; j++) {
				Set<String> s = new HashSet<>();
				for (Trip trip : trips[i][j]) {
					s.add(trip.getVendorId());
				}
				avrgvendPerDay[i][j] = s.size();
			}
		}
	}

	/**
	 * get the number of trips which don't have drop off location for each three
	 * types of taxi all over the year
	 */
	private void getNonDrop() {
		dropOff = new int[3];
		for (int i = 1; i < YEAR_LENGTH; i++) {
			for (int j = 1; j < MONTH_LENGTH; j++) {
				for (Trip trip : trips[i][j]) {
					if (trip.getDropOffLocationId() == "") {
						String tripType = trip.getTripType();
						switch (tripType) {
						case "yellow":
							dropOff[0]++;
						case "green":
							dropOff[1]++;
						case "fhv":
							dropOff[2]++;
						}
					}
				}
			}
		}
	}

	/**
	 * get the average number of minutes of each type of taxi calculate the total
	 * sum of all trips of this taxi type and then divide on the total number of
	 * trips of this type
	 */
	private void getMinutesPerTrip() {
		int yellowNum = 1, greenNum = 1, fhvNum = 1;
		avrgMinutePerTrip = new int[3];
		for (int i = 1; i < YEAR_LENGTH; i++) {
			for (int j = 1; j < MONTH_LENGTH; j++) {
				for (Trip trip : trips[i][j]) {
					String tripType = trip.getTripType();
					long tripLen = trip.getDropOffDatetime().getTime() - trip.getPickupDateTime().getTime();
					tripLen = tripLen / (60 * 1000) % 60;
					switch (tripType) {
					case "yellow":
						avrgMinutePerTrip[0] += tripLen;
						yellowNum++;
					case "green":
						avrgMinutePerTrip[1] += tripLen;
						greenNum++;
					case "fhv":
						avrgMinutePerTrip[2] += tripLen;
						fhvNum++;
					}
					// as start each count equal one to avoid divide by zero so it's need to reduce
					// by one if they are more then one trip
					if (yellowNum > 1)
						yellowNum--;
					if (greenNum > 1)
						greenNum--;
					if (fhvNum > 1)
						fhvNum--;
					avrgMinutePerTrip[0] = avrgMinutePerTrip[0] / yellowNum;
					avrgMinutePerTrip[1] = avrgMinutePerTrip[1] / greenNum;
					avrgMinutePerTrip[2] = avrgMinutePerTrip[2] / fhvNum;
				}
			}
		}
	}

	/**
	 * get the average number of trip per day of each type of taxi of the pickup
	 * location id of this trip is equal to the location id of the taxi zone madison
	 * and taxi borough is brooklyn
	 */
	private void getTripsMadBro() {
		String locationID = "";
		for (Location location : locations) {
			if (location.getZone().equals("Madison") && location.getBorough().equals("Brooklyn"))
				locationID = location.getId();
		}
		madBro = new int[YEAR_LENGTH][MONTH_LENGTH][3];
		for (int i = 1; i < YEAR_LENGTH; i++) {
			for (int j = 1; j < MONTH_LENGTH; j++) {
				for (Trip trip : trips[i][j]) {
					if (trip.getPickupLocationId().equals(locationID)) {
						String tripType = trip.getTripType();
						switch (tripType) {
						case "yellow":
							madBro[i][j][0]++;
						case "green":
							madBro[i][j][1]++;
						case "fhv":
							madBro[i][j][2]++;
						}
					}
				}
			}
		}
	}

	/**
	 * getters to the live data to be showed in the result class
	 */
	public int[][] getTripsPerDay() {
		return tripsPerDay;
	}

	public int[][] getAvrgvendPerDay() {
		return avrgvendPerDay;
	}

	public int[] getDropOff() {
		return dropOff;
	}

	public int[] getAvrgMinutePerTrip() {
		return avrgMinutePerTrip;
	}

	public int[][][] getMadBro() {
		return madBro;
	}

	/**
	 * this function is to calculate the live stream required data and send them to
	 * result class to display them
	 */
	public void liveAnalysis() {

		tripsNmberPerDay();
		averegeVehiclesPerDay();
		getNonDrop();
		getMinutesPerTrip();
		getTripsMadBro();

		resultHandler.showLive(getTripsPerDay(), getAvrgvendPerDay(), getDropOff(), getAvrgMinutePerTrip(),
				getMadBro());
	}

	// TODO print in file
	/**
	 * the functions below for the data required to print in the text file
	 */

	/**
	 * as we assume that the total number of records are equal to the total number
	 * of received messages
	 */
	public int getTotalRecords() {
		return totalRecords;
	}

	private int getTotalNumTrips() {
		int answer = 0;
		for (int i = 0; i < YEAR_LENGTH; i++) {
			for (int j = 0; j < MONTH_LENGTH; j++)
				answer += trips[i][j].size();
		}
		return answer;
	}

	/**
	 * calculate the average number of trips per day for the given month by
	 * calculating the total number of trips in this month divide on 30 the number
	 * of days in the month
	 */
	private double averageTripPerDay() {
		double answer = 0;
		for (int j = 0; j < MONTH_LENGTH; j++) {
			answer += trips[givenMonth][j].size();
		}
		answer /= 30.0;
		return answer;
	}

	/**
	 * calculate the total number of distinct drivers in all trips in the year.
	 */
	private int distinctVehicles() {
		ArrayList<String> newList = new ArrayList<>();
		for (int i = 0; i < YEAR_LENGTH; i++) {
			for (int j = 0; j < MONTH_LENGTH; j++) {
				for (Trip trip : trips[i][j]) {
					if (!newList.contains(trip.getVendorId()))
						newList.add(trip.getVendorId());
				}
			}
		}
		return newList.size();
	}

	/**
	 * get the total number of trip of each type of taxi of the pickup location id
	 * of this trip is equal to the location id of the taxi zone Woodside and taxi
	 * borough is Queens
	 */

	private int[] totalTripsWooQueen() {
		String locationID = "";
		int[] wooQueen = new int[3];
		for (Location location : locations) {
			if (location.getZone().equals("Woodside") && location.getBorough().equals("Queens"))
				locationID = location.getId();
		}
		for (int i = 0; i < YEAR_LENGTH; i++) {
			for (int j = 0; j < MONTH_LENGTH; j++) {
				for (Trip trip : trips[i][j]) {
					if (trip.getPickupLocationId().equals(locationID)) {
						String tripType = trip.getTripType();
						switch (tripType) {
						case "yellow":
							wooQueen[0]++;
						case "green":
							wooQueen[1]++;
						case "fhv":
							wooQueen[2]++;
						}
					}
				}
			}
		}
		return wooQueen;
	}

	/**
	 * this function is to calculate and write the required data and send them to
	 * result class to write them in text file
	 */
	private int fileNumber = 0;

	public void writeInTextFile() {
		fileNumber++;
		resultHandler.writeFinal(getTotalRecords(), getTotalNumTrips(), givenMonth, averageTripPerDay(),
				distinctVehicles(), totalTripsWooQueen(), fileNumber);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		writeInTextFile();
	}

}
