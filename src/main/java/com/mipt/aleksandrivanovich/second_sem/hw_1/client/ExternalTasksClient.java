package com.mipt.aleksandrivanovich.second_sem.hw_1.client;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.ExternalTaskDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.exception.ExternalApiException;
import com.mipt.aleksandrivanovich.second_sem.hw_1.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ExternalTasksClient {

    private static final Logger logger = LoggerFactory.getLogger(ExternalTasksClient.class);

    private final RestClient restClient;

    public ExternalTasksClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ExternalTaskDto createTask(String title, boolean completed) {
        Map<String, Object> body = Map.of(
            "title", title,
            "completed", completed
        );

        try {
            ResponseEntity<Void> response = restClient.post()
                .uri("/external/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(status -> status.is2xxSuccessful(), (req, res) -> {})
                .toBodilessEntity();

            String location = response.getHeaders().getFirst("Location");
            if (location != null) {
                String id = location.replaceAll(".*/", "");
                return getTask(Long.parseLong(id));
            }
            return null;
        } catch (RestClientResponseException e) {
            handleErrorResponse(e);
            throw new ExternalApiException("Failed to create task", e);
        }
    }

    public ExternalTaskDto getTask(Long id) {
        try {
            return restClient.get()
                .uri("/external/v1/tasks/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.is2xxSuccessful(), (req, res) -> {})
                .body(ExternalTaskDto.class);
        } catch (RestClientResponseException e) {
            if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                handleNotFound(e);
            }
            handleErrorResponse(e);
            throw new ExternalApiException("Failed to get task " + id, e);
        }
    }

    public List<ExternalTaskDto> getTasks(Boolean completed, int limit) {
        try {
            return restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/external/v1/tasks")
                    .queryParamIfPresent("completed", Optional.ofNullable(completed).map(Object::toString))
                    .queryParam("limit", limit)
                    .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.is2xxSuccessful(), (req, res) -> {})
                .body(new org.springframework.core.ParameterizedTypeReference<List<ExternalTaskDto>>() {});
        } catch (RestClientResponseException e) {
            handleErrorResponse(e);
            throw new ExternalApiException("Failed to get tasks", e);
        }
    }

    public void deleteTask(Long id) {
        try {
            restClient.delete()
                .uri("/external/v1/tasks/{id}", id)
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientResponseException e) {
            if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                handleNotFound(e);
            }
            handleErrorResponse(e);
            throw new ExternalApiException("Failed to delete task " + id, e);
        }
    }

    private void handleNotFound(RestClientResponseException e) {
        try {
            ProblemDetail problemDetail = e.getResponseBodyAs(ProblemDetail.class);
            String detail = Optional.ofNullable(problemDetail)
                .map(ProblemDetail::getDetail)
                .orElse("Task not found");
            throw new TaskNotFoundException(detail);
        } catch (Exception ex) {
            throw new TaskNotFoundException("Task not found");
        }
    }

    private void handleErrorResponse(RestClientResponseException e) {
        int rawStatusCode = e.getRawStatusCode();
        HttpStatus status = HttpStatus.valueOf(rawStatusCode);

        if (status.is5xxServerError()) {
            logger.error("External API error: {} - {}", status, e.getMessage());
            throw new ExternalApiException("External API error: " + status);
        }

        String contentType = e.getResponseHeaders().getContentType() != null
            ? e.getResponseHeaders().getContentType().toString()
            : "unknown";

        if (!contentType.contains("application/json")) {
            logger.warn("Unexpected content type from external API: {}", contentType);
            String body = e.getResponseBodyAsString();
            if (body.length() > 100) {
                body = body.substring(0, 100) + "...";
            }
            logger.warn("Response body (truncated): {}", body);
        }
    }
}