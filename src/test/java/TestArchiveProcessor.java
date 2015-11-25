import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.TestCase.assertEquals;
@RunWith(JUnitParamsRunner.class)
public class TestArchiveProcessor {
    private static Object[] pathToFileAndExpectedSetOfNumbers() {
        Set<String> expectedSetOfTelephoneNumbers = new TreeSet<>();
        expectedSetOfTelephoneNumbers.add("");
        expectedSetOfTelephoneNumbers.add("+1(401)34523452345");
        expectedSetOfTelephoneNumbers.add("+1(321)");
        expectedSetOfTelephoneNumbers.add("+4(802)234523");

        return new Object[]{
                new Object[]{"C:\\AppStore\\dat", expectedSetOfTelephoneNumbers},
        };
    }

    private static Object[] pathToFileAndExpectedSetOfEmails() {
        Set<String> expectedSetOfTelephoneNumbers = new TreeSet<>();
        expectedSetOfTelephoneNumbers.add("");
        expectedSetOfTelephoneNumbers.add("+1(401)34523452345");
        expectedSetOfTelephoneNumbers.add("+1(321)");
        expectedSetOfTelephoneNumbers.add("+4(802)234523");

        return new Object[]{
                new Object[]{"C:\\AppStore\\dat", expectedSetOfTelephoneNumbers},
        };
    }

    @Test
    @Parameters(method = "pathToFileAndExpectedSetOfNumbers")
    public void testExtractFineGrainedTelephoneNumber(String pathToFile, Set<String> expectedOutput){
        Path path = Paths.get(pathToFile);
        assertEquals(expectedOutput, ArchiveProcessor.getAllNumbersFromFile(path));
    }

    @Test
    @Parameters(method = "pathToFileAndExpectedSetOfEmails")
    public void testExtractEmails(String pathToFile,Set<String> expectedOutput){
        Path path = Paths.get(pathToFile);
        Set<String> results = ArchiveProcessor.getEmails(path);
        for (String r:results){
            System.out.println(r);
        }
        //assertEquals(expectedOutput, ArchiveProcessor.getEmails(path));
    }
}
