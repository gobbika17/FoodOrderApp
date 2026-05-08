package Foodpackage;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;


public class FoodOrderApp extends JFrame {


    JCheckBox pizza, burger, juice, pasta, sandwich, noodles, coffee, fries;
    JTextField qty1, qty2, qty3, qty4, qty5, qty6, qty7, qty8;
    JTextArea billArea;
    JLabel totalLabel;


    Order currentOrder;
    List<Order> savedOrders;


    public FoodOrderApp() {


        setTitle("Bite & Delight");
        setSize(750, 500);
        setLayout(new BorderLayout());


        // WindowAdapter
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "Do you want to exit?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION);


                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });


        savedOrders = DataManager.loadList("orders.dat");


        // HEADER
        JLabel header = new JLabel("Bite & Delight - Food Order", JLabel.CENTER);
        header.setOpaque(true);
        header.setBackground(new Color(230, 100, 20));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(100, 50));
        add(header, BorderLayout.NORTH);


        // LEFT PANEL
        JPanel left = new JPanel(new GridLayout(8, 3, 10, 10));
        left.setBorder(BorderFactory.createTitledBorder("Select Items"));
        left.setBackground(Color.WHITE);


        pizza = new JCheckBox("Pizza (100)");
        burger = new JCheckBox("Burger (80)");
        juice = new JCheckBox("Juice (50)");
        pasta = new JCheckBox("Pasta (120)");
        sandwich = new JCheckBox("Sandwich (70)");
        noodles = new JCheckBox("Noodles (90)");
        coffee = new JCheckBox("Coffee (60)");
        fries = new JCheckBox("Fries (75)");


        qty1 = new JTextField();
        qty2 = new JTextField();
        qty3 = new JTextField();
        qty4 = new JTextField();
        qty5 = new JTextField();
        qty6 = new JTextField();
        qty7 = new JTextField();
        qty8 = new JTextField();


        disableAllQty();


        addRow(left, pizza, qty1);
        addRow(left, burger, qty2);
        addRow(left, juice, qty3);
        addRow(left, pasta, qty4);
        addRow(left, sandwich, qty5);
        addRow(left, noodles, qty6);
        addRow(left, coffee, qty7);
        addRow(left, fries, qty8);


        addItemListeners();


        // RIGHT PANEL
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Your Bill"));


        billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        billArea.setEditable(false);


        totalLabel = new JLabel("Total: 0");
        totalLabel.setForeground(new Color(230, 100, 20));


        right.add(new JScrollPane(billArea), BorderLayout.CENTER);
        right.add(totalLabel, BorderLayout.SOUTH);


        // BUTTONS
        JButton billBtn = new JButton("Generate Bill");
        JButton saveBtn = new JButton("Save");
        JButton viewBtn = new JButton("View Orders");


        JPanel bottom = new JPanel();
        bottom.add(billBtn);
        bottom.add(saveBtn);
        bottom.add(viewBtn);


        billBtn.addActionListener(e -> generateBill());
        saveBtn.addActionListener(e -> saveOrder());
        viewBtn.addActionListener(e -> viewOrders());


        addNumberValidation();


        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(320);


        add(split, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);


        setVisible(true);
    }


    private void disableAllQty() {
        qty1.setEnabled(false); qty2.setEnabled(false); qty3.setEnabled(false);
        qty4.setEnabled(false); qty5.setEnabled(false); qty6.setEnabled(false);
        qty7.setEnabled(false); qty8.setEnabled(false);
    }


    private void addItemListeners() {
        pizza.addItemListener(e -> qty1.setEnabled(pizza.isSelected()));
        burger.addItemListener(e -> qty2.setEnabled(burger.isSelected()));
        juice.addItemListener(e -> qty3.setEnabled(juice.isSelected()));
        pasta.addItemListener(e -> qty4.setEnabled(pasta.isSelected()));
        sandwich.addItemListener(e -> qty5.setEnabled(sandwich.isSelected()));
        noodles.addItemListener(e -> qty6.setEnabled(noodles.isSelected()));
        coffee.addItemListener(e -> qty7.setEnabled(coffee.isSelected()));
        fries.addItemListener(e -> qty8.setEnabled(fries.isSelected()));
    }


    private void addRow(JPanel panel, JCheckBox box, JTextField qty) {
        panel.add(box);
        panel.add(new JLabel("Qty"));
        panel.add(qty);
    }


    private void addNumberValidation() {
        KeyAdapter key = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()))
                    e.consume();
            }
        };


        qty1.addKeyListener(key); qty2.addKeyListener(key);
        qty3.addKeyListener(key); qty4.addKeyListener(key);
        qty5.addKeyListener(key); qty6.addKeyListener(key);
        qty7.addKeyListener(key); qty8.addKeyListener(key);
    }


    private void generateBill() {
        currentOrder = new Order();
        int total = 0;


        try {
            total += addItem(pizza, qty1, "Pizza", 100);
            total += addItem(burger, qty2, "Burger", 80);
            total += addItem(juice, qty3, "Juice", 50);
            total += addItem(pasta, qty4, "Pasta", 120);
            total += addItem(sandwich, qty5, "Sandwich", 70);
            total += addItem(noodles, qty6, "Noodles", 90);
            total += addItem(coffee, qty7, "Coffee", 60);
            total += addItem(fries, qty8, "Fries", 75);


            currentOrder.setTotal(total);
            billArea.setText(currentOrder.generateBill());
            totalLabel.setText("Total: " + total);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers");
        }
    }


    private int addItem(JCheckBox box, JTextField qtyField, String name, int price) {
        if (box.isSelected()) {
            int q = Integer.parseInt(qtyField.getText());
            currentOrder.addItem(name, q, price);
            return price * q;
        }
        return 0;
    }


    private void saveOrder() {
        if (currentOrder == null) return;


        savedOrders.add(currentOrder);


        try {
            DataManager.saveList(savedOrders, "orders.dat");
            JOptionPane.showMessageDialog(this, "Saved!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file");
        }
    }


    private void viewOrders() {
        List<Order> list = DataManager.loadList("orders.dat");


        StringBuilder sb = new StringBuilder();


        for (Order o : list) {
            sb.append(o.generateBill()).append("\n\n");
        }


        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);


        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Saved Orders", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {
        new FoodOrderApp();
    }
}
