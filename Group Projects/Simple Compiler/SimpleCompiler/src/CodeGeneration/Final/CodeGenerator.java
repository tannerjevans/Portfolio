package CodeGeneration.Final;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class CodeGenerator {
    private String         plainName;
    private String         cCodeName;
    private Boolean        debug = true;
    private CodeStringData codeStringData;

    public CodeGenerator(String functionName, String[] varNames) {
        plainName = functionName.replaceAll("[^a-zA-Z0-9$%!]", "").toLowerCase();
        cCodeName = plainName + "_main";
        functionName = plainName + "_function";
        String functionName1 = functionName;
        int numVars = varNames.length;
        MapSort mapSort = new MapSort(varNames.clone());
        int[] ordMap = mapSort.getOrdMap();
        codeStringData = new CodeStringData(functionName1, numVars, varNames, ordMap);
    }

    public String getPlainName() {
        return plainName;
    }

    public void printToFile(String directoryPath) {
        String fileName = directoryPath + cCodeName + ".c";
        File file = new File(fileName);
        try{
            boolean result = Files.deleteIfExists(file.toPath());
        }catch (IOException x){
            System.err.println(x);
        }

        byte[] data = codeStringData.toString().getBytes();
        Path p = Paths.get(fileName);

        try (OutputStream out = new BufferedOutputStream(
            Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    public CodeStringData getStringData() {
        return codeStringData;
    }


    // Small custom quickSort that transforms an iota while alphabetizing variable names, producing
    //      an ordinal map between the order-of-appearance and alphabetical orderings. Used for printing
    //      and accepting arguments alphabetically in the C program arguments, but placing them in order-of-
    //      appearance in the variable array provided to compiled code.
    private class MapSort {
        private String[] variableNames;
        private int[] ordMap;

        public MapSort(String[] variableNames) {
            this.variableNames = variableNames;
            int numberVariables = variableNames.length;
            ordMap = IntStream.range(0, variableNames.length).toArray();
            mapSort(0, numberVariables - 1);
        }

        public int[] getOrdMap() {
            return ordMap;
        }

        private void swap(int i, int j) {
            String tempString = variableNames[i];
            int tempInt = ordMap[i];
            variableNames[i] = variableNames[j];
            ordMap[i] = ordMap[j];
            variableNames[j] = tempString;
            ordMap[j] = tempInt;
        }

        private int partition(int low, int high) {
            String pivot = variableNames[high];
            int i = low - 1;
            for (int j = low; j <= high - 1; j++) if (variableNames[j].compareTo(pivot) < 0) swap(++i, j);
            swap(++i, high);
            return i;
        }

        private void mapSort(int low, int high) {
            if (low < high) {
                int part = partition(low, high);
                mapSort(low, part - 1);
                mapSort(part + 1, high);
            }
        }
    }

}
