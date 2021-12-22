package fr.lva.framework.ftp;

import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FtpTest {

    @Test
    public void sendFileTest() throws IOException {
        FakeFtpServer fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(21);
        fakeFtpServer.start();

        Path path = Files.createTempFile("tmpFile", ".tmp");

        FtpUtils.sendFile(path.toFile());

        fakeFtpServer.stop();
    }
}
