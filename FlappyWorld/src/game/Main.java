package game;
import javafx.animation.Interpolator;
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


public class Main extends Application{
	
	private Button button = null;
	private Group root = null;
	private ImageView bkgrd = null ;
	private Node flappy = null;
	
    private void addActionEventHandler(){
    	button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//TODO: start the drop animation of the bird
            	TranslateTransition translateTransition = new TranslateTransition(Duration.millis(2000), flappy);
            	translateTransition.setFromY(flappy.getTranslateY());
        	translateTransition.setToY(300);
        	translateTransition.setInterpolator(new Interpolator() {
    			protected double curve(double t) {
    				double factor = .98;
    				return factor * t * t + (1-factor)*t ;
  			}
                  });
        	translateTransition.play();
            	
            }
        });
    }
    
    private void addMouseEventHandler(){
    }	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		//TODO 1: add background
		bkgrd = new ImageView("background.png");

		
		//TODO 2: add Flappy
		ImageView flappy = new ImageView("flappy.png");
		flappy.preserveRatioProperty().set(true);
		flappy.layoutXProperty().set(150);
		flappy.layoutYProperty().set(50);
		
		
		//TODO 3: add Button
		button = new Button("Start");
        	button.layoutXProperty().set(150);
		
		
		//Create a Group 
		root = new Group( );
		root.getChildren().add(bkgrd );
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
