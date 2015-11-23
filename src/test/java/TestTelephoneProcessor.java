import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class TestTelephoneProcessor {
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
                new Object[]{"+1 (102) 123532-2;;;;;;;;;;;;;;;;;;;;;;", "+1(102)1235322"}

        };
    }

    private static Object[] processedRawTelephoneNumbers() {
        return new Object[]{
                new Object[]{"+7(101)11122211","+7(401)11122211"},
                new Object[]{"+1(202)1235322","+1(802)1235322"},
                new Object[]{"+44(301)12323457","+44(321)12323457"},
                new Object[]{"+44(801)12323457","+44(801)12323457"},
        };
    }

    @Test
    @Parameters(method = "stringsWithRawTelephoneNumbersAndExpectedRawTelephoneNumbers")
    public void testExtractRawTelephoneNumberFromString(String stringWithRawTelephoneNumber, String validRawTelephoneNumber) {
        telephoneNumberProcessor = new TelephoneNumberProcessor(stringWithRawTelephoneNumber);
        assertEquals(
                validRawTelephoneNumber,
                telephoneNumberProcessor.extractTelephoneNumberFromAString(stringWithRawTelephoneNumber)
        );
    }

    @Test
    @Parameters(method = "boundaryConditions")
    public void testBoundaryConditionsForExtractedRawTelephoneNumberFromString(String stringWithRawTelephoneNumber, String validRawTelephoneNumber) {
        telephoneNumberProcessor = new TelephoneNumberProcessor(stringWithRawTelephoneNumber);
        assertEquals(
                validRawTelephoneNumber,
                telephoneNumberProcessor.extractTelephoneNumberFromAString(stringWithRawTelephoneNumber)
        );
    }

    @Test
    @Parameters(method = "boundaryConditions")
    public void testProcessCountryCodes(String processedRawTelephoneNumber, String validTelephoneNumber) {
        telephoneNumberProcessor = new TelephoneNumberProcessor(processedRawTelephoneNumber);
        assertEquals(
                validTelephoneNumber,
                telephoneNumberProcessor.processCityCode(processedRawTelephoneNumber)
        );
    }


}
