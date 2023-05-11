package logic;

public interface GUIConnector {

    /**
     * Displays a given pipe at a specified cell of the field.
     *
     * @param pos the position at which in the field the given pipe
     * @param pipe the type of the pipe
     */
    void displayPipe(Position pos, Pipe pipe, boolean isSource);

    /**
     * Called when the game is finished.
     */
    void onGameEnd(int turns);

    /**
     * Called when the size of the play field is changed
     *
     * @param columns the new columns value
     * @param rows the new rows value
     */
    void resize(int columns, int rows);
}
