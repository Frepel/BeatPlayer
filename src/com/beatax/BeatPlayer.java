package com.beatax;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.ListActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.beatax.BeatFilter;



public class BeatPlayer extends ListActivity {

	// Music path and objects
	public static final String MEDIA_PATH = new String("/sdcard/");
	public List<String> songs = new ArrayList<String>();
	public MediaPlayer mp = new MediaPlayer();
	ArrayAdapter<String> songList;
	public int countBeat;
	public int newBeat = 0;
	public int beatCurrent ;
	public boolean playStarted = false;
	String pos, total;
	Button getBeatsButton, playPauseButton, nextButton, backButton;

	@Override
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        
        getBeatsButton = (Button)findViewById(R.id.buttonAdd);
        //Listener for get Get Beats
        getBeatsButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
            	//beatObject.updateSongList();
            	updateBeatList();
            }
        });
    
        playPauseButton = (Button)findViewById(R.id.buttonPausePlay);
        // Listener for Play and Pause 
        playPauseButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
            	
            	if(playStarted == true){
            		if(mp.isPlaying()) {
            			playPauseButton.setText("Play");
            		
            		} else 
            			playPauseButton.setText("Pause");
            	}
            	onResumePlay();
            }
        });
        
        nextButton = (Button)findViewById(R.id.buttonNext);
        // Listener for Next
        nextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
            	onNext();
              
            }
        });
        
        backButton = (Button)findViewById(R.id.buttonPrevious);
        // Listener for Back 
        backButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
            	onBack();
            }
        });      
               
    }

	// Call back the application on stack 
	@Override
    protected void onResume()
    {
        super.onResume();
    }	
	
	// Construct Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, Menu.FIRST,   Menu.NONE, "Quit").setIcon(R.drawable.quit);
    	return true;
    }
    
    // Use Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	// End song when quitting application
    	if(mp.isPlaying()) {
    		mp.stop();
    	}    	
    	this.finish();
    	return true;
    }
    
 
	// Get audio files
    public void updateBeatList() {
    	File home = new File(MEDIA_PATH);
		if (home.listFiles( new BeatFilter()).length > 0) {
    		for (File file : home.listFiles( new BeatFilter())) {
    			songs.add(file.getName());
    			
    			//Skip beats that are present on the list
    			filterBeatsOnUpdate(songs);
		
    		songList = new ArrayAdapter<String>(this,R.layout.beat_item,songs);
    		setListAdapter(songList);
    		}
		}
    }

    
    // Handle duplicate songs on the list
    public  void filterBeatsOnUpdate(List<String> list)  
    {  
    	//establish unique entries
    	 Set<Object> uniqueEntries = new HashSet<Object>();
    	 //remove duplicates
    	 for (Iterator<String> entry = list.iterator(); entry.hasNext(); ) {  
    		Object element = entry.next();  
    		if (!uniqueEntries.add(element))   
            entry.remove();                   
         }  
       }  

    // Handle play song on click
    @Override
    protected void onListItemClick(ListView l, View v , int position, long id) {
		try {
			mp.reset();
			mp.setDataSource(MEDIA_PATH + songs.get(position));
			mp.prepare();
			mp.start();
			playPauseButton.setText("Pause");
			playStarted = true;
		} catch(IOException e) {
			Log.v(getString(R.string.app_name), e.getMessage());
		}
		
		beatCurrent = position;
		countBeat = beatCurrent;
		pos = Integer.toString(countBeat+1);
		total = Integer.toString(songs.size());
		Toast.makeText(this, pos+"/"+total, Toast.LENGTH_SHORT).show();
	}
    
    // Handle next song    
    public void onNext() {

    	if(!songs.isEmpty() && playStarted == true) {
    	
    		if (beatCurrent == (songs.size()-1)) {
    			
        		try {
        			mp.reset();
        			mp.setDataSource(MEDIA_PATH + songs.get(newBeat));
        			mp.prepare();
        			mp.start();
        			playPauseButton.setText("Pause");
        		} catch(IOException e) {
        			Log.v(getString(R.string.app_name), e.getMessage());
        		}  		
        		
        		Toast.makeText(this, "Last song of the list", Toast.LENGTH_SHORT).show();
    		}
    		
    		if (beatCurrent < (songs.size()-1)) {
    			
    			newBeat = beatCurrent + 1;
    			beatCurrent = newBeat;
    			
        		try {
        			mp.reset();
        			mp.setDataSource(MEDIA_PATH + songs.get(newBeat));
        			mp.prepare();
        			mp.start();
        			playPauseButton.setText("Pause");
        		} catch(IOException e) {
        			Log.v(getString(R.string.app_name), e.getMessage());
        		}
        		
    		}
    		
    	} else

    	Toast.makeText(this, "Get some beats first - Tap a song to play", Toast.LENGTH_SHORT).show();
    }
    
    // Handle previous song
    public void onBack() {
    	
    	if(!songs.isEmpty() && playStarted == true) {	  

    		if (beatCurrent == 0 ){
        		try {
        			mp.reset();
        			mp.setDataSource(MEDIA_PATH + songs.get(0));
        			mp.prepare();
        			mp.start();
        			playPauseButton.setText("Pause");
        		} catch(IOException e) {
        			Log.v(getString(R.string.app_name), e.getMessage());
        		}  		
    			
        		Toast.makeText(this, "First song of the list", Toast.LENGTH_SHORT).show();
    		}
    		
    		if (beatCurrent > 0) {
    			
    			newBeat = beatCurrent - 1;
    			beatCurrent = newBeat;
    			
        		try {
        			mp.reset();
        			mp.setDataSource(MEDIA_PATH + songs.get(newBeat));
        			mp.prepare();
        			mp.start();
        			playPauseButton.setText("Pause");
        		} catch(IOException e) {
        			Log.v(getString(R.string.app_name), e.getMessage());
        		}
    
    		}
    		
    	} else
    	
    	Toast.makeText(this, "Get some beats first - Tap a song to play", Toast.LENGTH_SHORT).show();

    }
   
    
    // Handle on Resume Play
    public void onResumePlay() {
    	
        if(playStarted == true){
        	
        	if(mp.isPlaying()) {
        		mp.pause();
        		
        	} else 
        		mp.start();
        	
        } else {
        		
        		Toast.makeText(this, "Get some beats first - Tap a song to play", Toast.LENGTH_SHORT).show();
        	}
    	}
    
    // Use back hard key to stop audio and kill application
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mp.stop();
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
