package com.lodenrogue.teachertools.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodenrogue.teachertools.model.Registration;
import com.lodenrogue.teachertools.model.Student;

public class RegistrationFacade extends AbstractFacade<Registration> {

	public RegistrationFacade() {
		super(Registration.class);
	}

	public List<Registration> findAllByGroup(long groupId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("groupId", groupId);
		return findAllFromQuery("FROM Registration WHERE groupId = :groupId", parameters);
	}
	
	public List<Student> findStudentsByGroup(long groupId) {
		List<Student> students = new ArrayList<Student>();
		List<Registration> registrations = findAllByGroup(groupId);
		for (Registration reg : registrations) {
			students.add(new StudentFacade().find(reg.getStudentId()));
		}
		return students;
	}

}
