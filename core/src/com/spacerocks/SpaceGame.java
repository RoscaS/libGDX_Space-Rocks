package com.spacerocks;

import com.framework.BaseGame;
import com.spacerocks.screens.LevelScreen;

public class SpaceGame extends BaseGame {

	public void create() {
	    super.create();
	    setActiveScreen(new LevelScreen());
    }
}
