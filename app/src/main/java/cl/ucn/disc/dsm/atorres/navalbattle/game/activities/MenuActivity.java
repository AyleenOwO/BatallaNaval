package cl.ucn.disc.dsm.atorres.navalbattle.game.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import cl.ucn.disc.dsm.atorres.navalbattle.R;
import cl.ucn.disc.dsm.atorres.navalbattle.bluetoothconnection.DeviceListActivity;
import cl.ucn.disc.dsm.atorres.navalbattle.bluetoothconnection.MultiplayerActivity;

/**
 * Activity del menu principal
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    /**
     *  Botones del menu
     */
    Button btnOne ,btnTwo, btnThree;

    /**
     * OnCreate del Menu
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        btnOne = findViewById(R.id.btn_vs_cpu);
        btnOne.setOnClickListener(this);
        btnTwo = findViewById(R.id.btn_vs_player);
        btnTwo.setOnClickListener(this);
        btnThree = findViewById(R.id.btn_rules);
        btnThree.setOnClickListener(this);
    }

    /**
     * Onclick del menu
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_vs_cpu:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_vs_player:
                //TODO coneccion por bluetooth
                intent = new Intent(this, MultiplayerActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_rules:
                //TODO layout reglas
                intent = new Intent(this, Rules.class);
                //startActivity(intent);
                break;
        }
    }
}