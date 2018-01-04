package services.SearchEngine;

import domain.SearchResponse;


public interface Search {
    public SearchResponse search(String terms,String indexLocation);
}
