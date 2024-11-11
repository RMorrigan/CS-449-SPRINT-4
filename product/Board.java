package sprint4.product;

import java.util.List;
import java.util.Map;

public abstract class Board {
          
    private Cell[][] grid;    
    private int boardSize;
    private int blueScore;
    private int redScore;
    private char currentPlayer;
    private boolean gameMode;
    
    public static enum Cell {EMPTY, S, O}
    
    public enum GameState {PLAYING, DRAW, BLUE_WON, RED_WON}

    private GameState currentGameState;
    
    // Default constructor initializes a 3x3 board in simple game mode with blue player starting
    public Board() {
        this.grid = new Cell[3][3];
        this.boardSize = 3;        
        this.gameMode = true; 
        this.currentPlayer = 'B'; 
        this.currentGameState = GameState.PLAYING;
        initializeBoard();
    }
    
    // Constructor to initialize a board with a specified size in simple game mode with blue player starting
    public Board(int size) {
        this.grid = new Cell[size][size];
        this.boardSize = size;     
        this.gameMode = true; 
        this.currentPlayer = 'B'; 
        this.currentGameState = GameState.PLAYING;        
        initializeBoard();
    }
    
    // Constructor to initialize a board with specified size and game mode with blue player starting
    public Board(int size, boolean mode) {
        this.grid = new Cell[size][size];
        this.boardSize = size;
        this.gameMode = mode;
        this.currentPlayer = 'B'; 
        this.currentGameState = GameState.PLAYING;        
        initializeBoard();
    }
    
    public void setBoardSize(int size) {
    	if(size > 2 && size <= 20)
        {
    		this.grid = new Cell[size][size]; 
        	this.boardSize = size;
        	initializeBoard();
        }    
        
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid[i][j] = Cell.EMPTY;
            }
        }
    }
    
    public Cell[][] getBoard() {
        return grid;
    }
    
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize)
            return grid[row][column];
        else
            return Cell.EMPTY;
    }
      
    public void setGameMode(boolean mode) {
        this.gameMode = mode; // true for simple game, false for general game
    }

    public boolean getGameMode() {
        return gameMode;
    }

    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    public Cell getSymbol(int row, int col) {    	    	
        return grid[row][col];
    }

    public boolean makeMove(int row, int column, Cell cell) {
    	boolean isValid = isValid(row, column);
    	if (isValid) {
    		grid[row][column] = cell;
            changeTurn();
        }
    	return isValid;
    }
    
    private boolean isValid(int row, int column) {
        return row >= 0 && row < boardSize && column >= 0 && column < boardSize && grid[row][column] == Cell.EMPTY;
    }
    
    public GameState getCurrentGameState() {
        return currentGameState;
    }
    
    public void changeTurn() {        
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
    }

    public void countSOS() {
        if (currentPlayer == 'B') {
        	blueScore++;
        } else {
        	redScore++;
        }
    }
    
    public abstract void newGame();
    protected abstract void updateGameState(char player);
    protected abstract int getBlueScore();
    protected abstract int getRedScore();
    protected abstract boolean hasSOS();
    protected abstract boolean isFull();
    protected abstract List<SOSEvent> getSOSList();
    protected abstract Map<SOSEvent, Character> getSOSPlayerMap();

}