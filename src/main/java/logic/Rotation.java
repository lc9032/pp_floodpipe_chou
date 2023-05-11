package logic;

/**
 * This enum class contains the different possible values for the rotation of a pipe.
 * Each value represents a rotation angle in degrees.
 */
public enum Rotation {
    ZERO(0),NINETY(90),ONE_EIGHTY(180),TWO_SEVENTY(270);

    private final int value;

    private Rotation(int val) {
        this.value = val;
    }

    /**
     * to get the rotation value.
     *
     * @return the integer value associated with a particular rotation.
     */
    public int getValue() {
        return value;
    }

    /**
     * takes an integer value as an argument and returns a corresponding Rotation enum
     *
     * @param val the integer value associated with a particular rotation.
     * @return the Rotation value associated with that integer value. If no matching value is found, it returns null.
     */
    public static Rotation toRotation(int val){
        Rotation toReturn = null;
        for (Rotation rotation: values()){
            if(rotation.value == val)
                toReturn = rotation;
        }
        return toReturn;
    }
}
