package edu.tamyky.light_simulation;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopDiffractionLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1000, 1000);
		config.setTitle("LightSimulation");
		new Lwjgl3Application(new DiffractionEngine(), config);
	}
}
