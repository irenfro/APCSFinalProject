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
	static final double max_y = 300; // end position
	static final double start_y = 50; // starting y position
	double range_y = max_y-start_y; // total change in y distance
	private double y = start_y; //current position
	private double v = 0.0; // current velocity
	private double boostV = -10; // how much to boost with each click
	private double g = 9.8;

	private void boost(){
		v = boostV;
		y-=calcDist();
	}
	private double calcDist(){ // calculate distance to reach v=0 after boost
		return (boostV*boostV)/(2*g);
	}


	private void addActionEventHandler(){
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addMouseEventHandler();

				//TODO: start the drop animation of the bird
				final Timeline timeline = new Timeline();
				final double duration = Math.sqrt(2*range_y/g);
				KeyValue kv = new KeyValue(flappy.translateYProperty(), max_y, new Interpolator () {
					@Override
					protected double curve(double t) {
						double time =  t * duration;
						double y = ((0.5*g) * time * time);
						//						System.out.println(y);
						double t2 = y / range_y;
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
				boost();
				System.out.println(y);
				range_y=max_y-y;
				//				final double duration = ((-2*boostV/g)+Math.sqrt(((4*boostV*boostV)/(g*g))+(8*(range_y)/g)))*0.5;				
				//				final double y0=calcDist()+range_y;

				//				KeyValue kv = new KeyValue(flappy.translateYProperty(), max_y, new Interpolator () {
				//					@Override
				//					protected double curve(double t) {
				//						double time = t * duration;
				//						double y =  (boostV*time)+(0.5*g*time*time);
				//						double t2 = y/y0;
				//						return t2;
				//					}
				//				}
				//						);
				//				KeyValue kv1 = new KeyValue(flappy.translateYProperty(), 290, new Interpolator () {
				//					@Override
				//					protected double curve(double t) {
				//						double time = t * duration;
				//						double distance = (0.5*a) * time * time;
				//						double t2 = distance / height;
				//						return t2;
				//					}
				//				});

				final double duration = -boostV/g;
				KeyValue kv = new KeyValue(flappy.translateYProperty(), flappy.getTranslateY()-calcDist(), Interpolator.LINEAR);
				final KeyFrame kf = new KeyFrame(Duration.millis(duration*1000), kv);
				//				final KeyFrame kf1 = new KeyFrame(Duration.millis(duration * 1000), kv1);
				timeline.getKeyFrames().add(kf);
				//				timeline.getKeyFrames().add(kf1);

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


