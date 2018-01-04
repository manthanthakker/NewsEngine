package services.Indexer;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexerServiceImpl implements Indexer {

    private static Analyzer analyzer = new StandardAnalyzer();
    private static final Analyzer sAnalyzer = new SimpleAnalyzer();

    private IndexWriter writer;
    private ArrayList<File> queue = new ArrayList<File>();

    IndexerServiceImpl(String indexDir) throws IOException {

        FSDirectory dir = FSDirectory.open(new File(indexDir).toPath());

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        writer = new IndexWriter(dir, config);
    }

    public static void main(String[] args) throws IOException {
        System.out
                .println("Enter the FULL path where the index will be created: (e.g. /Usr/index or c:\\temp\\index)");

        String indexPath = args[1];
        System.out.println("indexLocation:" + indexPath);
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String indexPath = br.readLine();


        IndexerServiceImpl indexer = null;
        try {
            indexer = new IndexerServiceImpl(indexPath);
        } catch (Exception ex) {
            System.out.println("Cannot create index..." + ex.getMessage());
            System.exit(-1);
        }

        // ===================================================
        // read input from user until he enters q for quit
        // ===================================================
        //while (!indexPath.equalsIgnoreCase("q")) {
        try {
            System.out
                    .println("Enter the FULL path to add into the index (q=quit): (e.g. /home/mydir/docs or c:\\Users\\mydir\\docs)");
            System.out
                    .println("[Acceptable file types: .xml, .html, .html, .txt]");
            String dataPath = args[0];
            System.out.println("data from path added:" + dataPath);

//                if (indexPath.equalsIgnoreCase("q")) {
//                   break;
//                }

            // try to add file into the index
            indexer.indexFileOrDirectory(dataPath);

        } catch (Exception e) {
            System.out.println("Error indexing " + " : "
                    + e.getMessage());
        }
        //}

        // ===================================================
        // after adding, we always have to call the
        // closeIndex, otherwise the index is not created
        // ===================================================
        indexer.closeIndex();


    }


    public void indexFileOrDirectory(String fileName) throws IOException {
        // ===================================================
        // gets the list of files in a folder (if user has submitted
        // the name of a folder) or gets a single file name (is user
        // has submitted only the file name)
        // ===================================================
        addFiles(new File(fileName));

        int originalNumDocs = writer.numDocs();
        for (File f : queue) {
            FileReader fr = null;
            try {
                Document doc = new Document();

                // ===================================================
                // add contents of file
                // ===================================================
                fr = new FileReader(f);
                doc.add(new TextField("contents", fr));
                doc.add(new StringField("path", f.getPath(), Field.Store.YES));
                doc.add(new StringField("filename", f.getName(),
                        Field.Store.YES));

                writer.addDocument(doc);
                System.out.println("Added: " + f);
            } catch (Exception e) {
                System.out.println("Could not add: " + f);
            } finally {
                fr.close();
            }
        }

        int newNumDocs = writer.numDocs();
        System.out.println("");
        System.out.println("************************");
        System.out
                .println((newNumDocs - originalNumDocs) + " documents added.");
        System.out.println("************************");

        queue.clear();
    }

    private void addFiles(File file) {

        if (!file.exists()) {
            System.out.println(file + " does not exist.");
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                addFiles(f);
            }
        } else {
            String filename = file.getName().toLowerCase();
            // ===================================================
            // Only index text files
            // ===================================================
            if (filename.endsWith(".htm") || filename.endsWith(".html")
                    || filename.endsWith(".xml") || filename.endsWith(".txt")) {
                queue.add(file);
            } else {
                System.out.println("Skipped " + filename);
            }
        }
    }

    /**
     * Close the index.
     *
     * @throws IOException when exception closing
     */
    public void closeIndex() throws IOException {
        writer.close();
    }
//    public static HashMap<String,String> get_test_queries()
//    {
//        HashMap test_queries=null;
//
//        try {
//            FileInputStream fileIn = new FileInputStream("query-test.ser");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            test_queries = (HashMap<String,String>) in.readObject();
//            in.close();
//            fileIn.close();
//        }catch(IOException i) {
//            i.printStackTrace();
//
//        }catch(ClassNotFoundException c) {
//            System.out.println("Employee class not found");
//            c.printStackTrace();
//
//        }
//        return test_queries;
//    }


