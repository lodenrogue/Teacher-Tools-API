package com.lodenrogue.teachertools.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lodenrogue.teachertools.error.ErrorMessage;
import com.lodenrogue.teachertools.error.MissingFieldsError;
import com.lodenrogue.teachertools.model.Group;
import com.lodenrogue.teachertools.model.Student;
import com.lodenrogue.teachertools.model.Teacher;
import com.lodenrogue.teachertools.service.GroupFacade;
import com.lodenrogue.teachertools.service.RegistrationFacade;
import com.lodenrogue.teachertools.service.TeacherFacade;

import io.swagger.annotations.Api;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@Api(value="Groups")
public class GroupController {

	/**
	 * Create a group
	 * 
	 * @param group
	 * @return
	 */
	@RequestMapping(path = "/groups", method = RequestMethod.POST)
	public ResponseEntity<Object> createGroup(@RequestBody Group group) {

		// Get missing fields
		List<String> missingFields = getMissingFields(group);

		// Check missing fields
		if (missingFields.size() > 0) {
			MissingFieldsError mfe = new MissingFieldsError(missingFields);
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			return new ResponseEntity<Object>(mfe, status);
		}

		// Check if teacher exists
		long teacherId = group.getTeacherId();
		Teacher teacher = new TeacherFacade().find(teacherId);
		if (teacher == null) {
			ErrorMessage msg = new ErrorMessage("No teacher with id " + teacherId + " found");
			HttpStatus status = HttpStatus.NOT_FOUND;
			ResponseEntity<Object> response = new ResponseEntity<Object>(msg, status);
			return response;
		}

		// Create and return group
		group.setCreatedDate(Calendar.getInstance());
		group = new GroupFacade().create(group);
		HttpStatus status = HttpStatus.CREATED;
		ResponseEntity<Object> response = new ResponseEntity<Object>(group, status);
		return response;
	}

	/**
	 * Get group by id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/groups/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getGroup(@PathVariable long id) {

		// Get group
		Group group = new GroupFacade().find(id);

		// Check if group exists
		if (group == null) {
			return createNotFound(id);
		}

		// Return group
		else {
			HttpStatus status = HttpStatus.OK;
			ResponseEntity<Object> response = new ResponseEntity<Object>(group, status);
			return response;
		}
	}

	/**
	 * Update group with matching id
	 * 
	 * @param id
	 * @param group
	 * @return
	 */
	@RequestMapping(path = "/groups/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateGroup(@PathVariable long id, @RequestBody Group group) {

		// Check that a group with that id exists
		Group existing = new GroupFacade().find(id);
		if (existing == null) {
			ErrorMessage msg = new ErrorMessage("No group with id " + id + " found");
			HttpStatus status = HttpStatus.NOT_FOUND;
			ResponseEntity<Object> response = new ResponseEntity<Object>(msg, status);
			return response;
		}

		// Get missing fields
		List<String> missingFields = getMissingFields(group);
		if (group.getCreatedDate() == null) {
			missingFields.add("createdDate");
		}

		// Check missing fields
		if (missingFields.size() > 0) {
			MissingFieldsError mse = new MissingFieldsError(missingFields);
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			ResponseEntity<Object> response = new ResponseEntity<Object>(mse, status);
			return response;
		}

		// Check that this group's teacher exists
		long teacherId = group.getTeacherId();
		Teacher existingTeacher = new TeacherFacade().find(teacherId);
		if (existingTeacher == null) {
			ErrorMessage msg = new ErrorMessage("No teacher with id " + teacherId + " found");
			HttpStatus status = HttpStatus.NOT_FOUND;
			ResponseEntity<Object> response = new ResponseEntity<Object>(msg, status);
			return response;
		}

		// Update and return group
		group.setEntityId(id);
		group.setModifiedDate(Calendar.getInstance());
		group = new GroupFacade().update(group);
		HttpStatus status = HttpStatus.OK;
		ResponseEntity<Object> response = new ResponseEntity<Object>(group, status);
		return response;
	}

	/**
	 * Delete group matching id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/groups/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteGroup(@PathVariable long id) {
		new GroupFacade().delete(id);
		ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.OK);
		return response;
	}

	/**
	 * Returns a list of students registered to the group matching the given
	 * id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/groups/{id}/students", method = RequestMethod.GET)
	public ResponseEntity<Object> getStudents(@PathVariable long id) {

		// Check that the group exists
		Group existing = new GroupFacade().find(id);
		if (existing == null) {
			return createNotFound(id);
		}
		else {
			List<Student> students = new RegistrationFacade().findStudentsByGroup(id);
			HttpStatus status = HttpStatus.OK;
			return new ResponseEntity<Object>(students, status);
		}
	}

	/**
	 * Creates and returns a 404 not found response
	 * 
	 * @param id
	 * @return
	 */
	private ResponseEntity<Object> createNotFound(long id) {
		ErrorMessage msg = new ErrorMessage("No group with id " + id + " found");
		HttpStatus status = HttpStatus.NOT_FOUND;
		return new ResponseEntity<Object>(msg, status);
	}

	private List<String> getMissingFields(Group group) {
		List<String> missingFields = new ArrayList<String>();
		if (group.getTeacherId() == null) missingFields.add("teacherId");
		if (group.getName() == null) missingFields.add("name");
		return missingFields;
	}

}
