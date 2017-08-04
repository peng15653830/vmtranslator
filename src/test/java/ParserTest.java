import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ParserTest {

    @Test
    public void testCreate(){
        Parser parser=new Parser();
        assertTrue(parser.hasMoreCommands());
    }

    @Test
    public void testAdvance(){
        Parser parser = new Parser();
        parser.advance();
        assertTrue(parser.hasMoreCommands());
        parser.advance();
        parser.advance();
        assertFalse(parser.hasMoreCommands());
    }

    @Test
    public void testCommandType(){
        Parser parser = new Parser();
        assertEquals(CommandType.C_PUSH,parser.commandType());
        parser.advance();
        assertEquals(CommandType.C_PUSH, parser.commandType());
        parser.advance();
        assertEquals(CommandType.C_ARITHMETIC, parser.commandType());
    }

    @Test
    public void testArg(){
        Parser parser = new Parser();
        assertEquals("constant",parser.arg1());
        assertEquals(new Integer(7),parser.arg2());
    }

}
