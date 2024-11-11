package sprint4.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sprint4.product.AutoPlayer;
import sprint4.product.Board;
import sprint4.product.Board.Cell;
import sprint4.product.SimpleGame;

class AutoPlayerTest {
    private AutoPlayer autoPlayer;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new SimpleGame();
        autoPlayer = new AutoPlayer(board, 'B');
    }

    @AfterEach
    void tearDown() {
        board = null;
        autoPlayer = null;
    }
    
    @Test
    public void testMakeStartMove() {
        int initialEmptyCells = autoPlayer.getNumEmptyCells();
        autoPlayer.makeStartMove();
        assertEquals(initialEmptyCells - 1, autoPlayer.getNumEmptyCells(), "Error: Expected the number of empty cells to decrease by one.");
    }
    
    @Test
    public void testMakeWinMoveAndAutoMove() {
    	//Winnable Scenario by Autoplayer
        board.makeMove(0, 0, Cell.S);
        board.makeMove(0, 2, Cell.S);
        autoPlayer.makeAutoMove(); //should access makeWinMove
        assertEquals(Board.GameState.BLUE_WON, board.getCurrentGameState(), "Expected game state: BLUE_WON");

        //Reset the board for a new scenario.
        board.newGame();
        //No immediate win available
        board.makeMove(0, 0, Cell.S);
        board.makeMove(0, 1, Cell.S);
        board.makeMove(1, 2, Cell.O);

        autoPlayer.makeAutoMove();
        //Check no change in gamestate
        assertNotEquals(Board.GameState.RED_WON, board.getCurrentGameState(), "Unexpected RED_WON state");
        assertNotEquals(Board.GameState.BLUE_WON, board.getCurrentGameState(), "Unexpected BLUE_WON state");
        assertNotEquals(Board.GameState.DRAW, board.getCurrentGameState(), "Unexpected DRAW state");
    }
}
