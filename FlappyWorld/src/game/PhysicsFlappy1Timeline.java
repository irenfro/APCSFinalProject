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


public class PhysicsFlappy1Timeline extends Application {
			
		private Button button = null;
		private Group root = null;
		private ImageView bkgrd = null ;
		private ImageView flappy = null;
		static final double g = 9.8;
		private Timeline timeline;
		private double boost = 0;
		private double boostFactor = 0;
		private double boostTime = 0.0;
		private Interpolator interpolator;
		
		// Flappy's properties
		static final double start_x = 150.0;
		static final double max_y = 350;
		static final double range_y = 300;
		double start_y = max_y - range_y;
		private double y = start_y;
		private double yv = 0.0;
		private double prev_t = 0.0;
		double duration = 0.0; // time required to reach destination	
		
		private void addActionEventHandler(){
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	//TODO 5: add mouse handler to the scene
	        		addMouseEventHandler();
	            	
	        		//TODO: start the drop animation of the bird
	            	if (timeline != null) {
	            		timeline.stop();
	            		prev_t = 0.0;
	            		start_y = y;
	            	}
	            	
	            	duration = timeForDistAccel(max_y - start_y, yv, g);
	            	timeline = new Timeline();
	            	KeyValue kv = new KeyValue(flappy.translateYProperty(), range_y, interpolator);
	            	KeyFrame kf = new KeyFrame(Duration.millis(duration * 500), kv);
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
	            		prev_t = 0.0;
	            		start_y = y;
	            	}
	            	System.out.println("duration without boost: "+timeForDistAccel(max_y - start_y, yv, g));
	            	calculateBoostFactor();
	            	System.out.println("yv: " +yv + " bF: " +boostFactor);
	            	boost = boostFactor * g;
	            	boostTime += 0.1;
	            	double vf = yv + (g - boost) * boostTime;
	            	double delta_d = (boostTime * (vf + yv))/2;
	            	duration += boostTime + timeForDistAccel(max_y - start_y - delta_d, vf, g);
	            	timeline = new Timeline();
	            	KeyValue kv = new KeyValue(flappy.translateYProperty(), range_y, interpolator);
	            	KeyFrame kf = new KeyFrame(Duration.millis(duration * 500), kv);
	            	timeline.getKeyFrames().add(kf);
	            	timeline.play();
	            }
	        });
	    }
	    
	    private void calculateBoostFactor() {
	    	if(yv > 50) {
	    		boostFactor = 100;
	    	} else  if(yv < 50 && yv > 20){
	    		boostFactor = 50;
	    	} else {
	    		boostFactor = 25;
	    	}
	    }
	    
	    private static double timeForDistAccel(double dist, double v0, double a) {
	    	return 2*dist / (Math.sqrt(v0*v0 + 2 * a * dist) + v0);
	    }
	    
	    private void initInterpolator() {
	    	duration = timeForDistAccel(max_y - start_y, yv, g);
			interpolator = new Interpolator () {
				@Override
				protected double curve(double t) {
					double delta_t = t - prev_t;
					prev_t = t;            			
					double delta_t_sec = delta_t * duration;
					double a = g;
					if (boostTime > 0.0) {
						a -= boost;
						boostTime -= delta_t_sec;	
						if (boostTime < 0) {
							boostTime = 0;
						}
					}
					y = y + (yv * delta_t_sec) + ((a * delta_t_sec * delta_t_sec) / 2);
					yv = yv + a * delta_t_sec;
					if(y >= 350) {
						yv = 0;
					} else if(y <= 20) {
						yv = a / 2;
					}
					return (y - start_y) / (max_y - start_y);
				}
	    	};
	    }

		@Override
		public void start(Stage primaryStage) throws Exception {
			
			//TODO 1: add background
			bkgrd = new ImageView("background.png");

			//TODO 2: add Flappy
			flappy = new ImageView("flappy.png");
			flappy.preserveRatioProperty().set(true);
			flappy.layoutXProperty().set(start_x);
			flappy.layoutYProperty().set(start_y);

			//TODO 3: add Button
			button = new Button("Start");
			button.layoutXProperty().set(start_x);
			
			initInterpolator();
			
			//Create a Group 
			root = new Group( );
			root.getChildren().add(bkgrd );
			root.getChildren().add(flappy);
			root.getChildren().add(button);

			//TODO 4: add action handler to the button
			addActionEventHandler();

			//Create scene and add to stage
			Scene scene = new Scene(root, 400, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		}

		public static void main(String[] args) {
			Application.launch(args);
		}
	}




