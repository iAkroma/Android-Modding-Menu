package il2cpp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.widget.Toast;

public class ActivityMain extends Activity  
{
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		// Toast.makeText(getApplicationContext(), executeCommand("mkdir /sdcard/tse"), Toast.LENGTH_LONG).show();
		
		Main.start(this);

		LinearLayout backg = new LinearLayout(getApplicationContext());
		backg.setBackgroundColor(Color.GREEN);
		setContentView(backg);
    }
}
