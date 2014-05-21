package com.jz.vdistack.fragment;

 


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.freerdp.freerdpcore.presentation.JzVDIHomeActivity;
import com.jz.vdistack.R;

public class SetingsFragment extends Fragment implements OnClickListener,AnimationListener{
	View view;
	private View content;
	private View content_0;
	private SceenSettingOpreat sso;
	private View curentview=null;
    private View lastView  =null;
	private String title="";
 
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view= inflater.inflate(R.layout.setingslayout, container, false); 
		content = view.findViewById(R.id.content);
		content_0 =view.findViewById(R.id.content_0);
 
	    view.findViewById(R.id.btn_wifi).setOnClickListener(this);
	    view.findViewById(R.id.btn_3g).setOnClickListener(this);
	    lastView = content_0;
		sso = new SceenSettingOpreat();
		return  view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		 case R.id.btn_3g:
			 title ="3G模式设置";
			 getActivity().getActionBar().setTitle(title);
			 Animation animation4 = AnimationUtils.loadAnimation(getActivity(),R.anim.gradual) ;
			 animation4.setAnimationListener(this);
			 content_0.startAnimation(animation4);
			 curentview = content;
			 JzVDIHomeActivity ja4 =(JzVDIHomeActivity)getActivity();
			 ja4.setIndex(2);
			 content.setVisibility(View.VISIBLE);
			 content.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.showgradual));
			 break;
		 case R.id.btn_wifi:
			 title ="wifi模式设置";
			 getActivity().getActionBar().setTitle(title);
			 Animation animation5 = AnimationUtils.loadAnimation(getActivity(),R.anim.gradual) ;
			 animation5.setAnimationListener(this);
			 content_0.startAnimation(animation5);
			 curentview =content;
			 JzVDIHomeActivity ja5 =(JzVDIHomeActivity)getActivity();
			 ja5.setIndex(2);
			 content.setVisibility(View.VISIBLE);
			 content.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.showgradual));
			 break;
		default:
			break;
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		lastView.setVisibility(lastView.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
		if(curentview !=null){
			curentview.setVisibility(lastView.getVisibility()==View.GONE?View.VISIBLE:View.GONE); 
		}
		 
		JzVDIHomeActivity jd = (JzVDIHomeActivity)getActivity();
		jd.setIstouche(false);
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		JzVDIHomeActivity jd = (JzVDIHomeActivity)getActivity();
		jd.setIstouche(true);
	}
	
	public void setDisplayHome(){
		
		JzVDIHomeActivity jdh = (JzVDIHomeActivity)getActivity();
		int index =jdh.getIndex();
		if(index == 2){
			lastView =content_0;
			curentview =content;
			getActivity().getActionBar().setTitle("设置");
		}else{
			getActivity().getActionBar().setTitle(title);
		}
		jdh.setIndex(index-1);
		Animation animation =AnimationUtils.loadAnimation(getActivity(), R.anim.showgradual);
		animation.setAnimationListener(this);
		lastView.startAnimation(animation);
		if(curentview !=null){
			curentview.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.gradual));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*****
	 * 
	 * @author asus
	 *
	 */
	private class SceenSettingOpreat implements OnClickListener,DialogInterface.OnClickListener{
		
		private int index = -1;
		private TextView textViewsolution;
		private TextView textViewcolor;
		public SceenSettingOpreat(){
			 
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			default:
				break;
			}
		}
		
		private void showSingleChoiceButton( ){
		   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	       builder.setTitle("显示分辨率");
	       
	       builder.setSingleChoiceItems(R.array.resolutions_values_array, 8, this);
	       builder.setNegativeButton("取消",this);
	       builder.show();

	    }
        private void showColorChoice(){
           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
  	       builder.setTitle("色彩");
  	      
  	       builder.setSingleChoiceItems(R.array.colors_array, 2, this);
  	       builder.setNegativeButton("取消",this);
  	       builder.show();
        }
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			CharSequence c[] ;
			if(which >=0){
				switch (index) {
				case 0:
					c=getActivity().getResources().getTextArray(R.array.colors_array);
					textViewcolor.setText(c[which]);
					break;
				case 1:
				    c =getActivity().getResources().getTextArray(R.array.resolutions_values_array);
					textViewsolution.setText(c[which]);
					if(which==1){
						 
					}else{
						 
					}
					break;
				default:
					break;
				}
			}
			
			index = -1;
			dialog.dismiss();
		}
		
	}
	
}
