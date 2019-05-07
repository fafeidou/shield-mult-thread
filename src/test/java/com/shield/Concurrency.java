package com.shield;

import com.shield.domain.EsMember;
import com.shield.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Concurrency {
    @Autowired
    private MemberMapper memberMapper;
    // 请求总数
    public static int clientTotal = 10000;

    // 同时并发执行的线程数
    public static int threadTotal = 50;

    public static AtomicLong count = new AtomicLong(0);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    public void concurrencyQuery() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("query db ....");
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from es_member limit 0 , 5000");
                    EsMember esMember = memberMapper.selectById(11);
                    semaphore.release();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        executorService.shutdown();
        log.info("count:{}", count.get());
    }

    private static void add() {
        count.incrementAndGet();
        // count.getAndIncrement();
    }
}
