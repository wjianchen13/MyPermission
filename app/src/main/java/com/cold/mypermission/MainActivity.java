package com.cold.mypermission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnNormal = null;
    private Button btnAsp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtils.init(this);
        btnNormal = (Button)findViewById(R.id.btn_normal);
        btnAsp = (Button)findViewById(R.id.btn_aspectj);
        btnNormal.setOnClickListener(clickListener);
        btnAsp.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_normal:
                    gotoNoramlActivity();
                    break;
                case R.id.btn_aspectj:
                    gotoAspectjActivity();
                    break;
            }
        }
    };

    private void gotoNoramlActivity() {
        Intent it = new Intent();
        it.setClass(MainActivity.this, NormalActivity.class);
        startActivity(it);
    }

    private void gotoAspectjActivity() {
        Intent it = new Intent();
        it.setClass(MainActivity.this, AspectjActivity.class);
        startActivity(it);
    }
}
