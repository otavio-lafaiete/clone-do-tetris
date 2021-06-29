package tetrisLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shape{

    private BufferedImage color;
    private int[][] coords;
    private Board board;
    private int model;
    private int x, y;
    private double currentTime, lastTime;
    private int speed;
    private final int NORMAL_SPEED, HIGH_SPEED;

    public Shape(BufferedImage color, int[][] coords, Board board, int model){

        this.color = color;
        this.coords = coords;
        this.board = board;
        this.model = model;
        x = 4;
        y = 0;
        currentTime = System.currentTimeMillis();
        lastTime = 0;
        NORMAL_SPEED = 450;
        HIGH_SPEED = 30;
        speed = NORMAL_SPEED;
    }

    public Shape(Shape s){
        this(s.color, s.coords, s.board, s.model);
    }


    public void update(){

        currentTime = System.currentTimeMillis();
        if(currentTime - lastTime > speed){

            if(y + coords.length + 1> Board.ROWS){
                setBoard();
                return;
            }

            for(int i = 0; i < coords.length; i++){
                for(int j = 0; j < coords[i].length; j++){
                    if(coords[i][j] != 0){
                        if(board.getBoard()[i + y + 1][j + x] != 0){
                            setBoard();
                            return;
                        }
                    }
                }
            }

            y++;
            lastTime = currentTime;
        }
    }

    public void render(Graphics g){

        for(int row = 0; row < coords.length; row++){
            for(int col = 0; col < coords[row].length; col++){
                    if(coords[row][col] != 0)
                        g.drawImage(color, (x + col) * Board.BLOCK_SIZE, (y + row) * Board.BLOCK_SIZE, null);
            }
        }
    }

    public void rotate(){

        int[][] matrix = getTranspose(coords);
        matrix = getReverseMatrix(matrix);

        if(y + coords.length == Board.ROWS){
            return;
        }

        if(x+matrix[0].length> Board.COLS){
            if(matrix.length > 3 || matrix[0].length > 3) {
                return;
            }
            else{
                setDeltaX(-1);
            }
        }

        for(int row = 0; row < matrix.length; row++){
            for(int col = 0; col < matrix[row].length; col++){
                if(matrix[row][col] != 0){
                    if(board.getBoard()[row + y][col + x] != 0){
                        return;
                    }
                }
            }
        }

        if(y+matrix.length> Board.ROWS){
            y--;
        }

        coords = matrix;
    }

    private int[][] getTranspose(int[][] matrix){

        int[][] newMatrix = new int[matrix[0].length][matrix.length];
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[0].length; j++)
                newMatrix[j][i] = matrix[i][j];
        return newMatrix;
    }

    private int[][] getReverseMatrix(int[][] matrix){

        for(int i = 0; i < matrix.length / 2; i++){
            int[] m = matrix[i];
            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = m;
        }
        return matrix;
    }

    public void setDeltaX(int deltaX){

        if(x + deltaX < 0 || x + deltaX + coords[0].length > board.COLS)
            return;

        for(int row = 0; row < coords.length; row++){
            for(int col = 0; col < coords[row].length; col++){
                if(coords[row][col] != 0){
                    if(board.getBoard()[row + y][col + x + deltaX] != 0){
                        return;
                    }
                }
            }
        }
        x += deltaX;
    }

    public void highSpeed(boolean high) {

        if(high){
            speed = HIGH_SPEED;
        }
        else{
            speed = NORMAL_SPEED;
        }
    }

    public void setBoard(){

        for(int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords[i].length; j++) {
                if (coords[i][j] != 0) {
                    board.getBoard()[i + y][j + x] = model;
                }
            }
        }
        
        checkLine();
        board.setNextShape();
    }

    public void checkLine(){

        int fullLines = 0;

        for(int row = Board.ROWS - 1; row >= 0; row--){

            boolean isLineFull = true;

            for(int col = 0; col < Board.COLS; col++){

                if(board.getBoard()[row][col] == 0){
                    isLineFull = false;
                }

            }

            if(isLineFull){

                fullLines++;

                for(int tempRow = row; tempRow > 0; tempRow--){
                    for(int col = 0; col < Board.COLS; col++){
                        board.getBoard()[tempRow][col] = board.getBoard()[tempRow - 1][col];
                    }
                }

                row++;

            }

        }

        if(fullLines > 0){
            board.updateScore(fullLines);
        }
    }

    public BufferedImage getColor() {
        return color;
    }

    public int[][] getCoords() {
        return coords;
    }
}


