package hr.fer.progi.satcom.dao;

import hr.fer.progi.satcom.models.Transmitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransmitterRepository extends JpaRepository<Transmitter, Long> {

    Optional<Transmitter> findByTransmFreq(String transmFreq);

}
