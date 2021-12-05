package TicTacToe_Lada303;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SettingsGame extends JFrame {

    private static final int MIN_WIN_LENGTH = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 8;

    private final JRadioButton humanVsAi;
    private final JRadioButton humanVsHuman;
    private final JSlider sliderWinLength;
    private final JSlider sliderFieldSize;
    private final GameWindow gameWindow;


    public SettingsGame(GameWindow gameWindow) throws HeadlessException {
        this.gameWindow = gameWindow;
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(gameWindow.getWidth()/2, gameWindow.getHeight());
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(14,1));
        panel.add(new JLabel(" "));
        panel.add(new JLabel(" "));
        panel.add(new JLabel(" Choose game mode:"));
        ButtonGroup gameMode = new ButtonGroup();
        humanVsAi = new JRadioButton(" Human versus AI", true);
        humanVsHuman = new JRadioButton(" Human versus human");
        gameMode.add(humanVsAi);
        gameMode.add(humanVsHuman);
        panel.add(humanVsAi);
        panel.add(humanVsHuman);
        panel.add(new JLabel(""));

        JLabel labelFieldSize = new JLabel(" Choose field size: " + MIN_FIELD_SIZE);
        sliderFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        sliderFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = sliderFieldSize.getValue();
                labelFieldSize.setText(" Choose field size: " + currentValue);
                sliderWinLength.setMaximum(currentValue);
                switch (currentValue) {
                    case 3 -> sliderWinLength.setMinimum(3);
                    case 4,5 -> sliderWinLength.setMinimum(4);
                        default -> sliderWinLength.setMinimum(currentValue - 2);
                }
            }
        });
        panel.add(labelFieldSize);
        panel.add(sliderFieldSize);
        panel.add(new JLabel(""));

        JLabel labelWinLength = new JLabel(" Choose win length: " + MIN_WIN_LENGTH);
        sliderWinLength = new JSlider(MIN_WIN_LENGTH, MIN_WIN_LENGTH, MIN_WIN_LENGTH);
        sliderWinLength.addChangeListener(
               e -> labelWinLength.setText(" Choose win length: " + sliderWinLength.getValue())
        );
        panel.add(labelWinLength);
        panel.add(sliderWinLength);
        panel.add(new JLabel(""));

        JButton btnSave = new JButton("Save new settings");
        btnSave.addActionListener(e -> saveSettings());
        JButton btnCancel = new JButton("Return default settings");
        btnCancel.addActionListener(e -> defaultSettings());
        panel.add(btnSave);
        panel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
        addComponentListener (new ComponentAdapter() {
            public void componentMoved (ComponentEvent e) {
                setLocation(gameWindow.getX()+gameWindow.getWidth(),gameWindow.getY());
            }
        });
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
    void changeLocation() {
        setLocation(gameWindow.getX()+gameWindow.getWidth(),gameWindow.getY());
    }
}
