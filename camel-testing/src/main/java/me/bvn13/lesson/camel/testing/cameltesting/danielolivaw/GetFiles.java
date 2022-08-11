package me.bvn13.lesson.camel.testing.cameltesting.danielolivaw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetFiles {

    public GetFiles() {
        File parentPath = new File("Desktop/oldBackup");
        List<String> files = list(parentPath);
    }

    protected List<String> list(File parent) {
        return listFiles(parent, parent);
    }

    protected List<String> listFiles(File parent, File folder) {
        List<String> fileList = new ArrayList<String>();
        if (folder.isDirectory()) {

            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        fileList.addAll(listFiles(parent, file));
                    } else {
                        String path = file.getPath();
                        String offset = parent.getPath();

                        path = path.substring(offset.length());
                        fileList.add(path);
                    }
                }
            }
        }

        return fileList;
    }
}