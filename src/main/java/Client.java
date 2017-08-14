import java.io.File;

public class Client {
    public void translate() {
        String filePath = "D:\\Program Files\\nand2tetris\\projects\\08\\FunctionCalls\\NestedCall";
        final String[] fileNames =
                new File(filePath).list(new java.io.FilenameFilter() {
                    public boolean accept(final File dir, final String name) {
                        return name.endsWith(".vm");
                    }
                });
        CodeWriter codeWriter = new CodeWriter("D:\\Program Files\\nand2tetris\\projects\\08\\FunctionCalls\\NestedCall\\NestedCall.asm");
        int functionIndex = 0;
        for (String fileName : fileNames) {
            if (fileName.startsWith("Sys.vm")) {
                Parser parser = new Parser(filePath + "\\" + fileName);
                codeWriter.writeInit();
                codeWriter.writeCall(parser.arg1() + "_" + functionIndex, parser.arg2());
                functionIndex++;
                while (parser.hasMoreCommands()) {
                    functionIndex = compileLineCommand(parser, codeWriter, functionIndex);
                    parser.advance();
                }
            }
        }

        for (String fileName : fileNames) {
            if (!fileName.startsWith("Sys.vm")) {
                Parser parser = new Parser(filePath + "\\" + fileName);
                while (parser.hasMoreCommands()) {
                    functionIndex = compileLineCommand(parser, codeWriter, functionIndex);
                    parser.advance();
                }
            }
        }
        codeWriter.close();
    }

    private int compileLineCommand(Parser parser, CodeWriter codeWriter, int functionIndex) {
        CommandType commandType = parser.commandType();
        String functionName = "";
        if (CommandType.C_PUSH.equals(commandType) || CommandType.C_POP.equals(commandType)) {
            codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
        } else if (CommandType.C_ARITHMETIC.equals(commandType)) {
            codeWriter.writeArithmetic(parser.arg1());
        } else if (CommandType.C_LABEL.equals(commandType)) {
            codeWriter.writeLabel(functionName + "$" + parser.arg1());
        } else if (CommandType.C_GOTO.equals(commandType)) {
            codeWriter.writeGoto(functionName + "$" + parser.arg1());
        } else if (CommandType.C_IF.equals(commandType)) {
            codeWriter.writeIf(functionName + "$" + parser.arg1());
        } else if (CommandType.C_FUNCTION.equals(commandType)) {
            codeWriter.writeFunction(parser.arg1(), parser.arg2());
            functionName = parser.arg1();
        } else if (CommandType.C_CAll.equals(commandType)) {
            codeWriter.writeCall(parser.arg1() + "_" + functionIndex, parser.arg2());
            functionIndex++;
        } else if (CommandType.C_RETURN.equals(commandType)) {
            codeWriter.writeReturn();
        }
        return functionIndex;
    }


}
