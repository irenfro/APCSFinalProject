package game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MAIN extends Application{
	
	private ImageView bkgrd = null ;
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

    
    private void flight(){
    	timeline.stop();
    	
    	timeline = new Timeline();
    	
        debugPrint("Before Animation");
    	
    	KeyValue rise = new KeyValue(flappy.yProperty(), flappy.yProperty().get() - jumpHeight, new Interpolator() {
    			protected double curve(double t) { // t goes 0 to 1,
    				double cTime = jumpDuration*t; //in sec the time elapsed
    				double cJump = (jumpVelocity*cTime)-(4.9*cTime*cTime); //current height of the bird
    				double c = (cJump/jumpHeight);
    				return c; //convert to decimal progress from 0 to 1
    			}
            });
		KeyFrame birdRise = new KeyFrame(Duration.seconds(jumpDuration/5), rise);
        timeline.getKeyFrames().add(birdRise);

        
        KeyValue fall = new KeyValue(flappy.yProperty(), ground, new Interpolator() {
			protected double curve(double t) {
				double cTime = fallDuration*t; //in sec the time elapsed
				double cDrop = (4.9*cTime*cTime); // distance dropped
				double c = (cDrop/fallHeight);
				return c; //convert to decimal progress
			}
        }); 
    	KeyFrame birdFall = new KeyFrame(Duration.seconds(fallDuration/3), fall);
    	timeline.getKeyFrames().add(birdFall);
        timeline.play();
    }
    
    public static void debugPrint(String stage){
		System.out.println("\n"+stage);
		System.out.println("Jump Velocity: " + jumpVelocity);
		System.out.println("Jump Duration: " + jumpDuration);
		System.out.println("Jump Height: " + jumpHeight);
		System.out.println("Fall Height: " + fallHeight);
		System.out.println("Fall Duration: " + fallDuration);
		System.out.println(flappy.yProperty().get());
	}

    private void addMouseEventHandler(){
    	root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			flight();
    		}
        });
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create a Group 
		root = new Group();
		
		//TODO 1: add background
		bkgrd = new ImageView("background.png");
		bkgrd.setFitWidth(400);
		
		//TODO 2: add Flappy
		flappy = new ImageView("flappy.png");
		flappy.xProperty().set(180);
		flappy.yProperty().set(ground-startHeight);

		//Add controls
		root.getChildren().add( bkgrd );
		root.getChildren().add( flappy );
		
		fallHeight = ground - flappy.yProperty().get();
		fallDuration = Math.sqrt(fallHeight/4.9);
		
		
		
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

