package CodeGeneration.Final;

public class CodeStringData {
    private String functionName;
    private int    numVars;
    private String numVarsString;
    private String[] varNames;
    private int[] ordMap;

    public CodeStringData(String functionName, int numVars, String[] varNames, int[] ordMap) {
        this.functionName = functionName;
        this.numVars      = numVars;
        this.varNames     = varNames;
        this.ordMap       = ordMap;
        numVarsString     = String.valueOf(numVars);
    }

    public String getAssemblyPrefix() {
        String prefix;
        String header = cat(
            line("# Generated assembly code header:"),
            tabLine(".file", quote(functionName + ".s")),
            tabLine(".option", "nopic"),
            tabLine(".text"),
            tabLine(".align", "1"),
            tabLine(".globl", functionName),
            tabLine(".type", (functionName + ", @function"))
        ); /*
        String prologue = cat(
            line("# Generated assembly code prologue:"),
            line(functionName + ":")
        );*/
        String prologue = cat(
            line("# Generated assembly code prologue:"),
            line(functionName + ":"),
            tabLine("ADDI", "sp, sp, -112"),
            tabLine("SD", "s0, 0(sp)"),
            tabLine("SD", "s1, 8(sp)"),
            tabLine("SD", "s2, 16(sp)"),
            tabLine("SD", "s3, 24(sp)"),
            tabLine("SD", "s4, 32(sp)"),
            tabLine("SD", "s5, 40(sp)"),
            tabLine("SD", "s6, 48(sp)"),
            tabLine("SD", "s7, 56(sp)"),
            tabLine("SD", "s8, 64(sp)"),
            tabLine("SD", "s9, 72(sp)"),
            tabLine("SD", "s10, 80(sp)"),
            tabLine("SD", "s11, 88(sp)")
        );
        prefix = header + prologue;
        return prefix;
    }

    public String getAssemblySuffix() {
        String suffix;
        /*
        String epilogue = cat(
            tabLine("# Generated assembly code epilogue:"),
            tabLine("JR", "ra")
        ); */

        String epilogue = cat(
            tabLine("# Generated assembly code epilogue:"),
            tabLine("LD", "s0, 0(sp)"),
            tabLine("LD", "s1, 8(sp)"),
            tabLine("LD", "s2, 16(sp)"),
            tabLine("LD", "s3, 24(sp)"),
            tabLine("LD", "s4, 32(sp)"),
            tabLine("LD", "s5, 40(sp)"),
            tabLine("LD", "s6, 48(sp)"),
            tabLine("LD", "s7, 56(sp)"),
            tabLine("LD", "s8, 64(sp)"),
            tabLine("LD", "s9, 72(sp)"),
            tabLine("LD", "s10, 80(sp)"),
            tabLine("LD", "s11, 88(sp)"),
            tabLine("ADDI", "sp, sp, 112"),
            tabLine("JR", "ra")
        );
        String footer = cat(
            tabLine("# Generated assembly code footer:"),
            tabLine(".size", (functionName + ", .-" + functionName)),
            tabLine(".ident", quote("Team No Compiler Errors: Del, Nathan, and Tanner"))
        );
        suffix = epilogue + footer;
        return suffix;
    }

    public String getAssemblyFileName() {
        return functionName + ".s";
    }

    public String toString() {
        return cat(
            IMPORTS,
            getGlobVarLines(),
            HEAD,
            ERROR_CHECK,
            VAR_ASSIGN,
            line(nest(1), "printf(\"Initial state:\\n\");"),
            STATE_PRINT,
            getFunctionCallLine(),
            line(nest(1), "printf(\"Final state:\\n\");"),
            STATE_PRINT,
            TAIL
        );
    }

    // Util constants
    final String INCL   = "#include ";
    final String LN     = "\n";
    final String TB     = "\t";
    final String DQT    = "\"";

    // Static C
    final String IMPORTS =
        cat(
            line(INCL, "<stdlib.h>"),
            line(INCL, "<stdio.h>"),
            line()
        );
    final String HEAD = cat(
        line("int main(int argc, char ** argv) {"),
        line(nest(1), "int i;")
    );
    final String ERROR_CHECK =
        cat(
            line(nest(1), "if (argc != numVars + 1) {"),
            line(nest(2), "printf(\"Parameters of %s: \", functionName);"),
            line(nest(2), "for (i = 0; i < numVars; i++)"),
            line(nest(3), "printf(\"%s \", varNames[ordMap[i]]);"),
            line(nest(2), "printf(\"\\n\");"),
            line(nest(2), "exit(1);"),
            line(nest(1), "}")
        );
    final String VAR_ASSIGN =
        cat(
            line(nest(1), "for (i = 0; i < numVars; i++)"),
            line(nest(2), "vars[ordMap[i]] = atol(argv[i + 1]);")
        );
    final String STATE_PRINT =
        cat(
            line(nest(1), "for (i = 0; i < numVars; i++)"),
            line(nest(2), "printf(\"%s=%ld\\n\", varNames[ordMap[i]], vars[ordMap[i]]);")
        );

    final String TAIL =
        cat(
            line("}")
        );

    // Dynamic C Producers
    public String getGlobVarLines() {
        String nameString = getNameString(true);
        String ordMapString = getOrdMapString();
        return line(
            line("extern void ", functionName, "(long int * vars);"),
            line("char * functionName = \"", functionName, "\";"),
            line("long int vars[", numVarsString, "];"),
            line("char * varNames[] = { ", nameString, " };"),
            line("int ordMap[] = { ", ordMapString, " };"),
            line("int numVars = ", numVarsString, ";")
        );
    }

    public String getFunctionCallLine() {
        return line(nest(1), functionName, "(vars);");
    }

    public String getNameString(Boolean addQuotes) {
        StringBuilder nameString = new StringBuilder();
        if (addQuotes) {
            for (int i = 0; i < numVars; i++) {
                nameString.append(quote(varNames[i]));
                if (i != (numVars - 1)) nameString.append(", ");
            }
        } else {
            for (int i = 0; i < numVars; i++) {
                nameString.append(varNames[i]);
                if (i != (numVars - 1)) nameString.append(", ");
            }
        }
        return nameString.toString();
    }
    public String getOrdMapString() {
        StringBuilder ordMapString = new StringBuilder();
        for (int i = 0; i < numVars; i++) {
            ordMapString.append(String.valueOf(ordMap[i]));
            if (i != (numVars - 1)) ordMapString.append(", ");
        }
        return ordMapString.toString();
    }

    // Util String Generators
    public String nest(int level) {
        StringBuilder nestString = new StringBuilder();
        while (level-- > 0) nestString.append(TB);
        return nestString.toString();
    }
    public String line(String... strings) {
        StringBuilder newString = new StringBuilder();
        for (String string : strings) newString.append(string);
        return newString + LN;
    }
    public String tabLine(String... strings) {
        StringBuilder newString = new StringBuilder();
        for (String string : strings) newString.append(TB + TB).append(string);
        return newString + LN;
    }
    public String cat(String... strings) {
        StringBuilder newString = new StringBuilder();
        for (String string : strings) newString.append(string);
        return newString.toString();
    }
    public String quote(String string) { return cat( DQT, string, DQT ); }
}
