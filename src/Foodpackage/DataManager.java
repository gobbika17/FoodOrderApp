package Foodpackage;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class DataManager {


    public static <T extends Serializable> void saveList(List<T> list, String file)
            throws IOException {


        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(list);
        oos.close();
    }


    public static <T extends Serializable> List<T> loadList(String file) {


        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
