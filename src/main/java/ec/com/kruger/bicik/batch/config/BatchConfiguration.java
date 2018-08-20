package ec.com.kruger.bicik.batch.config;

import ec.com.kruger.bicik.batch.model.RecordSO;
import ec.com.kruger.bicik.batch.model.Reserva;
import ec.com.kruger.bicik.batch.model.WriterSO;
import ec.com.kruger.bicik.batch.processor.RecordProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Date;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfiguration.class);

	@Autowired
	DataSource dataSource;
	
	@Value("${consulta.reserva.pendientes}")
	private String consultaReservaPendientes ;
	
	@Bean
	public ItemReader<Reserva> reader() {
		JdbcCursorItemReader<Reserva> reader = new JdbcCursorItemReader<>();
		reader.setSql(consultaReservaPendientes);
		reader.setDataSource(dataSource);
		reader.setRowMapper((ResultSet resultSet, int rowNum) -> {
			if (!(resultSet.isAfterLast()) && !(resultSet.isBeforeFirst())) {
				Reserva reserva = new Reserva();
				reserva.setId(resultSet.getLong("id"));
				reserva.setEstado(resultSet.getString("estado"));
				reserva.setCreateAt(resultSet.getDate("create_at"));
				LOGGER.info("RowMapper record : {}", reserva);
				return reserva;
			} else {
				LOGGER.info("Returning null from rowMapper");
				return null;
			}
		});
		return reader;
	}

	@Bean
	public ItemProcessor<RecordSO, WriterSO> processor() {
		return new RecordProcessor();
	}

	@Bean
	public ItemWriter<Reserva> writer() {
		JdbcBatchItemWriter<Reserva> writer = new JdbcBatchItemWriter<>();
		//writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setItemPreparedStatementSetter(setter());
		writer.setSql("insert into writer ( full_name, random_num) values (?,?)");
		writer.setDataSource(dataSource);
		return writer;
	}

	@Bean
	public ItemPreparedStatementSetter<Reserva> setter() {
	 return (item, ps) -> {
			//ps.setLong(1, item.getId());
			//ps.setString(1, item.getFullName());
			//ps.setString(2, item.getRandomNum());
		};
	}

	public Job job() {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer()) 
				.flow(step1()) 
				.end()
				.build(); 
		}
	 
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Reserva, Reserva>chunk(5)
				.reader(reader())
				//.processor(processor())
				.writer(new ReservaItemWriter())
				.build()
				;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private SimpleJobLauncher jobLauncher;

	@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
	public void sendSmsForExpiringBookmark() throws Exception {
		System.out.println(" Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		org.springframework.batch.core.JobExecution execution =  jobLauncher.run(job(), param);
		System.out.println("Job finished with status :" + execution.getStatus());
	}

}
