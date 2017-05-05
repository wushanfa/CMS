package com.gentlehealthcare.mobilecare.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;

/**
 * Created by ouyang on 15/5/21.
 */
public class CustomEditTextDialog extends Dialog {
    private LinearLayout ll_custom_edittext1,ll_custom_edittext2;
    private TextView tv_title1,tv_title2;
    private EditText et_conent1,et_conent2;
    private Button btnLeft, btnRight;
    private Context context;
    private TextView tvTitle;


    public CustomEditTextDialog(Context context) {
        super(context);
        this.context=context;
        init();

    }

    public CustomEditTextDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
       init();
    }


    private void init(){
        setCancelable(false);
        View rootView= LayoutInflater.from(this.context).inflate(R.layout.dialog_edittext_custom,null);
        setContentView(rootView);
        ll_custom_edittext1= (LinearLayout) rootView.findViewById(R.id.ll_custom_edittext1);
        ll_custom_edittext2= (LinearLayout) rootView.findViewById(R.id.ll_custom_edittext2);
        tv_title1= (TextView) rootView.findViewById(R.id.tv_title1);
        tv_title2= (TextView) rootView.findViewById(R.id.tv_title2);
        et_conent1= (EditText) rootView.findViewById(R.id.et_conent1);
        et_conent2=(EditText)rootView.findViewById(R.id.et_conent2);
        btnLeft = (Button) rootView.findViewById(R.id.btn_left);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        btnRight = (Button) rootView.findViewById(R.id.btn_right);



    }

    public void setContent(String[] titles){
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int)(320*d.density); // 高度设置为屏幕的0.6

     if (titles.length==1){
         ll_custom_edittext2.setVisibility(View.GONE);
         tv_title1.setText(titles[0]);
         lp.height=(int)(160*d.density);
     }else if (titles.length==2){
         tv_title1.setText(titles[0]);
         tv_title2.setText(titles[1]);
         lp.height=(int)(210*d.density);
     }
        dialogWindow.setAttributes(lp);
    }


    public void setLeftButton(String button,View.OnClickListener clickListener) {

        btnLeft.setText(button);
        btnLeft.setOnClickListener(clickListener);
    }

    public void setRightButton(String button,View.OnClickListener clickListener) {

        btnRight.setText(button);
        btnRight.setOnClickListener(clickListener);
    }


    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public String[] getText(){
        String text1=et_conent1.getText().toString()==null?"":et_conent1.getText().toString();
        String text2=et_conent2.getText().toString()==null?"":et_conent2.getText().toString();
        return new String[]{text1,text2};
    }



}
