# Offering Stream Consumer

## Requirements 
- java 8+

## Run application on localhost (profile)
```bash
mvn -f offering-stream-consumer-main/pom.xml spring-boot:run -Dspring.config.location=local -Dmaven.test.skip=true -Dserver.port=8081
```

Considerations>?
// In case Streams processing fails, the default case is to bring the application down, which should cause the cluster to
// relaunch a new application instance. Therefore, this automates the "when in doubt, reboot" methodology.
// Shut down the application from inside a background thread. The shutDownHook above calls kafkaStreams.close().
// KafkaStreams.close() waits for all stream threads to exit. This leads to a deadlock because the uncaughtExceptionHandler
// is called from a stream thread. Calling close() with a timeout should in theory prevent the deadlock, but it didn't work
// in practice. This does.
