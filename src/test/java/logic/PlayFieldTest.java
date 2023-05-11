package logic;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlayFieldTest {

    /**
     * Are non-square playing fields also possible?
     */
    @Test
    public void testNonSquarePossible(){
        int columns = 10;
        int rows = 2;
        int percentageOfWall = 0;
        boolean overflowMode = false;

        PlayField playField = new PlayField(columns, rows, overflowMode);
        playField.generatePlayField(percentageOfWall);

        assertEquals(playField.getColumns(),columns);
        assertEquals(playField.getRows(),rows);
    }

    /**
     * Does the randomly generated playing field not have more wall pieces than allowed?
     */
    @Test
    public void testFieldNotHaveMoreWallPiecesThanAllowed_ZeroPercent(){
        int columns = 10;
        int rows = 10;
        int percentageOfWall = 0;
        boolean overflowMode = false;

        for(int i = 0;i <= 100;i++) {
            PlayField playField = new PlayField(columns, rows, overflowMode);
            playField.generatePlayField(percentageOfWall);

            int wallCount = 0;
            for (int c = 0; c < columns; c++) {
                for (int r = 0; r < rows; r++) {
                    if (playField.getField(new Position(c, r)).getPipeType() == PipeType.WALL)
                        wallCount++;
                }
            }
            assertTrue(wallCount <= (0));
        }
    }

    @Test
    public void testFieldNotHaveMoreWallPiecesThanAllowed_ThirtyPercent(){
        int columns = 10;
        int rows = 10;
        int percentageOfWall = 30;
        boolean overflowMode = false;

        for(int i = 0;i <= 100;i++) {
            PlayField playField = new PlayField(columns, rows, overflowMode);
            playField.generatePlayField(percentageOfWall);

            int wallCount = 0;
            for (int c = 0; c < columns; c++) {
                for (int r = 0; r < rows; r++) {
                    if (playField.getField(new Position(c, r)).getPipeType() == PipeType.WALL)
                        wallCount++;
                }
            }
            assertTrue(wallCount <= ((columns * rows * percentageOfWall) / 100));
        }
    }

    @Test
    public void testFieldNotHaveMoreWallPiecesThanAllowed_SeventyPercent(){
        int columns = 10;
        int rows = 10;
        int percentageOfWall = 70;
        boolean overflowMode = false;

        for(int i = 0;i <= 100;i++) {
            PlayField playField = new PlayField(columns, rows, overflowMode);
            playField.generatePlayField(percentageOfWall);

            int wallCount = 0;
            for (int c = 0; c < columns; c++) {
                for (int r = 0; r < rows; r++) {
                    if (playField.getField(new Position(c, r)).getPipeType() == PipeType.WALL)
                        wallCount++;
                }
            }
            assertTrue(wallCount <= ((columns * rows * percentageOfWall) / 100));
        }
    }


    /**
     * Is the randomly generated playing field solvable?
     */
    @Test
    public void testFieldSolvable_zeroPercent(){
        int columns = 10;
        int rows = 10;
        int percentageOfWall = 0;
        boolean overflowMode = false;

        for(int i = 0;i <= 100;i++) {
            PlayField playField = new PlayField(columns, rows, overflowMode);
            playField.generatePlayField(percentageOfWall);

            assertTrue(playField.checkAllPipesReachableFromSource());
            assertTrue(playField.checkOpenEnd());
        }
    }

    @Test
    public void testFieldSolvable_thirtyPercent(){
        int columns = 10;
        int rows = 10;
        int percentageOfWall = 30;
        boolean overflowMode = false;

        for(int i = 0;i <= 100;i++) {
            PlayField playField = new PlayField(columns, rows, overflowMode);
            playField.generatePlayField(percentageOfWall);

            assertTrue(playField.checkAllPipesReachableFromSource());
            assertTrue(playField.checkOpenEnd());
        }
    }

    @Test
    public void testFieldSolvable_seventyPercent(){
        int columns = 10;
        int rows = 10;
        int percentageOfWall = 70;
        boolean overflowMode = false;

        for(int i = 0;i <= 100;i++) {
            PlayField playField = new PlayField(columns, rows, overflowMode);
            playField.generatePlayField(percentageOfWall);

            assertTrue(playField.checkAllPipesReachableFromSource());
            assertTrue(playField.checkOpenEnd());
        }
    }

    /**
     * Are all pipes reachable from the source correctly identified?
     */
    @Test
    public void testAllPipesReachableFromIdentifiedSource(){
        Position sourcePos = new Position(0,0);
        String board =
                            "┏┳┳━━┓\n"
                        +   "┣┛┗━━┛\n"
                        +   "┣━┳━╸╻\n"
                        +   "╹╺┻━━┫\n"
                        +   "╺━━━━┛\n";

        Logic game = new Logic(new FakeGUI(), board, sourcePos);

        assertTrue(game.getPlayField().checkAllPipesReachableFromSource());
    }

    /**
     * Are all pipes NOT reachable from the source correctly determined?
     */
    @Test
    public void testAllPipesNotReachableFromDeterminedSource(){
        Position sourcePos = new Position(0,0);
        String board =
                            "┏┳┳━━┓\n"
                        +   "┣┛┗━━┛\n"
                        +   "┣━┳━╸╻\n"
                        +   "╹╺┻━━┫\n"
                        +   "┓━━━━┛\n";

        Logic game = new Logic(new FakeGUI(), board, sourcePos);

        assertFalse(game.getPlayField().checkAllPipesReachableFromSource());
    }

    /**
     * Are there open ends on the field, i.e. a pipe that is not connected?
     */
    @Test
    public void testOpenEndField(){
        Position sourcePos = new Position(0,0);
        String board =
                            "┏┳┳━━┓\n"
                        +   "┣┛┗━━┛\n"
                        +   "┣━┳━━┓\n"
                        +   "┣━┻━━┫\n"
                        +   "┗━━━━━\n";

        Logic game = new Logic(new FakeGUI(), board, sourcePos);

        assertFalse(game.getPlayField().checkOpenEnd());
    }

    @Test
    public void testSetAllCellsAsWallPieces(){
        Position sourcePos = new Position(0,0);
        String board =
                          "┏━┳╸\n"
                        + "┃╻┗┓\n"
                        + "┗┻━┛\n";
        Logic game = new Logic(new FakeGUI(), board, sourcePos);

        game.getPlayField().setAllCellsAsWallPieces();

        for(int i = 0;i < game.getPlayField().getColumns();i++){
            for (int j = 0;j < game.getPlayField().getRows();j++){
                assertEquals(PipeType.WALL, game.getPlayField().getField(new Position(i,j)).getPipeType());
            }
        }
    }

    @Test
    public void testClearAllPipesFilledState(){
        Position sourcePos = new Position(0,0);
        GUIConnector gui = new FakeGUI();
        String board =
                          "┏━┳╸\n"
                        + "┃╻┗┓\n"
                        + "┗┻━┛\n";
        Logic game = new Logic(gui, board, sourcePos);

        for(int i = 0;i < game.getPlayField().getColumns();i++){
            for (int j = 0;j < game.getPlayField().getRows();j++){
                game.getPlayField().getField(new Position(i,j)).setFilled(true);
            }
        }

        game.getPlayField().clearAllPipesFilledState();

        for(int i = 0;i < game.getPlayField().getColumns();i++){
            for (int j = 0;j < game.getPlayField().getRows();j++){
                assertFalse(game.getPlayField().getField(new Position(i,j)).getIsFilled());
            }
        }
    }

    @Test
    public void testClearUnConnectedPipesFilledState(){
        Position sourcePos = new Position(0,0);
        GUIConnector gui = new FakeGUI();
        String board =
                          "┏━┳╸\n"
                        + "┃╻┗┓\n"
                        + "┗┻━┏\n";
        Logic game = new Logic(gui, board, sourcePos);

        for(int i = 0;i < game.getPlayField().getColumns();i++){
            for (int j = 0;j < game.getPlayField().getRows();j++){
                game.getPlayField().getField(new Position(i,j)).setFilled(true);
            }
        }

//        game.getPlayField().clearUnConnectedPipesFilledState();
        Position unConnectPipePos;
        do{
            unConnectPipePos = game.getPlayField().clearOneUnConnectedPipeFilledState();
        }while(unConnectPipePos != null);

        assertFalse(game.getPlayField().getField(new Position(3,2)).getIsFilled());
    }
}
