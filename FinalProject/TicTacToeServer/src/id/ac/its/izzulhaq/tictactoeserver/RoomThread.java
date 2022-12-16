package id.ac.its.izzulhaq.tictactoeserver;

import java.util.Arrays;

public class RoomThread extends Thread {
    private PlayerThread player1;
    private PlayerThread player2 = null;
    private boolean isAvailable = true;
    private boolean isGameEnd = false;

    public RoomThread(PlayerThread host) {
        this.player1 = host;
    }

    public void run() {
        while(player1 != null) {
            try {
                if (player2 == null) {
                    String message = "Waiting for player2";
                    player1.sendMessage(message);
                    Thread.sleep(10000);
                }
                else {
                    setAvailable(false);
                    startGame();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startGame() {
        String[] board = {"_", "_", "_", "_", "_", "_", "_", "_", "_"};
        int turn = 0;

        String message = "Game Started\n" + "Player 1: " + player1.getUsername() + "\n" +
                "Player 2: " + player2.getUsername() + "\n";
        player1.sendMessage(message);
        player2.sendMessage(message);

        while(!isGameEnd) {
            String boardState;
            String command;
            if (turn == 0) {
                player2.sendMessage(player1.getUsername() + "'s turn");
                boardState = stringBoard(board);
                message = "Now's your turn." +
                        "\nSelect position number between 1-9 based on available cell" +
                        "\n" + boardState +
                        "\nYour position: ";
                player1.sendMessage(message);
                command = player1.receiveMessage();

                int position = Integer.parseInt(command);
                board[position] = "X";
                turn = 1;
            }
            else {
                player1.sendMessage(player2.getUsername() + "'s turn");
                boardState = stringBoard(board);
                message = "Now's your turn." +
                        "\nSelect position number between 1-9 based on available cell" +
                        "\n" + boardState +
                        "\nYour position: ";
                player2.sendMessage(message);
                command = player2.receiveMessage();

                int position = Integer.parseInt(command);
                board[position] = "O";
                turn = 0;
            }

            if (checkWinner(board) == 1) {
                isGameEnd = true;
                message = "Game Over\n" +
                        player1.getUsername() + " is the winner\n";
                player1.sendMessage(message);
                player2.sendMessage(message);
            } else if (checkWinner(board) == 2) {
                isGameEnd = true;
                message = "Game Over\n" +
                        player2.getUsername() + " is the winner\n";
                player1.sendMessage(message);
                player2.sendMessage(message);
            } else if (checkWinner(board) == 3) {
                isGameEnd = true;
                message = "Game Over\n" +
                        "Draw\n";
                player1.sendMessage(message);
                player2.sendMessage(message);
            } else {
                isGameEnd = false;
            }
        }
    }

    private String stringBoard(String[] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sb.append(board[i]);

            if ((i + 1) % 3 == 0) {
                sb.append("\n");
            }
            else {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    private int checkWinner(String[] board) {
        for (int i = 0; i < 8; i++) {
            String line = switch (i) {
                case 0 -> board[0] + board[1] + board[2];
                case 1 -> board[3] + board[4] + board[5];
                case 2 -> board[6] + board[7] + board[8];
                case 3 -> board[0] + board[3] + board[6];
                case 4 -> board[1] + board[4] + board[7];
                case 5 -> board[2] + board[5] + board[8];
                case 6 -> board[0] + board[4] + board[8];
                case 7 -> board[2] + board[4] + board[6];
                default -> null;
            };

            if (line.equals("XXX")) {
                return 1;
            } else if (line.equals("OOO")) {
                return 2;
            }
        }

        if (Arrays.asList(board).contains("_")) {
            return 0;
        }
        else return 3;
    }

    public PlayerThread getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerThread player1) {
        this.player1 = player1;
    }

    public PlayerThread getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerThread player2) {
        this.player2 = player2;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isGameEnd() {
        return isGameEnd;
    }

    public void setGameEnd(boolean gameEnd) {
        isGameEnd = gameEnd;
    }
}
