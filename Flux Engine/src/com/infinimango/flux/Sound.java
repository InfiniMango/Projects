package com.infinimango.flux;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Sound {
	private AudioInputStream stream;
	private File file;

	/**
	 * Loads a new sound from the jar.
	 * @param path Path to sound file
	 * @return New Sound
	 */
	public static Sound load(String path){
		return new Sound(path);
	}

	/**
	 * Creates a new sound from path.
	 * @param path Path to sound file
	 */
	private Sound(String path) {
		try {
			file = new File(path);
		} catch (Throwable e) {
			Debug.error("Loading sound from \"" + path + "\" failed");
			e.printStackTrace();
		}
	}

	/**
	 * Plays the sound in a new thread.
	 */
	public synchronized void play() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					stream = AudioSystem.getAudioInputStream(file);
					clip.open(stream);
					clip.start();
				} catch (Exception e) {
					Debug.error("Playing sound failed");
					e.printStackTrace();
				}
			}
		}).start();
	}

}
