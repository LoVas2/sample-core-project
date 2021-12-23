package fr.lva.framework.utils;

import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileKeyMatcher {

    private final static int    BUFSIZE  = 32 * 1024; // 32 KB
    private final static String GZIP_EXT = ".gz";

    public static void main(String[] args) throws IOException {
        String keysFile = args[0];
        String jsonInFile = args[1];
        String jsonOutFile = (args.length > 2) ? args[2] : "out.sjson.gz";

        long start = System.currentTimeMillis();

        Set<String> knownKeys = loadKnownKeys(keysFile);
        Pattern p = Pattern.compile("\"_id\":\"([A-Z0-9_]+)\"");

        int readLines = 0, savedLines = 0;
        try (BufferedReader in = newJsonReader(jsonInFile);
             BufferedWriter out = newJsonWriter(jsonOutFile)) {
            String l;
            while ((l = in.readLine()) != null) {
                readLines++;
                Matcher m = p.matcher(l);
                if (m.find()) {
                    String key = m.group(1);
                    if (!knownKeys.contains(key)) {
                        out.write(l);
                        out.write('\n');
                        savedLines++;
                    }
                } else {
                    System.out.println("Regex match failed:" + l);
                }
            }
        }
        double d = (System.currentTimeMillis() - start) / 1000.0;
        System.out.println(
                "" + savedLines + " / " + readLines + " lines written\nProcessing time : " + d + " s");
    }

    private final static Set<String> loadKnownKeys(String f) throws IOException {
        int i = 0;
        Set<String> keys = new ConcurrentSkipListSet<>();
        try (BufferedReader in =
                     new BufferedReader(new InputStreamReader(new FileInputStream(f), UTF_8), BUFSIZE)) {
            String l;
            while ((l = in.readLine()) != null) {
                i++;
                keys.add(l);
            }
        }
        System.out.println("" + i + " known keys read");
        return keys;
    }

    private final static BufferedReader newJsonReader(String f) throws IOException {
        InputStream in = new FileInputStream(f);
        in = (f.endsWith(GZIP_EXT)) ? new GZIPInputStream(in, BUFSIZE)
                : new BufferedInputStream(in, BUFSIZE);
        final CharsetDecoder decoder = UTF_8.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPORT);
        return new BufferedReader(new InputStreamReader(in, decoder), BUFSIZE);
    }

    private final static BufferedWriter newJsonWriter(String f) throws IOException {
        OutputStream out = new FileOutputStream(f);
        out = (f.endsWith(GZIP_EXT)) ? new GZIPOutputStream(out, BUFSIZE)
                : new BufferedOutputStream(out, BUFSIZE);
        return new BufferedWriter(new OutputStreamWriter(out, UTF_8), BUFSIZE);
    }
}
