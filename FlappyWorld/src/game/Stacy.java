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

public class Stacy extends Application{

	private ImageView bkgrd = null ;
	private ImageView grnd = null;
	private ImageView pipeTop = null;
	private ImageView pipeBot = null;
	private Button startButton = null;
	private Button resetButton = null;
	private static ImageView flappy = null ;
	private Group root = null;
	static boolean moving = true;
	final static double ground = 375;
	static double startHeight = 250; //initial height of bird
	final static double jumpVelocity = 40; //v_0 at which bird jumps up from current location (meters/second)
	final static double jumpDuration = jumpVelocity / 9.8; //calculation for jump duration (see above)
	final static double jumpHeight = jumpVelocity * jumpDuration - (4.9 * jumpDuration * jumpDuration); //calculation for jump distance (see above)
	static double fallHeight = 0; //INITIAL calculation for drop distance, gets redefined in start(Stage primaryStage)
	static double fallDuration = 0; //calculation for drop duration(see above)
	Timeline timeline = new Timeline();
	TranslateTransition transPipeTop = null;
	TranslateTransition transPipeBot = null;
	TranslateTransition transGround = null;
	private int pipeTopLoc = 0;
	private int pipeBotLoc = 0;

	private void addActionEventHandlerStart(){
		startButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle (ActionEvent event){
				startButton.disableProperty().set(true);
				addMouseEventHandlerFly();
				moveGround();
				pipeAppear();
				KeyValue fall = new KeyValue(flappy.yProperty(), ground-40, new Interpolator() {
					protected double curve(double t) {
						double cTime = fallDuration*t; //in sec the time elapsed
						double cDrop = (4.9*cTime*cTime); // distance dropped
						double c = (cDrop/fallHeight);
						checkCollision();
						return c; //convert to decimal progress
					}
				});
				KeyFrame flapEnd = new KeyFrame(Duration.seconds(fallDuration/3), fall);
				timeline.getKeyFrames().add(flapEnd);
				timeline.playFromStart();

				transPipeTop.playFromStart();
				transPipeBot.playFromStart();
			}
		});
	}

	//    private void addActionEventHandlerReset(){
	//    	resetButton.setOnAction(new EventHandler<ActionEvent>(){
	//    		public void handle (ActionEvent event){
	////    			root.getChildren().remove(resetButton);
	//    	    	timeline.playFromStart();
	//    	    	transPipeTop.playFromStart();
	//    	    	transPipeBot.playFromStart();
	//    	    	transGround.playFromStart();
	//    		}
	//    	});
	//    }

	private void flight(){
		timeline.stop();
		timeline = new Timeline();
		
		KeyValue rise = new KeyValue(flappy.yProperty(), flappy.yProperty().get() - jumpHeight, new Interpolator() {
			protected double curve(double t) { // t goes 0 to 1,
				double cTime = jumpDuration*t; //in sec the time elapsed
				double cJump = (jumpVelocity*cTime)-(4.9*cTime*cTime); //current height of the bird
				double c = (cJump/jumpHeight);
				checkCollision();
				return c; //convert to decimal progress from 0 to 1
			}
		});
		KeyFrame flapStart = new KeyFrame(Duration.seconds(jumpDuration/5), rise);
		timeline.getKeyFrames().add(flapStart);


		KeyValue fall = new KeyValue(flappy.yProperty(), ground-40, new Interpolator() {
			protected double curve(double t) {
				double cTime = fallDuration*t; //in sec the time elapsed
				double cDrop = (4.9*cTime*cTime); // distance dropped
				double c = (cDrop/fallHeight);
				checkCollision();
				return c; //convert to decimal progress
			}
		}); 
		KeyFrame flapEnd = new KeyFrame(Duration.seconds(fallDuration/3), fall);
		timeline.getKeyFrames().add(flapEnd);
		timeline.play();
	}

	private void checkCollision(){
		double pipeLocX = pipeTop.xProperty().get()+pipeTop.translateXProperty().get();

		if(flappy.intersects(pipeLocX, pipeTop.getY(), 52, 320) || flappy.intersects(pipeLocX, pipeBot.getY(), 52, 320) || flappy.getY()>=ground-50){
			end();
		}

//		if(flappy.intersects(pipeLocX, pipeBot.getY(), 52, 320)){
//			end();
//		}
//		//		
//		//    	if(pipeLocT <=flappy.getX() && pipeLocT+52>=flappy.getX() && pipeTop.getY()+320<=flappy.getY()){
//		//    		end();
//		//    	}
//		//    	if(pipeLocB <=flappy.getX() && (pipeLocB+52)>=flappy.getX() && pipeBot.getY()>=(flappy.getY()+24)){
//		//    		end();
//		//    	}
//		//System.out.println(flappy.getY() + "=====>" + ground);
//
//		if(flappy.getY()>=ground-50){
//			end();
//		}
	}


	private void end(){
		timeline.stop();
		transPipeTop.stop();
		transPipeBot.stop();
		transGround.stop();
		startButton.disableProperty().set(false);
	}

	private void moveGround(){
		transGround = new TranslateTransition(new Duration(3000), grnd);
		transGround.setToX(-400);
		transGround.setInterpolator(Interpolator.LINEAR);
		transGround.setCycleCount(Timeline.INDEFINITE);
		transGround.play();
	}

	private void pipeAppear(){

		pipeTop = new ImageView("obstacle_top.png");
		pipeBot = new ImageView("obstacle_bottom.png");


		
		pipeTopLoc = -250 + (int)(Math.random()*(100));
		pipeBotLoc = pipeTopLoc+450;

		pipeTop.yProperty().set(pipeTopLoc);
		pipeBot.yProperty().set(pipeBotLoc);

		root.getChildren().addAll(pipeTop, pipeBot);
		
		transPipeTop = new TranslateTransition(new Duration(3750), pipeTop);
		transPipeBot = new TranslateTransition(new Duration(3750), pipeBot);

		transPipeTop.setFromX(450);
		transPipeBot.setFromX(450);
		transPipeTop.setToX(-50);
		transPipeBot.setToX(-50);
		transPipeTop.setInterpolator(Interpolator.LINEAR);
		//transPipeTop.setCycleCount(Timeline.INDEFINITE);
		transPipeBot.setInterpolator(Interpolator.LINEAR);
		//transPipeBot.setCycleCount(Timeline.INDEFINITE);	

		transPipeTop.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				int pipeTopLoc = -250 + (int)(Math.random()*(100));

				pipeTop.yProperty().set(pipeTopLoc);
				pipeBot.yProperty().set(pipeTopLoc+450);
				transPipeTop.playFromStart();
				transPipeBot.playFromStart();
			}
		});
	}

	private void addMouseEventHandlerFly(){
		root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				flight();
			}
		});
	}

	private void addMouseEventHandlerReset(){
		root.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				timeline.playFromStart();
				transPipeTop.playFromStart();
				transPipeBot.playFromStart();
				transGround.playFromStart();
			}
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new Group();

		bkgrd = new ImageView("background.png");
		bkgrd.setFitWidth(400);

		grnd = new ImageView("ground.png");
		grnd.setFitWidth(800);
		grnd.yProperty().set(ground-20);



		flappy = new ImageView("flappy.png");
		flappy.xProperty().set(180);
		flappy.yProperty().set(ground-startHeight);

		startButton = new Button ("RUN");
		startButton.layoutXProperty().set(250);
		startButton.layoutYProperty().set(350);
		//        
		//        resetButton = new Button ("RESET");
		//        resetButton.layoutXProperty().set(100);
		//        resetButton.layoutYProperty().set(350);

		root.getChildren().add( bkgrd );
		root.getChildren().add( grnd );
		root.getChildren().add( flappy );
		root.getChildren().add(startButton);
		//root.getChildren().add(resetButton);

		fallHeight = ground - flappy.yProperty().get();
		fallDuration = Math.sqrt(fallHeight/4.9);

		addActionEventHandlerStart();
		//addActionEventHandlerReset();

		Scene scene = new Scene(root, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
