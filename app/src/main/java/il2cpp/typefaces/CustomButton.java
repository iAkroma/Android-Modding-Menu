package il2cpp.typefaces;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.animation.StateListAnimator;
import android.os.Handler;

public class CustomButton {
	private Context context;
	private String buttonText;
	
	private int COLOR1, COLOR2, COLOR3, COLOR4;
	private int featureid;

	private LinearLayout line;
	private Button button;
	private TextView text;
	
	private float sizeText;

	private buttonClick callback;

	private Typeface font;

	public static interface buttonClick {
		void onClick(int featureid);
	}

	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 15.0f);
	}

	public void click() {
		
		callback.onClick(featureid);
		
		GradientDrawable grad = new GradientDrawable();
		//grad.setColor(COLOR4);
		grad.setStroke(7, COLOR4);
		grad.setCornerRadius(150f);
		button.setTextSize(sizeText + 1f);
		button.setBackgroundDrawable(grad);
		
		Handler hand = new Handler();
		hand.postDelayed(new Runnable() {
			public void run() {
				GradientDrawable grad = new GradientDrawable();
				//grad.setColor(COLOR3);
				grad.setStroke(5, COLOR4);
				grad.setCornerRadius(150f);
				button.setTextSize(sizeText);
				button.setBackgroundDrawable(grad);
			}
		}, 200);
	}

	public CustomButton(Context _ctx, int feature, String _text, float size, buttonClick call) {
		context = _ctx;
		buttonText = _text;
		callback = call;
		featureid = feature;

		this.COLOR1 = Color.parseColor("#F2F2F2");
		this.COLOR2 = Color.parseColor("#C7C7C7");
		this.COLOR3 = Color.parseColor("#FF464CE2");
		this.COLOR4 = Color.parseColor("#FF464CE2");

		sizeText = size;
		
		font = Typeface.createFromAsset(context.getAssets(), "Wish/Font.ttf");

		line = new LinearLayout(context);
		line.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(20)));
		line.setOrientation(LinearLayout.HORIZONTAL);
		line.setGravity(Gravity.CENTER_VERTICAL);

		button = new Button(context);
		
		GradientDrawable grad = new GradientDrawable();
		//grad.setColor(COLOR3);
		grad.setStroke(5, COLOR4);
		grad.setCornerRadius(150f);
		
		button.setStateListAnimator(null);
		
		button.setBackgroundDrawable(grad);
		
		text = new TextView(context);
		button.setPadding(0,0,0,0);
		button.setText(buttonText);
		button.setTextSize(size);
		button.setTypeface(font);
		button.setGravity(Gravity.CENTER);
		button.setTextColor(Color.WHITE);
		button.setElevation(0f);

		OnClickListener clck = new OnClickListener() {
			@Override
			public void onClick(View v) {
				click();
			}
		};
		button.setOnClickListener(clck);
		text.setOnClickListener(clck);
		
		line.setPadding(10,3,10,3);
		line.addView(button, -1, -1);
		// line.addView(text);
	}

	public LinearLayout getLine() {
		return line;
	}

}
