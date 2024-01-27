import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    static ArrayList<Ticket> objectsTickets = new ArrayList<>();
    static int[] medianOfFlights = new int[4];

    public static void main(String[] args) {
        minFlightTime(creatObjectsTickets(splitStrJson(readJson())));
        System.out.println("\nСредняя цена на билеты составляет - " + averagePrice(objectsTickets));
        Arrays.sort(medianOfFlights);
        System.out.println("Медиана для полета между городами Владивостоком и Тель-Авив составляет - "
                + medianOfFlights[medianOfFlights.length / 2] + " часов");
    }

    public static String readJson() {
        String json = "";
        try {
            json = new String(Files.readAllBytes(Paths.get("tickets.json")))
                    .replaceAll("[\\{\\}\\[\\],\"]", "").trim();
        } catch (IOException e) {
            System.out.println("что-то пошло не так!!!");
        }
        return json;
    }

    public static String[] splitStrJson(String strJson) {
        return strJson.split("\n");
    }

    public static List<Ticket> creatObjectsTickets(String[] splitJson) {

        String[] arrSplit = new String[6];
        StringBuilder paramOfTickets = new StringBuilder();
        while (true) {
            for (int i = 2; i < splitJson.length; i++) {
                if (!splitJson[i].equals("   ") && !splitJson[i].equals("  ")) {
                    arrSplit = splitJson[i].split(" ");
                    if (arrSplit[4].contains("name") || arrSplit[4].contains("time:")
                            || arrSplit[4].contains("carrier:") || arrSplit[4].contains("price:")) {
                        paramOfTickets.append(arrSplit[5]).append(",");
                    }
                }
            }
            break;
        }
        paramOfTickets.delete(paramOfTickets.length() - 1, paramOfTickets.length());

        String[] splitParamOfObjects = paramOfTickets.toString().split(",");
        for (int i = 0; i < splitParamOfObjects.length; i = i + 6) {

            String origin_name = splitParamOfObjects[i];
            String dest_name = splitParamOfObjects[i + 1];
            String depart_time = splitParamOfObjects[i + 2];
            String arriv_time = splitParamOfObjects[i + 3];
            String carrier = splitParamOfObjects[i + 4];
            int price = Integer.parseInt(splitParamOfObjects[i + 5]);

            if (!origin_name.equals("Ларнака") && !dest_name.equals("Уфа")) {
                objectsTickets.add(new Ticket(origin_name, dest_name, depart_time, arriv_time, carrier, price));
            }
        }
        for (Ticket ticket : objectsTickets) {
            System.out.println(ticket);
        }
        return objectsTickets;
    }

    public static void minFlightTime(List<Ticket> list) {
        String[] nameCarrier = {"TK", "S7", "SU", "BA"};

        for (int j = 0; j < nameCarrier.length; j++) {
            int minHour = Integer.MAX_VALUE;
            int minMinutes = Integer.MAX_VALUE;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCarrier().equals(nameCarrier[j])) {
                    String[] splitArrivTime = list.get(i).getArrival_time().split(":");
                    String[] splitDeparTime = list.get(i).getDeparture_time().split(":");
                    int[] arr = {Integer.parseInt(splitDeparTime[0]), Integer.parseInt(splitDeparTime[1]), // 0 - часы отправления, 1 - минуты отправления
                            Integer.parseInt(splitArrivTime[0]), Integer.parseInt(splitArrivTime[1])}; // 0 - часы прибытия, 1 - минуты прибытия

                    int less = arr[2] - arr[0] - 1;
                    int less1 = arr[3] - arr[1] + 60;

                    int more = arr[2] - arr[0];
                    int more1 = arr[3] - arr[1];

                    if (Integer.parseInt(splitArrivTime[1]) < Integer.parseInt(splitDeparTime[1])) {
                        //System.out.println((arr[2] - arr[0] - 1) + ":" + (arr[3] - arr[1] + 60));
                        if (minHour > less) {
                            minHour = less;
                            if (minMinutes > less1) minMinutes = less1;
                        }
                    } else {
                        //System.out.println((arr[2] - arr[0]) + ":" + (arr[3] - arr[1]));
                        if (minHour > more) {
                            minHour = more;
                            if (minMinutes > more1) minMinutes = more1;
                        }
                    }
                }
            }
            System.out.println("\nМинимальное время полета между Владивостоком и Тель-Авив с авиокомпанией "
                    + "\"" + nameCarrier[j] + "\"" + " составляет " + minHour + " часов " + minMinutes + " минут");

            medianOfFlights[j] = minHour;
        }
    }

    public static double averagePrice(List<Ticket> list) {
        double result = 0;
        for (Ticket ticket : list) {
            result += ticket.getPrice();
        }
        return result / list.size();
    }
}
