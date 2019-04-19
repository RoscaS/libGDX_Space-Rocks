package com.framework;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * Simple example of an actor that adds itself a bottom sensor.
 * NOT MENT TO BE USED, IT'S AN EXAMPLE !
 */
public class Box2DSensor extends Box2DActor {

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Box2DSensor(float x, float y, Stage s, World w) {
        super(x, y, s, w);
    }

    @Override
    protected void postConstruction() {
        // first, perform initialization tasks from Box2DActor
        super.postConstruction();

        // create additional player-specific texture
        FixtureDef bottomSensor = new FixtureDef();
        bottomSensor.isSensor = true;
        PolygonShape sensorShape = new PolygonShape();

        // center coordinates of sensor box (offset from body center)
        float x = 0;
        float y = -120;

        // dimensions of sensor box
        float w = getWidth() - 8;
        float h = getHeight();
        sensorShape.setAsBox(w / 200, h / 200, new Vector2(x / 200, y / 200), 0);
        bottomSensor.shape = sensorShape;

        // create and attach this new fixture
        Fixture bottomFixture = body.createFixture(bottomSensor);
        bottomFixture.setUserData("bottom");
    }
}
