package android.assignment1;

import android.app.Activity;
import android.assignment1.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Random extends Activity {

	private Button randomButton;
	private TextView display;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
		
        initFields();
		randomButton.setOnClickListener(new RandomClick());
    }
    
    
	// Initializing the GUI-components.
	private void initFields() {
		display = (TextView) findViewById(R.id.display);
		randomButton = (Button)findViewById(R.id.random_button);
	}
    
    
    private class RandomClick implements View.OnClickListener {
    	public void onClick(View v) {
    		display.setText(Integer.toString((int)(Math.random() * 100 + 1)));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
