package io.blackpine.sustain.concurrentSpark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SparkService {
    protected ExecutorService executorService; 
    private String sparkMaster;

    public SparkService(String sparkMaster, int threadCount) {
        this.sparkMaster = sparkMaster;
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    private SparkSession initSparkSession() throws Exception {
        SparkSession sparkSession = SparkSession.builder()
            .master(this.sparkMaster)
            .appName("ConcurrentSpark")
            .getOrCreate();

        // TODO - if they don't exist - add JARs to SparkContext
        // if (!sparkContext.jars.contains(...)) {...}

        return sparkSession;
    }

    public <T> Future<T> submit(SparkTask<T> sparkTask,
            String jobGroup) throws Exception {
        Future<T> future = this.executorService.submit(() -> {
            // initialize spark session
            SparkSession sparkSession = initSparkSession();
            JavaSparkContext sparkContext = 
                new JavaSparkContext(sparkSession.sparkContext());

            // set job group so all jobs submitted from this thread
            // share a common id, also set interruptOnCancel
            sparkContext.setJobGroup(jobGroup, "", true);

            try {
                // execute spark task
                return sparkTask.execute(sparkContext);
            } catch (InterruptedException e) {
                // if this callable is interrupted
                // -> cancel spark jobs for this group
                sparkContext.cancelJobGroup(jobGroup);
                throw e;
            }
        });

        return future;
    }
}
