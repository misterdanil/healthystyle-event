package org.healthystyle.event.repository.impl;

import java.util.Set;

import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.repository.CustomUserEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CustomUserEventRepositoryImpl implements CustomUserEventRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public void addRoles(Set<Role> roles, Long id) {
		System.out.println("here");
		String sql = "INSERT INTO user_event_role (user_event_id, role_id) values (:user_event_id, :role_id)";

		MapSqlParameterSource[] params = roles.stream().map(role -> {
			MapSqlParameterSource s = new MapSqlParameterSource();
			s.addValue("user_event_id", id);
			s.addValue("role_id", role.getId());
			return s;
		}).toArray(MapSqlParameterSource[]::new);

		jdbcTemplate.batchUpdate(sql, params);
	}

}
