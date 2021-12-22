/*
 * Copyright 2017-2018 by Wordline, an Atos Compagny. All rights reserved. This software is the
 * confidential and proprietary information of Atos ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Atos. En référence à l’article 17.2 du CCAP du marché
 * n°2016-05, passé entre l’ASIP Santé et la société Worldine et relatif aux Prestations de
 * construction, d’hébergement, d’exploitation des infrastructures téléphoniques, de déploiement
 * technique de la solution SI-Samu et de services téléphoniques, les droits de propriété
 * intellectuelle de Worldline sur ce programme seront cédés à l’ASIP Santé à l’issue du marché,
 * notifié le 5 mai 2017 pour une durée initiale de 5 ans et reconductible une fois pour une période
 * d’un an.
 */
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
