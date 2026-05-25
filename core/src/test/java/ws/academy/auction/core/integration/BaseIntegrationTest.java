package ws.academy.auction.core.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import ws.academy.auction.core.repository.LotRepository;
import ws.academy.auction.core.repository.ParticipantRepository;
import ws.academy.auction.core.repository.PhotoRepository;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
public class BaseIntegrationTest {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @ServiceConnection
    protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest")
    );

    @ServiceConnection
    protected static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer(
            DockerImageName.parse("rabbitmq:3-management")
    );

    static {
        postgreSQLContainer.start();
        rabbitMqContainer.start();
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected LotRepository lotRepository;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected PhotoRepository photoRepository;
}
