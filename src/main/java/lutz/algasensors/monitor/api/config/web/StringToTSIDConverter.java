package lutz.algasensors.monitor.api.config.web;

import io.hypersistence.tsid.TSID;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTSIDConverter implements Converter<String, TSID> {
	@Override
	public TSID convert(@NonNull String source) {
		return TSID.from(source);
	}
}
