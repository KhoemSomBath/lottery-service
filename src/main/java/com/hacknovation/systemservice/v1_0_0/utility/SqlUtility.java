package com.hacknovation.systemservice.v1_0_0.utility;


import com.hacknovation.systemservice.constant.SqlConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Sombath
 * create at 15/6/22 11:40 PM
 */

@Service
@RequiredArgsConstructor
public class SqlUtility {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    private final List<Class<?>> singleColumnDataType = List.of(String.class, Long.class, Integer.class, Date.class);


    // ======================================================== Void ============================================================ //

    public void executeQuery(Resource resource){
        String query = readFile(resource);
        jdbcTemplate.execute(query);
    };

    public void executeQuery(String query){
//        String query = readFile(resource);
        jdbcTemplate.execute(query);
    };

    public void executeQuery(Resource resource, Map<String, Object> parameters){
        String query = readFile(resource);
        namedParameterJdbcTemplate.update(query, parameters);
    };


    // ======================================================== Paginate ============================================================ //

    public <T> Page<T> executeQueryForPage(Resource resource, Pageable page, Map<String, Object> parameters, Class<T> type) {
        String query = readFile(resource);
        String countQuery = getCountQuery(query);
        query = getPaginateQuery(query, page);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);

        List<T> data = this.executeQueryForList(query, sqlParameterSource, type);
        Integer totalRow = this.executeQueryForObject(Integer.class, countQuery, sqlParameterSource);

        if (totalRow == null)
            totalRow = 0;

