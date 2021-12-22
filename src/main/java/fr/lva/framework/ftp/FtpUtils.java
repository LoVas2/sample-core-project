package fr.lva.framework.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public final class FtpUtils {

    private static final Logger LOG = LogManager.getLogger(FtpUtils.class);

    private static final String FILE_NAME_REMOTE_1 = "file1Sended.json";
    private static final String FILE_NAME_REMOTE_2 = "file2Sended.json";

    public static void sendFile(File file) {

        String server = "localhost";
        int port = 21;
        String user = "test";
        String pass = "test";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            InputStream inputStream = new FileInputStream(file);

            LOG.debug("Start uploading first file");
            boolean done = ftpClient.storeFile(FILE_NAME_REMOTE_1, inputStream);
            inputStream.close();
            if (done) {
                LOG.debug("The first file is uploaded successfully.");
            }

            // APPROACH #2: uploads second file using an OutputStream
            inputStream = new FileInputStream(file);

            LOG.debug("Start uploading second file");
            OutputStream outputStream = ftpClient.storeFileStream(FILE_NAME_REMOTE_2);
            byte[] bytesIn = new byte[4096];
            int read;

            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            inputStream.close();

            /*
            outputStream.close();
            boolean completed = ftpClient.completePendingCommand();
            if (completed) {
                LOG.debug("The second file is uploaded successfully.");
            } */

        } catch (IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ioe) {
                LOG.error(ioe.getMessage(), ioe);
            }
        }
    }

    private FtpUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

}
