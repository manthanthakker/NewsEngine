package services.CrawlAndIndex;

/**
 * Created by Manthan_personal on 9/10/17.
 */



import org.apache.commons.io.FileUtils;
import play.Logger;
import services.Crawler.CrawlerServiceImpl;
import services.Indexer.IndexerServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CrawlAndIndexServiceImpl implements CrawlAndIndexService {
    CrawlAndIndexServiceImpl()
    {

    }
    public static CompletionStage<Boolean> crawlAndIndex(String prod){
        final CompletionStage<Boolean> finalCSResult = CompletableFuture.supplyAsync(() -> {
           // String prod= Play.application().configuration().getString("play.prod");

            String indexPath = "/Users/Manthan_personal/Desktop/aws/newsEngine/projects/index";
            String dataPath = "/Users/Manthan_personal/Desktop/aws/newsEngine/projects/data";
            if (prod.equals("true")) {
                indexPath = "/home/ec2-user/server/projectFromServer/projects/index";
                dataPath = "/home/ec2-user/server/projectFromServer/projects/data";
            }
            String inputPath[] = {dataPath, indexPath};

            File indexDir = new File(indexPath);
            File dataDir = new File(dataPath);
            try {
                FileUtils.deleteDirectory(indexDir);
                FileUtils.deleteDirectory(dataDir);
                indexDir.mkdir();
                dataDir.mkdir();
                indexDir.canWrite();
                dataDir.canWrite();
                indexDir.canRead();
                dataDir.canRead();
                CrawlerServiceImpl.main(inputPath);
                IndexerServiceImpl.main(inputPath);

                return true;

            } catch (IOException e) {
                Logger.error("Error in deleting files");
            }
            return false;

        });
        return finalCSResult;


    }
}
