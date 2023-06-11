package com.example.gasheaterapp.data.file;

import com.example.gasheaterapp.data.Device;
import com.example.gasheaterapp.data.Range;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyFile {
    Gson gson = new Gson();



    public List<Integer> readRandomTemperatureValues() {
        List<Integer> temperatureValues = new ArrayList<>();
        try {
            String url = "https://triplie.github.io/numbers.txt";

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    int temperature = Integer.parseInt(line);
                    temperatureValues.add(temperature);
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
            }

            reader.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return temperatureValues;
    }


    public List<Range> ReadFileRange(){
        List<Range> ranges = null;
        try (Reader reader = new FileReader("/Users/lena/Desktop/GasHeaterApp/src/main/resources/com/example/gasheaterapp/file/Range.json")) {
           Type listType = new TypeToken<List<Range>>() {}.getType();
            ranges = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ranges;
    }

    public List<Range> SaveRangeToFile(Range range) {
        List<Range> ranges = ReadFileRange();
        ranges.add(range);
        try (FileWriter writer = new FileWriter("/Users/lena/Desktop/GasHeaterApp/src/main/resources/com/example/gasheaterapp/file/Range.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(ranges, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ranges;
    }

    public List<Range> DeleteRangeFromFile(int index) {
        List<Range> ranges = ReadFileRange();

        if (index >= 0 && index < ranges.size()) {
            ranges.remove(index);

            try (FileWriter writer = new FileWriter("/Users/lena/Desktop/GasHeaterApp/src/main/resources/com/example/gasheaterapp/file/Range.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(ranges, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid index. Cannot delete range.");
        }
        return ranges;
    }


}
