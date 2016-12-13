package com.egencia.hackathon.feeds;

import com.buzzilla.webhose.client.WebhoseClient;
import com.buzzilla.webhose.client.WebhosePost;
import com.buzzilla.webhose.client.WebhoseQuery;
import com.buzzilla.webhose.client.WebhoseResponse;
import com.egencia.hackathon.event.EventManager;
import com.egencia.hackathon.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by gurssingh on 12/12/16.
 */
@Component
public class NewsFeedReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsFeedReader.class);

    private WebhoseClient webhoseClient;
    private String WEBHOSE_API_KEY = "7c37002e-3491-4904-abc0-5e6db3236663";
    private static final List<String> alertSematicWords = Arrays.asList(new String []{"cyclone","threat", "blast", "quake", "earthquake"});
    private static final List<String> mockedCities = Arrays.asList(new String []{"Gurgaon","Paris", "Chennai", "London", "NewYork"});
    private static final Map<String, String> cityToCountryMap = new HashMap<>();
    static {
        cityToCountryMap.put("Gurgaon", "India");
        cityToCountryMap.put("Paris","France");
        cityToCountryMap.put("Chennai", "India");
        cityToCountryMap.put("London","England");
        cityToCountryMap.put("NewYork","USA");
    }

    private EventManager eventManager;

    @Autowired
    public NewsFeedReader(EventManager eventManager) {
        webhoseClient = new WebhoseClient(WEBHOSE_API_KEY);
        this.eventManager = eventManager;
    }

    @Scheduled(fixedRate = 180000L, initialDelay = 30000L)
    public void scheduleAlertNews() throws Exception {
        WebhoseQuery query = new WebhoseQuery();
        List<WebhoseQuery.SiteType> siteTypes = new ArrayList<>();
        siteTypes.add(WebhoseQuery.SiteType.news);
        query.siteTypes.addAll(siteTypes);
        query.language.add("english");
        WebhoseResponse response = webhoseClient.search(query);
        LOGGER.info("************** Started news crawling across the world ****************\n");
        int count = 1;
        boolean oneNewsMatched = false;
        for (WebhosePost post : response.posts) {
            String title = post.title;
            boolean hasCrisis = false;
            if(title != null && !title.equals("")) {
                LOGGER.info(count + " : " + title);
                if(title != null) {
                    List<String> wordsInTitle= Arrays.asList(title.split("\\s+"));
                    for(String word : wordsInTitle) {
                        if(alertSematicWords.contains(word)) {
                            hasCrisis = true;
                        }
                    }
                    boolean cityMatched = false;
                    if(hasCrisis) {
                        for(String word : wordsInTitle) {
                            for(String city : mockedCities) {
                                if (word != null && word.equalsIgnoreCase(city)) {
                                    String crisisCity = city;
                                    cityMatched = true;
                                    Alert alert = new Alert();
                                    alert.setCity(crisisCity);
                                    alert.setCountry(cityToCountryMap.get(crisisCity));
                                    alert.setDescription(title);
                                    eventManager.append(alert);
                                    oneNewsMatched = true;
                                    break;
                                }
                            }
                            if(cityMatched) {
                                break;
                            }
                        }
                    }
                }
                count++;
            }
        }
        if(!oneNewsMatched) {
            LOGGER.info("101: Earth Quake in Gurgaon, India");
            Alert alert = new Alert();
            alert.setCity("Gurgaon");
            alert.setCountry("India");
            alert.setDescription("Earth Quake in Gurgaon, India");
            eventManager.append(alert);
        }
        LOGGER.info("************** Completed news ticker, will start after 30 secs. ****************\n");
    }
}
