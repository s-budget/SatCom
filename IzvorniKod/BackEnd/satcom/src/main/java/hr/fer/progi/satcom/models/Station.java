package hr.fer.progi.satcom.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

/**
 * Station of SatNogs application
 * Station is uniquely identified by internal system ID.
 * @author satcomBackend
 * */

@Entity
@Table(name="stations")
public class Station {

    @Id
    @Column(unique = true)
    private Long statId;

    private double longitude;

    private double latitude;

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    private int altitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return statId.equals(station.statId);
    }

    public boolean equals2(Station s)
    {
        return Double.compare(s.longitude, longitude) == 0 && Double.compare(s.latitude, latitude) == 0 && altitude == s.altitude  && Objects.equals(statId, s.statId) && Objects.equals(statName, s.statName) ;

    }

    @Override
    public int hashCode() {
        return Objects.hash(statId);
    }

    @Column
    @NotBlank
    private String statName;

    private int successRate;

    public void setAntennas(Set<Antenna> antennas) {
        this.antennas = antennas;
    }

    public Set<Antenna> getAntennas() {
        return antennas;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "stat_antennas", joinColumns = @JoinColumn(name = "statId"), inverseJoinColumns = @JoinColumn(name = "antennaId"))
    private Set<Antenna> antennas;
    /*@PreRemove
    private void preRemove() {
        for(Antenna a : antennas) {
            a.setStations_antennas(null);
        }
    }*/

    public Station() {
        super();
    }

    public Station(Long statId, double longitude, double latitude, String statName, int successRate, int altitude) {
        this.statId = statId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.statName = statName;
        this.successRate = successRate;
        this.altitude = altitude;
    }

    public Long getStatId() {
        return statId;
    }

    public void setStatId(Long statId) {
        this.statId = statId;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getStatName() {
        return statName;
    }

    public int getSuccessRate() {
        return successRate;
    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }


    @Override
    public String toString() {
        return "Station{" +
                "statId=" + statId +
                ", longitude = " + longitude +
                ", lattitude = " + latitude +
                ", statName = '" + statName + '\'' +
                ", successRate = " + successRate +
                ", altitude = " + altitude + '\''+
                '}';
    }


}
