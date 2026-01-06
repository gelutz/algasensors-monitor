package lutz.algasensors.monitor.api.model;

import java.time.LocalDate;

public record DailyMedianTemperatureOutput(LocalDate date, Double medianTemperature) {
}

