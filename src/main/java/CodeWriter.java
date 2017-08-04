import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CodeWriter {
    private Map<String, String> segmentMap = new HashMap<>();
    private String fileName;
    private FileWriter fileWriter;
    private PrintWriter printWriter;
    private String prefix = "SimpleAdd";
    private int index = 0;

    public CodeWriter(String fileName) {

        segmentMap.put("argument", "ARG");
        segmentMap.put("local", "LCL");
        segmentMap.put("this", "THIS");
        segmentMap.put("that", "THAT");
        segmentMap.put("pointer", "3");
        segmentMap.put("temp", "5");

        this.fileName = fileName;
        try {
            fileWriter = new FileWriter(fileName, true);
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void writeInit() {
        printWriter.println("//system init");
        printWriter.println("@256");
        printWriter.println("D=A");
        printWriter.println("@SP");
        printWriter.println("M=D");
        writeCall("Sys.init", 0);
    }

    public void writeCall(String functionName, int numArgs) {
        printWriter.println("//call "+functionName);
    }

    public void writeLabel(String label) {
        printWriter.println("(" + label + ")");
    }


    public void writeReturn() {
        printWriter.println("(RETURN)");
    }


    public void writeFunction(String functionName,int numLocals) {
        printWriter.println("(" +functionName + ")");
        printWriter.println("@"+numLocals);
        printWriter.println("D=A");
        printWriter.println("@R13");
        printWriter.println("M=D");
        printWriter.println("("+functionName+"_loop_local)");
        printWriter.println("@R13");
        printWriter.println("MD=M-1");
        printWriter.println("@"+functionName+"_loop_local_end");
        printWriter.println("D;JLT");
        printWriter.println("@LCL");
        printWriter.println("A=M");
        printWriter.println("M=0");
        printWriter.println("@LCL");
        printWriter.println("A=M-1");
        printWriter.println("M=0");
        printWriter.println("@"+functionName+"_loop_local");
        printWriter.println("0;JMP");
        printWriter.println("("+functionName+"_loop_local_end)");
    }


    public void writeGoto(String label) {
        printWriter.println("//goto "+label);
        printWriter.println("@" + label);
        printWriter.println("0;JMP");
    }

    public void writeIf(String label) {
        printWriter.println("//goto-if "+label);
        printWriter.println("@SP");
        printWriter.println("AM=M-1");
        printWriter.println("D=M");
        printWriter.println("@" + label);
        printWriter.println("D;JNE");
    }

    public void writeArithmetic(String command) {
        printWriter.println("//"+command);
        if ("add".equals(command)) {
            printWriter.println("@SP");
            printWriter.println("AM=M-1");
            printWriter.println("D=M");
            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("M=M+D");
        } else if ("sub".equals(command)) {
            printWriter.println("@SP");
            printWriter.println("AM=M-1");
            printWriter.println("D=M");
            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("M=M-D");
        } else if ("neg".equals(command)) {
            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("M=-M");
        } else if ("eq".equals(command) || ("gt".equals(command)) || ("lt").equals(command)) {
            printWriter.println("@SP");
            printWriter.println("AM=M-1");
            printWriter.println("D=M");
            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("D=M-D");

            printWriter.println("@CON_" + command.toUpperCase() + "_" + index + "");
            printWriter.println("D;J" + command.toUpperCase() + "");

            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("M=0");
            printWriter.println("@CON_END_" + index + "");
            printWriter.println("0;JMP");

            printWriter.println("(CON_" + command.toUpperCase() + "_" + index + ")");
            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("M=-1");

            printWriter.println("(CON_END_" + index + ")");

            index++;
        } else if ("not".equals(command)) {
            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("M=!M");
        } else if ("and".equals(command)) {
            printWriter.println("@SP");
            printWriter.println("AM=M-1");
            printWriter.println("D=M");
            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("M=D&M");
        } else if ("or".equals(command)) {
            printWriter.println("@SP");
            printWriter.println("AM=M-1");
            printWriter.println("D=M");
            printWriter.println("@SP");
            printWriter.println("A=M-1");
            printWriter.println("M=D|M");
        }
    }

    public void writePushPop(CommandType commandType, String segment, int index) {
        if (CommandType.C_PUSH.equals(commandType)) {
            printWriter.println("//push "+segment+" "+index);
            if ("constant".equals(segment)) {
                printWriter.println("@" + index);
                printWriter.println("D=A");
                printWriter.println("@SP");
                printWriter.println("A=M");
                printWriter.println("M=D");
                printWriter.println("@SP");
                printWriter.println("M=M+1");
            } else if ("static".equals(segment)) {
                printWriter.println("@" + prefix + "." + index);
                printWriter.println("D=M");
                printWriter.println("@SP");
                printWriter.println("A=M");
                printWriter.println("M=D");
                printWriter.println("@SP");
                printWriter.println("M=M+1");
            } else if ("pointer".equals(segment) || "temp".equals(segment)) {
                String ramAlias = segmentMap.get(segment);
                printWriter.println("@" + ramAlias + "");
                printWriter.println("D=A");
                printWriter.println("@" + index);
                printWriter.println("A=D+A");
                printWriter.println("D=M");
                printWriter.println("@SP");
                printWriter.println("A=M");
                printWriter.println("M=D");
                printWriter.println("@SP");
                printWriter.println("M=M+1");
            } else {
                String ramAlias = segmentMap.get(segment);
                printWriter.println("@" + ramAlias);
                printWriter.println("D=M");
                printWriter.println("@" + index);
                printWriter.println("A=D+A");
                printWriter.println("D=M");
                printWriter.println("@SP");
                printWriter.println("A=M");
                printWriter.println("M=D");
                printWriter.println("@SP");
                printWriter.println("M=M+1");
            }
        } else if (CommandType.C_POP.equals(commandType)) {
            printWriter.println("//pop "+segment+" "+index);
            if ("static".equals(segment)) {
                printWriter.println("@SP");
                printWriter.println("AM=M-1");
                printWriter.println("D=M");
                printWriter.println("@" + prefix + "." + index);
                printWriter.println("M=D");
            } else if ("pointer".equals(segment) || "temp".equals(segment)) {
                String ramAlias = segmentMap.get(segment);
                printWriter.println("@" + ramAlias);
                printWriter.println("D=A");
                printWriter.println("@" + index);
                printWriter.println("D=D+A");
                printWriter.println("@R13");//中间结果存入通用寄存器
                printWriter.println("M=D");

                printWriter.println("@SP");
                printWriter.println("AM=M-1");
                printWriter.println("D=M");
                printWriter.println("@R13");
                printWriter.println("A=M");
                printWriter.println("M=D");
            } else {
                String ramAlias = segmentMap.get(segment);
                printWriter.println("@" + ramAlias);
                printWriter.println("D=M");
                printWriter.println("@" + index);
                printWriter.println("D=D+A");
                printWriter.println("@R13");//中间结果存入通用寄存器
                printWriter.println("M=D");

                printWriter.println("@SP");
                printWriter.println("AM=M-1");
                printWriter.println("D=M");
                printWriter.println("@R13");
                printWriter.println("A=M");
                printWriter.println("M=D");
            }
        }
    }

    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printWriter.close();

    }
}
