package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import logic.GUIConnector;
import logic.Pipe;
import logic.PipeType;
import logic.Position;

/**
 * This class is responsible for changing the gui when the logic deems it necessary.
 * Created by the gui and then passed as a parameter into the logic.
 *
 * @author LiChieh Chou
 */
public class JavaFXGUI implements GUIConnector{
    /**
     * the Controller of the user interface
     */
    private UserInterfaceController usrController;

    /**
     * the StackPane array used to put the pipes' images
     */
    private StackPane[][] pipeStackPanes;

    /**
     * The constructor. Gets passed all components of the gui that may change
     * due to actions in the logic.
     *
     * @param usrController the Controller of the user interface
     * @param pipeStackPanes the StackPane for putting pipes' images
     */
    public JavaFXGUI(UserInterfaceController usrController, StackPane[][] pipeStackPanes){
        this.usrController = usrController;
        this.pipeStackPanes = pipeStackPanes;
    }

    @Override
    public void displayPipe(Position pos, Pipe pipe, boolean isSource) {
        int x = pos.getX();
        int y = pos.getY();
        boolean isFilled = pipe.getIsFilled();

        ImageView imgView = new ImageView();
        imgView.fitWidthProperty().bind(pipeStackPanes[x][y].widthProperty());
        imgView.fitHeightProperty().bind(pipeStackPanes[x][y].heightProperty());
        imgView.setPreserveRatio(true);

        String path = switch (pipe.getPipeType()) {
            case END_PIPE ->        PipeImage.END_PIPE.getFilePath(isFilled);
            case CURVE_PIPE ->      PipeImage.CURVE_PIPE.getFilePath(isFilled);
            case STRAIGHT_PIPE ->   PipeImage.STRAIGHT_PIPE.getFilePath(isFilled);
            case T_PIPE ->          PipeImage.T_PIPE.getFilePath(isFilled);
            default ->              PipeImage.WALL.getFilePath();
        };

        switch (pipe.getRotation().getValue()) {
            case 90 ->  imgView.setRotate(90);
            case 180 -> imgView.setRotate(180);
            case 270 -> imgView.setRotate(270);
            default ->  imgView.setRotate(0);
        }

        Image image = new Image(path);

        imgView.setImage(image);
        pipeStackPanes[x][y].getChildren().clear();
        pipeStackPanes[x][y].getChildren().add(imgView);

        if ((isSource) && (pipe.getPipeType() != PipeType.WALL)) {
            ImageView wheelImg = new ImageView();
            wheelImg.setImage(new Image(PipeImage.SOURCE.getFilePath()));
            wheelImg.fitWidthProperty().bind(pipeStackPanes[x][y].widthProperty());
            wheelImg.fitHeightProperty().bind(pipeStackPanes[x][y].heightProperty());
            wheelImg.setPreserveRatio(true);
            pipeStackPanes[x][y].getChildren().add(wheelImg);
        }
    }

    @Override
    public void onGameEnd(int turns) {
        if(!usrController.editorMode){
            usrController.stopTimeLine();

            String msg = "Congratulation! You Win!!\n\n" +
                         "You used " + turns + " turns totally\n\n" +
                         "Do you want to start a new game?";

            Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.YES);
            alert.setTitle("Win");
            alert.setOnHidden(evt -> usrController.createNewGame());
            alert.show();
        }
    }

    @Override
    public void resize(int columns, int rows){
        usrController.setSize(columns, rows);
        pipeStackPanes = usrController.createPlayField();
    }
}
