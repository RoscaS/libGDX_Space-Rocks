package com.spacerocks.actors;

import box2dLight.ConeLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.framework.Box2DActor;
import com.effects.ThrusterEffect;
import com.spacerocks.screens.LevelScreen;

public class Spaceship extends Box2DActor {

    // particles
    public ThrusterEffect mainThruster;

    // sprites
    private Thrusters rightThruster;
    private Thrusters leftThruster;
    private Thrusters frontRightThruster;
    private Thrusters frontLeftThruster;
    private Thrusters backRightThruster;
    private Thrusters backLeftThruster;

    // lights
    private ConeLight flashlight;
    private ConeLight thrusterlight;

    // controle
    private boolean mainThrusterOn;
    private boolean flashlightOn;
    public int rotating;      // -1: left;     1: right;      0: none
    public int stabilisation;

    // const
    private final float thrusterMaxLight = 4;
    private final float rotationSpeed = 220;
    private final int acceleration = 2;
    private final int maxSpeed = 16;



	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Spaceship(float x, float y, Stage s, World w) {
        super(x, y, s, w);
        loadTexture("spaceship.png");

        setDynamic();
        setShapeRectangle();
        setPhysicsProperties(1, .5f, .1f);
        setMaxSpeed(maxSpeed);
        setAcceleration(acceleration);

        mainThrusterOn = false;
        flashlightOn = true;

        rotating = 0;
        stabilisation = 0;

        // particles
        mainThruster = new ThrusterEffect();
        mainThruster.setPosition(0, 32);
        mainThruster.setRotation(90);
        mainThruster.setScale(.25f);
        addActor(mainThruster);
        mainThruster.stop();

        // sprites
        initGazThrusters();

        // flashlight
        flashlight = new ConeLight(LevelScreen.HANDLER, 1000, Color.WHITE, 0, 0, 0, 0, 60);
        flashlight.setSoft(true);
        flashlight.setSoftnessLength(.9f);

        // thrusterlight
        thrusterlight = new ConeLight(LevelScreen.HANDLER, 265, Color.SCARLET, 0, 10, 10, 0, 25);
        thrusterlight.setSoft(true);
        thrusterlight.setSoftnessLength(1);
    }

