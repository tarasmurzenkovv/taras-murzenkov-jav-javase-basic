import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.TreeSet;

public class EmailAddressProcessor {
    private String rawString;
    private static EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    public EmailAddressProcessor(String rawString) {
        this.rawString = rawString;
    }

    /**
     * For each char at string with emails:
     *
     * - if it belongs to valid email characters set then add it to string builder;
     * - if it belongs to separator set of character then extract email from a builder, check the emails domain
     *   and reset builder.
     *
     * @return sorted unique emails
     */
    public Set<String> extractEmailsList() {
        EtmPoint etmPoint = etmMonitor.createPoint("EmailAddressProcessor: extractEmailsList");
        Set<String> emails = new TreeSet<>();
        StringBuilder stringBuilder = new StringBuilder();
        String stringWithEmails = this.extractStringWithEmails();
        char[] stringCharacters = stringWithEmails.toCharArray();
        int arrayLength = stringCharacters.length;
        for (int i = 0; i < arrayLength; i++) {
            char s = stringCharacters[i];
            if (addToBuilder(s)) {
                stringBuilder.append(s);
            }
            if (addToList(s, i, stringBuilder, arrayLength)) {
                String extractedEmail = stringBuilder.toString();
                if (isOrgDomain(extractedEmail)) {
                    emails.add(extractedEmail);
                }
                stringBuilder = new StringBuilder();
            }
        }
        etmPoint.collect();
        return emails;
    }

    public String extractStringWithEmails() {
        EtmPoint etmPoint = etmMonitor.createPoint("EmailAddressProcessor: extractStringWithEmails");
        int i = 0;
        if (StringUtils.isEmpty(rawString)) {
            return "";
        }

        char[] rawStringCharacters = rawString.toCharArray();
        do {
            i++;
            if (i == rawString.length()) {
                break;
            }
        } while (isPartOfTelephoneNumber(rawStringCharacters[i]));
        etmPoint.collect();
        return rawString.substring(i);
    }

    private boolean isOrgDomain(String validEmailAddress) {
        String[] parts = validEmailAddress.split("\\.");
        String domain = parts[1];
        return "org".equals(domain);
    }

    private boolean isSeparatorCharacter(char character) {
        char[] separatorCharacters = ",;  \t".toCharArray();
        for (char c : separatorCharacters) {
            if (character == c) {
                return true;
            }
        }
        return false;
    }

    private boolean addToList(char s, int currentPosition, StringBuilder stringBuilder, int length) {
        return (isSeparatorCharacter(s) || ((currentPosition + 1) == length)) && (!"".equals(stringBuilder.toString()));
    }

    private boolean addToBuilder(char currentCharacter) {
        return (!isSeparatorCharacter(currentCharacter)) && (!"".equals(currentCharacter));
    }

    private boolean isPartOfTelephoneNumber(char character) {
        EtmPoint etmPoint = etmMonitor.createPoint("EmailAddressProcessor: isPartOfTelephoneNumber");
        boolean b = "0123456789+()- ".contains(String.valueOf(character));
        etmPoint.collect();
        return b;
    }
}
