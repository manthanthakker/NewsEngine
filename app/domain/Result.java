package domain;



import java.net.URI;
import java.util.Date;


public class Result {

    private String title;
    private String description;
    private String publishedDate;
    private URI link;
    private Date dateToSort;

    @Override
    public String toString() {
        return "Result{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publishedDate=" + publishedDate +
                ", link=" + link +
                '}';
    }

    public Date getDateToSort() {
        return dateToSort;
    }

    public void setDateToSort(Date dateToSort) {
        this.dateToSort = dateToSort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public URI getLink() {
        return link;
    }

    public void setLink(URI link) {
        this.link = link;
    }





}
