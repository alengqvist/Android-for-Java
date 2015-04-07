package android.assignment_2;

public class Song {
	
	private String artist;
	private String album;
	private String title;
	private String path;
	private long duration;
	
	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getAlbum() {
		return album;
	}

	public long getDuration() {
		return duration;
	}

	public String getPath() {
		return path;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Song [artist=" + artist + ", album=" + album + ", title=" + title + ", path="
				+ path + ", duration=" + duration + "]";
	}


	/**
	 * Auto-generated to compare each variable.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Song other = (Song) obj;
		if (album == null) {
			
			if (other.album != null)
				return false;
		} 
		else if (!album.equals(other.album))
			return false;
		
		if (artist == null) {
			
			if (other.artist != null)
				return false;
		} 
		else if (!artist.equals(other.artist))
			return false;
		
		if (duration != other.duration)
			return false;
		
		if (path == null) {
			
			if (other.path != null)
				return false;
		} 
		else if (!path.equals(other.path))
			return false;
		
		if (title == null) {
			
			if (other.title != null)
				return false;
		} 
		else if (!title.equals(other.title))
			return false;
		
		return true;
	}

}