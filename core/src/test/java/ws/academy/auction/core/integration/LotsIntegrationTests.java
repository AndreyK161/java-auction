package ws.academy.auction.core.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import ws.academy.auction.api.dto.rq.CreateLotRq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Интеграционные тесты лотов")
public class LotsIntegrationTests extends BaseIntegrationTest {

    @Test
    @WithMockUser
    @DisplayName("Создание нового лота без инициализации обязательных полей - должна вернуться ошибка")
    void createLotShouldReturnInternalServerErrorResponse() throws Exception {
        CreateLotRq createLotRq = CreateLotRq.builder().build();

        mockMvc.perform(post("/api/v0/lots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(createLotRq))
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("Получение лота по идентификатору - должна вернуться ошибка 404, так как в БД пусто")
    void getLotByIdShouldReturnNotFoundResponse() throws Exception {
        mockMvc.perform(get("/api/v0/lots/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Лот не найден: id = 1"));
    }

    @Test
    @WithMockUser
    @DisplayName("Создание нового лота и его получение по идентификатору - " +
            "лот должен успешно сохраниться в БД и быть доступен по идентификатору")
    void createLotAndGetLotByIdShouldSuccessfullySavedAndRetrievedFromDB() throws Exception {
        CreateLotRq createLotRq = CreateLotRq.builder()
                .name("Первый лот")
                .description("Самый первый лот")
                .startPrice(100L)
                .build();

        MvcResult result = mockMvc.perform(post("/api/v0/lots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(createLotRq))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Первый лот"))
                .andExpect(jsonPath("$.description").value("Самый первый лот"))
                .andExpect(jsonPath("$.startPrice").value(100))
                .andExpect(jsonPath("$.deleted").value(false))
                .andReturn();

        Integer lotId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(get("/api/v0/lots/{id}", lotId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Первый лот"))
                .andExpect(jsonPath("$.description").value("Самый первый лот"))
                .andExpect(jsonPath("$.startPrice").value(100))
                .andExpect(jsonPath("$.deleted").value(false));
    }
}
