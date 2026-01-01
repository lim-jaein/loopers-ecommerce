package com.loopers.batch.job.ranking;

import com.loopers.batch.job.ranking.step.MonthlyRankingTasklet;
import com.loopers.batch.job.ranking.step.WeeklyRankingTasklet;
import com.loopers.batch.listener.JobListener;
import com.loopers.batch.listener.StepMonitorListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(
        name = "spring.batch.job.name",
        havingValue = RankingJobConfig.JOB_NAME
)
@RequiredArgsConstructor
@Configuration
public class RankingJobConfig {
    public static final String JOB_NAME = "rankingJob";
    private static final String STEP_WEEKLY_TASK_NAME = "weeklyRankingTask";
    private static final String STEP_MONTHLY_TASK_NAME = "monthlyRankingTask";

    private final JobRepository jobRepository;
    private final JobListener jobListener;
    private final StepMonitorListener stepMonitorListener;

    private final WeeklyRankingTasklet weeklyRankingTasklet;
    private final MonthlyRankingTasklet monthlyRankingTasklet;

    @Bean(JOB_NAME)
    public Job rankingJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(weeklyRankingStep())
                .next(monthlyRankingStep())
                .listener(jobListener)
                .build();
    }

    @JobScope
    @Bean(STEP_WEEKLY_TASK_NAME)
    public Step weeklyRankingStep() {
        return new StepBuilder(STEP_WEEKLY_TASK_NAME, jobRepository)
                .tasklet(weeklyRankingTasklet, new ResourcelessTransactionManager())
                .listener(stepMonitorListener)
                .build();
    }

    @JobScope
    @Bean(STEP_MONTHLY_TASK_NAME)
    public Step monthlyRankingStep() {
        return new StepBuilder(STEP_MONTHLY_TASK_NAME, jobRepository)
                .tasklet(monthlyRankingTasklet, new ResourcelessTransactionManager())
                .listener(stepMonitorListener)
                .build();
    }
}
