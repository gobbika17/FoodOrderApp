package Foodpackage;


import java.io.Serializable;
import java.util.ArrayList;


public class Order implements Serializable {


    ArrayList<String> items = new ArrayList<>();
    ArrayList<Integer> qtys = new ArrayList<>();
    ArrayList<Integer> prices = new ArrayList<>();


    int total;


    public void addItem(String name, int qty, int price) {
        items.add(name);
        qtys.add(qty);
        prices.add(price);
    }


    public void setTotal(int t) {
        total = t;
    }


    public String generateBill() {


        StringBuilder sb = new StringBuilder();


        sb.append("   BITE & DELIGHT\n");
        sb.append("   Fast Food Restaurant\n");
        sb.append("--------------------------------\n");
        sb.append(String.format("%-10s %-5s %-8s\n", "Item", "Qty", "Amount"));
        sb.append("--------------------------------\n");


        for (int i = 0; i < items.size(); i++) {
            int amount = qtys.get(i) * prices.get(i);
            sb.append(String.format("%-10s x%-3d %-8d\n",
                    items.get(i), qtys.get(i), amount));
        }


        double gst = total * 0.05;
        double grand = total + gst;


        sb.append("--------------------------------\n");
        sb.append(String.format("Sub Total: %d\n", total));
        sb.append(String.format("GST (5%%): %.2f\n", gst));
        sb.append("--------------------------------\n");
        sb.append(String.format("GRAND TOTAL: %.2f\n", grand));
        sb.append("--------------------------------\n");
        sb.append("   Thank you! Visit again\n");


        return sb.toString();
    }
}
