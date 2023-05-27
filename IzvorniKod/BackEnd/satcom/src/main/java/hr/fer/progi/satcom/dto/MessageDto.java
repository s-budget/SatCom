
package hr.fer.progi.satcom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hr.fer.progi.satcom.models.Message;

import java.io.IOException;
import java.time.LocalDateTime;


    // ova klasa sadrzi samo dio atributa od prave Message klase.
// ovu cemo koristiti za serijalizaciju i deserijalizaciju kod slanja poruke na satelit.
public class MessageDto {
        private String stationName;
        private String satelliteName;
        @JsonFormat(pattern = "dd-M-yyyy hh:mm:ss",timezone="Europe/Zagreb")
        private LocalDateTime creationDate;
        private String text;
        private String direction; // bilo bi dobro da napravimo neki DirectionEnum.
        private long freq;
        private String mode;
        private int baud;

        public MessageDto() {
        }

        // moguce i stvaranje objekta MessageDto iz danog objekta Messagea:
        public MessageDto(Message message) {
            this.stationName = message.getStationName();
            this.satelliteName = message.getSatelliteName();
            this.creationDate = message.getCreationDate();
            this.text = message.getText();
            this.direction = message.getDirection();
            this.freq = message.getFreq();
            this.mode = message.getMode();
            this.baud = message.getBaud();
        }

        public MessageDto(String stationName, String satelliteName, LocalDateTime creationDate, String text, String direction, long freq, String mode, int baud) {
            this.stationName = stationName;
            this.satelliteName = satelliteName;
            this.creationDate = creationDate;
            this.text = text;
            this.direction = direction;
            this.freq = freq;
            this.mode = mode;
            this.baud = baud;
        }

        public String toJson() {

//        JSONObject jo = new JSONObject();
//        jo.put("SatName", satelliteName);
//        jo.put("StatName", stationName);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        jo.put("Date", simpleDateFormat.format(creationDate));
//        jo.put("Text",text);
//        jo.put("Direction",direction);
//        jo.put("Freq",freq);
//        jo.put("Mode",mode);
//        jo.put("Baud",baud);

            //configure Object mapper for pretty print
            String stringMess = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                //stringMess = new StringWriter();
                //MessageDto messageDto = new MessageDto();
                stringMess = objectMapper.writeValueAsString(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return stringMess;
        }

        public MessageDto fromJson(String stringyJson) {

            ObjectMapper objectMapper = new ObjectMapper();
            MessageDto deserializedMessage = new MessageDto();
            try {
                deserializedMessage = objectMapper.readValue(stringyJson, MessageDto.class);
                System.out.println("Message Objectdeserialized in function!: \n" + deserializedMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }

//        JSONObject jo = new JSONObject(stringyJson);
//        satelliteName=jo.get("SatName").toString();
//        stationName=jo.get("StatName").toString();
//        creationDate=new SimpleDateFormat("dd-MM-yyyy").parse(jo.get("Date").toString());
//        text=jo.get("Text").toString();
//        direction=jo.get("Direction").toString();
//        freq=Long.parseLong(jo.get("Freq").toString());
//        mode=jo.get("Mode").toString();
//        baud=Integer.parseInt(jo.get("Baud").toString());

            return deserializedMessage;

        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public void setSatelliteName(String satelliteName) {
            this.satelliteName = satelliteName;
        }

        public void setCreationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public void setFreq(long freq) {
            this.freq = freq;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public void setBaud(int baud) {
            this.baud = baud;
        }

        public String getStationName() {
            return stationName;
        }

        public String getSatelliteName() {
            return satelliteName;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }

        public String getText() {
            return text;
        }

        public String getDirection() {
            return direction;
        }

        public long getFreq() {
            return freq;
        }

        public String getMode() {
            return mode;
        }

        public int getBaud() {
            return baud;
        }

        @Override
        public String toString() {
            return "MessageDto{" +
                    "stationName='" + stationName + '\'' +
                    ", satelliteName='" + satelliteName + '\'' +
                    ", creationDate=" + creationDate +
                    ", text='" + text + '\'' +
                    ", direction='" + direction + '\'' +
                    ", freq=" + freq +
                    ", mode='" + mode + '\'' +
                    ", baud=" + baud +
                    '}';
        }
    }
