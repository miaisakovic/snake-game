package snake_game_;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.Timer;

/**
 *
 * @author miaisakovic
 */

public class Snake_Game_ extends JComponent implements ActionListener {

    // Height and Width of our game
    static final int WIDTH = 810;
    static final int HEIGHT = 810; 

    //Title of the window
    String title = "Mia's Game";

    // sets the framerate and delay for our game
    // this calculates the number of milliseconds per frame
    // you just need to select an approproate framerate
    int desiredFPS = 21;
    int desiredTime = Math.round((1000 / desiredFPS));

    // timer used to run the game loop
    // this is what keeps our time running smoothly :)
    Timer gameTimer;

    // YOUR GAME VARIABLES WOULD GO HERE
    //Snake variables
    int snakeWidth = 15;
    int snakeHeight = 15;

    int[] snakeX = new int[810];
    int[] snakeY = new int[810];
    
    // length of the snake (number of pieces it has)
    int numPieces = 1;

    // control variables
    boolean snakeUp = false;
    boolean snakeDown = false;
    boolean snakeRight = false;
    boolean snakeLeft = false;

    //Apple variables 
    int appleWidth = 14;
    int appleHeight = 14;

    //gets a random number between 90 and 775 that is divisible by 20 for the x of the apple 
    int randNum1 = (int) (Math.random() * (750 - 90 + 1)) + 90;
    int leftOver1 = randNum1 % 15;
    int newNum1 = randNum1 + (15 - leftOver1);
    
    //gets a random number between 90 and 775 that is divisible by 20 for the y of the apple
    int randNum2 = (int) (Math.random() * (750 - 90 + 1)) + 90;
    int leftOver2 = randNum2 % 15;
    int newNum2 = randNum2 + (15 - leftOver2);

    int appleX = newNum1;
    int appleY = newNum2;
    
    //boolean for game over, it is game over it becomes true 
    boolean gameOver = false;

    //font to say game over 
    Font gameOverFont = new Font("sanserif", Font.BOLD, 50);

