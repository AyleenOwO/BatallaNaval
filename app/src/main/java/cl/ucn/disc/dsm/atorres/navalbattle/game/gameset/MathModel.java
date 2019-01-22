package cl.ucn.disc.dsm.atorres.navalbattle.game.gameset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cl.ucn.disc.dsm.atorres.navalbattle.game.adapters.BoardAdapter;

/**
 * Clase que llena un tablero
 */
public class MathModel {

    /**
     *Jugador al que se le llenara el tablero
     */
    private static Player player;

    /**
     * tablero
     */
    private static BoardAdapter boardAdapter;

    /**
     *
     */
    private static int x, y, cols;

    /**
     *Variable random
     */
    private static Random random = new Random();

    /**
     * Constructor de la clase
     */
    private MathModel() {
        x = 0;
        y = 0;
        cols = 1;
    }

    /**
     *
     * @return
     */
    private static int getPosFromXY() {
        return x * cols + y;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static int getPosFromXY(int x, int y) {
        return x * cols + y;
    }

    /**
     * Encuentra una casilla aleatoria de donde empezar
     */
    private static void getEmptyCell() {
        do {
            x = random.nextInt(cols);
            y = random.nextInt(cols);
        }
        while (boardAdapter.getItem(getPosFromXY()).getStatus() == Grid.Status.OCCUPIED);
    }

    /**
     *
     * @param size
     * @return
     */
    private static boolean isNorthValid(int size) {
        if (y - size < 0)
            return false;

        int source = getPosFromXY(x, y);
        for (int i = 0; i < size; i++)
            if (boardAdapter.getItem(source - i).getStatus() == Grid.Status.OCCUPIED)
                return false;

        return true;
    }

    /**
     *
     * @param size
     */
    private static void setNorthPlacement(int size) {
        for (int i = 0; i < size; i++)
            player.addGrid(boardAdapter.getItem(getPosFromXY(x, y - i)));
    }

    /**
     *
     * @param size
     * @return
     */
    private static boolean isEastValid(int size) {
        if (x + size > cols)
            return false;

        for (int i = 0; i < size; i++)
            if (boardAdapter.getItem(getPosFromXY(x + i, y)).getStatus() == Grid.Status.OCCUPIED)
                return false;

        return true;
    }

    /**
     *
     * @param size
     */
    private static void setEastPlacement(int size) {
        for (int i = 0; i < size; i++)
            player.addGrid(boardAdapter.getItem(getPosFromXY(x + i, y)));
    }

    /**
     *
     * @param size
     * @return
     */
    private static boolean isSouthValid(int size) {
        if (y + size > cols)
            return false;

        for (int i = 0; i < size; i++)
            if (boardAdapter.getItem(getPosFromXY(x, y + i)).getStatus() == Grid.Status.OCCUPIED)
                return false;

        return true;
    }

    /**
     *
     * @param size
     */
    private static void setSouthPlacement(int size) {
        for (int i = 0; i < size; i++)
            player.addGrid(boardAdapter.getItem(getPosFromXY(x, y + i)));
    }

    /**
     *
     * @param size
     * @return
     */
    private static boolean isWestValid(int size) {
        if (x - size < 0)
            return false;

        for (int i = 0; i < size; i++) {
            int nextX = x - i;
            if (nextX < 0 || boardAdapter.getItem(getPosFromXY(nextX, y)).getStatus() == Grid.Status.OCCUPIED)
                return false;
        }

        return true;
    }

    /**
     *
     * @param size
     */
    private static void setWestPlacement(int size) {
        for (int i = 0; i < size; i++)
            player.addGrid(boardAdapter.getItem(getPosFromXY(x - i, y)));
    }

    //performs random sampling

    /**
     *
     * @param size
     */
    private static void setPlacement(int size) {
        boolean notPlaced = true;

        while (notPlaced) {
            List<Integer> sample = new ArrayList<>(4);
            sample.add(0);
            sample.add(1);
            sample.add(2);
            sample.add(3);

            getEmptyCell();

            while (!sample.isEmpty()) {
                int i = random.nextInt(sample.size());
                switch (sample.get(i)) {
                    case 0:
                        if (isNorthValid(size)) {
                            setNorthPlacement(size);
                            notPlaced = false;
                        } else
                            sample.remove(i);
                        break;
                    case 1:
                        if (isEastValid(size)) {
                            setEastPlacement(size);
                            notPlaced = false;
                        } else
                            sample.remove(i);
                        break;
                    case 2:
                        if (isSouthValid(size)) {
                            setSouthPlacement(size);
                            notPlaced = false;
                        } else
                            sample.remove(i);
                        break;
                    case 3:
                        if (isWestValid(size)) {
                            setWestPlacement(size);
                            notPlaced = false;
                        } else
                            sample.remove(i);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //the main public function. this places ships in available squares.

    /**
     *
     * @param thePlayer
     * @param boardAdapter
     * @param dim
     */
    public static void generateShipPlacement(Player thePlayer, BoardAdapter boardAdapter, int dim) {
        player = thePlayer;

        MathModel.boardAdapter = boardAdapter;
        cols = dim;

        setPlacement(2);
        setPlacement(3);
        setPlacement(5);

        MathModel.boardAdapter.notifyDataSetChanged();
    }

}
