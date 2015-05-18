package game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private Button button = null;
	private Group root = null;
	private ImageView bkgrd = null ;
	private ImageView flappy = null;

	private void addActionEventHandler(){
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO: start the drop animation of the bird
				final Timeline timeline = new Timeline();
				final double height = 10;
				final double a = 9.8;
				final double duration = Math.sqrt(2*height/a);
				KeyValue kv = new KeyValue(flappy.translateYProperty(), 290, new Interpolator () {
					@Override
					protected double curve(double t) {
						double time = t * duration;
						double distance = (0.5*a) * time * time;
						double t2 = distance / height;
						return t2;
					}
				});
				final KeyFrame kf = new KeyFrame(Duration.millis(duration * 1000), kv);
				timeline.getKeyFrames().add(kf);
				timeline.play();

			}
		});
	}

	private void addMouseEventHandler(){
		root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final Timeline timeline = new Timeline();
				final double v0 = -5;
				final double a = 9.8;
				final double height = 10;
				final double duration = ((-2*v0/a)+Math.sqrt(((4*v0*v0)/(a*a))+(8*height/a)))*0.5;
				KeyValue kv = new KeyValue(flappy.translateYProperty(), 290, new Interpolator () {
					@Override
					protected double curve(double t) {
						double time = t * duration;
						double distance =  (v0*time) + (0.5*a*time*time);
						double t2 = distance/height;
						return t2;
					}
				}
						);
				final KeyFrame kf = new KeyFrame(Duration.millis(duration * 1000), kv);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		});
	}	

	private ImageView movingGround(double x){
		ImageView ground = new ImageView("ground.png");
		ground.setLayoutX(0);
		ground.setLayoutY(364);
		ground.setFitWidth(800);
		TranslateTransition transTransition = new TranslateTransition(new Duration(2500), ground);
		transTransition.setToX(-400);
		transTransition.setInterpolator(Interpolator.LINEAR);
		transTransition.setCycleCount(Timeline.INDEFINITE);
		transTransition.play();
		return ground;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		//TODO 1: add background
		ImageView ground = movingGround(0);
		bkgrd = new ImageView("background.png");
		
		//TODO 2: add Flappy
		flappy = new ImageView("flappy.png");
		flappy.preserveRatioProperty().set(true);
		flappy.layoutXProperty().set(150);
		flappy.layoutYProperty().set(50);


		//TODO 3: add Button
		button = new Button("Start");
		button.layoutXProperty().set(150);


		//Create a Group 
		root = new Group( );
		root.getChildren().add(bkgrd );
		root.getChildren().add(ground );
		root.getChildren().add(flappy);
		root.getChildren().add(button);

		//TODO 4: add action handler to the button
		addActionEventHandler();

		//TODO 5: add mouse handler to the scene
		addMouseEventHandler();


		//Create scene and add to stage
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}


