package com.content_load_sb.downloader.http;

import com.content_load_sb.downloader.DataDownloader;
import com.content_load_sb.downloader.DownloadEntity;
import com.content_load_sb.downloader.DownloadManager;
import com.content_load_sb.downloader.DownloadManagerException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by asd on 10.11.2017.
 */
public class HTTPDownloader implements DataDownloader {

    /**
     * The Constant logger.
     */
    private static final Logger logger = Logger.getLogger(HTTPDownloader.class.getName());
    /**
     * The default http port.
     */
    final Integer DEFAULT_HTTP_PORT = 80;
    /**
     * The http prefix.
     */
    private final String HTTP_PREFIX = "http://";

    /**
     * The protocol http.
     */
    private final String PROTOCOL_HTTP = "http";
    /**
     * The download manager.
     */
    DownloadManager downloadManager;

    // Properties props = null;

    /**
     * Instantiates a new HTTP downloader.
     *
     * @throws DownloadManagerException the download manager exception
     */
    // default constructor
    public HTTPDownloader() throws DownloadManagerException {
        downloadManager = new HTTPDownloadManager();
        /*
         * props = new Properties();
		 *
		 * try { props.load(getClass().getClassLoader().getResourceAsStream(
		 * "http.properties")); } catch (FileNotFoundException
		 * fileNotFoundException) { logger.severe(
		 * "exception while reading from properties:"
		 * +fileNotFoundException.getMessage()); throw new
		 * DownloadManagerException(fileNotFoundException.getMessage()); }
		 *
		 * catch (IOException ioException) { logger.severe(
		 * "exception while reading from properties:"+ioException.getMessage());
		 * throw new DownloadManagerException(ioException.getMessage()); }
		 *
		 * catch (Exception exception) { logger.severe(
		 * "exception while reading from properties:"+exception.getMessage());
		 * throw new DownloadManagerException(exception.getMessage()); }
		 */

    }

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws DownloadManagerException the download manager exception
     */
    public static void main(String[] args) throws DownloadManagerException {

        String downloadURL = "http://tutorialspoint.com/java/java_tutorial.pdf";

        String destinationFolder = "/Users/m01457/Downloads/destination/";

        HTTPDownloader httpDownloader = new HTTPDownloader();

        httpDownloader.downloadData(downloadURL, destinationFolder);

    }

    /* (non-Javadoc)
     * @see DataDownloader#downloadData(java.lang.String, java.lang.String)
     */
    public void downloadData(String downloadURL, String destinationFolder) {

        // parse URL and construct HTTPEntity
        DownloadEntity httpEntity = parseURL(downloadURL);
        String destination = null;
        // connect to HTTP Server
        try {
            boolean isConnected = downloadManager.connect(httpEntity.getHost(), httpEntity.getPort(),
                    httpEntity.getUserId(), httpEntity.getPassword());

            // download functionality
            if (isConnected) {
                destination = destinationFolder.concat(File.separator).concat(httpEntity.getRemoteURL());
                downloadManager.download(httpEntity.getRemoteURL(), destination);
            }

            // diconnect from http Server
            downloadManager.disconnect();
        } catch (DownloadManagerException e) {
            // to make sure we dont have incomplete download data, delete the
            // destination File
            try {
                if (destination != null && !destination.isEmpty()) {
                    FileUtils.forceDelete(new File(destination));
                }

            } catch (IOException ioException) {
                // no neeed to handle it
            }
            logger.severe("Download data Failed:" + e.getMessage());

        }

    }

    /**
     * Parses the URL.
     *
     * @param downloadURL the download URL
     * @return the download entity
     */
    private DownloadEntity parseURL(String downloadURL) {
        DownloadEntity httpEntity = new DownloadEntity();

        int http_prefix_length = HTTP_PREFIX.length();

        int host_details_end_prefix = downloadURL.indexOf("/", http_prefix_length);

        String host_details = downloadURL.substring(http_prefix_length, host_details_end_prefix);

        String host = new String();

        int port = DEFAULT_HTTP_PORT;

        String userId = new String();
        String password = new String();

        if (host_details.indexOf(":") == -1) {
            host = host_details;

        } else {
            String[] details = host_details.split(":");
            if (details != null && details.length == 2) {
                host = details[0];
                port = Integer.parseInt(details[1]);
            }
        }

        httpEntity.setHost(host);
        httpEntity.setPort(port);

        // String remoteURL = downloadURL.substring(host_details_end_prefix +
        // 1);
        httpEntity.setRemoteURL(downloadURL);

        try {

            // userId = props.getProperty("userId").trim();
            // password = props.getProperty("password").trim();

            httpEntity.setUserId(userId);
            httpEntity.setPassword(password);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return httpEntity;

    }

    /* (non-Javadoc)
     * @see DataDownloader#getProtocol()
     */
    public String getProtocol() {
        return PROTOCOL_HTTP;
    }

}
