package game;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Obstacle {

	private ImageView bottom = null;
	private ImageView top = null;
	private int[] heights = {50, -50, 75, -75};
	private int i = 0;
	private TranslateTransition transTransition;
	private TranslateTransition TransTransition;
	private double sceneHeight;
	private double sceneWidth;
	private boolean bound = true;

	
	public Obstacle(String n1, String n2) {
		String url = getClass().getResource(n1).toString();
		this.bottom = new ImageView(url);
		url = getClass().getResource(n2).toString();
		this.top = new ImageView(url);
		
	}
	
	public void movingGround(double sceneHeight, double sceneWidth){
		this.sceneHeight = sceneHeight;
		this.sceneWidth = sceneWidth;
		this.bottom.setLayoutX(425);
		this.bottom.setLayoutY(sceneHeight*0.9 - (100 + heights[i % 3]));
		i++;
		this.top.setLayoutX(425);
		this.top.setLayoutY(bottom.getLayoutY() - 450);
		transTransition = new TranslateTransition(new Duration(2500), this.bottom);
		TransTransition  = new TranslateTransition(new Duration(2500), this.top);
		TransTransition.setToX(-sceneWidth - 75);
		TransTransition.setInterpolator(new Interpolator() {
			@Override
			protected double curve(double t) {
				if(t == 1) {
					bound = true;
					random();
				}
				if(t >= .7 && bound) {
					bound = false;
					Main.score += 1;
					System.out.println(Main.score);
				}
				return t;
			}
		});
		TransTransition.setCycleCount(Timeline.INDEFINITE);
		transTransition.setToX(-sceneWidth - 75);
		transTransition.setInterpolator(Interpolator.LINEAR);
		transTransition.setCycleCount(Timeline.INDEFINITE);
	}
	
	public void random() {
		this.bottom.setLayoutY(sceneHeight*0.9 - (100 + ((int) Math.random() * 200)));
		i++;
		this.top.setLayoutY(bottom.getLayoutY() - 450);

	}
	
	public ImageView getImageView1() {
		return this.bottom;
	}
	
	public ImageView getImageView2()  {
		return this.top;
	}
	
	public double getX1() {
		return bottom.layoutXProperty().doubleValue() + bottom.getTranslateX() + bottom.xProperty().doubleValue();
	}
	
	public double getX2() {
		return top.layoutXProperty().doubleValue() + top.getTranslateX() + top.xProperty().doubleValue();
	}
	
	public double getY1() {
		return bottom.layoutYProperty().doubleValue();// + bottom.getTranslateY() + bottom.yProperty().doubleValue();
	}
	
	public double getY2() {
		return top.layoutYProperty().doubleValue();// + top.getTranslateY() + top.yProperty().doubleValue();
	}
	
	public void play(){
		TransTransition.play();
		transTransition.play();
	}
	
	public void stop() {
		transTransition.stop();
		TransTransition.stop();
	}
	
	
}

