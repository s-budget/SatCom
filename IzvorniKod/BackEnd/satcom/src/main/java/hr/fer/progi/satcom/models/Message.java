package hr.fer.progi.satcom.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

import javax.persistence.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@Entity(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private String stationName;
    private String satelliteName;
    @JsonFormat(pattern = "dd-M-yyyy hh:mm:ss",timezone="Europe/Zagreb")
    @Column(name = "timestampOfMessage")
    private LocalDateTime creationDate;
    private String text;
    private  String direction;
    private Long freq;
    private String mode;
    private Integer baud;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="userId", nullable=true)
    private User userMessage; // ? ostavit cu ovak. Ali pitati.

    // ovo bi trebao biti jedini konstruktor za ovu klasu. Ali imamo ih 2 za sad jer testiramo.
    public Message(Long messageId, String stationName, String satelliteName, LocalDateTime creationDate, String text, String direction, long freq, String mode, int baud, User userMessage) {
        this.messageId = messageId;
        this.stationName = stationName;
        this.satelliteName = satelliteName;
        this.creationDate = creationDate;
        this.text = text;
        this.direction = direction;
        this.freq = freq;
        this.mode = mode;
        this.baud = baud;
        this.userMessage = userMessage;
    }

    public Message(String stationName, String satelliteName, LocalDateTime creationDate, String text, String direction, long freq, String mode, int baud) {
        this.stationName = stationName;
        this.satelliteName = satelliteName;
        this.creationDate = creationDate;
        this.text = text;
        this.direction = direction;
        this.freq = freq;
        this.mode = mode;
        this.baud = baud;
    }

    @JsonIgnore
    public User getUserMessageObj() {
        return userMessage;
    }

    public String getUserMessage() {
        return  userMessage.getUsername();
    }

    public void setUserMessage(User userMessage) {
        this.userMessage = userMessage;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getSatelliteName() {
        return satelliteName;
    }

    public void setSatelliteName(String satelliteName) {
        this.satelliteName = satelliteName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Long getFreq() {
        return freq;
    }

    public void setFreq(Long freq) {
        this.freq = freq;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getBaud() {
        return baud;
    }

    public void setBaud(Integer baud) {
        this.baud = baud;
    }

    public Message() {super();}

    @Override
    public String toString() {
        return "Message{" +
                "stationName = '" + stationName + '\'' +
                ", satelliteName = '" + satelliteName + '\'' +
                ", creationDate = " + creationDate +
                ", text = '" + text + '\'' +
                ", direction = '" + direction + '\'' +
                ", freq = " + freq +
                ", mode = '" + mode + '\'' +
                ", baud = " + baud +
                '}';
    }

    public String getStringifiedJson() {
        JSONObject jo = new JSONObject();
        jo.put("SatName", satelliteName);
        jo.put("StatName", stationName);
        jo.put("Date", creationDate.format(DateTimeFormatter.ofPattern("dd-M-yyyy HH:mm:ss")));
        jo.put("Text",text);
        jo.put("Direction",direction);
        jo.put("Freq",freq);
        jo.put("Mode",mode);
        jo.put("Baud",baud);

        return jo.toString();
    }

    public Message(String stringyJson) throws ParseException {

        JSONObject jo = new JSONObject(stringyJson);
        satelliteName=jo.get("SatName").toString();
        stationName=jo.get("StatName").toString();
        creationDate=LocalDateTime.parse(jo.get("Date").toString(), DateTimeFormatter.ofPattern("dd-M-yyyy HH:mm:ss"));
        text=jo.get("Text").toString();
        direction=jo.get("Direction").toString();
        freq=Long.parseLong(jo.get("Freq").toString());
        mode=jo.get("Mode").toString();
        baud=Integer.parseInt(jo.get("Baud").toString());

    }

    public Long getMessageId() {
        return messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId) && Objects.equals(stationName, message.stationName) && Objects.equals(satelliteName, message.satelliteName) && (creationDate.toEpochSecond(ZoneOffset.UTC)- message.creationDate.toEpochSecond(ZoneOffset.UTC)<=1) && Objects.equals(text, message.text) && Objects.equals(direction, message.direction) && Objects.equals(freq, message.freq) && Objects.equals(mode, message.mode) && Objects.equals(baud, message.baud) && Objects.equals(userMessage, message.userMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, stationName, satelliteName, creationDate.toEpochSecond(ZoneOffset.UTC), text, direction, freq, mode, baud, userMessage);
    }
}

