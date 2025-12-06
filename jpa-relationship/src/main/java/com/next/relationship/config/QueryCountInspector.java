package com.next.relationship.config;

import java.util.concurrent.atomic.AtomicInteger;
import org.hibernate.resource.jdbc.spi.StatementInspector;

public class QueryCountInspector implements StatementInspector {

    private static final ThreadLocal<AtomicInteger> queryCount = ThreadLocal.withInitial(AtomicInteger::new);

    @Override
    public String inspect(String sql) {
        queryCount.get().incrementAndGet();
        return sql;
    }

    public static void reset() {
        queryCount.get().set(0);
    }

    public static int getCount() {
        return queryCount.get().get();
    }
}
