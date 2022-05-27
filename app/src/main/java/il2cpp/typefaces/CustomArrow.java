package il2cpp.typefaces;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomArrow
 {
	private Context context;
	
	private int COLOR1, COLOR2, COLOR3, COLOR4;
	private int featureid;
	public int current = 0;
	private String[] texts;
	
	private LinearLayout line;
	private TextView text;

	private float sizeText;

	private buttonClick callback;

	private Typeface font;

	public static interface buttonClick {
		void onClick(int featureid, int indx);
	}

	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public void swipe(int offset) {
		current += offset;
		if (current < 0) {
			current = texts.length - 1;
		}
		if (current >= texts.length) {
			current = 0;
		}
		setValue(current);
	}
	
	public void setValue(int idx) {
		current = idx;
		callback.onClick(featureid, idx);
		text.setText(texts[idx]);
	}
	
	public CustomArrow(Context _ctx, int feature, String[] _text, final buttonClick call) {
		context = _ctx;
		callback = call;
		featureid = feature;

		texts = _text;
		
		this.COLOR1 = Color.parseColor("#F2F2F2");
		this.COLOR2 = Color.parseColor("#C7C7C7");
		this.COLOR3 = Color.parseColor("#F04C4D");
		this.COLOR4 = Color.parseColor("#8C9EE1");

		// sizeText = size;

		font = Typeface.createFromAsset(context.getAssets(), "Wish/Font.ttf");

		line = new LinearLayout(context);
		line.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(20)));
		line.setOrientation(LinearLayout.HORIZONTAL);
		line.setGravity(Gravity.CENTER_VERTICAL);
		
		CustomButton button1 = new CustomButton(context, 0, "<", 12f, new CustomButton.buttonClick() {
			public void onClick(int feat) {
				swipe(-1);
			}
		});
		button1.getLine().setLayoutParams(new LinearLayout.LayoutParams(dpi(50), -1));
		button1.getLine().setPadding(0,0,0,0);
		
		CustomButton button2 = new CustomButton(context, 0, ">", 12f, new CustomButton.buttonClick() {
			public void onClick(int feat) {
				swipe(1);
			}
		});
		button2.getLine().setLayoutParams(new LinearLayout.LayoutParams(dpi(50), -1));
		button2.getLine().setPadding(0,0,0,0);
		
		text = new TextView(context);
		text.setText(texts[0]);
		text.setTextSize(13);
		text.setTypeface(font);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.WHITE);
		
		line.setPadding(10,3,10,3);
		line.addView(button1.getLine());
		
		line.addView(text, new LinearLayout.LayoutParams(-1, -1, 1));
		
		line.addView(button2.getLine());
	}

	public LinearLayout getLine() {
		return line;
	}

}
