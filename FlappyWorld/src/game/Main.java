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

	static final double start_x = 150.0;
	static final double max_y = 350; // end position
	static final double start_y = 50; // starting y position
	double range_y = max_y-start_y; // total change in y distance

	static private double g = 98;
	static private double boostV = -10; // how much to boost with each click
	static private double boostT=Math.abs(boostV/g);
	static private double boostD=Math.abs((boostV*boostV)/(2*g));

	static private Timeline timeline;
	private Interpolator interpolator;
	private double y0= start_y; //current position
	private double y1=y0; //next position
	private double v0 = 0.0; // current velocity
	private double v1=0.0; //next velocity
	private double t0=0.0; //current time
	private double t1=0.0; //next time
	private double duration = 0.0; //time to reach max_y
//	
//	private void boost(){
//		v0 = boostV;
//		y-=calcDist();
//	}

	private void interpolate(){
		interpolator = new Interpolator(){
			@Override
			protected double curve (double t){
			
				t1=t-t0;
				t0=t;
				double time = t1 * duration;
				y0=y0+(v0*time)+((0.5)*(g*time*time));
				v0=v0+(g*time);
				if(y0>=max_y){
					v0=0;
				}else if (y0<=10){
					v0=g;
				}
				System.out.println(y0+","+v0);
				return (y0-start_y)/range_y;
			}
		
		};
	}
    private static double calcTime(double dist, double v0) {
    	return (Math.sqrt(v0*v0 + 2 * g * dist) + v0)/g;
    }
    
	private void addActionEventHandler(){
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addMouseEventHandler();

				//TODO: start the drop animation of the bird
            	if (timeline != null) {
            		timeline.stop();
            		t0 = 0.0;
            		y0=flappy.getY();
            		range_y=max_y-y0;
            	}
            	duration = calcTime(range_y,v0);
            	timeline=new Timeline();
            	KeyValue kv = new KeyValue(flappy.translateYProperty(), range_y, interpolator);
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
				if (timeline != null) {
            		timeline.stop();
            		t0 = 0.0;
            		y0=flappy.getY();
            		range_y=max_y-y0;
            	}
				duration += boostT;
				v0=boostV;
				timeline = new Timeline();
				KeyValue kv = new KeyValue(flappy.translateYProperty(), range_y, interpolator);
				final KeyFrame kf = new KeyFrame(Duration.millis(duration*1000), kv);
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
		bkgrd = new ImageView("background2.png");

		//TODO 2: add Flappy
		flappy = new ImageView("flappy.png");
		flappy.preserveRatioProperty().set(true);
		flappy.layoutXProperty().set(start_x);
		flappy.layoutYProperty().set(start_y);


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
		interpolate();
		//TODO 5: add mouse handler to the scene


		//Create scene and add to stage
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}


