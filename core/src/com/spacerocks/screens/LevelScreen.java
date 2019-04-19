package com.spacerocks.screens;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.framework.BaseActor;
import com.framework.BaseScreen;
import com.framework.Box2DActor;
import com.spacerocks.ExplosionEffect;
import com.spacerocks.Rock;

import com.spacerocks.actors.Spaceship2;
import com.spacerocks.actors.Sun;


public class LevelScreen extends BaseScreen {

    private Sun sun;
    private boolean gameOver;
    private Spaceship2 spaceship;

    private ConeLight sunlight;
    private ConeLight light;
    private ConeLight thrusterLight;

    private boolean thrusterOn;


    // Static
    private static final Float sunX = 3840f;
    private static final Float sunY = 2160f;
    private static final Float sunRotationCoeff = 2f;

    public static final World WORLD = new World(new Vector2(0, -9.8f), true);
    public static final RayHandler HANDLER = new RayHandler(WORLD);

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.loadTexture("space.png");
        space.setSize(1920, 1080);
        Box2DActor.setWorldBounds(space);

        gameOver = false;

        // world = BaseScreen.world;

        spaceship = new Spaceship2(1920 / 2, 1080 / 2, mainStage);
        spaceship.initializePhysics(WORLD);
        spaceship.getBody().setGravityScale(0);

        sun = new Sun(sunX, sunY, mainStage);
        sun.initializePhysics(WORLD);
        sun.getBody().setGravityScale(0);

        Rock r1 = new Rock(600, 500, mainStage);
        r1.initializePhysics(WORLD);
        r1.getBody().setGravityScale(0);

        Rock r2 = new Rock(600, 300, mainStage);
        r2.initializePhysics(WORLD);
        r2.getBody().setGravityScale(0);

        HANDLER.setBlurNum(6);
        lightsInitialization();
    }

	/*------------------------------------------------------------------*\
	|*							Static Methods 				    	    *|
	\*------------------------------------------------------------------*/

    public static void explosion(Box2DActor target) {
        ExplosionEffect boom = new ExplosionEffect();
        boom.centerAtActor(target);
        boom.start();
        target.getStage().addActor(boom);
    }

    /*------------------------------------------------------------------*\
   	|*							Update                                  *|
   	\*------------------------------------------------------------------*/

    public void update(float dt) {

        lightsUpdate(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            spaceship.getBody().setLinearDamping(0);
            spaceship.moveForward();
        }
    }

    private void lightsUpdate(float dt) {
        Vector2 pos = sun.getPosition();
        Color c = sunlight.getColor();

        float scaledSunX = sunX / Box2DActor.PPM;
        float extremum = 2 * scaledSunX;
        float colorTreshold = scaledSunX - scaledSunX * .9f;
        float maxGreen = .64f;
        float maxLight = 20;


        if (c.g >= maxGreen) {
            sunlight.setColor(c.r, maxGreen, c.b, c.a);
        }

        if (light.getDistance() >= maxLight) {
            light.setDistance(maxLight);
        }

        if (pos.x <= -extremum) {
            sun.moveTo(new Vector2(extremum, pos.y));
        } else {

            sun.translateX(-1 * dt * sunRotationCoeff);

            if (c.g <= maxGreen) {
                if (pos.x < -colorTreshold) {
                    sunlight.setColor(c.r, c.g - dt / 4, c.b, c.a);
                    light.setDistance(light.getDistance() + dt * 4);
                }
                if (pos.x > colorTreshold && pos.x < colorTreshold * 15) {
                    sunlight.setColor(c.r, c.g + dt / 4, c.b, c.a);
                    light.setDistance(light.getDistance() - dt * 4);
                }
            } else {
                sunlight.setColor(c.r, maxGreen, c.b, c.a);
            }
        }


        float thrusterMax = 4;

        // thruster
        float x = (spaceship.getX() + spaceship.getWidth() / 2) / Box2DActor.PPM;
        float y = (spaceship.getY() + spaceship.getHeight() / 2) / Box2DActor.PPM;
        thrusterLight.setDirection(spaceship.getRotationAngle() + 180);
        thrusterLight.setPosition(x, y);

        if (thrusterOn && thrusterLight.getDistance() < thrusterMax) {
            thrusterLight.setDistance(thrusterLight.getDistance() + (30*dt));
        }
        if (thrusterOn && thrusterLight.getDistance() >= thrusterMax) {
            thrusterLight.setDistance(thrusterMax -1.5f);
        }
        if (!thrusterOn && thrusterLight.getDistance() > 0) {
            thrusterLight.setDistance(thrusterLight.getDistance() - (20*dt));
        }

    }

	/*------------------------------------------------------------------*\
	|*							Overriden Methods 						*|
	\*------------------------------------------------------------------*/

    @Override
    public void render(float dt) {
        super.render(dt);

        WORLD.step(1 / 60f, 6, 2);

        handlerRender();
    }

    @Override
    public boolean keyDown(int keycode) {

        // if (keycode == Input.Keys.X) spaceship.warp();

        if (keycode == Input.Keys.CONTROL_LEFT) {
            spaceship.shoot();
        }

        if (keycode == Input.Keys.UP) {
            thrusterOn = true;
        }

        if (keycode == Input.Keys.LEFT) {
            spaceship.getBody().setAngularDamping(0);
            spaceship.getBody().setAngularVelocity(4f);
        }

        if (keycode == Input.Keys.RIGHT) {
            spaceship.getBody().setAngularDamping(0);
            spaceship.getBody().setAngularVelocity(-4f);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.LEFT) {
            spaceship.getBody().setAngularVelocity(0);
        }

        if (keycode == Input.Keys.UP) {
            thrusterOn = false;
        }
        return false;
    }

    @Override
    public void dispose() {
        HANDLER.dispose();
    }

	/*------------------------------------------------------------------*\
	|*							Private methodes 						*|
	\*------------------------------------------------------------------*/

    private void lightsInitialization() {
        thrusterOn = false;

        light = new ConeLight(HANDLER, 1000, Color.WHITE, 0, 0, 0, 0, 60);
        light.setSoft(true);
        light.setSoftnessLength(.9f);

        sunlight = new ConeLight(HANDLER, 10000, Color.ORANGE, 90, 0, 0, 360, 180);
        sunlight.setSoft(true);
        sunlight.setSoftnessLength(60f);

        thrusterLight = new ConeLight(HANDLER, 265, Color.SCARLET, 0, 10, 10, 0, 25);
        thrusterLight.setSoft(true);
        thrusterLight.setSoftnessLength(1);

        light.attachToBody(spaceship.getBody());
        sunlight.attachToBody(sun.getBody());


    }

    private void handlerRender() {
        Camera camera = mainStage.getCamera();
        Matrix4 matrix = mainStage.getCamera().combined;
        matrix.scl(100);
        HANDLER.setCombinedMatrix(matrix, 0, 0, camera.viewportWidth, camera.viewportHeight);
        HANDLER.updateAndRender();
    }



    private void setEndGame(String textureName) {
        BaseActor messageLose = new BaseActor(0, 0, uiStage);
        messageLose.loadTexture(textureName);
        messageLose.centerAtPosition(1920 / 2, 1080 / 2);
        messageLose.setOpacity(0);
        messageLose.addAction(Actions.fadeIn(1));
        gameOver = true;
    }


}

