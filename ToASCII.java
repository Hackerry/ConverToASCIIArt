import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

/**
*	Name: Hackerry
*	Description: New method of generating ASCII art pictures. Support both black and white pictures and color pictures.
*				The numbers hard coded are obtained from drawing the characters in a picture(11px*20px) and computing the average RGB values.
*/
public class ToASCII {
    public static final String[] asciiChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,./<>?;\':\"[]{}\\|-=_+`~!@#$%^&*()"
            .split("");
    public static final int[] averageMethod = new int[] { 61, 72, 56, 54, 76, 55, 56, 69, 72, 40, 51, 54, 44, 55, 38,
            54, 44, 50, 48, 59, 87, 54, 74, 72, 65, 53, 76, 72, 59, 51, 60, 42, 68, 80, 63, 68, 65, 88, 73, 61, 49, 66,
            53, 72, 58, 54, 47, 59, 85, 51, 52, 56, 64, 63, 71, 44, 89, 80, 70, 19, 8, 31, 27, 26, 42, 28, 11, 64, 17,
            21, 56, 56, 54, 55, 31, 46, 14, 37, 53, 25, 42, 9, 29, 29, 85, 80, 79, 84, 24, 61, 85, 27, 44, 43 };
    public static final int fontSize = 20, fontWidth = 11, fontHeight = 20;
    public static final Font font = new Font("Consolas", Font.PLAIN, fontSize);

    public static void convertToASCIIAverage(File file, boolean blackAndWhite, int pixel, String outputName) throws Exception {
        BufferedImage image = null;
        if (!blackAndWhite)
            image = convertToBlackAndWhite(file, false);
        else
            image = ImageIO.read(file);

        int sizeH = pixel, sizeW = sizeH / 2, width = image.getWidth(), height = image.getHeight();
        double quotient = sizeH * sizeW;

        BufferedImage output = new BufferedImage(image.getWidth()/sizeW*fontWidth, image.getHeight()/sizeH*fontHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = output.createGraphics();
        g.setColor(Color.WHITE);
        StringBuilder sb = new StringBuilder();
        int temp, count = 0;
        Color color;
        for (int i = 0; i < height / sizeH; i++) {
            for (int j = 0; j < width / sizeW; j++) {
                temp = 0;
                for (int k = 0; k < sizeH * sizeW; k++) {
                    color = new Color(image.getRGB(j * sizeW + k % sizeW, i * sizeH + k / sizeW));
                    temp += (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                }
                temp /= (quotient * 255 / 89);
                sb.append(findRandomNormalize(temp));
            }
            g.setFont(font);
            g.drawString(sb.toString(), 0, (count++) * 20 + 17);
            sb = new StringBuilder();
            // System.out.println();
        }
        g.dispose();
        ImageIO.write(output, "jpg", new File("C:\\Users\\Administrator\\Desktop\\" + outputName + ".jpg"));
    }

    private static BufferedImage convertToBlackAndWhite(File file, boolean output) throws Exception {
        // Gray = (R*299 + G*587 + B*114 + 500) / 1000
        // Gray = (R*30 + G*59 + B*11 + 50) / 100
        if (!file.exists() || (!file.getName().endsWith("jpg") && !file.getName().endsWith("png")))
            return null;

        BufferedImage original = ImageIO.read(file);
        int width = original.getWidth(), height = original.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int temp;
        Color c;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                c = new Color(original.getRGB(i, j));
                temp = (c.getRed() * 30 + c.getGreen() * 59 + c.getBlue() * 11 + 50) / 100;
                image.setRGB(i, j, new Color(temp, temp, temp).getRGB());
            }
        }

        if (output) {
            String path = "C:\\Users\\Administrator\\Desktop\\"
                    + file.getName().subSequence(0, file.getName().lastIndexOf(".")) + ".jpg";
            System.out.println(path);
            ImageIO.write(image, "jpg", new File(path));
        }

        return image;
    }

