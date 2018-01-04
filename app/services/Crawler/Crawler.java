package services.Crawler;

/**
 * Created by Manthan_personal on 7/29/17.
 */
public interface Crawler {
    // Input: The link to be crawled
    // ACTION: Creates a file with name as "Doc-count-ID" with 4 different  tags in it.
    // <title>,<publicationDate>, <description>,<link>
    // NO crawling the links or any fancy stuff going around
    // Library: Jsoup used. (Harddoded parser)
    public int crawler(final String link,String dataPath,int count);
}
