package play_java_websocket_server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;

public class WebSocketConnect {

	final static String LOCAL_HOST = "ws://localhost:9000/ws";

	DataHandler dataHandler;
	Timer timer = new Timer();

	public WebSocketConnect() {

		dataHandler = dataHandler.getInstance();
		connectToServer();

	}

	/**
	 * connect to to server web socket and receive messages
	 */
	private void connectToServer() {
		try {
			timer.schedule(dataHandler, 0, 30000);// write data in text file every 30 second
			final WebSocketClient webSocketClient = new WebSocketClient(new URI(LOCAL_HOST));
			webSocketClient.addMessageHandler(new WebSocketClient.MessageHandler() {
				public void handleMessage(String message) {
					dataHandler.SetTrip(message);
					dataHandler.liveAnalysis();
				}
			});
			Thread.sleep(Long.MAX_VALUE);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
