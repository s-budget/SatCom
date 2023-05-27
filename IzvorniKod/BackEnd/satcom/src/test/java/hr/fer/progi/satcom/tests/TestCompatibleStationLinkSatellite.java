package hr.fer.progi.satcom.tests;

import hr.fer.progi.satcom.dao.StationRepository;
import hr.fer.progi.satcom.models.*;
import hr.fer.progi.satcom.services.impl.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestCompatibleStationLinkSatellite {
    @Autowired
    private StationServiceImpl stationService;
    @Autowired
    private StationRepository statRepo;
    @Autowired
    private SatelliteServiceImpl satelliteService;
    @Autowired
    private LinkServiceImpl linkService;
    @Autowired
    private TransmitterServiceImpl transmitterService;
    @Autowired
    private AntennaServiceImpl antennaService;
    @Autowired
    private UserServiceImpl userService;

    private Pair createSatelliteAndTransmitter(Long f) {
        Satellite s = satelliteService.createNewSatellite(new Satellite("testSatellite123456", userService.fetch(4L)));
        Set<Transmitter> t = new HashSet<>();
        Transmitter tr = transmitterService.createNewTransmitter(new Transmitter("testTransmitter123456", "PSK", f, 100, s));
        t.add(tr);
        s.setTransmitters(t);
        return new Pair(s.getSatId(), tr.getTransmId());
    }

    private void deleteSatelliteAndTransmitter(Long satId, Long transmId) {
        transmitterService.deleteTransmitter(transmId);
        satelliteService.deleteSatellite(satId);
    }
    @Test
    @DisplayName("Testing when satellite has no compatible links.")
    public void testSatelliteLinkNotComp() {
        Pair ids = createSatelliteAndTransmitter(0L);
        assertTrue(transmitterService.findAllCompatibleLinks(satelliteService.fetch(ids.first).getTransmittersObj()).isEmpty());
        deleteSatelliteAndTransmitter(ids.first, ids.second);
    }

    @Test
    @DisplayName("Testing when satellite has compatible links.")
    public void testSatelliteLinkComp() {
        Pair ids = createSatelliteAndTransmitter(103L);
        Link l = linkService.createNewLink(new Link("PSK", 103L, 100));
        Set<Link> compLinks = transmitterService.findAllCompatibleLinks(satelliteService.fetch(ids.first).getTransmittersObj());
        assertTrue(compLinks.contains(l) && compLinks.size()==1);
        deleteSatelliteAndTransmitter(ids.first, ids.second);
        linkService.deleteLink(l.getLinkId());
    }

    private Pair createStationAndAntenna(Long f1, Long f2) {
        Station st = stationService.createNewStation(new Station(-2L, 100.0, 1009.0, "testStat", 19, 100));
        Set<Antenna> a = new HashSet<>();
        Antenna an = new Antenna(-2L, f1, f2, "TestType");
        an.setStations_antennas(st);
        antennaService.createNewAntenna(an);
        a.add(an);
        st.setAntennas(a);
        statRepo.save(st);
        return new Pair(st.getStatId(), an.getAntennaId());
    }

    private void deleteStationAndAntenna(Long statId, Long anntennId) {
       antennaService.deleteAntenna(anntennId);
       stationService.deleteStation(statId);
    }


    @Test
    @DisplayName("Testing when link has no compatible stations.")
    public void testLinkStationNotComp() {
        Link l = linkService.createNewLink(new Link("PSK", -100L, 100));
        assertTrue(antennaService.findAllCompatibleStations(l.getAntennasObj()).isEmpty());
        linkService.deleteLink(l.getLinkId());
    }

    @Test
    @DisplayName("Testing when link has compatible stations.")
    public void testLinkStationComp() {
        Pair ids = createStationAndAntenna(1000L, 10L);
        Link l = linkService.createNewLink(new Link("PSK", 122L, 100));
        Set<Station> compStat = antennaService.findAllCompatibleStations(l.getAntennasObj());
        for(Station st:compStat) {
            assertTrue(st.equals2(stationService.fetch(ids.first)));
        }
        linkService.deleteLink(l.getLinkId());
        deleteStationAndAntenna(ids.first, ids.second);
    }


    private static class Pair {
        private Long first;
        private Long second;

        public Pair(Long first, Long second) {
            this.first = first;
            this.second = second;
        }
    }

}
