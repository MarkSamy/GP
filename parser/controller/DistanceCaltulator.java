package controller;

public class DistanceCaltulator {
	
	public Double getDistanceFromLatLonInKm(Double lat1, Double lon1, Double lat2, Double lon2) {
		Double R = (double) 6371; // Radius of the earth in km
		Double dLat = deg2rad(lat2 - lat1); // deg2rad below
		Double dLon = deg2rad(lon2 - lon1);
		Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		Double d = R * c; // Distance in km
		return d;
	}

	private Double deg2rad(Double deg) {
		return deg * (Math.PI / 180);
	}
}
