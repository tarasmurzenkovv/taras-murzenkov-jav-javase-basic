import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class TestTelephoneNumberProcessor {
    private TelephoneNumberProcessor telephoneNumberProcessor;

    private static Object[] stringsWithRawTelephoneNumbersAndExpectedRawTelephoneNumbers() {
        return new Object[]{
                new Object[]{"+7 (101) 111-222-11  abc@ert.com, def@sdf.org", "+7(101)11122211"},
                new Object[]{"+1 (102) 123532-2 some@mail.ru", "+1(102)1235322"},
                new Object[]{"+44 (301) 123 23 45 7zip@site.edu; ret@ghjj.org", "+44(301)12323457"}
        };
    }

    private static Object[] boundaryConditions() {
        return new Object[]{
                new Object[]{"", ""},
                new Object[]{"@", ""},
                new Object[]{"+1 (102) 123532-2", "+1(102)1235322"},
                new Object[]{"zip@site.edu; ret@ghjj.org", ""},
                new Object[]{"asdfasdfasdfasdfasf", ""},
                new Object[]{"@@", ""},
                new Object[]{"+1(203)", "+1(203)"},
                new Object[]{"+1 (102) 123532-2;;;;;;;;;;;;;;;;;;;;;;", "+1(102)1235322"}

        };
    }

    private static Object[] processedRawTelephoneNumbers() {
        return new Object[]{
                new Object[]{"+7(101)11122211", "+7(401)11122211"},
                new Object[]{"+1(202)1235322", "+1(802)1235322"},
                new Object[]{"+44(301)12323457", "+44(321)12323457"},
                new Object[]{"+44(801)12323457", "+44(801)12323457"},
                new Object[]{"", ""}
        };
    }

    private static Object[] integratedData() {
        return new Object[]{
                new Object[]{"+7 (101) 111-222-11  abc@ert.com, def@sdf.org", "+7(401)11122211"},
                new Object[]{"+1 (102) 123532-2 some@mail.ru", "+1(102)1235322"},
                new Object[]{"+44 (301) 123 23 45 7zip@site.edu; ret@ghjj.org", "+44(321)12323457"},
                new Object[]{"+1(234) 2323-33312;;;abc@domain.org", "+1(234)232333312"},
                new Object[]{"+1 (102) 123532-2;;;;;;;;;;;;;;;;;;;;;;", "+1(102)1235322"},
                new Object[]{"", ""}
        };
    }

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

    @Test
    @Parameters(method = "stringsWithRawTelephoneNumbersAndExpectedRawTelephoneNumbers")
    public void testExtractRawTelephoneNumberFromString(String stringWithRawTelephoneNumber, String validRawTelephoneNumber) {
        telephoneNumberProcessor = new TelephoneNumberProcessor(stringWithRawTelephoneNumber);
        assertEquals(
                validRawTelephoneNumber,
                telephoneNumberProcessor.extractTelephoneNumberFromAString(stringWithRawTelephoneNumber));
    }

    @Test
    @Parameters(method = "boundaryConditions")
    public void testBoundaryConditionsForExtractedRawTelephoneNumberFromString(String stringWithRawTelephoneNumber, String validRawTelephoneNumber) {
        telephoneNumberProcessor = new TelephoneNumberProcessor(stringWithRawTelephoneNumber);
        assertEquals(
                validRawTelephoneNumber,
                telephoneNumberProcessor.extractTelephoneNumberFromAString(stringWithRawTelephoneNumber));
    }

    @Test
    @Parameters(method = "processedRawTelephoneNumbers")
    public void testProcessCountryCodes(String processedRawTelephoneNumber, String validTelephoneNumber) {
        telephoneNumberProcessor = new TelephoneNumberProcessor(processedRawTelephoneNumber);
        assertEquals(
                validTelephoneNumber,
                telephoneNumberProcessor.processCityCode(processedRawTelephoneNumber));
    }

    @Test
    @Parameters(method = "integratedData")
    public void testExtractFinegrainedTelephoneNumber(String rawString, String expectedTelephoneNumber) {
        telephoneNumberProcessor = new TelephoneNumberProcessor(rawString);
        assertEquals(
                expectedTelephoneNumber,
                telephoneNumberProcessor.extractFineGrainedTelephoneNumber());
    }

    @Test
    @Parameters(method = "pathToFileAndExpectedSetOfNumbers")
    public void testExtractFineGrainedTelephoneNumber(String pathToFile, Set<String> expectedOutput) throws IOException {
        Path path = Paths.get(pathToFile);
        assertEquals(expectedOutput, TelephoneNumberProcessor.getAllNumbersFromFile(path));
    }
}
