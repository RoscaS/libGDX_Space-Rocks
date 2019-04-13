package com.spacerocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Spaceship extends BaseActor {

    private Thrusters mainThruster;
    private Thrusters leftThruster;
    private Thrusters rightThruster;


	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Spaceship(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture("spaceship.png");
        setBoundaryPolygon(8);

        setAcceleration(400);
        setMaxSpeed(1000);
        setDeceleration(0);

        mainThruster = new Thrusters(0, 0, s);
        addActor(mainThruster);
        mainThruster.setPosition(-mainThruster.getWidth(), getHeight() / 2 - mainThruster.getHeight() / 2);

        leftThruster = new Thrusters(0, 0, s, true);
        leftThruster.setOpacity(.5f);
        leftThruster.setScale(.75f);
        addActor(leftThruster);
        leftThruster.setPosition(0, getHeight() );
        leftThruster.rotateBy(-90);

        rightThruster = new Thrusters(0, 0, s, true);
        rightThruster.setOpacity(.5f);
        rightThruster.setScale(.75f);
        addActor(rightThruster);
        rightThruster.setPosition(0, -getHeight()/4 );
        rightThruster.rotateBy(90);
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

    public void act(float dt) {
        super.act(dt);

        float degreesPerSecond = 240; // rotation speed
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) rotateBy(degreesPerSecond * dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) rotateBy(-degreesPerSecond * dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)) {
            setAcceleration(2000);
            accelerateAtAngle(getRotation() - 90);
            setAcceleration(400);
            leftThruster.setVisible(true);
        } else {
            leftThruster.setVisible(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN)) {
            setAcceleration(2000);
            accelerateAtAngle(getRotation() + 90);
            setAcceleration(400);
            rightThruster.setVisible(true);
        } else {
            rightThruster.setVisible(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            accelerateAtAngle(getRotation());
            mainThruster.setVisible(true);
        } else {
            mainThruster.setVisible(false);
        }

        applyPhysics(dt);
        wrapAroundWorld();
    }
}
