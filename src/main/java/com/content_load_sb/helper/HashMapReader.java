package com.content_load_sb.helper;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dto.HashPath;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by asd on 16.11.2017.
 */
public class HashMapReader {
    private static HashMapReader instance;
    private List<HashPath> lstHashPath;

    private HashMapReader() {
        setLstHashPath();
    }

    public static HashMapReader getInstance() {
        if (instance == null) {
            instance = new HashMapReader();
        }
        return instance;
    }

    public List<HashPath> getLstHashPath() {
        return lstHashPath;
    }

    private void setLstHashPath() {
        lstHashPath = Helper.getInstance().jsonToData(HashPath.class,
                Helper.getInstance().readFileJson(Setting.getInstance().getSsdSourceFolderJson()));
    }

    public String getCalculatedHashByPathTail(String pathTail) {
        List<HashPath> searchedList = lstHashPath.stream()
                .filter(hashPath -> hashPath.getPathTail().equals(pathTail))
                .collect(Collectors.toList());
        if (searchedList.size() == 0) {
            return "";
        }
        return searchedList.get(0).getHash();
    }
}