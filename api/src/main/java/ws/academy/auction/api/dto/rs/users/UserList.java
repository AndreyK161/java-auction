package ws.academy.auction.api.dto.rs.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.ListPageData;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для списка пользователей")
public class UserList {
    @Schema(description = "Список пользователей")
    private List<UserListItem> items;

    @Schema(description = "Данные постраничной навигации")
    private ListPageData page;
}
