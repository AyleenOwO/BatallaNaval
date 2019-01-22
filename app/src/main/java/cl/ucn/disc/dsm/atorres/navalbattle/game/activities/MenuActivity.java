package cl.ucn.disc.dsm.atorres.navalbattle.game.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.bluetoothchat.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity del menu principal
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

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
                break;

            case R.id.btn_rules:
                //TODO layout reglas
                intent = new Intent(this, Rules.class);
                //startActivity(intent);
                break;
        }
    }
}