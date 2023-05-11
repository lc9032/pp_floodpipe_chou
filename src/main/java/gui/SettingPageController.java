package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * Class for the setting page.
 *
 * @author LiChieh Chou
 */
public class SettingPageController {

    /**
     * the Slider for the column value
     */
    @FXML
    private Slider colSlider;

    /**
     * the Slider for the row value
     */
    @FXML
    private Slider rowSlider;

    /**
     * the Slider for the percentage of the wall
     */
    @FXML
    private Slider wallSlider;

    /**
     * the Slider for the game speed
     */
    @FXML
    private Slider speedSlider;

    /**
     * the checkbox to select to enable the overflow mode or not
     */
    @FXML
    private CheckBox omClickBox;

    /**
     * the Label beside the colSlider to show the "Game Restart Required" message if the value is changed
     */
    @FXML
    private Label labelCol;

    /**
     * the Label beside the rowSlider to show the "Game Restart Required" message if the value is changed
     */
    @FXML
    private Label labelRow;

    /**
     * the Label beside the speedSlider to show the "Game Restart Required" message if the value is changed
     */
    @FXML
    private Label labelSpeed;

    /**
     * the Label beside the wallSlider to show the "Game Restart Required" message if the value is changed
     */
    @FXML
    private Label labelWall;

    @FXML
    private Label labelPercentageVal;

    /**
     * the Label beside the omClickBox to show the "Game Restart Required" message if the value is changed
     */
    @FXML
    private Label labelOverflow;

    /**
     * the Button for leaving the setting page without saving
     */
    @FXML
    private Button leaveBtn;

    /**
     * the Button for leaving the setting page with saving the settings and restarting the game if necessary
     */
    @FXML
    private Button saveBtn;

    /**
     * the Controller of the user interface
     */
    private UserInterfaceController usrController;

    /**
     * The size(columns, rows) of the game
     */
    private int columns, rows;

    /**
     *the maximum percentage of inserted wall pieces
     */
    private int percentageOfWall;

    /**
     *the animation speed
     */
    private int gameSpeed;

    /**
     * The Overflow mode
     */
    private boolean overflowMode;

    public SettingPageController() {
    }

    /**
     * to set reference to controller
     *
     * @param usrController the user interface controller
     * @param columns The size(columns) of the game field
     * @param rows The size(rows) of the game field
     * @param percentageOfWall the maximum percentage of inserted wall pieces
     * @param gameSpeed the animation speed
     * @param overflowMode The Overflow mode
     */
    public void setReferenceToController(UserInterfaceController usrController,
                                         int columns, int rows, int percentageOfWall, int gameSpeed, boolean overflowMode){
        this.usrController = usrController;
        this.columns = columns;
        this.rows = rows;
        this.percentageOfWall = percentageOfWall;
        this.gameSpeed = gameSpeed;
        this.overflowMode = overflowMode;
        colSlider.setValue(columns);
        rowSlider.setValue(rows);
        wallSlider.setValue(percentageOfWall);
        speedSlider.setValue(gameSpeed);
        omClickBox.setSelected(overflowMode);
        labelPercentageVal.setText((int) wallSlider.getValue() + "%");
    }

    /**
     * The handle of which the slides value is changed
     */
    @FXML
    void onSlideMouseDragged(MouseEvent event) {
        checkValuesChanged();
    }

    /**
     * The handle of which the slides value is changed
     */
    @FXML
    void onSlideMouseReleased(MouseEvent event) {
        checkValuesChanged();
    }

    /**
     * The handle of which the slides value is changed
     */
    @FXML
    void onSlideKeyReleased(KeyEvent event) {
        checkValuesChanged();
    }

    /**
     * The handle of which the checkbox is clicked
     */
    @FXML
    void onClickedBoxAction(ActionEvent event) {
        checkValuesChanged();
    }

    /**
     * to check if there is any of value change when there is an action on the sliders and checkbox.
     */
    private void checkValuesChanged(){
        labelPercentageVal.setText((int) wallSlider.getValue() + "%");

        if ((usrController.game != null)&&(!usrController.editorMode)) {
            if ((int) colSlider.getValue() == columns)
                labelCol.setText("");
            else
                labelCol.setText("⚠ Game Restart Required");

            if ((int) rowSlider.getValue() == rows)
                labelRow.setText("");
            else
                labelRow.setText("⚠ Game Restart Required");

            if ((int) wallSlider.getValue() == percentageOfWall){
                labelWall.setText("");
            }
            else{
                labelWall.setText("⚠ Game Restart Required");
            }
            if (omClickBox.isSelected() == overflowMode)
                labelOverflow.setText("");
            else
                labelOverflow.setText("⚠ Game Restart Required");

            if ((Objects.equals(labelCol.getText(), ""))
                    && (Objects.equals(labelRow.getText(), ""))
                    && (Objects.equals(labelWall.getText(), ""))
                    && (Objects.equals(labelOverflow.getText(), "")))
                saveBtn.setText("Save Settings");
            else
                saveBtn.setText("Save & Restart the Game");
        }
    }

    /**
     * The handle of which the leave button is clicked
     */
    @FXML
    void onFinishBtnClick(ActionEvent event) {

        if (event.getSource() == saveBtn) {
            columns = (int) colSlider.getValue();
            rows = (int) rowSlider.getValue();
            percentageOfWall = (int)wallSlider.getValue();
            gameSpeed = (int)speedSlider.getValue();
            overflowMode = omClickBox.isSelected();
            usrController.setSettings(columns, rows, percentageOfWall, gameSpeed, overflowMode);
            if (Objects.equals(saveBtn.getText(), "Save & Restart the Game"))
                usrController.createNewGame();
        }

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }
}