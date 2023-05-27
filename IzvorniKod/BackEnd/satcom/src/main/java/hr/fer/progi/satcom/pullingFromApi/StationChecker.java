package hr.fer.progi.satcom.pullingFromApi;

import hr.fer.progi.satcom.models.Antenna;
import hr.fer.progi.satcom.models.Station;
import hr.fer.progi.satcom.services.impl.StationServiceImpl;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@CrossOrigin(origins = "*")
public class StationChecker {
    static StationServiceImpl stationService;
    @Autowired
    public void setStationService(StationServiceImpl stationService) {
        StationChecker.stationService = stationService;
    }

    public static boolean checkStations(Long station_id) {
        String urlOfStations = "https://network.satnogs.org/api/";

        urlOfStations += "stations/";
        urlOfStations += station_id;
        HttpGet request = new HttpGet(urlOfStations);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response;
        String result;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            client.close();
            response.close();
            if (response.getStatusLine().getStatusCode() == 404) {
                return false;
            }
        } catch (IOException e) {
            return true;
        }


        Pattern pattern = Pattern.compile("\\{.*?\\[.*?].*?}");
        Matcher matcher = pattern.matcher(result);

        while (matcher.find()) {
            if (matcher.start() == matcher.end()) {
                continue;
            }
            //System.out.println(matcher.group());
            ImmutablePair<Station, LinkedList<Antenna>> stationData = formatStation(matcher.group());
            if (stationData == null) {
                return returnFalse(station_id);
            }
            Station st = stationData.left;
            LinkedList<Antenna> an = stationData.right;
            //StationService stationService = new StationServiceImpl();
            if (st != null) {

                   /* System.out.println(st);
                    for (Antenna j : an) {
                        System.out.println(j);
                    }*/
                //System.out.println("--------------------");
                Comparator<Antenna> customComparator = Comparator.comparing(Antenna::getAntennaFreqLow).thenComparing(Antenna::getAntennaFreqHigh).thenComparing(Antenna::getAntennaType);
                Set<Antenna> old = new TreeSet<>(customComparator);
                old.addAll(stationService.fetch(station_id).getAntennas());
                Set<Antenna> a = new TreeSet<>(customComparator);
                a.addAll(an);
                Antenna[] aArray = a.toArray(new Antenna[0]);
                Antenna[] oldArray = old.toArray(new Antenna[0]);


                boolean out;
                if (old.size() != a.size()) {
                    out = false;
                } else {
                    out = true;
                    for (int i = 0; i < a.size(); i++) {
                        if (!aArray[i].equals2(oldArray[i])) {
                            out = false;
                            break;
                        }
                    }
                }


                out = out && stationService.fetch(station_id).equals2(st);

                if (out) {
                    return true;
                } else {
                    return returnFalse(station_id);
                }

            } else {
                return returnFalse(station_id);
            }
        }
       return true;
    }

    private static boolean returnFalse(Long id) {
       /* try {
            new ScheduledTask().refreshStations();
        } catch (IOException e) {
            return true;
        }*/
        //stationService.delete(stationService.fetch(id));

        return false;
    }

    public static ImmutablePair<Station,LinkedList<Antenna>> formatStation(String stn) {
        String atribute_regex = "\"id\":(\\d*),\"name\":\"(.*?)\",\"altitude\":(\\d*),\"min_horizon\":.*?,\"lat\":(.*?),\"lng\":(.*?),.*?\"antenna\":\\[(.*?)],.*?,\"status\":\"(.*?)\",\"observations\":(\\d*)";
        String antennaRegex = "\\{.*?\"band\":\"([A-Z,\\s]*?)\",\"antenna_type\":\"(.*?)\",.*?}";

        Pattern pattern = Pattern.compile(atribute_regex);
        Matcher matcher = pattern.matcher(stn);
        if (matcher.find()) {
            if (matcher.start() != matcher.end()) {
                if (matcher.group(7).equals("Online")) {
                    Station st = new Station(Long.parseLong(matcher.group(1)), Double.parseDouble(matcher.group(5)), Double.parseDouble(matcher.group(4)), matcher.group(2), Integer.parseInt(matcher.group(8)), Integer.parseInt(matcher.group(3)));
                    LinkedList<Antenna> currentAntennas = new LinkedList<>();
                    //System.out.println(matcher.group(6));
                    Pattern inner_pattern = Pattern.compile(antennaRegex);
                    Matcher inner_matcher = inner_pattern.matcher(matcher.group(6));
                    while (inner_matcher.find()) {
                        if (inner_matcher.start() != inner_matcher.end()) {
                            //System.out.println(inner_matcher.group(1));
                            String[] bands=inner_matcher.group(1).split(", ");
                            for(int i=0;i< bands.length;i++)
                            {
                                String band=bands[i];
                                //System.out.println(band);
                                long freq_low;
                                long freq_high;
                                //za sad sam pisal sve u herzima
                                long modifier = 1;
                                if (Objects.equals(band, "ULF")) {
                                    freq_low = modifier * 300;
                                    freq_high = modifier * 3000;
                                } else if (Objects.equals(band, "VLF")) {
                                    freq_low = modifier * 3000;
                                    freq_high = modifier * 30000;
                                } else if (Objects.equals(band, "LF")) {
                                    freq_low = modifier * 30000;
                                    freq_high = 300000 * modifier;
                                } else if (Objects.equals(band, "MF")) {
                                    freq_low = 300000 * modifier;
                                    freq_high = 3000000 * modifier;
                                } else if (Objects.equals(band, "HF")) {
                                    freq_low = 3000000 * modifier;
                                    freq_high = 30000000 * modifier;
                                } else if (Objects.equals(band, "VHF")) {
                                    freq_low = 30000000 * modifier;
                                    freq_high = 300000000 * modifier;
                                } else if (Objects.equals(band, "UHF")) {
                                    freq_low = 300000000 * modifier;
                                    freq_high = 300000000 * modifier;
                                    freq_high *= 10;
                                } else if (Objects.equals(band, "L")) {
                                    freq_low = 100000000 * modifier;
                                    freq_low *= 10;
                                    freq_high = 200000000 * modifier;
                                    freq_high *= 10;
                                } else if (Objects.equals(band, "S")) {
                                    freq_low = 200000000 * modifier;
                                    freq_low *= 10;
                                    freq_high = 400000000 * modifier;
                                    freq_high *= 10;
                                } else if (Objects.equals(band, "C")) {
                                    freq_low = 400000000 * modifier;
                                    freq_low *= 10;
                                    freq_high = 800000000 * modifier;
                                    freq_high *= 10;
                                } else if (Objects.equals(band, "X")) {
                                    freq_low = 800000000 * modifier;
                                    freq_low *= 10;
                                    freq_high = 1200000000 * modifier;
                                    freq_high *= 10;
                                } else if (Objects.equals(band, "Ku")) {
                                    freq_low = 1200000000 * modifier;
                                    freq_low *= 10;
                                    freq_high = 1800000000 * modifier;
                                    freq_high *= 10;
                                } else if (Objects.equals(band, "K")) {
                                    freq_low = 1800000000 * modifier;
                                    freq_low *= 10;
                                    freq_high = 260000000 * modifier;
                                    freq_high *= 100;
                                } else if (Objects.equals(band,"Ka")) {
                                    freq_low = 260000000 * modifier;
                                    freq_low *= 100;
                                    freq_high = 400000000 * modifier;
                                    freq_high *= 100;
                                } else if (Objects.equals(band,"SHF")) {
                                    freq_low = 300000000 * modifier;
                                    freq_low *= 10;
                                    freq_high = 300000000 * modifier;
                                    freq_high *= 100;
                                } else if (Objects.equals(band, "EHF")) {
                                    freq_low = 300000000 * modifier;
                                    freq_low *= 100;
                                    freq_high = 300000000 * modifier;
                                    freq_high *= 1000;
                                } else {
                                    continue;
                                }


                                currentAntennas.add(new Antenna((long)-1,freq_high, freq_low, inner_matcher.group(2)));

                            }
                            
                        }
                    }


                    return new ImmutablePair<>(st,currentAntennas);
                }
            }
        }
        return null;
    }
}
