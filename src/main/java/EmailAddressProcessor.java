import java.util.ArrayList;
import java.util.List;

public class EmailAddressProcessor {
    private String rawString;

    public EmailAddressProcessor(String rawString) {
        this.rawString = rawString;
    }

    public String extractStringWithEmails() {
        String validCharacterOfTelephoneNumber = "0123456789+()- ";
        int i = 0;
        while (validCharacterOfTelephoneNumber.contains(rawString.substring(i, i + 1))) {
            i++;
        }
        return rawString.substring(i);
    }

    public boolean isSeparatorCharacter(String c) {
        return ",;  ".contains(c);
    }

    public List<String> extractEmailsList() {
        List<String> emails = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= rawString.length()-1; i++) {
            String s = rawString.substring(i, i + 1);
            if (isSeparatorCharacter(s) ||(i+1 == rawString.length())) {
                emails.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }
            if(!isSeparatorCharacter(s)){
                stringBuilder.append(s);
            }
        }
        return emails;
    }
}
