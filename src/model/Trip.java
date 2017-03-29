package model;

public class Trip {
	//0               1           2        3                    4                5              6                  
	//medallion, hack_license, vendor_id, rate_code, store_and_fwd_flag, pickup_datetime, dropoff_datetime
	//7                 8                 9               10                   11              12                   
	//passenger_count, trip_time_in_secs, trip_distance, pickup_longitude, pickup_latitude, dropoff_longitude
	//13
	//dropoff_latitude
	
	/*Note that longitude and latitude are switched for all given data*/
	
	String[] tripData;
	
	public Trip(String trip){
		this.tripData = trip.trim().split(",");
	}
	
	public double getDistance(){
		return Double.parseDouble(tripData[9]);
	}
	
	public double getPickLat(){
		return Double.parseDouble(tripData[10]);
	}
	
	public double getPickLon(){
		return Double.parseDouble(tripData[11]);
	}

	public double getDropLat(){
		return Double.parseDouble(tripData[12]);
	}
	
	public double getDropLon(){
		return Double.parseDouble(tripData[13]);
	}
}
