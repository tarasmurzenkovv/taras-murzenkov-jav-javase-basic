import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * I assume that each telephone number contains + () and its length is not relevant.
 * I.e. the following telephone number is valid +1(203)
 */

public class TelephoneNumberProcessor {
    private String rawTelephoneNumber;

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

    public String extractFineGrainedTelephoneNumber(){
        return processCityCode(extractTelephoneNumberFromAString(rawTelephoneNumber));
    }

    public static Set<String> getAllNumbersFromFile(Path path){
        Set<String> result = new TreeSet<>();
        try (Stream<String> lines = Files.lines(path)) {
            result  = lines
                    .map(line -> new TelephoneNumberProcessor(line).extractFineGrainedTelephoneNumber())
                    .collect(Collectors.toSet());
        } catch (IOException e){

        }
        return result;
    }
}
