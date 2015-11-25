import java.util.ArrayList;
import java.util.List;

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

    public boolean isSeparatorCharacter(String c) {
        return ",;  ".contains(c);
    }

    public List<String> extractEmailsList() {
        List<String> emails = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= rawString.length() - 1; i++) {
            String s = rawString.substring(i, i + 1);
            boolean toAdd = isSeparatorCharacter(s) || ((i + 1) == rawString.length());
            if (!isSeparatorCharacter(s)) {
                stringBuilder.append(s);
            }
            if (toAdd) {
                emails.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }
        }
        return emails;
    }
}
