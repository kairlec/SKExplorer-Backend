package com.kairlec.pojo.Json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FileList {
    @JSONField(name = "isRoot")
    private boolean isRoot;

    @JSONField(name = "local")
    private String localPath;

    @JSONField(name = "items")
    private List<FileInfo> items;

    private void sortItems() {
        items.sort((file1, file2) -> {
            if (file1 == null) {
                return -1;
            }
            if (file2 == null) {
                return 1;
            }
            if (file1.getType() == null || file2.getType() == null) {
                return file1.getName().compareTo(file2.getName());
            }
            if (file1.getType().equals("folder") && !file2.getType().equals("folder")) {
                return -1;
            }
            if (file2.getType().equals("folder") && !file1.getType().equals("folder")) {
                return 1;
            }
            return file1.getName().compareTo(file2.getName());
        });
    }

    public FileList(boolean isRoot, List<FileInfo> items) {
        this.isRoot = isRoot;
        setItems(items);
    }

    public void setItems(List<FileInfo> items) {
        this.items = items;
        sortItems();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}
