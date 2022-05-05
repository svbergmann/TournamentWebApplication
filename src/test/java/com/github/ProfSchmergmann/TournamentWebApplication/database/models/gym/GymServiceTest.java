package com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.LocationService;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class GymServiceTest {

	@Test
	public void createEastercupGyms(@Autowired LocationService locationService,
	                                @Autowired GymService gymService) {
		var gyms = new ArrayList<Gym>();

		var gym1 = new Gym();
		gym1.setName("OSZ Banken und Versicherungen");
		gym1.setLocation(locationService.find(10557, "Alt-Moabit", 10));
		gyms.add(gym1);

		var gym2 = new Gym();
		gym2.setName("Moabiter Grundschule");
		gym2.setLocation(locationService.find(10557, "Gerhardtstr.", 4));
		gyms.add(gym2);

		var gym3 = new Gym();
		gym3.setName("Kurt-Tucholsky-Grundschule");
		gym3.setLocation(locationService.find(10559, "Rathenower Str.", 18));
		gyms.add(gym3);

		var gym4 = new Gym();
		gym4.setName("Sporthalle Turmstr.");
		gym4.setLocation(locationService.find(10559, "Turmstr.", 85));
		gyms.add(gym4);

		var gym5 = new Gym();
		gym5.setName("Union-Sporthalle");
		gym5.setLocation(locationService.find(10551, "Siemensstr.", 20));
		gyms.add(gym5);

		var gym6 = new Gym();
		gym6.setName("Gymnasium Tiergarten");
		gym6.setLocation(locationService.find(10555, "Altonaer Str.", 26));
		gyms.add(gym6);

		var gym7 = new Gym();
		gym7.setName("Sportzentrum Gutsmuths");
		gym7.setLocation(locationService.find(10555, "Wullenweber Str.", 15));
		gyms.add(gym7);

		var gym8 = new Gym();
		gym8.setName("Allegro-Grundschule");
		gym8.setLocation(locationService.find(10785, "Lützowstr.", 83));
		gyms.add(gym8);

		for (int i = 0; i < gyms.size(); i++) {
			Gym g = gyms.get(i);
			g.setNumber(i + 1);
			g.setCapacity(500);
			gymService.create(g);
		}

	}
}