package pl.kaczanowski.analyze;

import java.math.BigDecimal;

public class IterationStatData implements Comparable<IterationStatData> {

	public static class Builder {

		private BigDecimal probabilityParam;

		private Double mean;

		private Double variance;

		private Double firstQuartile;
		private Double median;
		private Double thirdQuartile;
		private Integer min;
		private Integer max;

		private Integer mode;
		private Double modeProcentage;

		private Double bestEvaluatedProcentage;

		private Builder(final BigDecimal probabilityParameter) {
			probabilityParam = probabilityParameter;

		}

		public IterationStatData build() {
			IterationStatData statData = new IterationStatData(probabilityParam);
			statData.mean = mean;
			statData.variance = variance;
			statData.firstQuartile = firstQuartile;
			statData.median = median;
			statData.thirdQuartile = thirdQuartile;
			statData.min = min;
			statData.max = max;
			statData.mode = mode;
			statData.modeProcentage = modeProcentage;
			statData.bestEvaluatedProcentage = bestEvaluatedProcentage;

			return statData;
		}

		public Builder setBestEvaluatedProcentage(final Double bestEvaluatedProcentage) {
			this.bestEvaluatedProcentage = bestEvaluatedProcentage;
			return this;
		}

		public Builder setFirstQuartile(final Double firstQuartile) {
			this.firstQuartile = firstQuartile;
			return this;
		}

		public Builder setMax(final Integer max) {
			this.max = max;
			return this;
		}

		public Builder setMean(final Double mean) {
			this.mean = mean;
			return this;
		}

		public Builder setMedian(final Double median) {
			this.median = median;
			return this;
		}

		public Builder setMin(final Integer min) {
			this.min = min;
			return this;
		}

		public Builder setMode(final Integer mode) {
			this.mode = mode;
			return this;
		}

		public Builder setModeProcentage(final Double modeProcentage) {
			this.modeProcentage = modeProcentage;
			return this;
		}

		public Builder setProbabilityParam(final BigDecimal probabilityParam) {
			this.probabilityParam = probabilityParam;
			return this;
		}

		public Builder setThirdQuartile(final Double thirdQuartile) {
			this.thirdQuartile = thirdQuartile;
			return this;
		}

		public Builder setVariance(final Double variance) {
			this.variance = variance;
			return this;
		}

	}

	public static Builder build(final BigDecimal probabilityParameter) {
		return new Builder(probabilityParameter);
	}

	private final BigDecimal probabilityParameter;

	private Double mean;

	private Double variance;

	private Double firstQuartile;
	private Double median;
	private Double thirdQuartile;
	private Integer min;
	private Integer max;

	private Integer mode;
	private Double modeProcentage;

	private Double bestEvaluatedProcentage;

	private IterationStatData(final BigDecimal probabilityParameter) {
		this.probabilityParameter = probabilityParameter;
	}

	@Override
	public int compareTo(final IterationStatData o) {
		return probabilityParameter.compareTo(o.probabilityParameter);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof IterationStatData)) {
			return false;
		}
		IterationStatData other = (IterationStatData) obj;
		if (probabilityParameter == null) {
			if (other.probabilityParameter != null) {
				return false;
			}
		} else if (!probabilityParameter.equals(other.probabilityParameter)) {
			return false;
		}
		return true;
	}

	public Double getBestEvaluatedProcentage() {
		return bestEvaluatedProcentage;
	}

	public Double getFirstQuartile() {
		return firstQuartile;
	}

	public Integer getMax() {
		return max;
	}

	public Double getMean() {
		return mean;
	}

	public Double getMedian() {
		return median;
	}

	public Integer getMin() {
		return min;
	}

	public Integer getMode() {
		return mode;
	}

	public Double getModeProcentage() {
		return modeProcentage;
	}

	public BigDecimal getProbabilityParameter() {
		return probabilityParameter;
	}

	public Double getThirdQuartile() {
		return thirdQuartile;
	}

	public Double getVariance() {
		return variance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((probabilityParameter == null) ? 0 : probabilityParameter.hashCode());
		return result;
	}

}
