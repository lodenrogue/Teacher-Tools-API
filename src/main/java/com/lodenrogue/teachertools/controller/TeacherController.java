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
import com.lodenrogue.teachertools.model.Teacher;
import com.lodenrogue.teachertools.service.GroupFacade;
import com.lodenrogue.teachertools.service.TeacherFacade;

import io.swagger.annotations.Api;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@Api(value="Teachers")
public class TeacherController {

	/**
	 * Create a teacher
	 * 
	 * @param teacher
	 * @return
	 */
	@RequestMapping(path = "/teachers", method = RequestMethod.POST)
	public ResponseEntity<Object> createTeacher(@RequestBody Teacher teacher) {

		// Get missing fields
		List<String> missingFields = getMissingFields(teacher);

		// Check missing fields
		if (missingFields.size() > 0) {
			MissingFieldsError mfe = new MissingFieldsError(missingFields);
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			ResponseEntity<Object> response = new ResponseEntity<Object>(mfe, status);
			return response;
		}

		// Create and return teacher
		else {
			teacher.setCreatedDate(Calendar.getInstance());
			teacher = new TeacherFacade().create(teacher);
			HttpStatus status = HttpStatus.CREATED;
			ResponseEntity<Object> response = new ResponseEntity<Object>(teacher, status);
			return response;
		}
	}

	/**
	 * Get a teacher by id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/teachers/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getTeacher(@PathVariable long id) {

		// Get teacher
		Teacher teacher = new TeacherFacade().find(id);

		// Check if teacher exists
		if (teacher == null) {
			return notFound(id);
		}

		// Return teacher
		else {
			HttpStatus status = HttpStatus.OK;
			ResponseEntity<Object> response = new ResponseEntity<Object>(teacher, status);
			return response;
		}
	}

	/**
	 * Update existing teacher with given id
	 * 
	 * @param id
	 * @param teacher
	 * @return
	 */
	@RequestMapping(path = "/teachers/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateTeacher(@PathVariable long id, @RequestBody Teacher teacher) {

		// Check that teacher with that id exists
		Teacher existing = new TeacherFacade().find(id);
		if (existing == null) {
			return notFound(id);
		}

		// Get missing fields
		List<String> missingFields = getMissingFields(teacher);
		if (teacher.getCreatedDate() == null) {
			missingFields.add("createdDate");
		}

		// Check missing fields
		if (missingFields.size() > 0) {
			MissingFieldsError mfe = new MissingFieldsError(missingFields);
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			ResponseEntity<Object> response = new ResponseEntity<Object>(mfe, status);
			return response;
		}

		// Update and return teacher
		else {
			teacher.setEntityId(id);
			teacher.setModifiedDate(Calendar.getInstance());
			teacher = new TeacherFacade().update(teacher);
			HttpStatus status = HttpStatus.OK;
			ResponseEntity<Object> response = new ResponseEntity<Object>(teacher, status);
			return response;
		}
	}

	/**
	 * Delete teacher with matching id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/teachers/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteTeacher(@PathVariable long id) {
		new TeacherFacade().delete(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(path = "/teachers/{id}/groups", method = RequestMethod.GET)
	public ResponseEntity<Object> getGroups(@PathVariable long id) {

		// Check that teacher exists
		Teacher teacher = new TeacherFacade().find(id);
		if (teacher == null) {
			return notFound(id);
		}
		else {
			List<Group> groups = new GroupFacade().findAllByTeacher(id);
			HttpStatus status = HttpStatus.OK;
			ResponseEntity<Object> response = new ResponseEntity<Object>(groups, status);
			return response;
		}
	}

	@RequestMapping(path = "/teachers", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllTeachers() {
		List<Teacher> teachers = new TeacherFacade().findAll();
		return new ResponseEntity<Object>(teachers, HttpStatus.OK);
	}

	/**
	 * Get and return a list of missing fields for teacher
	 * 
	 * @param teacher
	 * @return
	 */
	private List<String> getMissingFields(Teacher teacher) {
		List<String> missingFields = new ArrayList<String>();
		if (teacher.getFirstName() == null) missingFields.add("firstName");
		if (teacher.getLastName() == null) missingFields.add("lastName");
		return missingFields;
	}

	/**
	 * Create and return a response entity to let the user know a resource
	 * with this id was not found
	 * 
	 * @param id
	 * @return
	 */
	private ResponseEntity<Object> notFound(long id) {
		ErrorMessage msg = new ErrorMessage("No teacher with id " + id + " found");
		HttpStatus status = HttpStatus.NOT_FOUND;
		ResponseEntity<Object> response = new ResponseEntity<Object>(msg, status);
		return response;
	}
}
