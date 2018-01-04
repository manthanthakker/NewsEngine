package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.mvc.*;

import services.CrawlAndIndex.CrawlAndIndexServiceImpl;
import services.SearchEngine.SearchImpl;
import views.html.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    public String prod="true";

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    public Result index() {
        return ok(index.render("Your new application is <b> NOT </b>ready."));
    }

    public Result homepage() {

        return ok(temp.render("<b>Hellow World</b>"));
    }

    public Result news() {
        String bootstrapPage = "assets/javascripts/news/newsBootstrap";
        return ok(temp.render(bootstrapPage));
    }

    public Result resume() {
        String bootstrapPage = "assets/javascripts/resume/resumeBootstrap";
        return ok(temp.render(bootstrapPage));
    }

    public Result login() {
        String bootstrapPage = "assets/javascripts/login/loginBootstrap";
        return ok(temp.render(bootstrapPage));
    }

    public CompletionStage<Result> search(String query) {

        String indexLocation="/Users/Manthan_personal/Desktop/aws/newsEngine/projects/index";
        if(prod.equals("true")){
            indexLocation="/home/ec2-user/server/projectFromServer/projects/index";
        }
        return SearchImpl.searchQuery(query,indexLocation ).thenApply(resp -> {
            JsonNode jsonNode;
            try {
                String json = mapper.writeValueAsString(resp);
                jsonNode = mapper.readTree(json);
                return ok(jsonNode);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return internalServerError();

        });

    }

    public CompletionStage<Result> access() {
        System.out.println("Access");
        JsonNode credentials = request().body().asJson();
        boolean result = credentials.get("password").equals("1234");
        System.out.println(credentials.get("password").equals("1234"));
        if (credentials.get("password").toString().replaceAll("\"", "").equals("1234")) {
            return CompletableFuture.completedFuture(Results.ok("token"));
        }
        return CompletableFuture.completedFuture(Results.badRequest("Not Successful"));

    }

    public Result fetchLatestNews(String token) {


        String bootstrapPage = "assets/javascripts/fetchLatestNews/fetchLatestNewsBootstrap";
        if (token.replaceAll("\"", "").equals("token"))
            return ok(temp.render(bootstrapPage));
        else
            return badRequest("incorrectToken");

    }

    public CompletionStage<Result> CrawlAndIndex(String token) {
        return CrawlAndIndexServiceImpl.crawlAndIndex(prod).thenApply(resp -> {
            if(!token.equals("1234"))
                return badRequest("Token Invalid");
            JsonNode jsonNode;
            try {
                String json = mapper.writeValueAsString(resp);
                jsonNode = mapper.readTree(json);
                return ok(jsonNode);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return internalServerError();

        });

    }


}
