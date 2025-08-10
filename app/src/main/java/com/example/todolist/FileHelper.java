package com.example.todolist;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;

public class FileHelper {

    public static final String filename = "listinfo.dat";

    public static void writedata(ArrayList<Task> item, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oas = new ObjectOutputStream(fos);
            oas.writeObject(item);
            oas.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Task> readData(Context context) {
        ArrayList<Task> itemList = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            itemList = (ArrayList<Task>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            // first run, no file yet
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return itemList;
    }
}
