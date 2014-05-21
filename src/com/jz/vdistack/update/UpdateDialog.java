package com.jz.vdistack.update;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jz.vdistack.R;

 

public class UpdateDialog extends AlertDialog implements android.view.View.OnClickListener{
    private Button btnsue;
    private Button btncancel;
    
	public UpdateDialog(Context context){
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.update_dialog,null);
		btnsue =(Button)view.findViewById(R.id.sue);
		btncancel =(Button)view.findViewById(R.id.cancel);
		btncancel.setOnClickListener(this);
		btnsue.setOnClickListener(this);
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setCanceledOnTouchOutside(false);
		setView(view);
		setTitle("新的版本更新");
	}
	
	
	public UpdateDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		View view = LayoutInflater.from(context).inflate(R.layout.update_dialog,null);
		setView(view);
		setTitle("新的版本更新");
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		Window window = this.getWindow();
		window.setWindowAnimations(R.style.dialogsty1);
		super.show();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		boolean bool = false;
		if(dialogOnclick != null){
			bool = dialogOnclick.updateDialogOnclick(v);
		}
		if(!bool){
			updateDialogOnclick.updateDialogOnclick(v);
		}
	}
	
	private UpdateDialogOnclick updateDialogOnclick = new UpdateDialogOnclick() {
		
		@Override
		public boolean updateDialogOnclick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.cancel){
				dismiss();
			}else if(R.id.sue==v.getId()){
				Window window = getWindow();
				window.setWindowAnimations(R.style.dialogsty2);
				dismiss();
			}
			return false;
		}
	};
	
	private UpdateDialogOnclick dialogOnclick;
	
	public void setUpdateDialogOnclick(UpdateDialogOnclick updateDialogOnclick){
		this.dialogOnclick= updateDialogOnclick;
	}

    public interface UpdateDialogOnclick{
    	public boolean updateDialogOnclick(View v);
    }
	
	 
}
