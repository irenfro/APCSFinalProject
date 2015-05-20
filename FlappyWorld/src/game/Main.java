package game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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

	static final double g = 300, boostV = -150, sceneWidth=400, sceneHeight=400;
	static final double start_x = 150, start_y = 150; // starting y position
	static final double max_y = sceneHeight*0.9-23; // end position
	static double v, duration, range;
	static Timeline timeline;
	static Interpolator interpolator;
	static boolean endGame;

	private double calcTime(double distance, double velocity){
		return ((-velocity+Math.sqrt(velocity*velocity+(2*g*distance)))/g);
	}
	private void checkLocation(){
		if(flappy.getY()>=max_y){ // end game if hits bottom
			endGame=true;
		}
	}
	private void interpolator(){
		interpolator = new Interpolator(){
			@Override
			protected double curve (double t){
				checkLocation();
				 if (flappy.getY()<=10){ //if hits top, go to free fall
					range=max_y-flappy.getY();
					v=0;
					duration = calcTime(range,v);
				}
				double time = t * duration;
				double distance = (v*time)+(0.5*g*time*time);	
				
				return distance/range;
			}
		};
	}
	
	private void addMouseEventHandler(){
		root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				checkLocation();
				if(timeline!=null){
					timeline.stop();
				}
				if(!endGame){
				range = max_y-flappy.getY();
				v=boostV;	
				duration = calcTime(range,boostV);
				timeline = new Timeline();
				KeyValue kv = new KeyValue(flappy.yProperty(),max_y, interpolator);
				final KeyFrame kf = new KeyFrame(Duration.millis(duration * 1000), kv);	
				timeline.getKeyFrames().add(kf);
				timeline.play();
				}
			}
		});
	}

	private ImageView movingGround(double x){
		ImageView ground = new ImageView("ground.png");
		ground.setLayoutX(0);
		ground.setLayoutY(sceneHeight*0.9);
		ground.setFitWidth(sceneWidth*2);
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
		bkgrd = new ImageView("background2.png");

		//TODO 2: add Flappy
		flappy = new ImageView("flappy.png");
		flappy.preserveRatioProperty().set(true);
		flappy.xProperty().set(start_x);
		flappy.yProperty().set(start_y);

		//Create a Group 
		root = new Group( );
		root.getChildren().add(bkgrd );
		root.getChildren().add(ground );
		root.getChildren().add(flappy);

		//TODO 4: add action handler to the button
		addMouseEventHandler();
		interpolator();
		
		//TODO 5: add mouse handler to the scene


		//Create scene and add to stage
		Scene scene = new Scene(root, sceneWidth, sceneHeight);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}

