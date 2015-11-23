import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * I assume that each telephone number contains + () and its length is not relevant.
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
                rawString = rawString.replaceAll("@", "");
                if (StringUtils.isEmpty(rawString)) {
                    return "";
                }
                rawTelephoneNumber = rawString.split("@")[0];
            }
            return removeSpacesAndCharacters(rawTelephoneNumber);
        } else {
            return "";
        }
    }

    public List<TelephoneNumberProcessor> getSortedTelephoneNumbers(List<TelephoneNumberProcessor> unsortedListOfTelephoneNumbers) {
        return null;
    }
}
