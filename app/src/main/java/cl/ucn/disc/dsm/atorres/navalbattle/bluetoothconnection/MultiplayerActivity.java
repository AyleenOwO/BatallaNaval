package cl.ucn.disc.dsm.atorres.navalbattle.bluetoothconnection;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import cl.ucn.disc.dsm.atorres.navalbattle.R;

/**
 *
 */
public class MultiplayerActivity extends AppCompatActivity {

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        GameFragment fragment = new GameFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

    }


}
