import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ArchiveProcessor {

    private static final String TMP = "tmp";
    private static final String ZIP = ".zip";
    private static final String GZ = ".gz";

    public static Set<String> getNumbers(Path path) throws IOException {
        return Files
                .lines(path)
                .map(line -> new TelephoneNumberProcessor(line).extractFineGrainedTelephoneNumber())
                .collect(Collectors.toSet());
    }

    public static Set<String> getEmails(Path path) throws IOException {
        Set<String> result = new TreeSet<>();
        Files.lines(path)
                .forEach(line -> result.addAll(new EmailAddressProcessor(line).extractEmails()));
        return result;
    }

    /**
     * Recursively process a given archive.
     *
     * @param file - gz/zip archive file
     */
    public static void processArchive(File file, Set<String> emails, Set<String> numbers) {
        // according to .gz format specification this archive format can contain only one file => simply extract it.
        try {
            if (isGZippedFile(file)) {
                createFile(new FileInputStream(file), true);
            } else {
                ZipFile zipFile = new ZipFile(file);
                Enumeration entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                    if (ArchiveProcessor.toDigIn(zipEntry)) {
                        InputStream inputStream = zipFile.getInputStream(zipEntry);
                        File tmpFile = ArchiveProcessor.createFile(inputStream, false);
                        processArchive(tmpFile, emails, numbers);
                    } else {
                        boolean isGz = ArchiveProcessor.isGZIppedArchiveEntry(zipEntry);
                        File tmpFile = createFile(zipFile.getInputStream(zipEntry), isGz);
                        emails.addAll(ArchiveProcessor.getEmails(tmpFile.toPath()));
                        numbers.addAll(ArchiveProcessor.getNumbers(tmpFile.toPath()));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to process a given file: " + file.getName() + ". Exception stack trace: " + e);
        }
    }

    private static File createFile(InputStream inputStream, boolean isGz) throws IOException {
        File innerFile;
        if (isGz) {
            innerFile = File.createTempFile(TMP, GZ);
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
            OutputStream outputStream = new FileOutputStream(innerFile);
            IOUtils.copy(gzipInputStream, outputStream);
        } else {
            innerFile = File.createTempFile(TMP, ZIP);
            OutputStream outputStream = new FileOutputStream(innerFile);
            IOUtils.copy(inputStream, outputStream);
        }
        return innerFile;
    }

    private static boolean toDigIn(ZipEntry zipEntry) {
        return zipEntry.toString().endsWith(ZIP) || zipEntry.isDirectory();
    }

    private static boolean isGZippedFile(File file) {
        return file.toString().endsWith(GZ);
    }

    private static boolean isGZIppedArchiveEntry(ZipEntry entry) {
        return entry.toString().endsWith(GZ);
    }
}
