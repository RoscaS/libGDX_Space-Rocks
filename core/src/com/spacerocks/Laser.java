package com.spacerocks;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.framework.BaseActor;

public class Laser extends BaseActor {

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Laser(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("laser.png");

        addAction(Actions.delay(.25f));
        addAction(Actions.after(Actions.fadeOut(.5f)));
        addAction(Actions.after(Actions.removeActor()));

        setSpeed(2000);
        setMaxSpeed(2000);
        setDeceleration(0);
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
        wrapAroundWorld();
    }

	/*------------------------------------------------------------------*\
	|*							Private Methods 						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Private Attributs 						*|
	\*------------------------------------------------------------------*/
}