    private static void generateAllASCIIChars() throws IOException {
        // size: 20
        // width: 11 height:20
        // ascent: 17 descent: 4
        // FontMetrics fm = g.getFontMetrics();
        // System.out.println(fm.getAscent() + " " + fm.getDescent() + " " +
        // Arrays.toString(fm.getWidths()));
        // System.out.println(Arrays.toString(temp));

        int count = 0;
        for (String st : asciiChars) {
            BufferedImage image = new BufferedImage(11, 20, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            g.setFont(font);
            g.clearRect(0, 0, 11, 20);
            g.setColor(Color.WHITE);
            g.drawString(st, 0, 16);
            g.dispose();
            ImageIO.write(image, "jpg", new File("C:\\Users\\Administrator\\Desktop\\test\\a" + (count++) + ".jpg"));
        }
    }

    private static char findRandomNormalize(int grayScale) {
        if (grayScale <= 10)
            return ' ';
        else if (grayScale <= 20)
            return random('.', '\"', '`', '\'', '-', ':', ',');
        else if (grayScale <= 30)
            return random('~', '!', '^', '_', '>', '*', '<', ';');
        else if (grayScale <= 40)
            return random('r', 'v', '=', '\\', '/');
        else if (grayScale <= 50)
            return random('+', '?', 'L', 'y',')', '(', '|', 'Y', 'z', 'T');
        else if (grayScale <= 55)
            return random('}', 's', 'J', '1', '2', 'i', 'F', 'V', 'C', 'w', 't', 'l', 'f', '{');
        else if (grayScale <= 60)
            return random('K', '[', ']', '3', 'o', 'k', 'X', 'I', 'A', 'Z');
        else if (grayScale <= 65)
            return random('E', 'P', 'a', 'S', 'j', '5', 'e', '4', 'h');
        else if (grayScale <= 70)
            return random('9', 'U', 'M', 'O', 'p');
        else if (grayScale <= 75)
            return random('D', 'R', 'W', 'H', 'd', 'q', 'b', '6');
        else if (grayScale <= 80)
            return random('#', '8', 'N', 'G', 'm', '$');
        else if (grayScale <= 85)
            return random('@', '&', '0', '%');
        else
            return random('g', 'B', 'Q');
    }
    
    private static char findRandomExact(int grayScale) {
        if (grayScale <= 4)
            return ' ';
        else if (grayScale <= 8)
            return '.';
        else if (grayScale <= 9)
            return '`';
        else if (grayScale <= 11)
            return '\'';
        else if (grayScale <= 14)
            return '-';
        else if (grayScale <= 17)
            return ':';
        else if (grayScale <= 19)
            return ',';
        else if (grayScale <= 21)
            return '\"';
        else if (grayScale <= 24)
            return '^';
        else if (grayScale <= 25)
            return '_';
        else if (grayScale <= 26)
            return '>';
        else if (grayScale <= 27)
            return Math.random() < 0.5 ? '*' : '<';
        else if (grayScale <= 28)
            return ';';
        else if (grayScale <= 29)
            return random('~', '!');
        else if (grayScale <= 31)
            return random('\\', '/');
        else if (grayScale <= 37)
            return '=';
        else if (grayScale <= 38)
            return 'v';
        else if (grayScale <= 40)
            return 'r';
        else if (grayScale <= 42)
            return random('+', '?', 'L');
        else if (grayScale <= 43)
            return ')';
        else if (grayScale <= 44)
            return '(';
        else if (grayScale <= 46)
            return '|';
        else if (grayScale <= 47)
            return 'Y';
        else if (grayScale <= 48)
            return 'z';
        else if (grayScale <= 49)
            return 'T';
        else if (grayScale <= 50)
            return 'y';
        else if (grayScale <= 51)
            return random('s', 'J', '1');
        else if (grayScale <= 52)
            return '2';
        else if (grayScale <= 53)
            return random('i', 'F', 'V');
        else if (grayScale <= 54)
            return random('C', 'w', 't', 'l', 'f', '{');
        else if (grayScale <= 55)
            return '}';
        else if (grayScale <= 56)
            return random('[', ']', '3', 'o', 'k');
        else if (grayScale <= 58)
            return 'X';
        else if (grayScale <= 59)
            return random('I', 'A', 'Z');
        else if (grayScale <= 60)
            return 'K';
        else if (grayScale <= 61)
            return random('a', 'S', 'j');
        else if (grayScale <= 63)
            return random('5', 'e');
        else if (grayScale <= 64)
            return random('4', 'h');
        else if (grayScale <= 65)
            return random('E', 'P');
        else if (grayScale <= 66)
            return 'U';
        else if (grayScale <= 68)
            return random('M', 'O');
        else if (grayScale <= 69)
            return 'p';
        else if (grayScale <= 70)
            return '9';
        else if (grayScale <= 71)
            return '6';
        else if (grayScale <= 72)
            return random('W', 'H', 'd', 'q', 'b');
        else if (grayScale <= 73)
            return 'R';
        else if (grayScale <= 74)
            return 'D';
        else if (grayScale <= 76)
            return random('G', 'm');
        else if (grayScale <= 79)
            return '$';
        else if (grayScale <= 80)
            return random('#', '8', 'N');
        else if (grayScale <= 84)
            return '%';
        else if (grayScale <= 85)
            return random('@', '&', '0');
        else if (grayScale <= 87)
            return 'B';
        else if (grayScale <= 88)
            return 'Q';
        else
            return 'g';
    }

    private static char random(char... poss) {
        return poss[(int) (Math.random() * poss.length)];
    }

    public static void main(String[] args) throws Exception {
        // convertToGrayScale(new File("src/pvz.jpg"), true);
        try {
			convertToASCIIAverage(new File(args[0]), args[1].equalsIgnoreCase("true"), Integer.parseInt(args[2]), args[3]);
		} catch(Exception e) {
			System.out.println("java ToASCII [filename] [flag for black and white] [pixel for one character] [output file name]");
		}
    }
}
