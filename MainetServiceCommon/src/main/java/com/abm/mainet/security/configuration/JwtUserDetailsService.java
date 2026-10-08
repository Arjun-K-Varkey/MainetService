package com.abm.mainet.security.configuration;

import java.util.ArrayList;

import com.abm.mainet.common.domain.Employee;
import com.abm.mainet.common.master.repository.EmployeeJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Anwarul.Hassan
 * @since 01-Dec-2023
 */

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private EmployeeJpaRepository employeeRepository;

	boolean accountNonExpired;
	boolean credentialsNonExpired;
	boolean accountNonLocked;
	boolean enabled;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeRepository.findEmployeeByLoginName(username);
		if (employee != null && !"0".equals(employee.getIsDeleted())) {
			enabled = true;
			return new User(employee.getEmploginname(), employee.getEmppassword(), enabled, accountNonExpired,
					credentialsNonExpired, accountNonLocked, new ArrayList<>());
		}
		if (employee != null) {
			return new User(employee.getEmploginname(), employee.getEmppassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
