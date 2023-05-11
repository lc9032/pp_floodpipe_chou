package logic;

/**
 * This Position class represents a Position in X and Y
 *
 * @author Li-Chieh Chou
 */
public class Position {
    private int xPos;
    private int yPos;

    /**
     * to create a position by given X, Y value
     *
     * @param xPos X value
     * @param yPos Y value
     */
    public Position(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * get the X value of a position
     *
     * @return the X value of a position
     */
    public int getX() {
        return xPos;
    }

    /**
     * get the Y value of a position
     *
     * @return the Y value of a position
     */
    public int getY() {
        return yPos;
    }

    /**
     * to convert the position to a string
     *
     * @return a string include the X and Y value of the position
     */
    public String toString() {
        return xPos + "," + yPos;
    }


    @Override
    public boolean equals(Object o) {
        boolean toReturn = false;
        if (o instanceof Position){
            if ((((Position)o).getX() == xPos) && (((Position)o).getY() == yPos))
                toReturn = true;
        }

        return toReturn;
    }

}
