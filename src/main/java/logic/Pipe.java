package logic;

/**
 * the Pipe class
 * which includes the pipe type, the angle of rotation, is filled or is not filled
 *
 * @author LiChieh Chou
 */
public class Pipe {

    /**
     * the pipe type(wall, end pipe, curve pipe, straight pipe or t pipe)
     */
    private PipeType pipeType;

    /**
     * the angle of rotation
     */
    private Rotation rotation;

    /**
     * is the pipe Filled or not
     */
    private boolean isFilled;

    /**
     * Constructor for Pipe.
     * Default is a wall piece
     */
    public Pipe(){
        this.pipeType = PipeType.WALL;
        this.rotation = Rotation.ZERO;
        this.isFilled = false;
    }

    /**
     * Constructor for Pipe.
     * the pipe type is to be set, with default 0-degree rotation and is not Filled
     *
     * @param pipeType the pipe type to be set
     */
    public Pipe(PipeType pipeType){
        this.pipeType = pipeType;
        this.rotation = Rotation.ZERO;
        this.isFilled = false;
    }

    /**
     * Constructor for Pipe.
     * Pipe type and rotation degree is to be set
     *
     * @param pipeType the pipe type to be set
     * @param rotation the rotation of the pipe
     */
    public Pipe(PipeType pipeType,Rotation rotation){
        this.pipeType = pipeType;
        this.rotation = rotation;
        this.isFilled = false;
    }

    /**
     * to set the pipe by pipe number
     * which follows the rule:
     * Opening to   Left    Bottom  Right   Top
     * Bit          4       3       2       1
     * Decimal      8       4       2       1
     *
     * @param pipeNumber the pipe number
     */
    public void setPipe(int pipeNumber){
        switch (pipeNumber) {
            case 1 -> setPipe(PipeType.END_PIPE, Rotation.ZERO);
            case 2 -> setPipe(PipeType.END_PIPE, Rotation.NINETY);
            case 3 -> setPipe(PipeType.CURVE_PIPE, Rotation.ZERO);
            case 4 -> setPipe(PipeType.END_PIPE, Rotation.ONE_EIGHTY);
            case 5 -> setPipe(PipeType.STRAIGHT_PIPE, Rotation.ZERO);
            case 6 -> setPipe(PipeType.CURVE_PIPE, Rotation.NINETY);
            case 7 -> setPipe(PipeType.T_PIPE, Rotation.ZERO);
            case 8 -> setPipe(PipeType.END_PIPE, Rotation.TWO_SEVENTY);
            case 9 -> setPipe(PipeType.CURVE_PIPE, Rotation.TWO_SEVENTY);
            case 10 -> setPipe(PipeType.STRAIGHT_PIPE, Rotation.NINETY);
            case 11 -> setPipe(PipeType.T_PIPE, Rotation.TWO_SEVENTY);
            case 12 -> setPipe(PipeType.CURVE_PIPE, Rotation.ONE_EIGHTY);
            case 13 -> setPipe(PipeType.T_PIPE, Rotation.ONE_EIGHTY);
            case 14 -> setPipe(PipeType.T_PIPE, Rotation.NINETY);
            default -> setPipe(PipeType.WALL, Rotation.ZERO);
        }
    }

    /**
     * to set the pipe by PipeType and rotation
     *
     * @param pipeType the pipe type
     * @param rotation the angle of rotation
     */
    public void setPipe(PipeType pipeType, Rotation rotation){
        this.pipeType = pipeType;
        this.rotation = rotation;
    }

    /**
     * to set the pipe type
     *
     * @param pipeType the pipe type
     */
    public void setPipeType(PipeType pipeType) {
        this.pipeType = pipeType;
    }

    /**
     * to set rotation
     *
     * @param rotation the angle of rotation
     */
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    /**
     * to set the Filled state
     *
     * @param Filled
     */
    public void setFilled(boolean Filled) {
        isFilled = Filled;
    }

    /**
     * to get the pipe type
     *
     * @return the pipe type
     */
    public PipeType getPipeType() {
        return pipeType;
    }

