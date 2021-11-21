package TicTacToe_Lada303;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameMap extends JPanel {

    public static final int MODE_VS_AI = 0;
    public static final int MODE_VS_HUMAN = 1;
    public static final int SIZE_BORDER = 5;
    private static final int DOT_GAMER1 = 1;
    private static final int DOT_GAMER2 = 2;
    private static final int DOT_EMPTY = 0;
    public static final Random RANDOM = new Random();

    private int stateGameOver;
    private int[][] field;
    private int gameMode;
    private int fieldSize=3;
    private int dotsToWin;
    private boolean isGameOver;
    private boolean isInitialized;
    private int countStep;
    private int playerNumTurn;

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
        countStep = 0;
        playerNumTurn = 1;
        field = new int[fieldSize][fieldSize];
        isInitialized = true;
        isGameOver = false;
        repaint();
    }

    private void update(MouseEvent e) {
        if (isGameOver || !isInitialized) {
            return;
        }
        if (!playerTurn(e)) {
            return;
        }
        if (isWin(DOT_GAMER1) || isDraw()) {
            return;
        }
        if (gameMode == MODE_VS_AI) {
            aiTurn();
            repaint();
        }
        if (gameMode == MODE_VS_HUMAN && !playerTurn(e)) {
            return;
        }
        if (isWin(DOT_GAMER2) || isDraw()) {
            return;
        }
    }

    private boolean playerTurn(MouseEvent e) {
        int cellX = e.getX();
        int cellY = e.getY();
        int startPointFieldX = getWidth()/10;
        int startPointFieldY = 2 * startPointFieldX;
        int sizeAllField = getWidth()-2*startPointFieldX+SIZE_BORDER;
        int sizeCell = sizeAllField/fieldSize;
        if (cellX <= startPointFieldX || cellX >= startPointFieldX+sizeAllField ||
                cellY <= startPointFieldY || cellY >= startPointFieldY+sizeAllField) { return false;}
        cellX = (cellX - startPointFieldX) / (sizeCell);
        cellY = (cellY - startPointFieldY) / (sizeCell);
        if (!isCellValid(cellY, cellX) || !isCellEmpty(cellY, cellX)) {
            return false;
        }

        if (playerNumTurn == 1) {
            field[cellY][cellX] = DOT_GAMER1;
        } else {
            field[cellY][cellX] = DOT_GAMER2;
        }

        if (gameMode == MODE_VS_HUMAN) {
            if (playerNumTurn == 1) {
                playerNumTurn = 2;
            } else {
                playerNumTurn = 1;
            }
        }
        countStep++;
        repaint();
        return true;
    }

    public boolean isWin(int symbol) {
        if (countStep >= dotsToWin *2-1) {
            if (checkWin(symbol, dotsToWin)) {
                stateGameOver = symbol;
                isGameOver = true;
                repaint();
                return true;
            }
        }
        return false;
    }

    public boolean isDraw() {
        if (countStep >= fieldSize * fieldSize) {
            stateGameOver = DOT_EMPTY;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    private void aiTurn() {
        countStep++;
        if (scanField(DOT_GAMER2, dotsToWin)) return;        // проверка выигрыша компа
        if (scanField(DOT_GAMER1, dotsToWin)) return;    // проверка выигрыша игрока на след ходу
        if (scanField(DOT_GAMER2, dotsToWin - 1)) return;
        if (scanField(DOT_GAMER1, dotsToWin - 1)) return;
        if (scanField(DOT_GAMER2, dotsToWin - 2)) return;
        if (scanField(DOT_GAMER1, dotsToWin - 2)) return;
        aiTurnEasy();
    }

    private void aiTurnEasy() {
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSize);
            y = RANDOM.nextInt(fieldSize);
        } while (!isCellEmpty(y, x));
        field[y][x] = DOT_GAMER2;
    }

    private boolean scanField(int dot, int length) {
        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                if (isCellEmpty(y, x)) {                // поставим фишку в каждую клетку поля по очереди
                    field[y][x] = dot;
                    if (checkWin(dot, length)) {
                        if (dot == DOT_GAMER2) return true;    // если комп выигрывает, то оставляем
                        if (dot == DOT_GAMER1) {
                            field[y][x] = DOT_GAMER2;            // Если выигрывает игрок ставим туда 0
                            return true;
                        }
                    }
                    field[y][x] = DOT_EMPTY;            // если никто ничего, то возвращаем как было
                }
            }
        }
        return false;
    }

    private boolean checkWin(int dot, int length) {
        for (int y = 0; y < fieldSize; y++) {            // проверяем всё поле
            for (int x = 0; x < fieldSize; x++) {
                if (checkLine(x, y, 1, 1, length, dot)) return true;    // проверка по диагонали +х +у
                if (checkLine(x, y, 1, -1, length, dot)) return true;   // проверка по диагонали +х -у
                if (checkLine(x, y, 1, 0, length, dot)) return true;    // проверка  по +х
                if (checkLine(x, y, 0, 1, length, dot)) return true;    // проверка линию по +у
            }
        }
        return false;
    }

    // проверка линии
    private boolean checkLine(int x, int y, int incrementX, int incrementY, int len, int dot) {
        int endXLine = x + (len - 1) * incrementX;            // конец линии по Х
        int endYLine = y + (len - 1) * incrementY;            // конец по У
        if (!isCellValid(endYLine, endXLine)) return false;    // Выход линии за пределы
        for (int i = 0; i < len; i++) {                    // идем по линии
            if (field[y + i * incrementY][x + i * incrementX] != dot) return false;    // символы одинаковые?
        }
        return true;
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
        int startPointFieldX = getHeight()/10;
        int startPointFieldY = startPointFieldX * 2;
        int sizeAllField = getWidth()-2*startPointFieldX+SIZE_BORDER;
        int sizeCell = sizeAllField/fieldSize - SIZE_BORDER;

        g.setFont(new Font("Arial", Font.BOLD, getWidth()/10));
        if (gameMode == MODE_VS_AI) {
            g.drawString("Player vs AI", startPointFieldX, startPointFieldX + getWidth()/20);
        } else {
            g.drawString("Player " + playerNumTurn + " turn", startPointFieldX, startPointFieldX + getWidth() / 20);
            if (playerNumTurn == 1) {
                g.setColor(Color.GREEN);
                g.fillOval(sizeAllField - 2 * SIZE_BORDER, startPointFieldX / 2,
                        startPointFieldX, startPointFieldX);
            } else {
                g.setColor(Color.RED);
                g.fillRect(sizeAllField - 2 * SIZE_BORDER, startPointFieldX / 2,
                        startPointFieldX, startPointFieldX);
            }
        }

        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                g.fillRect(startPointFieldX + j * (sizeCell+ SIZE_BORDER),
                           startPointFieldY + i * (sizeCell+ SIZE_BORDER),
                              sizeCell, sizeCell);
            }
        }

        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                if (isCellEmpty(y, x)) {
                    continue;
                }
                if (field[y][x] == DOT_GAMER1) {
                    g.setColor(Color.GREEN);
                    g.fillOval(startPointFieldX + x * (sizeCell + SIZE_BORDER) + SIZE_BORDER * 2,
                               startPointFieldY + y * (sizeCell + SIZE_BORDER) + SIZE_BORDER * 2,
                            sizeCell - SIZE_BORDER * 4, sizeCell - SIZE_BORDER * 4);
                } else {
                    g.setColor(Color.RED);
                    g.fillRect(startPointFieldX + x * (sizeCell + SIZE_BORDER) + SIZE_BORDER * 2,
                               startPointFieldY + y * (sizeCell + SIZE_BORDER) + SIZE_BORDER * 2,
                            sizeCell - SIZE_BORDER * 4, sizeCell - SIZE_BORDER * 4);
                }
            }
        }
        if (isGameOver) {
            showGameOverMessage(g);
        }

    }

    private boolean isCellValid(int y, int x) {
        return x >= 0 && y >= 0 && x < fieldSize && y < fieldSize;
    }

    private boolean isCellEmpty(int y, int x) {
        return field[y][x] == DOT_EMPTY;
    }

    private void showGameOverMessage(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, getHeight() / 2 - 60, getWidth(), 120);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Arial", Font.BOLD, getWidth()/10));
        int x = getWidth() / 4;
        int y = getHeight() / 2 + getWidth()/20;
        switch (stateGameOver) {
            case DOT_EMPTY -> g.drawString("   DRAW", x, y);
            case DOT_GAMER1 -> {
                if (gameMode == MODE_VS_AI) {
                    g.drawString("HUMAN WIN!", x, y);
                } else {
                    g.drawString("Gamer 1 WIN!", x - getWidth() / 20, y);
                }
            }
            case DOT_GAMER2 -> {
                if (gameMode == MODE_VS_AI) {
                    g.drawString("  AI WIN", x, y);
                } else {
                    g.drawString("Gamer 2 WIN!", x - getWidth() / 20, y);
                }
            }
        }
    }
}
