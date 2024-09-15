import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private final Board board;
    private final int WIDTH = 700;
    private final int HEIGHT = 650;

    public Main() {
        board = new Board();  // Initialize the board

        setTitle("Connect Four");  // Set window title
        setSize(WIDTH, HEIGHT);  // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close the window on exit
        setResizable(false);  // Prevent resizing of the window

        // Add mouse listener to handle user clicks for placing discs
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!board.isGameOver()) {
                    int col = e.getX() / (WIDTH / 7);  // Calculate which column was clicked
                    if (board.isSpotAvailable(col)) {
                        board.placeDisc(col);  // Place the disc in the clicked column
                        repaint();  // Redraw the board after placing the disc
                    }
                }
            }
        });
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                board.keyPressed(e);
                repaint();
            }
        };
        addKeyListener(keyListener);
        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);  // Make the window visible
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        board.drawBoard(g2, getWidth(), getHeight());  // Draw the game board
    }

    public static void main(String[] args) {
        // Run the game on the event-dispatching thread (EDT)
        // Start the game by creating the Main window
        SwingUtilities.invokeLater(Main::new);
    }
}
