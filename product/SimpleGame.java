package sprint4.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleGame extends Board {
	
	private List<SOSEvent> sosList;
    private Map<SOSEvent, Character> sosPlayerMap = new HashMap<>(); // Maps found SOS's to player
    private Cell[][] grid;   
    private GameState currentGameState;   
    private int boardSize;        
    private int blueScore;        
    private int redScore;         
    private char currentPlayer;    // 'B' for Blue player, 'R' for Red player
    
    // Default constructor initializing a 3x3 board
    public SimpleGame() {
        this.grid = new Cell[3][3];
        this.boardSize = 3;
        this.sosList = new ArrayList<>();
        initializeBoard();
        sosList.clear();
    }

    // Constructor with custom board size
    public SimpleGame(int size) {
    	this.grid = new Cell[size][size];
    	if (size >= 3 && size <= 20) { // sets upper bound to 20x20 to match spinner
    		this.boardSize = size;
    	}else
    		this.boardSize = 3; //invalid size resets to default      
    	this.sosList = new ArrayList<>();
        initializeBoard();
        sosList.clear();
    }
    
    // Initializes or resets the board to start a new game
    public void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid[i][j] = Cell.EMPTY;     // Set all cells to EMPTY
            }
        }
        currentGameState = GameState.PLAYING;
        currentPlayer = 'B';
        blueScore = redScore = 0;
    }    
       
    public Cell[][] getBoard() {
        return grid;
    }
    
    public void setBoardSize(int size) {
    	this.boardSize = size;
    	this.grid = new Cell[size][size];
        initializeBoard();
    }
    
    public int getBoardSize() {
    	return boardSize;
    }
       
    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    public GameState getCurrentGameState() {
		return currentGameState;
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize)
            return grid[row][column];
        else
            return Cell.EMPTY;
    }

    public Cell getSymbol(int row, int column) {
        return grid[row][column];
    }
    
    public int getBlueScore() {
    	return blueScore;
    }
    
    public int getRedScore() {
    	return redScore;
    }
    
    public List<SOSEvent> getSOSList() {
        return sosList;
    }
    
    public Map<SOSEvent, Character> getSOSPlayerMap() {
        return sosPlayerMap;
    }
    
    // Resets board to start a new game
    @Override
    public void newGame() {
    	initializeBoard();
    	sosList.clear();
    }
    
    // Handles a player's move on the boards
    @Override
    public boolean makeMove(int row, int column, Cell cell) {
        
        if (currentGameState != GameState.PLAYING) {
            System.out.println("Game over");
            return false;
        }
        if (isValid(row, column)) {
            grid[row][column] = cell;   
            updateGameState(currentPlayer);       
            if (currentGameState == GameState.PLAYING) {
                changeTurn();
            }
            return true;
        } 
        else {
            // The move is invalid
            return false;
        }
    }
    
    private boolean isValid(int row, int column) {
        return row >= 0 && row < boardSize && column >= 0 && column < boardSize && grid[row][column] == Cell.EMPTY;
    }
   
    // Switches the turn between players
    @Override
    public void changeTurn() {
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
        System.out.println("The current player is " + currentPlayer);
    }
   
    public void countSOS() {
    	if (currentPlayer == 'B') {
    		blueScore++;
    	}
    	else {
    		redScore++;
    	}
    }    

    public void updateGameState(char turn) {
    	if (hasSOS()) {
            currentGameState = (turn == 'B') ? GameState.BLUE_WON : GameState.RED_WON;
            System.out.println(currentGameState + "!");
        } 
    	else if (isFull()) {
            currentGameState = GameState.DRAW;
            System.out.println("Tie game");
        }
    }
    
    public boolean isFull() {
    	for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (grid[row][column] == Cell.EMPTY) {   
                    return false;	
                }
            }
    	}
    	return true;
    }
    
    public boolean hasSOS() {   
    	
    	Cell[] symbols = {Cell.S, Cell.O, Cell.S}; 

        // Find horizontal SOS Events
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (grid[row][column] == symbols[0] &&
                    grid[row][column + 1] == symbols[1] &&
                    grid[row][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "row");
                	sosList.add(newEvent);
            		sosPlayerMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        // Find vertical SOS Events
        for (int column = 0; column < boardSize; column++) {
            for (int row = 0; row < boardSize - 2; row++) {
                if (grid[row][column] == symbols[0] &&
                    grid[row + 1][column] == symbols[1] &&
                    grid[row + 2][column] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "column");
                	sosList.add(newEvent);
                	sosPlayerMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        // Find top-left to bottom-right diagonal SOS Events
        for (int row = 0; row < boardSize - 2; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (grid[row][column] == symbols[0] &&
                    grid[row + 1][column + 1] == symbols[1] &&
                    grid[row + 2][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "diagTlBr");
                	sosList.add(newEvent);
                	sosPlayerMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        // Find top-right to bottom-left diagonal SOS Events
        for (int row = 0; row < boardSize - 2; row++) {
            for (int column = boardSize - 1; column >= 2; column--) {
                if (grid[row][column] == symbols[0] &&
                    grid[row + 1][column - 1] == symbols[1] &&
                    grid[row + 2][column - 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "diagTrBl");
                	sosList.add(newEvent);
                	sosPlayerMap.put(newEvent, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }
        return false;
    }
}
