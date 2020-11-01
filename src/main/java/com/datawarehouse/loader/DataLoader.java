package com.datawarehouse.loader;

import com.datawarehouse.loader.exception.DataLoaderException;

/**
 * Data loader interface
 * Reads from file and loads data into database
 */
public interface DataLoader {

    /**
     * Loads data into database
     */
    void load() throws DataLoaderException;

    /**
     * Retrieves the data loader order (avoid integrity constraints violations)
     * @return a order number
     */
    Long getOrder();
}
