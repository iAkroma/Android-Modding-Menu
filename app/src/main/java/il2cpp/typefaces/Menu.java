package il2cpp.typefaces;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import il2cpp.Main;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;

class PageButton {

	LinearLayout mainlayout;
	TextView title;
	Context context;
	String buttonText;
	boolean isTap = false;

	public void setChecked(boolean check) {
		// Меняем дизайн кнопки
		isTap = check;
		GradientDrawable grad = new GradientDrawable();
		if (isTap) {
			grad.setColor(Color.parseColor("#304040"));
		}
		grad.setCornerRadius(19f);
		title.setBackgroundDrawable(grad);
	}

	public void setOnClick(OnClickListener clck) {
		mainlayout.setOnClickListener(clck);
		title.setOnClickListener(clck);
	}

	public PageButton(Context ctx, String name, boolean chk) {
		context = ctx;
		buttonText = name;

		mainlayout = new LinearLayout(context);
		title = new TextView(context);

		// сам лаяут кнопки
		mainlayout.setLayoutParams(new LinearLayout.LayoutParams(Menu.dp(45, context), -1));
		mainlayout.setGravity(Gravity.CENTER);

		// Кнопка вкладки
		title.setText(buttonText);
		title.setTextSize(9f);
		title.setTextColor(Color.WHITE);
		title.setTypeface(Menu.getfont(context));
		title.setGravity(Gravity.CENTER);

		setChecked(chk);

		mainlayout.addView(title, Menu.dp(40, context), Menu.dp(20, context));
	}

	public LinearLayout getLine() {
		// Возращаем сам виджет
		return mainlayout;
	}

}

public class Menu {
	
	protected int WIDTH = 900, HEIGHT = 800;
	private int COLOR1, COLOR2, COLOR3;
	private float CORNER_VALUE;
	
	protected boolean isHide = false;
	
	protected Context context;
	protected boolean isIconVisible;
	protected boolean isMenuVisible;
	protected ImageView iconView;
	protected FrameLayout parentBox;
	protected TextView closeButton;
	protected LinearLayout menulayout, headerLayout, pagesLayout, pageView;
	protected ScrollView scrollItems;
	protected ScrollView scrollPages;    
	protected boolean sem = false;
	
	protected ArrayList<PageButton> pageButtons = new ArrayList<>();
	protected ArrayList<LinearLayout> pageList = new ArrayList<>();
	public ArrayList<ArrayList<LinearLayout>> page2List = new ArrayList<>();
	
	public static void fromBase64(ImageView v, String base) {
		byte[] decodedString = Base64.decode(base, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		v.setImageBitmap(decodedByte);
	}
	
	// CFG
	protected HashMap<String, SwitchButton> switchList = new HashMap<>();
	protected HashMap<String, CustomSlider> sliderList = new HashMap<>();
	protected HashMap<String, CustomArrow> arrowList = new HashMap<>();
	
	protected TextView title;

	protected int isMenuHide;

	protected WindowManager wmManager;
	protected WindowManager.LayoutParams wmParams;

	protected LinearLayout childOfScroll;
	protected LinearLayout childOfScrollPages;

	public static Typeface getfont(Context ctx) {
		Typeface font = Typeface.createFromAsset(ctx.getAssets(), "Wiah/Font.ttf");
		return font; 
	}
	
	private ESPView esp;

	public static native void DrawOn(ESPView espView, Canvas canvas);

	private void DrawCanvas() {
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2, 56 | WindowManager.LayoutParams.FLAG_FULLSCREEN, -3); 
		layoutParams.gravity = 51; 
		layoutParams.x = 0; 
		layoutParams.y = 0;
		esp.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        wmManager.addView(esp, layoutParams);
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
	
	public void setSem(boolean b) {
		sem = b;
	}

	/* CALLBACKS */

	public void setTitle(String text) {
		title.setText(text);
	}
	
	protected void init(Context context)
	{
		isMenuHide = 0;
		this.context = context;
		
		this.WIDTH  = dpi(290);
		this.HEIGHT = dpi(260);
		this.COLOR1 = Color.parseColor("#00000000"); //хз че это 
		this.COLOR2 = Color.parseColor("#FF00FF"); //хз че это 
		this.COLOR3 = Color.parseColor("#5A0FB4"); //линии
		this.CORNER_VALUE = 25f;
		
		isIconVisible = false;
		isMenuVisible = false;
		iconView = new ImageView(context);

		parentBox = new FrameLayout(context);

		parentBox.setOnTouchListener(handleMotionTouch);
		wmManager = ((Activity)context).getWindowManager();
		int aditionalFlags=0;
		if (Build.VERSION.SDK_INT >= 11)
			aditionalFlags = WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
		if (Build.VERSION.SDK_INT >=  3)
			aditionalFlags = aditionalFlags | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		wmParams = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			30,//initialX
			50,//initialy
			WindowManager.LayoutParams.TYPE_APPLICATION,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
			WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN |
			WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
			WindowManager.LayoutParams.FLAG_FULLSCREEN |
			aditionalFlags,
			PixelFormat.TRANSPARENT
		);
		wmParams.gravity = Gravity.TOP | Gravity.LEFT;
	}

