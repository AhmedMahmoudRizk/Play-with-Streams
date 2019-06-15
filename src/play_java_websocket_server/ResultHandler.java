package play_java_websocket_server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultHandler {

    private static ResultHandler resultHandler;
    private int[][] tripsPerDay;
    private int[][] avrgvendPerDay;
    private int[] dropOff;
    private int[] avrgMinutePerTrip;
    private int[][][] madBro;

    

    public ResultHandler() {
        // TODO Auto-generated constructor stub
    }
    
    public static ResultHandler getInstance() {

		if (resultHandler == null)
			resultHandler = new ResultHandler();

		return resultHandler;
	}
    /**
     * parameters :
     *
     * 1- tripsPerDay : 2D array 13 * 32 contain all day of years and number of
     * trips in each day
     *
     * 2- avrgvendPerDay : 2 D array size is 13 the number of months in year and 32 the number of days in month
     * contain the average number of distinct divers for day in the year
     *
     * 3- dropOff : one D array size is 3 contain the number of trips without
     * drop off location [0] for yellow ,[1] for green and [2] for fhv
     *
     * 4- avrgMinutePerTrip : one D array size is 3 contain the average number
     * of minute for trips for each taxi type [0] for yellow ,[1] for green and
     * [2] for fhv
     *
     * 5- madBro : 3 D array the third dimention size is 3 contain the number of trips which
     * pickup location is madison brooklyn [0] for yellow ,[1] for green and [2]
     * for fhv
     */
    
   
    
    public void showLive(int[][] tripsPerDay, int[][] avrgvendPerDay, int[] dropOff, int[] avrgMinutePerTrip,
			int[][][] madBro) {

//		System.out.println("total number of trips for each day in the year = ");
//		for (int i = 1; i < 13; i++) {
//			for (int j = 1; j < 32; j++) {
//				System.out.print(tripsPerDay[i][j] + "--");
//			}
//		}
//		System.out.println();
//		System.out.println("average number of drivers per day for each month  = ");
//		for (int i = 1; i < 13; i++) {
//			System.out.print(avrgvendPerDay[i] + "--");
//		}
//		System.out.println();
//		System.out.println("dropOff Trips yellow = " + dropOff[0] + " green = " + dropOff[1] + " fhv = " + dropOff[2]);
//
//		System.out.println("average Trips yellow = " + avrgMinutePerTrip[0] + " green = " + avrgMinutePerTrip[1]
//				+ " fhv = " + avrgMinutePerTrip[2]);
//
//		System.out.println("Trips from madison to Brooklyn yellow = " + madBro[0] + " green = " + madBro[1] + " fhv = "
//				+ madBro[2]);
        this.tripsPerDay = tripsPerDay;
        this.avrgvendPerDay = avrgvendPerDay;
        this.dropOff = dropOff;
        this.avrgMinutePerTrip = avrgMinutePerTrip;
        this.madBro = madBro;
    }

    public static ResultHandler getResultHandler() {
        return resultHandler;
    }

   

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

   
    public void writeFinal(int totalRecords, int totalNumTrips, int givenMonth, double averageTripPerDay,
            int distinctVehicles, int[] totalTripsWooQueen, int fileNumber) {
        String content = "total number of records =  " + totalRecords + ", total number of trips =  " + totalNumTrips
                + ", the given month is : " + givenMonth + " averageTripPerDay for the this month = "
                + averageTripPerDay + " " + ", distinctVehicles =  " + distinctVehicles + ", Woodside,Queens Yellow = "
                + totalTripsWooQueen[0] + ", Woodside,Queens Green = " + totalTripsWooQueen[1]
                + ", Woodside,Queens FHV = " + totalTripsWooQueen[2];

        try (FileWriter writer = new FileWriter("result" + fileNumber + ".txt");
                BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(content);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

    }
}
