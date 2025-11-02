package lutz.algasensors.monitor.common;

import io.hypersistence.tsid.TSID;

import java.util.Optional;

public final class IdUtils {

	private static final TSID.Factory tsidFactory;

	static {
		Optional.ofNullable(System.getenv("tsid.node"))
		        .ifPresent(node -> System.setProperty("tsid.node", node));

		Optional.ofNullable(System.getenv("tsid.node.count"))
		        .ifPresent(count -> System.setProperty("tsid.node.count", count));
		tsidFactory = TSID.Factory.builder().build();
	}

	public static TSID tsid() {
		return TSID.Factory.getTsid();
	}
}
