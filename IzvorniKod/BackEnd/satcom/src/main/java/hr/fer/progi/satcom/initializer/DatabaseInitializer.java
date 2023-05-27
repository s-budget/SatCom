package hr.fer.progi.satcom.initializer;

import hr.fer.progi.satcom.dao.StationRepository;
import hr.fer.progi.satcom.dao.UserRepository;
import hr.fer.progi.satcom.models.User;
import hr.fer.progi.satcom.pullingFromApi.ScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class DatabaseInitializer {

    @Autowired
    ScheduledTask scheduledTask;

    @Autowired
    UserRepository userRepo;

    @Autowired
    StationRepository stationRepository;

    @PostConstruct
    public void initialize() throws IOException {
        if(!userRepo.existsByUsername("superAdmin") && !userRepo.existsByEmail("super.admin@gmail.com")) {
            userRepo.save(new User("superAdmin", "super.admin@gmail.com", "$2a$10$1zMJQfBquiu.LhEZ4CtD/OdU1gUrN2g75b9XtAm4GHpI7s1ryQD.G", "SUPER_ADMIN"));
        }

        if(!userRepo.existsByUsername("testUser") && !userRepo.existsByEmail("test.user@gmail.com")) {
            userRepo.save(new User("testUser", "test.user@gmail.com", "$2a$10$0iAUc0eXzW3Wioxs8bIxjezu4K1m3Kfyq6UM0DHlX1zu6nFuv2tyi", "USER"));
        }

        if(stationRepository.count() <= 0L) {
            scheduledTask.refreshStations();
        }

    }
}
