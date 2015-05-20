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
	
	private static double start_y=50;
	private static double max_y=300;
	private double range=max_y-start_y;
	
	private double g=300;
	private double boostV=-100;
	private double boostT=-boostV/g;
	private double boostD=(boostV*boostT)+(0.5*g*boostT*boostT);
	private Timeline timeline;

	
	private double calcTime(double dist, double v){
		return (-v+Math.sqrt((v*v)+2*g*dist))/g;
	}
	private void addActionEventHandler(){
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addMouseEventHandler();
				//TODO: start the drop animation of the bird
				if(timeline!=null){
					timeline.stop();
				}
				final double height = max_y-flappy.getY();
				final double duration = calcTime(height,0);
				timeline=new Timeline();
				KeyValue kv = new KeyValue(flappy.yProperty(), max_y, new Interpolator () {
					@Override
					protected double curve(double t) {
//						System.out.println(flappy.getY()+","+flappy.yProperty().get());
						double time = t * duration;
						double distance = (0.5*g) * time * time;
						double t2 = distance / height;
						if(flappy.getY()>=max_y){return 1;}
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
				if(timeline!=null){
					timeline.stop();
				}
				timeline=new Timeline();
				KeyValue kv = new KeyValue(flappy.yProperty(), flappy.getY()+boostD, new Interpolator () {
					@Override
					protected double curve(double t) {
						double time = t * boostT;
						double distance =  (boostV*time) + (0.5*g*time*time);
						double t2 = distance/boostD;
						return t2;
					}
				}
						);
				final KeyFrame kf = new KeyFrame(Duration.millis(boostT * 1000), kv);
				timeline.getKeyFrames().add(kf);
				
				final double height = max_y-flappy.getY();
				final double duration = calcTime(height,0);
				KeyValue kv1 = new KeyValue(flappy.yProperty(), max_y, new Interpolator () {
					@Override
					protected double curve(double t) {
						double time = t * duration;
						double distance = (0.5*g) * time * time;
						double t2 = distance / height;
						if(flappy.getY()>=max_y){return 1;}
						return t2;
					}
				});
				final KeyFrame kf1 = new KeyFrame(Duration.millis(duration * 1000), kv1);
				timeline.getKeyFrames().add(kf1);

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
		flappy.layoutXProperty().set(150);
		flappy.layoutYProperty().set(start_y);


		//TODO 3: add Button
		button = new Button("Start");
		button.layoutXProperty().set(150);


		//Create a Group 
		root = new Group( );
		root.getChildren().add(bkgrd);
		root.getChildren().add(ground);
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


