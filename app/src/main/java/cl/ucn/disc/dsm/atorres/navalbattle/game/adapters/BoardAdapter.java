package cl.ucn.disc.dsm.atorres.navalbattle.game.adapters;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.android.bluetoothchat.R;

import java.util.List;

import androidx.core.content.ContextCompat;
import cl.ucn.disc.dsm.atorres.navalbattle.game.gameset.Grid;

/**
 * Adaptador del tablero de cada jugador
 */
public class BoardAdapter extends ArrayAdapter<Grid> {

    /**
     * Inflador
     */
    private LayoutInflater inflater;

    /**
     * Constructor de la clase
     */
    public BoardAdapter(Context context, List<Grid> objects) {
        super(context, -1, objects);
        inflater = LayoutInflater.from(context);
    }

    /**
     * Define el color de la casilla de cada jugador, segun el jugador
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.grid_view, parent, false);
        Grid grid = getItem(position);

        Button button = view.findViewById(R.id.btn_grid);

        if (grid.getStatus() == Grid.Status.HIT)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorHit));
        else if (grid.getStatus() == Grid.Status.MISSED)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMissed));

        else if (grid.getPlayerNum() == 2)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorUnknown));

        else if (grid.getStatus() == Grid.Status.VACANT)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorVacant));
        else
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorOccupied));

        return view;
    }

    /**
     * Rellena el tablero con casillas disponibles
     */
    public void addGrids(GridView gridView, int playerNum, int numGrids) {
        gridView.setAdapter(this);
        for (int i = 0; i < numGrids; i++)
            this.add(new Grid(playerNum, Grid.Status.VACANT));
    }
}
