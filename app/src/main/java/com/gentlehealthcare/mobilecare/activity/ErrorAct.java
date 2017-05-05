package com.gentlehealthcare.mobilecare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.net.LinstenNetState;

/**
 * Created by Zyy on 2016/4/21.
 * 类说明：
 */
public class ErrorAct extends  BaseActivity {

   private Button btnTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_error);
        setBtnTry();
    }

    private void setBtnTry(){
        btnTry= (Button) findViewById(R.id.btn_try);
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LinstenNetState.isLinkState(getApplicationContext())) {
                    Intent intent=new Intent();
                    intent.setClass(ErrorAct.this, HomeAct.class);
                    startActivity(intent);
                }else{
                    ShowToast(getString(R.string.unstable));
                }
            }
        });
    }

    @Override
    protected void resetLayout() {

    }

}
