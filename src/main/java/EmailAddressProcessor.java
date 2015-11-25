import java.util.Set;
import java.util.TreeSet;

public class EmailAddressProcessor {
    private String rawString;

    public EmailAddressProcessor(String rawString) {
        this.rawString = rawString;
    }

    public String extractStringWithEmails() {
        int i = 0;
        do {
            i++;
            if (i == rawString.length()) {
                break;
            }
        } while ("0123456789+()- ".contains(rawString.substring(i, i + 1)));

        return rawString.substring(i);
    }

    /**
     * @return sorted unique emails
     */
    public Set<String> extractEmailsList() {
        Set<String> emails = new TreeSet<>();
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(rawString);
        for (int i = 0; i < rawString.length(); i++) {
            String s = rawString.substring(i, i + 1);
            if (addToBuilder(s)) {
                stringBuilder.append(s);
            }
            if (addToList(s, i, stringBuilder)) {
                String extractedEmail = stringBuilder.toString();
                if(isOrgDomain(extractedEmail)){
                    emails.add(extractedEmail);
                }
                stringBuilder = new StringBuilder();
            }
        }
        return emails;
    }

    private boolean isOrgDomain(String validEmailAddress){
        String[] parts = validEmailAddress.split("\\.");
        String domain = parts[1];
        return "org".equals(domain);
    }

    private boolean isSeparatorCharacter(String c) {
        return ",;  \t".contains(c);
    }

    private boolean addToList(String s, int currentPosition, StringBuilder stringBuilder) {
        return (isSeparatorCharacter(s) || ((currentPosition + 1) == rawString.length())) && (!"".equals(stringBuilder.toString()));
    }

    private boolean addToBuilder(String currentCharacter) {

        return (!isSeparatorCharacter(currentCharacter)) && (!"".equals(currentCharacter));
    }

}
