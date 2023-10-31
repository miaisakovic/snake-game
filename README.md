# Snake Game
Classic 2D snake game developed with Swing and AWT.

## Setup 
If Java has not been previously installed, download the correct JDK for your operating system [here](https://www.oracle.com/ca-en/java/technologies/downloads/) and then follow the installation wizard.

To verify that Java is installed, run the following:
```
$ java -version
```
After that, clone this repository:
```
$ git clone https://github.com/miaisakovic/snake-game.git
``` 
When asked to enter credentials, input your username and personal access token.

## How to Play
Use the arrow keys to move the snake (the green square(s)) in the desired direction. When the snake eats apples (red squares), it gets longer. The goal is to make the snake as large as possible. However, the game is over if the snake collides with itself or the blue border.

To play this game, run the following commands:
```
$ cd <relative path to snake-game>
$ javac SnakeGame.java
$ java SnakeGame
```

<p align="center">
  <img src="https://github.com/miaisakovic/snake-game/blob/main/snake_game.png" width="500" height="500" />
</p>
