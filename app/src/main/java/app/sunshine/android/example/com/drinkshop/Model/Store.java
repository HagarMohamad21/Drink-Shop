package app.sunshine.android.example.com.drinkshop.Model;

public class Store {
    String id,name;
    double lat,lng,distance_in_km;

    public double getDistance_in_km() {
        return distance_in_km;
    }

    public void setDistance_in_km(double distance_in_km) {
        this.distance_in_km = distance_in_km;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
