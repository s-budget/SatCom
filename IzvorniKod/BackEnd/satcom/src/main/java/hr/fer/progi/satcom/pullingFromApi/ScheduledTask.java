package hr.fer.progi.satcom.pullingFromApi;

import hr.fer.progi.satcom.dao.AntennaRepository;
import hr.fer.progi.satcom.dao.LinkRepository;
import hr.fer.progi.satcom.dao.StationRepository;
import hr.fer.progi.satcom.models.Antenna;
import hr.fer.progi.satcom.models.Station;
import hr.fer.progi.satcom.services.impl.AntennaServiceImpl;
import hr.fer.progi.satcom.services.impl.StationServiceImpl;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@Configurable
public class ScheduledTask  {

    static StationRepository statRepo;

    static StationServiceImpl statService;
    static AntennaServiceImpl antennaService;

    static LinkRepository linkRepo;

    static AntennaRepository antennaRepo;
    @Autowired
    public void  setStatService(StationServiceImpl statService)
    {
        ScheduledTask.statService=statService;
    }
    @Autowired
    public void  setAntennaService(AntennaServiceImpl antennaService)
    {
        ScheduledTask.antennaService=antennaService;
    }

    @Autowired
    public void setStatRepo(StationRepository statRepo) {
        ScheduledTask.statRepo = statRepo;
    }

    @Autowired
    public void setLinkRepo(LinkRepository linkRepo) {
        ScheduledTask.linkRepo = linkRepo;
    }

    @Autowired
    public void setAntennaRepo(AntennaRepository antennaRepo) {
        ScheduledTask.antennaRepo = antennaRepo;
    }

    @Scheduled(cron="0 0 0 * * *", zone="Europe/Paris")//@Scheduled(cron="0 0 0 * * *", zone="Europe/Paris")
    public void refreshStations() throws IOException {
        String urlOfStations = "https://network.satnogs.org/api/stations/?client_version=&id=&name=&";
        ArrayList<Station> newStations = new ArrayList<>();
        boolean tableAlreadyCleared=false;
        long antennasCreated=0;
        for (int i = 1; true; i++) {
            urlOfStations += "page=";
            urlOfStations += i;
            urlOfStations += "&status=2";
            HttpGet request = new HttpGet(urlOfStations);
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            client.close();
            response.close();
            if (response.getStatusLine().getStatusCode()==404) {
                //System.out.println(response.getStatusLine());
                break;
            }

            Pattern pattern = Pattern.compile("\\{.*?\\[.*?].*?}");
            Matcher matcher = pattern.matcher(result);

            while (matcher.find()) {
                if (matcher.start() == matcher.end()) {
                    continue;
                }
                //System.out.println(matcher.group());
                if(!tableAlreadyCleared)
                {
                    statRepo.deleteAllInBatch();
                    antennaRepo.deleteAllInBatch();
                    tableAlreadyCleared=true;
                }

                ImmutablePair<Station,LinkedList<Antenna>> stationData= StationChecker.formatStation(matcher.group());
                assert stationData != null;
                Station st = stationData.left;
                LinkedList<Antenna> an=stationData.right;
                if (st != null) {
                    statService.forceSaveStation(st.getStatId(),st.getAltitude(),st.getLatitude(),st.getLongitude(),st.getStatName(),st.getSuccessRate());
                    statRepo.save(st);
                    System.out.println(st);
                    for (Antenna j : an) {
                        if(j!=null)
                        {
                            antennasCreated++;
                            j.setAntennaId(antennasCreated);
                            j.setStations_antennas(st);
                            antennaService.forceSaveStation(j.getAntennaId(),j.getAntennaFreqLow(),j.getAntennaFreqHigh(),j.getAntennaType());
                            antennaRepo.save(j);
                            if(linkRepo.findCompatibleLinksForAntenna(j.getAntennaFreqHigh(), j.getAntennaFreqLow()).isPresent())
                            {
                                j.setLinkAntenna(linkRepo.findCompatibleLinksForAntenna(j.getAntennaFreqHigh(), j.getAntennaFreqLow()).get());

                            }
                            //System.out.println(j);
                            antennaRepo.save(j);
                        }
                    }
                    st.setAntennas(new HashSet<>(an));
                    statRepo.save(st);
                    //System.out.println("--------------------");
                    newStations.add(st);
                }

            }

            urlOfStations = urlOfStations.split("page=")[0];

        }
        //linkRepo.delete(linkRepo.findByLinkMode("UHF1").get());
    }
}
