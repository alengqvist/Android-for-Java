package android.assignment1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ColorDisplay extends Activity {

	private View display;
	private EditText iRed;
	private EditText iGreen;
	private EditText iBlue;
	private Button bSetColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_display);
		
		initFields();
		bSetColor.setOnClickListener(new SetColor());
		
	}
	
	// Initializing the GUI-components.
	private void initFields() {
		display = (View) findViewById(R.id.display);
		iRed = (EditText) findViewById(R.id.input_red);
		iGreen = (EditText) findViewById(R.id.input_green);
		iBlue = (EditText) findViewById(R.id.input_blue);
		bSetColor = (Button) findViewById(R.id.button_setColor);
	}
	
 
	private class SetColor implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			if(isValidColor(iRed) && isValidColor(iGreen) && isValidColor(iBlue)) {
				
				Integer red = Integer.parseInt(iRed.getText().toString().trim());
				Integer green = Integer.parseInt(iGreen.getText().toString().trim());
				Integer blue = Integer.parseInt(iBlue.getText().toString().trim());
				
				display.setBackgroundColor(Color.parseColor(String.format("#%02x%02x%02x", red, green, blue)));				
			}
		}
	}

	// Takes a EditText and validates the input.
	private boolean isValidColor(EditText input) {
		
		String text = input.getText().toString().trim();
		if(TextUtils.isEmpty(text)){
			setInputError(input, getString(R.string.error_empty));
			return false;
		}
		
		Integer value = Integer.parseInt(text);
		if(value > 255 || value < 0){
			setInputError(input, getString(R.string.error_range_color));
			return false;
		}
		return true;
	}

	// I'm aware of the setError()'s icon and it's positions. Its a bit buggy. But for this assignment I will let it be that way.
	private void setInputError(EditText input, String e) {
		input.setError(e);
		input.setText(null);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
