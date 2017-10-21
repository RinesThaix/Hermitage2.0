package mem.kitek.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Darknet implements IPeopleCalculus {
    private final static String cmd = "darknet/darknet detect " +
            "darknet/cfg/yolo.cfg " +
            "darknet/yolo.weights -thresh 0.1";
    private final static String RESULT_PATH = "predictions.png";
    private int threshold = 0; // now match everything that looks like a person

    private volatile boolean isLoaded;
    private Scanner in;
    private Scanner err;
    private PrintWriter out;
    private Thread thread;
    private CountDownLatch latch = new CountDownLatch(1);

    public Darknet() throws IOException  {
        thread = new Thread(() -> {
            Process process;
            try {
                process = Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                System.err.println("Unable to create process: " + e.getMessage());
                return;
            }
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
            OutputStream outputStream = process.getOutputStream();
            in = new Scanner(inputStream);
            err = new Scanner(errorStream);
            out = new PrintWriter(outputStream);
            load();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for darknet to terminate");
            }
        });
        thread.start();
    }

    private void load() {
        new Thread(() -> {
            while (err.hasNext()) {
                String token = err.next();
//                System.out.println(token);
                if (token.contains("Done!")) {
                    System.out.println("Darknet initialized");
                    isLoaded = true;

                    // this hell is needed to read the first "enter image path" to make it react to commands
                    StringBuilder builder = new StringBuilder();
                    while (in.hasNext()) {
                        String outToken = in.next();
                        builder.append(outToken).append(" ");
                        if (builder.toString().endsWith("Enter Image Path: ")) {
                            break;
                        }
                    }

                    latch.countDown();
                    return;
                }
            }
        }).start();
    }

    private int countPersons(String result) {
        Matcher matcher = Pattern.compile("person: \\d+?%").matcher(result);
        int peopleCnt = 0;
        while (matcher.find()) {
            Matcher percentMatcher = Pattern.compile("\\d+").matcher(matcher.group());
            if (percentMatcher.find()) {
                String percentString = percentMatcher.group();
                try {
                    int percent = Integer.parseInt(percentMatcher.group());
                    if (percent >= threshold) {
                        peopleCnt++;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Unable to parse percents from string " + percentString);
                }
            }
        }
        return peopleCnt;
    }

    @Override
    public ImageNeuralParsingResult parse(Path path) {
        try {
            latch.await();
            StringBuilder builder = new StringBuilder();
            out.println(path.toAbsolutePath());
            out.flush();
            while (in.hasNext()) {
                String outToken = in.next();
                builder.append(outToken).append(" ");
                if (builder.toString().endsWith("Enter Image Path: ")) {
                    break;
                }
            }
            int peopleCnt = countPersons(builder.toString());
            return new ImageNeuralParsingResult(peopleCnt, Paths.get(RESULT_PATH));
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting for the latch");
            return null;
        }
    }

    public boolean isLoadwed() {
        return isLoaded;
    }

    public void shutdown() {
        thread.stop();
    }
}

