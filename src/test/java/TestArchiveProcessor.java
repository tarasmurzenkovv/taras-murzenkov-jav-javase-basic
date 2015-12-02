import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import net.lingala.zip4j.exception.ZipException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.TestCase.assertEquals;


/**
 * It is not a UNIT test. It is an integration test because you need to provide a path to archive to run this test.
 */
@RunWith(JUnitParamsRunner.class)
public class TestArchiveProcessor {

    private final static String LARGE_EXTRACTED_FILE = "C:\\AppStore\\dat";
    // the archive that was provided with the task description
    private final static String PATH_TO_ZIP_FILE = "C:\\AppStore\\inputs.zip";
    private static final String PATH_TO_TXT_FILE = "C:\\AppStore\\file.txt";

    // for performance measurement
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

    private static Object[] expectedSetOfEmailsAndNumbers() {
        Set<String> expectedEmails = new HashSet<>();
        Set<String> expectedNumbers = new HashSet<>();

        expectedEmails.add("abc@kjkj.org");
        expectedEmails.add("wer@t.org");
        expectedEmails.add("abc@domain.org");
        expectedEmails.add("@111.org");
        expectedEmails.add("dan@at.org");
        expectedEmails.add("uuu@ttt.org");
        expectedEmails.add("ann@domain.org");
        expectedEmails.add("123@23423.org");
        expectedEmails.add("ann1@domain.org");
        expectedEmails.add("ret@ghjj.org");
        expectedEmails.add("def@sdf.org");

        expectedNumbers.add("");
        expectedNumbers.add("+1(234)232333312");
        expectedNumbers.add("+1(102)1235322");
        expectedNumbers.add("+7(401)11122211");
        expectedNumbers.add("+1(4542)114214111");
        expectedNumbers.add("+1(2342)114234");
        expectedNumbers.add("+1(321)");
        expectedNumbers.add("+44(321)12323457");
        expectedNumbers.add("+1(321)23452345234512312341234");
        expectedNumbers.add("+8(802)23452345");
        expectedNumbers.add("+22(401)234234234");
        expectedNumbers.add("+1(401)34523452345");
        expectedNumbers.add("+4(802)234523");

        return new Object[]{
                new Object[]{expectedEmails, expectedNumbers}
        };
    }

    @Before
    public void setUp() {
        BasicEtmConfigurator.configure();
        etmMonitor = EtmManager.getEtmMonitor();
        etmMonitor.start();
    }

    // stop measuring a performance
    @After
    public void tearDown() {
        etmMonitor.stop();
    }

    @Test
    @Parameters(method = "pathToFileAndExpectedSetOfNumbers")
    public void testExtractFineGrainedTelephoneNumber(Set<String> expectedOutput) throws IOException {
        Path path = Paths.get(LARGE_EXTRACTED_FILE);
        assertEquals(expectedOutput, ArchiveProcessor.getNumbers(path));
    }

    @Test
    @Parameters(method = "pathToFileAndExpectedSetOfEmails")
    public void testExtractEmails(Set<String> expectedOutput) throws IOException {
        assertEquals(expectedOutput, ArchiveProcessor.getEmails(Paths.get(LARGE_EXTRACTED_FILE)));
        etmMonitor.render(new SimpleTextRenderer());
    }


    @Test
    @Parameters(method = "expectedSetOfEmailsAndNumbers")
    public void testProcessArchive(Set<String> expectedEmails, Set<String> expectedNumbers) throws IOException {
        File file = new File(PATH_TO_ZIP_FILE);
        Set<String> emails = new HashSet<>();
        Set<String> numbers = new HashSet<>();
        ArchiveProcessor.processArchive(file, emails, numbers);
        assertEquals(expectedEmails, emails);
        assertEquals(expectedNumbers, numbers);
    }

    @Test
    public void testAppendFileToArchive() throws IOException, ZipException {
        File zipArchive = new File("C:\\AppStore\\app_desc.zip");
        File txtFile = new File("C:\\AppStore\\test2.txt");

        ArchiveProcessor.appendFileToArchive(zipArchive, txtFile);
    }
}
