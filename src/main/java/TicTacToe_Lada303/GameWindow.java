package TicTacToe_Lada303;

import javax.swing.*;
import java.awt.*;


public class GameWindow extends JFrame {

    private static final int WINDOW_SIZE = 500;
    private int modeGame;
    private int fieldSize;
    private int winLength;
    private final GameMap gameMap;

    public GameWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_SIZE - WINDOW_SIZE/5, WINDOW_SIZE);
        setTitle("TioTacToe Game");
        setLocation(200,100);
        setResizable(false);

        JButton btnSettings = new JButton("Game Settings");
        JButton btnStartGame = new JButton("Start New Game");
        JButton btnExitGame = new JButton("Exit Game");
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(1,3));
        panelButtons.add(btnSettings);
        panelButtons.add(btnStartGame);
        panelButtons.add(btnExitGame);
        add(panelButtons, BorderLayout.SOUTH);

        gameMap = new GameMap();
        add(gameMap, BorderLayout.CENTER);
        SettingsGame settingsGame = new SettingsGame(this);
        settingsGame.defaultSettings();

        btnSettings.addActionListener((e) -> settingsGame.setVisible(!settingsGame.isVisible()));
        btnStartGame.addActionListener(e -> gameMap.startNewGame(this));
        btnExitGame.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public void setNewModeGame(int mode) {
        modeGame = mode;
    }
    public void setNewFieldSize(int fSize) {
        fieldSize = fSize;
    }
    public void setNewWinLength(int wLength) {
        winLength = wLength;
    }

    public int getModeGame() {
        return modeGame;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public int getWinLength() {
        return winLength;
    }
}
