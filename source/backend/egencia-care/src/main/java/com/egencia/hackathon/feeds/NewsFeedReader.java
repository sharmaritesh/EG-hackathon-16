package com.egencia.hackathon.feeds;

import com.buzzilla.webhose.client.WebhoseClient;
import com.buzzilla.webhose.client.WebhosePost;
import com.buzzilla.webhose.client.WebhoseQuery;
import com.buzzilla.webhose.client.WebhoseResponse;
import com.egencia.hackathon.model.Alert;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gurssingh on 12/12/16.
 */
@Component
public class NewsFeedReader {

    private WebhoseClient webhoseClient;
    private String WEBHOSE_API_KEY = "7c37002e-3491-4904-abc0-5e6db3236663";
    private static final List<String> alertSematicWords = Arrays.asList(new String []{"cyclone","threat", "blast", "quake", "earthquake"});

    public NewsFeedReader() {
        webhoseClient = new WebhoseClient(WEBHOSE_API_KEY);
    }

    //@Scheduled(fixedRate = 1000L)
    public void scheduleAlertNews() throws Exception {
        WebhoseQuery query = new WebhoseQuery();
        List<WebhoseQuery.SiteType> siteTypes = new ArrayList<>();
        siteTypes.add(WebhoseQuery.SiteType.news);
        query.siteTypes.addAll(siteTypes);
        query.language.add("english");
        WebhoseResponse response = webhoseClient.search(query);
        for (WebhosePost post : response.posts) {
            String title = post.title;
            if(title != null) {
                List<String> wordsInTitle= Arrays.asList(title.split("\\s+"));
                for(String alertingWord: alertSematicWords) {
                    if(wordsInTitle.contains(alertingWord)) {
                        //pushAlertToQueue();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        NewsFeedReader newsFeedReader= new NewsFeedReader();
        newsFeedReader.scheduleAlertNews();
    }


}
