package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

/**
 * this class contains the logic of FloodPipe game
 *
 * @author LiChieh Chou
 */
public class Logic {
    /**
     * Connection to the gui.
     */
    private GUIConnector gui;

    /**
     * The 2-dimensional field on which the player play.
     */
    private PlayField playField;

    /**
     * The total turns the player used to solve the pipe puzzle
     */
    private int turns;

    /**
     * Constructor for a game of flood pipe.
     * Initializes the field.
     *
     * @param gui  connection to the gui
     * @param columns   The size(columns) of the game field
     * @param rows   The size(rows) of the game field
     * @param percentageOfWall the percentage of wall pieces in the play field
     * @param overflowMode the overflow mode
     */
    public Logic(GUIConnector gui, int columns, int rows, int percentageOfWall, boolean overflowMode){
        this.gui = gui;

        turns = 0;
        playField = new PlayField(columns, rows, overflowMode);
        playField.generatePlayField(percentageOfWall);
        playField.mix();
        handleEndOfGame();
        displayPlayField();
    }

    /**
     * This Constructor is only for testing!!!
     * which receives a playing field including the layout in the form of a string with box-drawing characters.
     * The conversion into the intended logical game field structure must already take place in this.
     *
     * @param gui connection to the gui
     * @param board the layout in the form of a string with box-drawing characters
     */
    Logic(GUIConnector gui, String board, Position sourcePos){
        this.gui = gui;
        String[] boardParts = board.split("\n");
        int rows = boardParts.length;
        int columns = boardParts[0].length();

        playField = new PlayField(columns, rows, false);
        playField.setSourcePos(sourcePos);

        for(int i = 0;i < columns;i++) {
            for(int j = 0;j < rows;j++) {
                Pipe pipe = new Pipe();
                switch (boardParts[j].charAt(i)) {
                    case '╹', '╺', '╻', '╸' -> pipe.setPipeType(PipeType.END_PIPE);
                    case '┗', '┏', '┓', '┛' -> pipe.setPipeType(PipeType.CURVE_PIPE);
                    case '┃', '━' -> pipe.setPipeType(PipeType.STRAIGHT_PIPE);
                    case '┣', '┳', '┫', '┻' -> pipe.setPipeType(PipeType.T_PIPE);
                }
                switch (boardParts[j].charAt(i)) {
                    case '╹', '┗', '┃', '┣' -> pipe.setRotation(Rotation.ZERO);
                    case '╺', '┏', '━', '┳' -> pipe.setRotation(Rotation.NINETY);
                    case '╻', '┓', '┫' -> pipe.setRotation(Rotation.ONE_EIGHTY);
                    case '╸', '┛', '┻' -> pipe.setRotation(Rotation.TWO_SEVENTY);
                }
                playField.setField(new Position(i,j),pipe);
            }
        }
        displayPlayField();
    }

    /**
     * set the gui
     *
     * @param gui the gui to be set
     */
    public void setGui(GUIConnector gui) {
        this.gui = gui;
    }


    /**
     * get the gui
     *
     * @return the gui
     */
    public GUIConnector getGui() {
        return gui;
    }

    /**
     * set the play field
     *
     * @param playField the new play field to be set
     */
    public void setPlayField(PlayField playField) {
        this.playField = playField;
    }

    /**
     * get the play field
     *
     * @return the play field currently playing
     */
    public PlayField getPlayField() {
        return playField;
    }

    /**
     * enable/disable the overflow mode
     *
     * @param overflowMode the new overflow value
     */
    public void setOverflowMode(boolean overflowMode){
        playField.setOverflow(overflowMode);
        displayPlayField();
    }

    /**
     * When a pipe is clicked, the pipe need to be rotated
     *
     * @param pos     The Position of the pipe which the mouse clicked
     * @param direction  The player use the right mouse button or the left mouse button to click the pipe
     */
    public void rotated(Position pos, boolean direction){
        turns++;
        playField.rotation(pos, direction);
        clearUnConnectedPipesFilledState();
        this.handleEndOfGame();
        displayPipe(pos);
    }

    /**
     Sets a Pipe object at the given position in the playField array and displays it on the graphical user interface.
     @param pos The position in the playField array where the Pipe object should be set.
     @param pipe The Pipe object to be set at the given position in the playField array.
     */
    public void setPipe(Position pos,Pipe pipe){
        playField.setField(pos, pipe);
        displayPipe(pos);
    }

    /**
     * to update the source position
     *
     * @param pos the new source position
     */
    public void updateSourcePos(Position pos){
        Position oldPos = playField.getSourcePos();
        playField.setSourcePos(pos);
        if (oldPos != null) displayPipe(oldPos);
        if (pos != null) displayPipe(pos);
    }

