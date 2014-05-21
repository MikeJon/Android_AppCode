package com.freerdp.freerdpcore.presentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jz.vdistack.R;
import com.jz.vdistack.fragment.SetingsFragment;
import com.jz.vdistack.fragment.ViewPageAdapter;
import com.jz.vdistack.fragment.WcTopView;
import com.jz.vdistack.fragment.WcViewPage;
import com.jz.vdistack.network.WcNetWorkUtils;
import com.jz.vdistack.update.UpdateDialog;
import com.jz.vdistack.update.UpdateDialog.UpdateDialogOnclick;
import com.jz.vdistack.update.UpdateMessage;
import com.jz.vdistack.update.UpdateServce;
import com.jz.vdistack.update.WcUpdateManage;

/*****
 * 
 * @author asus
 *
 */
public  class JzVDIHomeActivity extends FragmentActivity implements OnPageChangeListener{
     private WcViewPage viewPager ;
     private ViewPageAdapter vpAdapter;  
     private List<View> views; 
     private static final int[] pics = { R.drawable.leadpage1,  
         R.drawable.leadpage2, R.drawable.leadpage3,  
         R.drawable.point_help,R.drawable.pad_mousewaypic };  
 //底部小店图片
     private ImageView[] dots ;
     private LinearLayout ll;
 //记录当前选中位置  
     private int currentIndex;  
     private FragmentTransaction fragmentTransaction;
     private WcTopView wcTopView;
   	 protected SetingsFragment setingsFragment;
	 private int index = 0;
	 public static JzVDIHomeActivity jzVDIHomeActivity; 
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.vdi_main);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,ActionBar.DISPLAY_HOME_AS_UP);
		getActionBar().hide();
		wcTopView=(WcTopView)findViewById(R.id.topview);
		final SharedPreferences spf = getSharedPreferences("FIRST",Context.MODE_MULTI_PROCESS);
		boolean b =spf.getBoolean("FIRST", true);
		if(b){
	    wcTopView.setBackgroundColor(getResources().getColor(R.color.wccolor));
	    TextView textView = new TextView(this);
	    textView.setGravity(Gravity.CENTER);
	    textView.setTextColor(getResources().getColor(android.R.color.black));
	    textView.setTextSize(20);
	    wcTopView.setIstouche(true);
	    textView.setText("左右滑动查看操作说明 <点击我知道啦>");
	    textView.setClickable(true);
	    textView.setFocusableInTouchMode(true);
	    textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Editor e = spf.edit();
				 e.putBoolean("FIRST", false);
				 e.commit();
				 wcTopView.setIstouche(false);
				 wcTopView.removeAllViews();
				 wcTopView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}
		});
	    
	    Log.v("WC","HHHHHHHHHHHHHHHHHHHHHHHHHHHH::"+WcNetWorkUtils.getMacInfo(this));
	    FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	    wcTopView.addView(textView,fl);
	}
	   
		views = new ArrayList<View>();  
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  
	            LinearLayout.LayoutParams.MATCH_PARENT);  
	          
	        //初始化引导图片列表  
	        for(int i=0; i<pics.length; i++) {  
	            ImageView iv = new ImageView(this);  
	            iv.setLayoutParams(mParams);  
	            iv.setScaleType(ScaleType.FIT_XY);
	            iv.setImageResource(pics[i]);  
	            views.add(iv);  
	        }  
		viewPager = (WcViewPage)findViewById(R.id.viewpager);
        //初始化Adapter  
        vpAdapter = new ViewPageAdapter(views);  
        viewPager.setAdapter(vpAdapter);  
        //绑定回调  
        viewPager.setOnPageChangeListener(this);  
          
        //初始化底部小点  
        initDots();  
		findViewById(R.id.log).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sessionIntent = new Intent(JzVDIHomeActivity.this, SessionActivity.class);							
				startActivity(sessionIntent);
				 
			}
		});
        findViewById(R.id.setings).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 viewPager.setIsmovew(false);
				 ll.setVisibility(View.GONE);
				 index  = 1;
				 getActionBar().show();
				 getActionBar().setTitle("设置");
				 setingsFragment = new SetingsFragment();
				 fragmentTransaction =getSupportFragmentManager().beginTransaction();
				 fragmentTransaction.replace(R.id.content, setingsFragment);
				 fragmentTransaction.setCustomAnimations(R.anim.showgradual, R.anim.gradual);
				 fragmentTransaction.commit();
				 fragmentTransaction.show(setingsFragment);
				 View view = findViewById(R.id.linear);
				 Animation animation =AnimationUtils.loadAnimation(getBaseContext(), R.anim.gradual);
				 animation.setAnimationListener(animationListener);
				 view.startAnimation(animation);  
			}
		});
        if(savedInstanceState!=null&&savedInstanceState.getBoolean("UPDATE")){
        	
        }else{
        	jzVDIHomeActivity = this;
            Intent intent = new Intent(this,UpdateServce.class);
            startService(intent); 
        }
        
	}
	
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

    

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putBoolean("UPDATE", true);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);
		return true;
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		Intent intent =null;
		switch (itemId) {
		case R.id.newBookmark:
		     intent = new Intent(this, BookmarkActivity.class);
			break;
		case R.id.appSettings:
			intent = new Intent(this, ApplicationSettingsActivity.class);
			break;
		case R.id.help:
			intent = new Intent(this, HelpActivity.class);
		case R.id.exit:
			finish();
			break ;
		case R.id.about:
			intent= new Intent(this, AboutActivity.class);
			break;
		case android.R.id.home:
			if(index ==1){
				 viewPager.setIsmovew(true);
				 ll.setVisibility(View.VISIBLE);
				 getActionBar().hide();
				 fragmentTransaction =getSupportFragmentManager().beginTransaction();
				 fragmentTransaction.setCustomAnimations(R.anim.showgradual,R.anim.gradual);
				 fragmentTransaction.commit();
				 fragmentTransaction.hide(setingsFragment);
				 View view = findViewById(R.id.linear);
				 Animation animation =AnimationUtils.loadAnimation(getBaseContext(), R.anim.showgradual);
				 animation.setAnimationListener(animationListener);
				 view.startAnimation(animation);  
				 index=0;
			}else if(index >=2&&!wcTopView.isIstouche()){
				  setingsFragment.setDisplayHome();
			}
			 
			break;
		default:
			break;
		}
		if(intent !=null){
			startActivity(intent);
		}
		
		return true;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&index!=0){
			if(index ==1){
				 viewPager.setIsmovew(true);
				 ll.setVisibility(View.VISIBLE);
				 getActionBar().hide();
				 fragmentTransaction =getSupportFragmentManager().beginTransaction();
				 fragmentTransaction.setCustomAnimations(R.anim.showgradual,R.anim.gradual);
				 fragmentTransaction.commit();
				 fragmentTransaction.hide(setingsFragment);
				 View view = findViewById(R.id.linear);
				 Animation animation =AnimationUtils.loadAnimation(getBaseContext(), R.anim.showgradual);
				 animation.setAnimationListener(animationListener);
				 view.startAnimation(animation);  
				 index=0;
			}else if(index >= 2&&!wcTopView.isIstouche()){
				 
				 setingsFragment.setDisplayHome( );
				  
			}
			
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}


	 
	 
	private void analysisJson(String str) throws JSONException{
		JSONObject jsonObject = new JSONObject(str);
		 
		
		//Log.v("WC","KK:"+str);
	}
	 
	 private AnimationListener animationListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			wcTopView.setIstouche(true);
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			View ll = findViewById(R.id.linear);
			if(ll.getVisibility()==View.GONE){
				ll.setVisibility(View.VISIBLE);
			}else{
				ll.setVisibility(View.GONE);
			}
			wcTopView.setIstouche(false);
			
		}
	};

	



	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setCurDot(arg0); 
	}
	
	private void initDots() {  
        ll = (LinearLayout) findViewById(R.id.ll);  
        dots = new ImageView[pics.length];  
        //循环取得小点图片  
        for (int i = 0; i < pics.length; i++) {  
            dots[i] = (ImageView) ll.getChildAt(i);  
            dots[i].setEnabled(true);//都设为灰色  
           // dots[i].setOnClickListener(this);  
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应  
        }  
        currentIndex = 0;  
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态  
    }  
      
  /** 
 *这只当前引导小点的选中  
 */  
	private void setCurDot(int positon)  {  
		    if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) 
		        return;  
		    dots[positon].setEnabled(false);  
		    dots[currentIndex].setEnabled(true);  
		    currentIndex = positon;  
		}


	public int getIndex() {
		return index;
	}
	
	
	public void setIndex(int index) {
		this.index = index;
	}  
	
	public void setIstouche(boolean bool){
		wcTopView.setIstouche(bool);
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	   jzVDIHomeActivity = null;
		super.onDestroy();
	}
	
	
	
}
