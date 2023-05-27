package hr.fer.progi.satcom.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Satellite model.
 * Satellites are defined by satellite admin role.
 * Satellite is uniquely identified by satId (a long).
 * @author satcomBackend
 * */

@Entity
@Table(name = "satellites")
public class Satellite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long satId;

    @NotBlank
    @Column(unique = true)
    private String satName;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "owningSatellite", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Transmitter> transmitters;
    @PreRemove
    private void preRemove() {
        for (Transmitter t : transmitters) {
            t.setOwningSatellite(null);
        }
    }


    public Satellite() {
        super();
    }

    public Satellite(String satName, User user) {
        this.satName = satName;
        this.creationDate = new Date();
        this.createdBy = user;
    }

    public Long getSatId() {
        return satId;
    }

    public String getSatName() {
        return satName;
    }

    public void setSatName(String satName) {
        this.satName = satName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @JsonIgnore
    public User getCreatedByUser() {
        return createdBy;
    }

    public String getCreatedBy() {
        return createdBy.getUsername();
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @JsonIgnore
    public Set<Transmitter> getTransmittersObj() {
        return transmitters;
    }

    public Set<String> getTransmitters() {
        return transmitters.stream().map(t -> t.getTransmName()).collect(Collectors.toSet());
    }

    public void setTransmitters(Set<Transmitter> transmitters) {
        this.transmitters = transmitters;
    }
}
