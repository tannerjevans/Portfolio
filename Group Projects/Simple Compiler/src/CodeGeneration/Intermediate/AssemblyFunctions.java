package CodeGeneration.Intermediate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class AssemblyFunctions {

    public List<String> registers;
    public List<String> tempRegisters;
    public List<String> memStorage;
    public List<String> sRegisters;
    public Stack<String> whileStack;
    public Stack<String> elseStack;
    public Stack<String> exitStack;
    public int elseExitCounter;
    public int notCounter;
    public int mulCounter;
    private int whileExitCounter;
    public boolean ISDEBUGGING;
    private String fileString;

    public AssemblyFunctions()
    {
        registers = new ArrayList<>();
        tempRegisters = new ArrayList<>();
        memStorage = new ArrayList<>();
        sRegisters = new ArrayList<>();
        whileStack = new Stack<>();
        elseStack = new Stack<>();
        exitStack = new Stack<>();
        elseExitCounter = 0;
        notCounter = 0;
        mulCounter = 0;
        whileExitCounter = 0;
        ISDEBUGGING = false;
        fileString = "";
    }

    private void addToFileString(String toAdd)
    {
        fileString += ("\t\t" + toAdd + "\n");
    }

    private void addLabelToFile(String toAdd)
    {
        fileString += ("\t" + toAdd + "\n");
    }

    public void addPrefix(String prefix){
        fileString = prefix + fileString;
    }

    public void addSuffix(String suffix){
        fileString = fileString + suffix;
    }

    public void addHeader()
    {
        fileString += ".extern main\n";
        fileString += "main:\n";
    }

    public void printFileString()
    {
        System.out.println(fileString);
    }

    public void initializeMemory(List<String> mList)
    {
        for(int i = 0; (i < 11 && i < mList.size()); i++)
        {
            memStorage.add(mList.get(i));
            sRegisters.add(mList.get(i));
            addToFileString("LW" + " s" + (i + 1) + ", " + i*8 +"(a0)");
        }
        if(mList.size() > 11)
        {
            for(int i = 11; i < mList.size(); i++)
            {
                memStorage.add(mList.get(i));
            }
        }

        //System.out.println("sRegs, size "+ sRegisters.size() + ":");
        for (String sRegister : sRegisters) {
            //System.out.println(sRegister);
        }
        //System.out.println("memory, size " + memStorage.size() + ":");
        for (String s : memStorage) {
            //System.out.println(s);
        }
    }

    public void saveMemory()
    {
        for(int i = 0; i < sRegisters.size(); i++)
        {
            addToFileString("SW s" + (i + 1) + ", " + (8 * i) + "(a0)");
        }
    }

    public void printToFile(String fileName)
    {
        String filename = ".\\output.s";

        File file = new File(fileName);

        try{
            boolean result = Files.deleteIfExists(file.toPath());
        }catch (IOException x){
            System.err.println(x);
        }

        byte[] data = fileString.getBytes();
        Path p = Paths.get(fileName);

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    private void addVariable(String variableName)
    {
        if(registers.size() < 8)
        {
            registers.add(variableName);
        }
        else
        {
            //Handle changing full registers here
        }
    }

    private void addTempVariable(String variableName)
    {
        tempRegisters.add(variableName);
    }

    private String memLookupStore(String variableName)
    {
        if(sRegisters.indexOf(variableName) == -1) {
            int memIndex = memStorage.indexOf(variableName);
            if (memIndex > -1) {
                String tempName = tempLookupOrAdd(variableName);
                //System.out.println("LW" + " " + tempName + ", " + memIndex*8 +"(a0)");
                return ("LW" + " " + tempName + ", " + memIndex * 8 + "(a0)\n");
                //return "t" + tempRegisters.indexOf(tempName);
            }
        }
        return "";
    }

    private String regLookupNoFile(String variableName)
    {
        int index = sRegisters.indexOf(variableName);
        if(index > -1)
            return "s" + (index + 1); index = registers.indexOf(variableName);
        if(index > -1)
            return "a" + (index + 1);
        else
        {
            index = tempRegisters.indexOf(variableName);
            if(index > -1)
                return "t" + index;
            else
                return "VARIABLE NOT FOUND";
        }
    }

    private String regLookup(String variableName)
    {

        int index = sRegisters.indexOf(variableName);
        if(index > -1)
            return "s" + (index + 1); index = registers.indexOf(variableName);
        int memIndex = memStorage.indexOf(variableName);
        if( memIndex > -1)
        {
            String tempName = tempLookupOrAdd(variableName);
            //System.out.println("LW" + " " + tempName + ", " + memIndex*8 +"(a0)");
            addToFileString("LW" + " " + tempName + ", " + memIndex*8 +"(a0)");
            //return "t" + tempRegisters.indexOf(tempName);
        }
        if(index > -1)
            return "a" + (index + 1);
        else
        {
            index = tempRegisters.indexOf(variableName);
            if(index > -1)
                return "t" + index;
            else
                return "VARIABLE NOT FOUND";
        }
    }

    private int memIndexLookup(String variableName)
    {
        int index = memStorage.indexOf(variableName);
        if(index > -1)
            return index * 8;
        else
        {
            return -1;
        }
    }

    private String tempLookupOrAdd(String variableName)
    {
        int index = tempRegisters.indexOf(variableName);
        if(index > -1)
            return "t" + index;
        else
        {
            for(int i = 0; i < tempRegisters.size(); i++)
            {
                if(tempRegisters.get(i).equals("REP"))
                {
                    tempRegisters.set(i, variableName);
                    return ("t" + i);
                }
            }
            addTempVariable(variableName);
            return "t" + (tempRegisters.size() - 1);
        }
    }

    //Does not actually remove the register, but replaces it with a dummy REP value
    private void removeTemp(String variableName)
    {
        int index = tempRegisters.indexOf(variableName);
        if(index > -1)
        {
            tempRegisters.set(index, "REP");
        }
    }

    private String regLookupOrAdd(String variableName)
    {
        int index = sRegisters.indexOf(variableName);
        if(index > -1)
            return "s" + (index + 1);
        index = registers.indexOf(variableName);
        if(index > -1)
            return "a" + (index + 1);
        else
        {
            addVariable(variableName);
            return "a" + (registers.indexOf(variableName) + 1);
        }
    }

    private void clearARegs()
    {
        registers.clear();
    }

    private String getFirstEmptyTempIndex(int offset)
    {
        int index = tempRegisters.size() + offset;
        return ("t" + index);
    }

    private void CheckIfNeedToSave(String destination)
    {
        if(sRegisters.contains(destination)) {
        }
        else
        {
            addToFileString("SW " + regLookupOrAdd(destination) + ", " + memIndexLookup(destination)  + "(a0)");
        }
    }

    public void ASSIGN(String destination, String operand1)
    {
        if(ISDEBUGGING)
            System.out.println("ADD " + regLookupOrAdd(destination) + ", x0" + ", " + regLookup(operand1));
        addToFileString("ADD " + regLookupOrAdd(destination) + ", x0" + ", " + regLookup(operand1));
        CheckIfNeedToSave(destination);
        tempRegisters.clear();
        clearARegs();
    }

    public void ASSIGN(String destination, int value)
    {
        if(ISDEBUGGING)
            System.out.println("ADDI " + regLookupOrAdd(destination) + ", x0" + ", " + value);
        addToFileString("ADDI " + regLookupOrAdd(destination) + ", x0" + ", " + value);
        CheckIfNeedToSave(destination);
        tempRegisters.clear();
        clearARegs();
    }

    public void ADD(String destination, String operand1, String operand2)
    {
        if(ISDEBUGGING)
            System.out.println("ADD " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));


        addToFileString("ADD " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        removeTemp(operand1);
        removeTemp(operand2);
    }

    public void ADDI(String destination, String operand1, int immediateValue)
    {
        if(ISDEBUGGING)
            System.out.println("ADDI " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + immediateValue);
        addToFileString("ADDI " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + immediateValue);
        removeTemp(operand1);
    }

    public void ADDI(String destination, int immediateValue, String operand2)
    {
        if(ISDEBUGGING)
            System.out.println("ADDI " + tempLookupOrAdd(destination) + ", " + regLookup(operand2) + ", " + immediateValue);
        addToFileString("ADDI " + tempLookupOrAdd(destination) + ", " + regLookup(operand2) + ", " + immediateValue);
        removeTemp(operand2);
    }

    public void ADDI(String destination, int value1, int value2)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING) {
            System.out.println("ADDI " + temp0 + ", x0, " + value1);
            System.out.println("ADDI " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + value2);
        }
        addToFileString("ADDI " + temp0 + ", x0, " + value1);
        addToFileString("ADDI " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + value2);
    }

    public void SUB(String destination, String operand1, String operand2)
    {
        if(ISDEBUGGING)
            System.out.println("SUB " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        addToFileString("SUB " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        removeTemp(operand1);
        removeTemp(operand2);
    }

    public void SUB(String destination, String operand1, int immediateValue)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING)
        {
            System.out.println("ADDI " + temp0 + ", x0, " + immediateValue);
            System.out.println("SUB " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + temp0);
        }
        addToFileString("ADDI " + temp0 + ", x0, " + immediateValue);
        addToFileString("SUB " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + temp0);
        removeTemp(operand1);
    }
    public void SUB(String destination, int immediateValue, String operand2)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING) {
            System.out.println("ADDI " + temp0 + ", x0, " + immediateValue);
            System.out.println("SUB " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + regLookup(operand2));
        }
        addToFileString("ADDI " + temp0 + ", x0, " + immediateValue);
        addToFileString("SUB " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + regLookup(operand2));
        removeTemp(operand2);
    }
    public void SUB(String destination, int operand1, int operand2)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING)
            System.out.println("ADDI " + temp0 + ", x0, " + operand1);
        addToFileString("ADDI " + temp0 + ", x0, " + operand1);
        String temp1 = getFirstEmptyTempIndex(1);
        if(ISDEBUGGING)
        {
            System.out.println("ADDI " + temp0 + ", x0, " + operand2);
            System.out.println("SUB " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + temp1);
        }
        addToFileString("ADDI " + temp0 + ", x0, " + operand2);
        addToFileString("SUB " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + temp1);
    }

    public void MUL(String destination, String operand1, String operand2)
    {
        if(ISDEBUGGING)
            System.out.println("MUL " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        addToFileString("MUL " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        removeTemp(operand1);
        removeTemp(operand2);
    }

    public void MUL(String destination, String operand1, int immediateValue)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING) {
            System.out.println("ADDI " + temp0 + ", x0, " + immediateValue);
            System.out.println("MUL " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + temp0);
        }
        addToFileString("ADDI " + temp0 + ", x0, " + immediateValue);
        addToFileString("MUL " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + temp0);
        removeTemp(operand1);
    }
    public void MUL(String destination, int operand1, String operand2)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING) {
            System.out.println("ADDI " + temp0 + ", x0, " + operand1);
            System.out.println("MUL " + tempLookupOrAdd(destination) + ", " + regLookup(operand2) + ", " + temp0);
        }
        addToFileString("ADDI " + temp0 + ", x0, " + operand1);
        addToFileString("MUL " + tempLookupOrAdd(destination) + ", " + regLookup(operand2) + ", " + temp0);
        removeTemp(operand2);
    }
    public void MUL(String destination, int operand1, int immediateValue)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING)
            System.out.println("ADDI " + temp0 + ", x0, " + operand1);
        addToFileString("ADDI " + temp0 + ", x0, " + operand1);
        String temp1 = getFirstEmptyTempIndex(1);
        if(ISDEBUGGING) {
            System.out.println("ADDI " + temp0 + ", x0, " + immediateValue);
            System.out.println("MUL " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + temp1);
        }
        addToFileString("ADDI " + temp1 + ", x0, " + immediateValue);
        addToFileString("MUL " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + temp1);
    }

    public void AND(String destination, String operand1, String operand2)
    {
        if(ISDEBUGGING)
            System.out.println("AND " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        addToFileString("AND " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        removeTemp(operand1);
        removeTemp(operand2);
    }

    public void ANDI(String destination, String operand1, int immediateValue)
    {
        if(ISDEBUGGING)
            System.out.println("ANDI " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + immediateValue);
        addToFileString("ANDI " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + immediateValue);
        removeTemp(operand1);
    }

    public void ANDI(String destination, int operand1, String operand2)
    {
        if(ISDEBUGGING)
            System.out.println("ANDI " + tempLookupOrAdd(destination) + ", " + regLookup(operand2) + ", " + operand1);
        addToFileString("ANDI " + tempLookupOrAdd(destination) + ", " + regLookup(operand2) + ", " + operand1);
        removeTemp(operand2);
    }

    public void ANDI(String destination, int operand1, int operand2)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING) {
            System.out.println("ADDI " + temp0 + ", x0, " + operand1);
            System.out.println("ANDI " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + operand2);
        }
            addToFileString("ADDI " + temp0 + ", x0, " + operand1);
            addToFileString("ANDI " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + operand2);

    }
    public void OR(String destination, String operand1, String operand2)
    {
        if(ISDEBUGGING)
            System.out.println("OR " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        addToFileString("OR " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + regLookup(operand2));
        removeTemp(operand1);
        removeTemp(operand2);
    }

    public void ORI(String destination, String operand1, int immediateValue)
    {
        if(ISDEBUGGING)
            System.out.println("ORI " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + immediateValue);
        addToFileString("ORI " + tempLookupOrAdd(destination) + ", " + regLookup(operand1) + ", " + immediateValue);
        removeTemp(operand1);
    }

    public void ORI(String destination, int operand1, String operand2)
    {
        if(ISDEBUGGING)
            System.out.println("ORI " + tempLookupOrAdd(destination) + ", " + regLookup(operand2) + ", " + operand1);
        addToFileString("ORI " + tempLookupOrAdd(destination) + ", " + regLookup(operand2) + ", " + operand1);
        removeTemp(operand2);
    }

    public void ORI(String destination, int operand1, int operand2)
    {
        String temp0 = getFirstEmptyTempIndex(0);
        if(ISDEBUGGING) {
            System.out.println("ADDI " + temp0 + ", x0, " + operand1);
            System.out.println("ORI " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + operand2);
        }
        addToFileString("ADDI " + temp0 + ", x0, " + operand1);
        addToFileString("ORI " + tempLookupOrAdd(destination) + ", " + temp0 + ", " + operand2);
    }

    public void WHILE(String label, String checkType, String reg1, String reg2)
    {
        if(ISDEBUGGING)
            System.out.println(label + ":");
        addInitialWhileCheck(checkType,reg1,reg2);
        addLabelToFile(label + ":");
        String endLabel = "\n\tOD" + whileExitCounter +":";
        whileExitCounter++;
        switch (checkType) {
            case "=" -> whileStack.push(BEQ(reg1, reg2, label) + endLabel);
            case ">" -> whileStack.push(BGT(reg1, reg2, label) + endLabel);
            case ">=" -> whileStack.push(BGE(reg1, reg2, label) + endLabel);
            case "<" -> whileStack.push(BLT(reg1, reg2, label) + endLabel);
            case "<=" -> whileStack.push(BLE(reg1, reg2, label) + endLabel);
            default -> System.out.println("ERROR: Invalid while argument");
        }
    }

    public void WHILE(String label, String checkType, int reg1, String reg2)
    {
        if(ISDEBUGGING)
            System.out.println(label + ":");
        addInitialWhileCheck(checkType,reg1,reg2);
        addLabelToFile(label + ":");
        String endLabel = "\n\tOD" + whileExitCounter +":";
        whileExitCounter++;
        switch (checkType) {
            case "=" -> whileStack.push(BEQ(reg1, reg2, label) + endLabel);
            case ">" -> whileStack.push(BGT(reg1, reg2, label) + endLabel);
            case ">=" -> whileStack.push(BGE(reg1, reg2, label) + endLabel);
            case "<" -> whileStack.push(BLT(reg1, reg2, label) + endLabel);
            case "<=" -> whileStack.push(BLE(reg1, reg2, label) + endLabel);
            default -> System.out.println("ERROR: Invalid while argument");
        }
    }

    public void WHILE(String label, String checkType, String reg1, int reg2)
    {
        if(ISDEBUGGING)
            System.out.println(label + ":");
        addInitialWhileCheck(checkType,reg1,reg2);
        addLabelToFile(label + ":");
        String endLabel = "\n\tOD" + whileExitCounter +":";
        whileExitCounter++;
        switch (checkType) {
            case "=" -> whileStack.push(BEQ(reg1, reg2, label) + endLabel);
            case ">" -> whileStack.push(BGT(reg1, reg2, label) + endLabel);
            case ">=" -> whileStack.push(BGE(reg1, reg2, label) + endLabel);
            case "<" -> whileStack.push(BLT(reg1, reg2, label) + endLabel);
            case "<=" -> whileStack.push(BLE(reg1, reg2, label) + endLabel);
            default -> System.out.println("ERROR: Invalid while argument");
        }
    }

    public void WHILE(String label, String checkType, int reg1, int reg2)
    {
        if(ISDEBUGGING)
            System.out.println(label + ":");
        addInitialWhileCheck(checkType,reg1,reg2);
        addLabelToFile(label + ":");
        String endLabel = "\n\tOD" + whileExitCounter +":";
        whileExitCounter++;
        switch (checkType) {
            case "=" -> whileStack.push(BEQ(reg1, reg2, label) + endLabel);
            case ">" -> whileStack.push(BGT(reg1, reg2, label) + endLabel);
            case ">=" -> whileStack.push(BGE(reg1, reg2, label) + endLabel);
            case "<" -> whileStack.push(BLT(reg1, reg2, label) + endLabel);
            case "<=" -> whileStack.push(BLE(reg1, reg2, label) + endLabel);
            default -> System.out.println("ERROR: Invalid while argument");
        }
    }

    public void IF(String checkType, String reg1, String reg2)
    {
        elseStack.push("ELSE" + elseExitCounter);
        exitStack.push("EXIT" + elseExitCounter);
        elseExitCounter++;
        String elseLabel = elseStack.peek();

        switch (checkType) {
            case "=" ->
                //System.out.println(BNE(reg1, reg2, elseLabel));
                addToFileString(BNE(reg1, reg2, elseLabel));
            case ">" ->
                //System.out.println(BLE(reg1, reg2, elseLabel));
                addToFileString(BLE(reg1, reg2, elseLabel));
            case ">=" ->
                //System.out.println(BLT(reg1, reg2, elseLabel));
                addToFileString(BLT(reg1, reg2, elseLabel));
            case "<" ->
                //System.out.println(BGE(reg1, reg2, elseLabel));
                addToFileString(BGE(reg1, reg2, elseLabel));
            case "<=" ->
                //System.out.println(BGT(reg1, reg2, elseLabel));
                addToFileString(BGT(reg1, reg2, elseLabel));
            default -> System.out.println("ERROR: Invalid if argument");
        }
    }
    public void IF(String checkType, int reg1, String reg2)
    {
        elseStack.push("ELSE" + elseExitCounter);
        exitStack.push("EXIT" + elseExitCounter);
        elseExitCounter++;
        String elseLabel = elseStack.peek();

        switch (checkType) {
            case "=" ->
                //System.out.println(BNE(reg1, reg2, elseLabel));
                addToFileString(BNE(reg1, reg2, elseLabel));
            case ">" ->
                // System.out.println(BLE(reg1, reg2, elseLabel));
                addToFileString(BLE(reg1, reg2, elseLabel));
            case ">=" ->
                //System.out.println(BLT(reg1, reg2, elseLabel));
                addToFileString(BLT(reg1, reg2, elseLabel));
            case "<" ->
                //System.out.println(BGE(reg1, reg2, elseLabel));
                addToFileString(BGE(reg1, reg2, elseLabel));
            case "<=" ->
                //System.out.println(BGT(reg1, reg2, elseLabel));
                addToFileString(BGT(reg1, reg2, elseLabel));
            default -> System.out.println("ERROR: Invalid if argument");
        }
    }
    public void IF(String checkType, String reg1, int reg2)
    {
        elseStack.push("ELSE" + elseExitCounter);
        exitStack.push("EXIT" + elseExitCounter);
        elseExitCounter++;
        String elseLabel = elseStack.peek();

        switch (checkType) {
            case "=" ->
                //System.out.println(BNE(reg1, reg2, elseLabel));
                addToFileString(BNE(reg1, reg2, elseLabel));
            case ">" ->
                //System.out.println(BLE(reg1, reg2, elseLabel));
                addToFileString(BLE(reg1, reg2, elseLabel));
            case ">=" ->
                // System.out.println(BLT(reg1, reg2, elseLabel));
                addToFileString(BLT(reg1, reg2, elseLabel));
            case "<" ->
                //System.out.println(BGE(reg1, reg2, elseLabel));
                addToFileString(BGE(reg1, reg2, elseLabel));
            case "<=" ->
                //System.out.println(BGT(reg1, reg2, elseLabel));
                addToFileString(BGT(reg1, reg2, elseLabel));
            default -> System.out.println("ERROR: Invalid if argument");
        }
    }
    public void IF(String checkType, int reg1, int reg2)
    {
        elseStack.push("ELSE" + elseExitCounter);
        exitStack.push("EXIT" + elseExitCounter);
        elseExitCounter++;
        String elseLabel = elseStack.peek();

        switch (checkType) {
            case "=" ->
                // System.out.println(BNE(reg1, reg2, elseLabel));
                addToFileString(BNE(reg1, reg2, elseLabel));
            case ">" ->
                // System.out.println(BLE(reg1, reg2, elseLabel));
                addToFileString(BLE(reg1, reg2, elseLabel));
            case ">=" ->
                // System.out.println(BLT(reg1, reg2, elseLabel));
                addToFileString(BLT(reg1, reg2, elseLabel));
            case "<" ->
                //System.out.println(BGE(reg1, reg2, elseLabel));
                addToFileString(BGE(reg1, reg2, elseLabel));
            case "<=" ->
                //System.out.println(BGT(reg1, reg2, elseLabel));
                addToFileString(BGT(reg1, reg2, elseLabel));
            default -> {
            }
            // System.out.println("ERROR: Invalid if argument");
        }
    }

    public void ELSE()
    {
        UNCONDITIONALJUMP(exitStack.peek());
        String elseLabel = elseStack.pop();
        if(ISDEBUGGING)
            System.out.println(elseLabel + ":");
        addLabelToFile(elseLabel + ":");
    }

    public void FI()
    {
        String fiLabel = exitStack.pop();
        if(ISDEBUGGING)
            System.out.println(fiLabel + ":");
        addLabelToFile(fiLabel + ":");
    }

    public void OD()
    {
        String odLabel = whileStack.pop();
        if(ISDEBUGGING)
            System.out.println(odLabel);
        addToFileString(odLabel);
    }

    private String BEQ(String reg1, String reg2, String label)
    {
        String buffer = memLookupStore(reg1);
        buffer += "\t\t" + memLookupStore(reg2);
        buffer += ("\t\tBEQ " + regLookupNoFile(reg1) + ", " + regLookupNoFile(reg2) + ", " + label);
        return buffer;
    }

    private String BEQ(int reg1, String reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg1 + "\n");
        buffer += "\t\t" +memLookupStore(reg2);
        buffer += ("\t\tBEQ t6, " + regLookupNoFile(reg2) + ", " + label);
        return buffer;
    }

    private String BEQ(String reg1, int reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg2 + "\n");
        buffer += "\t\t" + memLookupStore(reg1);
        buffer += ("\t\tBEQ " + regLookupNoFile(reg1) + ", t6, " + label);
        return buffer;
    }

    private String BEQ(int reg1, int reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg1 + "\n");
        buffer += ("\t\tADDI t5, x0, " + reg2 + "\n");
        buffer += ("\t\tBEQ t6, t5, " + label);
        return buffer;
    }


    private String BNE(String reg1, String reg2, String label)
    {
        String buffer = memLookupStore(reg1);
        buffer += "\t\t" + memLookupStore(reg2);
        buffer += ("\t\tBNE " + regLookupNoFile(reg1) + ", " + regLookupNoFile(reg2) + ", " + label);
        return buffer;
    }

    private String BNE(int reg1, String reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg1 + "\n");
        buffer += "\t\t" + memLookupStore(reg2);
        buffer += ("\t\tBNE t6, " + regLookupNoFile(reg2) + ", " + label);
        return buffer;
    }

    private String BNE(String reg1, int reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg2 + "\n");
        buffer += "\t\t" + memLookupStore(reg1);
        buffer += ("\t\tBNE " + regLookupNoFile(reg1) + ", t6, " + label);
        return buffer;
    }

    private String BNE(int reg1, int reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg1 + "\n");
        buffer += ("\t\tADDI t5, x0, " + reg2 + "\n");
        buffer += ("\t\tBNE t6, t5, " + label);
        return buffer;
    }

    private String BLT(String reg1, String reg2, String label)
    {
        String buffer = memLookupStore(reg1);
        buffer += "\t\t" +memLookupStore(reg2);
        buffer += ("\t\tBLT " + regLookupNoFile(reg1) + ", " + regLookupNoFile(reg2) + ", " + label);
        return buffer;
    }

    private String BLT(int reg1, String reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg1 + "\n");
        buffer += "\t\t" + memLookupStore(reg2);
        buffer += ("\t\tBLT t6, " + regLookupNoFile(reg2) + ", " + label);
        return buffer;
    }

    private String BLT(String reg1, int reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg2 + "\n");
        buffer += "\t" +memLookupStore(reg1);
        buffer += ("\tBLT " + regLookupNoFile(reg1) + ", t6, " + label);
        return buffer;
    }

    private String BLT(int reg1, int reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg1 + "\n");
        buffer += ("\t\tADDI t5, x0, " + reg2 + "\n");
        buffer += ("\t\tBLT t6, t5, " + label);
        return buffer;
    }

    private String BGE(String reg1, String reg2, String label)
    {
        String buffer = memLookupStore(reg1);
        buffer += "\t\t" + memLookupStore(reg2);
        buffer +=("\t\tBGE " + regLookupNoFile(reg1) + ", " + regLookupNoFile(reg2) + ", " + label);
        return buffer;
    }

    private String BGE(int reg1, String reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg1 + "\n");
        buffer += "\t\t" + memLookupStore(reg2);
        buffer += ("\t\tBGE t6, " + regLookupNoFile(reg2) + ", " + label);
        return buffer;
    }

    private String BGE(String reg1, int reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg2 + "\n");
        buffer += "\t\t" + memLookupStore(reg1);
        buffer += ("\t\tBGE " + regLookupNoFile(reg1) + ", t6, " + label);
        return buffer;
    }

    private String BGE(int reg1, int reg2, String label)
    {
        String buffer = ("ADDI t6, x0, " + reg1 + "\n");
        buffer += ("\t\tADDI t5, x0, " + reg2 + "\n");
        buffer += ("\t\tBGE t6, t5, " + label);
        return buffer;
    }

    private String BLE(String reg1, String reg2, String label)
    {
        String buffer = memLookupStore(reg1);
        buffer += "\t\t" + memLookupStore(reg2);
        //Add 1 to reg2 and store it in t0
        buffer += ("\t\tADDI " + "t6, " + regLookupNoFile(reg2) + " 1\n");
        //Use BLT comparison on reg1 and t0
        buffer += ("\t\tBLT " + regLookupNoFile(reg1) + ", t6, " + label);
        return buffer;
    }

    private String BLE(int reg1, String reg2, String label)
    {
        String buffer = memLookupStore(reg2);
        buffer += ("\t\tADDI " + "t5, " + regLookupNoFile(reg2) + " 1\n");
        buffer += ("\t\tADDI t6, x0, " + reg1 + "\n");
        buffer += ("\t\tBLT t6, t5, " + label);
        return buffer;
    }

    private String BLE(String reg1, int reg2, String label)
    {
        //Store value 1 in t0
        String buffer = ("ADDI t6, x0, 1\n");
        //Add that 1 to reg2 and store in t0
        buffer += ("\t\tADDI t6, t6, " + reg2 + "\n");
        buffer += "\t\t" + memLookupStore(reg1);
        buffer += ("\t\tBLT " + regLookupNoFile(reg1) + ", t6, " + label);
        return buffer;
    }

    private String BLE(int reg1, int reg2, String label)
    {
        //Store value 1 in t0
        String buffer = ("ADDI t6, x0, 1\n");
        //Add that 1 to reg2 and store in t0
        buffer += ("\t\tADDI t6, t6, " + reg2 + "\n");
        //Add reg1 into t1
        buffer += ("\t\tADDI t5, x0, " + reg1 + "\n");
        //Use BLT comparison on reg1 and t0
        buffer += ("\t\tBLT t5, t6, " + label);
        return buffer;
    }


    private String BGT(String reg1, String reg2, String label)
    {
        String buffer = memLookupStore(reg1);
        buffer += "\t\t" +memLookupStore(reg2);
        buffer += ("\t\tADDI t6, " + regLookupNoFile(reg2) + ", 1\n");
        buffer += ("\t\tBGE " + regLookupNoFile(reg1) + ", t6, " + label);
        return buffer;
    }

    private String BGT(int reg1, String reg2, String label)
    {
        //Add 1 to reg2 and store in t0
        String buffer = memLookupStore(reg2);
        buffer += ("\t\tADDI t6, " + regLookupNoFile(reg2) + ", 1\n");
        //Store value of reg1 in t1
        buffer += ("\t\tADDI t5, x0, " + reg1 + "\n");
        buffer += ("\t\tBGE t5, t6, " + label);
        return buffer;
    }

    private String BGT(String reg1, int reg2, String label)
    {
        //store value in reg2 in t0
        String buffer = ("ADDI t6, x0, " + reg2 + "\n");
        //add 1 to value in t0
        buffer += ("\t\tADDI t6, t6, 1\n");
        buffer += "\t\t" + memLookupStore(reg1);
        buffer += ("\t\tBGE " + regLookupNoFile(reg1) + ", t6, " + label);
        return buffer;
    }

    private String BGT(int reg1, int reg2, String label)
    {
        //store value in reg1 in t0
        String buffer = ("ADDI t0, x0, " + reg1 + "\n");
        //store value in reg2 in t1
        buffer += ("\t\tADDI t1, x0, " + reg2 + "\n");
        //add 1 to value in t1
        buffer += ("\t\tADDI t1, t1, 1\n");
        buffer += ("\t\tBGE t0, t1, " + label);
        return buffer;
    }

    private void UNCONDITIONALJUMP(String label)
    {
        if(ISDEBUGGING)
            System.out.println("BEQ x0, x0, " + label);
        addToFileString("BEQ x0, x0, " + label);
    }

    private void addInitialWhileCheck(String checkType, String reg1, String reg2)
    {
        String loopEndLabel = "OD" + whileExitCounter;
        switch (checkType) {
            case "=" ->
                //System.out.println(BNE(reg1, reg2, loopEndLabel));
                addToFileString(BNE(reg1, reg2, loopEndLabel));
            case ">" ->
                //System.out.println(BLE(reg1, reg2, loopEndLabel));
                addToFileString(BLE(reg1, reg2, loopEndLabel));
            case ">=" ->
                //System.out.println(BLT(reg1, reg2, loopEndLabel));
                addToFileString(BLT(reg1, reg2, loopEndLabel));
            case "<" ->
                //System.out.println(BGE(reg1, reg2, loopEndLabel));
                addToFileString(BGE(reg1, reg2, loopEndLabel));
            case "<=" ->
                //System.out.println(BGT(reg1, reg2, loopEndLabel));
                addToFileString(BGT(reg1, reg2, loopEndLabel));
            default -> System.out.println("ERROR: Invalid if argument");
        }
    }
    private void addInitialWhileCheck(String checkType, String reg1, int reg2)
    {
        String loopEndLabel = "OD" + whileExitCounter;
        switch (checkType) {
            case "=" ->
                //System.out.println(BNE(reg1, reg2, loopEndLabel));
                addToFileString(BNE(reg1, reg2, loopEndLabel));
            case ">" ->
                //System.out.println(BLE(reg1, reg2, loopEndLabel));
                addToFileString(BLE(reg1, reg2, loopEndLabel));
            case ">=" ->
                //System.out.println(BLT(reg1, reg2, loopEndLabel));
                addToFileString(BLT(reg1, reg2, loopEndLabel));
            case "<" ->
                //System.out.println(BGE(reg1, reg2, loopEndLabel));
                addToFileString(BGE(reg1, reg2, loopEndLabel));
            case "<=" ->
                //System.out.println(BGT(reg1, reg2, loopEndLabel));
                addToFileString(BGT(reg1, reg2, loopEndLabel));
            default -> System.out.println("ERROR: Invalid if argument");
        }
    }
    private void addInitialWhileCheck(String checkType, int reg1, String reg2)
    {
        String loopEndLabel = "OD" + whileExitCounter;
        switch (checkType) {
            case "=" ->
                //System.out.println(BNE(reg1, reg2, loopEndLabel));
                addToFileString(BNE(reg1, reg2, loopEndLabel));
            case ">" ->
                //System.out.println(BLE(reg1, reg2, loopEndLabel));
                addToFileString(BLE(reg1, reg2, loopEndLabel));
            case ">=" ->
                //System.out.println(BLT(reg1, reg2, loopEndLabel));
                addToFileString(BLT(reg1, reg2, loopEndLabel));
            case "<" ->
                // System.out.println(BGE(reg1, reg2, loopEndLabel));
                addToFileString(BGE(reg1, reg2, loopEndLabel));
            case "<=" ->
                //System.out.println(BGT(reg1, reg2, loopEndLabel));
                addToFileString(BGT(reg1, reg2, loopEndLabel));
            default -> {
            }
            //System.out.println("ERROR: Invalid if argument");
        }
    }
    private void addInitialWhileCheck(String checkType, int reg1, int reg2)
    {
        String loopEndLabel = "OD" + whileExitCounter;
        switch (checkType) {
            case "=" ->
                //System.out.println(BNE(reg1, reg2, loopEndLabel));
                addToFileString(BNE(reg1, reg2, loopEndLabel));
            case ">" ->
                //System.out.println(BLE(reg1, reg2, loopEndLabel));
                addToFileString(BLE(reg1, reg2, loopEndLabel));
            case ">=" ->
                //System.out.println(BLT(reg1, reg2, loopEndLabel));
                addToFileString(BLT(reg1, reg2, loopEndLabel));
            case "<" ->
                //System.out.println(BGE(reg1, reg2, loopEndLabel));
                addToFileString(BGE(reg1, reg2, loopEndLabel));
            case "<=" ->
                //System.out.println(BGT(reg1, reg2, loopEndLabel));
                addToFileString(BGT(reg1, reg2, loopEndLabel));
            default -> System.out.println("ERROR: Invalid if argument");
        }
    }
    public void NOT(String variableName)
    {
        if(ISDEBUGGING) {
            System.out.println("BNE " + regLookup(variableName) + ", x0, ELSENOT" + notCounter);
            System.out.println("ADDI t0, x0, 1");
        }
        addToFileString("BNE " + regLookup(variableName) + ", x0, ELSENOT" + notCounter);
        addToFileString("ADDI t0, x0, 1");
        UNCONDITIONALJUMP("EXITNOT" + notCounter);
        if(ISDEBUGGING) {
            System.out.println("ELSENOT" + notCounter + ":");
            System.out.println("ADD t0, x0, x0");
            System.out.println("EXITNOT" + notCounter + ":");
        }
        addToFileString("ELSENOT" + notCounter + ":");
        addToFileString("ADD t0, x0, x0");
        addToFileString("EXITNOT" + notCounter + ":");

        notCounter++;
    }

    public void SKIP()
    {
        addToFileString("ADDI x0, x0, 0");
    }

    public void MULV2(String destination, String operand1, String operand2)
    {
        //Jump to end if either is 0
        String returnValue = getFirstEmptyTempIndex(2);
        System.out.println("ADD " + returnValue + ", x0, x0");
        System.out.println("BEQ x0, " + regLookup(operand1) + ", MULEXIT" + mulCounter);
        System.out.println("BEQ x0, " + regLookup(operand2) + ", MULEXIT" + mulCounter);

        //Store both initial values in temp vars
        String val1Store = getFirstEmptyTempIndex(1);
        String val2Store = getFirstEmptyTempIndex(0);
        String absolute1Store = getFirstEmptyTempIndex(3);
        System.out.println("ADD " + val1Store + ", x0, " + regLookup(operand1));
        System.out.println("ADD " + val2Store + ", x0, " + regLookup(operand2));
        System.out.println("ADDI " + absolute1Store + ", x0, 1");

        //Main body. Add val1 to returnValue. decrement val2. check if val2 equals 0. loop if not
        System.out.println("MULBODY" + mulCounter + ":");
        System.out.println("ADD " + returnValue + ", " + returnValue + ", " + val1Store);
        System.out.println("SUB " + val2Store + ", " + val2Store + ", " + absolute1Store);
        System.out.println("BNE " + val2Store + ", x0, MULBODY" + mulCounter);

        //Set the destination value to the result value. Increment the mulcounter
        System.out.println("MULEXIT" + mulCounter + ":");
        System.out.println("ADD " + tempLookupOrAdd(destination) + ", x0, " + returnValue);
        mulCounter++;
    }

    public void MULV2(String destination, int operand1, String operand2)
    {
        //if operand2 is zeros, just assign zero to the destination
        if(operand1 == 0)
        {
            System.out.println("ADD " + destination + ", x0, x0");
        }
        else
        {
            //Jump to end if either is 0
            String returnValue = getFirstEmptyTempIndex(2);
            System.out.println("ADD " + returnValue + ", x0, x0");
            System.out.println("BEQ x0, " + regLookup(operand2) + ", MULEXIT" + mulCounter);

            //Store both initial values in temp vars
            String val1Store = getFirstEmptyTempIndex(1);
            String val2Store = getFirstEmptyTempIndex(0);
            String absolute1Store = getFirstEmptyTempIndex(3);
            System.out.println("ADDI " + val1Store + ", x0, " + operand1);
            System.out.println("ADD " + val2Store + ", x0, " + regLookup(operand2));
            System.out.println("ADDI " + absolute1Store + ", x0, 1");

            //Main body. Add val1 to returnValue. decrement val2. check if val2 equals 0. loop if not
            System.out.println("MULBODY" + mulCounter + ":");
            System.out.println("ADD " + returnValue + ", " + returnValue + ", " + val1Store);
            System.out.println("SUB " + val2Store + ", " + val2Store + ", " + absolute1Store);
            System.out.println("BNE " + val2Store + ", x0, MULBODY" + mulCounter);

            //Set the destination value to the result value. Increment the mulcounter
            System.out.println("MULEXIT" + mulCounter + ":");
            System.out.println("ADD " + tempLookupOrAdd(destination) + ", x0, " + returnValue);
            mulCounter++;
        }
    }

    public void MULV2(String destination, String operand1, int operand2)
    {
        //if operand2 is zeros, just assign zero to the destination
        if(operand2 == 0)
        {
            System.out.println("ADD " + destination + ", x0, x0");
        }
        else
        {
            //Jump to end if either is 0
            String returnValue = getFirstEmptyTempIndex(2);
            System.out.println("ADD " + returnValue + ", x0, x0");
            System.out.println("BEQ x0, " + regLookup(operand1) + ", MULEXIT" + mulCounter);

            //Store both initial values in temp vars
            String val1Store = getFirstEmptyTempIndex(1);
            String val2Store = getFirstEmptyTempIndex(0);
            String absolute1Store = getFirstEmptyTempIndex(3);
            System.out.println("ADD " + val1Store + ", x0, " + regLookup(operand1));
            System.out.println("ADDI " + val2Store + ", x0, " + operand2);
            System.out.println("ADDI " + absolute1Store + ", x0, 1");

            //Main body. Add val1 to returnValue. decrement val2. check if val2 equals 0. loop if not
            System.out.println("MULBODY" + mulCounter + ":");
            System.out.println("ADD " + returnValue + ", " + returnValue + ", " + val1Store);
            System.out.println("SUB " + val2Store + ", " + val2Store + ", " + absolute1Store);
            System.out.println("BNE " + val2Store + ", x0, MULBODY" + mulCounter);

            //Set the destination value to the result value. Increment the mulcounter
            System.out.println("MULEXIT" + mulCounter + ":");
            System.out.println("ADD " + tempLookupOrAdd(destination) + ", x0, " + returnValue);
            mulCounter++;
        }
    }

    public void MULV2(String destination, int operand1, int operand2) {
        //if operand2 is zeros, just assign zero to the destination
        if (operand2 == 0 || operand1 == 0) {
            System.out.println("ADD " + destination + ", x0, x0");
        } else {
            //Jump to end if either is 0
            String returnValue = getFirstEmptyTempIndex(2);
            System.out.println("ADD " + returnValue + ", x0, x0");

            //Store both initial values in temp vars
            String val1Store = getFirstEmptyTempIndex(1);
            String val2Store = getFirstEmptyTempIndex(0);
            String absolute1Store = getFirstEmptyTempIndex(3);
            System.out.println("ADDI " + val1Store + ", x0, " + operand1);
            System.out.println("ADDI " + val2Store + ", x0, " + operand2);
            System.out.println("ADDI " + absolute1Store + ", x0, 1");

            //Main body. Add val1 to returnValue. decrement val2. check if val2 equals 0. loop if not
            System.out.println("MULBODY" + mulCounter + ":");
            System.out.println("ADD " + returnValue + ", " + returnValue + ", " + val1Store);
            System.out.println("SUB " + val2Store + ", " + val2Store + ", " + absolute1Store);
            System.out.println("BNE " + val2Store + ", x0, MULBODY" + mulCounter);

            //Set the destination value to the result value. Increment the mulcounter
            System.out.println("ADD " + tempLookupOrAdd(destination) + ", x0, " + returnValue);
            mulCounter++;
        }
    }
}
