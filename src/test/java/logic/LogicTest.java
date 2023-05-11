package logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogicTest {

    /**
     * Is the rotation of a pipe correct?
     * For example, does a pipe opened to the top become one opened to the right when rotated to the right,
     * and does a pipe opened to the top, right, bottom become one opened to the left, bottom, right?
     * For each type of pipe (line, curve, T-branch, end piece),
     * at least one test in each of the two directions of rotation must be provided.
     */
    @Test
    public void testEndPipeClockwiseRotationCorrect(){
        Position sourcePos = new Position(0,0);
        String boardBeforeRotation =
                            "╸╹\n"
                        +   "╺╻\n";

        String boardAfterRotation =
                            "╹╺\n"
                        +   "╻╸\n";

        Logic game1 = new Logic(new FakeGUI(), boardBeforeRotation, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), boardAfterRotation, sourcePos);

        for(int i = 0;i < game1.getPlayField().getColumns();i++){
            for (int j = 0;j < game1.getPlayField().getRows();j++) {
                game1.rotated(new Position(i,j),false);
            }
        }

        assertEquals(game1.fieldToString(),game2.fieldToString());
    }

    @Test
    public void testEndPipeCounterclockwiseRotationCorrect(){
        Position sourcePos = new Position(0,0);
        String boardBeforeRotation =
                            "╹╺\n"
                        +   "╻╸\n";

        String boardAfterRotation =
                            "╸╹\n"
                        +   "╺╻\n";

        Logic game1 = new Logic(new FakeGUI(), boardBeforeRotation, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), boardAfterRotation, sourcePos);

        for(int i = 0;i < game1.getPlayField().getColumns();i++){
            for (int j = 0;j < game1.getPlayField().getRows();j++) {
                game1.rotated(new Position(i,j),true);
            }
        }
        assertEquals(game1.fieldToString(),game2.fieldToString());
    }

    @Test
    public void testStraightPipeClockwiseRotationCorrect(){
        Position sourcePos = new Position(0,0);
        String boardBeforeRotation = "━┃\n";
        String boardAfterRotation = "┃━\n";

        Logic game1 = new Logic(new FakeGUI(), boardBeforeRotation, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), boardAfterRotation, sourcePos);

        for(int i = 0;i < game1.getPlayField().getColumns();i++){
            for (int j = 0;j < game1.getPlayField().getRows();j++) {
                game1.rotated(new Position(i,j),false);
            }
        }

        assertEquals(game1.fieldToString(),game2.fieldToString());
    }

    @Test
    public void testStraightPipeCounterclockwiseRotationCorrect(){
        Position sourcePos = new Position(0,0);
        String boardBeforeRotation = "━┃\n";
        String boardAfterRotation = "┃━\n";

        Logic game1 = new Logic(new FakeGUI(), boardBeforeRotation, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), boardAfterRotation, sourcePos);

        for(int i = 0;i < game1.getPlayField().getColumns();i++){
            for (int j = 0;j < game1.getPlayField().getRows();j++) {
                game1.rotated(new Position(i,j),true);
            }
        }
        assertEquals(game1.fieldToString(),game2.fieldToString());
    }

    @Test
    public void testCurvePipeClockwiseRotationCorrect(){
        Position sourcePos = new Position(0,0);
        String boardBeforeRotation =
                            "┏┓\n"
                        +   "┗┛\n";

        String boardAfterRotation =
                            "┓┛\n"
                        +   "┏┗\n";

        Logic game1 = new Logic(new FakeGUI(), boardBeforeRotation, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), boardAfterRotation, sourcePos);

        for(int i = 0;i < game1.getPlayField().getColumns();i++){
            for (int j = 0;j < game1.getPlayField().getRows();j++) {
                game1.rotated(new Position(i,j),false);
            }
        }

        assertEquals(game1.fieldToString(),game2.fieldToString());
    }

    @Test
    public void testCurvePipeCounterclockwiseRotationCorrect(){
        Position sourcePos = new Position(0,0);
        String boardBeforeRotation =
                            "┓┛\n"
                        +   "┏┗\n";

        String boardAfterRotation =
                            "┏┓\n"
                        +   "┗┛\n";

        Logic game1 = new Logic(new FakeGUI(), boardBeforeRotation, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), boardAfterRotation, sourcePos);

        for(int i = 0;i < game1.getPlayField().getColumns();i++){
            for (int j = 0;j < game1.getPlayField().getRows();j++) {
                game1.rotated(new Position(i,j),true);
            }
        }
        assertEquals(game1.fieldToString(),game2.fieldToString());
    }

    @Test
    public void testTPipeClockwiseRotationCorrect(){
        Position sourcePos = new Position(0,0);
        String boardBeforeRotation =
                            "┣┫\n"
                        +   "┳┻\n";

        String boardAfterRotation =
                            "┳┻\n"
                        +   "┫┣\n";

        Logic game1 = new Logic(new FakeGUI(), boardBeforeRotation, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), boardAfterRotation, sourcePos);

        for(int i = 0;i < game1.getPlayField().getColumns();i++){
            for (int j = 0;j < game1.getPlayField().getRows();j++) {
                game1.rotated(new Position(i,j),false);
            }
        }
        assertEquals(game1.fieldToString(),game2.fieldToString());
    }

    @Test
    public void testTPipeCounterclockwiseRotationCorrect(){
        Position sourcePos = new Position(0,0);
        String boardBeforeRotation =
                            "┳┻\n"
                        +   "┫┣\n";

        String boardAfterRotation =
                            "┣┫\n"
                        +   "┳┻\n";

        Logic game1 = new Logic(new FakeGUI(), boardBeforeRotation, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), boardAfterRotation, sourcePos);

        for(int i = 0;i < game1.getPlayField().getColumns();i++){
            for (int j = 0;j < game1.getPlayField().getRows();j++) {
                game1.rotated(new Position(i,j),true);
            }
        }
        assertEquals(game1.fieldToString(),game2.fieldToString());
    }

    @Test
    public void testSourcePipeCanBeFilledByFillNextPipe(){
        Position sourcePos = new Position(0,0);
        String board =
                            "╺┓\n"
                        +   "╺┛\n";

        Logic game = new Logic(new FakeGUI(), board, sourcePos);

        game.fillNextPipe();
        assertTrue(game.getPlayField().getField(new Position(0,0)).getIsFilled());
    }

    @Test
    public void testOtherPipesCanBeFilledByfindNextFillablePipe(){
        Position sourcePos = new Position(0,0);
        String board =
                            "╺┓\n"
                        +   "╺┛\n";

        Logic game = new Logic(new FakeGUI(), board, sourcePos);

        game.fillNextPipe();
        game.fillNextPipe();
        assertTrue(game.getPlayField().getField(new Position(1,0)).getIsFilled());

        game.fillNextPipe();
        assertTrue(game.getPlayField().getField(new Position(1,1)).getIsFilled());

        game.fillNextPipe();
        assertTrue(game.getPlayField().getField(new Position(0,1)).getIsFilled());
    }

    @Test
    public void testResize_Bigger(){
        Position sourcePos = new Position(0,0);
        String board =
                          "┏━┳╸\n"
                        + "┃╻┗┓\n"
                        + "┗┻━┛\n";
        Logic game1 = new Logic(new FakeGUI(), board, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), board, sourcePos);

        int oldColumns = game1.getPlayField().getColumns();
        int oldRows = game1.getPlayField().getRows();

        int newColumns = 10;
        int newRows = 10;

        game2.resize(newColumns, newRows);

        assertEquals(10,game2.getPlayField().getColumns());
        assertEquals(10,game2.getPlayField().getRows());

        for(int i = 0;i < oldColumns;i++){
            for(int j = 0;j < oldRows;j++){
                Position pos = new Position(i,j);
                assertEquals(game1.getPlayField().getField(pos).toString(),
                             game2.getPlayField().getField(pos).toString());
            }
        }
    }

    @Test
    public void testResize_Smaller(){
        Position sourcePos = new Position(0,0);
        String board =
                          "┏━┳╸\n"
                        + "┃╻┗┓\n"
                        + "┗┻━┛\n";
        Logic game1 = new Logic(new FakeGUI(), board, sourcePos);
        Logic game2 = new Logic(new FakeGUI(), board, sourcePos);

        int newColumns = 2;
        int newRows = 2;

        game2.resize(newColumns, newRows);

        assertEquals(2,game2.getPlayField().getColumns());
        assertEquals(2,game2.getPlayField().getRows());

        for(int i = 0;i < newColumns;i++){
            for(int j = 0;j < newRows;j++){
                Position pos = new Position(i,j);
                assertEquals(game1.getPlayField().getField(pos).toString(),
                             game2.getPlayField().getField(pos).toString());
            }
        }
    }
}
