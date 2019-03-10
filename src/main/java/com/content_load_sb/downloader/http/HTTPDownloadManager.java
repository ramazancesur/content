package com.content_load_sb.downloader.http;

import com.content_load_sb.downloader.DownloadManager;
import com.content_load_sb.downloader.DownloadManagerException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.*;
import java.util.logging.Logger;


/**
 * Created by asd on 10.11.2017.
 */
public class HTTPDownloadManager implements DownloadManager {
    /**
     * The Constant logger.
     */
    private static final Logger logger = Logger.getLogger(HTTPDownloadManager.class.getName());

    /**
     * The client.
     */
    HttpClient client;

    /**
     * The method.
     */
    GetMethod method;

    /**
     * The connection.
     */
    // HttpConnection connection;
    HttpConnection connection;

    /**
     * Instantiates a new HTTP download manager.
     */
    public HTTPDownloadManager() {

        client = new HttpClient();
        method = new GetMethod();
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see DownloadManager#connect(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
     */
    public boolean connect(String host, Integer port, String userName, String password)
            throws DownloadManagerException {
        boolean isConnected = false;

        logger.info("Connecting to the server ...");

        HostConfiguration hc = new HostConfiguration();
        hc.setHost(host, port, "http");

        HttpConnectionManager connmgr = new SimpleHttpConnectionManager();
        try {

            connection = connmgr.getConnectionWithTimeout(hc, 0);

        } catch (ConnectionPoolTimeoutException connectionPoolTimeoutException) {
            logger.severe("exception while opening http connection:" + connectionPoolTimeoutException.getMessage());
            throw new DownloadManagerException(connectionPoolTimeoutException.getMessage());
        }

        try {
            logger.info("opening connection to http");
            connection.open();
        } catch (IOException ex) {
            // isConnected = false;
            // ex.printStackTrace();
            logger.severe("exception while opening connection:" + ex.getMessage());
            throw new DownloadManagerException(ex.getMessage());
        }

        client.setHttpConnectionManager(connmgr);

        isConnected = connection.isOpen();

        System.out.println("isConnected:" + isConnected);

        if (isConnected) {
            logger.info("The client was connected successfully!");
        }

        connmgr.releaseConnection(connection);

        return isConnected;
    }

    /* (non-Javadoc)
     * @see DownloadManager#download(java.lang.String, java.lang.String)
     */
    public void download(String remoteURL, String destination) throws DownloadManagerException {

        OutputStream outputStream = null;
        InputStream inputStream = null;
        File downloadFile2 = null;

        try {

            // method = new GetMethod(remoteURL);

            logger.info("in download api remoteURL is:" + remoteURL);

            String charset = "UTF-8";

            URI uri = new URI(remoteURL, true, charset);

            method.setURI(uri);

            logger.info("uri in download is :" + method.getURI());

            // Provide custom retry handler is necessary
            // method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            // new DefaultHttpMethodRetryHandler(3, false));

            logger.info("invoking execute method");

            // Execute the method.
            int statusCode = client.executeMethod(method);

            logger.info("status code is:" + statusCode);

            if (statusCode != HttpStatus.SC_OK) {
                logger.severe(
                        "Error:" + "File Download failed: " + remoteURL + " " + HttpStatus.getStatusText(statusCode));
                // return;
                throw new DownloadManagerException("Resource Not Available");
            }

            downloadFile2 = new File(destination);

            downloadFile2.getParentFile().mkdirs();
            downloadFile2.createNewFile();

            outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile2));

            logger.info("File Downloading...");

            inputStream = method.getResponseBodyAsStream();

            byte[] buffer = new byte[4096];

            int numRead;

            while ((numRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, numRead);

            }

            logger.info("File has been downloaded successfully.");

        } catch (HttpException httpException) {
            logger.severe("Fatal protocol violation: " + httpException.getMessage());
            throw new DownloadManagerException(httpException.getMessage());
        } catch (IOException ioException) {

            logger.severe("Fatal transport error: " + ioException.getMessage());
            throw new DownloadManagerException(ioException.getMessage());
        } catch (Exception exception) {
            logger.severe("Fatal  error: " + exception.getMessage());
            throw new DownloadManagerException(exception.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
            }
        }

    }

    /* (non-Javadoc)
     * @see DownloadManager#disconnect()
     */
    public void disconnect() {

        if (this.method != null) {

            logger.info("disconnecting ...");
            this.method.releaseConnection();

            logger.info("The client was disconnected successfully!");

        }

    }
}
