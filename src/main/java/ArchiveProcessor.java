import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
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
     * Depth for search algorithm to recursively process a given archive.
     * @param zipFile
     */

    // http://stackoverflow.com/questions/981578/how-to-unzip-files-recursively-in-java
    public static void getListOfFilesFromArchive(ZipFile zipFile) throws IOException {
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()){
            ZipEntry zipEntry = (ZipEntry)entries.nextElement();
            if(zipEntry.toString().endsWith(".zip")){
                // How can I get a list of files from the archive? Simply calling getListOfFiles(new ZipFile(zipEntry)) gives me: FileNotFoundException: inputs\--11--\data.zip
                InputStream stream = zipFile.getInputStream(zipEntry);
                ZipFile enclosedZipFile = new ZipFile(stream);
                getListOfFilesFromArchive(new ZipFile(zipEntry.getName()));//gives me: FileNotFoundException: inputs\--11--\data.zip
            }else{


            }
        }
    }

    static public void extractFolder(String zipFile) throws ZipException, IOException
    {
        System.out.println(zipFile);
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
        String newPath = zipFile.substring(0, zipFile.length() - 4);

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements())
        {

            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
            }

            if (currentEntry.endsWith(".zip"))
            {
                // found a zip file, try to open
                extractFolder(destFile.getAbsolutePath());
            }
        }
    }
}
