package tetrisLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {

    private InterfacePrincipal interfacePrincipal;
    private Score score;
    private JButton btn2;
    private JButton btn;
    private JLabel tetris;

    //Esta classe é a tela principal, onde tudo será exibido
    public Window(){

        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(365, 360);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        score = new Score(this);

        tetris =  new JLabel("TETRIS");
        tetris.setFont(new Font("Arial", Font.BOLD, 50));
        tetris.setBounds(getWidth()/2 - 100, 60, 200, 50);
        tetris.setHorizontalAlignment(SwingConstants.CENTER);
        add(tetris);

        btn = new JButton("START");
        btn.setBounds(getWidth()/2 - 50, 160, 100, 50);
        btn.setFocusPainted(false);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.LIGHT_GRAY);
        add(btn);

        btn2 = new JButton("SCORES");
        btn2.setBounds(getWidth()/2 - 50, 220, 100, 50);
        btn2.setFocusPainted(false);
        btn2.setBackground(Color.BLACK);
        btn2.setForeground(Color.LIGHT_GRAY);
        add(btn2);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interfacePrincipal = new InterfacePrincipal();
                add(interfacePrincipal);
                setVisibleFalse();
                interfacePrincipal.setBackground(new Color(18, 10, 143));
                interfacePrincipal.setBestScore(score.getBestScore());
            }
        });

        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleFalse();
                score.setVisible(true);
                setBackground(Color.LIGHT_GRAY);
                add(score);
            }
        });


    }

    public void setVisibleFalse(){

        btn.setVisible(false);
        btn2.setVisible(false);
        tetris.setVisible(false);
    }

    public void setVisibleTrue(){

        btn.setVisible(true);
        btn2.setVisible(true);
        tetris.setVisible(true);
    }
}
