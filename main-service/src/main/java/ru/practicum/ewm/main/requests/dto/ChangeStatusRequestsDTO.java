package ru.practicum.ewm.main.requests.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ChangeStatusRequestsDTO {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private EventRequestStatus status;
}
