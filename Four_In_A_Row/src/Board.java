import java.awt.*;
import java.awt.event.KeyEvent;

public class Board {
    private final Spot[][] grid;
    private Type currentColor;
    private boolean gameOver;

    public Board() {
        grid = new Spot[7][6];
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                grid[col][row] = new Spot();
            }
        }
        currentColor = Type.ORANGE;
        gameOver = false;
    }

    //Get a specific spot on the board
    public Spot getSpot(int row, int col) {
        return grid[row][col];
    }

    public void changePlayer() {
        if (currentColor == Type.ORANGE) {
            currentColor = Type.GREEN;
        } else {
            currentColor = Type.ORANGE;
        }
    }
    public boolean isColFull(int col) {
        return !grid[col][0].isWhite();
    }
    public boolean isSpotAvailable(int col) {
        return (col >= 0 && col < 7 && !isColFull(col));
    }

    public boolean placeDisc(int col) {
        if (!isColFull(col)) {  // Check if the column is full
            for (int row = 5; row >= 0; row--) {  // Start from the bottom (row 5) and move upwards
                if (grid[col][row].isWhite()) {  // Find the first empty spot
                    grid[col][row].setColor(currentColor);  // Set the disc with the current player's color
                    if (checkWinner(currentColor) || isDraw()) {
                        gameOver = true;  // Mark the game as over if there's a win or draw
                    } else {
                        changePlayer();  // Switch to the other player
                    }
                    return true;
                }
            }
        }
        return false;  // No available spot in this column
    }

    public boolean checkWinner(Type color) {
        //Check horizontal win
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                if (grid[col][row].getColor() == color && grid[col + 1][row].getColor() == color
                        && grid[col + 2][row].getColor() == color && grid[col + 3][row].getColor() == color) {
                    return true;
                }
            }
        }
        //Check vertical win
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 7; col++) {
                if (grid[col][row].getColor() == color && grid[col][row + 1].getColor() == color
                        && grid[col][row + 2].getColor() == color && grid[col][row + 3].getColor() == color) {
                    return true;
                }
            }
        }
        //Check diagonal win

        //Right diagonal win
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                if (grid[col][row].getColor() == color
                        && grid[col + 1][row + 1].getColor() == color
                        && grid[col + 2][row + 2].getColor() == color
                        && grid[col + 3][row + 3].getColor() == color) {
                    return true;
                }
            }
        }

        //Left diagonal win
        for (int row = 0; row < 3; row++) {
            for (int col = 6; col > 2; col--) {
                if (grid[col][row].getColor() == color
                        && grid[col - 1][row + 1].getColor() == color
                        && grid[col - 2][row + 2].getColor() == color
                        && grid[col - 3][row + 3].getColor() == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDraw() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if (grid[i][j].getColor() == Type.WHITE) {
                    return false;
                }
            }
        }
        return true;
    }

    public void drawBoard(Graphics2D g2, int width, int height) {
        int cellSize = Math.min(width / 7, height / 6); // Determine the size of each cell based on window size
        int boardWidth = cellSize * 7;
        int boardHeight = cellSize * 6;

        int margin = 50; // Space added to the top

        // Draw board background
        g2.setColor(new Color(128, 0, 128));  // Blue color for the board
        g2.fillRect(0, margin, boardWidth, boardHeight);  // Shift the board down by the margin

        // Draw the spots (circles for discs)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                // Draw the "holes" in the board
                g2.setColor(Color.LIGHT_GRAY); // Default color for empty spots
                g2.fillOval(col * cellSize, row * cellSize + margin, cellSize-10, cellSize-10);  // Draw empty circles

                // Draw the discs
                Spot spot = grid[col][row];
                if (spot.getColor() == Type.ORANGE) {
                    g2.setColor(new Color(255, 87, 0)); // Orange for player one
                } else if (spot.getColor() == Type.GREEN) {
                    g2.setColor(new Color(0, 255, 0)); // Green for player two
                } else {
                    continue;  // Skip empty spots (which are already drawn black)
                }
                // Fill the circle with the color of the disc
                g2.fillOval(col * cellSize, row * cellSize + margin, cellSize-10, cellSize-10);
            }
        }

        // Display game over message if needed
        if (gameOver) {
            g2.setColor(Color.YELLOW);  // Game over text color
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            String message = "";
            if (checkWinner(Type.ORANGE)) {
                message = "Orange Wins!";
            } else if (checkWinner(Type.GREEN)) {
                message = "Green Wins!";
            } else if (isDraw()) {
                message = "It's a Draw!";
            }
            g2.drawString(message, width / 2 - g2.getFontMetrics().stringWidth(message) / 2, height / 2);
            g2.setFont(new Font("Arial", Font.BOLD,  30));
            g2.drawString("Reset - press 'Space' or 'enter'.",
                    width / 2 - 200, height / 2 + 50);
            g2.drawString("Exit - press 'Esc'", width / 2 - 100, height / 2 + 100);
        }
    }
    public void resetBoard() {
        for (int col = 6; col >= 0; col--) {
            for (int row = 5; row >= 0; row--) {
                grid[col][row].setColor(Type.WHITE);
            }
        }
        this.gameOver = false;
        this.currentColor = Type.ORANGE;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) && gameOver) {
            resetBoard();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}
