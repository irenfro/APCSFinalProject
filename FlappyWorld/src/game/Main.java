package game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
//import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

//	private Button button = null;
	static private Group root = null;
	static private ImageView bkgrd = null ;
	static private ImageView flappy = null;
	static private ImageView ground = null;
	static private ImageView clickRun = null;
	static private ImageView instructions = null;
	static private ImageView getReady = null;
	static private ImageView gameOver=null;

	static final double g = 300, boostV = -150, sceneWidth=400, sceneHeight=400;
	static final double start_x = 150, start_y = 150; // starting y position
	static final double max_y = sceneHeight*0.9-23; // end position
	static double v, duration, range;
	static TranslateTransition movingGround;
	static Timeline timeline;
	static Interpolator interpolator;
	static boolean endGame;

	private double calcTime(double distance, double velocity){
		return ((-velocity+Math.sqrt(velocity*velocity+(2*g*distance)))/g);
	}
	private void checkLocation(){
		double threshold=5;
		if(flappy.getY()>=max_y-threshold){ // end game if hits bottom 
			endGame=true;
			movingGround.stop();
			timeline.stop();
			root.getChildren().add(gameOver);
		}
	}
	private void interpolator(){
		interpolator = new Interpolator(){
			@Override
			protected double curve (double t){
//				if (flappy.getY()<=10){ //if hits top, go to free fall
//					range=max_y-flappy.getY();
//					v=0;
//					duration = calcTime(range,v);
//				}
				double time =  t * duration;
				int distance = (int) ((v*time)+(0.5*g*time*time));	
				System.out.println(flappy.getY());
				checkLocation();
				return distance/range;
			}
		};
	}

	private void addMouseEventHandler(){
		root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
			int n=0;
			@Override
			public void handle(MouseEvent event) {
				n++;
				if(n==1){
					moveGround(); 
					root.getChildren().remove(clickRun);					
					root.getChildren().add(instructions);
					root.getChildren().add(getReady);
				}
				else{
					root.getChildren().remove(instructions);
					root.getChildren().remove(getReady);
					if(!endGame){
						if(timeline!=null){
							timeline.stop();
						}
						range = max_y-flappy.getY();
						v=boostV;	
						duration = calcTime(range,boostV);
						timeline = new Timeline();
						KeyValue kv = new KeyValue(flappy.yProperty(),max_y, interpolator);
						final KeyFrame kf = new KeyFrame(Duration.millis(duration * 1000), kv);	
						timeline.getKeyFrames().add(kf);
//						checkLocation();
						timeline.play();
					}
				}
			}
		});
	}

	private ImageView moveGround(){
		movingGround = new TranslateTransition(new Duration(2000), ground);
		movingGround.setToX(-sceneWidth);
		movingGround.setInterpolator(Interpolator.LINEAR);
		movingGround.setCycleCount(Timeline.INDEFINITE);
		movingGround.play();
		return ground;
	}

	//	private ImageView moveObstacles(){
	//		
	//	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		//TODO 1: add background
		ground = new ImageView("ground.png");
		ground.setLayoutX(0);
		ground.setLayoutY(sceneHeight*0.9);
		ground.setFitWidth(sceneWidth*2);

		bkgrd = new ImageView("background2.png");

		//TODO 2: add Flappy
		flappy = new ImageView("flappy.png");
		flappy.preserveRatioProperty().set(true);
		flappy.xProperty().set(start_x);
		flappy.yProperty().set(start_y);


		//initial displays
		clickRun= new ImageView("clickrun.png");
		clickRun.setLayoutX(sceneWidth/6);
		clickRun.setLayoutY(sceneHeight/2);
		getReady = new ImageView("getready.png");
		getReady.setLayoutX(sceneWidth/4);
		getReady.setLayoutY(sceneHeight/6);
		instructions = new ImageView("instructions.png");
		instructions.setLayoutX(sceneWidth/3.5);
		instructions.setLayoutY(sceneHeight/2);
		gameOver = new ImageView("gameover.png");
		gameOver.setLayoutX(sceneWidth/4);
		gameOver.setLayoutY(sceneHeight/4);

		//Create a Group 
		root = new Group( );
		root.getChildren().add(bkgrd );
		root.getChildren().add(ground );
		root.getChildren().add(flappy);
		root.getChildren().add(clickRun);

		System.out.println(max_y);
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

