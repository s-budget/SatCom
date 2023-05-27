package hr.fer.progi.satcom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Link model.
 * Link is uniquely defined by linkId (a long).
 * Link has working mode and frequency.
 * @author satcomBackend
 * */
@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;
    @NotBlank
    @Column(name = "linkMode")
    private String linkMode;
    @NotNull
    @Column(name = "linkFreq")
    private Long linkFreq;

    @NotNull
    @Column(name = "linkBaud")
    private Integer linkBaud;

    @OneToMany(mappedBy = "link", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Transmitter> transmitter;
    @PreRemove
    private void preRemove() {
        if(transmitter != null) {
            for (Transmitter t : transmitter) {
                t.setLink(null);
            }
        }
        if(antennas != null) {
            for(Antenna a : antennas) {
                a.getLinkAntennaObj().remove(this);
            }
        }

    }

    @PreUpdate
    private void preRemove2() {
        if(transmitter != null) {
            for (Transmitter t : transmitter) {
                t.setLink(null);
            }
        }
            }

    @ManyToMany(mappedBy = "linkAntenna", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)

    private Set<Antenna> antennas;

    @JsonIgnore
    public Set<Transmitter> getTransmitterObj() {
        return transmitter;
    }

    public Set<String> getTransmitters() {
        return transmitter.stream().map(t -> t.getTransmName()).collect(Collectors.toSet());
    }

    public void setTransmitter(Set<Transmitter> transmitter) {
        this.transmitter = transmitter;
    }

    public Integer getLinkBaud() {
        return linkBaud;
    }

    public void setLinkBaud(Integer linkBaud) {
        this.linkBaud = linkBaud;
    }

    @JsonIgnore
    public Set<Antenna> getAntennasObj() {
        return antennas;
    }

    public Set<String> getAntennas() {
        return antennas.stream().map(t -> t.getAntennaType()).collect(Collectors.toSet());
    }

    public void setAntennas(Set<Antenna> antennas) {
        this.antennas = antennas;
    }


    public Link() {
        super();
    }

    public Link(String linkMode, Long linkFreq, Integer linkBaud) {
        this.linkMode = linkMode;
        this.linkFreq = linkFreq;
        this.linkBaud = linkBaud;
    }
    public Link(String linkMode, Long linkFreq, Integer linkBaud, Set<Antenna> antennas) {
        this.linkMode = linkMode;
        this.linkFreq = linkFreq;
        this.antennas.addAll(antennas);
    }

    public Long getLinkId() {
        return linkId;
    }
    public void setLinkId(Long linkId) {this.linkId = linkId;}

    public String getLinkMode() {
        return linkMode;
    }

    public void setLinkMode(String linkMode) {
        this.linkMode = linkMode;
    }

    public Long getLinkFreq() {
        return linkFreq;
    }

    public void setLinkFreq(Long linkFreq) {
        this.linkFreq = linkFreq;
    }

    @Override
    public String toString() {
        return String.format("Link ID: " + linkId + "\nlink mode: " +
                linkMode + "\nlink frequency: " + linkFreq);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(linkMode, link.linkMode) && Objects.equals(linkFreq, link.linkFreq) && Objects.equals(linkBaud, link.linkBaud);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linkMode, linkFreq, linkBaud);
    }
}
