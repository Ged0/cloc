package com.linezh;

import com.linezh.objs.FileCount;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: line
 * Date: 2019-05-27
 * Time: 20:19
 * Description:
 */
public class ClocExecutor {
    private ExecutorService singleFileCountPool;
    private ExecutorService totalCountThread;

    private long finishedCount;
    private long submitCount;
    private CountDownLatch stopSignal;

    private Map<String, FileCount> countMap;

    public ClocExecutor() {
        singleFileCountPool = Executors.newWorkStealingPool();
        totalCountThread = Executors.newSingleThreadExecutor();

        finishedCount = 0;
        submitCount = 0;
        stopSignal = new CountDownLatch(1);

        countMap = new HashMap<>();
    }


    public long getFinishedCount() {
        return finishedCount;
    }

    public long getSubmitCount() {
        return submitCount;
    }

    public CountDownLatch getStopSignal() {
        return stopSignal;
    }

    public void submit(File file) {
        if (file.exists()) {
            _submit(file);
        } else {
            System.out.println("file not exists!");
        }
        this.stopSignal.countDown();
    }

    public void _submit(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                _submit(f);
            }
        } else {
            this.submitCount += 1;
            singleFileCountPool.submit(() -> {
                try {
                    final FileCount singleFileCount = CodeParser.parserSingleFile(file, Cloc.CODE_RULE_MAP);
                    totalCountThread.submit(() -> {
                        FileCount totalCount = this.countMap.get(singleFileCount.getLanguage());
                        if (totalCount == null) {
                            this.countMap.put(singleFileCount.getLanguage(), new FileCount(singleFileCount));
                        } else {
                            totalCount.add(singleFileCount);
                        }
                        ++this.finishedCount;
                    });
                } catch (Exception e) {
                    --this.finishedCount;
                }
            });
        }
    }

    public void printResult() {
        if (this.getSubmitCount() == 0) {
            return;
        }
        System.out.println("-------------------------");
        System.out.println("-------------------------");
        FileCount totalCount = new FileCount("TOTAL", 0, 0, 0);
        for (Map.Entry<String, FileCount> entry : countMap.entrySet()) {
            String key = entry.getKey();
            FileCount value = entry.getValue();
            totalCount.add(value);
            System.out.println(String.format("type: %s   code:%d   comment:%d   blank:%d", key, value.getCodeCount(),
                    value.getCommentsCount(), value.getBlankCount()));
        }
        System.out.println(String.format("type: %s   code:%d   comment:%d   blank:%d", totalCount.getLanguage(),
                totalCount.getCodeCount(), totalCount.getCommentsCount(), totalCount.getBlankCount()));

        System.out.println("files numbers:" + this.getSubmitCount());

        this.singleFileCountPool.shutdown();
        this.totalCountThread.shutdown();
    }
}
