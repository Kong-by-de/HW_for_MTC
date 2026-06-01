package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskStatisticsService {

    private final JdbcTemplate jdbcTemplate;

    public TaskStatisticsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TaskPriorityCount> getTasksCountByPriority() {
        String sql = "SELECT priority, COUNT(*) as count FROM task GROUP BY priority";

        return jdbcTemplate.query(sql, new TaskPriorityCountRowMapper());
    }

    private static class TaskPriorityCountRowMapper implements RowMapper<TaskPriorityCount> {
        @Override
        public TaskPriorityCount mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaskPriorityCount(
                rs.getString("priority"),
                rs.getLong("count")
            );
        }
    }

    public static class TaskPriorityCount {
        private String priority;
        private Long count;

        public TaskPriorityCount(String priority, Long count) {
            this.priority = priority;
            this.count = count;
        }

        public String getPriority() { return priority; }
        public Long getCount() { return count; }
    }
}