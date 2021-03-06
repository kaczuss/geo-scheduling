package pl.kaczanowski.graph.dataproviders;

import static com.google.common.base.Preconditions.checkNotNull;
import static pl.kaczanowski.utils.SimpleFunctions.stringToInteger;
import static pl.kaczanowski.utils.SimplePredicates.isNotEmptyString;
import static pl.kaczanowski.utils.SimplePredicates.notStartsFrom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import pl.kaczanowski.model.ModulesGraph;
import pl.kaczanowski.model.ProcessorsGraph;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

/**
 * Provides graph and processors tree.
 * @author kaczanowskip
 */
public class SchedulingGraphsDataProvider {

	private static abstract class AbstractGraphFileDataIterator implements Iterator<Object[]> {

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private static class GeoGraphFileIterator extends AbstractGraphFileDataIterator implements Iterator<Object[]> {

		private List<File> files;

		private int actualFile;

		public GeoGraphFileIterator(final Collection<File> listFiles) {
			this.files = Lists.newArrayList(listFiles);
			actualFile = 0;
		}

		@Override
		public boolean hasNext() {
			return actualFile < files.size();
		}

		@Override
		public Object[] next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			try {
				File processorsFile = files.get(actualFile);

				// first line - tasks cost
				Object[] result = new Object[2];
				String treeName = getTreeGraphName(processorsFile);

				result[0] = getModuleGraph(treeName);

				result[1] = getProcessorGraphFromFile(processorsFile);
				actualFile++;

				return result;
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		}

	}

	private static class SchedulingGraphFileIterator extends AbstractGraphFileDataIterator {

		private static final Logger LOG = LoggerFactory
				.getLogger(SchedulingGraphsDataProvider.SchedulingGraphFileIterator.class);

		private final List<File> scheduleFiles;

		private int actualFile;

		public SchedulingGraphFileIterator(final Collection<File> files) {
			scheduleFiles = ImmutableList.copyOf(files);
			actualFile = 0;
		}

		@Override
		public boolean hasNext() {
			return actualFile < scheduleFiles.size();
		}

		@Override
		public Object[] next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			try {
				File scheduleFile = scheduleFiles.get(actualFile);

				// first line - tasks cost
				Object[] result = new Object[4];
				String treeName = getTreeGraphName(scheduleFile);

				result[0] = getModuleGraph(treeName);

				List<String> schedulingLines = readFile(scheduleFile);

				int processorsNumber = Integer.valueOf(schedulingLines.get(0));
				int processorDevisionLineNumber = processorsNumber + 1;
				result[1] =
						new ProcessorsGraph(
								getProcessorsGraph(schedulingLines.subList(1, processorDevisionLineNumber)));
				int processorDevisionLastLine =
						processorDevisionLineNumber
								+ Integer.valueOf(schedulingLines.get(processorDevisionLineNumber)) + 1;
				result[2] =
						getProcessDivision(schedulingLines.subList(processorDevisionLineNumber + 1,
								processorDevisionLastLine));

				result[3] = Integer.valueOf(schedulingLines.get(processorDevisionLastLine));

				actualFile++;

				return result;
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
				throw Throwables.propagate(e);
			}
		}

	}

	private static class SchedulingPolicyGraphFileIterator extends AbstractGraphFileDataIterator {

		private static final Logger LOG = LoggerFactory
				.getLogger(SchedulingGraphsDataProvider.SchedulingPolicyGraphFileIterator.class);

		private final ImmutableList<File> schedulePolicyFiles;

		private int currentSchedulingPolicyFile;

		public SchedulingPolicyGraphFileIterator(final Collection<File> files) {
			checkNotNull(files);
			this.schedulePolicyFiles = ImmutableList.copyOf(files);
			currentSchedulingPolicyFile = 0;
		}

		@Override
		public boolean hasNext() {
			return currentSchedulingPolicyFile < schedulePolicyFiles.size();
		}

