package gyk4j.wreck.util.logging;

import java.util.Map;

public interface StatisticsListener<T, U> {
	Map<T, U> get();
	U get(T t);
	U count(T t);
	void clear();
}
