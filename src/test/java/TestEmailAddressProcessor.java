import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;
import java.util.TreeSet;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class TestEmailAddressProcessor {
    private EmailAddressProcessor emailAddressProcessor;

    private static Object[] stringWithRawEmailAddresses() {
        return new Object[]{
                new Object[]{"+7 (101) 111-222-11  abc@ert.com, def@sdf.org", "abc@ert.com, def@sdf.org"},
                new Object[]{"+1 (102) 123532-2 some@mail.ru", "some@mail.ru"},
                new Object[]{"+44 (301) 123 23 45 7zip@site.edu; ret@ghjj.org", "zip@site.edu; ret@ghjj.org"},
                new Object[]{"+1(234) 2323-33312;;;abc@domain.org",";;;abc@domain.org"},
                new Object[]{"+44 (301) 123 23 45 7", ""},
                new Object[]{"", ""},
                new Object[]{null, ""}
        };
    }
    private static Object[] rawEmailsAndExtractedEmails() {
        Set<String> expectedResult1 = new TreeSet<>();
        expectedResult1.add("def@sdf.org");

        Set<String> expectedResult2 = new TreeSet<>();

        Set<String> expectedResult3 = new TreeSet<>();
        expectedResult3.add("ret@ghjj.org");

        Set<String> expectedResult4 = new TreeSet<>();
        expectedResult4.add("ret@ghjj.org");
        expectedResult4.add("retv@ghjjv.org");
        expectedResult4.add("reta@ghjja.org");

        Set<String> expectedResult5 = new TreeSet<>();
        expectedResult5.add("ret@ghjj.org");

        Set<String> expectedResult6 = new TreeSet<>();
        expectedResult6.add("dan@at.org");

        return new Object[]{
                new Object[]{"abc@ert.com, def@sdf.org", expectedResult1},
                new Object[]{"s@m.ru", expectedResult2},
                new Object[]{";;;;zip@site.edu; ret@ghjj.org", expectedResult3},
                new Object[]{"  ;;,  zip@site.edu  ret@ghjj.org", expectedResult3},
                new Object[]{"zip@site.edu  ret@ghjj.org;retv@ghjjv.org,reta@ghjja.org", expectedResult4},
                new Object[]{"zip@site.edu  ret@ghjj.org;zip@site.edu  ret@ghjj.org", expectedResult5},
                new Object[]{";;;;;\t\t\t;;;;\t\t\t\tdan@at.org smith@hp.com", expectedResult6},
                new Object[]{"", new TreeSet<>()}
        };
    }

    @Test
    @Parameters(method = "stringWithRawEmailAddresses")
    public void testGetStringWithEmails(String rawString, String expectedResult){
        emailAddressProcessor = new EmailAddressProcessor(rawString);
        assertEquals(expectedResult, emailAddressProcessor.extractStringWithEmails());
    }

    @Test
    @Parameters(method = "rawEmailsAndExtractedEmails")
    public void testGetStringWithEmails(String rawString, Set<String> expectedResult){
        emailAddressProcessor = new EmailAddressProcessor(rawString);
        assertEquals(expectedResult, emailAddressProcessor.extractEmailsList());
    }
}
