import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Parser {
    public static void main(String[] args) throws IOException {
        String fileName = "D:\\LocalWorkspace\\Adidas_Compass\\compass-backend-performance-nonfunctional\\SCV_and_PF_LOAD\\archive\\2016.12.07_distributed_by_useCases\\summary";
        List<String> lines = Files.readAllLines(Paths.get(fileName + ".csv"), Charset.forName("UTF-8"));
        for (int i = 1; i <= 3; i++) {
            FileWriter fileWriter = new FileWriter(fileName + "_" + i + ".csv");
            String newLine = System.getProperty("line.separator");
            fileWriter.write(lines.get(0) + newLine);
            for (String line : lines) {
                if (line.contains("[Use case " + i + "]"))
                    fileWriter.write(line + newLine);
            }
            fileWriter.close();
        }
    }
}
