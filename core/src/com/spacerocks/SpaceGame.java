package com.spacerocks;

public class SpaceGame extends BaseGame {

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public void create() {
	    super.create();
	    setActiveScreen(new LevelScreen());
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

}
