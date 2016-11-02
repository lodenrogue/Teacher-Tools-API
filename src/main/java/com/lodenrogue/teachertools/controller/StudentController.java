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
import com.lodenrogue.teachertools.model.Student;
import com.lodenrogue.teachertools.service.StudentFacade;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class StudentController {

	/**
	 * Create student
	 * 
	 * @param student
	 * @return
	 */
	@RequestMapping(path = "/students", method = RequestMethod.POST)
	public ResponseEntity<Object> createStudent(@RequestBody Student student) {

		// Get missing fields
		List<String> missingFields = getMissingFields(student);

		// Check missing fields
		if (missingFields.size() > 0) {
			MissingFieldsError mfe = new MissingFieldsError(missingFields);
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			ResponseEntity<Object> response = new ResponseEntity<Object>(mfe, status);
			return response;
		}

		// Create and return student
		else {
			student.setCreatedDate(Calendar.getInstance());
			student = new StudentFacade().create(student);
			HttpStatus status = HttpStatus.CREATED;
			ResponseEntity<Object> response = new ResponseEntity<Object>(student, status);
			return response;
		}
	}

	/**
	 * Get student by id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/students/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getStudent(@PathVariable long id) {

		// Get student
		Student student = new StudentFacade().find(id);

		// Check if student exists
		if (student == null) {
			return createNotFound(id);
		}

		// Return student
		else {
			HttpStatus status = HttpStatus.OK;
			ResponseEntity<Object> response = new ResponseEntity<Object>(student, status);
			return response;
		}
	}

	/**
	 * Returns a list of all students
	 * 
	 * @return
	 */
	@RequestMapping(path = "/students", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllStudents() {
		List<Student> students = new StudentFacade().findAll();
		HttpStatus status = HttpStatus.OK;
		return new ResponseEntity<Object>(students, status);
	}

	/**
	 * Update student matching the path id
	 * 
	 * @param id
	 * @param student
	 * @return
	 */
	@RequestMapping(path = "/students/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateStudent(@PathVariable long id, @RequestBody Student student) {

		// Check that student with that id exists
		Student existing = new StudentFacade().find(id);
		if (existing == null) {
			return createNotFound(id);
		}

		// Get missing fields
		List<String> missingFields = getMissingFields(student);
		if (student.getCreatedDate() == null) {
			missingFields.add("createdDate");
		}

		// Check missing fields
		if (missingFields.size() > 0) {
			MissingFieldsError mfe = new MissingFieldsError(missingFields);
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			ResponseEntity<Object> response = new ResponseEntity<Object>(mfe, status);
			return response;
		}

		// Update and return student
		else {
			student.setEntityId(id);
			student.setModifiedDate(Calendar.getInstance());
			student = new StudentFacade().update(student);
			HttpStatus status = HttpStatus.OK;
			ResponseEntity<Object> response = new ResponseEntity<Object>(student, status);
			return response;
		}
	}

	/**
	 * Delete student matching the path id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/students/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteStudent(@PathVariable long id) {
		new StudentFacade().delete(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	/**
	 * Create and return a list containing any missing fields
	 * 
	 * @param student
	 * @return
	 */
	private List<String> getMissingFields(Student student) {
		List<String> missingFields = new ArrayList<String>();
		if (student.getFirstName() == null) missingFields.add("firstName");
		if (student.getLastName() == null) missingFields.add("lastName");
		return missingFields;
	}

	/**
	 * Create and return a response entity to let the user know a resource
	 * with this id was not found
	 * 
	 * @param id
	 * @return
	 */
	private ResponseEntity<Object> createNotFound(long id) {
		ErrorMessage msg = new ErrorMessage("No student with id " + id + " found");
		HttpStatus status = HttpStatus.NOT_FOUND;
		ResponseEntity<Object> response = new ResponseEntity<Object>(msg, status);
		return response;
	}
}
