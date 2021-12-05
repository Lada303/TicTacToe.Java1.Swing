package TicTacToe_Lada303;

public class Cell {

    protected final int columnNumber;
    protected final int rowNumber;
    private Dots dot;

    protected Cell(int x, int y) {
        columnNumber = x; rowNumber = y; dot = Dots.EMPTY;
    }

    protected void setDot(Dots dot) {
        this.dot = dot;
    }

    protected Dots getDot() {
        return dot;
    }

    protected boolean isEmptyCell() {
        return dot == Dots.EMPTY;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "columnNumber=" + columnNumber +
                ", rowNumber=" + rowNumber +
                ", dot=" + dot +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell that = (Cell) obj;
        if (this.columnNumber != that.columnNumber) return false;
        if (this.rowNumber != that.rowNumber) return false;
        return this.dot == that.dot;

    }
}
