package id.ac.its.izzulhaq.tictactoe;

public class Board {
    private int[] board;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        board = new int[9];

        for (int i = 0; i < 9; i++) {
            board[i] = 0;
        }
    }

    public int[] getBoard() {
        return board;
    }

    public int getBoardValueFromIndex(int i) {
        return board[i];
    }

    public void setBoard(int[] board) {
        this.board = board;
    }

    public void setBoardWithIndex(int value, int i) {
        this.board[i] = value;
    }

//    public String printBoard() {
//
//    }
}
