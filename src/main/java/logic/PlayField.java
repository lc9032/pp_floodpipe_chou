package logic;

import java.util.Random;

/**
 * the class for the play field on which the player play
 *
 * @author LiChieh Chou
 */
public class PlayField {
    /**
     * the source position
     */
    private Position sourcePos;

    /**
     * the overflow mode
     */
    private boolean overflow;

    /**
     * The 2-dimensional field on which the player play.
     */
    private Pipe[][] field;

    /**
     * Constructor for the play field
     *
     * @param columns the size(columns) of the play field
     * @param rows the size(rows) of the play field
     * @param overflow overflow mode
     */
    PlayField(int columns, int rows, boolean overflow){
        this.sourcePos = null;
        this.overflow = overflow;
        this.field = new Pipe[columns][rows];
    }

    /**
     * to get the source position
     *
     * @return the source position
     */
    public Position getSourcePos() {
        return sourcePos;
    }

    /**
     * to get the overflow mode value
     *
     * @return the overflow mode value
     */
    public boolean getOverflow() {
        return overflow;
    }

    /**
     * to get the Pipe for a certain position on the current playing field
     *
     * @param pos the position to get the pipe
     * @return the Pipe
     */
    public Pipe getField(Position pos) {
        if(pos != null)
            return field[pos.getX()][pos.getY()];
        else
            return null;
    }

    /**
     * to get the columns of the play field
     * @return columns number
     */
    public int getColumns(){
        return field.length;
    }

    /**
     * to get the rows of the play field
     * @return rows number
     */
    public int getRows(){
        return field[0].length;
    }

    /**
     * to set the source position
     *
     * @param sourcePos the position to be set as the source
     */
    public void setSourcePos(Position sourcePos) {
        this.sourcePos = sourcePos;
    }

    /**
     * to set the overflow value
     *
     * @param overflow the new overflow value
     */
    public void setOverflow(boolean overflow) {
        this.overflow = overflow;

        checkAllPipesReachableFromSource();
    }

    /**
     * to set the Pipe for a certain position on the current playing field
     *
     * @param pos the position in which the pipe to be set
     * @param pipe the pipe to be set
     */
    public void setField(Position pos, Pipe pipe){
        field[pos.getX()][pos.getY()] = pipe;
    }

    /**
     * to create a solvable play field
     *
     * @param percentageOfWall the max allowed percentage of wall pieces
     */
    public void generatePlayField(int percentageOfWall){
        int MaxWallPieces = ((getColumns() * getRows() * percentageOfWall)/100);

        int sX = (int) (Math.random() * getColumns());
        int sY = (int) (Math.random() * getRows());
        setSourcePos(new Position(sX, sY));

        do {
            for (int i = 0;i < getColumns();i++){
                for (int j = 0;j < getRows();j++){
                    setField(new Position(i,j),new Pipe(PipeType.WALL));
                }
            }

            boolean[][] isChecked = new boolean[getColumns()][getRows()];
            Position pos;
            do {
                pos = findAPositionAvailableToAddConnection();
                if (pos != null) {
                    isChecked[pos.getX()][pos.getY()] = false;
                    generatePlayFieldRecursion(pos, isChecked, percentageOfWall);
                }
            }while ((countWallPieces() > MaxWallPieces) && (pos != null));
        }while((countWallPieces() > MaxWallPieces) || (!checkOpenEnd()));
    }