    /**
     * to get the pipe number
     * which follows the rule:
     * Opening to   Left    Bottom  Right   Top
     * Bit          4       3       2       1
     * Decimal      8       4       2       1
     *
     * @return the pipe number
     */
    public int getPipeNumber(){
        int pipeNumber = 0;

        if(isOpenToTop()) pipeNumber += 1;
        if(isOpenToRight()) pipeNumber += 2;
        if(isOpenToBottom()) pipeNumber += 4;
        if(isOpenToLeft()) pipeNumber += 8;

        return pipeNumber;
    }

    /**
     * to get the angle of rotation
     *
     * @return the angle of rotation
     */
    public Rotation getRotation() {
        Rotation toReturn;
        if (pipeType == PipeType.WALL) toReturn = Rotation.ZERO;
        else toReturn = rotation;

        return toReturn;
    }

    /**
     * to get the pipe's Filling state
     *
     * @return true, is Filled; false, is not Filled
     */
    public boolean getIsFilled() {
        return isFilled;
    }

    /**
     * to rotate the pipe by 90 degree clockwise
     */
    public void clockwise(){
        if (rotation == Rotation.TWO_SEVENTY) rotation = Rotation.ZERO;
        else rotation = Rotation.toRotation((rotation.getValue() + 90));

        if(pipeType == PipeType.STRAIGHT_PIPE) rotation = Rotation.toRotation(rotation.getValue()%180);
    }

    /**
     * to rotate the pipe by 90 degree counterclockwise
     */
    public void counterclockwise(){
        if (rotation == Rotation.ZERO) rotation = Rotation.TWO_SEVENTY;
        else rotation = Rotation.toRotation((rotation.getValue() - 90));

        if(pipeType == PipeType.STRAIGHT_PIPE) rotation = Rotation.toRotation(rotation.getValue()%180);
    }

    /**
     * to add a route to a direction
     *
     * @param dir the direction
     */
    public void addOpenDirection(Direction dir){
        int pipeNum = getPipeNumber();

        pipeNum |= (int)Math.pow(2, dir.ordinal());

        setPipe(pipeNum);
    }

    /**
     * to check if the pipe is opened to top
     *
     * @return true, if open to top; false, if not open to top
     */
    public boolean isOpenToTop(){
        return checkOpens(Direction.TOP);
    }

    /**
     * to check if the pipe is opened to right
     *
     * @return true, if open to right; false, if not open to right
     */
    public boolean isOpenToRight(){
        return checkOpens(Direction.RIGHT);
    }

    /**
     * to check if the pipe is opened to bottom
     *
     * @return true, if open to bottom; false, if not open to bottom
     */
    public boolean isOpenToBottom(){
        return checkOpens(Direction.BOTTOM);
    }

    /**
     * to check if the pipe is opened to left
     *
     * @return true, if open to left; false, if not open to left
     */
    public boolean isOpenToLeft(){
        return checkOpens(Direction.LEFT);
    }

    /**
     * to check if the pipe is opened to top, bottom, right or left
     *
     @param dir the direction to check
     */
    private boolean checkOpens(Direction dir){
        int dirDegree = 90*dir.ordinal();
        boolean toReturn = false;

        if ((pipeType == PipeType.END_PIPE)
           &&(rotation.getValue() == dirDegree)){
            toReturn = true;
        }
        else if ((pipeType == PipeType.CURVE_PIPE)
                &&((rotation.getValue() == dirDegree)||(rotation.getValue() == (dirDegree+270)%360))){
            toReturn = true;
        }
        else if ((pipeType == PipeType.STRAIGHT_PIPE)
                &&((rotation.getValue() == dirDegree)||(rotation.getValue() == (dirDegree+180)%360))){
            toReturn = true;
        }
        else if ((pipeType == PipeType.T_PIPE)
                &&((rotation.getValue() == dirDegree)||(rotation.getValue() == (dirDegree+180)%360)||(rotation.getValue() == (dirDegree+270)%360))){
            toReturn = true;
        }
        return toReturn;
    }

    @Override
    public String toString() {
        return "Pipe{" +
                "pipeType=" + pipeType +
                ", rotation=" + rotation +
                '}';
    }
}
