import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {
    static ArrayList<Ticket> objectsTickets = new ArrayList<>();
    static String[] minTime = new String[4];

    public static void main(String[] args) {
        String fileName = "results.txt";
        minFlightTime(creatObjectsTickets(splitStrJson(readJson())));
        String averagePrice = "\nСредняя цена на билеты составляет - " + averagePrice(objectsTickets);
        String priceDifference = "\nРазница между средней ценой и медианой для полета между городами составляет - "
                + (averagePrice(objectsTickets) - medianPrice(objectsTickets));
        String medianPrice = "\nМедиананная цена билетов составляет - " + medianPrice(objectsTickets);

        try {
            FileWriter fw = new FileWriter(fileName);
            for (String s : minTime) {
                fw.write(s);
                fw.write(System.lineSeparator());
            }
            fw.write(averagePrice);
            fw.write(System.lineSeparator());
            fw.write(medianPrice);
            fw.write(System.lineSeparator());
            fw.write(priceDifference);
            fw.close();
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл!!!");
        }

        try {
            File file = new File(fileName);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("Файл не может быть открыт на вашей платформе!");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при открытии файла!");
        }
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
            String tempStr = "";
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
            tempStr = "\nМинимальное время полета между Владивостоком и Тель-Авив с авиакомпанией "
                    + "\"" + nameCarrier[j] + "\"" + " составляет " + minHour + " часов " + minMinutes + " минут";
            System.out.println(tempStr);
            minTime[j] = tempStr;
        }
    }

    public static double averagePrice(List<Ticket> list) {
        double result = 0;
        for (Ticket ticket : list) {
            result += ticket.getPrice();
        }
        return result / list.size();
    }

    public static int medianPrice(List<Ticket> list) {
        int[] arr = new int[list.size()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i).getPrice();
        }
        //System.out.println(Arrays.toString(arr));
        Arrays.sort(arr);
        //System.out.println(Arrays.toString(arr));
        if (arr.length % 2 == 0) {
            return (arr[arr.length / 2 - 1] + arr[arr.length / 2]) / 2;
        } else return arr[arr.length / 2];

    }
}
