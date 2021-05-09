package me.lucyy.squirtgun.platform.scheduler;

import com.google.common.base.Preconditions;
import me.lucyy.squirtgun.platform.Platform;
import java.util.function.Consumer;

/**
 * TODO javadoc
 */
public class Task {

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private int interval = -1;
		private int delay = -1;
		private boolean isAsync = false;
		private Consumer<Platform> action;

		public Builder interval(int interval) {
			Preconditions.checkArgument(interval > 0, "Interval was not greater than 0");
			this.interval = interval;
			return this;
		}

		public Builder delay(int delay) {
			Preconditions.checkArgument(interval > 0, "Delay was not greater than 0");
			this.delay = delay;
			return this;
		}

		public Builder async() {
			this.isAsync = true;
			return this;
		}

		public Builder action(Consumer<Platform> action) {
			this.action = action;
			return this;
		}

		public Task build() {
			Preconditions.checkArgument(delay != -1, "Delay has not been set");
			Preconditions.checkNotNull(action, "Action has not been set");
			return new Task(action, delay, interval, isAsync);
		}
	}

	private final Consumer<Platform> action;
	private final int delay;
	private final int interval;
	private final boolean isAsync;

	public Task(final Consumer<Platform> action, final int delay, final int interval, final boolean isAsync) {
		this.action = action;
		this.delay = delay;
		this.interval = interval;
		this.isAsync = isAsync;
	}

	public boolean isRepeating() {
		return interval != -1;
	}

	public int getDelay() {
		return delay;
	}

	public int getInterval() {
		return interval;
	}

	public void execute(Platform platform) {
		action.accept(platform);
	}

	public boolean isAsync() {
		return isAsync;
	}
}