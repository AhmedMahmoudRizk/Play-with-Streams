/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package play_java_websocket_server;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Rizk
 */
public class ReportsController implements Initializable {

    @FXML
    ChoiceBox month;
    @FXML
    BarChart dataBars;
    private ResultHandler resultHandler;
    private int[][] tripsPerDay;
    Timeline Updater;
    private int[] dropOff;

    @FXML
    Button numOfTripsBtn;
    private int[][] AvrgvendPerDay;
    private int[] avrgMinutePerTrip;
    private int[][][] madBro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        resultHandler = resultHandler.getInstance();
        initTripsPerDayBar();
        //month.setItems(FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December") //used "setItems" not "new ChoiceBox" because existing FXML items shouldn't be initialized
    }

    public void initTripsPerDayBar() {

        month.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12") //used "setItems" not "new ChoiceBox" because existing FXML items shouldn't be initialized
        );
//       
        handleTripsPerDayBar();
    }

    
    // a function to handle clicking on Trips Per Day Button
    public void handleTripsPerDayBar() {
        dataBars.getData().clear(); // clear the bar chart initially
        if (Updater != null) { // for the first run of the program the updater TimeLine isn't initialized so i don't need to stop it
            Updater.stop(); // make sure thet the updater timeLine of previous chart is stoped 
        }
        month.setVisible(true); // set months choice box to visible since in other buttons it will be hidden
        month.getSelectionModel().select(0); //select month 1 by Default
        month.getSelectionModel().selectedItemProperty()    // add listner to change in the choices of months
                .addListener((obs, oldV, newV)
                        -> setTripsPerDayBar(Integer.parseInt((String) newV))); // when changing the choice the listner calls this function which takes the new choice value
        setTripsPerDayBar(1); // initially set data of month 1
        
        // this part for making the TimeLine which makes a live change to the graph each 5 seconds
        Updater = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTripsPerDayBar(Integer.parseInt(month.getSelectionModel().getSelectedItem().toString())); // each 5 seconds this function is called it simply reset the bar data
            }
        }));
        Updater.setCycleCount(Timeline.INDEFINITE); // indefinite recycling for time line counter
        Updater.play(); // start the TimeLine
    }

    // this function sets the barChart
    private void setTripsPerDayBar(int monthNumber) {
        dataBars.getData().clear();
        this.tripsPerDay = resultHandler.getTripsPerDay();
        XYChart.Series dataSeries1 = new XYChart.Series();

        dataSeries1.setName("Number of Trips Per Day");

        for (int j = 1; j < 32; j++) {
            dataSeries1.getData().add(new XYChart.Data(Integer.toString(j), tripsPerDay[monthNumber][j]));
            System.out.println(tripsPerDay[monthNumber][j] + "--");
        }

        dataBars.getXAxis().setLabel("Day");
        dataBars.getYAxis().setLabel("Number of Trips");

        dataBars.getData().add(dataSeries1);
    }

    public void handleAvrgvendPerDayBar() {
        dataBars.getData().clear();
        Updater.stop();

        month.setVisible(true);
        month.getSelectionModel().select(0); //select Any by Default
        month.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, newV)
                        -> setAvrgvendPerDayBar(Integer.parseInt((String) newV)));
        setAvrgvendPerDayBar(1);
        Updater = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setAvrgvendPerDayBar(Integer.parseInt(month.getSelectionModel().getSelectedItem().toString()));
            }
        }));
        Updater.setCycleCount(Timeline.INDEFINITE);
        Updater.play();

    }

    private void setAvrgvendPerDayBar(int monthNumber) {
        dataBars.getData().clear();
        this.AvrgvendPerDay = resultHandler.getAvrgvendPerDay();
        XYChart.Series dataSeries1 = new XYChart.Series();

        dataSeries1.setName("Average Vehicles per day");

        for (int j = 1; j < 32; j++) {
            dataSeries1.getData().add(new XYChart.Data(Integer.toString(j), AvrgvendPerDay[monthNumber][j]));
            System.out.println(AvrgvendPerDay[monthNumber][j] + "--");
        }

        dataBars.getXAxis().setLabel("Day");
        dataBars.getYAxis().setLabel("Average Vehicles");

        dataBars.getData().add(dataSeries1);
    }

    public void handleDropOffBar() {
        dataBars.getData().clear();
        Updater.stop();

        month.setVisible(false);
        setDropOffBar();
        Updater = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setDropOffBar();
            }
        }));
        Updater.setCycleCount(Timeline.INDEFINITE);
        Updater.play();
    }

    private void setDropOffBar() {
        dataBars.getData().clear();
        this.dropOff = resultHandler.getDropOff();
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.getData().add(new XYChart.Data("Yellow", dropOff[0]));
        dataSeries1.getData().add(new XYChart.Data("Green", dropOff[1]));
        dataSeries1.getData().add(new XYChart.Data("FHV", dropOff[2]));

        dataSeries1.setName("Number of trips without drop-off location id for each taxi type");
        dataBars.getXAxis().setLabel("Taxi Type");
        dataBars.getYAxis().setLabel("Number of trips without drop-off location id");
//
        dataBars.getData().add(dataSeries1);
    }

    public void handleAvrgMinutePerTripBar() {
        dataBars.getData().clear();
        Updater.stop();

        month.setVisible(false);
        setAvrgMinutePerTripBar();
        Updater = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setAvrgMinutePerTripBar();
            }
        }));
        Updater.setCycleCount(Timeline.INDEFINITE);
        Updater.play();
    }

    private void setAvrgMinutePerTripBar() {
        dataBars.getData().clear();
        this.avrgMinutePerTrip = resultHandler.getAvrgMinutePerTrip();
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.getData().add(new XYChart.Data("Yellow", avrgMinutePerTrip[0]));
        dataSeries1.getData().add(new XYChart.Data("Green", avrgMinutePerTrip[1]));
        dataSeries1.getData().add(new XYChart.Data("FHV", avrgMinutePerTrip[2]));

        dataSeries1.setName("Average Minutes per trip for Each Taxi Type");
        dataBars.getXAxis().setLabel("Taxi Type");
        dataBars.getYAxis().setLabel("Average Minutes per trip");
//
        dataBars.getData().add(dataSeries1);
    }

    public void handleMadBroBar() {
        dataBars.getData().clear();
        Updater.stop();

        month.setVisible(true);
        month.getSelectionModel().select(0); //select Any by Default
        month.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, newV)
                        -> setMadBroBar(Integer.parseInt((String) newV)));
        setMadBroBar(1);

        Updater = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setMadBroBar(Integer.parseInt(month.getSelectionModel().getSelectedItem().toString()));
            }
        }));
        Updater.setCycleCount(Timeline.INDEFINITE);
        Updater.play();
    }

    private void setMadBroBar(int monthNumber) {
        dataBars.getData().clear();
        this.madBro = resultHandler.getMadBro();
        XYChart.Series dataSeries1 = new XYChart.Series();
        XYChart.Series dataSeries2 = new XYChart.Series();
        XYChart.Series dataSeries3 = new XYChart.Series();

        for (int j = 1; j < 32; j++) {
            dataSeries1.getData().add(new XYChart.Data(Integer.toString(j), madBro[monthNumber][j][0]));
            dataSeries2.getData().add(new XYChart.Data(Integer.toString(j), madBro[monthNumber][j][1]));
            dataSeries3.getData().add(new XYChart.Data(Integer.toString(j), madBro[monthNumber][j][2]));

        }

        dataSeries1.setName("Yellow");
        dataSeries2.setName("Green");
        dataSeries3.setName("FHV");
        
        dataBars.getXAxis().setLabel("Day");
        dataBars.getYAxis().setLabel("Number of trips picked up from “Madison,Brooklyn”");
//
        dataBars.getData().addAll(dataSeries1, dataSeries2, dataSeries3);
    }

}
