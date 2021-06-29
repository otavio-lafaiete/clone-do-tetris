package tetrisLogic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;

//Esta classe se refere ao jogo propriamente dito
public class Board extends JPanel implements KeyListener {

    private BufferedImage[] colorArray;
    public static final int BLOCK_SIZE = 16, COLS = 10, ROWS = 20;
    public final int[][] board = new int[ROWS][COLS];
    private  final Shape[] shapes;
    private Shape currentShape, nextShape;
    private Timer timer;
    private SecureRandom random;
    private boolean gameOver;
    private boolean gamePaused;
    private InterfacePrincipal interfacePrincipal;
    private int lines;
    private int score;

    public Board(InterfacePrincipal interfacePrincipal){

        this.interfacePrincipal = interfacePrincipal;
        setBackground(Color.BLACK);
        setBounds(20, 20,Board.COLS * Board.BLOCK_SIZE ,Board.ROWS * Board.BLOCK_SIZE);

        random = new SecureRandom();
        colorArray = new BufferedImage[8];
        shapes = new tetrisLogic.Shape[7];

        try {
            colorArray[1] = ImageIO.read(Board.class.getResource("/tetrominoesTextures/red.png"));
            colorArray[2] = ImageIO.read(Board.class.getResource("/tetrominoesTextures/cyan.png"));
            colorArray[3] = ImageIO.read(Board.class.getResource("/tetrominoesTextures/magenta.png"));
            colorArray[4] = ImageIO.read(Board.class.getResource("/tetrominoesTextures/orange.png"));
            colorArray[5] = ImageIO.read(Board.class.getResource("/tetrominoesTextures/green.png"));
            colorArray[6] = ImageIO.read(Board.class.getResource("/tetrominoesTextures/yellow.png"));
            colorArray[7] = ImageIO.read(Board.class.getResource("/tetrominoesTextures/blue.png"));

        }catch(IOException ioException){
            System.err.print("An error has ocurred when reading textures");
            System.exit(1);
        }

        shapes[0] = new tetrisLogic.Shape(colorArray[1], new int[][]{
                {1, 1, 1, 1},
        }, this, 1); // IShape
        shapes[1] = new tetrisLogic.Shape(colorArray[2], new int[][]{
                {1, 1, 0},
                {0, 1, 1},
        }, this, 2); // ZShape
        shapes[2] = new tetrisLogic.Shape(colorArray[3], new int[][]{
                {0,1,1},
                {1,1,0},
        }, this, 3); // SShape
        shapes[3] = new tetrisLogic.Shape(colorArray[4], new int[][]{
                {1,1,1},
                {0,0,1}
        }, this, 4); // JShape
        shapes[4] = new tetrisLogic.Shape(colorArray[5], new int[][]{
                {1,1,1},
                {1,0,0}
        }, this, 5); // LShape
        shapes[5] = new tetrisLogic.Shape(colorArray[6], new int[][]{
                {1,1,1},
                {0,1,0}
        }, this, 6); // TShape
        shapes[6] = new tetrisLogic.Shape(colorArray[7], new int[][]{
                {1,1},
                {1,1}
        }, this, 7); // OShape

        nextShape = new Shape(shapes[random.nextInt(shapes.length)]);
        setNextShape();

        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentShape.update();
                repaint();
            }
        });

        timer.start();
    }

    public void setNextShape(){

        if(board[0][4] != 0) {
            timer.stop();
            timer = null;
            gameOver = true;

            int option = JOptionPane.showConfirmDialog(this, "Você quer salvar seu score?");
            if(option == 0){
                String name = JOptionPane.showInputDialog("Insira seu nome: ");
                RecordScore.recordOutput(name, score);
            }


            return;
        }
        currentShape = nextShape;
        nextShape = new Shape(shapes[random.nextInt(shapes.length)]);
        interfacePrincipal.setNextShape(nextShape);
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void updateScore(int fullLines){

        //Dependendo da quantia de linhas que o jogador tira de uma única vez ele rece mais ou menos pontos
        score += Math.pow(10, fullLines);
        lines += fullLines;
    }

    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        currentShape.render(g);

        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++){
                if(board[row][col] != 0)
                    g.drawImage(colorArray[board[row][col]], col * BLOCK_SIZE, row * BLOCK_SIZE, null);
            }
        }

        if(gamePaused){
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.red);
            g.drawString("PAUSE", 50, ROWS* BLOCK_SIZE / 2);
        }

        if(gameOver){
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.red);
            g.drawString("GAME OVER", 20, ROWS* BLOCK_SIZE / 2);
        }
    }


    public Timer getTimer() {
        return timer;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(!gamePaused){
            if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                currentShape.setDeltaX(-1);
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                currentShape.setDeltaX(1);
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                currentShape.highSpeed(true);
            }
            if(e.getKeyCode() == KeyEvent.VK_UP){
                currentShape.rotate();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.highSpeed(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public int[][] getBoard() {
        return board;
    }

    public void pause(){
        timer.stop();
        gamePaused = true;
        repaint();
    }

    public void play(){
        timer.start();
        gamePaused = false;
    }

    public int getLines() {
        return lines;
    }

    public int getScore() {
        return score;
    }

    public Shape getNextShape() {
        return nextShape;
    }
}
