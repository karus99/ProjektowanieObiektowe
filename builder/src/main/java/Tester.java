import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tester
{
    private static final Logger LOG = LogManager.getRootLogger();

    public static void main(String[] args)
    {
        HTTPTemplateBuilder tb = new HTTPTemplateBuilder("test.json", "templates/test.html");
        LOG.debug(tb.build());
    }
}
