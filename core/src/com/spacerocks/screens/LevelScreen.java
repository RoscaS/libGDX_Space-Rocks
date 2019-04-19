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
import com.effects.ExplosionEffect;
import com.spacerocks.actors.Rock;

import com.spacerocks.actors.Spaceship;
import com.spacerocks.actors.Sun;


public class LevelScreen extends BaseScreen {

    // Actors
    private Sun sun;
    private Spaceship spaceship;

    // Static
    private static final Float sunX = 3840f;
    private static final Float sunY = 2160f;
    private static final Float sunRotationCoeff = 1f;

    public static final World WORLD = new World(new Vector2(0, -9.8f), true);
    public static final RayHandler HANDLER = new RayHandler(WORLD);

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public void initialize() {

        HANDLER.setBlurNum(6);

        BaseActor space = new BaseActor(0, 0, mainStage);
        space.loadTexture("space.png");
        space.setSize(1920, 1080);
        Box2DActor.setWorldBounds(space);

        spaceship = new Spaceship(1920 / 2, 1080 / 2, mainStage, WORLD);
        sun = new Sun(sunX, sunY, mainStage, WORLD);
        sun.postConstruction();

        new Rock(600, 500, mainStage, WORLD);
        new Rock(600, 300, mainStage, WORLD);


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

        ConeLight light = spaceship.getFlashlight();
        ConeLight sunlight = sun.getSunlight();

        Vector2 pos = sun.getPosition();
        Color c = sunlight.getColor();

        float scaledSunX = sunX / Box2DActor.PPM;
        float colorTreshold = scaledSunX - scaledSunX * .9f;
        float maxGreen = .64f;
        float maxLight = 20;


        if (c.g >= maxGreen) {
            sunlight.setColor(c.r, maxGreen, c.b, c.a);
        }

        if (light.getDistance() >= maxLight) {
            light.setDistance(maxLight);
        }

        if (pos.x <= -2 * scaledSunX) {
            sun.moveTo(new Vector2( 2 * scaledSunX, pos.y));
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
        if (keycode == Input.Keys.CONTROL_LEFT) {
            spaceship.shoot();
        }
        if (keycode == Input.Keys.UP) {
            spaceship.setMainThrusterOn();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.LEFT) {
            spaceship.getBody().setAngularDamping(10f);
        }

        if (keycode == Input.Keys.UP) {
            spaceship.setMainThrusterOff();
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

    private void handlerRender() {
        Camera camera = mainStage.getCamera();
        Matrix4 matrix = mainStage.getCamera().combined;
        matrix.scl(100);
        HANDLER.setCombinedMatrix(matrix, 0, 0, camera.viewportWidth, camera.viewportHeight);
        HANDLER.updateAndRender();
    }
}

