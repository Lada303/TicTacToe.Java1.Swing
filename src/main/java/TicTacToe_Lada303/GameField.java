package TicTacToe_Lada303;

public class GameField {

    private final Cell[][] field;

    protected GameField(int x, int y) {
        field =new Cell[y][x];
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                field[i][j]=new Cell(j,i);
            }
        }
    }

       protected boolean isCellValid(int x, int y) {
        return (0 <= x && x < field[0].length) && (0 <= y && y < field.length);
    }

    protected int getCountRow() {
        return field.length;
    }

    protected int getCountColumn() {
        return field[1].length;
    }

    protected Cell getCell(int x, int y) {
        return field[y][x];
    }
    protected Cell getCell(Cell cell) {
        return field[cell.rowNumber][cell.columnNumber];
    }

    protected  Cell[] getRow(Cell cell) {
        Cell[] row=new Cell[field[0].length];
        System.arraycopy(field[cell.rowNumber], 0, row, 0, row.length);
        return row;
    }

    protected  Cell[] getColumn(Cell cell) {
        Cell[] column=new Cell[field.length];
        for (int i = 0; i < column.length; i++) {
            column[i]= field[i][cell.columnNumber];
        }
        return column;
    }

    protected  Cell[] getD1(Cell cell) {
        int b = cell.rowNumber-cell.columnNumber;
        Cell[] d1 = new Cell[field.length];
        for (int i = (b >= 0 ? b : 0); i < d1.length - (b >= 0 ? 0 : -b); i++) {
            d1[i]= field[i][i-b];
        }
        return d1;
    }

    protected  Cell[] getD2(Cell cell) {
        int b = cell.rowNumber+cell.columnNumber;
        Cell[] d2=new Cell[field.length];
        for (int i = (b < field.length ? 0 : b - (field.length - 1));
                    i < d2.length - (b < field.length ? (field.length - 1) - b : 0); i++) {
             d2[i]= (field[i][b-i]);
        }
        return d2;
    }

    protected boolean isD1(Cell cell, int lengthD) {
        return (cell.rowNumber-(field.length-lengthD) <=cell.columnNumber
                && cell.columnNumber<= field[0].length-lengthD+cell.rowNumber);
    }

    protected boolean isD2(Cell cell, int lengthD) {
        return (lengthD-1-cell.rowNumber <=cell.columnNumber
                && cell.columnNumber<= field[0].length-1-cell.rowNumber+(field.length-lengthD));
    }
}
