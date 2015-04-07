package android.assignment_2;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

public class MusicManager {

	private List<Song> playList = new ArrayList<Song>();
	private ContentResolver contentResolver;

	
	public MusicManager(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	public List<Song> getPlayList() {
		return playList;
	}

	public void retrieve() {

		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return;
		}

		Cursor music = contentResolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] {
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA },
						MediaStore.Audio.Media.IS_MUSIC + " > 0 ", null, null);
		
		if (music == null || !music.moveToFirst()) {
			return;
		}

		do {
			Song song = new Song();
			song.setArtist(music.getString(0));
			song.setAlbum(music.getString(1));
			song.setTitle(music.getString(2));
			song.setPath(music.getString(3));
			playList.add(song);
		} while (music.moveToNext());
	}
}