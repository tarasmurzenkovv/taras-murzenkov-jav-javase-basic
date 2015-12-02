import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ArchiveProcessor {
    private static final String TMP = "tmp";
    private static final String ZIP = ".zip";
    private static final String GZ = ".gz";
    private static final String TXT = ".txt";
    private static final String EMAILS = "emails";
    private static final String NUMBERS = "numbers";
    public static final String LINE_SEPARATOR = "line.separator";

    public static boolean isProcessed = false;

    /**
     * Appends the given sets of emails and phone numbers to the zip archive file.
     * For each Set<String>() will be created a separate txt file.
     * The given sets of emails and number will be sorted in a natural order
     *
     * @param pathToZipArchive - path to zip archive file
     * @throws IOException
     */

    public static void processArchive(String pathToZipArchive) throws IOException, ZipException {

        Set<String> emails = new HashSet<>();
        Set<String> numbers = new HashSet<>();

        File zipArchive = new File(pathToZipArchive);
        ArchiveProcessor.processArchive(zipArchive, emails, numbers);

        File fileWithEmails = ArchiveProcessor.sortAndWriteSetToFile(emails, EMAILS);
        File fileWithNumbers = ArchiveProcessor.sortAndWriteSetToFile(numbers, NUMBERS);

        ArchiveProcessor.appendFileToArchive(zipArchive, fileWithEmails);
        ArchiveProcessor.appendFileToArchive(zipArchive, fileWithNumbers);
        ArchiveProcessor.isProcessed = true;
    }

    /**
     * Recursively process a given archive.
     *
     * @param file - gz/zip archive file
     */
    public static void processArchive(File file, Set<String> emails, Set<String> numbers) throws IOException {
        if (isGZippedFile(file)) {
            createFile(new FileInputStream(file), true);
        } else {
            if (file != null) {
                boolean isZipped = new ZipInputStream(new FileInputStream(file)).getNextEntry() != null;
                if (isZipped) {
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
            }
        }
    }

    private static File sortAndWriteSetToFile(Set<String> givenSet, String fileName) throws IOException {
        File tmpOutputFile = File.createTempFile(fileName, TXT);
        File outputFile = new File(fileName + TXT);
        givenSet = givenSet.stream().sorted().collect(Collectors.toSet());
        givenSet
                .stream()
                .forEach(element -> {
                    try {
                        element += System.getProperty(LINE_SEPARATOR);
                        Files.write(tmpOutputFile.toPath(), element.getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        boolean ableToRename = tmpOutputFile.renameTo(outputFile);
        if (!ableToRename) {
            Files.delete(outputFile.toPath());
            tmpOutputFile.renameTo(outputFile);
        }

        return outputFile;
    }

    public static void appendFileToArchive(File file, File toAppendFile) throws net.lingala.zip4j.exception.ZipException {
        net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(file);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        zipFile.addFile(toAppendFile, parameters);
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
