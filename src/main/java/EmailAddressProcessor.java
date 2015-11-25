import java.util.ArrayList;
import java.util.List;

public class EmailAddressProcessor {
    private String rawString;

    public EmailAddressProcessor(String rawString) {
        this.rawString = rawString;
    }

    private boolean isSeparatorCharacter(String c) {
        return ",;  ".contains(c);
    }
    private boolean addToList(String s, int currentPosition, StringBuilder stringBuilder){
        return (isSeparatorCharacter(s) || ((currentPosition + 1) == rawString.length()))&&(!"".equals(stringBuilder.toString()));
    }
    private boolean addToBuilder(String currentCharacter){
        return (!isSeparatorCharacter(currentCharacter))&&(!"".equals(currentCharacter));
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

    public List<String> extractEmailsList() {
        List<String> emails = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= rawString.length() - 1; i++) {
            String s = rawString.substring(i, i + 1);

            if (addToBuilder(s)) {
                stringBuilder.append(s);
            }
            if (addToList(s,i,stringBuilder)) {
                emails.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }
        }
        return emails;
    }
}
