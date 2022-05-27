package il2cpp.typefaces;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;

public class SwitchButton {
	private Context context;
	private String buttonText;
	public boolean isChecked = false;
	
	private int COLOR1, COLOR2, COLOR3, COLOR4;
	private int featureid;
	
	private LinearLayout line;
	private ImageView button;
	private TextView text;
	
	private switchChange callback;
	
	private Typeface font;
	
	public static interface switchChange {
		void onClick(int featureid, int ischeck, boolean checks);
	}
	
	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public void setChecked(boolean isCheck) {
		isChecked = isCheck;
		callback.onClick(featureid, isChecked ? 1 : 0, isCheck);
		if (isCheck) {
			setAss(button, "check.png");
		} else {
			setAss(button, "uncheck.png");
		}
	}
	
	public final void setAss(ImageView image, String src) {
		try {
			InputStream ims = context.getAssets().open("Wish/" + src);
			Drawable d = Drawable.createFromStream(ims, null);
			image.setImageDrawable(d);
		}
		catch(IOException ex) {
			// mind coder

		}
	}
	
	
	public SwitchButton(Context _ctx, int feature, String _text, float size, switchChange call) {
		context = _ctx;
		buttonText = _text;
		callback = call;
		featureid = feature;
		
		this.COLOR1 = Color.parseColor("#F2F2F2");
		this.COLOR2 = Color.parseColor("#C7C7C7");
		this.COLOR3 = Color.parseColor("#F04C4D");
		this.COLOR4 = Color.parseColor("#FF8281");
		
		font = Typeface.createFromAsset(context.getAssets(), "Wish/Font.ttf");
		
		line = new LinearLayout(context);
		line.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(20)));
		line.setOrientation(LinearLayout.HORIZONTAL);
		line.setGravity(Gravity.CENTER_VERTICAL);
		
		button = new ImageView(context);
		setAss(button, "uncheck.png");
		
		text = new TextView(context);
		text.setText(buttonText);
		text.setTextSize(size);
		text.setTypeface(font);
		text.setGravity(Gravity.CENTER_VERTICAL);
		text.setTextColor(Color.WHITE);
		
		OnClickListener clck = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setChecked(!isChecked);
			}
		};
		
		button.setOnClickListener(clck);
		text.setOnClickListener(clck);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -1, 1);
		lp.leftMargin = 10;
		text.setLayoutParams(lp);
		button.setLayoutParams(new LinearLayout.LayoutParams(dpi(15), dpi(15)));
		
		line.setPadding(10, 0, 10, 0);
		line.addView(text);
		line.addView(button);
	}
	
	public LinearLayout getLine() {
		return line;
	}
	
}
