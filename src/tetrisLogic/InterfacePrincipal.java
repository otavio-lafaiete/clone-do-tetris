package tetrisLogic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//Esta classe se refere a tela em que o jogo está rodando
public class InterfacePrincipal extends JPanel {

    private Board board;
    private Shape nextShape;

    public InterfacePrincipal(){

        setBounds(0,0, 375, 370);
        setLayout(null);
        setVisible(true);
        setFocusable(true);

        ImageIcon playIcon = new ImageIcon();
        ImageIcon playIcon1 = new ImageIcon();
        ImageIcon playIcon2 = new ImageIcon();
        ImageIcon pauseIcon =  new ImageIcon();
        ImageIcon pauseIcon1 = new ImageIcon();
        ImageIcon pauseIcon2 = new ImageIcon();
        ImageIcon restartIcon =  new ImageIcon();
        ImageIcon restartIcon1 =  new ImageIcon();
        ImageIcon restartIcon2 =  new ImageIcon();

        try {
            playIcon.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/play.png")));
            playIcon1.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/play1.png")));
            playIcon2.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/play2.png")));
            pauseIcon.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/pause.png")));
            pauseIcon1.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/pause1.png")));
            pauseIcon2.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/pause2.png")));
            restartIcon.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/restart.png")));
            restartIcon1.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/restart1.png")));
            restartIcon2.setImage(ImageIO.read(InterfacePrincipal.class.getResource("/icons/restart2.png")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        JButton play = new JButton(playIcon);
        play.setBounds(200, 20, 33, 33);
        play.setPressedIcon(playIcon2);
        play.setRolloverIcon(playIcon1);
        play.setBorderPainted(false);
        play.setBackground(new Color(18, 10, 143));
        play.setFocusable(false);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getBoard().play();
            }
        });

        JButton pause = new JButton(pauseIcon);
        pause.setBounds(251, 20, 33, 33);
        pause.setPressedIcon(pauseIcon2);
        pause.setRolloverIcon(pauseIcon1);
        pause.setBorderPainted(false);
        pause.setBackground(new Color(18, 10, 143));
        pause.setFocusable(false);

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getBoard().pause();
            }
        });

        JButton restart = new JButton(restartIcon);
        restart.setBounds(302, 20, 33, 33);
        restart.setPressedIcon(restartIcon2);
        restart.setRolloverIcon(restartIcon1);
        restart.setBorderPainted(false);
        restart.setBackground(new Color(18, 10, 143));
        restart.setFocusable(false);

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });

        add(play);add(pause);add(restart);

        board = new Board(this);
        addKeyListener(board);
        add(board);
    }

    public void restart(){

        if(board != null){
            remove(board);
            removeKeyListener(board);
            if(board.getTimer() != null)
                board.getTimer().stop();
            board.setTimer(null);
            board = new Board(this);
            add(board);
            addKeyListener(board);
        }

    }

    public Board getBoard() {
        return board;
    }

    @Override

    public void paintComponent(Graphics g){         //Aqui é feito o desenho da interface

        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.GRAY);
        g2D.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2D.drawLine(200, 65, 350, 65);
        g2D.drawLine(350, 65, 350, 20);
        g2D.drawLine(17, 17, 17,Board.ROWS * Board.BLOCK_SIZE + 21);
        g2D.drawLine(Board.COLS * Board.BLOCK_SIZE + 21, 17, Board.COLS * Board.BLOCK_SIZE + 21,Board.ROWS * Board.BLOCK_SIZE + 22);
        g2D.drawLine(17, 17, Board.COLS * Board.BLOCK_SIZE + 22, 17);
        g2D.drawLine(17, Board.ROWS * Board.BLOCK_SIZE + 22, Board.COLS * Board.BLOCK_SIZE + 22, Board.ROWS * Board.BLOCK_SIZE + 22);
        g2D.drawLine(200, 80, 350, 80);
        g2D.drawLine(350, 80, 350,Board.ROWS * Board.BLOCK_SIZE + 22);

        g.setFont(new Font("Arial", Font.BOLD, 15 ));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Next Shape: ", 200, 100);
        g2D.setColor(Color.BLACK);
        g2D.fillRect(200, 104, 130, Board.BLOCK_SIZE * 4);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(String.format("Score: %d", board.getScore()), 200,200);
        g.drawString(String.format("Lines: %d", board.getLines()), 200,225);
        g.drawString("Highest Score:", 200, 270);

        for(int row = 0; row < nextShape.getCoords().length; row++){
            for(int col = 0; col < nextShape.getCoords()[row].length; col++){
                if(nextShape.getCoords()[row][col] != 0){
                    if(nextShape.getCoords()[row].length > 3){
                        g.drawImage(nextShape.getColor(), 232 + col * Board.BLOCK_SIZE , 120 + row * Board.BLOCK_SIZE, null);
                    }else{
                        g.drawImage(nextShape.getColor(), 248 + col * Board.BLOCK_SIZE , 120 + row * Board.BLOCK_SIZE, null);
                    }
                }
            }
        }
    }

    public void setNextShape(Shape nextShape){

        this.nextShape = nextShape;
        repaint();
    }

    public void setBestScore(String bestScore){

        //O bestScore vem com diversos espaços utilizados para alinhamento na janela de scores então é necessário
        //fazer um tratamento e retirar esses espaços para mostrar na interface do jogo

        int index  = bestScore.indexOf(" ");

        char[] charArray = bestScore.toCharArray();

        charArray[index] = '-';

        bestScore = String.valueOf(charArray).replaceAll(" ", "");

        JLabel bestScoreLabel = new JLabel("    " + bestScore);
        bestScoreLabel.setBounds(200, 280, 150, 20);
        bestScoreLabel.setFont(new Font("Arial", Font.BOLD, 15 ));
        bestScoreLabel.setForeground(Color.LIGHT_GRAY);
        add(bestScoreLabel);
    }
}
