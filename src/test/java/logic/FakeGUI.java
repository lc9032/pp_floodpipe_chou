package logic;

/**
 * Fake GUI used for testing the logic of the game FloodPipe. All methods do
 * nothing. To ensure that the logic calls the correct methods for the gui, it
 * could be possibly to add package private boolean attributes, that tell if a
 * certain method has been called.
 */
public class FakeGUI implements GUIConnector {
    @Override
    public void displayPipe(Position pos, Pipe pipe, boolean isSource) {

    }

    @Override
    public void onGameEnd(int turns) {

    }

    @Override
    public void resize(int columns, int rows){

    }
}