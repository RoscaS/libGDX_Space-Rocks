package com.spacerocks.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.spacerocks.SpaceGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Game myGame = new SpaceGame();
		LwjglApplication laucher = new LwjglApplication(myGame, "Space Rocks", 800, 600);
	}
}
