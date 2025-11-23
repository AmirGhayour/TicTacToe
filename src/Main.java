
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	// ************************************************************ custom Rectangle
	public class Rectangle extends javafx.scene.shape.Rectangle {
		private int number;

		public Rectangle(int number) {
			super();
			this.number = number;
		}
	}

	// ************************************************************ main
	public static void main(String[] args) {
		launch(args);
	}

	private static boolean b;
	private Board.State player;
	private Board board;
	private boolean playWithAI, gameOver;
	Rectangle[] rectangle;
	Button againButton, player1, player2;
	ImageView imageWin, imageTurn;

	// ************************************************************ Start
	@Override
	public void start(Stage arg0) throws Exception {

		// ################################################# Initialization
		board = new Board();
		player = Board.State.X;
		playWithAI = true;
		gameOver = false;
		b = true; // prevent from open again when resetButtons are already open

		// ################################################# Buttons
		againButton = new Button();
		againButton.getStyleClass().add("againButton");
		againButton.relocate(332, 388);

		player1 = new Button();
		player1.getStyleClass().add("player1");
		player1.relocate(332, 388);
		player1.setVisible(false);

		player2 = new Button();
		player2.getStyleClass().add("player2");
		player2.relocate(312, 438);
		player2.setVisible(false);

		// ################################################# imageViews
		imageWin = new ImageView(new Image("images/Owins.png"));
		imageWin.relocate(130, 380);
		imageWin.setVisible(false);

		imageTurn = new ImageView(new Image("images/Xturn.png"));
		imageTurn.relocate(135, 395);
		imageTurn.setVisible(false);

		// ################################################# Game Board
		rectangle = new Rectangle[9];

		for (int i = 0; i < 9; i++) {
			rectangle[i] = new Rectangle(i);
			rectangle[i].setWidth(86);
			rectangle[i].setHeight(86);
			rectangle[i].getStyleClass().add("invisibleRect");
			rectangle[i].setOnMouseClicked(this::rectHandle);
		}

		rectangle[0].relocate(85, 68);
		rectangle[1].relocate(189, 68);
		rectangle[2].relocate(293, 68);
		rectangle[3].relocate(85, 170);
		rectangle[4].relocate(189, 170);
		rectangle[5].relocate(293, 170);
		rectangle[6].relocate(85, 272);
		rectangle[7].relocate(189, 272);
		rectangle[8].relocate(293, 272);

		// ################################################# Buttons Click Handle
		againButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (!b)
					return;

				imageWin.setVisible(false);
				imageTurn.setVisible(false);

				player1.setVisible(true);
				TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), player1);
				translate.setFromX(0);
				translate.setToX(-90);
				translate.setCycleCount(1);
				translate.play();

				ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), player1);
				scale.setCycleCount(1);
				scale.setFromX(0);
				scale.setFromY(0);
				scale.setToX(1);
				scale.setToY(1);
				scale.play();

				player2.setVisible(true);
				TranslateTransition translate2 = new TranslateTransition(Duration.seconds(0.5), player2);
				translate2.setFromX(0);
				translate2.setToX(-90);
				translate2.setToY(0);
				translate2.setCycleCount(1);
				translate2.play();

				ScaleTransition scale2 = new ScaleTransition(Duration.seconds(0.5), player2);
				scale2.setCycleCount(1);
				scale2.setFromX(0);
				scale2.setFromY(0);
				scale2.setToX(1);
				scale2.setToY(1);
				scale2.play();

				b = false;
			}
		});

		player1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				playWithAI = true;
				imageTurn.setVisible(false);
				reset();
			}
		});

		player2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				playWithAI = false;
				imageTurn.setVisible(true);
				reset();
			}
		});

		// ################################################# layout
		Pane root = new Pane(againButton, player1, player2, imageWin, imageTurn);

		for (int i = 0; i < 9; i++)
			root.getChildren().add(rectangle[i]);

		root.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				hideResetButtons();
			}
		});

		Scene scene = new Scene(root, 462, 518);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // get style
		arg0.setScene(scene);
		arg0.setTitle("TicTacToe");
		arg0.setResizable(false);
		arg0.show();
	}

	// ************************************************************ rectHandle
	private void rectHandle(MouseEvent event) {

		if (gameOver)
			return;

		Rectangle rect = (Rectangle) event.getTarget();

		if (rect.getFill() != Color.TRANSPARENT) // check move available or not
			return;

		board.move(rect.number);

		if (playWithAI && board.getMoves().size() > 1) {
			rect.getStyleClass().add("XRect");
			MiniMax.miniMax(board, player);
			rectangle[board.getLastMove()].getStyleClass().add("ORect");
		} else if (player == Board.State.X) {
			rect.getStyleClass().add("XRect");
			player = Board.State.O;
			imageTurn.setImage(new Image("images/Oturn.png"));
		} else {
			rect.getStyleClass().add("ORect");
			player = Board.State.X;
			imageTurn.setImage(new Image("images/Xturn.png"));
		}

		if (board.isGameOver()) {
			gameOver = true;
			imageTurn.setVisible(false);

			if (board.getWinner() == Board.State.X)
				imageWin.setImage(new Image("images/Xwins.png"));
			else if (board.getWinner() == Board.State.O)
				imageWin.setImage(new Image("images/Owins.png"));
			else
				imageWin.setImage(new Image("images/Draw.png"));

		}

	}

	// ************************************************************ hideResetButtons
	private void hideResetButtons() {
		player1.setVisible(false);
		player2.setVisible(false);

		if (!playWithAI && !board.isGameOver())
			imageTurn.setVisible(true);

		if (board.isGameOver())
			imageWin.setVisible(true);

		b = true;
	}

	// ************************************************************ reset
	private void reset() {
		imageWin.setVisible(false);
		imageTurn.setImage(new Image("images/Xturn.png"));
		gameOver = false;
		player = Board.State.X;
		board.reset();
		hideResetButtons();
		for (int i = 0; i < 9; i++) {
			rectangle[i].getStyleClass().clear();
			rectangle[i].getStyleClass().add("invisibleRect");
		}
	}

}
