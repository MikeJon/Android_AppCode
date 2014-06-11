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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jz.vdistack.R;
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
     private WcViewPage  viewPager ;
      
     private ViewPageAdapter vpAdapter;  
     private List<View> views; 
     public static final int[] pics = { R.drawable.leadpage1,  
                    R.drawable.leadpage2, R.drawable.vdi_back};  
 //底部小店图片
     private ImageView[] dots ;
     private LinearLayout ll;
 //记录当前选中位置  
     private int currentIndex;  
	 private int index = 0;
	 public static JzVDIHomeActivity jzVDIHomeActivity; 
	 private View content;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.vdi_main);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,ActionBar.DISPLAY_HOME_AS_UP);
		getActionBar().hide();
		viewPager = (WcViewPage)findViewById(R.id.viewpager);
		final SharedPreferences spf = getSharedPreferences("FIRST",Context.MODE_MULTI_PROCESS);
		boolean b =spf.getBoolean("FIRST", true);
		content = findViewById(R.id.linear);
		if(b){
	       views = new ArrayList<View>();  
	       content.setVisibility(View.GONE);
	       LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);  
		        //初始化引导图片列表  
		        for(int i=0; i<pics.length; i++) {  
		        	RelativeLayout iv=null;
		        	iv = new RelativeLayout(this);  
			        iv.setLayoutParams(mParams);  
			        iv.setBackgroundResource(pics[i]);
		        	if(i==pics.length-1){
		        		Button btn = new Button(this);
		        		btn.setText("立即体验");
		        		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
		        				RelativeLayout.LayoutParams.WRAP_CONTENT);
		        		rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		        		rl.addRule(RelativeLayout.CENTER_HORIZONTAL);
		        		btn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Editor e = spf.edit();
								e.putBoolean("FIRST", false);
								e.commit();
								viewPager.setVisibility(View.GONE);
								ll.setVisibility(View.GONE);
								content.setVisibility(View.VISIBLE);
							}
						});
		        		iv.addView(btn,rl);
		        	} 
		            views.add(iv);  
		        }  
	       vpAdapter = new ViewPageAdapter(views);  
	       viewPager.setAdapter(vpAdapter);  
	       viewPager.setOnPageChangeListener(this);   
	       initDots();  
    	}else{
    		findViewById(R.id.ll).setVisibility(View.GONE);
    		content.setVisibility(View.VISIBLE);
    	}
		findViewById(R.id.log).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sessionIntent = new Intent(JzVDIHomeActivity.this, SessionActivity.class);							
				startActivity(sessionIntent);
				 
			}
		});
        findViewById(R.id.aboutvdi).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent= new Intent(JzVDIHomeActivity.this, AboutActivity.class);
				startActivity(intent);
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
		//inflater.inflate(R.menu.home_menu, menu);
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
		if(arg0 == 2){
			SharedPreferences spf = getSharedPreferences("FIRST",Context.MODE_MULTI_PROCESS);
			Editor e = spf.edit();
			e.putBoolean("FIRST", false);
			e.commit();
			viewPager.setVisibility(View.GONE);
			ll.setVisibility(View.GONE);
			content.setVisibility(View.VISIBLE);
		}
		
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
		 
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	   jzVDIHomeActivity = null;
		super.onDestroy();
	}
	
	
	
}
