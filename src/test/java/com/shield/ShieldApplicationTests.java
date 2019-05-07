package com.shield;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.shield.domain.EsMember;
import com.shield.mapper.MemberMapper;
import com.shield.repository.MemberRepository;
import com.shield.utils.ForeachUtil;
import com.shield.utils.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShieldApplicationTests {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final int CORE_CPUS = Runtime.getRuntime().availableProcessors();

//    private static ExecutorService executor = new ThreadPoolExecutor(
//            CORE_CPUS, 10, 60L, TimeUnit.SECONDS,
//            new ArrayBlockingQueue<>(6000));

    @Test
    public void concurrencyInsertMember() throws InterruptedException, ExecutionException {
        Integer countAll = memberMapper.selectCount(new QueryWrapper<>());
        int batchCount = 10000;
        //批量次数
        int n = countAll / batchCount;
        //批量插入
        int finalBatchCount = batchCount;
        CompletionService<List<Integer>>
                completionService = new ExecutorCompletionService<>(ThreadPoolService.getInstance());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("query db ....");
        for (int i = 1; i <= n; i++) {
            int finalI = i;
            completionService.submit(() -> {
                try {
                    PageHelper.startPage(finalI, finalBatchCount);
                    List<EsMember> esMembers = memberMapper.selectList(new QueryWrapper<>());
                    memberRepository.saveAll(esMembers);
                } catch (Exception e) {
                    log.error("member insert pageNum:{} - pageSize:{}", finalI, finalBatchCount);
                }

                return Arrays.asList(finalI);
            });
        }
        List<Integer> result = ForeachUtil.foreachAddWithReturn(n, (i) -> get(completionService));
        ThreadPoolService.getInstance().shutdown();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    public static <T> List<T> get(CompletionService<List<T>> completionService) {
        // lambda cannot handler checked exception
        try {
            return completionService.take().get();
        } catch (Exception e) {
            e.printStackTrace();  // for log
            throw new RuntimeException(e.getCause());
        }
    }

    @Test
    public void testCount() {
        long count = memberRepository.count();

    }

}
