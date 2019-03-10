package com.content_load_sb.downloader;

/**
 * Created by asd on 10.11.2017.
 */
public interface DataDownloader {
    /**
     * Download data.
     *
     * @param URL               the url
     * @param destinationFolder the destination folder
     */
    public void downloadData(String URL, String destinationFolder);

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    public String getProtocol();
}
