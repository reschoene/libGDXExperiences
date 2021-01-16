package br.com.reschoene.mariobros.desktop;

import br.com.reschoene.mariobros.MarioBros;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setWindowedMode(750, 400);
		new Lwjgl3Application(new MarioBros(), cfg);
	}
}
