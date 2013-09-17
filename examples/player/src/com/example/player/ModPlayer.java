/*
 * Example mod player using libxmp.jar.
 *  
 * This code is in the public domain.
 */

package com.example.player;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import org.helllabs.java.libxmp.*;


public class ModPlayer {
	private int oldPos = -1;

	private void showInfo(FrameInfo fi) {
		if (fi.pos != oldPos) {
			System.out.printf("Pos: %d, Pattern: %d\r", fi.pos, fi.pattern);
			oldPos = fi.pos;
		}
	}

	private void showHeader(Module mod) {
		System.out.println("Module name  : " + mod.name);
		System.out.println("Module type  : " + mod.type);
		System.out.println("Module length: " + mod.len + " patterns");
	}

	private void run(String[] args) throws LineUnavailableException {

		final Player player = new Player(44100);
		final AudioPlay audio = new AudioPlay(44100);

		for (String arg : args) {
			try {
				if (!Module.test(arg))
					continue;

				System.out.println("\nPlaying " + arg + "...");
				
				Module mod = new Module(player, arg);
				showHeader(mod);
				
				mod.setOnFrameListener(new Module.OnFrameListener() {
					@Override
					public void onFrame(FrameInfo fi) {
						audio.play(fi.buffer, fi.bufferSize);
						showInfo(fi);
					}
				});

				mod.play();
			} catch (IOException e) {
				System.out.println("Can't play " + arg);
			}
			System.out.print("\n");
		}
	}

	public static void main(String[] args) {
		System.out.println("Libxmp player test");
		ModPlayer modPlayer = new ModPlayer();
		try {
			modPlayer.run(args);
		} catch (LineUnavailableException e) {
			System.out.println("Can't initialize audio");
		}
		System.out.println("End");
	}

}
