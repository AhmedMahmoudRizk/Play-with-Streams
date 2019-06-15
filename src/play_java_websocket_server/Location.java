package play_java_websocket_server;

public class Location {

	private String id;
	private String zone;
	private String borough;
	
	public Location(String id, String zone, String borough) {
		this.id = id;
		this.zone = zone;
		this.borough = borough;
	}
	
	public String getId() {
		return id;
	}

	public String getZone() {
		return zone;
	}

	public String getBorough() {
		return borough;
	}
	
}
