package gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main class for the user interface.
 *
 * @author LiChieh Chou
 */
public class UserInterfaceController implements Initializable {

    /**
     * the BorderPane that contain all buttons, labels, images
     */
    @FXML
    private BorderPane borderPane;

    /**
     * the Pane in the center of the borderPane, which contains a GridPane for the pipe images
     */
    @FXML
    private Pane centerPane;

    /**
     * The "Start" Button
     */
    @FXML
    private Button startBtn;

    /**
     * The menu item "Exit"
     */
    @FXML
    private MenuItem menuItemExit;

    /**
     * The menu item "Load"
     */
    @FXML
    private MenuItem menuItemLoad;

    /**
     * The menu item "New Game"
     */
    @FXML
    private MenuItem menuItemNewGame;

    /**
     * The menu item "Editor"
     */
    @FXML
    private MenuItem menuItemEditor;

    /**
     * The menu item "Save"
     */
    @FXML
    private MenuItem menuItemSave;

    /**
     * The menu item "Setting"
     */
    @FXML
    private MenuItem menuItemSetting;

    /**
     * For creating a new game
     */
    protected Logic game;

    /**
     * editor mode
     */
    protected boolean editorMode;

    /**
     * The size(columns, rows) of the game field
     */
    private int columns,rows;

    /**
     * the maximum percentage of inserted wall pieces
     */
    private int percentageOfWall;

    /**
     * the animation speed
     */
    private int gameSpeed;

    /**
     * The Overflow mode
     */
    private boolean overflowMode;

    /**
     * the timeline for animation
     */
    private Timeline timeline;

    /**
     * To set the settings of the game
     * Which the settings including the columns, rows, percentage of the wall pieces, game speed and overflow mode
     *
     * @param columns The size(columns) of the game field
     * @param rows The size(rows) of the game field
     * @param percentageOfWall the maximum percentage of inserted wall pieces
     * @param gameSpeed the animation speed
     * @param overflowMode The Overflow mode
     */
    public void setSettings(int columns, int rows, int percentageOfWall, int gameSpeed, boolean overflowMode){
        this.columns = columns;
        this.rows = rows;
        this.percentageOfWall = percentageOfWall;
        this.gameSpeed = gameSpeed;
        this.overflowMode = overflowMode;

        if(editorMode){
            game.setOverflowMode(overflowMode);
            game.resize(columns,rows);
            if(game.getPlayField().getSourcePos() == null)
                menuItemSave.setDisable(true);
        }else{
            setTimeLineRate(gameSpeed);
        }
    }

    /**
     * set the size of the play field
     *
     * @param columns the new columns value
     * @param rows the new rows value
     */
    public void setSize(int columns, int rows){
        this.columns = columns;
        this.rows = rows;
    }

    /**
     * Initialize all setting of the game.
     * the default size is 5x5, speed is 5 and overflow mode is "off".
     *
     * @param location  probably not used
     * @param resources probably not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuItemSave.setDisable(true);
        columns = 5;
        rows = 5;
        percentageOfWall = 20;
        gameSpeed = 11;
        overflowMode = false;
        startBtn.prefHeightProperty().bind(centerPane.heightProperty());
        startBtn.prefWidthProperty().bind(centerPane.widthProperty());
        createTimeLine();
        setTimeLineRate(gameSpeed);
    }

    /**
     * The callback function when the "Start" button is clicked.
     * Will call the "createNewGame" function to create a new game
     */
    @FXML
    private void onStartButtonClick() {
        startBtn.setVisible(false);
        menuItemSave.setDisable(false);
        createNewGame();
    }

    /**
     * To create a new game
     */
    protected void createNewGame(){
        StackPane[][] pipeStackPanes = createPlayField();

        game = new Logic(new JavaFXGUI(this, pipeStackPanes), columns, rows, percentageOfWall, overflowMode);

        setTimeLineRate(gameSpeed);
        playTimeLine();
    }

