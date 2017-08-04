public class Client {
    public void translate() {
        Parser parser = new Parser();
        CodeWriter codeWriter = new CodeWriter("D:\\Program Files\\nand2tetris\\projects\\08\\ProgramFlow\\FibonacciSeries\\FibonacciSeries.asm");
        while (parser.hasMoreCommands()) {
            CommandType commandType = parser.commandType();
            if (CommandType.C_PUSH.equals(commandType) || CommandType.C_POP.equals(commandType)) {
                codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
            } else if (CommandType.C_ARITHMETIC.equals(commandType)) {
                codeWriter.writeArithmetic(parser.arg1());
            } else if (CommandType.C_LABEL.equals(commandType)) {
                codeWriter.writeLabel(parser.arg1());
            } else if (CommandType.C_GOTO.equals(commandType)) {
                codeWriter.writeGoto(parser.arg1());
            } else if (CommandType.C_IF.equals(commandType)) {
                codeWriter.writeIf(parser.arg1());
            }
            parser.advance();
        }
        codeWriter.close();
    }
}
