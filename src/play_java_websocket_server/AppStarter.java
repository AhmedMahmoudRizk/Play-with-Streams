package play_java_websocket_server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Rizk
 */
public class AppStarter extends Application {

    static WebSocketConnect webSocketConnect;

    Pane root;
    Pane root2;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
            String path = new File(".").getAbsoluteFile().toString();
            System.out.println(path.substring(0, path.length()-1));
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // this will be run in a separate thread

        webSocketConnect = new WebSocketConnect();
            }
        });

// start the thread
        thread.start();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AppStarter.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            
            
            System.out.println(new File("C:\\Users\\Yahia\\Downloads\\Compressed\\play_websocket\\src\\play_java_websocket_server\\taxi_zones_simple.csv").exists());
            root = new Pane();
            root2 = FXMLLoader.load(getClass().getResource("Reports.fxml"));
            root.getChildren().addAll(root2);
//            primaryStage.initStyle(StageStyle.UNDECORATED);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException ex) {
            Logger.getLogger(AppStarter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
