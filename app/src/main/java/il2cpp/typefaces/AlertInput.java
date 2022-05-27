package il2cpp.typefaces;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlertInput {
	private Context context;
	private String buttonText;
	
	private int COLOR1, COLOR2, COLOR3, COLOR4;
	private String value = null;
	
	private onChange callback;
	private EditText myedittext2;

	private Typeface font;

	public static interface onChange {
		void onPut(String text);
	}

	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public void setPut(String text) {
		callback.onPut(value);
	}
	
	public AlertInput(Context _ctx, String _text, onChange call) {
		context = _ctx;
		buttonText = _text;
		
		callback = call;
		
		this.COLOR1 = Color.parseColor("#2D3037");
		this.COLOR2 = Color.parseColor("#17191D");
		this.COLOR3 = Color.parseColor("#F04C4D");
		this.COLOR4 = Color.parseColor("#8C9EE1");

		font = Typeface.createFromAsset(context.getAssets(), "Wish/Font.ttf");
		
		final AlertDialog.Builder dialog2 = new AlertDialog.Builder(context);
		//dialog2.setTitle(nameFeat);
		//dialog2.setMessage("Введите текст");
		myedittext2 = new EditText(context);

		myedittext2.setTextSize(15f);
		myedittext2.setTextColor(Color.WHITE);
		myedittext2.setHintTextColor(Color.GRAY);
		myedittext2.setTypeface(font);

		LinearLayout.LayoutParams myp2 = new LinearLayout.LayoutParams(-1, -1);

		LinearLayout line = new LinearLayout(context);
		line.setOrientation(LinearLayout.VERTICAL);
		TextView textView = new TextView(context);

		textView.setText(_text);
		textView.setTextSize(15f);
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		textView.setTypeface(font);
		line.addView(textView, -1, -2);

		// BUTT
		LinearLayout linep2 = new LinearLayout(context);
		linep2.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(20)));
		
		linep2.setPadding(20, 20, 20, 20);
		
		line.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		line.setBackgroundColor(COLOR1);
		myedittext2.setLines(1);

		myp2.setMargins(20, 0, 20, 0);
		myedittext2.setLayoutParams(myp2);

		line.addView(myedittext2);
		dialog2.setView(line);
		
		GradientDrawable grad = new GradientDrawable();
		grad.setColor(COLOR2);
		grad.setCornerRadius(5f);
		
		myedittext2.setBackgroundDrawable(grad);
		
		myedittext2.setHint(_text);
		line.addView(linep2, -1, -2);
		
		
		final Dialog d = dialog2.show();
		CustomButton butt = new CustomButton(context, 0, "OK", 13f, new CustomButton.buttonClick() {
			public void onClick(int f) {
				value = myedittext2.getText().toString();
				setPut(value);
				d.dismiss();
			}
		});
		butt.getLine().setPadding(10, 0, 10, 10);
		line.addView(butt.getLine(), -1, -1);
	}
	
	public void open() {
		
	}
	
	public String getValue() {
		return value;
	}
}
