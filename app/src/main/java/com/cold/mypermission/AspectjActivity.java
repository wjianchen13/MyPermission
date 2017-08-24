package com.cold.mypermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * name:AspectjActivity
 * desc: 使用Aspectj方式
 * author:
 * date: 2017-08-22 19:30
 * remark:
 */
public class AspectjActivity extends AppCompatActivity {

    private Button btnTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspectj);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_test:
                    checkPermission();
                    break;

            }
        }
    };

    @NeedPermission(permissions = {Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS})
    private void checkPermission() {
        Toast.makeText(AspectjActivity.this, "权限请求成功", Toast.LENGTH_SHORT).show();
    }
}
