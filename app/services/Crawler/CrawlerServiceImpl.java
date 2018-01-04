package services.Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.Play;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class CrawlerServiceImpl implements Crawler {


    public static void main(String[] args) {

        try {
            File f = new File("input.txt");
            ArrayList<String> links = new ArrayList<>();
            Scanner fr = new Scanner(f);
            int count = 0;
            while (fr.hasNext()) {

                String link = fr.next();
                System.out.println("Crawling "+link);
                links.add(link);
                Crawler c = new CrawlerServiceImpl();
                count=c.crawler(link,args[0],count);

            }

        } catch (Exception e) {
            System.out.println("Exception is " + e);
        }
    }

    public static void main2(String argsp[]){
        try {
            Document doc = Jsoup.connect("https://www.upi.com/rss/").get();
            Elements rss1 = doc.getElementsByClass("blu_l");

            for (Element element : rss1) {

                System.out.println(element.attr("href"));
            }

        }
        catch (Exception e){

        }



    }
    public int crawler(String linkToCrawl,String dataPath,int count) {
        try {
            Document doc = Jsoup.connect(linkToCrawl).get();
            Elements rss1 = doc.getElementsByTag("item");

            for (Element element : rss1) {
                count++;
                String title = parseCdata(element.getElementsByTag("title").html());
                String date = parseCdata(element.getElementsByTag("pubDate").html());
                String link = parseCdata(element.getElementsByTag("link").html());

                File file = new File(dataPath+"/DOC-" + count + ".txt");
                FileWriter fw = new FileWriter(file);
                String description = parseCdata(element.getElementsByTag("description").html());
                //System.out.println("Title: " + title + "description " + description + " \n date " + date + "\nLink:" + link);
                fw.write("<title>" + title + "</title>\n");
                fw.write("<publishedDate>" + date + "</publishedDate>\n");
                fw.write("<link>" + link + "</link>\n");
                fw.write("<description>" + description + "</description>\n");
                System.out.println(count);

                fw.flush();
                fw.close();
            }
        } catch (Exception e) {
            System.out.println("Exception " + e);
            return count;
        }
        return count;
    }

    public static String parseCdata(String sentence) {
        sentence = sentence.replace("<!--// <![CDATA[", "");
        sentence = sentence.replace("// ]]> -->", "");
        return sentence;
    }
}
