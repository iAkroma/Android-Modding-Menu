package il2cpp.typefaces;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ColorPicker {
	private Context context;
	
	private int COLOR1, COLOR2, COLOR3, COLOR4;
	
	private int rc, gc, bc;
	private onChange callback;
	
	private LinearLayout colorLayout;
	private SeekBar redSeek, greenSeek, blueSeek;

	private Typeface font;

	public static interface onChange {
		void onPut(int color);
	}

	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public void setC(int r, int g, int b) {
		GradientDrawable grad = new GradientDrawable();
		grad.setCornerRadius(20f);
		grad.setColor(Color.rgb(r,g,b));
		rc = r;
		bc = b;
		gc = g;
		colorLayout.setBackgroundDrawable(grad);
	}
	
	public OnSeekBarChangeListener getSlider() {
		OnSeekBarChangeListener ch = new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar slider2, int value, boolean a) {
				setC(redSeek.getProgress(), greenSeek.getProgress(), blueSeek.getProgress());
			}

			@Override
			public void onStopTrackingTouch(SeekBar slider) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar slider) {

			}
		};
		return ch;
	}
	
	public ColorPicker(Context _ctx, final onChange call) {
		context = _ctx;
		
		callback = call;

		this.COLOR1 = Color.parseColor("#F2F2F2");
		this.COLOR2 = Color.parseColor("#C7C7C7");
		this.COLOR3 = Color.parseColor("#F04C4D");
		this.COLOR4 = Color.parseColor("#8C9EE1");

		font = Typeface.createFromAsset(context.getAssets(), "Wish/Font.ttf");

		final AlertDialog.Builder dialog2 = new AlertDialog.Builder(context);
		
		LinearLayout line = new LinearLayout(context);
		line.setOrientation(LinearLayout.VERTICAL);
		
		// BUTT
		
		line.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		line.setBackgroundColor(COLOR1);
		
		dialog2.setView(line);

		GradientDrawable grad = new GradientDrawable();
		grad.setColor(COLOR2);
		grad.setCornerRadius(15f);

		colorLayout = new LinearLayout(context);
		setC(50,0,0);
		
		line.addView(colorLayout, -1, dpi(50));
		line.setPadding(10,10,10,10);
		
		redSeek = new SeekBar(context);
		redSeek.setMax(255);
		redSeek.setPadding(15,0,15,0);
		redSeek.setOnSeekBarChangeListener(getSlider());
		
		line.addView(redSeek, -1, dpi(30));
		
		greenSeek = new SeekBar(context);
		greenSeek.setMax(255);
		greenSeek.setPadding(15,0,15,0);
		greenSeek.setOnSeekBarChangeListener(getSlider());
		
		line.addView(greenSeek, -1, dpi(30));
		
		blueSeek = new SeekBar(context);
		blueSeek.setMax(255);
		blueSeek.setPadding(15,0,15,0);
		blueSeek.setOnSeekBarChangeListener(getSlider());
		
		line.addView(blueSeek, -1, dpi(30));
		
		final Dialog d = dialog2.show();
		CustomButton butt = new CustomButton(context, 0, "OK", 13f, new CustomButton.buttonClick() {
				public void onClick(int f) {
					d.dismiss();
					call.onPut(Color.rgb(rc,gc,bc));
				}
			});
		butt.getLine().setPadding(0, 0, 0, 0);
		line.addView(butt.getLine(), -1, -2);
	}

	public String getValue() {
		return "";
	}
}