//     public static ArrayList<String> search(String s,String indexLocation)
//    {
//        try {
//            int Q_ID = 1;
//            // =========================================================
//            // Now search
//            // =========================================================
//            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()));
//            IndexSearcher searcher = new IndexSearcher(reader);
//            //TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);
//
//            //run_test_queries(get_test_queries(),indexLocation);
//
//            while (!s.equalsIgnoreCase("q")) {
//                try {
//                    System.out.print("Enter the search query (q=quit):");
//
//
//                    Query q = new QueryParser("contents", analyzer).parse(s);
//                    //searcher.search(q, collector);
//                    //ScoreDoc[] hits = collector.topDocs().scoreDocs;
//                    TopDocs docs = searcher.search(q, 100);
//                    ScoreDoc[] hits = docs.scoreDocs;
//                    // 4. display results
//                    System.out.println("Found " + hits.length + " hits.");
//                    for (int i = 0; i < hits.length; ++i) {
//                        int docId = hits[i].doc;
//                        Document d = searcher.doc(docId);
//                        //  System.out.println((i + 1) + ". " + d.get("path")
//                        //	    + " score=" + hits[i].score);
//                        System.out.println(" Q" + Q_ID + " " + d.get("path").substring(33) + " " + (i + 1) + " " + hits[i].score + " GOOGLE_SERVER_#234");
//                    }
//                    // 5. term stats --> watch out for which "version" of the term
//                    // must be checked here instead!
//                    Term termInstance = new Term("contents", s);
////                long termFreq = reader.totalTermFreq(termInstance);
////                long docCount = reader.docFreq(termInstance);
//                    //System.out.println(s + " Term Frequency " + termFreq
//                    //	+ " - Document Frequency " + docCount);
//                    Q_ID++;
//                } catch (Exception e) {
//                    System.out.println("Error searching " + s + " : "
//                            + e.getMessage());
//                    break;
//                }
//
//            }
//        }catch(Exception e)
//        {
//            System.out.println("There was an issue in searching throught the index"+e);
//
//        }
//        return new ArrayList<>();
//    }

    //    public static void run_test_queries(HashMap<String,String> test_queries, String indexLocation) throws IOException
//    {
//        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation).toPath()));
//        IndexSearcher searcher = new IndexSearcher(reader);
//        for(String q_id:test_queries.keySet())
//        {
//            File f=new File(q_id+"-results.txt");
//            FileWriter fw=new FileWriter(f);
//            try {
//                String s=test_queries.get(q_id);
//
//                Query q = new QueryParser( "contents", analyzer).parse(s);
//                //searcher.search(q, collector);
//                //ScoreDoc[] hits = collector.topDocs().scoreDocs;
//                TopDocs docs=searcher.search(q,100);
//                ScoreDoc[] hits=docs.scoreDocs;
//                // 4. display results
//                System.out.println("Found " + hits.length + " hits.");
//                String results="";
//                for (int i = 0; i < hits.length; ++i) {
//                    int docId = hits[i].doc;
//                    Document d = searcher.doc(docId);
//                    //  System.out.println((i + 1) + ". " + d.get("path")
//                    //	    + " score=" + hits[i].score);
//                    // System.out.println(" Q"+Q_ID+" "+d.get("path").substring(33)+" "+(i+1)+" "+hits[i].score+" GOOGLE_SERVER_#234");
//                    results+=" Q"+q_id+" "+d.get("path").substring(33)+" "+(i+1)+" "+hits[i].score+" GOOGLE_SERVER_#234\n";
//                }
//                // 5. term stats --> watch out for which "version" of the term
//                // must be checked here instead!
//                Term termInstance = new Term("contents", s);
////                long termFreq = reader.totalTermFreq(termInstance);
////                long docCount = reader.docFreq(termInstance);
//                //System.out.println(s + " Term Frequency " + termFreq
//                //	+ " - Document Frequency " + docCount);
//                fw.write(results);
//                fw.close();
//            } catch (Exception e) {
//                System.out.println("Error searching " + test_queries.get(q_id) + " : "
//                        + e.getMessage());
//                break;
//            }
//
//
//
//        }
//    }
}