    /**
     * This method generates a play field for the game using recursion.
     *
     * @param pos the current position of the playfield to be filled with a pipe.
     * @param isChecked a boolean matrix used to check if a position has already been checked.
     * @param percentageOfWall the percentage of walls in the playfield.
     */
    private void generatePlayFieldRecursion(Position pos, boolean[][] isChecked, int percentageOfWall){
        if(pos == null) return;
        if(!isChecked[pos.getX()][pos.getY()]) {
            isChecked[pos.getX()][pos.getY()] = true;

            Random rand = new Random();
            Pipe newPipe;
            do{
                int pipeTypeOrd = 1;
                if(rand.nextInt(100)>percentageOfWall){
                    pipeTypeOrd = rand.nextInt(3) + 2;
                }
                int rotOrd = rand.nextInt(4);
                newPipe = new Pipe(PipeType.values()[pipeTypeOrd], Rotation.values()[rotOrd]);
                setField(pos, newPipe);

            }while (!checkIfPipeFitAtThisPosition(pos, isChecked) && checkIsPossibleToFindAFitPipe(pos));

            if(newPipe.isOpenToTop()) generatePlayFieldRecursion(getUpperPos(pos), isChecked, percentageOfWall);
            if(newPipe.isOpenToBottom()) generatePlayFieldRecursion(getDownPos(pos), isChecked, percentageOfWall);
            if(newPipe.isOpenToRight()) generatePlayFieldRecursion(getRightPos(pos), isChecked, percentageOfWall);
            if(newPipe.isOpenToLeft()) generatePlayFieldRecursion(getLeftPos(pos), isChecked, percentageOfWall);
        }
    }

    /**
     * This method is used to check if it is possible to find a fit pipe for the given position on the game field.
     *
     * @param pos The position on the game field to check for a fit pipe.
     * @return A boolean value indicating whether it is possible to find a fit pipe for the given position or not.
     */
    private boolean checkIsPossibleToFindAFitPipe(Position pos){
        boolean toReturn = true;

        Position upperPos = getUpperPos(pos);
        Position downPos = getDownPos(pos);
        Position rightPos = getRightPos(pos);
        Position leftPos = getLeftPos(pos);

        if((upperPos != null)&&(downPos != null)&&(rightPos != null)&&(leftPos != null))
        {
            if(getField(upperPos).isOpenToBottom()
                    && getField(downPos).isOpenToTop()
                    && getField(rightPos).isOpenToLeft()
                    && getField(leftPos).isOpenToRight())
                toReturn = false;
        }

        return toReturn;
    }

    /**
     * Checks if a pipe can fit in a given position in the playfield.
     *
     * @param pos The position in the playfield to check.
     * @param isChecked A 2D boolean array representing whether each position in the playfield has already been checked.
     * @return true if a pipe can fit in the given position, false otherwise.
     */
    private boolean checkIfPipeFitAtThisPosition(Position pos, boolean[][] isChecked){
        boolean toReturn = true;

        Position pos2;

        pos2 = getUpperPos(pos);
        if(pos2 == null){
            if(getField(pos).isOpenToTop()) toReturn = false;
        }
        else if(isChecked[pos2.getX()][pos2.getY()]){
            if((getField(pos).isOpenToTop() && (!isConnectedToTop(pos)))
                    || (getField(pos2).isOpenToBottom() && (!isConnectedToBottom(pos2)))) toReturn = false;
        }

        pos2 = getDownPos(pos);
        if(pos2 == null){
            if(getField(pos).isOpenToBottom()) toReturn = false;
        }
        else if(isChecked[pos2.getX()][pos2.getY()]){
            if((getField(pos).isOpenToBottom() && (!isConnectedToBottom(pos)))
                    || (getField(pos2).isOpenToTop() && (!isConnectedToTop(pos2)))) toReturn = false;
        }

        pos2 = getRightPos(pos);
        if(pos2 == null){
            if(getField(pos).isOpenToRight()) toReturn = false;
        }
        else if(isChecked[pos2.getX()][pos2.getY()]){
            if((getField(pos).isOpenToRight() && (!isConnectedToRight(pos)))
                    || (getField(pos2).isOpenToLeft() && (!isConnectedToLeft(pos2)))) toReturn = false;
        }

        pos2 = getLeftPos(pos);
        if(pos2 == null){
            if(getField(pos).isOpenToLeft()) toReturn = false;
        }
        else if(isChecked[pos2.getX()][pos2.getY()]){
            if((getField(pos).isOpenToLeft() && (!isConnectedToLeft(pos)))
                    || (getField(pos2).isOpenToRight() && (!isConnectedToRight(pos2)))) toReturn = false;
        }

        return toReturn;
    }

