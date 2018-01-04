package domain;

import java.util.ArrayList;

/**
 * Created by Manthan_personal on 7/30/17.
 */
public class SearchResponse {
    private ArrayList<Result> results=new ArrayList<>();

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }
}
