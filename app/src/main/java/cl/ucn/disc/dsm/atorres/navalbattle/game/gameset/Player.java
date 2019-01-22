package cl.ucn.disc.dsm.atorres.navalbattle.game.gameset;

import java.util.ArrayList;

/**
 *Clase que representa al jugador
 */
public class Player {

    /**
     * Numero de barcos posicionados
     */
    private int numShipsArranged = 0;

    /**
     * numero de ataques permitidos
     */
    private int AttacksAllowed = 1;

    /**
     * numero de barcos del jugador
     */
    private int numShips = 3;

    /**
     * numero del jugador
     */
    private int playerNum;

    /**
     * lista de barcos pertenecientes al jugador
     */
    private ArrayList<Ship> ships = new ArrayList<>(numShips);

    /**
     * Constructor del jugador
     * @param playerNum
     */
    public Player(int playerNum) {
        this.playerNum = playerNum;

        Ship ship = new Ship(Ship.ShipType.LITTLE_GUY);
        ships.add(ship);
        ship = new Ship(Ship.ShipType.MIDDLE_MAN);
        ships.add(ship);
        ship = new Ship(Ship.ShipType.BIG_BOY);
        ships.add(ship);
    }

    /**
     *
     * @return numero de barcos posicionados
     */
    public int getNumShipsArranged() {
        return numShipsArranged;
    }

    /**
     *
     * @return numero de barcos del jugador
     */
    public int getNumShips() {
        return numShips;
    }

    /**
     *
     * @return lista de barcos
     */
    public ArrayList<Ship> getShips() {
        return ships;
    }

    /**
     *
     * @return true si es que puede añadir casillas
     */
    public boolean canAddGrid() {
        return (numShips - numShipsArranged) > 0;
    }

    /**
     * Añade una casilla al barco que se esta posicionando
     */
    public void addGrid(Grid grid) {
        Ship ship = ships.get(numShipsArranged);

        ship.addGrid(grid);
        if (!ship.canAddGrids())
            numShipsArranged++;
    }

    /**
     *
     * @return true si el jugador puede atacar
     */
    public boolean canAttack() {
        return AttacksAllowed > 0;
    }

    /**
     * ataca una casilla del tablero enemigo
     * @param grid casilla a atacar
     */
    public void attackGrid(Grid grid) {
        if (grid.getStatus() == Grid.Status.VACANT){
            grid.setStatus(Grid.Status.MISSED);
            AttacksAllowed--;
        }
        if (grid.getStatus() == Grid.Status.OCCUPIED)
            grid.setStatus(Grid.Status.HIT);
    }

    /**
     * Restaura los ataques del jugador
     */
    public void resetAttacks() {
        AttacksAllowed++;
    }

    /**
     *
     * @return true si el jugador aun no pierde
     */
    public boolean isAlive() {
        int numShipsAlive = 0;
        for (Ship ship : ships)
            if (ship.isAlive())
                numShipsAlive++;
        return numShipsAlive > 0;
    }
}
