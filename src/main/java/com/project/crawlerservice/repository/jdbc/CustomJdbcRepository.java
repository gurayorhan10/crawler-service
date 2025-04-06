package com.project.crawlerservice.repository.jdbc;

import com.project.crawlerservice.dto.AssetDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
public class CustomJdbcRepository extends NamedParameterJdbcDaoSupport {

    public static final RowMapper<AssetDataDTO> ASSET_DATA_DTO_ROW_MAPPER = new BeanPropertyRowMapper<>(AssetDataDTO.class);

    public CustomJdbcRepository(DataSource dataSource){
        setDataSource(dataSource);
    }

    public List<AssetDataDTO> findAssetDataByUsername(String username){
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getNamedParameterJdbcTemplate();
        sql.append("""
                    SELECT D.NAME, A.PIECE, A.AVERAGE, A.CURRENCY AS ASSET_CURRENCY, D.DAILY_VALUE, D.CURRENCY AS DATA_CURRENCY FROM ASSET A, DATA D WHERE A.CODE = D.CODE AND A.TYPE = D.TYPE AND A.USERNAME = :username
                   """);
        params.addValue("username",username);
        return namedParameterJdbcTemplate.query(sql.toString(),params,ASSET_DATA_DTO_ROW_MAPPER);
    }

}