        return new PageImpl<>(data, page, totalRow);
    }

    public <T> Page<T> executeQueryForPage(String query, Pageable page, Map<String, Object> parameters, Class<T> type) {
        String countQuery = getCountQuery(query);
        query = getPaginateQuery(query, page);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);

        List<T> data = this.executeQueryForList(query, sqlParameterSource, type);
        Integer totalRow = this.executeQueryForObject(Integer.class, countQuery, sqlParameterSource);

        if (totalRow == null)
            totalRow = 0;

        return new PageImpl<>(data, page, totalRow);
    }

    public <T> Page<T> executeQueryForPage(String query, String countQuery, Pageable page, Map<String, Object> parameters, Class<T> type) {
        query = getPaginateQuery(query, page);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);

        List<T> data = this.executeQueryForList(query, sqlParameterSource, type);
        Integer totalRow = this.executeQueryForObject(Integer.class, countQuery, sqlParameterSource);

        if (totalRow == null)
            totalRow = 0;

        return new PageImpl<>(data, page, totalRow);
    }

    public <T> Page<T> executeQueryForPage(Resource resource, Resource countResource, Pageable page, Map<String, Object> parameters, Class<T> type) {
        String query = readFile(resource);
        String countQuery = readFile(countResource);
        query = getPaginateQuery(query, page);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);

        List<T> data = this.executeQueryForList(query, sqlParameterSource, type);
        Integer totalRow = this.executeQueryForObject(Integer.class, countQuery, sqlParameterSource);

        if (totalRow == null)
            totalRow = 0;

        return new PageImpl<>(data, page, totalRow);
    }

    public <T> Page<T> executeQueryForPage(String query, Pageable page, Map<String, Object> parameters, String countColumn, Class<T> type) {
        String countQuery = getCountQuery(query, countColumn);
        query = getPaginateQuery(query, page);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);

        List<T> data = this.executeQueryForList(query, sqlParameterSource, type);
        Integer totalRow = this.executeQueryForObject(Integer.class, countQuery, sqlParameterSource);

        if (totalRow == null)
            totalRow = 0;

        return new PageImpl<>(data, page, totalRow);
    }


    // ========================================================= List ============================================================== //
    public <T> List<T> executeQueryForList(Resource resource, Class<T> type) {
        String query = readFile(resource);
        return this.executeQueryForList(query, type);
    }

    public <T> List<T> executeQueryForList(Resource resource, Map<String, Object> parameters, Class<T> type) {
        String query = readFile(resource);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);
        return this.executeQueryForList(query, sqlParameterSource, type);
    }

    public <T> List<T> executeQueryForList(String query, Map<String, Object> parameters, Class<T> type) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);
        return this.executeQueryForList(query, sqlParameterSource, type);
    }

    // ================================================ Integer, String, Long Object... =================================================== //

    public <T> T executeQueryForObject(Resource resource, Map<String, Object> parameters, Class<T> type) {
        String query = readFile(resource);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);
        return this.executeQueryForObject(type, query, sqlParameterSource);
    }

    public <T> T executeQueryForObject(String query, Map<String, Object> parameters, Class<T> type) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parameters);
        return this.executeQueryForObject(type, query, sqlParameterSource);
    }

    public <T> T executeQueryForObject(Resource resource, Class<T> type) {
        String query = readFile(resource);
        return this.executeQueryForObject(type, query);
    }

    public <T> T executeQueryForObject(String query, Class<T> type) {
        return this.executeQueryForObject(type, query);
    }

    // ========================================================= JdbcTemplate ======================================================= //

    public  <T> List<T> executeQueryForList(String query, Class<T> type) {
        if (singleColumnDataType.contains(type))
            return jdbcTemplate.queryForList(query, type);
        var rowMapper = BeanPropertyRowMapper.newInstance(type);
        return jdbcTemplate.query(query, rowMapper);
    }

    private <T> T executeQueryForObject(Class<T> type, String query) {
        try {
            if (singleColumnDataType.contains(type))
                return jdbcTemplate.queryForObject(query, type);

            var rowMapper = BeanPropertyRowMapper.newInstance(type);
            return jdbcTemplate.queryForObject(query, rowMapper);
        } catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    // ================================================= NamedParameterJdbcTemplate=================================================== //

    private <T> List<T> executeQueryForList(String query, SqlParameterSource sqlParameterSource, Class<T> type) {
        if (singleColumnDataType.contains(type))
            return namedParameterJdbcTemplate.queryForList(query, sqlParameterSource, type);
        var rowMapper = BeanPropertyRowMapper.newInstance(type);
        return namedParameterJdbcTemplate.query(query, sqlParameterSource, rowMapper);
    }

    private <T> T executeQueryForObject(Class<T> type, String query, SqlParameterSource sqlParameterSource) {
        if (singleColumnDataType.contains(type))
            return namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, type);
        var rowMapper = BeanPropertyRowMapper.newInstance(type);
        return namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, rowMapper);
    }

    // =================================================== helper function =========================================================== //

    public static Resource getResource(Resource[] resources, String fileName) {
        for (Resource resource : resources) {
            if (Objects.equals(resource.getFilename(), fileName))
                return resource;
        }
        return null;
    }

    public String getQuery(Resource resource, Map<String, String> forReplace) {
        if(resource == null)
            return SqlConstant.EMPTY_STRING;
        return readFileAndReplace(resource, forReplace);
    }

    public String getQuery(Resource resource) {
        if(resource == null)
            return SqlConstant.EMPTY_STRING;
        return this.readFile(resource);
    }

    private String readFileAndReplace(Resource resource, Map<String, String> forReplace) {
        String query = readFile(resource);
        for(String key: forReplace.keySet()){
            query = query.replaceAll(key, forReplace.get(key));
        }
        return query;
    }

    private String readFile(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String getCountQuery(String query) {
        String _query = query.toLowerCase();
        int startIndex = _query.indexOf(SqlConstant.FROM);
        int endIndex = _query.lastIndexOf(SqlConstant.GROUP);
        if (endIndex < 0) endIndex = _query.lastIndexOf(SqlConstant.ORDER);
        if (endIndex < 0) endIndex = _query.length();
        return SqlConstant.COUNT_QUERY.concat(query.substring(startIndex, endIndex));
    }

    // when user want to count on actually column
    private String getCountQuery(String query, String countOnColumn) {
        String _query = query.toLowerCase();
        int startIndex = _query.indexOf(SqlConstant.FROM);
        int endIndex = _query.lastIndexOf(SqlConstant.GROUP);
        if (endIndex < 0) endIndex = _query.lastIndexOf(SqlConstant.ORDER);
        if (endIndex < 0) endIndex = _query.length();
        return SqlConstant.COUNT_DISTINCT_QUERY.replace("*", countOnColumn).concat(query.substring(startIndex, endIndex));
    }

    private String getPaginateQuery(String query, Pageable page) {
        query = query.trim();
        if (query.endsWith(";")) {
            query = query.replaceAll(";", "");
        }

        query = query.concat(SqlConstant.LIMIT).concat(String.valueOf(page.getPageSize()));
        query = query.concat(SqlConstant.OFFSET).concat(String.valueOf(page.getOffset()));

        return query;
    }

}
