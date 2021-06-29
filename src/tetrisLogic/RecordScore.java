package tetrisLogic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//Classe utilizada para fazer a leitura e gravação do score
public class RecordScore {

    private static Scanner input;
    private static Formatter output;
    private static ArrayList<String> scores;

    public static void openInput(){

        try {
            input = new Scanner(Paths.get("Scores.txt"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public static void readInput(){

        scores = new ArrayList<>();

        while(input.hasNext()){
            scores.add(input.next() + " " + input.nextInt());
        }
    }

    public static void closeInput(){

        if(input != null){
            input.close();
        }
    }

    public static void openOutput(){

        try {
            output = new Formatter("Scores.txt");
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public static void recordOutput(String name, int score){

        if(Files.exists(Paths.get("Scores.txt"))){
            openInput();
            readInput();
            closeInput();

            scores.add(name + " " +score);
            openOutput();
            for(String s: scores){
                output.format("%s%n", s);
            }
            closeOutput();

        }else{
            openOutput();
            output.format("%s %d", name, score);
            closeOutput();
        }

    }

    public static void closeOutput(){

        if(output != null){
            output.close();
        }
    }
}
