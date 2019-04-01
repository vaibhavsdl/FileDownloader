package com.agoda.service;

import java.io.File;
import java.net.URI;

/**
 * Base interface which all file downloader service should implement
 */
public interface FileDownloader
{
    File downloadFile(String path, URI fileUrl);
}
