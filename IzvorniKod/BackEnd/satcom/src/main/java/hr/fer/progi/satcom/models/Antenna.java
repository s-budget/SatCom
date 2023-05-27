package hr.fer.progi.satcom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name="antennas")
public class Antenna {

    @Id
    @Column(unique = true)
    private Long antennaId;
    @Column(name = "freq_high")
    private  Long antennaFreqHigh;
    @Column(name = "freq_low")
    private Long antennaFreqLow;
    @Column(name = "type")
    private String antennaType;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="linkAntenna",  joinColumns = @JoinColumn(name = "antennaId"),
          inverseJoinColumns = @JoinColumn(name = "linkId"))
    private Set<Link> linkAntenna;
    @ManyToMany(mappedBy = "antennas", fetch = FetchType.LAZY)
    private Set<Station> stations_antennas;
    @PreRemove
    private void preRemove() {
        for(Link l : linkAntenna) {
            l.setAntennas(new HashSet<>());
        }
        for(Station s:stations_antennas) {
            s.setAntennas(new HashSet<>());
        }
    }

    public void setStations_antennas(Station stations_antennas) {
        this.stations_antennas = new HashSet<>();
        this.stations_antennas.add(stations_antennas);
    }

    @JsonIgnore
    public Set<Station> getStations_antennasObj() {
        return stations_antennas;
    }

    public Set<String> getStations_antennas() {
        return stations_antennas.stream().map(t -> t.getStatName()).collect(Collectors.toSet());
    }




    public Antenna(){}

    public Antenna(Long id,Long antennaFreqHigh, Long antennaFreqLow, String antennaType) {
        this.antennaId=id;
        this.antennaFreqHigh = antennaFreqHigh;
        this.antennaFreqLow = antennaFreqLow;
        this.antennaType = antennaType;
    }

    public Long getAntennaId() {
        return antennaId;
    }

    public Long getAntennaFreqHigh() {
        return antennaFreqHigh;
    }

    public void setAntennaFreqHigh(Long  antennaFreqHigh) {
        this.antennaFreqHigh = antennaFreqHigh;
    }

    public Long getAntennaFreqLow() {
        return antennaFreqLow;
    }

    public void setAntennaId(long id)
    {
        this.antennaId=id;
    }

    public void setAntennaFreqLow(Long antennaFreqLow) {
        this.antennaFreqLow = antennaFreqLow;
    }

    public String getAntennaType() {
        return antennaType;
    }

    public void setAntennaType(String antennaType) {
        this.antennaType = antennaType;
    }

    @JsonIgnore
    public Set<Link> getLinkAntennaObj() {
        return linkAntenna;
    }

    public Set<String> getLinkAntenna() {
        return linkAntenna.stream().map(t -> t.getLinkMode()).collect(Collectors.toSet());
    }

    public void setLinkAntenna(Set<Link> linkAntenna) {
        this.linkAntenna = linkAntenna;
    }



    public boolean equals2(Antenna antenna) {
        return  Objects.equals(antennaFreqHigh, antenna.antennaFreqHigh) && Objects.equals(antennaFreqLow, antenna.antennaFreqLow) && Objects.equals(antennaType, antenna.antennaType) ;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Antenna antenna = (Antenna) o;
        return Objects.equals(antennaId, antenna.antennaId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(antennaId);
    }


    @Override
    public String toString() {
        return String.format("Antenna Type:%s\n Upper frequency bound:%s\n Lower frequency bound:%s", this.antennaType, this.antennaFreqHigh, this.antennaFreqLow);
    }
}