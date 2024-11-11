package sprint4.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralGame extends Board {
    private List<SOSEvent> sosList;  
    private Map<SOSEvent, Character> sosPlayerMap = new HashMap<>();
    private Cell[][] grid;   
    private GameState currentGameState;  
    private int boardSize;       
    private int blueScore;        
    private int redScore;        
    private char currentPlayer;   // 'B' for Blue player, 'R' for Red player
    
    // Constructor for default 3x3 game board
    public GeneralGame() {
    	this.grid = new Cell[3][3];
    	this.boardSize = 3;
    	this.sosList = new ArrayList<>();
    	sosList.clear();
        initializeBoard(); 
    }

    // Constructor accepting custom game board size
    public GeneralGame(int size) {
    	this.grid = new Cell[size][size];
    	if (size >= 3 && size <= 20) { // sets upper bound to 20x20 to match spinner
    		this.boardSize = size;
    	}else
    		this.boardSize = 3; //invalid size resets to default
    	this.sosList = new ArrayList<>();
    	sosList.clear();
        initializeBoard(); 
    }

    // Setup game board for new game
    public void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid[i][j] = Cell.EMPTY;
            }
        }
        currentGameState = GameState.PLAYING; // Reset game state & scores
        currentPlayer = 'B';
        blueScore = redScore = 0;    
    }    
    
    public Cell[][] getBoard() {
    	return grid;
    }

    // Set new size for game board and reinitialize
    public void setBoardSize(int size) {
    	this.boardSize = size;
    	this.grid = new Cell[size][size];
    	sosList.clear();
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
    
    public List<SOSEvent> getSOSList(){
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
    
    @Override
    public boolean makeMove(int row, int column, Cell cell) {

        if (currentGameState != GameState.PLAYING) {
            System.out.println("Game over");
            return false;
        }    
        //Validity check 
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize || grid[row][column] != Cell.EMPTY) {
            System.out.println("Invalid move");
            return false; // Invalid move
        }
        grid[row][column] = cell;  // Place the cell

        if (!hasSOS() && currentGameState == GameState.PLAYING) {  // If no SOS is made, switch the player
        	changeTurn();       
        } 
        else {  
        	System.out.println(currentPlayer + " made an SOS");
        	printScores();  
            printList();    
        }      
        
        updateGameState(currentPlayer);        
        return true;
    }
    
    @Override
    public void changeTurn() {
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
        System.out.println("Switching players... Current Player is " + currentPlayer);
    }

    private void printScores() {  // Print current scores for both players
        System.out.println("Blue Score: " + blueScore);
        System.out.println("Red Score: " + redScore);
    }

    public void printList() {  // Print list of SOS events found
        sosList.forEach(event -> System.out.println(event));
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
        boolean boardFull = isFull();

        if (boardFull) {
            if (blueScore > redScore) {  // Blue wins
                currentGameState = GameState.BLUE_WON;
                logWinner("Blue");
            } 
            else if (redScore > blueScore) {   // Red wins
                currentGameState = GameState.RED_WON;
                logWinner("Red");
            } 
            else {                            // Tie game
                currentGameState = GameState.DRAW;
                logDraw();
            }
        }
    }

    private void logWinner(String winner) {  
        System.out.println(winner + " WINS");
    }

    private void logDraw() {               
        System.out.println("Tie Game");
    }
	
    public boolean isFull() {
    	for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (grid[row][column] == Cell.EMPTY) {   
                    return false;	// Board is not full because an empty cell was found
                }
            }
    	}
    	return true;  // No empty cells are found, board is full
    }
    
    private boolean eventInList(SOSEvent event, List<SOSEvent> eventList) {
        for (SOSEvent existingEvent : eventList) {
            if (existingEvent.equals(event)) {
                return true; 
            }
        }
        return false; 
    }
    
    public boolean hasSOS() {   // Checks board for any SOS events in all directions
        
    	Cell[] symbols = {Cell.S, Cell.O, Cell.S}; 
        boolean sosFound = false;
        
        // Find horizontal SOS Events
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize - 2; column++) {
                if (grid[row][column] == symbols[0] &&
                    grid[row][column + 1] == symbols[1] &&
                    grid[row][column + 2] == symbols[2]) {
                	SOSEvent newEvent = new SOSEvent(symbols[0], row, column, "row");
                	if (!eventInList(newEvent, sosList)) {
                		sosList.add(newEvent);
                		sosPlayerMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}               	
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
                	if (!eventInList(newEvent, sosList)) {
                		sosList.add(newEvent);
                		sosPlayerMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
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
                	if (!eventInList(newEvent, sosList)) {
                		sosList.add(newEvent);
                		sosPlayerMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
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
                	if (!eventInList(newEvent, sosList)) {
                		sosList.add(newEvent);
                		sosPlayerMap.put(newEvent, currentPlayer); // Record player with SOS
                		sosFound = true;
                		countSOS();
                	}
                }
            }
        }
        return sosFound;
    }
    /*
    // Convert numerical directions into strings describing the event's path for easier tracking
    private String getDirectionString(int rowDirection, int columnDirection) {
        if (rowDirection == 0) return "row";  // Horizontal event
        if (columnDirection == 0) return "column";  // Vertical event
        return rowDirection == 1 ? (columnDirection == 1 ? "diagTlBr" : "diagTrBl") : "";  // Diagonal events
    }*/
}
