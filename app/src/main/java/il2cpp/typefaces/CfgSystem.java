package il2cpp.typefaces;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;
import android.content.ClipboardManager;
import android.view.Gravity;
import android.widget.ScrollView;

public class CfgSystem {
	private Context context;
	private Menu menu;
	
	private int COLOR1, COLOR2, COLOR3, COLOR4;
	
	private LinearLayout line;
	private LinearLayout configs;
	
	private HashMap<String, String> cfgList = new HashMap<>();
	
	private SharedPreferences pSharedPref;
	private String cfgLoad;
	private String cfgName;
	
	CustomButton loadCfg, newCfg, removeCfg, saveCfg;
	
	private Typeface font;

	public static interface switchChange {
		void onClick(int featureid, int ischeck, boolean checks);
	}
	
	public void updateList() {
		configs.removeAllViewsInLayout();
		Iterator cfgs = cfgList.keySet().iterator();
		while(cfgs.hasNext()) {
			final String name = (String) cfgs.next();
			final String cfgText = (String) cfgList.get(name);
			CustomButton cfg = new CustomButton(context, 0, name, 11, new CustomButton.buttonClick() {
				public void onClick(int f) {
					if (!name.equals(cfgName)) Toast.makeText(context, "Конфиг выбран, нажмите ещё раз, чтобы скопировать его", 0).show();
					if (name.equals(cfgName)) {
						ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE); 
						ClipData clip = ClipData.newPlainText(cfgList.get(cfgName), cfgList.get(cfgName));
						clipboard.setPrimaryClip(clip);
						Toast.makeText(context, "Конфиг скопирован", 0).show();
					}
					cfgName = name;
				}
			});
			configs.addView(cfg.getLine(), -1, dpi(25));
		}
	}

	public int dpi(float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	private void saveMap(String key, Map<String,String> inputMap){
		if (pSharedPref != null){
			JSONObject jsonObject = new JSONObject(inputMap);
			String jsonString = jsonObject.toString();
			SharedPreferences.Editor editor = pSharedPref.edit();
			editor.remove(key).commit();
			editor.putString(key, jsonString);
			editor.commit();
		}
	}

	private Map<String,String> loadMap(String key){
		Map<String,String> outputMap = new HashMap<String,String>();
		try{
			if (pSharedPref != null){
				String jsonString = pSharedPref.getString(key, (new JSONObject()).toString());
				JSONObject jsonObject = new JSONObject(jsonString);
				Iterator<String> keysItr = jsonObject.keys();
				while(keysItr.hasNext()) {
					String k = keysItr.next();
					String v = (String) jsonObject.get(k);
					outputMap.put(k,v);
				}
			}
		}catch(Exception e){
			Toast.makeText(context, e.toString(), 0).show();
			e.printStackTrace();
		}
		return outputMap;
	}
	
	
	public CfgSystem(Context _ctx, Menu men) {
		context = _ctx;
		menu = men;
		pSharedPref = context.getSharedPreferences("Configs", Context.MODE_PRIVATE);
		LinearLayout line1 = new LinearLayout(context);
		LinearLayout line2 = new LinearLayout(context);
		
		cfgList = (HashMap) loadMap("cfgList");
		
		loadCfg = new CustomButton(context, 0, "Load", 11, new CustomButton.buttonClick() {
			public void onClick(int f) {
				if (cfgList.containsKey(cfgName)) {
					menu.loadCfg(cfgList.get(cfgName));
				} else {
					Toast.makeText(context, "Конфиг не выбран", 0).show();
				}
			}
		});
		newCfg = new CustomButton(context, 0, "Add", 11, new CustomButton.buttonClick() {
			public void onClick(int f) {
				AlertInput nw = new AlertInput(context, "Введите текст конфига", new AlertInput.onChange() {
					public void onPut(final String cfgt) {
						AlertInput name = new AlertInput(context, "Введите название конфига", new AlertInput.onChange() {
							public void onPut(final String cfgn) {
								cfgList.put(cfgn, cfgt);
								updateList();
								saveMap("cfgList", cfgList);
								Toast.makeText(context, "Конфиг добавлен", 0).show();
							}
						});
					}
				});
			}
		});
		removeCfg = new CustomButton(context, 0, "Remove", 11, new CustomButton.buttonClick() {
			public void onClick(int f) {
				if (cfgList.containsKey(cfgName)) {
					cfgList.remove(cfgName);
					Toast.makeText(context, "Конфиг удален", 0).show();
					updateList();
					saveMap("cfgList", cfgList);
				} else {
					Toast.makeText(context, "Конфиг не выбран", 0).show();
				}
			}
		});
		saveCfg = new CustomButton(context, 0, "Save", 11, new CustomButton.buttonClick() {
			public void onClick(int f) {
				AlertInput nameCfg = new AlertInput(context, "Введите название конфига", new AlertInput.onChange() {
					public void onPut(String nm) {
						if (cfgList.containsKey(nm) && nm.length() == 0) {
							Toast.makeText(context, "Такое имя уже занято", 0).show();
						} else {
							String cfg = menu.getCfg();
							cfgList.put(nm, cfg);
							saveMap("cfgList", cfgList);
						}
						updateList();
					}
				});
				updateList();
			}
		});
		
		loadCfg.getLine().setPadding(5, 10, 2, 5);
		newCfg.getLine().setPadding(2, 10, 5, 5);
		
		removeCfg.getLine().setPadding(5, 2, 2, 5);
		saveCfg.getLine().setPadding(2, 2, 5, 5);
		
		line1.addView(loadCfg.getLine(), new LinearLayout.LayoutParams(-1, -1, 1));
		line1.addView(newCfg.getLine(), new LinearLayout.LayoutParams(-1, -1, 1));
		
		line2.addView(removeCfg.getLine(), new LinearLayout.LayoutParams(-1, -1, 1));
		line2.addView(saveCfg.getLine(), new LinearLayout.LayoutParams(-1, -1, 1));
		
		this.COLOR1 = Color.parseColor("#F2F2F2");
		this.COLOR2 = Color.parseColor("#C7C7C7");
		this.COLOR3 = Color.parseColor("#F04C4D");
		this.COLOR4 = Color.parseColor("#FF8281");

		font = Typeface.createFromAsset(context.getAssets(), "Wish/Font.ttf");

		line = new LinearLayout(context);
		line.setLayoutParams(new LinearLayout.LayoutParams(-1, dpi(210)));
		line.setOrientation(LinearLayout.VERTICAL);
		//line.setGravity(Gravity.CENTER_VERTICAL);

		
		line.setPadding(10, 0, 10, 0);
		
		configs = new LinearLayout(context);
		configs.setOrientation(LinearLayout.VERTICAL);
		
		
		updateList();
		
		LinearLayout line0 = new LinearLayout(context);
		
		line0.setPadding(5, 5, 5, 0);
		
		LinearLayout pcfg = new LinearLayout(context);
		pcfg.setOrientation(LinearLayout.VERTICAL);
		pcfg.setGravity(Gravity.CENTER);
		
		ScrollView scrl = new ScrollView(context);
		scrl.addView(configs, -1, -1);
		
		pcfg.addView(scrl, -1, -1);
		GradientDrawable grad = new GradientDrawable();
		grad.setColor(Color.parseColor("#17191D"));
		grad.setCornerRadius(20);
		pcfg.setBackgroundDrawable(grad);
		pcfg.setPadding(5,10,5,10);
		
		
		line0.addView(pcfg, new LinearLayout.LayoutParams(-1, -1));
		
		line.addView(line0, new LinearLayout.LayoutParams(-1, -1, 2));
		line.addView(line1, new LinearLayout.LayoutParams(-1, dpi(45), 1));
		line.addView(line2, new LinearLayout.LayoutParams(-1, dpi(45), 1));
		
	}

	public LinearLayout getLine() {
		return line;
	}

}
