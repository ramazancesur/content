package com.content_load_sb.fileops;

import com.content_load_sb.helper.ApplicationContextHolder;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.service.DirectoryService;
import com.content_load_sb.service.FileService;
import com.content_load_sb.service.interfaces.IDirectoryService;
import com.content_load_sb.service.interfaces.IFileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

/**
 * Created by ramazancesur on 14.11.2017.
 */
public class CopyFileVisitor extends SimpleFileVisitor<Path> {
    private Path sourceDir;
    private Path targetDir;
    private boolean isCreateMode = false;

    private IDirectoryService directoryService;
    private IFileService fileService;

    public CopyFileVisitor(Path sourceDir, Path targetDir) {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;

        Properties properties = Helper.getInstance().getRestrictProp("application.properties", "hibernate.hbm2ddl.auto");
        if (properties.getProperty("hibernate.hbm2ddl.auto").contains("create")) {
            isCreateMode = true;
        }
        // ****** Manuel Servis çağırmak durumunda kaldım ********* //
        directoryService = ApplicationContextHolder.getContext().getBean(DirectoryService.class);
        fileService = ApplicationContextHolder.getContext().getBean(FileService.class);
    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attributes) {
        Path targetFile = targetDir.resolve(sourceDir.relativize(file));
        if (isCreateMode == true) {
            fileService.createFileData(targetFile);
        } else if (!Files.exists(targetFile)) {
            Helper.getInstance().fileCopy(new File(file.toUri()), new File(targetFile.toUri()));
            if (!isCreateMode) {
                fileService.createFileData(targetFile);
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir,
                                             BasicFileAttributes attributes) {
        try {
            Path newDir = targetDir.resolve(sourceDir.relativize(dir));
            if (isCreateMode == true) {
                directoryService.createDirectoryData(newDir, dir);
            } else if (!Files.exists(newDir)) {
                Files.createDirectory(newDir);
                if (!isCreateMode) {
                    directoryService.createDirectoryData(newDir, dir);
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }

        return FileVisitResult.CONTINUE;
    }


    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e)
            throws IOException {
        System.err.printf("Visiting failed for %s\n", file);
        return FileVisitResult.SKIP_SUBTREE;
    }
}