    /**
     Creates the play field by creating a GridPane and a two-dimensional array of StackPanes.

     @return a two-dimensional array of StackPanes representing the positions in the play field
     */
    protected StackPane[][] createPlayField(){
        StackPane[][] pipeStackPanes;

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.prefHeightProperty().bind(centerPane.heightProperty());
        gridPane.prefWidthProperty().bind(centerPane.widthProperty());

        centerPane.getChildren().clear();
        centerPane.getChildren().add(gridPane);

        pipeStackPanes = new StackPane[columns][rows];
        for (int i = 0;i < columns;i++){
            for(int j = 0;j < rows;j++) {
                pipeStackPanes[i][j] = new StackPane();
                pipeStackPanes[i][j].setMinSize(30,30);
                pipeStackPanes[i][j].prefWidthProperty().bind(Bindings.min(gridPane.widthProperty().divide(columns),
                        gridPane.heightProperty().divide(rows)));
                pipeStackPanes[i][j].prefHeightProperty().bind(Bindings.min(gridPane.widthProperty().divide(columns),
                        gridPane.heightProperty().divide(rows)));
                pipeStackPanes[i][j].setOnMousePressed(this::onPipePress);
                pipeStackPanes[i][j].setOnDragDropped(this::onPipeDrop);
                pipeStackPanes[i][j].setOnDragOver(this::onPipeDragOver);

                gridPane.add(pipeStackPanes[i][j],i,j);
            }
        }
        return pipeStackPanes;
    }

