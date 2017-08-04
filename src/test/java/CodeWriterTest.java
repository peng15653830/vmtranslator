import org.junit.Test;

public class CodeWriterTest {

    @Test
    public void testCreate() {
        CodeWriter codeWriter = new CodeWriter("D:\\Program Files\\nand2tetris\\projects\\07\\StackArithmetic\\SimpleAdd\\SimpleAdd.asm");
        codeWriter.close();
    }

    @Test
    public void testPushPop() {
        CodeWriter codeWriter = new CodeWriter("D:\\Program Files\\nand2tetris\\projects\\07\\StackArithmetic\\SimpleAdd\\SimpleAdd.asm");
        codeWriter.writePushPop(CommandType.C_PUSH, "pointer", 0);
        codeWriter.writePushPop(CommandType.C_PUSH, "argument", 3);
        codeWriter.writePushPop(CommandType.C_POP, "argument", 3);
        codeWriter.writePushPop(CommandType.C_PUSH, "static", 3);
        codeWriter.writePushPop(CommandType.C_POP, "static", 3);
        codeWriter.close();
    }

    @Test
    public void testArithmetic() {
        CodeWriter codeWriter = new CodeWriter("D:\\Program Files\\nand2tetris\\projects\\07\\StackArithmetic\\SimpleAdd\\SimpleAdd.asm");
//        codeWriter.writeArithmetic("add");
        codeWriter.writeArithmetic("eq");
        codeWriter.writeArithmetic("gt");
        codeWriter.writeArithmetic("lt");
        codeWriter.close();
    }

}
