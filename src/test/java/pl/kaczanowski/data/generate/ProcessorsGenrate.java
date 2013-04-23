package pl.kaczanowski.data.generate;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class ProcessorsGenrate {

	public static void main(final String[] args) {
		List<Integer> values = Lists.newArrayList();
		for (int i = 0; i < 40; ++i) {
			values.add(4);
		}
		System.out.println(Joiner.on(";").join(values));
	}
}