    @Override
    protected void postConstruction() {
        super.postConstruction();
        getBody().setGravityScale(0);
        flashlight.attachToBody(getBody());
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

    @Override
    public void act(float dt) {
        super.act(dt);

        mainThrusterLightUpdate(dt);
        spaceShipControl(dt);
        stabilisationAnimation(dt);

        wrapAroundWorld();
    }


    public void shoot() {
        if (getStage() == null) return;

        Laser laser = new Laser(0, 0, this.getStage(), getWorld());
        laser.postConstruction();
        laser.getBody().setGravityScale(0);
        putAhead(laser, 100);

        laser.setRotationAngle(getRotationAngle());
        laser.setFixedRotation();
        laser.setRotation(getRotation());
        laser.light.attachToBody(laser.getBody());
        laser.moveForward();
    }

	/*------------------------------*\
	|*				Getters			*|
	\*------------------------------*/

    public ConeLight getFlashlight() {
        return flashlight;
    }

	/*------------------------------*\
	|*				Setters			*|
	\*------------------------------*/

    public void setMainThrusterOn() {
        mainThrusterOn = true;
        mainThruster.start();
    }

    public void setMainThrusterOff() {
        mainThrusterOn = false;
        mainThruster.stop();
    }

	/*------------------------------------------------------------------*\
	|*							Private Methods 						*|
	\*------------------------------------------------------------------*/

    private void toggleFlashlight() {
        flashlightOn = !flashlightOn;
        flashlight.setActive(flashlightOn);
    }

    private void mainThrusterLightUpdate(float dt) {
        float x = (getX() + getWidth() / 2) / Box2DActor.PPM;
        float y = (getY() + getHeight() / 2) / Box2DActor.PPM;
        thrusterlight.setDirection(getRotationAngle() + 180);
        thrusterlight.setPosition(x, y);

        if (mainThrusterOn && thrusterlight.getDistance() < thrusterMaxLight) {
            thrusterlight.setDistance(thrusterlight.getDistance() + (30 * dt));
        }
        if (mainThrusterOn && thrusterlight.getDistance() >= thrusterMaxLight) {
            thrusterlight.setDistance(thrusterMaxLight - 1.5f);
        }
        if (!mainThrusterOn && thrusterlight.getDistance() > 0) {
            thrusterlight.setDistance(thrusterlight.getDistance() - (20 * dt));
        }
    }

    private void stabilisationAnimation(float dt) {
        if (rotating == -1 && stabilisation > 0) {
            if (stabilisation > 50) {
                rotateRightAnimation();
            } else {
                if (stabilisation % 5 == 0) {
                    rotateRightAnimation();
                }
                if (stabilisation % 3 == 0) {
                    stopRotateRightAnimation();
                }
            }
            stabilisation--;
        }

        if (rotating == 1 && stabilisation > 0) {
            if (stabilisation > 50) {
                rotateLeftAnimation();
            } else {
                if (stabilisation % 5 == 0) {
                    rotateLeftAnimation();
                }
                if (stabilisation % 3 == 0) {
                    stopRotateLeftAnimation();
                }
            }
            stabilisation--;
        }

        if (stabilisation == 1) {
            stabilisation = 0;
            rotating = 0;
        }
    }

    public void rotateLeft(float dt) {
        getBody().setAngularVelocity(rotationSpeed * dt);

        if (MathUtils.random(0, 1) == 1) {
            rotateLeftAnimation();
        } else {
            stopRotateLeftAnimation();
        }
        rotating = -1;
    }

    public void rotateLeftAnimation() {
        frontRightThruster.setVisible(true);
        backLeftThruster.setVisible(true);
    }

    public void stopRotateLeftAnimation() {
        frontRightThruster.setVisible(false);
        backLeftThruster.setVisible(false);
    }


    public void rotateRight(float dt) {
        getBody().setAngularVelocity(-rotationSpeed * dt);
        if (MathUtils.random(0, 1) == 1) {
            rotateRightAnimation();
        } else {
            stopRotateRightAnimation();
        }
        rotating = 1;

    }

    public void rotateRightAnimation() {
        frontLeftThruster.setVisible(true);
        backRightThruster.setVisible(true);
    }

    public void stopRotateRightAnimation() {
        frontLeftThruster.setVisible(false);
        backRightThruster.setVisible(false);
    }


    private void spaceShipControl(float dt) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rotateLeft(dt);
        } else {
            stopRotateLeftAnimation();
            if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rotating == -1 && stabilisation == 0) {
                stabilisation = 50;
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rotateRight(dt);
        } else {
            stopRotateRightAnimation();
            if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && rotating == 1 && stabilisation == 0) {
                stabilisation = 50;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)) {
            applyImpluse(.1f, -90f);
            leftThruster.setVisible(true);
        } else {
            leftThruster.setVisible(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN)) {
            applyImpluse(.1f, 90f);
            rightThruster.setVisible(true);
        } else {
            rightThruster.setVisible(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            toggleFlashlight();
        }
    }

    private void initGazThrusters() {
        // left thruster
        leftThruster = new Thrusters(0, 0, getStage(), true);
        leftThruster.setOpacity(.5f);
        leftThruster.setScale(.75f);
        addActor(leftThruster);
        leftThruster.setPosition(0, getHeight());
        leftThruster.rotateBy(-90);

        // right thruster
        rightThruster = new Thrusters(0, 0, getStage(), true);
        rightThruster.setOpacity(.5f);
        rightThruster.setScale(.75f);
        addActor(rightThruster);
        rightThruster.setPosition(0, -getHeight() / 4);
        rightThruster.rotateBy(90);


        // front left thruster
        frontLeftThruster = new Thrusters(0, 0, getStage(), true);
        frontLeftThruster.setOpacity(.3f);
        frontLeftThruster.setScale(.75f);
        addActor(frontLeftThruster);
        frontLeftThruster.setPosition(getWidth() * .6f, getHeight() - getHeight() * .3f);
        frontLeftThruster.rotateBy(-90);
        frontLeftThruster.setVisible(false);


        // front right thruster
        frontRightThruster = new Thrusters(0, 0, getStage(), true);
        frontRightThruster.setOpacity(.3f);
        frontRightThruster.setScale(.75f);
        addActor(frontRightThruster);
        frontRightThruster.setPosition(getWidth() * .6f, getHeight() - getHeight() * .9f);
        frontRightThruster.rotateBy(90);
        frontRightThruster.setVisible(false);


        // back left thruster
        backLeftThruster = new Thrusters(0, 0, getStage(), true);
        backLeftThruster.setOpacity(.3f);
        backLeftThruster.setScale(.75f);
        addActor(backLeftThruster);
        backLeftThruster.setPosition(-getWidth() * .35f, getHeight() - getHeight() * .4f);
        backLeftThruster.rotateBy(-90);
        backLeftThruster.setVisible(false);


        // back right thruster
        backRightThruster = new Thrusters(0, 0, getStage(), true);
        backRightThruster.setOpacity(.3f);
        backRightThruster.setScale(.75f);
        addActor(backRightThruster);
        backRightThruster.setPosition(-getWidth() * .35f, getHeight() - getHeight() * .85f);
        backRightThruster.rotateBy(90);
        backRightThruster.setVisible(false);
    }
}
