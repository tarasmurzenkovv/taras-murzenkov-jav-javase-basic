import java.util.List;

/**
 * I assume that each telephone number contains + () and its length is not relevant.
 */

public class TelephoneNumberProcessor {
    String rawTelephoneNumber;

    public TelephoneNumberProcessor(String rawTelephoneNumber) {
        this.rawTelephoneNumber = rawTelephoneNumber;
    }

    public String extractTelephoneNumberFromAString(String rawString) {
        if(rawString.length() >1){
            if(rawString.contains("@")){
                rawTelephoneNumber = rawString.split("@")[0];
            }
            return removeSpacesAndCharacters(rawTelephoneNumber);
        }else{
            return "";
        }
    }

    private String removeSpacesAndCharacters(String rawTelephoneNumber) {
        return rawTelephoneNumber.replaceAll("[^+()0123456789]","");
    }

    public List<TelephoneNumberProcessor> getSortedTelephoneNumbers(List<TelephoneNumberProcessor> unsortedListOfTelephoneNumbers) {
        return null;
    }
}
