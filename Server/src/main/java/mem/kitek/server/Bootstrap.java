package mem.kitek.server;

import jline.console.ConsoleReader;
import mem.kitek.server.logger.FormatLogger;
import mem.kitek.server.logger.LoggingOutputStream;
import mem.kitek.server.spring.ApiController;
import mem.kitek.server.spring.HeadController;
import mem.kitek.server.spring.WebController;
import org.fusesource.jansi.AnsiConsole;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;

/**
 * Created by RINES on 20.10.17.
 */
public class Bootstrap {

    private final static int port = 8228;

    private static FormatLogger logger;

    private static ApplicationContext context;

    public static void main(String[] args) {
        ConsoleReader reader = setupLogger();
        ApiManager.init();
        HallManager.init();
        setupSpring();
        runConsole(reader);
    }

    private static void setupSpring() {
        Object[] controllers = new Object[]{
                HeadController.class, ApiController.class, WebController.class
        };
        context = SpringApplication.run(controllers, new String[0]);
    }

    private static ConsoleReader setupLogger() {
        System.setProperty("library.jansi.version", "HermitageServer");
        AnsiConsole.systemInstall();
        ConsoleReader consoleReader;
        try {
            consoleReader = new ConsoleReader();
            consoleReader.setExpandEvents(false);
            logger = new FormatLogger(consoleReader);
            System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
            System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
        } catch (IOException ex) {
            throw new IllegalStateException("Could not load console worker!");
        }
        return consoleReader;
    }

    private static void runConsole(ConsoleReader reader) {
        while (true) {
            try {
                String line = reader.readLine("> ");
                if(line != null)
                    switch(line.toLowerCase()) {
                        case "stop":
                        case "end": {
                            SpringApplication.exit(context);
                            System.exit(0);
                            return;
                        }
                    }
            } catch (Exception ex) {
                logger.error(ex, "Can not handle command from server console. Is everything ok?");
            }
        }
    }

    public static int getPort() {
        return port;
    }

    public static FormatLogger getLogger() {
        return logger;
    }

}
