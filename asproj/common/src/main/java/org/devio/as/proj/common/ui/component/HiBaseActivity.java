package org.devio.as.proj.common.ui.component;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.devio.as.proj.common.R;

public class HiBaseActivity extends AppCompatActivity implements HiBaseActionInterface {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) return;
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}
