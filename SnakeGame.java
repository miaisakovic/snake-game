import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class SnakeGame extends JComponent implements ActionListener {
    // Height and width of the game
    static final int WIDTH = 700;
    static final int HEIGHT = 700; 

    // Title of the window
    String title = "Snake Game";

    // Sets the framerate and delay for the game
    // This calculates the number of milliseconds per frame
    int desiredFPS = 21;
    int desiredTime = Math.round((1000 / desiredFPS));

    // Timer used to run the game loop
    // This is what keeps our time running smoothly :)
    Timer gameTimer;

    // Snake variables
    int snakeWidth = 15;
    int snakeHeight = 15;

    int[] snakeX = new int[700];
    int[] snakeY = new int[700];

    int snakeLength = 1;

    // Control variables
    boolean snakeUp = false;
    boolean snakeDown = false;
    boolean snakeRight = false;
    boolean snakeLeft = false;

    // Apple variables 
    int appleWidth = 14;
    int appleHeight = 14;

    int appleX = randomCooridinate();
    int appleY = randomCooridinate();

    boolean gameOver = false;

    // Font for game over
    Font gameOverFont = new Font("sanserif", Font.BOLD, 50);

    // Font for the score
    Font scoreFont = new Font("sanserif", Font.BOLD, 15);

    // Constructor to create the Frame and place the panel in
    public SnakeGame() {
        // Creates a windows to show the game
        JFrame frame = new JFrame(title);

        // Sets the size of the game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // Adds the game to the window
        frame.add(this);

        // Sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // Shows the window to the user
        frame.setVisible(true);

        // Add listener for keyboard
        frame.addKeyListener(new Keyboard());

        // Set things up for the game at startup
        preSetup();

        // Start the game loop
        gameTimer = new Timer(desiredTime, this);
        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    // We use the Graphics object, g, to perform the drawing of the game
    // NOTE: This is already double buffered! (helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // Clear the screen
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the background in black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 700, 700);

        // Make a blue border 
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, WIDTH, 30);
        g.fillRect(0, 0, 30, HEIGHT);
        g.fillRect(0, 670, WIDTH, 30);
        g.fillRect(670, 0, 30, HEIGHT);

        // Draw the snake
        g.setColor(Color.GREEN);
        for (int i = 0; i < snakeLength; i++) {
            g.fillRect(snakeX[i], snakeY[i], snakeWidth - 2, snakeHeight - 2);
        }

        // Draw the apple
        g.setColor(Color.RED);
        g.fillRect(appleX, appleY, appleWidth, appleHeight);

        // Draw game over sign
        if (gameOver) {
            g.setColor(Color.BLUE);
            g.fillRect(150, 220, 400, 200);
            g.setColor(Color.WHITE);
            g.setFont(gameOverFont);
            g.drawString("GAME OVER", 195, 335);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(scoreFont);
        g.drawString("Length = " + snakeLength, 30, 20);
    }

    // This method is used to do any pre-setup (it's run before the game loop begins!)
    public void preSetup() {
        // Starting position of the snake
        snakeX[0] = 60;
        snakeY[0] = 60;
    }

    // The main game loop (here is where all the logic for the game will go)
    public void gameLoop() {
        moveSnake();

        // Check if snake collided with the blue wall
        if (snakeX[0] <= 15 || snakeX[0] >= 670 || snakeY[0] <= 15 || snakeY[0] >= 670) {
            gameOver = true;
        }

        // Check if snake collided with itself
        for (int i = 5; i < snakeLength; i++) {
            if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
                gameOver = true;
            }
        }

        // Check if snake collided with the apple
        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            // Add to snake length
            for (int i = snakeLength; i < snakeLength + 5; i++) {
                snakeX[i] = snakeX[0];
                snakeY[i] = snakeY[0];
            }

            snakeLength += 5;

            boolean appleOnSnake = false;

            do {
                // Get random coordinates for the apple's new position
                appleX = randomCooridinate();
                appleY = randomCooridinate();

                appleOnSnake = false;

                // Check if the apple is on the snake
                for (int i = 0; i < snakeLength; i++) {
                    if (snakeX[i] == appleX && snakeY[i] == appleY) {
                        appleOnSnake = true;
                        break;
                    }
                }
            }
            while (appleOnSnake == true);
        }
    }

    public void moveSnake() {
        if (snakeLength > 1 && !gameOver) {
            for (int i = snakeLength; i > 0; i--) {
                snakeX[i] = snakeX[i - 1];
                snakeY[i] = snakeY[i - 1];
            }
        }
        // If the snake is moving up and it is not game over, 
        //   the snake will move in that direction by 15 units 
        if (snakeUp && !gameOver) {
            snakeY[0] = snakeY[0] - 15;
        }
        // If the snake is moving down and it is not game over, 
        //   the snake will move in that direction by 15 units
        if (snakeDown && !gameOver) {
            snakeY[0] = snakeY[0] + 15;
        }
        // If the snake is moving right and it is not game over, 
        //   the snake will move in that direction by 15 units
        if (snakeRight && !gameOver) {
            snakeX[0] = snakeX[0] + 15;
        }
        // If the snake is moving left and it is not game over, 
        //   the snake will move in that direction by 15 units
        if (snakeLeft && !gameOver) {
            snakeX[0] = snakeX[0] - 15;
        }
    }

    // Used to implement any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            // If the snake has a length of 1, 
            //   it can move in any of the four directions
            if (snakeLength == 1 && !gameOver) {
                if (key == KeyEvent.VK_UP) {
                    snakeUp = true;
                    snakeDown = false;
                    snakeRight = false;
                    snakeLeft = false;
                } else if (key == KeyEvent.VK_DOWN) {
                    snakeUp = false;
                    snakeDown = true;
                    snakeRight = false;
                    snakeLeft = false;
                } else if (key == KeyEvent.VK_RIGHT) {
                    snakeUp = false;
                    snakeDown = false;
                    snakeRight = true;
                    snakeLeft = false;
                } else if (key == KeyEvent.VK_LEFT) {
                    snakeUp = false;
                    snakeDown = false;
                    snakeRight = false;
                    snakeLeft = true;
                }

            // If the snake has a length greater than 1, 
            //   it cannot move in the direction opposite to 
            //   the one it is currently moving in 
            } else if (snakeLength > 1 && !gameOver) {
                if ((key == KeyEvent.VK_UP) && (!snakeDown)) {
                    snakeUp = true;
                    snakeRight = false;
                    snakeLeft = false;
                } else if ((key == KeyEvent.VK_DOWN) && (!snakeUp)) {
                    snakeDown = true;
                    snakeRight = false;
                    snakeLeft = false;
                } else if ((key == KeyEvent.VK_RIGHT) && (!snakeLeft)) {
                    snakeUp = false;
                    snakeDown = false;
                    snakeRight = true;
                } else if ((key == KeyEvent.VK_LEFT) && (!snakeRight)) {
                    snakeUp = false;
                    snakeDown = false;
                    snakeLeft = true;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        gameLoop();
        repaint();
    }

    public int randomCooridinate() {
        // Gets a random number between 45 and 660 that is divisible by 15
        int randomNum = (int) (Math.random() * (660 - 45)) + 45;
        int remainder = randomNum % 15;
        return randomNum + (15 - remainder);
    }

    public static void main(String[] args) {
        // Create an instance of the game
        SnakeGame game = new SnakeGame();
    }
}
