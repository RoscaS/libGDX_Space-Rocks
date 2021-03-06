package com.spacerocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.framework.BaseActor;

public class Spaceship extends BaseActor {

    private Thrusters mainThruster;
    private Thrusters leftThruster;
    private Thrusters rightThruster;

    private Shield shield;
    private int shieldPower;

    private final float rotationSpeed = 240;
    // private final float baseShieldOpacity = .70f;


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

        // main thruster
        mainThruster = new Thrusters(0, 0, s);
        addActor(mainThruster);
        mainThruster.setPosition(-mainThruster.getWidth(), getHeight() / 2 - mainThruster.getHeight() / 2);

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

        // shield
        shield = new Shield(0, 0, s);
        addActor(shield);
        shield.centerAtPosition(getWidth() / 2, getHeight() / 2);
        shieldPower = 100;
    }

    /*------------------------------*\
   	|*				Getters			*|
   	\*------------------------------*/

    public int getShieldPower() {
        return shieldPower;
    }


    /*------------------------------*\
   	|*				Setters			*|
   	\*------------------------------*/

    public void setShieldPower(int shieldPower) {
        this.shieldPower = shieldPower;
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

    public void act(float dt) {
        super.act(dt);

        spaceShipControl(dt);

        shield.setOpacity(shieldPower / 100f);
        if (shieldPower <= 0) shield.setVisible(false);

        applyPhysics(dt);
        wrapAroundWorld();
    }

    public void warp() {
        if (getStage() == null) return;

        Warp w1 = new Warp(0, 0, getStage());
        w1.centerAtActor(this);
        setPosition(MathUtils.random(1920), MathUtils.random(1080));
        Warp w2 = new Warp(0, 0, getStage());
        w2.centerAtActor(this);
    }

    public void shoot() {
        if (getStage() == null) return;
        Laser laser = new Laser(0, 0, this.getStage());
        laser.centerAtActor(this);
        laser.setRotation(getRotation());
        laser.setMotionAngle(getRotation());
    }

    /*------------------------------------------------------------------*\
   	|*							Private Methods 						*|
   	\*------------------------------------------------------------------*/

    private void spaceShipControl(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) rotateBy(rotationSpeed * dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) rotateBy(-rotationSpeed * dt);

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
    }
}
