package mem.kitek.webcam;

import com.github.sarxos.webcam.Webcam;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by RINES on 21.10.17.
 */
public class WebCam {

    private static Webcam webcam;

    private static boolean intialized = false;
    private static BufferedImage image;
    private static JPanel panel;
    private static Container container;
    private static int persons;
    private static int id;
    private static int hallID;

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter camera id, please:");
        id = scan.nextInt();
        System.out.println("Enter hall id now:");
        hallID = scan.nextInt();
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        while(true) {
            updateCamera();
            sendFile();
//            showImage(ImageIO.read(new File("webcam.jpg")));
            try {
                showImage(ImageIO.read(new URL("http://78.155.192.82:8228/" + id + ".jpg")));
            }catch(Exception ex) {}
            requestCameraOnline();
            try {
                Thread.sleep(100L);
            }catch(InterruptedException ex) {}
        }
    }

    private static void updateCamera() throws IOException {
        webcam.open();
        ImageIO.write(webcam.getImage(), "JPG", new File("webcam.jpg"));
    }

    private static void sendFile() throws IOException {
        MultipartUtility utility = new MultipartUtility("http://78.155.192.82:8228/api/sendPicture?cam_id=" + id, "UTF-8");
        utility.addFilePart("image", new File("webcam.jpg"));
        utility.finish();
    }

    private static void requestCameraOnline() throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(executeGet("http://78.155.192.82:8228/api/getHallByID?hall_id=" + hallID));
        if(result.containsKey("error"))
            System.out.println("ERROR: " + result.get("error_message"));
        else
            persons = (int) (long) result.get("people");
    }

    private static String executeGet(String urlGet) {
        try {
            URL url = new URL(urlGet);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null)
                    sb.append(line);
                return sb.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static void showImage(BufferedImage image) {
        WebCam.image = image;
        if(intialized) {
            panel.repaint();
            return;
        }
        intialized = true;
        try {
            ImageIO.write(image, "jpg", new File("showing.jpg"));
            JFrame frame = new JFrame();
            frame.setTitle("WebCam");
            frame.setSize(image.getWidth(), image.getHeight());
            frame.setResizable(true);
            panel = new JPanel() {

                @Override
                public void paintComponent(Graphics graphics) {
                    super.paintComponent(graphics);
                    graphics.drawImage(WebCam.image, 0, 0, null);
                    Font font = graphics.getFont();
                    graphics.setFont(new Font(font.getName(), font.getStyle(), 100));
                    graphics.setColor(Color.WHITE);
                    graphics.drawString(String.valueOf(persons), 50, 100);
                }

            };
            panel.setSize(image.getWidth(), image.getHeight());
            container = new Container();
            container.setSize(panel.getWidth(), panel.getHeight());
            container.add(panel);
            frame.add(container);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
