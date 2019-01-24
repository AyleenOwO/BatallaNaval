package cl.ucn.disc.dsm.atorres.navalbattle.game.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import cl.ucn.disc.dsm.atorres.navalbattle.R;
import cl.ucn.disc.dsm.atorres.navalbattle.game.Game;
import cl.ucn.disc.dsm.atorres.navalbattle.game.adapters.BoardAdapter;

/**
 * Activity del juego
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Mensaje a desplegar por pantalla
     */
    private TextView tvMessage;

    /**
     * Boton de reinicio
     */
    private Button btnRestart, btnConnect;

    /**
     * Tableros
     */
    private GridView gvBoard1, gvBoard2;

    /**
     * Adaptador de los tableros
     */
    private BoardAdapter boardAdapter1, boardAdapter2;


    /**
     * Oncreate del activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setFields();
        enableGame();
    }

    /**
     * Inicia el juego
     */
    private void enableGame() {
        Game game = Game.getInstance();
        game.setFields(this, tvMessage, btnRestart,
                gvBoard1, gvBoard2, boardAdapter1, boardAdapter2);
        game.initialize();
    }

    /**
     * Pasa los parametros de la vista al activity
     */
    private void setFields() {
        tvMessage = findViewById(R.id.tv_message);
        btnRestart = findViewById(R.id.btn_initialize);
        btnConnect =findViewById(R.id.btn_connect);
        gvBoard1 = findViewById(R.id.gv_one);
        gvBoard2 = findViewById(R.id.gv_two);
        boardAdapter1 = new BoardAdapter(this, new ArrayList<>());
        boardAdapter2 = new BoardAdapter(this, new ArrayList<>());

        btnConnect.setVisibility(View.GONE);
    }
}