package com.estafet.bankx.camel.base;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Created by estafet.
 */
public class FtpServerTestSupport {

    private static final String FTP_ROOT_DIR = "./target/res/home";
    private static final File USERS_FILE = new File("./src/test/resources/etc/users.properties");
    private static final String DEFAULT_LISTENER = "default";

    public static FtpServer setUp() throws Exception {
        CamelTestSupport.deleteDirectory(FTP_ROOT_DIR);

        FtpServerFactory factory = createFtpServerFactory();
        if (factory != null) {
            FtpServer ftpServer = factory.createServer();
            if (ftpServer != null) {
                ftpServer.start();
                return ftpServer;
            }
        }

        return null;
    }

    public static void tearDown(FtpServer ftpServer) throws Exception {

        if (ftpServer != null) {
            try {
                ftpServer.stop();
            } catch (Exception e) {
                // ignore while shutting down as we could be polling during shutdown
                // and get errors when the ftp server is stopping. This is only an issue
                // since we host the ftp server embedded in the same jvm for unit testing
            }
        }
    }

    private static FtpServerFactory createFtpServerFactory() throws Exception {
        assertTrue(USERS_FILE.exists());
        assertTrue("Port number is not initialized in an expected range: " + ServerTestSupport.port, ServerTestSupport.port >= 21000);

        NativeFileSystemFactory fileSystemFactory = new NativeFileSystemFactory();
        fileSystemFactory.setCreateHome(true);

        PropertiesUserManagerFactory pumf = new PropertiesUserManagerFactory();
        pumf.setAdminName("admin");
        pumf.setPasswordEncryptor(new ClearTextPasswordEncryptor());
        pumf.setFile(USERS_FILE);
        UserManager userManager = pumf.createUserManager();

        ListenerFactory factory = new ListenerFactory();
        factory.setPort(ServerTestSupport.port);

        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.setUserManager(userManager);
        serverFactory.setFileSystem(fileSystemFactory);
        serverFactory.setConnectionConfig(new ConnectionConfigFactory().createConnectionConfig());
        serverFactory.addListener(DEFAULT_LISTENER, factory.createListener());

        return serverFactory;
    }

//    public void sendFile(String url, Object body, String fileName) {
//        template.sendBodyAndHeader(url, body, Exchange.FILE_NAME, simple(fileName));
//    }
}