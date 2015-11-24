import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;


@RunWith(JUnitParamsRunner.class)
public class TestEmailAddressProcessor {
    private EmailAddressProcessor emailAddressProcessor;

    private static Object[] stringWithRawEmailAddresses() {
        return new Object[]{
                new Object[]{"+7 (101) 111-222-11  abc@ert.com, def@sdf.org", "abc@ert.com, def@sdf.org"},
                new Object[]{"+1 (102) 123532-2 some@mail.ru", "some@mail.ru"},
                new Object[]{"+44 (301) 123 23 45 7zip@site.edu; ret@ghjj.org", "zip@site.edu; ret@ghjj.org"},
                new Object[]{"+44 (301) 123 23 45 7", ""}
        };
    }

    private static Object[] rawEmailsAndExtractedEmails() {
        List<String> expectedResult1 = new ArrayList<>();
        expectedResult1.add("abc@ert.com");
        expectedResult1.add("def@sdf.org");

        List<String> expectedResult2 = new ArrayList<>();
        expectedResult2.add("some@mail.ru");

        List<String> expectedResult3 = new ArrayList<>();
        expectedResult3.add("zip@site.edu");
        expectedResult3.add("ret@ghjj.org");

        return new Object[]{
                //new Object[]{"abc@ert.com, def@sdf.org", expectedResult1},
                new Object[]{"s@m.ru", expectedResult2}//,

                //new Object[]{"zip@site.edu; ret@ghjj.org", expectedResult3}
        };
    }

    @Test
    public void testIsSeparatorCharacter(){
        emailAddressProcessor = new EmailAddressProcessor(",");
        assertTrue(emailAddressProcessor.isSeparatorCharacter(" "));
    }

    @Test
    @Parameters(method = "stringWithRawEmailAddresses")
    public void testGetStringWithEmails(String rawString, String expectedResult){
        emailAddressProcessor = new EmailAddressProcessor(rawString);
        assertEquals(expectedResult, emailAddressProcessor.extractStringWithEmails());
    }

    @Test
    @Parameters(method = "rawEmailsAndExtractedEmails")
    public void testGetStringWithEmails(String rawString, List<String> expectedResult){
        emailAddressProcessor = new EmailAddressProcessor(rawString);
        assertEquals(expectedResult, emailAddressProcessor.extractEmailsList());
    }

}
