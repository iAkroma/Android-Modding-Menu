package il2cpp.typefaces;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.SeekBar;
import android.graphics.PorterDuff;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class CustomSlider {
	private Context context;
	private String buttonText;
	
	private int COLOR1, COLOR2, COLOR3, COLOR4;

	private LinearLayout line, textLine;
	private SeekBar slider;
	private TextView text, textValue;
	
	private int min, featureid;
	private int max;
	public int progress;
	
	private Typeface font;
	
	private sliderChange callback;

	public static interface sliderChange {
		void onChange(int featureid, int value);
	}
	
	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public void setProgress(int i) {
		callback.onChange(featureid, i);
		progress = i;
		slider.setProgress(i);
		textValue.setText(Integer.toString(i));
	}
	
	public CustomSlider(Context _ctx, int featid, String _text, float size, final int mn, int mx, int pr, final sliderChange call) {
		context = _ctx;
		buttonText = _text;
		
		featureid = featid;
		
		callback = call;
		
		min = mn;
		max = mx;
		progress = pr;

		this.COLOR1 = Color.parseColor("#F2F2F2");
		this.COLOR2 = Color.parseColor("#C7C7C7");
		this.COLOR3 = Color.parseColor("#FF464CE2");
		this.COLOR4 = Color.parseColor("#FF464CE2");

		font = Typeface.createFromAsset(context.getAssets(), "Wish/Font.ttf");

		line = new LinearLayout(context);
		line.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(30)));
		line.setOrientation(LinearLayout.VERTICAL);
		line.setGravity(Gravity.CENTER_VERTICAL);
		
		slider = new SeekBar(context);
		
		slider.setMax((int) mx);
		slider.setProgress(pr);
		
		/*
		GradientDrawable thumb = new GradientDrawable();
		thumb.setColor(COLOR3);
		thumb.setSize(40, 40);
		thumb.setCornerRadius(100);
		thumb.setPadding(20, 0, 20, 0);
		
		thumb.setTintMode(PorterDuff.Mode.MULTIPLY);
		
		slider.setThumb(thumb);
		*/
		GradientDrawable btn = new GradientDrawable();
		btn.setColor(Color.TRANSPARENT);
		
		slider.setProgressDrawable(btn);
		
		slider.getThumb().setColorFilter(Color.parseColor((String) "#FF464CE2"), PorterDuff.Mode.SRC_IN);
		
		slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar slider2, int value, boolean a) {
				if (value < mn) {
					value = mn;
				}
				setProgress(value);
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar slider) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar slider) {
				
			}
		});
		
		text = new TextView(context);
		text.setText(buttonText);
		text.setTextSize(size);
		text.setTypeface(font);
		text.setGravity(Gravity.CENTER_VERTICAL);
		text.setTextColor(Color.WHITE);
		
		textValue = new TextView(context);
		textValue.setText(Integer.toString(pr));
		textValue.setTextSize(size);
		textValue.setTypeface(font);
		textValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		textValue.setTextColor(Color.WHITE);
		
		textLine = new LinearLayout(context);
		textLine.setOrientation(LinearLayout.HORIZONTAL);
		textLine.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1, 1);
		text.setLayoutParams(lp);
		textValue.setLayoutParams(lp);
		
		textLine.addView(text);
		textLine.addView(textValue);
		
		line.setPadding(10, 0, 10, 0);
		
		LinearLayout sliderLine = new LinearLayout(context);
		sliderLine.setPadding(10, 0, 10, 0);
		slider.setPadding(20, 0, 20, 0);
		
		GradientDrawable grd = new GradientDrawable();
		grd.setColor(Color.parseColor("#17191D"));
		grd.setCornerRadius(200);
		sliderLine.setBackgroundDrawable(grd);
		
		sliderLine.addView(slider, -1, -1);
		
		line.addView(textLine);
		line.addView(sliderLine, new LinearLayout.LayoutParams(-1, -1, 1));
		
	}

	public LinearLayout getLine() {
		return line;
	}

}
