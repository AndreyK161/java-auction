package ws.academy.auction.api.dto.rs.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessageRs {
    private boolean success;
    private String message;
}
