import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;

/**
 * This class can be used to convert pictures into ASCII art.
 * @author Hackerry
 * 
 */
public class ConvertToASCIIArt {
	private static String outputPath = "output.txt";
    private static Color[] colors;

    public static void main(String[] args) throws IOException {
        String fileName = "Geisel Library.jpg";
        int node = 3;
        boolean toFile = false;
        if (args.length >= 1) {
            fileName = args[0];
        }
        if (args.length >= 2) {
            node = Integer.parseInt(args[1]);
        }
        if(args.length >= 3) {
			outputPath = args[2];
			toFile = true;
        }
        BufferedImage image = ImageIO.read(new File(fileName));
        int width = image.getWidth() / node;
        int height = image.getHeight() / node;
        Color c;
        colors = new Color[width * height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                c = new Color(image.getRGB(j * node, i * node));
                colors[i * width + j] = c;
            }
        }

        convertToASCIIArt(image, width, toFile);
    }

    public static void convertToASCIIArt(BufferedImage image, int width, boolean toFile) throws FileNotFoundException, IOException {
        Color c;
        int all;
        PrintWriter pw = null;
        StringBuilder sb = new StringBuilder();
        if (toFile) {
            File f = new File(outputPath);
            if (!f.exists()) {
                f.createNewFile();
            }
            pw = new PrintWriter(f);
        }
        for (int i = 0; i < colors.length; i++) {
            c = colors[i];
            all = c.getRed() + c.getGreen() + c.getBlue();

            if (all >= 240 * 3) {
                sb.append("*");
            } else if (all >= 220 * 3) {
                sb.append("#");
            } else if (all >= 200 * 3) {
                sb.append("=");
            } else if (all >= 180 * 3) {
                sb.append("+");
            } else if (all >= 160 * 3) {
                sb.append("_");
            } else if (all >= 140 * 3) {
                sb.append(",");
            } else if (all >= 120 * 3) {
                sb.append("`");
            } else if (all <= 20 * 3) {
                sb.append(".");
            } else if (all <= 10 * 3) {
                sb.append(" ");
            } else {
                sb.append(" ");
            }

            if ((i + 1) % width == 0) {
                if (toFile) {
                    pw.println(sb.toString());
                } else {
                    System.out.println(sb.toString());
                }
                sb.delete(0, sb.length());
            }
        }
        if (pw != null) {
            pw.close();
        }
    }

    public static void printColors(Color[] colors, int width) {
        Color c;
        for (int i = 0; i < colors.length; i++) {
            c = colors[i];
            int r = c.getRed();
            int g = c.getGreen();
            int b = c.getBlue();
            int a = c.getAlpha();
            StringBuilder sb = new StringBuilder("[");
            if (r < 10) {
                sb.append("00");
                sb.append(r);
            } else if (r < 100) {
                sb.append("0");
                sb.append(r);
            } else {
                sb.append(r);
            }
            sb.append(",");
            if (g < 10) {
                sb.append("00");
                sb.append(g);
            } else if (g < 100) {
                sb.append("0");
                sb.append(g);
            } else {
                sb.append(g);
            }
            sb.append(",");
            if (b < 10) {
                sb.append("00");
                sb.append(b);
            } else if (b < 100) {
                sb.append("0");
                sb.append(b);
            } else {
                sb.append(b);
            }
            sb.append(",");
            if (a < 10) {
                sb.append("00");
                sb.append(a);
            } else if (a < 100) {
                sb.append("0");
                sb.append(a);
            } else {
                sb.append(a);
            }
            sb.append("]");
            System.out.print(sb.toString());

            if ((i + 1) % width == 0) {
                System.out.println();
            }
        }
    }
}
