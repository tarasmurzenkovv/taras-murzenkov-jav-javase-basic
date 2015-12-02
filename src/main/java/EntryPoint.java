import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class EntryPoint {
    public static void main(String[] args) {
        System.out.println("Enter path to archive e.x. C:\\Documents\\zip_archive.zip");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String pathToZipArchive = scanner.nextLine();
        if (StringUtils.isEmpty(pathToZipArchive)) {
            System.out.println("Specify path to the archive. ");
            pathToZipArchive = scanner.nextLine();
        }
        try {
            System.out.println("Processing the given archive: " + pathToZipArchive);
            ArchiveProcessor.processArchive(pathToZipArchive);
            System.out.println("Finished processing the given archive: " + pathToZipArchive);
        } catch (IOException | ZipException e) {
            System.out.println("Exception occurred while processing the given archive. Exception: " + e);
        }
    }
}
