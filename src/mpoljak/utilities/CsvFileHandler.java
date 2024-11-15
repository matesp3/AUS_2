package mpoljak.utilities;

import java.io.*;

public class CsvFileHandler {
    public static void main(String[] args) {
        File file = new File("src/mpoljak/utilities/csv-test.csv");
//        try (FileWriter fw = new FileWriter(file)) {
//            fw.write("id;name;age\n");
//            fw.write("id;name;age\n");
//            fw.write("id;name;age\n");
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
             String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                System.out.println(data[0] + "|" + data[1] + "|" + data[2]);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
