package com.spacerocks;

public class LevelScreen extends BaseScreen {

    private Spaceship spaceship;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public void initialize() {
	    BaseActor space = new BaseActor(0, 0, mainStage);
	    space.loadTexture("space.png");
	    space.setSize(800, 600);
	    BaseActor.setWorldBounds(space);
	    spaceship = new Spaceship(400, 300, mainStage);
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

	public void update(float dt) { }

	/*------------------------------------------------------------------*\
	|*							Private Methods 						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Private Attributs 						*|
	\*------------------------------------------------------------------*/
}
