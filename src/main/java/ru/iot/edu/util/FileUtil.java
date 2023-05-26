package ru.iot.edu.util;

import ru.iot.edu.model.StudentTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.iot.edu.controller.api.StudentTaskController.getUploadedDirPath;

public class FileUtil {
    private FileUtil() {
    }

    public static String getUploadedPath(StudentTask studentTask) {
        return getUploadedFile(studentTask).getAbsolutePath();
    }

    public static File getUploadedFile(StudentTask studentTask) {
        return new File(
                Path.of(getUploadedDirPath(), studentTask.getFilePath() + ".ino").toUri()
        );
    }

    public static String getUploadedFileContent(StudentTask studentTask) {
        try {
            return new String(Files.readAllBytes(Path.of(getUploadedPath(studentTask))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
