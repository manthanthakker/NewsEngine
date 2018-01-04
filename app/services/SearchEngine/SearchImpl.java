package services.SearchEngine;

import domain.Result;
import domain.SearchResponse;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import play.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.net.URI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by Manthan_personal on 7/30/17.
 */
public class SearchImpl implements Search {

    private static Analyzer analyzer = new StandardAnalyzer();
    private static final Analyzer sAnalyzer = new SimpleAnalyzer();

    @Override
    public SearchResponse search(String terms, String indexLocation) {
        terms = terms+"~0.1";
        try {
            int Q_ID = 1;
            // =========================================================
            // Now search
            // =========================================================
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()));
            IndexSearcher searcher = new IndexSearcher(reader);
            //TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);

            //run_test_queries(get_test_queries(),indexLocation);

            System.out.print("Enter the search query (q=quit):");

            Query q = new QueryParser("contents", analyzer).parse(terms);
            //searcher.search(q, collector);
            //ScoreDoc[] hits = collector.topDocs().scoreDocs;
            TopDocs docs = searcher.search(q, 100);
            ScoreDoc[] hits = docs.scoreDocs;
            // 4. display results
            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                //  System.out.println((i + 1) + ". " + d.get("path")
                //	    + " score=" + hits[i].score);

                System.out.println(" Q" + Q_ID + " " + d.get("path").substring(33) + " " + (i + 1) + " " + hits[i].score + " GOOGLE_SERVER_#234");
            }
            // 5. term stats --> watch out for which "version" of the term
            // must be checked here instead!
            Term termInstance = new Term("contents", terms);
//                  long termFreq = reader.totalTermFreq(termInstance);
//                  long docCount = reader.docFreq(termInstance);
            //System.out.println(s + " Term Frequency " + termFreq
            //	+ " - Document Frequency " + docCount);
            Q_ID++;
        } catch (Exception e) {
            System.out.println("Error searching " + terms + " : " + e.getMessage());

        }
        return null;
    }

    public static void main(String argsp[]) {
        String terms;
        String indexLocation = "/Users/Manthan_personal/Desktop/Desktop/play-java-starter-example/index";
        terms = "Indian Space Research Organisation~";
        try {
            int Q_ID = 1;
            // =========================================================
            // Now search
            // =========================================================
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()));
            IndexSearcher searcher = new IndexSearcher(reader);
            //TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);

            //run_test_queries(get_test_queries(),indexLocation);

            System.out.print("Enter the search query (q=quit):");

            Query q = new QueryParser("contents", analyzer).parse(terms);
            //searcher.search(q, collector);
            //ScoreDoc[] hits = collector.topDocs().scoreDocs;
            TopDocs docs = searcher.search(q, 100);
            ScoreDoc[] hits = docs.scoreDocs;
            // 4. display results
            System.out.println("Found " + hits.length + " hits.");
            ArrayList<Result> results = new ArrayList<>();
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                //  System.out.println((i + 1) + ". " + d.get("path")
                //	    + " score=" + hits[i].score);

                File f = new File(d.get("path"));
                Scanner br = new Scanner(f);
                String title = br.nextLine().replaceAll("<[^>]*>", "").replaceAll("</[^>]*>", "");
                String publishedDate = br.nextLine().replaceAll("<[^>]*>", "").replaceAll("</[^>]*>", "");
                String link = br.nextLine().replaceAll("<[^>]*>", "").replaceAll("</[^>]*>", "");
                String description = br.nextLine().replaceAll("<[^>]*>", "").replaceAll("</[^>]*>", "");
                Result rs = new Result();
                rs.setTitle(title);
                URI uri = new URI(link);
                rs.setLink(uri);
                rs.setDescription(description);
                rs.setPublishedDate(publishedDate);
                results.add(rs);
                System.out.println(title + "Q" + Q_ID + " " + d.get("path").substring(33) + " " + (i + 1) + " " + hits[i].score + " GOOGLE_SERVER_#234");
            }
            // 5. term stats --> watch out for which "version" of the term
            // must be checked here instead!
            Term termInstance = new Term("contents", terms);
