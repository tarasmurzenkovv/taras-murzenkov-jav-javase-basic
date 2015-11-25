import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArchiveProcessor {

    public static Set<String> getAllNumbersFromFile(Path path) {
        Set<String> result = new TreeSet<>();
        try (Stream<String> lines = Files.lines(path)) {
            result = lines
                    .map(line -> new TelephoneNumberProcessor(line).extractFineGrainedTelephoneNumber())
                    .collect(Collectors.toSet());
        } catch (IOException e) {

        }
        return result;
    }

    public static Set<String> getEmails(Path path) {
        Set<String> result = new TreeSet<>();
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> result.addAll(new EmailAddressProcessor(line).extractEmailsList()));
        } catch (IOException e) {

        }
        return result;
    }
}
