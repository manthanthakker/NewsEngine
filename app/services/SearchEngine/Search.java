package services.SearchEngine;

import domain.SearchResponse;

/**
 * Created by Manthan_personal on 7/30/17.
 */
public interface Search {
    public SearchResponse search(String terms,String indexLocation);
}
