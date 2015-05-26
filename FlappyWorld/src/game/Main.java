package game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private Group root = null;
	private ImageView bkgrd = null ;
	private ImageView flappy = null;
	private Ground ground = null;

	private boolean click = false;
	private Obstacle pipe = new Obstacle("/obstacle_bottom.png", "/obstacle_top.png");;
	private String url = getClass().getResource("/flappy.png").toString();

	static double g = 300;
	static final double boostV = -150;
	static final double sceneWidth=400;
	static final double sceneHeight=400;
	static final double start_x = 150, start_y = 150; // starting y position
	static final double max_y = sceneHeight*0.9-23; // end position
	static double v, duration, range;
	
	static Timeline timeline;
	static TranslateTransition transTransition;
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
				
				if (flappy.getY()<=10 && endGame){ //if hits top, go to free fall
					range=max_y-flappy.getY();
					v=0;
					duration = calcTime(range,v);
				}
				if(endGame) {
					animationStop();
				}
				double time = t * duration;
				double distance = (v*time)+(0.5*g*time*time);	
				checkCollision();
				print();
				return distance/range;
			}
		};
	}
	
	public void animationStop() {
		try {
			timeline.stop();
			ground.stop();
			pipe.stop();
		} catch (Exception e) {
			
		}
	}
	
	public void print() {
		double flappyY = (flappy.yProperty().doubleValue() + flappy.getTranslateY());
		double flappyX = (flappy.xProperty().doubleValue() + flappy.getTranslateX());
		System.out.println("flappy Y: " + flappyY);
		System.out.println("pipe Y: " + pipe.getY1());
		System.out.println("flappy X: " + flappyX);
		System.out.println("pipe X: " + pipe.getX1());
		System.out.println("flappyY: " + flappyY);
		System.out.println("pipeY: " + pipe.getY2());
		System.out.println("flappyX: " + flappyX);
		System.out.println("pipeX: " + pipe.getX2());
		
		
	}
	
	private void addMouseEventHandler(){
		root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
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

	@Override
	public void start(Stage primaryStage) throws Exception {

		//TODO 1: add background
		Ground ground = new Ground("/ground.png");
		ground.movingGround(sceneHeight, sceneWidth);
		bkgrd = new ImageView("background2.png");

		//TODO 2: add Flappy
		flappy = new ImageView(url);
		flappy.preserveRatioProperty().set(true);
		flappy.xProperty().set(start_x);
		flappy.yProperty().set(start_y);
		

		//Create a Group 
		root = new Group( );
		root.getChildren().add(bkgrd );
		root.getChildren().add(ground.getImageView() );
		root.getChildren().add(pipe.getImageView1());
		root.getChildren().add(pipe.getImageView2());
		root.getChildren().add(flappy);
		

		//TODO 5: add mouse handler to the scene
		addMouseEventHandler();
		interpolator();
		pipe.movingGround(sceneHeight, sceneWidth);
		
		

		//Create scene and add to stage
		Scene scene = new Scene(root, sceneWidth, sceneHeight);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	public void checkCollision() {
		double flappyY = (flappy.yProperty().doubleValue() + flappy.getTranslateY());
		double flappyX = (flappy.xProperty().doubleValue() + flappy.getTranslateX());
		if(flappyY >= pipe.getY1() && flappyX >= pipe.getX1()) {
			System.out.println("flappy Y: " + flappyY);
			System.out.println("pipe Y: " + pipe.getY1());
			System.out.println("flappy X: " + flappyX);
			System.out.println("pipe X: " + pipe.getX1());
			endGame = true;
		} else if(flappyY <= pipe.getY2() && flappyX >= pipe.getX2()) {
			System.out.println("flappyY: " + flappyY);
			System.out.println("pipeY: " + pipe.getY2());
			System.out.println("flappyX: " + flappyX);
			System.out.println("pipeX: " + pipe.getX2());
			endGame = true;
		}
		System.out.println(endGame);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}


