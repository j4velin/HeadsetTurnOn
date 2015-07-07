package de.j4velin.headsetturnon;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startService(new Intent(Main.this, ListeningService.class));
                finish();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                stopService(new Intent(Main.this, ListeningService.class));
                finish();
            }
        });
        if (AdminReceiver.isActive(this)) {
            findViewById(R.id.admin).setVisibility(View.GONE);
        } else {
            findViewById(R.id.admin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            new ComponentName(Main.this, AdminReceiver.class));
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Required to turn the display off");
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (AdminReceiver.isActive(this)) {
                findViewById(R.id.admin).setVisibility(View.GONE);
            }
        }
    }
}
