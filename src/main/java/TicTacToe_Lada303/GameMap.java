package TicTacToe_Lada303;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameMap extends JPanel {

    public static final int MODE_VS_AI = 0;
    public static final int MODE_VS_HUMAN = 1;
    private static final Random RANDOM = new Random();

    private GameField field;
    private int gameMode;
    private int fieldSize;
    private int dotsToWin;
    private int sizeBorder;
    private int startPointField;
    private int sizeAllField;
    private int sizeCell;
    private boolean isInitialized;
    private int countStep;
    private int playerNumTurn;
    private Cell lastStepGamer1;
    private Cell lastStepGamer2;
    private Dots stateGameOver;

    public GameMap() {
        isInitialized = false;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
    }

    public void startNewGame(GameWindow gameWindow) {
        gameMode = gameWindow.getModeGame();
        fieldSize = gameWindow.getFieldSize();
        dotsToWin = gameWindow.getWinLength();
        sizeBorder = getHeight() / 100;
        startPointField = getHeight() / 10;
        sizeAllField = getWidth() - 2 * startPointField + sizeBorder;
        sizeCell = sizeAllField / fieldSize - sizeBorder;
        countStep = 0;
        playerNumTurn = 1;
        field = new GameField(fieldSize, fieldSize);
        isInitialized = true;
        stateGameOver = null;
        repaint();
    }

    private void update(MouseEvent e) {
        if (stateGameOver != null || !isInitialized) {
            return;
        }
        if (playerNumTurn == 1 && !playerTurn(e)) {
            return;
        }
        if (isWin(lastStepGamer1) || isDraw()) {
            return;
        }
        if (gameMode == MODE_VS_AI) {
            aiTurn();
        } else if (playerNumTurn == 2 && !playerTurn(e)) {
            return;
        }
        if (!isWin(lastStepGamer2)) {
            isDraw();
        }
    }

    private boolean playerTurn(MouseEvent e) {
        int cellX = e.getX();
        int cellY = e.getY();
        if (cellX <= startPointField || cellX >= startPointField + sizeAllField ||
                cellY <= 2 * startPointField || cellY >= 2 * startPointField + sizeAllField) {
            return false;
        }
        cellX = (cellX - startPointField) / (sizeCell + sizeBorder);
        cellY = (cellY - 2 * startPointField) / (sizeCell + sizeBorder);
        if (!field.isCellValid(cellX, cellY) || !field.getCell(cellX, cellY).isEmptyCell()) {
            return false;
        }
        if (playerNumTurn == 1) {
            field.getCell(cellX,cellY).setDot(Dots.GAMER_1);
            lastStepGamer1 = field.getCell(cellX,cellY);
            if (gameMode == MODE_VS_HUMAN) {
                playerNumTurn = 2;
            }
        } else {
            field.getCell(cellX,cellY).setDot(Dots.GAMER_2);
            lastStepGamer2 = field.getCell(cellX,cellY);
            playerNumTurn = 1;
        }
        countStep++;
        repaint();
        return true;
    }

    public boolean isDraw() {
        if (countStep >= fieldSize * fieldSize) {
            stateGameOver = Dots.EMPTY;
            repaint();
            return true;
        }
        return false;
    }

    public boolean isWin(Cell cell) {
        if (countStep >= dotsToWin * 2 - 1) {
            if (checkWin(cell)) {
                stateGameOver = cell.getDot();
                repaint();
                return true;
            }
        }
        return false;
    }

    public boolean checkWin(Cell lastCell) {
        if (countNonInterruptDotsToWin(lastCell.getDot(), field.getRow(lastCell)) == dotsToWin) return true;
        if (countNonInterruptDotsToWin(lastCell.getDot(), field.getColumn(lastCell)) == dotsToWin) return true;
        if (field.isD1(lastCell,dotsToWin) &&
                countNonInterruptDotsToWin(lastCell.getDot(), field.getD1(lastCell)) == dotsToWin) return true;
        return (field.isD2(lastCell,dotsToWin) &&
                countNonInterruptDotsToWin(lastCell.getDot(), field.getD2(lastCell)) == dotsToWin);
    }

    public int countNonInterruptDotsToWin(Dots dot, Cell[] arrCells) {
        int counter=0;
        for (Cell cell : arrCells) {
            counter = (cell != null && cell.getDot() == dot ? counter + 1 : 0);
            if (counter == dotsToWin) return counter;
        }
        return counter;
    }

    public void aiTurn() {
        for (int decrement = 1; decrement < dotsToWin; decrement++) {
            if (decrement == 1 && isAIStep(lastStepGamer2, decrement)) {
                break;
            }
            if (isAIStep(lastStepGamer1, decrement)) {
                break;
            }
        }
        if (lastStepGamer2 == null || !field.getCell(lastStepGamer2).isEmptyCell()) {
            int x, y;
            do {
                x = RANDOM.nextInt(field.getCountColumn());
                y = RANDOM.nextInt(field.getCountRow());
            } while (field.getCell(x,y).getDot() != null);
            lastStepGamer2=field.getCell(x,y);
        }
        field.getCell(lastStepGamer2).setDot(Dots.GAMER_2);
        countStep++;
        repaint();
    }

    public boolean isAIStep(Cell lastCell, int decrement) {
        if (lastCell == null || countStep < dotsToWin * 2 - 1 && lastCell.getDot() == Dots.GAMER_2) {return false;}
        if (field.isD1(lastCell, dotsToWin)
                && countDotsInLine(lastCell.getDot(), field.getD1(lastCell)) == dotsToWin - decrement
                && isEmptyValidCellInLine(field.getD1(lastCell), lastCell)) {return true;}
        if (field.isD2(lastCell, dotsToWin)
                && countDotsInLine(lastCell.getDot(), field.getD2(lastCell)) == dotsToWin - decrement
                && isEmptyValidCellInLine(field.getD2(lastCell), lastCell)) {return true;}
        if (countDotsInLine(lastCell.getDot(), field.getRow(lastCell)) == dotsToWin - decrement
                && isEmptyValidCellInLine(field.getRow(lastCell), lastCell)) {return true;}
        return countDotsInLine(lastCell.getDot(), field.getColumn(lastCell)) == dotsToWin - decrement
                && isEmptyValidCellInLine(field.getColumn(lastCell), lastCell);
    }

    public int countDotsInLine(Dots dot, Cell[] arrCells) {
        int counter=0;
        for (Cell cell : arrCells) {
            if (cell != null && cell.getDot() == dot) counter++;
        }
        return counter;
    }

    public boolean isEmptyValidCellInLine(Cell[] arrCells, Cell lastCell) {
        int position=0;
        for (int i = 0; i < arrCells.length; i++) {
            if (arrCells[i] != null && arrCells[i]==lastCell) {
                position=i;
                break;
            }
        }
        for (int i = 1; i < arrCells.length; i++) {
           if (position + i < arrCells.length && arrCells[position + i] != null
                   && arrCells[position + i].isEmptyCell()) {
                lastStepGamer2 = arrCells[position + i];
                return true;
           }
           if (position - i >= 0 && arrCells[position - i] != null
                   && arrCells[position - i].isEmptyCell()) {
               lastStepGamer2 = arrCells[position - i];
               return true;
           }
           if (position + i >= arrCells.length && position - i < 0) {
               break;
           }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        onRepaint(g);
    }

    private void onRepaint(Graphics g) {
        if (!isInitialized) {
            return;
        }
        drawHead(g);
        drawField(g);
        drawDots(g);
        if (stateGameOver != null) {
            showGameOverMessage(g);
        }
    }

    private void drawHead(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, getWidth()/10));
        if (gameMode == MODE_VS_AI) {
            g.drawString("Player     vs     AI", startPointField, startPointField + getWidth() / 20);
        } else {
            g.drawString("Player " + playerNumTurn + " turn", startPointField, startPointField + getWidth() / 20);
            if (playerNumTurn == 1) {
                draw0(g, 0, 0, 0);
            } else {
                drawX(g, 0, 0, 0);
            }
        }
    }

    private void  drawField (Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                g.fillRect(startPointField + j * (sizeCell+ sizeBorder),
                        2 * startPointField  + i * (sizeCell+ sizeBorder),
                        sizeCell, sizeCell);
            }
        }
    }

    private void drawDots(Graphics g) {
        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                if (field.getCell(x, y).isEmptyCell()) {
                    continue;
                }
                if (field.getCell(x, y).getDot() == Dots.GAMER_1) {
                    draw0(g, x, y, 1);
                } else {
                    drawX(g, x, y, 1);
                }
            }
        }
    }

    private void draw0(Graphics g, int x, int y, int flag) {
        if (flag == 0) {
            g.setColor(Color.GREEN);
            g.fillOval(sizeAllField - 2 * sizeBorder, startPointField / 2,
                    startPointField, startPointField);
            g.setColor(Color.DARK_GRAY);
            g.fillOval(sizeAllField - sizeBorder, startPointField / 2 + sizeBorder,
                    startPointField - 2 * sizeBorder, startPointField - 2 * sizeBorder);
        }
        if (flag == 1) {
            g.setColor(Color.GREEN);
            g.fillOval(startPointField + x * (sizeCell + sizeBorder) + sizeBorder * 2,
                    2 * startPointField + y * (sizeCell + sizeBorder) + sizeBorder * 2,
                    sizeCell - sizeBorder * 4, sizeCell - sizeBorder * 4);
            g.setColor(Color.DARK_GRAY);
            g.fillOval(startPointField + x * (sizeCell + sizeBorder) + sizeBorder * 4,
                    2 * startPointField + y * (sizeCell + sizeBorder) + sizeBorder * 4,
                    sizeCell - sizeBorder * 8, sizeCell - sizeBorder * 8);
        }
    }

    private void drawX(Graphics g, int x, int y, int flag) {
        if (flag == 0) {
            g.setColor(Color.RED);
            g.fillRect(sizeAllField - 2 * sizeBorder, startPointField - sizeBorder,
                    startPointField, sizeBorder * 2);
            g.fillRect(sizeAllField + startPointField / 2 - 3 * sizeBorder, startPointField / 2,
                    sizeBorder * 2, startPointField);
        }
        if (flag == 1) {
            g.setColor(Color.RED);
            g.fillRect(startPointField + x * (sizeCell + sizeBorder) + sizeBorder * 2,
                    2 * startPointField + y * (sizeCell + sizeBorder) + sizeCell / 2 - sizeBorder * 2,
                    sizeCell - sizeBorder * 4, sizeBorder * 4);
            g.fillRect(startPointField + x * (sizeCell + sizeBorder) + sizeCell / 2 - sizeBorder * 2,
                    2 * startPointField + y * (sizeCell + sizeBorder) + sizeBorder * 2,
                    sizeBorder * 4, sizeCell - sizeBorder * 4);
        }
    }

    private void showGameOverMessage(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(startPointField, 2 * startPointField + sizeAllField / 3,
                   sizeAllField - sizeBorder, sizeAllField / 3 - sizeBorder);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Arial", Font.BOLD, getWidth() / 10));
        int y = getHeight() / 2 + sizeBorder * 8;

        switch (stateGameOver) {
            case EMPTY -> g.drawString("         DRAW", startPointField, y);
            case GAMER_1 -> {
                if (gameMode == MODE_VS_AI) {
                    g.drawString("   HUMAN WIN!", startPointField, y);
                } else {
                    g.drawString("  Gamer 1 WIN!", startPointField, y);
                }
            }
            case GAMER_2 -> {
                if (gameMode == MODE_VS_AI) {
                    g.drawString("         AI WIN", startPointField, y);
                } else {
                    g.drawString("  Gamer 2 WIN!", startPointField, y);
                }
            }
        }
    }

}
