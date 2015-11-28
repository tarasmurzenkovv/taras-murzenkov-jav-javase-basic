import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ArchiveProcessor {
    public static Map<ZipEntry, Boolean> files = new HashMap<>();

    public static Set<String> getNumbers(Path path) throws IOException {
        return Files
                .lines(path)
                .map(line -> new TelephoneNumberProcessor(line).extractFineGrainedTelephoneNumber())
                .collect(Collectors.toSet());
    }

    public static Set<String> getEmails(Path path) throws IOException {
        Set<String> result = new TreeSet<>();
        Files.lines(path).forEach(line -> new EmailAddressProcessor(line).extractEmailsList());
        return result;
    }

    /**
     * Recursively process a given archive.
     * @param file - zip archive file
     */

    public static void getListOfFilesFromArchive(File file){
        try(ZipFile zipFile = new ZipFile(file)){
            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()){
                ZipEntry zipEntry = (ZipEntry)entries.nextElement();
                if(zipEntry.toString().endsWith(".zip")){
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    getListOfFilesFromArchive(ArchiveProcessor.createFile(inputStream));
                }else{
                    System.out.println(zipEntry.toString());
                }
            }
        }catch (IOException e){

        }
    }

    private static File createFile(InputStream inputStream) throws IOException {
        File innerFile = File.createTempFile("tmp", "zip");
        OutputStream outputStream = new FileOutputStream(innerFile);
        IOUtils.copy(inputStream,outputStream);
        return innerFile;
    }
}
