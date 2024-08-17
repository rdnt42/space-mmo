package integration.setup;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class KafkaReadWriter<V, RV> {
    private final KafkaProducer<String, V> producer;
    private final KafkaConsumer<String, RV> consumer;
    private final ConcurrentLinkedDeque<RV> messages = new ConcurrentLinkedDeque<>();

    private final String produceTopic;
    private final String receiveTopic;
    private boolean enabled = false;


    public KafkaReadWriter(Map<String, String> configs, String produceTopic, String receiveTopic, Class<RV> clazz) {
        this.produceTopic = produceTopic;
        this.receiveTopic = receiveTopic;

        Properties properties = new Properties();
        properties.putAll(configs);
        properties.setProperty("group.id", "test");
        properties.setProperty("enable.auto.commit", "true");
        properties.setProperty("auto.commit.interval.ms", "1000");

        var keySend = new StringSerializer();
        var valueSend = new KafkaJsonSerializer<V>();
        this.producer = new KafkaProducer<>(properties, keySend, valueSend);

        var keyReceive = new StringDeserializer();
        var valueReceive = new KafkaJsonDeserializer<>(clazz);

        this.consumer = new KafkaConsumer<>(properties, keyReceive, valueReceive);

        init();
    }

    public void init() {
        consumer.subscribe(List.of(receiveTopic));
        enabled = true;
        Runnable task = () -> {
            while (enabled) {
                ConsumerRecords<String, RV> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, RV> record : records)
                    messages.add(record.value());
            }
            consumer.close();
        };
        Thread thread = new Thread(task);
        thread.start();
    }


    public void clear() {
        messages.clear();
    }

    public void close() {
        enabled = false;
        producer.close();
    }

    public void produce(V value) {
        ProducerRecord<String, V> record = new ProducerRecord<>(produceTopic, "test.key", value);
        producer.send(record);
    }

    public RV receive() {
        return receive(5, SECONDS);
    }

    public RV receive(long timeout, TimeUnit unit) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < unit.toMillis(timeout)) {
            RV message;
            if ((message = getMessage()) != null) {
                return message;
            }
            try {
                Thread.sleep(100); // Задержка перед следующей проверкой
            } catch (Exception ignored) {
            }
        }

        throw new RuntimeException("Could not receive message from topic " + receiveTopic);
    }

    public RV produceAndReceive(V value) {
        produce(value);

        return receive();
    }

    private RV getMessage() {
        return messages.pollFirst();
    }
}
