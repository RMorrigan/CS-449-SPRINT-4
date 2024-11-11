package sprint4.product;

import java.util.Objects;

import sprint4.product.Board.Cell;

public class SOSEvent {  // Event details are set when created and can't be changed later
    private Cell cell;
    private int row;
    private int column;
    private String direction;
 
    public SOSEvent(Cell cell, int row, int column, String direction) {
        this.cell = cell;  // The type of cell (S or O) that starts SOS event
        this.row = row;    // The row index where SOS starts
        this.column = column;    // The column index where SOS starts
        this.direction = direction;  // The direction of SOS event (horizontal, vertical, diagonal Tlbr & Trbl
    }

    public Cell getCell() {
        return cell;
    }
    
    public void setCell(Cell cell) {
    	this.cell = cell;
    }

    public int getRow() {
        return row;
    }
    
    public void setRow(int row) {
    	this.row = row;
    }

    public int getColumn() {
        return column;
    }
    
    public void setColumn(int column) {
    	this.column = column;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() { // Change to string for testing
        return String.format("Cell: %s, Row: %d, Column: %d, Direction: %s", cell, row, column, direction);
    }

    @Override
    public boolean equals(Object event) {  // Checks if event is equal to another event
        if (this == event) return true;
        if (event == null || getClass() != event.getClass()) return false;
        SOSEvent otherEvent = (SOSEvent) event;
        return cell == otherEvent.cell && row == otherEvent.row && column == otherEvent.column && Objects.equals(direction, otherEvent.direction);
    }

    @Override
    public int hashCode() {   // Create hash code for this SOS event
        return Objects.hash(cell, row, column, direction);
    }
}
