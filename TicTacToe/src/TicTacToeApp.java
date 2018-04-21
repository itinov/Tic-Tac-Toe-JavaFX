
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A TicTacToe game implemented using JavaFx and simple array manipulation via a 6x6 grid.
 * @author 
 *    V. E., Version 1.0
 * <dt><b>TicTacToeApp</b><dd>
 * 	  Author: Ivan Tinov
 * <dt><b>Date Created:</b><dd>
 *    July 28th, 2017
 * <dt><b>Additional Information:</b><dd>
 * 	  Application is complete however there is still a few bugs that have to be addressed.
 * 		* Once a X or O value is placed, it shouldn't be able to be replaced but in this version there
 * 		  is a glitch that allows X and O values to overlap each other >.<
 */

/* Class */
public class TicTacToeApp extends Application {
	
	// variables used in the program
	private boolean playable = true;
	private boolean turnX = true;
	private Tile[][] board = new Tile[3][3];
	private List<Combo> combos = new ArrayList<>();
	private Pane root = new Pane();
	
	
	// createContent method to establish a 6x6 grid
	private Parent createContent() {
		
		root.setPrefSize(600, 600); // size of window
		// create 6x6 grid 
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Tile tile = new Tile();
				tile.setTranslateX(j * 200);
				tile.setTranslateY(i * 200);
				
				root.getChildren().add(tile);
				
				board[j][i] = tile; // assign tile to each of the elements in the array
			}
		}
		
		// horizontal lines, y is incremented each loop
		for(int y = 0; y < 3; y++) {
			combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
		}
		
		// vertical lines
		for(int x = 0; x < 3; x++) {
			combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
		}
		
		// diagonal lines
		combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
		combos.add(new Combo(board[2][0], board[1][1], board[0][2]));
		
		return root;
	}
	
	// tile class
	private class Tile extends StackPane {
		private Text text = new Text();
		
		// Tile constructor to customize the way each tile looks
		public Tile() {
			Rectangle border = new Rectangle(200, 200);
			border.setFill(Color.AQUAMARINE);
			border.setStroke(Color.BLACK);
			
			text.setFont(Font.font(72));
			text.setFill(Color.CHOCOLATE);
			
			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);
			
			setOnMouseClicked(event -> {
				if(!playable) {
					return;
				}
				
				if(event.getButton() == MouseButton.PRIMARY) {
					if(!turnX) {
						return;
					}
					drawX();
					turnX = false;
					checkState();
				} else if (event.getButton() == MouseButton.SECONDARY) {
					if(turnX) {
						return;
					}
					drawO();
					turnX = true;
					checkState();
				}
			});
		}
		
		public double getCenterX() {
			return getTranslateX() + 100;
		}


		public double getCenterY() {
			return getTranslateY() + 100;
		}
		
		
		/*
		 * 3 possible values for each text-box, 
		 * by default -> empty
		 * left click -> X
		 * right click -> O
		 */
		public String getValue() {
			return text.getText();
		}
		
		private void drawX() {
			text.setText("X");
		}
		
		private void drawO() {
			text.setText("O");
		}
	}
	// go through all the possible combo and check all of them to see if winning combo.
	private void checkState() {
		for(Combo combo:combos) {
			if(combo.isComplete()) {
				playable = false;
				playWinAnimation(combo);
				break;
			}
		}
	}
	/* Method used for animating a line crossing 3 X's or 3 O's after a player has won */
	private void playWinAnimation(Combo combo) {
		Line line = new Line(); // create line object from line class
		line.setStartX(combo.tiles[0].getCenterX()); // set the start coordinate value for x
		line.setEndX(combo.tiles[0].getCenterX());   // set the end coordinate value for x
		line.setStartY(combo.tiles[0].getCenterY()); // set the start coordinate value for y
		line.setEndY(combo.tiles[0].getCenterY());	 // set the end coordinate value for y
		
		root.getChildren().add(line); // add line to the list of children on the scene
		
		Timeline timeline = new Timeline(); // create a timeline object for proper animation cycle
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), // animation cycle = 1sec
				new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
				new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
		timeline.play(); // play timeline animation
	}
	
	
	
	// data structure for checking if 3 X's or 3 O's in a row, column, or diagonal
	// 0 == 1, 0 == 2, therefore 0 == 1 == 2... X X X or O O O
	private class Combo {
		private Tile[] tiles;
		public Combo(Tile...tiles) {
			this.tiles = tiles;			
		}
		// isComplete method to tell whether or not a player won
		public boolean isComplete() {
			if(tiles[0].getValue().isEmpty()) {
				return false;
			} else {
				return tiles[0].getValue().equals(tiles[1].getValue()) 
						&& tiles[0].getValue().equals(tiles[2].getValue());
			}
		}
	}
	
	/* Main Method */
	public static void main(String[] args) {
		launch(args);
	}

	/* Start Stage */
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.setTitle("Tic-Tac-Toe™");
		primaryStage.show();
	}

}
