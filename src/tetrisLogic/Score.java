package tetrisLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

//Esta classe se refere a tela que exibe todos os scores
public class Score extends JPanel {

    private static Scanner input;
    private ArrayList<Integer> scores;
    private ArrayList<String> names;
    private StringBuilder namesScores;

    public Score(Window window){

        setBounds(0,0, 385, 370);
        setLayout(null);

        scores = new ArrayList<>();
        names = new ArrayList<>();
        namesScores = new StringBuilder();

        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 0, 385, 328);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(0, 0, 385, 328);

        textArea.setFont(new Font("Arial", Font.BOLD, 15));
        textArea.setBackground(Color.LIGHT_GRAY);
        textArea.setEditable(false);

        if(Files.exists(Paths.get("Scores.txt"))){
            openInput();
            readInput();
            closeInput();
            sort();
        }


        textArea.append(namesScores.toString());
        add(scrollPane);

        JButton voltar = new JButton("VOLTAR");
        voltar.setBounds(0, 317, 365, 43);
        voltar.setFocusPainted(false);
        voltar.setBackground(Color.BLACK);
        voltar.setForeground(Color.LIGHT_GRAY);
        add(voltar);

        voltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                window.setVisibleTrue();
            }
        });
    }

    public void openInput(){

        try {
            input = new Scanner(Paths.get("Scores.txt"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void readInput(){

        while(input.hasNext()){
            names.add(input.next());
            scores.add(input.nextInt());
        }
    }

    public void closeInput(){

        if(input != null){
            input.close();
        }
    }

    public void sort(){

        if(this.scores != null && this.names != null){

            int[] scores = new int[this.scores.size()];
            String[] names = new String[this.names.size()];

            for(int i = 0; i < scores.length; i++){
                scores[i] = this.scores.get(i);
                names[i] = this.names.get(i);
            }

            for(int i = 0; i < scores.length - 1; i++){
                for(int y = i + 1; y < scores.length; y++){
                    if(scores[i] < scores[y]){

                        int temp = scores[i];
                        scores[i] = scores[y];
                        scores[y] = temp;

                        String sTemp = names[i];
                        names[i] = names[y];
                        names[y] = sTemp;
                    }
                }
            }

            for(int i = 0; i < scores.length; i++){
                namesScores.append(String.format("%-30s\t%d%n", names[i].trim(), scores[i]));
            }

        }
    }

    public String getBestScore(){

        String[] scores = namesScores.toString().split("\n");
        return scores[0];
    }
}
