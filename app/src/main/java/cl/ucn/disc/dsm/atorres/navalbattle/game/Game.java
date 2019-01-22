package cl.ucn.disc.dsm.atorres.navalbattle.game;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Random;

import cl.ucn.disc.dsm.atorres.navalbattle.game.adapters.BoardAdapter;
import cl.ucn.disc.dsm.atorres.navalbattle.game.gameset.Grid;
import cl.ucn.disc.dsm.atorres.navalbattle.game.gameset.MathModel;
import cl.ucn.disc.dsm.atorres.navalbattle.game.gameset.Player;
import cl.ucn.disc.dsm.atorres.navalbattle.game.gameset.ShipArrangement;

/**
 * Clase que representa al juego
 */
public class Game {

    /**
     * instancia del la clase
     */
    private static Game instance;

    /**
     * contexto de la activity
     */
    private Context context;

    /**
     * numero de casillas por lado
     */
    private int numGridsside;

    /**
     * Mensaje a desplegar por pantalla
     */
    private TextView tvMessage;

    /**
     * Boton de reinicio
     */
    private Button btnRestart;

    /**
     * Tableros
     */
    private GridView gvBoard1, gvBoard2;

    /**
     * Adaptador de los tableros
     */
    private BoardAdapter boardAdapter1, boardAdapter2;

    /**
     * Jugadores
     */
    private Player player1, player2;

    /**
     * Constructor de la clase
     */
    private Game() {
    }

    /**
     * Patron Singleton
     *
     * @return instancia unica
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    /**
     * Pasa las variables del MainActivity
     */
    public void setFields(Context context, TextView textViewMessage,
                          Button buttonRestart, GridView gridViewBoard1, GridView gridViewBoard2,
                          BoardAdapter boardAdapter1, BoardAdapter boardAdapter2) {
        this.context = context;
        this.numGridsside = 7;
        this.tvMessage = textViewMessage;
        this.btnRestart = buttonRestart;
        this.gvBoard1 = gridViewBoard1;
        this.gvBoard2 = gridViewBoard2;
        this.boardAdapter1 = boardAdapter1;
        this.boardAdapter2 = boardAdapter2;
    }

    /**
     * [re]inicia lo basico del juego
     */
    public void initialize() {
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
            }
        });
        disableClicking();
        setMessage("Posiciona tus barcos tocando tu territorio");

        boardAdapter1.clear();
        boardAdapter2.clear();
        boardAdapter1.addGrids(gvBoard1, 1, getNumGridsBoardArea());
        boardAdapter2.addGrids(gvBoard2, 2, getNumGridsBoardArea());

        player1 = new Player(1);
        player2 = new Player(2);

        letP2arrange();
    }

    /**
     * La CPU prepara su tablero
     */
    private void letP2arrange() {
        MathModel.generateShipPlacement(player2, boardAdapter2, numGridsside); // Zach's code
        letP1arrange();
    }

    /**
     * El jugador prepara su tablero
     */
    private void letP1arrange() {
        gvBoard1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grid grid = (Grid) parent.getAdapter().getItem(position);
                player1.addGrid(grid);
                boardAdapter1.notifyDataSetChanged();

                if (player1.canAddGrid())
                    setMessage(player1.getShips().get(player1.getNumShipsArranged()).getNumGridsLeft() +
                            " casilla(s) quedan para agregar a tu barco " +
                            (player1.getNumShipsArranged() + 1));
                else {
                    gvBoard1.setOnItemClickListener(null);

                    if (checkArrange())
                        letP1attack();
                    else
                        setMessage("Posicionamiento invalido, click REINICIAR");
                }
            }
        });
    }

    /**
     * Verifica si la posicion de los barcos tienen un largo valido
     *
     * @return
     */
    private boolean checkArrange() {
        ShipArrangement shipArr = new ShipArrangement();
        BoardAdapter boardAdapter = boardAdapter1;
        int c = 0;
        for (int i = 0; i < boardAdapter.getCount(); i++) {
            Grid grid = boardAdapter1.getItem(i);
            if (grid.getStatus() == Grid.Status.OCCUPIED) {
                c = c + 1;
                if (c == 10) {
                    if (((shipArr.checkArrangeLH(boardAdapter)) || (shipArr.checkArrangeLV(boardAdapter)))) {
                        if (((shipArr.checkArrangeMH(boardAdapter)) || (shipArr.checkArrangeMV(boardAdapter)))) {
                            if (((shipArr.checkArrangeSH(boardAdapter)) || (shipArr.checkArrangeSV(boardAdapter)))) {
                                return true;
                            }
                        }
                    }

                }
            }
        }
        return false;
    }

    /**
     * El jugador ataca
     */
    private void letP1attack() {
        btnRestart.setVisibility(View.GONE);
        setMessage(("¡TU turno!"));
        gvBoard2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grid grid = (Grid) parent.getAdapter().getItem(position);
                player1.attackGrid(grid);
                boardAdapter2.notifyDataSetChanged();

                if (!player2.isAlive()) {
                    disableClicking();
                    btnRestart.setVisibility(View.VISIBLE);
                    setMessage("¡GANASTE! apreta reinciar");
                } else if (player1.canAttack()) {
                    setMessage(("¡TU turno!"));
                } else {
                    gvBoard2.setOnItemClickListener(null);
                    player1.resetAttacks();
                    letP2attack();
                }
            }
        });
    }

    /**
     * Metodo en el que la CPU ataca
     */
    private void letP2attack() {
        Random random = new Random();
        while (player2.canAttack()) {
            Grid grid;
            do {
                grid = boardAdapter1.getItem(random.nextInt(getNumGridsBoardArea()));
            }
            while (grid.getStatus() == Grid.Status.HIT || grid.getStatus() == Grid.Status.MISSED);
            setMessage(("Turno del oponente"));
            player2.attackGrid(grid);
            boardAdapter1.notifyDataSetChanged();

            if (!player1.isAlive()) {
                disableClicking();
                btnRestart.setVisibility(View.VISIBLE);
                setMessage("perdiste, apreta reiniciar");
                break;
            }
        }
        if (player1.isAlive()) {
            player2.resetAttacks();
            letP1attack();
        }
    }

    /**
     * @param msg mensaje a mostrar en pantalla
     */
    private void setMessage(String msg) {
        tvMessage.setText(msg);
    }

    /**
     * desactiva lo tactil de los tableros
     */
    private void disableClicking() {
        gvBoard1.setOnItemClickListener(null);
        gvBoard2.setOnItemClickListener(null);
    }

    /**
     * @return area del tablero
     */
    private int getNumGridsBoardArea() {
        return (int) Math.pow(numGridsside, 2);
    }
}