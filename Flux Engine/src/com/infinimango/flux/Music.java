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

public class Music {
	private Clip clip;
	private AudioInputStream stream;
	private File file;

	Thread thread;

	/**
	 * Loads a new music file from the jar.
	 * @param path Path to music file
	 * @return New Music
	 */
	public static Music load(String path){
		return new Music(path);
	}

	/**
	 * Creates a new music file from path.
	 * @param path Path to music file
	 */
	private Music(String path) {
		try {
			file = new File(path);
		} catch (Throwable e) {
			Debug.error("Loading sound from \"" + path + "\" failed\n");
			e.printStackTrace();
		}
	}

	/**
	 * Loops the music in a thread.
	 */
	public synchronized void start() {
		if(clip != null && clip.isActive()) return;
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					clip = AudioSystem.getClip();
					stream = AudioSystem.getAudioInputStream(file);
					clip.open(stream);
					clip.start();
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				} catch (Exception e) {
					Debug.error("Starting music playback failed");
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public boolean isPlaying(){
		return clip.isActive();
	}

	/**
	 * Loops the music in a thread.
	 */
	public synchronized void stop() {
		try {
			clip.close();
			thread.join();
		} catch (InterruptedException e) {
			Debug.error("Terminating music thread failed");
			e.printStackTrace();
		}
	}
}
