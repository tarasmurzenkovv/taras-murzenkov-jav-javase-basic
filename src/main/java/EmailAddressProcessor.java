import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class EmailAddressProcessor {
    private String rawString;

    public EmailAddressProcessor(String rawString) {
        this.rawString = rawString;
    }

    /**
     * For each char at string with emails:
     *
     * - if it belongs to valid email characters set then add it to string builder;
     * - if it belongs to separator set of characters then
     *                               - extract email from a builder;
     *                               - check the emails domain. if it is org then add to TreeSet<>();
     *                               - reset builder.
     *
     * @return sorted unique emails
     */
    public Set<String> extractEmails() {
        Set<String> emails = new HashSet<>();
        StringBuilder stringBuilder = new StringBuilder();
        String stringWithEmails = this.extractStringWithEmails();
        char[] stringCharacters = stringWithEmails.toCharArray();
        int arrayLength = stringCharacters.length;
        for (int i = 0; i < arrayLength; i++) {
            char s = stringCharacters[i];
            if (addToBuilder(s)) {
                stringBuilder.append(s);
            }
            if (addToSet(s, i, stringBuilder, arrayLength)) {
                String extractedEmail = stringBuilder.toString();
                if (isOrgDomain(extractedEmail)) {
                    emails.add(extractedEmail);
                }
                stringBuilder = new StringBuilder();
            }
        }
        return emails;
    }

    public String extractStringWithEmails() {
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

    private boolean addToSet(char s, int currentPosition, StringBuilder stringBuilder, int length) {
        return (isSeparatorCharacter(s) || ((currentPosition + 1) == length)) && (!"".equals(stringBuilder.toString()));
    }

    private boolean addToBuilder(char currentCharacter) {
        return (!isSeparatorCharacter(currentCharacter)) && (!"".equals(String.valueOf(currentCharacter)));
    }

    private boolean isPartOfTelephoneNumber(char character) {
        return  "0123456789+()- ".contains(String.valueOf(character));
    }
}
