package gyk4j.wreck.util.logging;

import java.util.HashMap;
import java.util.Map;

public class CountListener<T> implements StatisticsListener<T, Integer> {

	private final Map<T, Integer> statistics = new HashMap<>();
	
	@Override
	public Map<T, Integer> get() {
		return statistics;
	}

	@Override
	public Integer get(T t) {
		return get().getOrDefault(t, 0);
	}

	@Override
	public Integer count(T t) {
		Integer v = get().merge(t, 1, (ov, nv) -> ++ov);
		return (v == null)? 1: v;
	}

	@Override
	public void clear() {
		get().clear();
	}
	
}
