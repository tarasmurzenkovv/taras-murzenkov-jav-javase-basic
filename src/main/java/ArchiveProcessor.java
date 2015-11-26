import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArchiveProcessor {
    private static EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    public static Set<String> getNumbers(Path path) {
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
        EtmPoint etmPoint = etmMonitor.createPoint("EmailAddressProcessor: getEmails");
        Set<String> result = new TreeSet<>();
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> result.addAll(new EmailAddressProcessor(line).extractEmailsList()));
        } catch (IOException e) {

        }
        etmPoint.collect();
        return result;
    }
}
