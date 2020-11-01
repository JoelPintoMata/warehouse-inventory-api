package com.datawarehouse.loader;

import com.datawarehouse.loader.exception.DataLoaderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class DataLoaderManager {

    private List<DataLoader> dataLoaderList;

    @Autowired
    public DataLoaderManager() {
        this.dataLoaderList = new ArrayList<>(2);
    }

    public void load() throws DataLoaderException {
        dataLoaderList.sort(Comparator.comparing(DataLoader::getOrder));
        for(DataLoader dataLoader : dataLoaderList) {
            dataLoader.load();
        }
    }

    public List<DataLoader> getLoaders() {
        return this.dataLoaderList;
    }
}
