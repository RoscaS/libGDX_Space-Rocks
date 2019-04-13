package com.spacerocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Spaceship extends BaseActor {

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public Spaceship(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("spaceship.png");
        setBoundaryPolygon(8);

        setAcceleration(200);
        setMaxSpeed(100);
        setDeceleration(10);
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

	public void act(float dt) {
	    super.act(dt);

	    float degreesPerSecond = 120; // rotation speed
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) rotateBy(degreesPerSecond * dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) rotateBy(degreesPerSecond * dt);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) rotateBy(getRotation());

        applyPhysics(dt);
        wrapAroundWorld();
    }
}
