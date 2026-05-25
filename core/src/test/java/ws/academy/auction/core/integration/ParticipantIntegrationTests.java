package ws.academy.auction.core.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ws.academy.auction.api.dto.rq.participants.ParticipateRq;
import ws.academy.auction.api.dto.rq.participants.RegisterParticipantRq;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ParticipantIntegrationTests extends BaseIntegrationTest {

    private final String participantEmail = "test@test.ru";

    @BeforeAll
    public static void setUp() {
        //todo заполнить тестовую БД данными (можно использовать миграции, JDBC, Hibernate, JOOQ)
        //todo сама БД создается в BaseIntegrationTest при помощи testcontainers
    }

    @Test
    @DisplayName("Регистрация участника")
    void participantRegistrationShouldBeSuccessful() throws Exception {
        String email = "test123@test.ru";
        String userName = "TestUser123";

        RegisterParticipantRq registerParticipantRq = RegisterParticipantRq.builder()
                .email(email)
                .userName(userName)
                .build();

        mockMvc.perform(post("/api/v1/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(registerParticipantRq))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.userName").value(userName));
    }

    @Test
    @DisplayName("Получение списка зарегистрированных участников")
    void registeredParticipantListShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(get("/api/v1/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("page", 1)
                        .requestAttr("count", 10)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.participants").isArray())
                .andExpect(jsonPath("$.participants").isNotEmpty());
    }

    @Test
    @DisplayName("Получение участника по email")
    void participantShouldBeSuccessfullyReceivedByEmail() throws Exception {
        String email = "test321@test.ru";
        String userName = "TestUser321";

        RegisterParticipantRq registerParticipantRq = RegisterParticipantRq.builder()
                .email(email)
                .userName(userName)
                .build();

        mockMvc.perform(post("/api/v1/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(registerParticipantRq))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.userName").value(userName));

        mockMvc.perform(get("/api/v1/participants/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.userName").value(userName));
    }

    @Test
    @DisplayName("Изменение данных участника")
    void participantDataShouldBeSuccessfullyUpdated() throws Exception {
        String email = "test111@test.ru";
        String userName = "TestUser111";

        RegisterParticipantRq registerParticipantRq = RegisterParticipantRq.builder()
                .email(email)
                .userName(userName)
                .build();

        mockMvc.perform(post("/api/v1/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(registerParticipantRq))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.userName").value(userName));

        registerParticipantRq.setUserName("UpdatedTestUser111");

        mockMvc.perform(patch("/api/v1/participants/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(registerParticipantRq))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.userName").value(userName));

        mockMvc.perform(get("/api/v1/participants/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.userName").value(userName));
    }

    @Test
    @DisplayName("Удаление участника")
    void participantShouldBeSuccessfullyDeleted() throws Exception {
        String email = "test121@test.ru";
        String userName = "TestUser121";

        RegisterParticipantRq registerParticipantRq = RegisterParticipantRq.builder()
                .email(email)
                .userName(userName)
                .build();

        mockMvc.perform(post("/api/v1/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(registerParticipantRq))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.userName").value(userName));

        mockMvc.perform(delete("/api/v1/participants/{email}", email))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/participants/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Получение истории участия в торгах")
    public void participationHistoryShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(get("/api/v1/participants/{email}/history/participations", participantEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Получение списка выигранных лотов")
    public void purchasedLotsHistoryShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(get("/api/v1/participants/{email}/history/lots", participantEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Получение данных личного счета участника")
    public void participantAccountInfoShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(get("/api/v1/participants/{email}/account", participantEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(0));
    }

    @Test
    @DisplayName("Пополнение личного счета участника")
    public void participantAccountShouldBeSuccessfullyReplenished() throws Exception {
        mockMvc.perform(post("/api/v1/participants/{email}/account/replenish", participantEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", not(equalTo(0))));
    }

    @Test
    @DisplayName("Списание средств с личного счета участника")
    public void participantAccountShouldBeSuccessfullyWithdrawal() throws Exception {
        mockMvc.perform(post("/api/v1/participants/{email}/account/withdraw", participantEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", not(equalTo(0))));
    }

    @Test
    @DisplayName("Регистрация участника на торги")
    public void participantShouldBeSuccessfullyRegisteredForAuction() throws Exception {
        ParticipateRq participateRq = ParticipateRq.builder()
                .auctionId(1)
                .depositAmount(1000)
                .build();

        mockMvc.perform(post("/api/v1/participants/{email}/participate", participantEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(participateRq))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantEmail").value(participantEmail));
    }
}
