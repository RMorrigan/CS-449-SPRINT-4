package sprint4.product;

import javax.swing.JFrame;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import sprint4.product.Board.Cell;
import sprint4.product.Board.GameState;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;

import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	public int CELL_SIZE;
	public int CELL_PADDING;
	public int SYMBOL_SIZE;
	public static final int SYMBOL_STROKE_WIDTH = 8;
	private int CANVAS_SIZE = 300;
	public char currentTurn;
	Cell currentPlayerSymbol;
		
	private GameBoardCanvas gameBoardCanvas; 
	private JTextField currentTurnText;
	private JTextField scoreText;
	
	private JRadioButton rdbtnBlueS;
	private JRadioButton rdbtnBlueO;
	private JRadioButton rdbtnBlueHuman;
    private JRadioButton rdbtnBlueComp;
    private ButtonGroup playerTypeB = new ButtonGroup(); 
    private ButtonGroup B;
	
    private JRadioButton rdbtnRedS;
    private JRadioButton rdbtnRedO;
	private JRadioButton rdbtnRedHuman;
    private JRadioButton rdbtnRedComp;
    private ButtonGroup playerTypeR = new ButtonGroup(); 
    private ButtonGroup R;
    
    private ButtonGroup G; 
    private JSpinner spinBoardSize;
    
    private SimpleGame simpleGame; 
    private GeneralGame generalGame; 
    private AutoPlayer compBlue; 
    private AutoPlayer compRed;
    private Board board; 
    //private int boardSize;    
        
    public GUI() {
    	
    	setTitle("SOS GAME");
    	setResizable(false); 
    	setVisible(true);
    	setSize(350, 300); 
    	setBackground(Color.WHITE);		
		getContentPane().setBackground(Color.WHITE);
		setContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); 
		
		//Initialize
		generalGame = new GeneralGame();
        simpleGame = new SimpleGame();
        board = simpleGame;
        compBlue = new AutoPlayer(board, 'B');
        compRed = new AutoPlayer(board, 'R');
	}
    
    public Board getBoard(){
		return board;
	}
		
	// Setup content pane with game components
	protected void setContentPane(){	
		
        gameBoardCanvas = new GameBoardCanvas();
        gameBoardCanvas.setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
        Border gridBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        gameBoardCanvas.setBorder(gridBorder);
        
        JPanel topPanel = new JPanel();
    	topPanel.setBackground(Color.WHITE);
    	JPanel rightPanel = new JPanel();
    	rightPanel.setBackground(Color.WHITE);
    	JPanel leftPanel = new JPanel();
    	leftPanel.setBackground(Color.WHITE);
    	JPanel bottomPanel = new JPanel();
    	bottomPanel.setBackground(Color.WHITE);
        
        topPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
        //top panel attributes
        JLabel lblTitle = new JLabel("SOS");
        lblTitle.setBackground(Color.WHITE);
        JLabel lblBoardInput = new JLabel("Board Size: ");
        lblBoardInput.setBackground(Color.WHITE);
        
        //BoardSize init:3 min:3 max:20 step:1
        spinBoardSize = new JSpinner();
        spinBoardSize.setModel(new SpinnerNumberModel(3, 3, 8, 1));
        spinBoardSize.addChangeListener(e -> {
            int newSize = (int) spinBoardSize.getValue();
            board.setBoardSize(newSize); // Change the board size
            board.newGame();
            gameBoardCanvas.repaint();
        });
        
        // simple or general game mode option  
        JLabel lblGameMode = new JLabel("Game Mode: ");
        JRadioButton rdbtnSimpleGame = new JRadioButton("Simple Game", true);
        rdbtnSimpleGame.setBackground(Color.WHITE);
        JRadioButton rdbtnGeneralGame = new JRadioButton("General Game");
        rdbtnGeneralGame.setBackground(Color.WHITE);
        rdbtnSimpleGame.addActionListener(e -> {
            board = simpleGame;
            rdbtnGeneralGame.setEnabled(false);
        });

        rdbtnGeneralGame.addActionListener(e -> {
            board = generalGame;
            rdbtnSimpleGame.setEnabled(false);
        });
        
        G = new ButtonGroup();
        G.add(rdbtnSimpleGame);
        G.add(rdbtnGeneralGame);
       
        topPanel.add(lblTitle);
        topPanel.add(lblGameMode);
        topPanel.add(rdbtnSimpleGame);
        topPanel.add(rdbtnGeneralGame);  
        topPanel.add(lblBoardInput);    
        topPanel.add(spinBoardSize);
    
        //Right Panel Attributes - Red Player
    	//RED PLAYER'S LABEL AND BUTTONS
        JLabel lblRed = new JLabel("Red Player"); 
        rdbtnRedS = new JRadioButton("S", true);
        rdbtnRedS.setActionCommand("S");
        rdbtnRedS.setBackground(Color.WHITE);
        
        rdbtnRedO = new JRadioButton("O");
        rdbtnRedO.setActionCommand("O");
        rdbtnRedO.setBackground(Color.WHITE);
        
        rdbtnRedHuman = new JRadioButton("Human", true);
        rdbtnRedHuman.setBackground(Color.WHITE);
        
        rdbtnRedComp = new JRadioButton("Computer");
        rdbtnRedComp.setBackground(Color.WHITE);
        
        //ButtonGroups for Red
        R = new ButtonGroup();
        R.add(rdbtnRedS);
        R.add(rdbtnRedO);
        
        playerTypeR = new ButtonGroup();
        playerTypeR.add(rdbtnRedHuman);
        playerTypeR.add(rdbtnRedComp);
        
        rdbtnRedComp.addActionListener(e -> {
        	
            compRed = new AutoPlayer(board, 'R');
            R.clearSelection();
            rdbtnRedS.setEnabled(false);
            rdbtnRedO.setEnabled(false);
            rdbtnRedHuman.setEnabled(false);
            if (board.getCurrentPlayer() == 'R') {
                compRed.makeAutoMove();
                gameBoardCanvas.repaint();
                handleGameResult();
            }
        });
        
        rightPanel.add(lblRed);
        rightPanel.add(rdbtnRedHuman);
        rightPanel.add(rdbtnRedS);
        rightPanel.add(rdbtnRedO);          
        rightPanel.add(rdbtnRedComp);
        
    	//Left Panel Attributes - Blue Player
    	//Blue Player's label and buttons
        JLabel lblBlue = new JLabel("Blue Player");        
        rdbtnBlueS = new JRadioButton("S", true);
        rdbtnBlueS.setActionCommand("S");
        rdbtnBlueS.setBackground(Color.WHITE);
        
        rdbtnBlueO = new JRadioButton("O");
        rdbtnBlueO.setActionCommand("O");
        rdbtnBlueO.setBackground(Color.WHITE);
        
        rdbtnBlueHuman = new JRadioButton("Human", true);
        rdbtnBlueHuman.setBackground(Color.WHITE);
        
        rdbtnBlueComp = new JRadioButton("Computer");
        rdbtnBlueComp.setBackground(Color.WHITE);
        
        //ButtonGroups for Blue
        B = new ButtonGroup();
        B.add(rdbtnBlueS);
        B.add(rdbtnBlueO);
        
        playerTypeB.add(rdbtnBlueHuman);
        playerTypeB.add(rdbtnBlueComp);
        
        leftPanel.add(lblBlue);
        leftPanel.add(rdbtnBlueHuman);
        leftPanel.add(rdbtnBlueS);
        leftPanel.add(rdbtnBlueO);         
        leftPanel.add(rdbtnBlueComp);
        
        rdbtnBlueComp.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {            	
            	compBlue = new AutoPlayer(board, 'B');
                B.clearSelection();
                rdbtnBlueS.setEnabled(false);
                rdbtnBlueO.setEnabled(false);
                rdbtnBlueHuman.setEnabled(false);
	            if (board.getCurrentPlayer() == 'B') {
	                compBlue.makeAutoMove();
	                gameBoardCanvas.repaint();
	                handleGameResult();
	            }
            }
        });
        
        //Bottom Panel attributes
        currentTurnText = new JTextField();
        currentTurnText.setBorder(null);
        currentTurnText.setBackground(Color.WHITE);
        currentTurnText.setColumns(16);
        
        scoreText = new JTextField(); 
        scoreText.setBorder(null);
        scoreText.setBackground(Color.WHITE);
        scoreText.setColumns(16);
        
        // NEW GAME BUTTON
        JButton btnNewGame = new JButton("New Game");
        btnNewGame.setToolTipText("Start New Game");
        btnNewGame.addActionListener(e -> {
            board.newGame();
            spinBoardSize.setValue(3); 
            rdbtnBlueS.setEnabled(true);
            rdbtnBlueO.setEnabled(true);
            rdbtnBlueHuman.setEnabled(true);
            rdbtnBlueComp.setEnabled(true);
            rdbtnRedS.setEnabled(true);
            rdbtnRedO.setEnabled(true);
            rdbtnRedHuman.setEnabled(true);
            rdbtnRedComp.setEnabled(true);
                       
            rdbtnSimpleGame.setEnabled(true);
            rdbtnGeneralGame.setEnabled(true);
            
            rdbtnBlueS.setSelected(true);
            rdbtnRedS.setSelected(true);
            rdbtnSimpleGame.setSelected(true);
            
            board = new SimpleGame();
            rdbtnBlueHuman.setSelected(true);
            rdbtnRedHuman.setSelected(true);
            
            currentTurnText.setText(""); 
            scoreText.setText(""); 
            gameBoardCanvas.repaint(); 
        });
        
        // AUTOPLAY BUTTON
        JButton btnAutoPlayer = new JButton("AutoPlay");
        btnAutoPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable all player option buttons
                rdbtnBlueS.setEnabled(false);
                rdbtnBlueO.setEnabled(false);
                rdbtnBlueHuman.setEnabled(false);
                rdbtnBlueComp.setEnabled(false);
                rdbtnRedS.setEnabled(false);
                rdbtnRedO.setEnabled(false);
                rdbtnRedHuman.setEnabled(false);
                rdbtnRedComp.setEnabled(false);

                B.clearSelection();
                R.clearSelection();

                // Log current game mode and board size to the console
                System.out.println(board.getGameMode());
                System.out.println(board.getBoardSize());

                fullAutoGame(board);
                gameBoardCanvas.repaint();
            }
        });
        
        bottomPanel.add(btnNewGame);
        bottomPanel.add(scoreText);
        bottomPanel.add(currentTurnText);
        bottomPanel.add(btnAutoPlayer);
        
        // Set up contentPane
        Container ContentPane = getContentPane();
        ContentPane.setLayout(new BorderLayout());
        ContentPane.add(topPanel, BorderLayout.NORTH);
        ContentPane.add(rightPanel, BorderLayout.EAST);
        ContentPane.add(bottomPanel, BorderLayout.SOUTH);
        ContentPane.add(leftPanel, BorderLayout.WEST);        
        ContentPane.add(gameBoardCanvas, BorderLayout.CENTER);
        topPanel.setPreferredSize(new Dimension(150, 100));
        rightPanel.setPreferredSize(new Dimension(100, 100));        
        bottomPanel.setPreferredSize(new Dimension(150, 100));  
        leftPanel.setPreferredSize(new Dimension(100, 100));
	}       

    class GameBoardCanvas extends JPanel {	
    	
    	GameBoardCanvas() {
    	    addMouseListener(new MouseAdapter() {
    	        @Override
    	        public void mouseClicked(MouseEvent e) {
    	            if (board.getCurrentGameState() != GameState.PLAYING) {
    	                return;
    	            }

    	            int rowSelected = e.getY() / CELL_SIZE;
    	            int colSelected = e.getX() / CELL_SIZE;

    	            if (board.getCell(rowSelected, colSelected) != Cell.EMPTY) {
    	                return;
    	            }

    	            currentTurn = board.getCurrentPlayer();
    	            ButtonModel selection = currentTurn == 'B' ? B.getSelection() : R.getSelection();

    	            if (selection != null && selection.getActionCommand() != null) {
    	                currentPlayerSymbol = Cell.valueOf(selection.getActionCommand());
    	                board.makeMove(rowSelected, colSelected, currentPlayerSymbol);
    	                gameBoardCanvas.repaint();
    	                handleGameResult();
    	            }
    	        }
    	    });
    	}

		@Override
		public void paintComponent(Graphics g) { 
			super.paintComponent(g);   
			setBackground(Color.WHITE);
			
		    int canvasSize = Math.min(getWidth(), getHeight());
		    CELL_SIZE = canvasSize / board.getBoardSize();
		    CELL_PADDING = CELL_SIZE / 20;
		    SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
		    
			drawGridLines(g);
			drawBoard(g);
			drawWinLine(g);
		}
		
		// Draws grid lines according to board size
	    private void drawGridLines(Graphics g) {
	        g.setColor(Color.BLACK);
	        int gridSize = getSize().width / board.getBoardSize();
	        
	        for (int i = 1; i < board.getBoardSize(); i++) {
	            int pos = i * gridSize;
	            g.drawLine(pos, 0, pos, getSize().height);
	            g.drawLine(0, pos, getSize().width, pos);
	        }
	    }
	
	    // Draws S and O symbols on the board
	    private void drawBoard(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        
	        for (int row = 0; row < board.getBoardSize(); row++) {
	            for (int col = 0; col < board.getBoardSize(); col++) {
	                int x1 = col * CELL_SIZE + CELL_PADDING;
	                int y1 = row * CELL_SIZE + CELL_PADDING;
	                Cell cellValue = board.getCell(row, col);
	                if (cellValue == Cell.S || cellValue == Cell.O) {
	                    drawSymbol(g2d, cellValue.name().charAt(0), x1, y1);
	                }
	            }
	        }
	    }
	}
    
    private void fullAutoGame(Board board) {
	    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() { // create new swing worker
	        @Override
	        protected Void doInBackground() throws Exception {
	        	compBlue = new AutoPlayer(board, 'B');
	        	compRed = new AutoPlayer(board, 'R');
	            while (board.getCurrentGameState() == GameState.PLAYING) {
	                // Make the blue computer's move
	                if (board.getCurrentPlayer() == 'B') {
	                    compBlue.makeAutoMove();
	                }

	                // Make the red computer's move
	                if (board.getCurrentGameState() == GameState.PLAYING && board.getCurrentPlayer() == 'R') {
	                    compRed.makeAutoMove();
	                }
	                publish();
	                
	                // Delay for video
	                try {
	                    Thread.sleep(500);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }

	            return null;
	        }

	        @Override
	        protected void process(List<Void> chunks) {
	            gameBoardCanvas.repaint();
	        }

	        @Override
	        protected void done() {
	            gameBoardCanvas.repaint();
	            handleGameResult();
	        }
	    };

	    worker.execute();
	} 
	
	//Update GUI based on game's current state and scores
    public void handleGameResult() {
        switch (board.getCurrentGameState()) {
            case BLUE_WON:
                currentTurnText.setText("Blue Wins!");
                break;
            case RED_WON:
                currentTurnText.setText("Red Wins!");
                break;
            case DRAW:
                currentTurnText.setText("Tie Game!");
                break;
            default:
                currentTurnText.setText("Play: " + (board.getCurrentPlayer() == 'B' ? "Blue" : "Red"));
                break;
        }	        
        scoreText.setText(String.format("Blue Score: %d, Red Score: %d", board.getBlueScore(), board.getRedScore()));
        repaint();
    }

    private void drawSymbol(Graphics2D g2d, char symbol, int x, int y) {
    	String letter = String.valueOf(symbol);
    	int fontSize = CELL_SIZE > 30 ? SYMBOL_SIZE : 24;
	    Font font = new Font("Arial", Font.BOLD, fontSize);
	    g2d.setFont(font);

	    FontMetrics fm = g2d.getFontMetrics(font);
	    int textWidth = fm.stringWidth(letter);
	    int textHeight = fm.getHeight();
	    
	    int centerX = x + (CELL_SIZE - textWidth) / 2; // Calculate the center position for the text
	    int centerY = y + (CELL_SIZE - textHeight) / 2 + fm.getAscent();

	    g2d.setColor(Color.BLACK);
	    g2d.drawString(letter, centerX, centerY);
	}
	 
	 private Color getPlayerColor(char player) {
	    return player == 'B' ? Color.BLUE : Color.RED;
	}
    
	private void drawWinLine(Graphics g) {
	    List<SOSEvent> sosEventList = board.getSOSList();
	    Map<SOSEvent, Character> playerSOSMap = board.getSOSPlayerMap();

	    for (SOSEvent event : sosEventList) {
	        Color playerColor = getPlayerColor(playerSOSMap.get(event));
	        g.setColor(playerColor);
	        drawLine(g, event);
	    }
	}	
	
	private void drawLine(Graphics g, SOSEvent event) {
	        
	        int startRow = event.getRow()* CELL_SIZE + CELL_SIZE/2;
	        int startCol = event.getColumn()* CELL_SIZE + CELL_SIZE/2;
	        String direction = event.getDirection();

	        int lineLength = (int) (CELL_SIZE * 2.3);
	        ((Graphics2D) g).setStroke(new BasicStroke(5));

	        // Draw a line based on the type of the SOS event
	        switch (direction) {
	        case "row":
	            g.drawLine(startCol, startRow, startCol + lineLength, startRow);
	            break;
	        case "column":
	            g.drawLine(startCol, startRow, startCol, startRow + lineLength);
	            break;
	        case "diagTlBr":
	            g.drawLine(startCol, startRow, startCol + lineLength, startRow + lineLength);
	            break;
	        case "diagTrBl":
	            g.drawLine(startCol, startRow, startCol - lineLength, startRow + lineLength);
	            break;
	        }
	 }
		
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
                gui.board.initializeBoard();
            }
		});
	}
}