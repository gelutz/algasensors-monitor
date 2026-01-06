package lutz.algasensors.monitor.infrastructure.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeederRunner implements ApplicationRunner {
	private static final String SEED_OPTION = "seed";
	private static final int DEFAULT_DAYS_BACK = 30;

	private final TemperatureLogSeederService seederService;

	@Override
	public void run(ApplicationArguments args) {
		if (!args.containsOption(SEED_OPTION)) {
			return;
		}

		int daysBack = parseDaysBack(args);
		log.info("Seeder triggered via CLI. Generating temperature logs for the last {} days...", daysBack);

		long startTime = System.currentTimeMillis();
		seederService.seedTemperatureLogs(daysBack);
		long duration = System.currentTimeMillis() - startTime;

		log.info("Seeding completed in {} ms", duration);
	}

	private int parseDaysBack(ApplicationArguments args) {
		try {
			var values = args.getOptionValues(SEED_OPTION);
			if (values != null && !values.isEmpty()) {
				return Integer.parseInt(values.getFirst());
			}
		} catch (NumberFormatException e) {
			log.warn("Invalid days value provided, using default: {}", DEFAULT_DAYS_BACK);
		}
		return DEFAULT_DAYS_BACK;
	}
}

