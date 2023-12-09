package newWorker;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilesManager {

    public static void createAndSaveFile(String filePath, String content) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            out.println(content);
        } catch (IOException e) {
            System.out.println("Error writing to file " + filePath);
            e.printStackTrace();
        }

        Path absolutePath = Paths.get(filePath).toAbsolutePath();
        System.out.println("The absolute path of the file is: " + absolutePath);
    }

    public static List<String> getFilesInDirectory(String directoryPath) {
        createAndSaveFile(directoryPath + "/teste.txt", "conteudo de testes");


        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path path : directoryStream) {
                if (!Files.isDirectory(path)) {
                    System.out.println("Nome do ficheiro: " + path);
                    fileNames.add(path.toString());
                }
            }
        } catch (IOException ex) {
            System.err.println("An I/O error occurred while opening the directory: " + ex.getMessage());
        }
        return fileNames;
    }

    public static void filterFiles(List<String> inputFiles, String outputFile, String category) {
        try {
            Path outputPath = Paths.get(outputFile).getParent();
            if (outputPath != null && !Files.exists(outputPath)) {
                System.out.println("Vou criar a diretoria destino. ");
                Files.createDirectories(outputPath);
            }
        } catch (IOException e) {
            System.out.println("Error creating output directory");
            e.printStackTrace();
            return;
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)))) {
            for (String inputFile : inputFiles) {
                try (BufferedReader in = new BufferedReader(new FileReader(inputFile))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.contains(category)) {
                            out.println(line);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file " + inputFile);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to output file " + outputFile);
            e.printStackTrace();
        }
    }

    public static boolean addProductToFile(String date, String product, String category, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true); // Set true for append mode
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(date + " | Categoria: " + category + ", Produto: " + product); // New line
            printWriter.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving product in file " + filePath);
            return false;
        }
    }
}
