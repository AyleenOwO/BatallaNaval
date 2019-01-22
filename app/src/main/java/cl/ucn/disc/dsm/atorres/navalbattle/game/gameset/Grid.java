package cl.ucn.disc.dsm.atorres.navalbattle.game.gameset;

/**
 * Clase que reperesenta una casilla del tablero
 */
public class Grid {

    /**
     * Jugador al que pertenece la casilla
     */
    private int playerNum;

    /**
     * Estado de la casilla
     */
    private Status status;

    /**
     * Constructor de la clase
     *
     * @param playerNum
     * @param status
     */
    public Grid(int playerNum, Status status) {
        this.playerNum = playerNum;
        this.status = status;
    }

    /**
     * @return a que jugador pertenece la casilla
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * @return el estado de la casilla
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Cambia el estado de la casilla
     *
     * @param status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Estado de la casilla
     */
    public enum Status {
        VACANT, OCCUPIED, HIT, MISSED
    }
}