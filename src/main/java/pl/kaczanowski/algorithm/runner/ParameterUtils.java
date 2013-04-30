package pl.kaczanowski.algorithm.runner;

import java.util.Map;

import com.google.common.collect.Maps;

public class ParameterUtils {

	private static final String PARAM_VALUE_DELIMITER = "=";

	public Map<Parameters, String> readParamters(final String[] args) {
		Map<Parameters, String> result = Maps.newHashMap();

		for (String param : args) {
			String key = param.substring(0, param.indexOf(PARAM_VALUE_DELIMITER));

			Parameters paramters = Parameters.getByPrefix(key);

			String value = param.substring(param.indexOf(PARAM_VALUE_DELIMITER) + 1);
			result.put(paramters, value);

		}

		return result;
	}
}
