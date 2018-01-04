package domain;

import java.util.ArrayList;

public class SearchResponse {
    private ArrayList<Result> results=new ArrayList<>();

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }
}
