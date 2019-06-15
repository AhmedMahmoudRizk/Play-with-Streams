package play_java_websocket_server;

import java.util.Date;

public class Trip {

	String tripType;
	String vendorId;
	String pickupLocationId;
	String dropOffLocationId = "";
	String type;
	Date pickupDateTime;
	Date dropOffDatetime;

	public Trip() {
		// TODO Auto-generated constructor stub
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getPickupLocationId() {
		return pickupLocationId;
	}

	public void setPickupLocationId(String pickupLocationId) {
		this.pickupLocationId = pickupLocationId;
	}

	public String getDropOffLocationId() {
		return dropOffLocationId;
	}

	public void setDropOffLocationId(String dropOffLocationId) {
		this.dropOffLocationId = dropOffLocationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getPickupDateTime() {
		return pickupDateTime;
	}

	public void setPickupDateTime(Date pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	public Date getDropOffDatetime() {
		return dropOffDatetime;
	}

	public void setDropOffDatetime(Date dropOffDatetime) {
		this.dropOffDatetime = dropOffDatetime;
	}

}