	public void setIconImage()
	{
		iconView = new ImageView(context);
		setAss(iconView, "icon.png");
	}
	public void setWidth(int px)
	{
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)menulayout.getLayoutParams();
		lp.width = px;
		menulayout.setLayoutParams(lp);
		WIDTH=px;
	}
	public void setHeight(int px)
	{
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)menulayout.getLayoutParams();
		lp.height = px;
		menulayout.setLayoutParams(lp);
		HEIGHT=px;
	}
	public int getWidth(int px) {return WIDTH;}
	public int getHeight(int px) {return HEIGHT;}

	public void showIcon() {
		ObjectAnimator animation = ObjectAnimator.ofFloat(iconView, "alpha", 0, 1.0f);
		animation.setDuration(550);
		animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

				}

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
		animation.start();
		if (Main.hide) {
			iconView.setVisibility(View.INVISIBLE);
		} else if (!Main.hide) {
			iconView.setVisibility(View.VISIBLE);
		}
		if (isHide) {
			iconView.setVisibility(View.INVISIBLE);
		}
		if (!isIconVisible)
		{
			isMenuVisible = false;
			parentBox.removeAllViews();
			parentBox.addView(iconView, dpi(50),dpi(50));
			isIconVisible = true;
		}
	}

	public void setSize() {
		menulayout.setLayoutParams(new ViewGroup.LayoutParams(dpi(220), dpi(220)));
	}
	
	public void showMenu() {
		ObjectAnimator animation = ObjectAnimator.ofFloat(menulayout, "alpha", 0, 1.0f);
		animation.setDuration(500);
		animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

				}

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
		animation.start();
		if (!isMenuVisible)
		{
			isIconVisible = false;
			parentBox.removeAllViews();
			parentBox.addView(menulayout, WIDTH, HEIGHT);
			isMenuVisible = true;
		}
	}

	public void newPage(final int pageid, String text, String iconSrc, int countLines) {
		LinearLayout pagelayout = new LinearLayout(context);
		pagelayout.setOrientation(LinearLayout.HORIZONTAL);
		
		PageButton pageBtn = new PageButton(context, text, false);
		pagesLayout.addView(pageBtn.getLine());
		pageButtons.add(pageBtn);
		
		ArrayList<LinearLayout> lines = new ArrayList<>();
		for (int i = 0; i < countLines; i++) {
			LinearLayout line = new LinearLayout(context);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1, 1);
			GradientDrawable grad = new GradientDrawable();
			
			lp.setMargins(5, 0, 5, 0);
			line.setLayoutParams(lp);
			line.setOrientation(LinearLayout.VERTICAL);
			
			//grad.setColor(COLOR2);
			grad.setCornerRadius(CORNER_VALUE);
			line.setBackgroundDrawable(grad);
			
			LinearLayout lineLow = new LinearLayout(context);
			GradientDrawable grad2 = new GradientDrawable();
			grad2.setColor(COLOR3);
			grad2.setCornerRadius(CORNER_VALUE);
			lineLow.setBackgroundDrawable(grad2);
			
			line.addView(lineLow, -1, dpi(5));
			
			ScrollView scrl = new ScrollView(context);
			scrl.setScrollBarSize(0);
			scrl.setMinimumHeight(1);
			scrl.setMinimumWidth(1);
			LinearLayout scroll = new LinearLayout(context);
			scroll.setOrientation(LinearLayout.VERTICAL);
			
			line.addView(scrl, new LinearLayout.LayoutParams(-1, -1 ,1));
			scroll.setPadding(0, 5, 0, 5);
			scrl.setPadding(10,0,10,0);
			scrl.addView(scroll, -1, -1);
			
			lines.add(scroll);
			pagelayout.addView(line);
		}
		
		page2List.add(lines);
		
		pageBtn.setOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPage(pageid);
			}
		});
		
		pageList.add(pagelayout);
		pageView.addView(pagelayout, -1, -1);
	}
	
	public void showPage(int id) {
		for (int idx = 0; idx < pageButtons.size(); idx++) {
			pageButtons.get(idx).setChecked(false);
		}
		for (int pgx = 0; pgx < pageList.size(); pgx++) {
			pageList.get(pgx).setVisibility(View.GONE);
		}
		pageList.get(id).setVisibility(View.VISIBLE);
		pageButtons.get(id).setChecked(true);
		
		ObjectAnimator animation = ObjectAnimator.ofFloat(pageList.get(id), "alpha", 0.4f, 1.0f);
		animation.setDuration(250);
		animation.start();
	}
	
	public void newSwitch(int pageid, int lineid, int feat, String text, float size, final SwitchButton.switchChange callb) {
		SwitchButton butt = new SwitchButton(context, feat, text, size, callb);
		switchList.put(String.format("%d:%d", pageid, feat), butt);
		page2List.get(pageid).get(lineid).addView(butt.getLine());
	}
	
	public void newSlider(int pageid, int lineid, int feat, String text, float textsize, int min, int max, int curr, CustomSlider.sliderChange clbk) {
		CustomSlider slider = new CustomSlider(context, feat, text, textsize, min, max, curr, clbk);
		sliderList.put(String.format("%d:%d", pageid, feat), slider);
		page2List.get(pageid).get(lineid).addView(slider.getLine());
	}
	
	public void newButton(int pageid, int lineid, int feat, String text, float size, final CustomButton.buttonClick call) {
		CustomButton butt = new CustomButton(context, feat, text, size, call);
		page2List.get(pageid).get(lineid).addView(butt.getLine());
	}
	
	public void newInput(int pageid, int lineid, int feat,final String text, float size, final AlertInput.onChange call) {
		CustomButton butt = new CustomButton(context, feat, text, size, new CustomButton.buttonClick() {
			public void onClick(int featr) {
				new AlertInput (context, text, call);
			}
		});
		page2List.get(pageid).get(lineid).addView(butt.getLine());
	}
	
	public void newArrow(int pageid, int lineid, int feat,final String[] text, float size, final CustomArrow.buttonClick call) {
		CustomArrow arr = new CustomArrow(context, feat, text, call);
		arrowList.put(String.format("%d:%d", pageid, feat), arr);
		page2List.get(pageid).get(lineid).addView(arr.getLine());
	}
	
	public void newText(int pageid, int lineid, String text, float size) {
		
		LinearLayout line = new LinearLayout(context);
		line.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(20)));
		line.setOrientation(LinearLayout.VERTICAL);
		line.setGravity(Gravity.CENTER_VERTICAL);
		line.setPadding(5,5,5,5);
		TextView title = new TextView(context);
		title.setText(text);
		title.setTextSize(10);
		title.setTypeface(getfont(context));
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		
		GradientDrawable grad = new GradientDrawable();
		grad.setColor(Color.parseColor("#5A0FB4"));//линии возле кнопок
		grad.setCornerRadius(20f);
		
		title.setBackgroundDrawable(grad);
		
		line.addView(title,  -1, -1);
		
		page2List.get(pageid).get(lineid).addView(line, -1, dpi(15));
		
	}
	
	public void loadCfg(String cfg) {
		
		try {
			byte[] data = Base64. decode(cfg, Base64. DEFAULT);
			String text = new String(data, "UTF-8");
			cfg = text;
		} catch (UnsupportedEncodingException e) {}

		String[] fncs = cfg.split(",");
		for (int id = 0; id < fncs.length; id++) {
			String[] cfgItem = fncs[id].split(">");
			if (cfgItem[0].equals("switch")) {
				if (switchList.containsKey(cfgItem[1])) {
					switchList.get(cfgItem[1]).setChecked(Boolean.parseBoolean(cfgItem[2]));
				}
			} else if (cfgItem[0].equals("slider")) {
				if (sliderList.containsKey(cfgItem[1])) {
					sliderList.get(cfgItem[1]).setProgress(Integer.parseInt(cfgItem[2]));
				}
			} else if (cfgItem[0].equals("arrow")) {
				if (arrowList.containsKey(cfgItem[1])) {
					arrowList.get(cfgItem[1]).setValue(Integer.parseInt(cfgItem[2]));
				}
			}
		}
	}
	
	public String getCfg() {
		String cfg = "";
		
		Iterator switches = switchList.keySet().iterator();
		while(switches.hasNext()) {
			String key= (String) switches.next();
			SwitchButton value = (SwitchButton) switchList.get(key);
			String val = Boolean.toString(value.isChecked);
			String item = "," + "switch>" + key + ">" + val;
			cfg += item;
		}
		
		Iterator sliders = sliderList.keySet().iterator();
		while(sliders.hasNext()) {
			String key= (String) sliders.next();
			CustomSlider value = (CustomSlider) sliderList.get(key);
			String val = Integer.toString(value.progress);
			
			String item = "," + "slider>" + key + ">" + val;
			cfg += item;
		}
		
		Iterator arrows = arrowList.keySet().iterator();
		while(arrows.hasNext()) {
			String key = (String) arrows.next();
			CustomArrow value = (CustomArrow) arrowList.get(key);
			String val = Integer.toString(value.current);

			String item = "," + "arrow>" + key + ">" + val;
			cfg += item;
		}
		
		cfg = cfg.substring(1);
		
		try {
			byte[] data = cfg.getBytes("UTF-8");
			String base64 = Base64.encodeToString(data, Base64. DEFAULT);
			
			return base64;
		} catch (UnsupportedEncodingException e) {}
		
		return "?";
	}
	
	public int dpi(float dp)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public static int dp(float dp, Context context)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public void goUrl(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}
	
	public Menu(final Context context) {
		
		init(context);
		
		try {
			esp = new ESPView(context);
			DrawCanvas();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), 1).show();
		}

		menulayout = new LinearLayout(context);
		menulayout.setOrientation(LinearLayout.VERTICAL);
		scrollItems = new ScrollView(context);
		scrollItems.setBackgroundColor(Color.TRANSPARENT);

		setIconImage();
		try {

			wmManager.addView(parentBox, wmParams);
			showMenu();

			/* Menu layout */
			GradientDrawable grad = new GradientDrawable();
			grad.setCornerRadius(CORNER_VALUE);
			//grad.setColor(COLOR1);
			menulayout.setOrientation(LinearLayout.VERTICAL);
			menulayout.setBackgroundDrawable(grad);
			/* Menu layout */
			
			LinearLayout menu = new LinearLayout(context);
			menu.setOrientation(LinearLayout.VERTICAL);
			GradientDrawable gradd = new GradientDrawable();
			gradd.setCornerRadius(CORNER_VALUE);
			gradd.setColor(Color.parseColor("#303030"));//ОСНОВНОЙ ЦВЕТ МЕНЮ 
			menu.setBackgroundDrawable(gradd);
			
			// ***** HEADER *****
			headerLayout = new LinearLayout(context);
			GradientDrawable grad2 = new GradientDrawable();
			grad2.setCornerRadii(new float[] {CORNER_VALUE, CORNER_VALUE, CORNER_VALUE, CORNER_VALUE, 0, 0, 0, 0});
			grad2.setColor(COLOR3);
			headerLayout.setBackgroundDrawable(grad2);
			headerLayout.setGravity(Gravity.CENTER);
			
			menulayout.addView(headerLayout, -1, dpi(5));
			
			// ***** TITLE *****
			
			// ***** PAGES LAYOUT *****
			
			LinearLayout titleBlock = new LinearLayout(context);
			TextView titlet = new TextView(context);
			titlet.setText("Shainy");
			titlet.setTextSize(13);
			titlet.setTypeface(getfont(context));
			titlet.setTextColor(Color.parseColor("#FFFFFF"));//ЦВЕТ НАЗВАНИЯ МЕНЮ 
			titlet.setGravity(Gravity.CENTER);
			titleBlock.addView(titlet, -1, -1);
			titlet.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						showIcon();
					}
				});

			// Полоска между текстом и вкладкой
			LinearLayout div = new LinearLayout(context);
			LinearLayout div2 = new LinearLayout(context);

			div2.setBackgroundColor(Color.parseColor("#000000"));
			div.setPadding(0, 20, 0, 20);
			div.addView(div2, dpi(80), -1);
			
			pagesLayout = new LinearLayout(context);
			pagesLayout.setOrientation(LinearLayout.HORIZONTAL);
			pagesLayout.setPadding(10,0,10,0);
			GradientDrawable pg = new GradientDrawable();
			pg.setColor(Color.parseColor("#303030"));///Цвет Верхней хуйни
			pg.setCornerRadii(new float[] {0, 0, 0, 0, CORNER_VALUE, CORNER_VALUE, CORNER_VALUE, CORNER_VALUE});
			pagesLayout.setBackgroundDrawable(pg);
			
			pagesLayout.addView(titleBlock, dpi(50), -1);
			pagesLayout.addView(div, dpi(2), -1);
			
			menulayout.addView(pagesLayout, -1, dpi(30f));
			
			LinearLayout ln = new LinearLayout(context);
			menulayout.addView(ln, -1, dpi(5));
			
			// ***** Page view layout ***** 
			pageView = new LinearLayout(context);
			// pageView.setBackgroundColor(Color.RED);
			pageView.setPadding(10,0,10,0);
			
			menu.addView(pageView, new LinearLayout.LayoutParams(-1, -1, 1));
			
			// ***** CLOSE *****
			closeButton = new TextView(context);
			closeButton.setTypeface(getfont(context));
			closeButton.setText(Main.getTitle());
			closeButton.setTextSize(9f);
			closeButton.setGravity(Gravity.LEFT | Gravity.LEFT);
			closeButton.setTextColor(Color.WHITE);
			closeButton.setPadding(10,0,10,0);
			
			LinearLayout closeLayout = new LinearLayout(context);
			closeLayout.setGravity(Gravity.LEFT);

			closeLayout.addView(closeButton, -1, -1);
			
			menu.addView(closeLayout, -1, -2);
			
			menu.setPadding(0, 10, 0, 0);
			menulayout.addView(menu, -1, -1);
			
			showIcon();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}
	}



	View.OnTouchListener handleMotionTouch = new View.OnTouchListener()
	{
		private float initX;          
		private float initY;
		private float touchX;
		private float touchY;

		double clock=0;
		@Override
		public boolean onTouch(View vw, MotionEvent ev)
		{

			switch (ev.getAction())
			{
				case MotionEvent.ACTION_DOWN:

					initX = wmParams.x;
					initY = wmParams.y;
					touchX = ev.getRawX();
					touchY = ev.getRawY();
					clock = System.currentTimeMillis();
					break;

				case MotionEvent.ACTION_MOVE:
					wmParams.x = (int)initX + (int)(ev.getRawX() - touchX);

					wmParams.y = (int)initY + (int)(ev.getRawY() - touchY);


					wmManager.updateViewLayout(vw, wmParams);
					break;

				case MotionEvent.ACTION_UP:
					if (isIconVisible && (System.currentTimeMillis() < (clock + 200)))
					{
						showMenu();
					}
					break;
			}
			return true;
		}
	};

	private int convertDipToPixels(int i) {
        return (int) ((((float) i) * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