//                  long termFreq = reader.totalTermFreq(termInstance);
//                  long docCount = reader.docFreq(termInstance);
            //System.out.println(s + " Term Frequency " + termFreq
            //	+ " - Document Frequency " + docCount);
            Q_ID++;
        } catch (Exception e) {
            System.out.println("Error searching " + terms + " : " + e.getMessage());

        }

    }


    public static CompletionStage<SearchResponse> searchQuery(String terms, String indexLocation) {
        final String queryTerm=terms+"~0.2";
        final CompletionStage<SearchResponse> finalCSResult = CompletableFuture.supplyAsync(() -> {

            SearchResponse searchResponse = new SearchResponse();
            try {
                int Q_ID = 1;
                // =========================================================
                // Now search
                // =========================================================
                IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()));
                IndexSearcher searcher = new IndexSearcher(reader);
                //TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);

                //run_test_queries(get_test_queries(),indexLocation);

                System.out.print("Enter the search query (q=quit):");

                Query q = new QueryParser("contents", analyzer).parse(queryTerm);
                //searcher.search(q, collector);
                //ScoreDoc[] hits = collector.topDocs().scoreDocs;
                TopDocs docs = searcher.search(q, 100);
                ScoreDoc[] hits = docs.scoreDocs;
                // 4. display results
                System.out.println("Found " + hits.length + " hits.");
                ArrayList<Result> results = new ArrayList<>();
                for (int i = 0; i < hits.length; ++i) {
                    int docId = hits[i].doc;
                    Document d = searcher.doc(docId);
                    //  System.out.println((i + 1) + ". " + d.get("path")
                    //	    + " score=" + hits[i].score);

                    File f = new File(d.get("path"));
                    Scanner br = new Scanner(f);
                    String title = br.nextLine().replaceAll("<[^>]*>", "").replaceAll("</[^>]*>", "");
                    String publishedDate = br.nextLine().replaceAll("<[^>]*>", "").replaceAll("</[^>]*>", "");
                    String link = br.nextLine().replaceAll("<[^>]*>", "").replaceAll("</[^>]*>", "");
                    String description = br.nextLine().replaceAll("<[^>]*>", "").replaceAll("</[^>]*>", "");
                    Result rs = new Result();
                    rs.setTitle(title);
                    URI uri = new URI(link);
                    rs.setLink(uri);
                    rs.setDescription(description);
                    rs.setPublishedDate(publishedDate);
                    String dateFormat[]=publishedDate.split(" ");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                    try {
                        String dateToSortString = dateFormat[1] + " "+dateFormat[2] +" "+ dateFormat[3];
                        System.out.println(dateToSortString);
                        Date dateToSort = formatter.parse(dateToSortString);
                        rs.setDateToSort(dateToSort);
                        results.add(rs);
//                        Collections.sort((ArrayList) results);
//                        results.add(rs);
                    }catch (Exception e){
                        Logger.error(" error in decoding for :" ,e);
                    }
                    System.out.println(title + "Q" + Q_ID + " " + d.get("path").substring(33) + " " + (i + 1) + " " + hits[i].score + " GOOGLE_SERVER_#234");
                }


                // 5. term stats --> watch out for which "version" of the term
                // must be checked here instead!
                Term termInstance = new Term("contents", terms);
                searchResponse.setResults(results);
//                  long termFreq = reader.totalTermFreq(termInstance);
//                  long docCount = reader.docFreq(termInstance);
                //System.out.println(s + " Term Frequency " + termFreq
                //	+ " - Document Frequency " + docCount);
                Q_ID++;

            } catch (Exception e) {
                System.out.println("Error in Searching " + e);
            }
            return searchResponse;
        });
        return finalCSResult;
    }

}
