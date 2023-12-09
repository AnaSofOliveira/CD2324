package markapp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class MarkApp {

    public static void main(String[] args) {
        // args[0] - image pathname; args[1] - image result pathname
        // args[2]...args[n] keywords to mark image
        String inputPath = args[0];
        String outputPath = args[1];
        System.out.println("MARK APP");
        System.out.println(inputPath);
        System.out.println(outputPath);

        ArrayList<String> keywords = new ArrayList<>();
        for (int i = 2; i < args.length; i++) keywords.add(args[i]);

        System.out.println("Keywords: " + keywords);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        BufferedImage img = null;

        try {
            img = ImageIO.read(Path.of(inputPath).toFile());
            System.out.println("Imagem lida");
            if(img != null) {
                annotateImage(img, keywords);
                System.out.println("Imagem anotada");
                try {
                    ImageIO.write(img, "jpg", Path.of(outputPath).toFile());
                    System.out.println("Imagem anotada guardada.");
                } catch (Exception e) {
                    System.out.println("Erro ao guardar imagem anotada: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler a imagem: " + e.getMessage());
        }
    }

    private static void annotateImage(BufferedImage img, ArrayList<String> keywords) {
        Graphics2D gfx = img.createGraphics();
        gfx.setFont(new Font("Arial", Font.PLAIN, 18));
        gfx.setColor(new Color(0x00ff00));
        String sentence = "";
        for (String s : keywords) sentence += s + " ";
        gfx.drawString(sentence, 10, 20);
        Polygon poly = new Polygon();
        poly.addPoint(3, 3);
        poly.addPoint(10 * sentence.length(), 3);
        poly.addPoint(10 * sentence.length(), 25);
        poly.addPoint(3, 25);
        poly.addPoint(3, 3);
        gfx.setColor(new Color(0xff0000));
        gfx.draw(poly);
        System.out.println("Imagem anotada no markapp");
    }
}