		@Override
		public Object[] next() {

			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			try {
				File policyFile = schedulePolicyFiles.get(currentSchedulingPolicyFile);

				LOG.debug("file " + policyFile.getName());

				// first line - tasks cost
				Object[] result = new Object[5];
				String treeName = getTreeGraphName(policyFile);

				result[0] = getModuleGraph(treeName);

				List<String> policyLines = readFile(policyFile);

				int processorsNumber = Integer.valueOf(policyLines.get(0));
				int processorDevisionLineNumber = processorsNumber + 1;
				result[1] =
						new ProcessorsGraph(getProcessorsGraph(policyLines.subList(1, processorDevisionLineNumber)));
				int processorDevisionLastLine =
						processorDevisionLineNumber + Integer.valueOf(policyLines.get(processorDevisionLineNumber))
								+ 1;
				result[2] =
						getProcessDivision(policyLines.subList(processorDevisionLineNumber + 1,
								processorDevisionLastLine));

				result[3] = Integer.valueOf(policyLines.get(processorDevisionLastLine));
				result[4] = Integer.valueOf(policyLines.get(processorDevisionLastLine + 1));

				currentSchedulingPolicyFile++;

				return result;
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
				throw Throwables.propagate(e);
			}
		}

	}

	@DataProvider
	public static Iterator<Object[]> getGeoData() {

		Collection<File> listFiles =
				FileUtils.listFiles(new File("src/test/resources/graphs/geoProcessors"),
						FileFilterUtils.fileFileFilter(), null);
		return new GeoGraphFileIterator(listFiles);

	}

	protected static Integer[][] getGraphConnections(final List<String> connectionsMatrix) {

		Integer[][] result = new Integer[connectionsMatrix.size()][connectionsMatrix.size()];

		for (int i = 0; i < connectionsMatrix.size(); i++) {
			result[i] = getIntegerArray(connectionsMatrix.get(i));
		}

		return result;
	}

	protected static Integer[] getIntegerArray(final String integersLine) {
		Iterable<Integer> iterable = Iterables.transform(Splitter.on(';').split(integersLine), stringToInteger());
		return Iterables.toArray(iterable, Integer.class);
	}

	protected static Set<Integer> getIntegerSet(final String integersLine) {
		return Sets.newTreeSet(Iterables.transform(Splitter.on(';').split(integersLine), stringToInteger()));
	}

	public static ModulesGraph getModuleGraph(final String treeName) throws IOException {
		File graphFile = new File("src/test/resources/graphs/" + treeName + ".txt");

		List<String> graphLines = readFile(graphFile);

		Integer[] taskCosts = getIntegerArray(graphLines.get(0));
		ModulesGraph modulesGraph =
				new ModulesGraph(treeName, taskCosts,
						getGraphConnections(graphLines.subList(1, 1 + taskCosts.length)));
		return modulesGraph;
	}

	protected static Map<Integer, Set<Integer>> getProcessDivision(final List<String> processorDevision) {
		Map<Integer, Set<Integer>> result = Maps.newHashMapWithExpectedSize(processorDevision.size());

		for (int i = 0; i < processorDevision.size(); i++) {
			result.put(i, getIntegerSet(processorDevision.get(i)));
		}

		return result;
	}

	public static ProcessorsGraph getProcessorGraphFromFile(final File processorsFile) throws IOException {
		List<String> processorsLines = readFile(processorsFile);

		ProcessorsGraph processorsGraph = new ProcessorsGraph(getProcessorsGraph(processorsLines));
		return processorsGraph;
	}

	protected static Integer[][] getProcessorsGraph(final List<String> processorsGraphMatrix) {

		Integer[][] result = new Integer[processorsGraphMatrix.size()][processorsGraphMatrix.size()];

		for (int i = 0; i < processorsGraphMatrix.size(); i++) {
			result[i] = getIntegerArray(processorsGraphMatrix.get(i));

		}

		return result;
	}

	@DataProvider
	public static Iterator<Object[]> getSchedulingData() {

		Collection<File> listFiles =
				FileUtils.listFiles(new File("src/test/resources/graphs/scheduling"),
						FileFilterUtils.fileFileFilter(), null);
		return new SchedulingGraphFileIterator(listFiles);

	}

	@DataProvider
	public static Iterator<Object[]> getSchedulingPolicyData() {
		Collection<File> listFiles =
				FileUtils.listFiles(new File("src/test/resources/graphs/schedulingPolicy"),
						FileFilterUtils.fileFileFilter(), null);
		return new SchedulingPolicyGraphFileIterator(listFiles);

	}

	protected static String getTreeGraphName(final File file) {
		return file.getName().subSequence(0, file.getName().indexOf('_')).toString();
	}

	protected static ArrayList<String> readFile(final File graphFile) throws IOException {
		return Lists.newArrayList(Iterables.filter(Iterables.filter(
				CharStreams.readLines(Files.newReaderSupplier(graphFile, Charsets.UTF_8)), isNotEmptyString()),
				notStartsFrom('#')));
	}

}
