import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * I assume that each telephone number contains + () and its length is not relevant.
 * I.e. the following telephone number is valid +1(203)
 */

public class TelephoneNumberProcessor {
    String rawTelephoneNumber;

    public TelephoneNumberProcessor(String rawTelephoneNumber) {
        this.rawTelephoneNumber = rawTelephoneNumber;
    }

    private String removeSpacesAndCharacters(String rawTelephoneNumber) {
        return rawTelephoneNumber.replaceAll("[^+()0123456789]", "");
    }

    public String processCityCode(String processedTelephoneNumber) {

        if(StringUtils.isEmpty(processedTelephoneNumber)){
            return "";
        }

        int positionOfBra = processedTelephoneNumber.indexOf("(") + 1;
        int positionOfKet = processedTelephoneNumber.indexOf(")");
        int telephoneNumberLength = processedTelephoneNumber.length();

        String cityCode = processedTelephoneNumber.substring(positionOfBra, positionOfKet);

        switch (cityCode) {
            case "101":
                cityCode = "401";
                break;
            case "202":
                cityCode = "802";
                break;
            case "301":
                cityCode = "321";
                break;
            default:
                break;
        }

        return processedTelephoneNumber.substring(0, positionOfBra) + cityCode +
                processedTelephoneNumber.substring(positionOfKet, telephoneNumberLength);
    }

    public String extractTelephoneNumberFromAString(String rawString) {
        if (rawString.length() > 1) {
            if (rawString.contains("@")) {
                String[] splittedStrings = rawString.split("@");
                if(splittedStrings.length == 0){
                    return "";
                }
                rawTelephoneNumber = splittedStrings[0];
                if (StringUtils.isEmpty(rawTelephoneNumber)) {
                    return "";
                }
            }
            return removeSpacesAndCharacters(rawTelephoneNumber);
        } else {
            return "";
        }
    }

    public String extractFinegrainedTelephoneNumber(){
        return processCityCode(extractTelephoneNumberFromAString(rawTelephoneNumber));
    }

    public List<TelephoneNumberProcessor> getSortedTelephoneNumbers(List<TelephoneNumberProcessor> unsortedListOfTelephoneNumbers) {
        return null;
    }
}
