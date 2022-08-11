import java.io.*;
import java.util.TreeMap;

public class Controller {
    private static final TreeMap<Integer, Integer> bid = new TreeMap<>();
    private static final TreeMap<Integer, Integer> ask = new TreeMap<>();
    private static final StringBuffer sb = new StringBuffer();

    public static void main(String[] args) {
        reader();
        writer();
    }

    private static String[] split(String data) {
        String[] array = new String[4];
        int begin = 0;
        int index = 0;
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == ',') {
                array[index++] = data.substring(begin, i);
                begin = i + 1;
            }
        }
        array[index] = data.substring(begin);
        return array;
    }

    public static void reader() {
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            while (br.ready())
            {
                String[] data = split(br.readLine());
                switch (data[0]) {
                    case "u" -> update(data[1], data[2], data[3]);
                    case "q" -> {
                        if (data[1].equals("best_bid") || data[1].equals("best_ask"))
                            query(data[1], "0");
                        else
                            query(data[1], data[2]);
                    }
                    case "o" -> order(data[1], data[2]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writer() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            {
                bw.write(sb.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(String price, String size, String type) {
        if (type.equals("bid")) {
            if (Integer.parseInt(size) == 0) {
                bid.remove(Integer.parseInt(price));
            } else {
                bid.put(Integer.parseInt(price), Integer.parseInt(size));
            }
        } else if (Integer.parseInt(size) == 0) {
            ask.remove(Integer.parseInt(price));
        } else {
            ask.put(Integer.parseInt(price), Integer.parseInt(size));
        }
    }

    public static void query(String type, String price) {
        switch (type) {
            case "best_bid" -> {
                sb.append(bid.lastEntry().getKey()).append(",").append(bid.lastEntry().getValue()).append("\n");
            }
            case "best_ask" -> {
                sb.append(ask.firstEntry().getKey()).append(",").append(ask.firstEntry().getValue()).append("\n");
            }
            case "size" -> {
                Integer bestPrice = ask.containsKey(Integer.parseInt(price)) ? ask.get(Integer.parseInt(price)) : bid.get(Integer.parseInt(price));
                if (bestPrice == null)
                    bestPrice = 0;
                sb.append(bestPrice).append("\n");
            }
        }
    }
    public static void order(String type, String size) {
        int sharesToRemove = Integer.parseInt(size);
        if (type.equals("buy")) {
            while (true){
                if (ask.firstEntry() != null) {
                    if (ask.firstEntry().getValue() <= sharesToRemove) {
                        sharesToRemove -= ask.firstEntry().getValue();
                        ask.remove(ask.firstEntry().getKey());
                    } else {
                        ask.put(ask.firstEntry().getKey(), ask.firstEntry().getValue() - sharesToRemove);
                        return;
                    }
                } else
                    return;
            }
        }
        if (type.equals("sell")) {
            while (true) {
                if (bid.lastEntry() != null) {
                    if (bid.lastEntry().getValue() <= sharesToRemove) {
                        sharesToRemove -= bid.lastEntry().getValue();
                        bid.remove(bid.lastEntry().getKey());
                    } else {
                        bid.put(bid.lastEntry().getKey(), bid.lastEntry().getValue() - sharesToRemove);
                        return;
                    }
                } else
                    return;
            }
        }
    }
}