    /**
     * to count how many wall pieces are on the play field
     *
     * @return the number of the wall pieces
     */
    private int countWallPieces(){
        int counter = 0;
        for(int i = 0;i < getColumns();i++){
            for(int j = 0;j < getRows();j++){
                if(getField(new Position(i,j)).getPipeType() == PipeType.WALL)
                    counter++;
            }
        }
        return counter;
    }

    /**
     * Finds a position available to add a connection.
     *
     * @return The position available to add a connection, or null if none is found.
     */
    private Position findAPositionAvailableToAddConnection(){
        Position toReturn = null;

        if(getField(sourcePos).getPipeType() == PipeType.WALL)
            toReturn = sourcePos;

        for(int i = 0;((i < getColumns())&&(toReturn == null));i++){
            for(int j = 0;((j < getRows())&&(toReturn == null));j++){
                Position pos = new Position(i,j);
                if((getField(pos).getPipeType() != PipeType.WALL)
                        &&((getField(pos)).getPipeType() != PipeType.T_PIPE)){
                    if(getUpperPos(pos) != null){
                        if(getField(getUpperPos(pos)).getPipeType() == PipeType.WALL)
                            toReturn = pos;
                    }
                    if(getDownPos(pos) != null){
                        if(getField(getDownPos(pos)).getPipeType() == PipeType.WALL)
                            toReturn = pos;
                    }
                    if(getRightPos(pos) != null){
                        if(getField(getRightPos(pos)).getPipeType() == PipeType.WALL)
                            toReturn = pos;
                    }
                    if(getLeftPos(pos) != null){
                        if(getField(getLeftPos(pos)).getPipeType() == PipeType.WALL)
                            toReturn = pos;
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * rotate a pipe on the field
     *
     * @param pos The Position of the pipe to be rotated
     * @param direction the direction of rotation
     */
    public void rotation(Position pos, boolean direction){
        if (direction)
            field[pos.getX()][pos.getY()].counterclockwise();
        else
            field[pos.getX()][pos.getY()].clockwise();
    }

    /**
     * to mix the play field
     * (each cell will be rotated by a random number of turns)
     */
    public void mix(){
        for(int i = 0;i < getColumns();i++){
            for(int j = 0;j < getRows();j++){
                double r = Math.random()*4;
                Position pos = new Position(i,j);
                Pipe pipe = getField(pos);
                pipe.setRotation(Rotation.toRotation((int)r*90));
                setField(pos,pipe);
            }
        }
    }

    /**
     * This function is called by the timer, which will automatically fill the next pipe
     *
     * @return a position where the pipe is filled
     */
    public Position findNextFillablePipe(){
        Position toReturn = null;

        if(!getField(sourcePos).getIsFilled()){
            field[sourcePos.getX()][sourcePos.getY()].setFilled(true);
            toReturn = sourcePos;
        } else {

            for(int i = 0;((i < getColumns())&&(toReturn == null));i++){
                for(int j = 0;((j < getRows())&&(toReturn == null));j++){
                    Position pos = new Position(i,j);
                    if(getField(pos).getIsFilled()){
                        if ((isConnectedToTop(pos))&&(!getField(getUpperPos(pos)).getIsFilled())) {
                            getField(getUpperPos(pos)).setFilled(true);
                            toReturn = getUpperPos(pos);
                        }
                        else if ((isConnectedToBottom(pos))&&(!getField(getDownPos(pos)).getIsFilled())) {
                            getField(getDownPos(pos)).setFilled(true);
                            toReturn = getDownPos(pos);
                        }
                        else if ((isConnectedToRight(pos))&&(!getField(getRightPos(pos)).getIsFilled())) {
                            getField(getRightPos(pos)).setFilled(true);
                            toReturn = getRightPos(pos);
                        }
                        else if ((isConnectedToLeft(pos))&&(!getField(getLeftPos(pos)).getIsFilled())) {
                            getField(getLeftPos(pos)).setFilled(true);
                            toReturn = getLeftPos(pos);
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * to set all cells as wall pieces
     */
    public void setAllCellsAsWallPieces(){
        for (int i = 0;i < getColumns();i++){
            for (int j = 0;j < getRows();j++){
                setField(new Position(i,j),new Pipe(PipeType.values()[0],Rotation.ZERO));
            }
        }
        setSourcePos(null);
    }

    /**
     * to clear the Filling state for all pipes
     *
     */
    public void clearAllPipesFilledState(){
        for (int i = 0; i < getColumns(); i++) {
            for (int j = 0; j < getRows(); j++) {
                Position pos = new Position(i,j);
                getField(pos).setFilled(false);
            }
        }
    }

    /**
     * to clear the Filling state for a pipe that is not connected to the source
     *
     * @return a position where the pipe that the filled state is cleared
     */
    public Position clearOneUnConnectedPipeFilledState(){
        Position toReturn = null;
        if(sourcePos != null) {
            boolean[][] isChecked = new boolean[getColumns()][getRows()];
            checkConnection(sourcePos, isChecked);
            for (int i = 0; i < getColumns() && toReturn == null; i++) {
                for (int j = 0; j < getRows() && toReturn == null; j++) {
                    Position pos = new Position(i, j);
                    if ((!isChecked[i][j]) && (getField(pos).getPipeType() != PipeType.WALL) && (getField(pos).getIsFilled())) {
                        getField(pos).setFilled(false);
                        toReturn = pos;
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * to check are all pipes are reachable from the source
     *
     * @return true, if all pipe are reachable from the source;
     *         false, if there is at least one pipe is not connected to the source
     */
    public boolean checkAllPipesReachableFromSource(){
        boolean toReturn = true;

        if(sourcePos != null) {
            boolean[][] isChecked = new boolean[getColumns()][getRows()];

            if(!checkConnection(sourcePos, isChecked)) toReturn = false;

            for (int i = 0; i < getColumns(); i++) {
                for (int j = 0; j < getRows(); j++) {
                    Position pos = new Position(i,j);
                    if ((!isChecked[i][j])&&(getField(pos).getPipeType() != PipeType.WALL)){
                        toReturn = false;
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * Checks if all pipes in the network are connected.
     *
     * @param pos The position of the pipe to check.
     * @param isChecked A boolean array that keeps track of the pipes that have been checked.
     * @return True if all pipes are connected, false otherwise.
     */
    private boolean checkConnection(Position pos,boolean[][] isChecked){
        boolean toReturn = true;

        if (!isChecked[pos.getX()][pos.getY()]){

            isChecked[pos.getX()][pos.getY()] = true;

            if(isConnectedToTop(pos)){
                if(!checkConnection(getUpperPos(pos),isChecked)) toReturn = false;
            }
            if(isConnectedToBottom(pos)){
                if(!checkConnection(getDownPos(pos),isChecked)) toReturn = false;
            }
            if(isConnectedToRight(pos)){
                if (!checkConnection(getRightPos(pos),isChecked)) toReturn = false;
            }
            if(isConnectedToLeft(pos)){
                if(!checkConnection(getLeftPos(pos),isChecked)) toReturn = false;
            }
        }
        return toReturn;
    }

    /**
     * to check if there is an open end on the field
     *
     * @return true, if there is No open end; false, it there is an open end
     */
    public boolean checkOpenEnd(){
        boolean toReturn = true;
        for(int i = 0;((i < getColumns())&&(toReturn));i++){
            for(int j = 0;((j < getRows())&&(toReturn));j++){
                Position pos = new Position(i,j);
                if(getField(pos).isOpenToTop())
                    if(!isConnectedToTop(pos)) toReturn = false;

                if(getField(pos).isOpenToBottom())
                    if(!isConnectedToBottom(pos)) toReturn = false;

                if(getField(pos).isOpenToRight())
                    if(!isConnectedToRight(pos)) toReturn = false;

                if(getField(pos).isOpenToLeft())
                    if(!isConnectedToLeft(pos)) toReturn = false;
            }
        }
        return toReturn;
    }

    /**
     * Checks if a pipe at the given position is connected to the top.
     *
     * @param pos the position to check if connected to the top
     * @return true if connected to the top, false otherwise
     */
    private boolean isConnectedToTop(Position pos){
        boolean toReturn = false;
        if(getField(pos).isOpenToTop()){
            if (getUpperPos(pos)!= null)
                toReturn = getField(getUpperPos(pos)).isOpenToBottom();
        }
        return toReturn;
    }

    /**
     * Checks if a pipe at the given position is connected to the bottom.
     *
     * @param pos the position to check if connected to the bottom
     * @return true if connected to the bottom, false otherwise
     */
    private boolean isConnectedToBottom(Position pos){
        boolean toReturn = false;
        if(getField(pos).isOpenToBottom()){
            if (getDownPos(pos)!= null)
                toReturn = getField(getDownPos(pos)).isOpenToTop();
        }
        return toReturn;
    }

    /**
     * Checks if a pipe at the given position is connected to the right.
     *
     * @param pos the position to check if connected to the right
     * @return true if connected to the right, false otherwise
     */
    private boolean isConnectedToRight(Position pos){
        boolean toReturn = false;
        if(getField(pos).isOpenToRight()){
            if (getRightPos(pos)!= null)
                toReturn = getField(getRightPos(pos)).isOpenToLeft();
        }
        return toReturn;
    }

    /**
     * Checks if a pipe at the given position is connected to the left.
     *
     * @param pos the position to check if connected to the left
     * @return true if connected to the left, false otherwise
     */
    private boolean isConnectedToLeft(Position pos){
        boolean toReturn = false;
        if(getField(pos).isOpenToLeft()){
            if (getLeftPos(pos)!= null)
                toReturn =getField(getLeftPos(pos)).isOpenToRight();
        }
        return toReturn;
    }

    /**
     * This method is checking if the given position is located on the most top row of the game board.
     *
     * @param pos the position to check
     * @return whether the given position is at the most top row or not.
     */
    private boolean isAtMostTopRow(Position pos){
        return (pos.getY() == 0);
    }

    /**
     * This method is checking if the given position is located on the most bottom row of the game board.
     *
     * @param pos the position to check
     * @return whether the given position is at the most bottom row or not.
     */
    private boolean isAtMostBottomRow(Position pos){
        return (pos.getY() == getRows()-1);
    }

    /**
     * This method is checking if the given position is located on the most right column of the game board.
     *
     * @param pos the position to check
     * @return whether the given position is at the most right column or not.
     */
    private boolean isAtMostRightColumn(Position pos){
        return (pos.getX() == getColumns()-1);
    }

    /**
     * This method is checking if the given position is located on the most left column of the game board.
     *
     * @param pos the position to check
     * @return whether the given position is at the most left column or not.
     */
    private boolean isAtMostLeftColumn(Position pos){
        return (pos.getX() == 0);
    }

    /**
     * Returns the position above the given position.
     *
     * @param pos the current position
     * @return the position above the given position
     */
    private Position getUpperPos(Position pos){
        Position newPos = null;
        if(!isAtMostTopRow(pos))
            newPos = new Position(pos.getX(), pos.getY()-1);
        else if(overflow)
            newPos = new Position(pos.getX(), getRows()-1);

        return newPos;
    }

    /**
     * Returns the position below the given position.
     *
     * @param pos the current position
     * @return the position below the given position
     */
    private Position getDownPos(Position pos){
        Position newPos = null;
        if(!isAtMostBottomRow(pos))
            newPos = new Position(pos.getX(), pos.getY()+1);
        else if(overflow)
            newPos = new Position(pos.getX(), 0);

        return newPos;
    }

    /**
     * Returns the position to the right of the given position.
     * @param pos the current position
     * @return the position to the right of the given position
     */
    private Position getRightPos(Position pos){
        Position newPos = null;
        if(!isAtMostRightColumn(pos))
            newPos = new Position(pos.getX()+1, pos.getY());
        else if(overflow)
            newPos = new Position(0, pos.getY());

        return newPos;
    }

    /**
     * Returns the position to the left of the given position.
     * @param pos the current position
     * @return the position to the left of the given position
     */
    private Position getLeftPos(Position pos){
        Position newPos = null;
        if(!isAtMostLeftColumn(pos))
            newPos = new Position(pos.getX()-1, pos.getY());
        else if(overflow)
            newPos = new Position(getColumns()-1, pos.getY());

        return newPos;
    }
}
