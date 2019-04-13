package com.spacerocks;

import com.framework.BaseGame;

public class SpaceGame extends BaseGame {

	public void create() {
	    super.create();
	    setActiveScreen(new LevelScreen());
    }
}
