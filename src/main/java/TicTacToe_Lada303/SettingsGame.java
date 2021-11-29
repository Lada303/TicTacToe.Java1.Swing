package TicTacToe_Lada303;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SettingsGame extends JFrame {

    private static final int MIN_WIN_LENGTH = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;
    private static final String FIELD_SIZE_PREFIX = "Field size: ";
    private static final String WIN_LENGTH_PREFIX = "Win length: ";

    private final JRadioButton humanVsAi;
    private final JRadioButton humanVsHuman;
    private final JSlider sliderWinLength;
    private final JSlider sliderFieldSize;
    private final GameWindow gameWindow;


    public SettingsGame(GameWindow gameWindow) throws HeadlessException {
        this.gameWindow = gameWindow;
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(gameWindow.getWidth()/2, gameWindow.getHeight());
        setTitle("Settings");
        setLocation(gameWindow.getX()+gameWindow.getWidth(),gameWindow.getY());
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11,1));

        panel.add(new JLabel(" Choose game mode:"));
        ButtonGroup gameMode = new ButtonGroup();
        humanVsAi = new JRadioButton(" Human versus AI", true);
        humanVsHuman = new JRadioButton(" Human versus human");
        gameMode.add(humanVsAi);
        gameMode.add(humanVsHuman);
        panel.add(humanVsAi);
        panel.add(humanVsHuman);

        JLabel labelFieldSize = new JLabel(" "+FIELD_SIZE_PREFIX + MIN_FIELD_SIZE);
        JLabel labelWinLength = new JLabel(" "+WIN_LENGTH_PREFIX + MIN_WIN_LENGTH);
        sliderFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        sliderWinLength = new JSlider(MIN_WIN_LENGTH, MAX_FIELD_SIZE, MIN_WIN_LENGTH);
        sliderFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = sliderFieldSize.getValue();
                labelFieldSize.setText(" " + FIELD_SIZE_PREFIX + currentValue);
                sliderWinLength.setMaximum(currentValue);
            }
        });
        sliderWinLength.addChangeListener(
                e -> labelWinLength.setText(" " + WIN_LENGTH_PREFIX + sliderWinLength.getValue())
        );

        panel.add(new JLabel(" Choose field size:"));
        panel.add(labelFieldSize);
        panel.add(sliderFieldSize);
        panel.add(new JLabel(" Choose win length:"));
        panel.add(labelWinLength);
        panel.add(sliderWinLength);

        JButton btnSave = new JButton("Save new settings");
        btnSave.addActionListener(e -> saveSettings());
        JButton btnCancel = new JButton("Return default settings");
        btnCancel.addActionListener(e -> defaultSettings());

        panel.add(btnSave);
        panel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
    }

    private void saveSettings() {
        gameWindow.setNewModeGame(humanVsAi.isSelected() ? GameMap.MODE_VS_AI : GameMap.MODE_VS_HUMAN);
        gameWindow.setNewFieldSize(sliderFieldSize.getValue());
        gameWindow.setNewWinLength(sliderWinLength.getValue());
    }

     void defaultSettings() {
        gameWindow.setNewModeGame(GameMap.MODE_VS_AI);
        humanVsAi.setSelected(true);
        gameWindow.setNewFieldSize(MIN_FIELD_SIZE);
        sliderFieldSize.setValue(MIN_FIELD_SIZE);
        gameWindow.setNewWinLength(MIN_WIN_LENGTH);
        sliderWinLength.setValue(MIN_WIN_LENGTH);
    }
}
