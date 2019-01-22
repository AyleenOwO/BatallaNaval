package cl.ucn.disc.dsm.atorres.navalbattle.game.gameset;

import cl.ucn.disc.dsm.atorres.navalbattle.game.adapters.BoardAdapter;

/**
 * Clase que verifica si la posicion de los barcos es valida
 */
public class ShipArrangement {
    /**
     *
     */
    Grid checkL1, checkL2, checkL3, checkL4, checkL5, checkM1, checkM2, checkM3;

    /**
     * Verifica si un barco(Grande) tiene una posicion horizontal valida
     * @param board tablero donde se encuentran los barcos
     * @return true si es valido
     */
    public boolean checkArrangeLH(BoardAdapter board) {
        int boardSize = (int) Math.sqrt(board.getCount());
        for (int i = 0; i < board.getCount() - 4; i++) {
            Grid grid = board.getItem(i);
            if (grid.getStatus() == Grid.Status.OCCUPIED) {
                if (((i + 1) % boardSize == boardSize - 1) ||
                        ((i + 2) % boardSize == boardSize - 1) ||
                        ((i + 3) % boardSize == boardSize - 1)) {
                } else {
                    Grid grid2 = board.getItem(i + 1);
                    if (grid2.getStatus() == Grid.Status.OCCUPIED) {
                        Grid grid3 = board.getItem(i + 2);
                        if (grid3.getStatus() == Grid.Status.OCCUPIED) {
                            Grid grid4 = board.getItem(i + 3);
                            if (grid4.getStatus() == Grid.Status.OCCUPIED) {
                                Grid grid5 = board.getItem(i + 4);
                                if (grid5.getStatus() == Grid.Status.OCCUPIED) {
                                    checkL1 = board.getItem(i);
                                    checkL2 = board.getItem(i + 1);
                                    checkL3 = board.getItem(i + 2);
                                    checkL4 = board.getItem(i + 3);
                                    checkL5 = board.getItem(i + 4);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Verifica si un barco(Grande) tiene una posicion vertical valida
     * @param board tablero donde se encuentran los barcos
     * @return true si es valido
     */
    public boolean checkArrangeLV(BoardAdapter board) {
        int boardSize = (int) Math.sqrt(board.getCount());
        for (int i = 0; i < (board.getCount() - (boardSize * 4)); i++) {
            Grid grid = board.getItem(i);
            if (grid.getStatus() == Grid.Status.OCCUPIED) {
                Grid grid2 = board.getItem(i + boardSize);
                if (grid2.getStatus() == Grid.Status.OCCUPIED) {
                    Grid grid3 = board.getItem(i + boardSize * 2);
                    if (grid3.getStatus() == Grid.Status.OCCUPIED) {
                        Grid grid4 = board.getItem(i + boardSize * 3);
                        if (grid4.getStatus() == Grid.Status.OCCUPIED) {
                            Grid grid5 = board.getItem(i + boardSize * 4);
                            if (grid5.getStatus() == Grid.Status.OCCUPIED) {
                                checkL1 = board.getItem(i);
                                checkL2 = board.getItem(i + boardSize);
                                checkL3 = board.getItem(i + boardSize * 2);
                                checkL4 = board.getItem(i + boardSize * 3);
                                checkL5 = board.getItem(i + boardSize * 4);
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
     * Verifica si un barco(Mediano) tiene una posicion horizontal valida
     * @param board tablero donde se encuentran los barcos
     * @return true si es valido
     */
    public boolean checkArrangeMH(BoardAdapter board) {
        int boardSize = (int) Math.sqrt(board.getCount());
        for (int i = 0; i < board.getCount() - 2; i++) {
            Grid grid = board.getItem(i);
            if (grid.getStatus() == Grid.Status.OCCUPIED) {
                if ((board.getItem(i) != checkL1) && (board.getItem(i) != checkL2) && (board.getItem(i) != checkL3) && (board.getItem(i) != checkL4) && (board.getItem(i) != checkL5)) {
                    if (((i + 1) % boardSize == boardSize - 1) ||
                            ((i + 2) % boardSize == boardSize - 1)) {
                        //loop continues
                    } else {
                        Grid grid2 = board.getItem(i + 1);
                        if (grid2.getStatus() == Grid.Status.OCCUPIED) {
                            Grid grid3 = board.getItem(i + 2);
                            if (grid3.getStatus() == Grid.Status.OCCUPIED) {
                                if ((grid3 != checkL1) && (grid3 != checkL2) && (grid3 != checkL3)
                                        && (grid3 != checkL4) && (grid3 != checkL5)) {
                                    checkM1 = board.getItem(i);
                                    checkM2 = board.getItem(i + 1);
                                    checkM3 = board.getItem(i + 2);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * Verifica si un barco(Mediano) tiene una posicion vertical valida
     * @param board tablero donde se encuentran los barcos
     * @return true si es valido
     */
    public boolean checkArrangeMV(BoardAdapter board) {
        int boardSize = (int) Math.sqrt(board.getCount());
        for (int i = 0; i < (board.getCount() - (boardSize * 2)); i++) {
            Grid grid = board.getItem(i);
            if (grid.getStatus() == Grid.Status.OCCUPIED) {
                if ((board.getItem(i) != checkL1) && (board.getItem(i) != checkL2) && (board.getItem(i) != checkL3) && (board.getItem(i) != checkL4) && (board.getItem(i) != checkL5)) {
                    Grid grid2 = board.getItem(i + boardSize);
                    if (grid2.getStatus() == Grid.Status.OCCUPIED) {
                        Grid grid3 = board.getItem(i + boardSize * 2);
                        if (grid3.getStatus() == Grid.Status.OCCUPIED) {
                            if ((grid3 != checkL1) && (grid3 != checkL2) && (grid3 != checkL3)
                                    && (grid3 != checkL4) && (grid3 != checkL5)) {
                                checkM1 = board.getItem(i);
                                checkM2 = board.getItem(i + boardSize);
                                checkM3 = board.getItem(i + boardSize * 2);
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
     * Verifica si un barco(pequeño) tiene una posicion horizontal valida
     * @param board tablero donde se encuentran los barcos
     * @return true si es valido
     */
    public boolean checkArrangeSH(BoardAdapter board) {
        int boardSize = (int) Math.sqrt(board.getCount());
        for (int i = 0; i < board.getCount(); i++) {
            Grid grid = board.getItem(i);
            if (grid.getStatus() == Grid.Status.OCCUPIED) {
                if ((board.getItem(i) != checkM1) && (board.getItem(i) != checkM2) && (board.getItem(i) != checkM3)
                        && ((board.getItem(i) != checkL1) && (board.getItem(i) != checkL2) && (board.getItem(i) != checkL3) && (board.getItem(i) != checkL4) && (board.getItem(i) != checkL5))) {
                    if ((i + 1) % boardSize == boardSize - 1) {
                        //loop continues
                    } else {
                        Grid grid2 = board.getItem(i + 1);
                        if (grid2.getStatus() == Grid.Status.OCCUPIED) {
                            if ((grid2 != checkM1) && (grid2 != checkM2) && (grid2 != checkM3)
                                    && (grid2 != checkL1) && (grid2 != checkL2) && (grid2 != checkL3)
                                    && (grid2 != checkL4) && (grid2 != checkL5)) {
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
     * Verifica si un barco(pequeño) tiene una posicion vertical valida
     * @param board tablero donde se encuentran los barcos
     * @return true si es valido
     */
    public boolean checkArrangeSV(BoardAdapter board) {
        int boardSize = (int) Math.sqrt(board.getCount());
        for (int i = 0; i < (board.getCount() - boardSize); i++) {
            Grid grid = board.getItem(i);
            if (grid.getStatus() == Grid.Status.OCCUPIED) {
                if ((board.getItem(i) != checkM1) && (board.getItem(i) != checkM2) && (board.getItem(i) != checkM3)
                        && ((board.getItem(i) != checkL1) && (board.getItem(i) != checkL2) && (board.getItem(i) != checkL3) && (board.getItem(i) != checkL4) && (board.getItem(i) != checkL5))) {
                    Grid grid2 = board.getItem(i + boardSize);
                    if (grid2.getStatus() == Grid.Status.OCCUPIED) {
                        if ((grid2 != checkM1) && (grid2 != checkM2) && (grid2 != checkM3)
                                && (grid2 != checkL1) && (grid2 != checkL2) && (grid2 != checkL3)
                                && (grid2 != checkL4) && (grid2 != checkL5)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
