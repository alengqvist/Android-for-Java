package android.assignment1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BMI extends Activity {
	
	private AlertDialog alert;
	private TextView display;
	private EditText lengthInput;
	private EditText weightInput;
	private Button reset;
	private Button compute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bmi);
		
		initFields();
		compute.setOnClickListener(new ButtonClick());  
		reset.setOnClickListener(new ButtonClick());  
		
		alert = setup_alert();
	}
	
	// Initializing the GUI-components.
	private void initFields() {
		display = (TextView) findViewById(R.id.display);
		lengthInput = (EditText) findViewById(R.id.input_length);
		weightInput = (EditText) findViewById(R.id.input_weight);
		compute = (Button)findViewById(R.id.button_compute);
		reset = (Button)findViewById(R.id.button_reset);
	}

	private class ButtonClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			if(v == compute){
				
				String weightText = weightInput.getText().toString().trim();
				String lengthText = lengthInput.getText().toString().trim();
				
				if(TextUtils.isEmpty(weightText) || TextUtils.isEmpty(weightText)){
					alert.show();
				}
				else{		
					display.setText(getString(R.string.your_bmi) + " " + Float.toString(calculateBMI(Float.parseFloat(weightText), Float.parseFloat(lengthText)/100)));
				}
			}
			
			else if(v == reset){
				
				lengthInput.setText(null);
				weightInput.setText(null);
				display.setText(R.string.display_bmi);
			}
		}
	}
	
	
	private AlertDialog setup_alert() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_empty_setMessage);
        builder.setCancelable(false);
        builder.setTitle(R.string.alert_empty_setTitle);
        builder.setPositiveButton(R.string.alert_done, new DialogDone());
        return builder.create();
	}
	
	
    private class DialogDone implements DialogInterface.OnClickListener {
    	public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
        }
    }

    // Calculates the BMI.
	private float calculateBMI(float weight, float length) {
	     return (float) (weight / (length * length));
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