    //font for the score
    Font scoreFont = new Font("sanserif", Font.BOLD, 15);
    // GAME VARIABLES END HERE    

    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public Snake_Game_() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();
        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);

        //Set things up for the game at startup
        preSetup();

        //Start the game loop
        gameTimer = new Timer(desiredTime, this);
        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        //clear the screen
        g.clearRect(0, 0, WIDTH, HEIGHT);

        //GAME DRAWING GOES HERE
        g.setColor(Color.BLACK);
        //draw the background in black
        g.fillRect(20, 20, 760, 760);

        //makes a blue border 
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, WIDTH, 30);
        g.fillRect(0, 0, 30, HEIGHT);
        g.fillRect(0, 780, WIDTH, 30);
        g.fillRect(780, 0, 30, HEIGHT);

        //draw the snake
        g.setColor(Color.GREEN);
        for (int i = 0; i < numPieces; i++) {
            g.fillRect(snakeX[i], snakeY[i], snakeWidth - 2, snakeHeight - 2);
        }

        //draw the apple
        g.setColor(Color.RED);
        g.fillRect(appleX, appleY, appleWidth, appleHeight);

        //draw game over sign
        if (gameOver) {
            g.setColor(Color.BLUE);
            g.fillRect(205, 305, 400, 200);
            g.setColor(Color.WHITE);
            g.setFont(gameOverFont);
            g.drawString("GAME OVER", 255, 420);
        }

        //draw score
        g.setColor(Color.WHITE);
        g.setFont(scoreFont);
        g.drawString("Length = " + numPieces, 30, 20);

        //GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void preSetup() {
        // Any of your pre setup before the loop starts should go here
        // starting position of the snake
        snakeX[0] = 60;
        snakeY[0] = 60;
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void gameLoop() {
        //MOVE THE SNAKE 
        moveSnake();
        //COLLIDES WITH BLUE WALL OR ITSELF
        //check if snake collided with the blue wall
        if (snakeX[0] <= 15 || snakeX[0] >= 780 || snakeY[0] <= 15 || snakeY[0] >= 780) {
            //end game 
            gameOver = true;
        }
        //check if snake collided with itself
        for (int i = 5; i < numPieces; i++) {
            if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
                //end game
                gameOver = true;
            }
        }

        //COLLIDES WITH APPLE
        //check if snake collided with apple
        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            //add to snake length
            for (int i = numPieces; i < numPieces + 5; i++) {
                snakeX[i] = snakeX[0];
                snakeY[i] = snakeY[0];
            }

            //update the value of the variable
            numPieces = numPieces + 5;

            //spawn a new apple 
            randNum1 = (int) (Math.random() * (750 - 45 + 1)) + 45;
            leftOver1 = randNum1 % 15;
            int num1 = randNum1 + (15 - leftOver1);

            randNum2 = (int) (Math.random() * (750 - 45 + 1)) + 45;
            leftOver2 = randNum2 % 15;
            int num2 = randNum2 + (15 - leftOver2);

            //make sure the apple isn't on the snake
            boolean appleOnSnake = false;

            for (int i = 0; i < numPieces; i++) {
                //if apple is on the snake
                if (snakeX[i] == num1 && snakeY[i] == num2) {
                    appleOnSnake = true;
                    break;
                    
                //if apple is not on the snake 
                } else if (snakeX[i] != num1 || snakeY[i] != num2) {
                    appleOnSnake = false;
                    num1 = newNum1;
                    num2 = newNum2;
                    appleX = newNum1;
                    appleY = newNum2;
                }
            }
            //loops through until the apple is not on the snake
            while (appleOnSnake == true) {
                //gets new random coordinates for the x and y of the apple 
                randNum1 = (int) (Math.random() * (750 - 45 + 1)) + 45;
                leftOver1 = randNum1 % 15;
                num1 = randNum1 + (15 - leftOver1);

                randNum2 = (int) (Math.random() * (750 - 45 + 1)) + 45;
                leftOver2 = randNum2 % 15;
                num2 = randNum2 + (15 - leftOver2);

                appleX = num1;
                appleY = num2;
                appleOnSnake = false;
                //checks if the apple is on the snake again 
                for (int i = 0; i < numPieces; i++) {
                    if (snakeX[i] == num1 && snakeY[i] == num2) {
                        appleOnSnake = true;
                        break;
                    }
                }
            }
        }
    }

    public void moveSnake() {
        //move entire snake not just the head
        if (numPieces > 1 && !gameOver) {
            for (int i = numPieces; i > 0; i--) {
                snakeX[i] = snakeX[i - 1];
                snakeY[i] = snakeY[i - 1];
            }
        }
        //if the snake is moving up and it is not game over, the snake will move in that direction by 15 units 
        if (snakeUp && !gameOver) {
            snakeY[0] = snakeY[0] - 15;
        }
        //if the snake is moving down and it is not game over, the snake will move in that direction by 15 units
        if (snakeDown && !gameOver) {
            snakeY[0] = snakeY[0] + 15;
        }
        //if the snake is moving right and it is not game over, the snake will move in that direction by 15 units
        if (snakeRight && !gameOver) {
            snakeX[0] = snakeX[0] + 15;
        }
        //if the snake is moving left and it is not game over, the snake will move in that direction by 15 units
        if (snakeLeft && !gameOver) {
            snakeX[0] = snakeX[0] - 15;
        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {

        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e) {

        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {

        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e) {
            // determine the key pressed
            int key = e.getKeyCode();
            //if the snake a length of 1, it can move in any of the 4 directions whenever
            if (numPieces == 1 && !gameOver) {
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
            //if the snake has a length more than 1, it cannot move in a direction opposite to the one it is currently moving in 
            } else if (numPieces > 1 && !gameOver) {
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

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        gameLoop();
        repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        Snake_Game_ game = new Snake_Game_();
    }
}

