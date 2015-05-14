package game;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class Main extends Application{
	
	private Button button = null;
	private Group root = null;
	private ImageView bkgrd = null ;
	private Node flappy = null;
	
    private void addActionEventHandler(){
    }
    
    private void addMouseEventHandler(){
    }	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		//TODO 1: add background
		bkgrd = new ImageView("background.png");

		
		
		//TODO 2: add Flappy

		
		
		//TODO 3: add Button

		
		
		//Create a Group 
		root = new Group( );
		root.getChildren().add(bkgrd );
		
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
