package android.assignment_2;

import java.util.List;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;



public class MP3Player extends Activity {
	
	private ImageButton playButton;
	private ImageButton nextButton;
	private ImageButton previousButton;
	private ListView listView;
	private List<Song> playList;

	private MusicService musicService = null;
	private MusicManager musicManager;
	private MusicAdapter adapter;
	
	private boolean playing = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_player);

		// Start the Music Service.
		Intent intent = new Intent(this, MusicService.class);
		this.startService(intent);
		this.bindService(intent, connection, Context.BIND_AUTO_CREATE);

		// Retriever which fetches all the music.
		RetrieverTask retriever = new RetrieverTask();
		retriever.execute(0);

		// Initializing GUI-components.
		playButton = (ImageButton) findViewById(R.id.music_play_button);
		nextButton = (ImageButton) findViewById(R.id.music_next_button);
		previousButton = (ImageButton) findViewById(R.id.music_previous_button);
		
		// Set GUI-components.
		playButton.setOnClickListener(new PlayClick());
		nextButton.setOnClickListener(new NextClick());
		previousButton.setOnClickListener(new PreviousClick());

		// If there is a savedInstanceState.
		if (savedInstanceState != null)
			playing = savedInstanceState.getBoolean("playing");
		changePlayButton();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("playing", playing);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		unbindService(connection);
		super.onDestroy();
	}

	private class PlayClick implements OnClickListener {

		@Override
		public void onClick(View arg0) {

			// If Play-state is stopped (only if there hasnt been any tracks played yet.
			if (musicService.getCurrentState() == MusicService.State.Stopped) {
				playing = true;
				musicService.startPlaying(playList, 0);
			}
			
			// If a track has been paused or is playing.
			else {

				musicService.pauseOrResume();

				if (playing || musicService.getCurrentState() == MusicService.State.Playing){
					playing = false;
				}
				else {
					playing = true;
				}
			}
			updatePlayPauseButtonImage();
			adapter.notifyDataSetChanged();
		}
	}
	
	// When the Play button change state Play/Pause.
	private void changePlayButton() {
		if (musicService != null) {
			if (musicService.getCurrentState() == MusicService.State.Stopped) {
				playing = false;
			} else if (musicService.getCurrentState() == MusicService.State.Playing) {
				playing = true;
			} else {
				playing = false;
			}
		}
		updatePlayPauseButtonImage();
	}

	// Update image on the Play-button.
	private void updatePlayPauseButtonImage() {
		if (playing) {
			playButton.setImageResource(R.drawable.ic_action_pause);
		} else {
			playButton.setImageResource(R.drawable.ic_action_play);
		}
	}

	// Next track.
	private class NextClick implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			musicService.playNextSong();
			playing = true;
			changePlayButton();
			adapter.notifyDataSetChanged();
		}
	}

	// Previous track.
	private class PreviousClick implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			musicService.playPreviousSong();
			playing = true;
			changePlayButton();
			adapter.notifyDataSetChanged();
		}
	}
	
	// When click on a track in the list.
	private class OnTrackClick implements OnItemClickListener {

		private List<Song> playList;

		public OnTrackClick(List<Song> playList) {
			this.playList = playList;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
			musicService.startPlaying(playList, pos);
			playing = true;
			changePlayButton();
			adapter.notifyDataSetChanged();
		}

	}
	
	// RETRIEVER - fetches all song from SD-card through the MusicManager and adds it to the adapter.
	private class RetrieverTask extends AsyncTask<Integer, Integer, MusicManager> {
		
		@Override
		protected MusicManager doInBackground(Integer... steps) {
			musicManager = new MusicManager(MP3Player.this.getContentResolver());
			musicManager.retrieve();
			return musicManager;
		}

		@Override
		protected void onPostExecute(MusicManager result) {
			playList = result.getPlayList();
			adapter = new MusicAdapter(MP3Player.this);
			adapter.addAll(playList);
			listView = (ListView) findViewById(R.id.music_playlist);
			listView.setOnItemClickListener(new OnTrackClick(playList));
			listView.setAdapter(adapter);
		}
	}
	
	
	// ADAPTER.
	private class MusicAdapter extends ArrayAdapter<Song> {
		
		
		public MusicAdapter(Context context) {
			super(context, 0);
		}
		
	    private class ViewHolder {
	        TextView label;
	    }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
	    	 // Using a ViewHolder to speed things up. Basically just saves all the objects to solve the DRY-issue.
	    	 ViewHolder viewHolder;
	    	 if (convertView == null) {
	             
	    		 viewHolder = new ViewHolder();
	             
	             LayoutInflater inflater = LayoutInflater.from(getContext());
	             convertView = inflater.inflate(R.layout.activity_mp3_player_row, parent, false);
	             viewHolder.label = (TextView) convertView.findViewById(R.id.music_label);
	             convertView.setTag(viewHolder);
	          } else {
	              viewHolder = (ViewHolder) convertView.getTag();
	          }
	    	 
	 		Song song = getItem(position);
	 		
	 		viewHolder.label.setText(song.getArtist() + " - " + song.getTitle());
	         
	 		// If the listitem is the current song. Change text color.
			if (musicService != null) {
				Song currSong = musicService.getCurrentPlayerSong();
				if (currSong != null) {

					if (song.equals(currSong)) {
						viewHolder.label.setTextColor(getResources().getColor(R.color.green_text));
					}
					else {
						viewHolder.label.setTextColor(getResources().getColor(R.color.egg_white_text));
					}
				}
			}
			return convertView;
		}
	}
	
	// Service connection to establish connection for the Music Service with Binder.
	private ServiceConnection connection = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName cName, IBinder binder) {
			musicService = ((MusicService.MusicBinder) binder).getService();
		}

		public void onServiceDisconnected(ComponentName cName) {
			musicService = null;
		}
	};
}