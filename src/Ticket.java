public class Ticket {
    private String origin_name;
    private String destination_name;
    private String departure_time;
    private String arrival_time;
    private String carrier;
    private int price;

    public Ticket(String origin_name, String destination_name, String departure_time
            , String arrival_time, String carrier, int price) {
        this.origin_name = origin_name;
        this.destination_name = destination_name;
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
        this.carrier = carrier;
        this.price = price;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Билет: отправление - " + getOrigin_name() + ", прибытие - " + getDestination_name()
                + ", время отправления - " + getDeparture_time() + ", время прибытия - " + getArrival_time()
                + ", авиаперевозчик - " + getCarrier() + ", цена билета - " + getPrice();
    }
}
