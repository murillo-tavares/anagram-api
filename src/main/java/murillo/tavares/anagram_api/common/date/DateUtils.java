package murillo.tavares.anagram_api.common.date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

	@Setter
	private static Clock clock = Clock.systemDefaultZone();

	@Bean
	static Clock clock() {
		clock = Clock.systemDefaultZone();
		return clock;
	}

	public static LocalDate currentDate() {
		return LocalDate.now(clock);
	}

	public static void resetClock() {
		clock = Clock.systemDefaultZone();
	}
}
