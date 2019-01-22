package cl.ucn.disc.dsm.atorres.navalbattle.game.gameset;

import java.util.ArrayList;

/**
 * Clase que representa a un barco en el tablero
 */
public class Ship {

    /**
     * Numero de casillas que han sido agregadas
     */
    private int numGridsAdded = 0;

    /**
     * Tipo de barco
     */
    private ShipType shipType;

    /**
     * numero de casillas
     */
    private int numGrids;

    /**
     * lista de casillas en donde se encuentra el barco
     */
    private ArrayList<Grid> grids;

    /**
     * Constructor, construye el barco segun su tipo
     */
    public Ship(ShipType shipType) {
        this.shipType = shipType;
        setShipType();
        grids = new ArrayList<>(numGrids);
    }

    /**
     * @return numero de casillas
     */
    public int getNumGrids() {
        return numGrids;
    }

    /**
     * @return numero de casillas
     */
    public int getNumGridsLeft() {
        return numGrids - numGridsAdded;
    }

    /**
     * Asigna el numero de casillas segun el tipo de barco
     */
    private void setShipType() {
        if (shipType == ShipType.LITTLE_GUY) {
            numGrids = 2;
        } else if (shipType == ShipType.MIDDLE_MAN) {
            numGrids = 3;
        } else {
            numGrids = 5;
        }
    }

    /**
     * @return true si aun puede añadir casillas
     */
    public boolean canAddGrids() {
        return (numGrids - numGridsAdded) > 0;
    }

    /**
     * @param grid casilla a agregar
     */
    public void addGrid(Grid grid) {
        grid.setStatus(Grid.Status.OCCUPIED);
        grids.add(grid);
        numGridsAdded++;
    }

    /**
     * @return false si es que han atinado a todas las casiillas del barco
     */
    public boolean isAlive() {
        for (Grid grid : grids)
            if (grid.getStatus() != Grid.Status.HIT)
                return true;
        return false;
    }

    /**
     * Tipo de barco
     */
    public enum ShipType {
        LITTLE_GUY, MIDDLE_MAN, BIG_BOY
    }
}

