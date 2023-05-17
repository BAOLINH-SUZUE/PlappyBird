package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class PlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;
	Texture gameover;
	Texture[] birds;
	int flapState= 0;
	float birdY = 0;
	float velocity = 0;
	Circle birdcircle;
	int score = 0;
	int scoringTube= 0;
	BitmapFont font;
	int gameState = 0;
	float gravity = 2;
	Texture topTube;
	Texture bottomTube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;

	float tubeVelocity= 4;

	int numberOfTubes= 4;
	float []tubeX = new  float[numberOfTubes];
	float []tubeOffset= new float[numberOfTubes];
	float distanceBetweenTheTube;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangles;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");
		//ShapeRenderer shapeRenderer= new ShapeRenderer();
		birdcircle= new Circle();

		// create bird
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);


		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		//distanceBetweenTheTube = Gdx.graphics.getHeight()* 3/4;
		topTubeRectangle = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		startGame();

	}
	public void startGame()
	{
		birdY= Gdx.graphics.getHeight()/2 - birds[flapState].getHeight()/2;
		for (int i = 0; i< numberOfTubes; i++)
		{
			tubeOffset[i]= (randomGenerator.nextFloat()- 0.5f ) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth() +i * distanceBetweenTheTube ;
			topTubeRectangle[i]= new Rectangle();
			bottomTubeRectangles[i]= new Rectangle();

		}
	}
	@Override
	public void render () {
		batch.begin();
		//create background
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState == 1) {
			// score
			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				Gdx.app.log("Score", String.valueOf(score));
				score++;
				if (scoringTube < 4) {
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}


			if (Gdx.input.justTouched()) {
				velocity = -30;

			}
			for (int i = 0; i < numberOfTubes; i++) {

				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTheTube;
				//	tubeOffset[i]= (randomGenerator.nextFloat()- 0.5f ) * (Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}
				//Create bottom and toptube and align
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);


				topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

			//prevent the bird from leaving the screen
			if (birdY > 0|| velocity <0) {
				velocity = velocity + gravity;
				birdY -= velocity;

			} else {
				gameState = 2;
			}


		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}

		}else  if (gameState ==2)
		{

			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight());
			if(Gdx.input.justTouched())
			{
				gameState = 1;
				startGame();
				score= 0;
				scoringTube= 0;
			velocity =0;
			}
			}

			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}

			//create flapping effect
			batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
			font.draw(batch, String.valueOf(score), 100, 200);

			batch.end();

			birdcircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);
			// shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.RED);
			//shapeRenderer.circle(birdcircle.x,birdcircle.y, birdcircle.radius);

			for (int i = 0; i < numberOfTubes; i++) {
				//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(),topTube.getHeight());
				//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
				if (Intersector.overlaps(birdcircle, topTubeRectangle[i]) || Intersector.overlaps(birdcircle, bottomTubeRectangles[i])) {
					gameState = 2;
				}
			}
			//shapeRenderer.end();
		}

}
