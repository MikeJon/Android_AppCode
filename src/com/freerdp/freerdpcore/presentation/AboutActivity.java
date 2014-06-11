/*
   Activity showing information about aFreeRDP and FreeRDP build information

   Copyright 2013 Thinstuff Technologies GmbH, Author: Martin Fleisz

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.presentation;

 


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.jz.vdistack.R;
import com.jz.vdistack.fragment.ViewPageAdapter;
import com.jz.vdistack.fragment.WcViewPage;

public class AboutActivity extends Activity implements OnPageChangeListener{
	
	private WcViewPage wcViewPage;
	 private ViewPageAdapter vpAdapter;  
     private List<View> views;
	private WebView webView; 
	private boolean is = false;
	//记录当前选中位置  
    private int currentIndex;  
	 private int index = 0;
	 //底部小店图片
     private ImageView[] dots ;
     private LinearLayout ll;
	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		wcViewPage = (WcViewPage)findViewById(R.id.viewpager);
		wcViewPage.setOnPageChangeListener(this);
		views = new ArrayList<View>();
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageResource(R.drawable.leadpage1);
		views.add(imageView);
		ImageView imageView2 = new ImageView(this);
		imageView2.setScaleType(ScaleType.FIT_XY);
		imageView2.setImageResource(R.drawable.leadpage2);
		views.add(imageView2);
	    webView = new WebView(this);
		WebSettings settings = webView.getSettings();
		settings.setPluginsEnabled(true);
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setDefaultTextEncodingName("GBK");
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
	 
		webView.setBackgroundColor(0);
		webView.loadUrl("file:///android_asset/about_page/shiyidonhua.swf");
		views.add(webView);
		vpAdapter = new ViewPageAdapter(views);  
	    wcViewPage.setAdapter(vpAdapter);
	    initDots();  
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
		if(arg0==2)webView.loadUrl("file:///android_asset/about_page/shiyidonhua.swf");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 
		super.onDestroy();
	}
	
	private void initDots() {  
        ll = (LinearLayout) findViewById(R.id.ll);  
        dots = new ImageView[JzVDIHomeActivity.pics.length];  
        //循环取得小点图片  
        for (int i = 0; i < JzVDIHomeActivity.pics.length; i++) {  
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
		    if (positon < 0 || positon > JzVDIHomeActivity.pics.length - 1 || currentIndex == positon) 
		        return;  
		    dots[positon].setEnabled(false);  
		    dots[currentIndex].setEnabled(true);  
		    currentIndex = positon;  
		}

	
	
}
