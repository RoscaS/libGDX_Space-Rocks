package com.spacerocks.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.framework.Box2DActor;
import com.spacerocks.ThrusterEffect;
import com.spacerocks.Thrusters;

public class Spaceship2 extends Box2DActor {

    public ThrusterEffect thrusterEffect;

    private Thrusters leftThruster;
    private Thrusters rightThruster;

    private final float rotationSpeed = 240;
    public final int acceleration = 2;
    public final int maxSpeed = 4;


    // public RayHandler handler;
    // public PointLight pointLight;


	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Spaceship2(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture("spaceship.png");

        setDynamic();
        setShapeRectangle();
        setPhysicsProperties(1, .5f, .1f);
        setMaxSpeed(maxSpeed);
        setAcceleration(acceleration);

        thrusterEffect = new ThrusterEffect();
        thrusterEffect.setPosition(0, 32);
        thrusterEffect.setRotation(90);
        thrusterEffect.setScale(.25f);
        addActor(thrusterEffect);

        // left thruster
        leftThruster = new Thrusters(0, 0, s, true);
        leftThruster.setOpacity(.5f);
        leftThruster.setScale(.75f);
        addActor(leftThruster);
        leftThruster.setPosition(0, getHeight());
        leftThruster.rotateBy(-90);

        // right thruster
        rightThruster = new Thrusters(0, 0, s, true);
        rightThruster.setOpacity(.5f);
        rightThruster.setScale(.75f);
        addActor(rightThruster);
        rightThruster.setPosition(0, -getHeight() / 4);
        rightThruster.rotateBy(90);

    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

    public void act(float dt) {
        super.act(dt);

        // System.out.println(getSpeed());

        spaceShipControl(dt);

        // shield.setOpacity(shieldPower / 100f);
        // if (shieldPower <= 0) shield.setVisible(false);



        wrapAroundWorld();
    }

	/*------------------------------*\
	|*				Getters			*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Setters			*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Private Methods 						*|
	\*------------------------------------------------------------------*/

    private void spaceShipControl(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // rotateBy(rotationSpeed * dt);
            getBody().setAngularVelocity(rotationSpeed * dt);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // rotateBy(-rotationSpeed * dt);
            getBody().setAngularVelocity(-rotationSpeed * dt);

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)) {
            // setAcceleration(2000);
            // accelerateAtAngle(getRotation() - 90);
            // setAcceleration(25);
            leftThruster.setVisible(true);
        } else {
            leftThruster.setVisible(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN)) {
            // setAcceleration(2000);
            // accelerateAtAngle(getRotation() + 90);
            // setAcceleration(25);
            rightThruster.setVisible(true);
        } else {
            rightThruster.setVisible(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // accelerateAtAngle(getRotation());
            thrusterEffect.start();
        } else {
            thrusterEffect.stop();
        }
    }


}
