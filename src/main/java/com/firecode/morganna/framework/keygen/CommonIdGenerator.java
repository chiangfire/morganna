package com.firecode.morganna.framework.keygen;

import java.util.Calendar;

import com.firecode.morganna.framework.AbstractClock;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * @see https://github.com/shardingjdbc/sharding-jdbc/blob/dev/sharding-jdbc-core/src/main/java/io/shardingjdbc/core/keygen/DefaultKeyGenerator.java
 * 
 * Use snowflake algorithm. Length is 64 bit
 * 1bit   sign bit
 * 41bits time offset
 * 10bits work process Id
 * 12bits auto increment offset in one mills
 * @author gaohongtao
 */
public abstract class CommonIdGenerator{
	
	private final long SJDBC_EPOCH;

	private static final long SEQUENCE_BITS = 12L;

	private static final long WORKER_ID_BITS = 10L;

	private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

	private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

	private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

	private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

	private AbstractClock clock = AbstractClock.systemClock();

	private long workerId;
	
	private long sequence;

	private long lastTime;

	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.NOVEMBER, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		SJDBC_EPOCH = calendar.getTimeInMillis();
		initWorkerId();
	}

	void initWorkerId() {
		String workerId = System.getProperty("snowflake.id.generator.worker.id");
		if (!Strings.isNullOrEmpty(workerId)) {
			setWorkerId(Long.valueOf(workerId));
			return;
		}
		workerId = System.getenv("SNOWFLAKE_ID_GENERATOR_WORKER_ID");
		if (Strings.isNullOrEmpty(workerId)) {
			return;
		}
		setWorkerId(Long.valueOf(workerId));
	}

	/**
	 * @param workerId  
	 */
	protected void setWorkerId(final Long workerId) {
		Preconditions.checkArgument(workerId >= 0L && workerId < WORKER_ID_MAX_VALUE);
		this.workerId = workerId;
	}

	protected long getWorkerIdLength() {
		return WORKER_ID_BITS;
	}

	protected synchronized Number createId() {
		long time = this.clock.millis();
		Preconditions.checkState(lastTime <= time,"Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastTime,time);
		if (lastTime == time) {
			if (0L == (++sequence & SEQUENCE_MASK)) {
				time = waitUntilNextTime(time);
			}
		} else {
			sequence = 0;
		}
		lastTime = time;
		
		return ((time - SJDBC_EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (this.workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
	}

	private long waitUntilNextTime(final long lastTime) {
		long time = this.clock.millis();
		while (time <= lastTime) {
			time = this.clock.millis();
		}
		return time;
	}

	protected long getWorkerId() {
		return this.workerId;
	}
	protected void setClock(AbstractClock clock) {
		this.clock = clock;
	}

}
