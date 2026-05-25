package ws.academy.auction.core.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ws.academy.auction.api.dto.AuctionStatus;
import ws.academy.auction.api.dto.LotBid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuctionsIntegrationTests extends BaseIntegrationTest {

    @BeforeAll
    public static void setUp() {
        //todo заполнить тестовую БД данными (можно использовать миграции, JDBC, Hibernate, JOOQ)
        //todo сама БД создается в BaseIntegrationTest при помощи testcontainers
    }

    @Test
    @DisplayName("Получение перечня завершенных торгов")
    void completedAuctionsListShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(get("/api/v1/auctions/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Создание торгов")
    void auctionShouldBeSuccessfullyCreated() throws Exception {
        String startDate = "2025-01-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expectedStartDate = LocalDate.parse(startDate, formatter);

        mockMvc.perform(post("/api/v1/auctions/{date}", startDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.status").value(AuctionStatus.CREATED))
                .andExpect(jsonPath("$.startDateTime").value(expectedStartDate.toString()));
    }

    @Test
    @DisplayName("Старт торгов")
    void auctionShouldBeSuccessfullyStarted() throws Exception {
        mockMvc.perform(post("/api/v1/auctions/1/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.status").value(AuctionStatus.STARTED));
    }

    @Test
    @DisplayName("Завершение торгов")
    void auctionShouldBeSuccessfullyCompleted() throws Exception {
        mockMvc.perform(post("/api/v1/auctions/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.status").value(AuctionStatus.COMPLETED));
    }

    @Test
    @DisplayName("Получение информации о торгах")
    void auctionInfoShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(get("/api/v1/auctions/1/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Добавление лотов в торги")
    void auctionLotsShouldBeSuccessfullyAdded() throws Exception {
        Set<Integer> lotsId = Set.of(1,2,3);

        mockMvc.perform(post("/api/v1/auctions/1/lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(lotsId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.lots").isArray())
                .andExpect(jsonPath("$.lots").isNotEmpty())
                .andExpect(jsonPath("$.lots").value(hasItems(lotsId)));
    }

    @Test
    @DisplayName("Получение перечня лотов, участвующих в торгах")
    void auctionLotsListShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(get("/api/v1/auctions/1/lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Старт торгов по лоту")
    void lotBiddingShouldBeSuccessfullyStarted() throws Exception {
        mockMvc.perform(post("/api/v1/auctions/1/lot/1/start"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Совершение ставки на лот - ставка должна быть успешно принята")
    void betShouldBeSuccessfullyAccepted() throws Exception {
        LotBid lotBid = LotBid.builder()
                .amount(1000)
                .participantEmail("test@test.ru")
                .build();
        Integer lotId = 1;

        mockMvc.perform(post("/api/v1/auctions/1/lot/{lotId}/bid", lotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(lotBid))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lotId").value(lotId))
                .andExpect(jsonPath("$.lotBidList").isArray())
                .andExpect(jsonPath("$.lotBidList").isNotEmpty());
    }

    @Test
    @DisplayName("Получение списка ставок по лоту")
    void lotBidsListShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(post("/api/v1/auctions/1/lot/1/bid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Получение списка участников, зарегистрированных на торги")
    void registeredParticipantListShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(post("/api/v1/auctions/1/participants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Получение \"табло\" по торгам")
    void auctionBiddingBoardShouldBeSuccessfullyReceived() throws Exception {
        mockMvc.perform(get("/api/v1/auctions/1/bidding/board"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.biddingList").isArray())
                .andExpect(jsonPath("$.biddingList").isNotEmpty());
    }
}