    /**
     * This function is called by the timer, which will automatically fill the next pipe
     */
    public void fillNextPipe(){
        Position nextFillablePipePos = getPlayField().findNextFillablePipe();

        if(nextFillablePipePos != null)
            displayPipe(nextFillablePipePos);

        handleEndOfGame();
    }

    /**
     * determines if all pipes are reachable from the source and there is no open end
     * and initiates the response on the gui.
     */
    private void handleEndOfGame(){
        if(playField.checkAllPipesReachableFromSource() && playField.checkOpenEnd()){
            fillAllConnectedPipes();
            displayPlayField();
            gui.onGameEnd(turns);
        }
    }

    /**
     * Resize the play field
     *
     * @param columns   The new columns value
     * @param rows   The new rows value
     */
    public void resize(int columns,int rows){
        gui.resize(columns, rows);

        PlayField oldPlayField = playField;
        PlayField newPlayField = new PlayField(columns, rows, oldPlayField.getOverflow());
        setPlayField(newPlayField);
        newPlayField.setAllCellsAsWallPieces();

        for(int i = 0;((i < oldPlayField.getColumns()) && (i < columns));i++){
            for(int j = 0;((j < oldPlayField.getRows()) && (j < rows));j++){
                Position pos = new Position(i,j);
                newPlayField.setField(pos, oldPlayField.getField(pos));
            }
        }

        if (oldPlayField.getSourcePos() != null) {
            if ((oldPlayField.getSourcePos().getX() < columns) && (oldPlayField.getSourcePos().getY() < rows))
                newPlayField.setSourcePos(oldPlayField.getSourcePos());
        }
        displayPlayField();
    }

    /**
     * To create a new field(with all wall pieces)
     */
    public void newPlayField(){
        getPlayField().setAllCellsAsWallPieces();
        displayPlayField();
    }

    /**
     * To mix the field(each cell will be rotated by a random number of turns.)
     */
    public void mix(){
        getPlayField().mix();
        fillAllConnectedPipes();
        displayPlayField();
    }

    /**
     * to fill all the pipes that are connected to the source
     */
    public void fillAllConnectedPipes(){
        if(getPlayField().getSourcePos() != null) {
            Position findNextFillablePipePos;
            do {
                findNextFillablePipePos = getPlayField().findNextFillablePipe();
                if(findNextFillablePipePos != null) displayPipe(findNextFillablePipePos);
            } while (findNextFillablePipePos != null);
            clearUnConnectedPipesFilledState();
        }
        else {
            playField.clearAllPipesFilledState();
            displayPlayField();
        }
    }

    /**
     * to clear the Filling state for all pipes that are not connected to the source
     */
    private void clearUnConnectedPipesFilledState(){
        Position unConnectPipePos;
        do{
            unConnectPipePos = playField.clearOneUnConnectedPipeFilledState();
            if(unConnectPipePos != null) displayPipe(unConnectPipePos);
        }while(unConnectPipePos != null);
    }

    /**
     * Save the game to a file
     *
     * @param file   The file to save
     */
    public void saveGame(File file) {
        Gson gson = new GsonBuilder().registerTypeAdapter(PlayField.class, new PlayFieldSerializer()).create();
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(gson.toJson(playField));
            writer.close();
        } catch (IOException ex) {
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    /**
     * Load the game from a file
     *
     * @param file   The file to load
     */
    public void loadGame(File file) throws Exception {
        String json = Files.readString(file.toPath());
        Gson gson = new GsonBuilder().registerTypeAdapter(PlayField.class,new PlayFieldDeserializer()).create();

        try{
            playField = gson.fromJson(json, PlayField.class);
        } catch(JsonParseException e){
            throw new Exception("File format error");
        }

        if(playField != null){
            resize(playField.getColumns(), playField.getRows());
            displayPlayField();
        }
        else{
            throw new Exception("Empty File");
        }
    }

    /**
     * to display the play field on the gui
     */
    public void displayPlayField(){
        for (int i = 0;i < playField.getColumns();i++) {
            for (int j = 0; j < playField.getRows(); j++) {
                displayPipe(new Position(i,j));
            }
        }
    }

    /**
     * to display a pipe on the certain position on the gui
     */
    public void displayPipe(Position pos){
        boolean isSource = false;
        if(playField.getSourcePos() != null) isSource = playField.getSourcePos().equals(pos);
        gui.displayPipe(pos, playField.getField(pos), isSource);
    }

    /**
     * Only used for testing/debugging. Returns a String representation of the field..
     *
     * @return a String representation of the field.
     */
    String fieldToString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < playField.getColumns(); i++) {
            for (int j = 0; j < playField.getRows(); j++) {
                result.append(playField.getField(new Position(i,j)));
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

}
