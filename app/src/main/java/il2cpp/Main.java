package il2cpp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.Toast;
import il2cpp.typefaces.AlertInput;
import il2cpp.typefaces.CustomArrow;
import il2cpp.typefaces.CustomButton;
import il2cpp.typefaces.CustomSlider;
import il2cpp.typefaces.Menu;
import il2cpp.typefaces.SwitchButton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import il2cpp.typefaces.CfgSystem;
import android.content.res.AssetManager;
import java.net.URISyntaxException;

public class Main {
	protected static Context context;
	protected LinearLayout childOfScroll;
	
	public static boolean hide;
	
	private static native String[] getFeatures();
	private static native void OnChange(int feature, int value, String strv, boolean check);
	public static native String getTitle();
	
	public static void start(final Context context) {
		Main.context = context;
		if (Build.VERSION.SDK_INT >= 23) {
			if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
				|| context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				((Activity) context).requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
				init(context);
			}
			else {
				init(context);
			}
		}
		else {
			init(context);
		}
	}
	
	public static String urlRequest(String str) {
        
		StringBuilder sb = new StringBuilder();
        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(str).openConnection().getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
	
	public static void init(final Context context) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		System.loadLibrary("BLIZZARD");
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
				@Override
				public void run() {
				
				try {
					new Main().MenuMain(context);
				} catch(Exception e) {
					Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		}, 1000);
	}
	
	private int num(String txt) {
		return Integer.parseInt(txt);
	}
	
	public final void MenuMain(final Context context) {
		
		Main.context = context;
		
		final Menu menu = new Menu(context);
		//menu.setTitle(getTitle());
		
		int pageId = 0;
		
		String[] listFT = getFeatures();
		
		for (int i = 0; i < listFT.length; i++) {
			final int feature = i;
			String str = listFT[i];
			final String[] split = str.split("_");
			menu.setSize();
			// PAGE_Name_Icon_Lines
			// SWITCH_Page_Line_Feature_Text_Size
			// SLIDER_Page_Line_Feature_Text_Min_Max_Current_Size
			// BUTTON_Page_Line_Feature_Text_Size
			// ARROW_Page_Line_Feature_Texts_Size
			if (split[0].equals("PAGE")) {
				menu.newPage(pageId, split[1], split[2], num(split[3]));
				pageId++;
			} else if (split[0].equals("SWITCH")) {
				menu.newSwitch(num(split[1]), num(split[2]), num(split[3]), split[4], num(split[5]), new SwitchButton.switchChange() {
					public void onClick(int feat, int checked, boolean check) {
						OnChange(feat, checked, "", check);
					} 
				}); 
			} else if (split[0].equals("SLIDER")) {
				menu.newSlider(num(split[1]), num(split[2]), num(split[3]), split[4], num(split[8]), num(split[5]), num(split[6]), num(split[7]), new CustomSlider.sliderChange() {
					public void onChange(int feat, int value) {
						OnChange(feat, value, "", false);
					}
				});
			} else if (split[0].equals("BUTTON")) {
				menu.newButton(num(split[1]), num(split[2]), num(split[3]), split[4], num(split[5]), new CustomButton.buttonClick() {
					public void onClick(int feat) {
						OnChange(feat, 0, "", false);
					}
				});
			} else if (split[0].equals("INPUT")) {
				menu.newInput(num(split[1]), num(split[2]), num(split[3]), split[4], num(split[5]), new AlertInput.onChange() {
					public void onPut(String text) {
						OnChange(num(split[3]), 0, text, false);
					}
				});
			} else if (split[0].equals("TITLE")) {
				menu.newText(num(split[1]), num(split[2]), split[3], num(split[4]));
			} else if (split[0].equals("ARROW")) {
				menu.newArrow(num(split[1]), num(split[2]), num(split[3]), split[4].split(","), Float.parseFloat(split[5]), new CustomArrow.buttonClick() {
					public void onClick(int feat, int idx) {
						OnChange(feat, idx, "", false);
					}
				});
			}
		}
		if (pageId != 0) {
			menu.showPage(0);
		}
		
		CfgSystem cfg = new CfgSystem(context, menu);
			menu.page2List.get(4).get(0).addView(cfg.getLine());
		
		} 
}

