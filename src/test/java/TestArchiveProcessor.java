import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class TestArchiveProcessor {
    // private final static String pathToFile = "C:\\AppStore\\dat";
    private final static String pathToFile = "/Users/terance1/Desktop/dat.zip";

    //private final static String pathToZipFile = "C:\\AppStore\\inputs.zip";

    private final static String pathToZipFile = "/Users/terance1/Desktop/dat.zip";
    private static EtmMonitor etmMonitor;

    private static Object[] pathToFileAndExpectedSetOfNumbers() {
        Set<String> expectedSetOfTelephoneNumbers = new TreeSet<>();
        expectedSetOfTelephoneNumbers.add("");
        expectedSetOfTelephoneNumbers.add("+1(401)34523452345");
        expectedSetOfTelephoneNumbers.add("+1(321)");
        expectedSetOfTelephoneNumbers.add("+4(802)234523");

        return new Object[]{
                new Object[]{expectedSetOfTelephoneNumbers},
        };
    }

    private static Object[] pathToFileAndExpectedSetOfEmails() {
        Set<String> expectedSetOfTelephoneNumbers = new TreeSet<>();
        expectedSetOfTelephoneNumbers.add("dan@at.org");
        expectedSetOfTelephoneNumbers.add("wer@t.org");
        return new Object[]{
                new Object[]{expectedSetOfTelephoneNumbers},
        };
    }

    @Before
    public void setUp() {
        BasicEtmConfigurator.configure();
        etmMonitor = EtmManager.getEtmMonitor();
        etmMonitor.start();
    }

    @After
    public void tearDown() {
        etmMonitor.stop();
    }


    @Test
    @Parameters(method = "pathToFileAndExpectedSetOfNumbers")
    public void testExtractFineGrainedTelephoneNumber(Set<String> expectedOutput) throws IOException {
        Path path = Paths.get(pathToFile);
        assertEquals(expectedOutput, ArchiveProcessor.getNumbers(path));
    }

    @Test
    @Parameters(method = "pathToFileAndExpectedSetOfEmails")
    public void testExtractEmails(Set<String> expectedOutput) throws IOException {
        assertEquals(expectedOutput, ArchiveProcessor.getEmails(Paths.get(pathToFile)));
        etmMonitor.render(new SimpleTextRenderer());
    }

    @Test
    public void testProcessArchive() throws IOException {
        File file = new File(pathToFile);
        ArchiveProcessor.getListOfFilesFromArchive(file);
    }

}
