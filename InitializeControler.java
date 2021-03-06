package UI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import GameCore.BoardContents;
import GameCore.Castle;
import GameCore.Cavalry;
import GameCore.Game;
import GameCore.HeadQuarter;
import GameCore.Player;
import GameCore.Soldier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class InitializeControler {
	@FXML private AnchorPane bigPane;
	@FXML private Label label;
	private Player turn;	
	private ArrayList<ImageView> tempImgs = new ArrayList<ImageView>();
	static Game game = new Game();

	
	
	public void initialize() {
		turn = game.getPlayer1();
		drawRawBoard();
	}
	
	//----------------------making the base ready to accept a pawn----------------------//
	@FXML 
	private void setCavalry() {
		Game.setContent(BoardContents.CAVALRY);
		Cavalry.setHorizontalSw(false);
	}
	@FXML 
	private void setCavalry2() {
		Game.setContent(BoardContents.CAVALRY);
		Cavalry.setHorizontalSw(true);
	}
	@FXML 
	private void setSoldier() {
		Game.setContent(BoardContents.SOLDIER);
	}
	@FXML
	private void setCastle() {
		Game.setContent(BoardContents.CASTLE);
	}
	@FXML 
	private void setHeadQuarter() {
		Game.setContent(BoardContents.HEADQUARTER);
	}
	
	//---------------------------------placing the pawns-------------------------------//
	@FXML
	private void placePawn(MouseEvent event) throws IOException {
		boolean xBorder = 100 < event.getSceneX() && event.getSceneX() < 100 + Game.boardWidth;
		boolean yBoarder = 100 < event.getSceneY() && event.getSceneY() < 100 + Game.boardWidth;
		if (xBorder && yBoarder) {
			placePawnOnBoard(event, turn);
		}
	}
	
	private void placePawnOnBoard(MouseEvent event, Player player) throws IOException {
		
		//the following statements check if we have valid pawns left and will place pawns on players board
		if(Game.getContent() == BoardContents.SOLDIER && player.getSoldierCount() < Player.MAX_SOLDIER_COUNT) {
			Soldier.placeSoldier(event.getSceneX(), event.getSceneY(),player);
		}
		if(Game.getContent() == BoardContents.CAVALRY && player.getCavalryCount() < Player.MAX_CAVALRY_COUNT && Cavalry.isHorizontalSw()) {
			Cavalry.placeCavalry(event.getSceneX(), event.getSceneY(),player);
		}
		if(Game.getContent() == BoardContents.CAVALRY && player.getCavalryCount() < Player.MAX_CAVALRY_COUNT && !Cavalry.isHorizontalSw()) {
			Cavalry.placeCavalry(event.getSceneX(), event.getSceneY(),player);
		}
		if(Game.getContent() == BoardContents.CASTLE && player.getCastleCount() < Player.MAX_CASTLE_COUNT) {
			Castle.placeCastle(event.getSceneX(), event.getSceneY(),player);
		}
		if(Game.getContent() == BoardContents.HEADQUARTER && player.getHeadquarterCount() < Player.MAX_HEADQU_COUNT) {
			HeadQuarter.placeHeadQuarter(event.getSceneX(), event.getSceneY(),player);
		}
		
		//setting nonempty places on board
		drawXs(turn);
		
		//check for placing status , if done then....
		if (game.getPlayer1().areAllPawnsPlaced() && turn == game.getPlayer1()) {
			drawRawBoard();
			turn = game.getPlayer2();
			label.setText("player two");
		}
		if (game.getPlayer2().areAllPawnsPlaced()) setToGameScene();
	}
	
	//-------------------------------drawing methods-----------------------------//
	public void drawXs(Player player) throws FileNotFoundException {
		double width = (double)Game.boardWidth / Game.boardSize, initX = 100 , initY = 100;

		
		for (int i = 0 ; i < Game.boardSize ; i++) {
			for (int j = 0 ; j < Game.boardSize ; j++) {
				if (player.getGameBoard().getBoardArray()[i][j] != BoardContents.EMPTY) {
					
					 Image image = new Image(new FileInputStream("D:\\midproject\\pics\\final pics\\X.png"));  
				     ImageView imageView = new ImageView(image); 
				      
				     double curX = initX + ( j * width);
				     double curY = initY + (i* width);
				     
				     imageView.setX(curX); 
				     imageView.setY(curY); 
				     imageView.setFitHeight(width); 
				     imageView.setFitWidth(width); 

				    bigPane.getChildren().add(imageView);
				}
			}
		}		
	}
	public void drawTempXs(Player player) throws FileNotFoundException {
		
		double width = (double)Game.boardWidth / Game.boardSize, initX = 100 , initY = 100;
	
		for (int i = 0 ; i < Game.boardSize ; i++) {
			for (int j = 0 ; j < Game.boardSize ; j++) {
				if (player.getGameBoard().getBoardArray()[i][j] != BoardContents.EMPTY) {
					
					 Image image = new Image(new FileInputStream("D:\\midproject\\pics\\final pics\\O.png"));  
				     ImageView imageView = new ImageView(image); 
				      
				     double curX = initX + ( j * width);
				     double curY = initY + (i* width);
				     
				     imageView.setX(curX); 
				     imageView.setY(curY); 
				     imageView.setFitHeight(width-5); 
				     imageView.setFitWidth(width-5); 
				     tempImgs.add(imageView);
				     bigPane.getChildren().add(imageView);
				}
			}
		}		
	}	
	private void drawRawBoard() {
		double curX, curY, width = (double)Game.boardWidth / Game.boardSize, initX = 100 , initY = 100;
		
		//drawing background
		Rectangle r = new Rectangle(initX,initY, initX + Game.boardWidth, initY + Game.boardWidth);
		r.setFill(Color.rgb(14, 29, 76));
		bigPane.getChildren().add(r);
		
		for (curX = initX ; curX <= initX + Game.boardWidth ; curX += width) {
			Line l1 = new Line(curX, initY, curX, initY +  Game.boardWidth);
			l1.setStroke(Color.WHITE);				
			l1.setFill(Color.WHITE);
			bigPane.getChildren().add(l1);
		}
		
		//drawing horizontal lines
		for (curY = initY ; curY <= initY + Game.boardWidth ; curY += width ) {
			Line l1 = new Line(initX , curY ,initX +  Game.boardWidth,curY);
			l1.setStroke(Color.WHITE);
			l1.setFill(Color.WHITE);
		    bigPane.getChildren().add(l1);

		}		
	}
	@FXML
	private void drawInitBoard(MouseEvent event) throws FileNotFoundException {
		for (ImageView a: tempImgs ) {
			bigPane.getChildren().remove(a);
		}
		tempImgs.clear();
		Player initPlayer = new Player();
		boolean xBorder = 100 < event.getSceneX() && event.getSceneX() < 100 + Game.boardWidth;
		boolean yBoarder = 100 < event.getSceneY() && event.getSceneY() < 100 + Game.boardWidth;

		if (xBorder && yBoarder) {		
			if(Game.getContent() == BoardContents.SOLDIER && turn.getSoldierCount() < Player.MAX_SOLDIER_COUNT) {
				Soldier.placeSoldier(event.getSceneX(), event.getSceneY(),initPlayer);
			}
			if(Game.getContent() == BoardContents.CAVALRY && Cavalry.isHorizontalSw() && turn.getCavalryCount() < Player.MAX_CAVALRY_COUNT) {
				Cavalry.placeCavalry(event.getSceneX(), event.getSceneY(),initPlayer);
			}
			if(Game.getContent() == BoardContents.CAVALRY && !Cavalry.isHorizontalSw() && turn.getCavalryCount() < Player.MAX_CAVALRY_COUNT) {
				Cavalry.placeCavalry(event.getSceneX(), event.getSceneY(),initPlayer);
			}
			if(Game.getContent() == BoardContents.CASTLE && turn.getCastleCount() < Player.MAX_CASTLE_COUNT) {
				Castle.placeCastle(event.getSceneX(), event.getSceneY(),initPlayer );
			}
			if(Game.getContent() == BoardContents.HEADQUARTER && turn.getHeadquarterCount() < Player.MAX_HEADQU_COUNT) {
				HeadQuarter.placeHeadQuarter(event.getSceneX(), event.getSceneY(),initPlayer);
			}
		}
		drawTempXs(initPlayer);
	}
	//--------------------------changing scenes--------------------------//
	private void setToGameScene() throws IOException {
		Parent gameScene = FXMLLoader.load(getClass().getResource("MainGameScene.fxml"));
		Main.gameScene = new Scene(gameScene);
		Main.gameScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.gameScene);
	}
	@FXML
	private void backToMain() {
		Main.window.setScene(Main.mainMenu);
	}
	//-----------------getting game obj for the main game-------------------------//
	public static Game getGame() {
		return game;
	}
}