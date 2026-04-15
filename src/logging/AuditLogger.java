package logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AuditLogger {
    private static final Logger logger = Logger.getLogger("AuditLogger");
    static {
        try {
            FileHandler handler = new FileHandler("audit.log",true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void log(String message){
        logger.info(message);
    }
}