    /**
     * The handle of the "save" button
     */
    @FXML
    void onSaveBtnClicked() {
        Stage stage = (Stage) menuItemSave.getParentPopup().getOwnerWindow();

        if ((game != null)){
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                                                            "Json files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);

            File gameSaveDir = new File(System.getProperty("user.dir") +"/game_saves");
            if(!gameSaveDir.exists()){
                gameSaveDir.mkdirs();
            }
            fileChooser.setInitialDirectory(gameSaveDir);

            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                if(game != null) game.saveGame(file);
            }
        }

    }

    /**
     * The handle of the menu item "load"
     */
    @FXML
    void onLoadBtnClicked() throws Exception {
        Stage stage = (Stage) menuItemLoad.getParentPopup().getOwnerWindow();

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Json files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("Other files (*.*)", "*.*")
        );

        File gameSaveDir = new File(System.getProperty("user.dir") +"/game_saves");
        if(!gameSaveDir.exists()){
            gameSaveDir.mkdirs();
        }

        fileChooser.setInitialDirectory(gameSaveDir);

        File file = fileChooser.showOpenDialog(stage);

        if(file != null) {
            if (game == null) createNewGame();

            try{
                game.loadGame(file);
                if(editorMode)
                    game.fillAllConnectedPipes();
            } catch(Exception e){
                if(!editorMode) {
                    game = null;
                    centerPane.getChildren().clear();
                    centerPane.getChildren().add(startBtn);
                    startBtn.setVisible(true);
                    menuItemSave.setDisable(true);
                }
                String msg = e.getMessage();
                Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
                alert.showAndWait();
            }
            menuItemSave.setDisable(false);
        }
    }

    /**
     * The handle of the menu item "Editor"
     */
    @FXML
    void onEditorBtnClick() {
        if(!editorMode){
            editorMode = true;

            if(game == null) {
                createNewGame();
                game.newPlayField();
                menuItemSave.setDisable(true);
            }

            Pane leftPane = new Pane();
            borderPane.setLeft(leftPane);
            BorderPane.setMargin(leftPane, new Insets(10,10,10,10));
            createPipeSelectingArea(leftPane);

            Pane rightPane = new Pane();
            borderPane.setRight(rightPane);
            BorderPane.setMargin(rightPane, new Insets(5,5,5,5));
            createEditorModeButtons(rightPane);

            menuItemEditor.setText("Game Mode");
            menuItemNewGame.setDisable(true);

            game.fillAllConnectedPipes();
            stopTimeLine();
        }else{
            if (game.getPlayField().getSourcePos() == null)
            {
                String msg =    "There is no source on the field.\n\n" +
                                "Please insert a source onto the field.\n\n";
                Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
                alert.showAndWait();
            } else {
                switchFromEditorToGameMode();
                game.getPlayField().clearAllPipesFilledState();
                game.displayPlayField();
                playTimeLine();
                menuItemSave.setDisable(false);
            }
        }
    }

    /**
     Switches the application from editor mode to game mode.
     This method updates the editorMode flag to false,
     removes the editor controls from the UI, and enables the New Game menu item.
     */
    private void switchFromEditorToGameMode(){
        editorMode = false;
        borderPane.setLeft(null);
        borderPane.setRight(null);
        menuItemEditor.setText("Editor Mode");
        menuItemNewGame.setDisable(false);
    }

    /**
     * To create the pipe selecting area,
     * which includes a source image, wall, end-pipe, curve-pipe, straight-pipe and t-pipe.
     * The user can drag an image and drop it onto the play field.
     */
    private void createPipeSelectingArea(Pane pane){
        VBox vBox = new VBox();
        vBox.setStyle("-fx-border-color: black");
        for(int i = 0;i < 6;i++){
            StackPane stackPane = new StackPane();
            ImageView img = new ImageView();

            stackPane.setOnDragDetected(this::onPipeDrag);
            stackPane.getChildren().add(img);
            stackPane.setStyle("-fx-border-color: black");

            img.fitHeightProperty().bind(pane.heightProperty().divide(6).subtract(3));
            img.setPreserveRatio(true);

            String path = switch (i) {
                case 0 -> PipeImage.SOURCE.getFilePath();
                case 1 -> PipeImage.WALL.getFilePath();
                case 2 -> PipeImage.END_PIPE.getFilePath();
                case 3 -> PipeImage.CURVE_PIPE.getFilePath();
                case 4 -> PipeImage.STRAIGHT_PIPE.getFilePath();
                case 5 -> PipeImage.T_PIPE.getFilePath();
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
            img.setImage(new Image(path));

            vBox.getChildren().add(stackPane);
        }
        pane.getChildren().add(vBox);
    }

    /**
     * Creates two buttons for the editor mode and adds them to the given pane.
     *
     * @param pane the pane to add the buttons to
     */
    private void createEditorModeButtons(Pane pane){
        VBox vBox = new VBox();
        Button newPlayFieldBtn = new Button();
        Button mixBtn = new Button();

        vBox.prefHeightProperty().bind(pane.heightProperty());
        vBox.setAlignment(Pos.CENTER);
        VBox.setMargin(newPlayFieldBtn, new Insets(5,5,5,5));
        VBox.setMargin(mixBtn, new Insets(5,5,5,5));

        newPlayFieldBtn.setText("Empty\nPlayField");
        mixBtn.setText("Mix");

        newPlayFieldBtn.prefHeightProperty().bind(pane.heightProperty().divide(8));
        newPlayFieldBtn.prefWidthProperty().bind(pane.heightProperty().divide(8));
        mixBtn.prefHeightProperty().bind(pane.heightProperty().divide(8));
        mixBtn.prefWidthProperty().bind(pane.heightProperty().divide(8));

        newPlayFieldBtn.setOnAction(this::onNewPlayFieldBtnClick);
        mixBtn.setOnAction(this::onMixBtnClick);

        vBox.getChildren().add(newPlayFieldBtn);
        vBox.getChildren().add(mixBtn);

        pane.getChildren().add(vBox);
    }

    /**
     * The handle of the menu item "settings".
     * in which to open the setting page and to pass the current setting to the setting page's controller.
     */
    @FXML
    void onSettingBtnClick() throws IOException {
        Stage stage;
        Parent root;

        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingPage.fxml"));
        stage.setTitle("Settings");
        root = loader.load();

        SettingPageController settingPageController = loader.getController();
        settingPageController.setReferenceToController(this, columns, rows, percentageOfWall, gameSpeed, overflowMode);

        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(menuItemSetting.getParentPopup().getOwnerWindow());

        stage.showAndWait();
    }

    /**
     * The handle of the menu item "Exit"
     * in which to close the game
     */
    @FXML
    void onExitBtnClicked() {
        Stage stage = (Stage) menuItemExit.getParentPopup().getOwnerWindow();
        stage.close();
    }

    /**
     * the callback function when the New Play Field button is clicked
     */
    @FXML
    void onNewPlayFieldBtnClick(ActionEvent event) {
        menuItemSave.setDisable(true);
        game.newPlayField();
    }

    /**
     * the callback function when the Mix button is clicked
     */
    @FXML
    void onMixBtnClick(ActionEvent event) {
        game.mix();
    }

    /**
     * create a new timeline for the animation of the game
     */
    private void createTimeLine(){
        timeline = new Timeline(new KeyFrame(Duration.seconds(1),ev -> {
            if (game != null)
                game.fillNextPipe();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * start playing the timeline
     */
    public void playTimeLine(){
        timeline.play();
    }

    /**
     * stop playing the timeline
     */
    public void stopTimeLine(){
        timeline.stop();
    }

    /**
     * to set the rate of the timeline
     *
     * @param speed the animation speed
     */
    public void setTimeLineRate(int speed){
        timeline.setRate((double)speed / 10);
    }

    /**
     * The handle of Clicking the pipes
     * in which to send the column and row's index to Logic
     */
    @FXML
    void onPipePress(MouseEvent event) {
        int column = GridPane.getColumnIndex((Node) event.getSource());
        int row = GridPane.getRowIndex((Node) event.getSource());
        boolean direction;

        if (event.getButton() == MouseButton.PRIMARY) direction = true;
        else if (event.getButton() == MouseButton.SECONDARY) direction = false;
        else return;

        game.rotated(new Position(column,row), direction);
        if(editorMode) game.fillAllConnectedPipes();
    }

    /**
     * the callback function when a pipe in the selecting area is dragged
     */
    @FXML
    void onPipeDrag(MouseEvent event) {
        StackPane sourcePane = (StackPane)event.getSource();
        VBox vB = (VBox) sourcePane.getParent();

        Dragboard db = sourcePane.startDragAndDrop(TransferMode.ANY);

        ClipboardContent cb = new ClipboardContent();
        switch(vB.getChildren().indexOf(sourcePane)){
            case 0 -> cb.putString(PipeImage.SOURCE.name());
            case 1 -> cb.putString(PipeImage.WALL.name());
            case 2 -> cb.putString(PipeImage.END_PIPE.name());
            case 3 -> cb.putString(PipeImage.CURVE_PIPE.name());
            case 4 -> cb.putString(PipeImage.STRAIGHT_PIPE.name());
            case 5 -> cb.putString(PipeImage.T_PIPE.name());
        }
        db.setContent(cb);

        event.consume();
    }

    /**
     * the handle of a pipe is dropped
     *
     * @param dragEvent
     */
    @FXML
    void onPipeDrop(DragEvent dragEvent) {
        int column = GridPane.getColumnIndex((Node) dragEvent.getSource());
        int row = GridPane.getRowIndex((Node) dragEvent.getSource());
        String pipeStr = dragEvent.getDragboard().getString();

        Position pos = new Position(column, row);

        if(pipeStr.equals(PipeImage.SOURCE.name())){
            if(!game.getPlayField().getField(pos).getPipeType().equals(PipeType.WALL)) {
                game.updateSourcePos(pos);
                menuItemSave.setDisable(false);
            }
        }
        else {
            Pipe pipe = game.getPlayField().getField(pos);
            pipe.setRotation(Rotation.ZERO);
            if (pipeStr.equals(PipeImage.WALL.name())){
                pipe.setPipeType(PipeType.WALL);
                if(game.getPlayField().getSourcePos().equals(pos)) {
                    game.updateSourcePos(null);
                    menuItemSave.setDisable(true);
                }
            } else if (pipeStr.equals(PipeImage.END_PIPE.name())) pipe.setPipeType(PipeType.END_PIPE);
            else if (pipeStr.equals(PipeImage.CURVE_PIPE.name())) pipe.setPipeType(PipeType.CURVE_PIPE);
            else if (pipeStr.equals(PipeImage.STRAIGHT_PIPE.name())) pipe.setPipeType(PipeType.STRAIGHT_PIPE);
            else if (pipeStr.equals(PipeImage.T_PIPE.name())) pipe.setPipeType(PipeType.T_PIPE);
            game.setPipe(pos, pipe);
        }
        game.fillAllConnectedPipes();
    }

    /**
     * the handle of a pipe is dragging
     *
     * @param dragEvent
     */
    @FXML
    void onPipeDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasString()){
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }
}