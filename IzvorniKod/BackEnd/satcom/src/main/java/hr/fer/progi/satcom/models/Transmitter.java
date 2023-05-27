package hr.fer.progi.satcom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="transmitters")
public class Transmitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transmId;

    @NotBlank
    private String transmName;

    @NotBlank
    private String transmMode;

    @NotNull
    private Long transmFreq;

    @NotNull
    private Integer transmBaud;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "linkId", nullable = true)
    private Link link;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "satId", nullable = true)
    private Satellite owningSatellite;

    public Transmitter() {
        super();
    }

    public Transmitter(String transmName, String transmMode, Long transmFreq, Integer transmBaud, Satellite satellite) {
        this.transmName = transmName;
        this.transmMode = transmMode;
        this.transmFreq = transmFreq;
        this.transmBaud = transmBaud;
        this.owningSatellite = satellite;
    }

    public Long getTransmId() {
        return transmId;
    }

    public String getTransmName() {
        return transmName;
    }

    public void setTransmName(String transmName) {
        this.transmName = transmName;
    }

    public String getTransmMode() {
        return transmMode;
    }

    public void setTransmMode(String transmMode) {
        this.transmMode = transmMode;
    }

    public Long getTransmFreq() {
        return transmFreq;
    }

    public void setTransmFreq(Long transmFreq) {
        this.transmFreq = transmFreq;
    }

    public Integer getTransmBaud() {
        return transmBaud;
    }

    public void setTransmBaud(Integer transmBaud) {
        this.transmBaud = transmBaud;
    }

    @JsonIgnore
    public Satellite getOwningSatelliteObj() {
        return owningSatellite;
    }

    public String getOwningSatellite() {
        return owningSatellite.getSatName();
    }

    public void setOwningSatellite(Satellite satellite) {
        this.owningSatellite = satellite;
    }

    @JsonIgnore
    public Link getLinkObj() {
        return link;
    }

    public String getLink() {
        if(link == null) {
            return null;
        }

        return link.getLinkMode();
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return String.format("Transmitter name: %s\n Frequency:%s\n Mode: %s\n Baud:%s\n", this.transmName, this.transmFreq, this.transmName, this.transmBaud);
    }
}
