package android.assignment_2;

import java.util.List;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;



public class MusicService extends Service {

	public class MusicBinder extends Binder{
		MusicService getService() {
			return MusicService.this;
		}
	}

	private int NOTIFICATION_ID = 1;
	private Player player;
	private final IBinder binder = new MusicBinder();

	enum State { Stopped, Playing, Paused };
	private State currentState = State.Stopped;
	public State getCurrentState() { return currentState; }


	private void startNotification() {

		/* 1. Setup Notification Builder */
		Notification.Builder builder = new Notification.Builder(this);

		/* 2. Configure Notification */
		builder.setSmallIcon(R.drawable.ic_music)
			   .setTicker("Music")
			   .setAutoCancel(false);

		/* 3. Configure Drop-down Action */
		builder.setContentTitle(getResources().getString(R.string.title_music_notify_title))
			   .setContentText(player.getCurrentSong().getArtist() +" - "+ player.getCurrentSong().getTitle());

		/* 4. Create Pending Intent in which if the user click on the notification starts the MP3Player Activity. */
		Intent intent = new Intent(this, MP3Player.class);
		PendingIntent notifIntent = PendingIntent.getActivity(this, 0, intent, 0);
		builder.setContentIntent(notifIntent);

		/* 5. Create Notification and use Manager to launch it */
		Notification notification = builder.build();
		startForeground(NOTIFICATION_ID, notification);
	}

	private void stopNotification() {
		stopForeground(true);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	@Override
	public void onDestroy() {

		stopForeground(true);

		if (player != null) {
			player.destroyMediaPlayer();
			player = null;
		}
	}

	public Song getCurrentPlayerSong() {
		if (player != null) {
			return player.getCurrentSong();
		}
		return null;
	}

	public void startPlaying(List<Song> playList, int pos) {

		if (player == null) {
			player = new Player(playList, pos);
			player.start();
		} 
		else {
			player.destroyMediaPlayer();
			player = new Player(playList, pos);
			player.start();
		}
	}

	public void playNextSong() {
		if (player != null) {
			player.playNext();
		}
	}

	public void playPreviousSong() {
		if (player != null) {
			player.playPrevious();
		}
	}
	
	public void pauseOrResume() {
		if (player != null) {
				player.pauseOrResume();
		}
	}

	private class Player extends Thread implements OnCompletionListener {

		private MediaPlayer mediaPlayer;
		private Song currentSong;
		private List<Song> playList;
		private int pos;

		
		public Song getCurrentSong() {
			return currentSong;
		}

		public Player(List<Song> playlist, int pos) {
			this.playList = playlist;
			this.pos = pos;
			currentSong = playList.get(pos);
			mediaPlayer = new MediaPlayer();
		}

		public void pauseOrResume() throws IllegalStateException {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				currentState = MusicService.State.Paused;
				stopNotification();
			} else {
				mediaPlayer.start();
				currentState = MusicService.State.Playing;
				startNotification();
			}
		}
		
		private void play(Song song) {
			if (song == null) {
				return;
			}
			try {
				if (mediaPlayer.isPlaying()){
					mediaPlayer.stop();
				}
				mediaPlayer.reset();
				mediaPlayer.setDataSource(MusicService.this, Uri.parse(song.getPath()));
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				mediaPlayer.prepare();
				mediaPlayer.setOnCompletionListener(this);
				mediaPlayer.start();
				currentState = MusicService.State.Playing;
				currentSong = song;
				startNotification();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void playNext() {
			if (pos < playList.size() - 1) {
				pos += 1;
				play(playList.get(pos));
			}
		}

		public void playPrevious() {
			if (pos >= 1) {
				pos -= 1;
				play(playList.get(pos));
			}
		}
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			playNext();
		}

		@Override
		public void run() {
			if (!playList.isEmpty())
				play(currentSong);
			else {
				System.out.println("No music to play.");
			}
		}

		public void destroyMediaPlayer() {
			if (mediaPlayer == null) {
				return;
			}
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
			currentState = MusicService.State.Stopped;
			stopNotification();
		}
	}
}