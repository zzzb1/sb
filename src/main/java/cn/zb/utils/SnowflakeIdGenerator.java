package cn.zb.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

/**
 * 可排序自生成Id生成器.
 *
 * 长度为64bit,从高位到低位依次为
 *
 * 1bit   符号位
 * 41bits 时间偏移量从2016年11月1日零点到现在的毫秒数
 * 10bits 工作进程Id
 * 12bits 同一个毫秒内的自增量
 * @author zb Created in 11:18 PM 2018/5/8
 */
public class SnowflakeIdGenerator implements IdGenerate {

    // 基准时间, 一般小于开发时间，用于减少ID长度
    public static final long EPOCH;

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_BITS = 10L;

    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.APRIL, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        EPOCH = calendar.getTimeInMillis(); // 设置基准时间为 2017年4月1日
        //initWorkerId();
    }

    private long workerId;
    private long sequence;
    private long lastTime;

    public SnowflakeIdGenerator() {
        this.workerId = workerIdGen();
    }

    public SnowflakeIdGenerator(long workerId) {
        this.workerId = workerId;
    }

    @Override
    public synchronized Number generateId() {
        long time = timeGen();
        if (lastTime == time) {
            if (0L == (sequence = ++sequence & SEQUENCE_MASK)) {
                time = waitUntilNextTime(time);
            }
        } else {
            sequence = 0;
        }
        lastTime = time;
        return ((time - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    private long waitUntilNextTime(final long lastTime) {
        long time = timeGen();
        while (time <= lastTime) {
            time = timeGen();
        }
        return time;
    }

    // 获取当前的时间戳
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 根据机器IP获取工作进程Id,如果线上机器的IP二进制表示的最后10位不重复,建议使用此种方式
     * ,列如机器的IP为192.168.1.108,二进制表示:11000000 10101000 00000001 01101100
     * ,截取最后10位 01 01101100,转为十进制364,设置workerId为364.
     */
    protected long workerIdGen() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        byte[] ipArray = address.getAddress();
        int ipLength = ipArray.length;
        return (((ipArray[ipLength - 2] & 0B11) << Byte.SIZE) + (ipArray[ipLength - 1] & 0xFF));
    }

}
