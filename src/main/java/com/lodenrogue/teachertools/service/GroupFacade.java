package com.lodenrogue.teachertools.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodenrogue.teachertools.model.Group;

public class GroupFacade extends AbstractFacade<Group> {

	public GroupFacade() {
		super(Group.class);
	}

	public List<Group> findAllByTeacher(long teacherId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("teacherId", teacherId);
		return findAllFromQuery("FROM Group WHERE teacherId = :teacherId", parameters);
	}
}
