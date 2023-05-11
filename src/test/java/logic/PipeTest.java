package logic;

import org.junit.Test;
import static org.junit.Assert.*;

public class PipeTest {

    @Test
    public void testGetEndPipeNumberCorrect(){
        Pipe pipe = new Pipe(PipeType.END_PIPE);

        pipe.setRotation(Rotation.ZERO);
        assertEquals(1,pipe.getPipeNumber());

        pipe.setRotation(Rotation.NINETY);
        assertEquals(2,pipe.getPipeNumber());

        pipe.setRotation(Rotation.ONE_EIGHTY);
        assertEquals(4,pipe.getPipeNumber());

        pipe.setRotation(Rotation.TWO_SEVENTY);
        assertEquals(8,pipe.getPipeNumber());
    }

    @Test
    public void testGetStraightPipeNumberCorrect(){
        Pipe pipe = new Pipe(PipeType.STRAIGHT_PIPE);

        pipe.setRotation(Rotation.ZERO);
        assertEquals(5,pipe.getPipeNumber());

        pipe.setRotation(Rotation.NINETY);
        assertEquals(10,pipe.getPipeNumber());
    }

    @Test
    public void testGetCurvePipeNumberCorrect(){
        Pipe pipe = new Pipe(PipeType.CURVE_PIPE);

        pipe.setRotation(Rotation.ZERO);
        assertEquals(3,pipe.getPipeNumber());

        pipe.setRotation(Rotation.NINETY);
        assertEquals(6,pipe.getPipeNumber());

        pipe.setRotation(Rotation.ONE_EIGHTY);
        assertEquals(12,pipe.getPipeNumber());

        pipe.setRotation(Rotation.TWO_SEVENTY);
        assertEquals(9,pipe.getPipeNumber());
    }

    @Test
    public void testGetTPipeNumberCorrect(){
        Pipe pipe = new Pipe(PipeType.T_PIPE);

        pipe.setRotation(Rotation.ZERO);
        assertEquals(7,pipe.getPipeNumber());

        pipe.setRotation(Rotation.NINETY);
        assertEquals(14,pipe.getPipeNumber());

        pipe.setRotation(Rotation.ONE_EIGHTY);
        assertEquals(13,pipe.getPipeNumber());

        pipe.setRotation(Rotation.TWO_SEVENTY);
        assertEquals(11,pipe.getPipeNumber());
    }

    @Test
    public void testCheckIsOpenToTop(){
        Pipe pipe = new Pipe(PipeType.T_PIPE,Rotation.ZERO);

        assertTrue(pipe.isOpenToTop());
    }

    @Test
    public void testCheckIsNOTOpenToTop(){
        Pipe pipe = new Pipe(PipeType.T_PIPE,Rotation.NINETY);

        assertFalse(pipe.isOpenToTop());
    }

    @Test
    public void testCheckIsOpenToRight(){
        Pipe pipe = new Pipe(PipeType.T_PIPE,Rotation.ZERO);

        assertTrue(pipe.isOpenToRight());
    }

    @Test
    public void testCheckIsNOTOpenToRight(){
        Pipe pipe = new Pipe(PipeType.T_PIPE,Rotation.ONE_EIGHTY);

        assertFalse(pipe.isOpenToRight());
    }

    @Test
    public void testCheckIsOpenToBottom(){
        Pipe pipe = new Pipe(PipeType.T_PIPE,Rotation.ZERO);

        assertTrue(pipe.isOpenToBottom());
    }

    @Test
    public void testCheckIsNOTOpenToBottom(){
        Pipe pipe = new Pipe(PipeType.T_PIPE,Rotation.TWO_SEVENTY);

        assertFalse(pipe.isOpenToBottom());
    }

    @Test
    public void testCheckIsOpenToLeft(){
        Pipe pipe = new Pipe(PipeType.T_PIPE,Rotation.NINETY);

        assertTrue(pipe.isOpenToLeft());
    }

    @Test
    public void testCheckIsNOTOpenToLeft(){
        Pipe pipe = new Pipe(PipeType.T_PIPE,Rotation.ZERO);

        assertFalse(pipe.isOpenToLeft());
    }

    @Test
    public void testAddOpeningDirection(){
        Pipe pipe = new Pipe(PipeType.WALL);

        pipe.addOpenDirection(Direction.TOP);
        assertEquals(PipeType.END_PIPE, pipe.getPipeType());
        assertEquals(Rotation.ZERO,pipe.getRotation());

        pipe.addOpenDirection(Direction.RIGHT);
        assertEquals(PipeType.CURVE_PIPE, pipe.getPipeType());
        assertEquals(Rotation.ZERO,pipe.getRotation());

        pipe.addOpenDirection(Direction.LEFT);
        assertEquals(PipeType.T_PIPE, pipe.getPipeType());
        assertEquals(Rotation.TWO_SEVENTY,pipe.getRotation());
    }
}
