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
import com.lodenrogue.teachertools.model.Registration;
import com.lodenrogue.teachertools.model.Student;
import com.lodenrogue.teachertools.service.GroupFacade;
import com.lodenrogue.teachertools.service.RegistrationFacade;
import com.lodenrogue.teachertools.service.StudentFacade;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

	/**
	 * Create a registration
	 * 
	 * @param registration
	 * @return
	 */
	@RequestMapping(path = "/registrations", method = RequestMethod.POST)
	public ResponseEntity<Object> createRegistration(@RequestBody Registration registration) {

		// Get missing fields
		List<String> missingFields = getMissingFields(registration);

		// Check missing fields
		if (missingFields.size() > 0) {
			MissingFieldsError mfe = new MissingFieldsError(missingFields);
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			return new ResponseEntity<Object>(mfe, status);
		}

		// Check if group exists
		long groupId = registration.getGroupId();
		Group existingGroup = new GroupFacade().find(groupId);

		if (existingGroup == null) {
			ErrorMessage msg = new ErrorMessage("No group with id " + groupId + " found");
			HttpStatus status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Object>(msg, status);
		}

		// Check if student exists
		long studentId = registration.getStudentId();
		Student existingStudent = new StudentFacade().find(studentId);

		if (existingStudent == null) {
			ErrorMessage msg = new ErrorMessage("No student with id " + studentId + " found");
			HttpStatus status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Object>(msg, status);
		}

		// Create registration
		registration.setCreatedDate(Calendar.getInstance());
		registration = new RegistrationFacade().create(registration);
		HttpStatus status = HttpStatus.CREATED;
		return new ResponseEntity<Object>(registration, status);
	}

	/**
	 * Get registration with given id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/registrations/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getRegistration(@PathVariable long id) {

		// Get registration
		Registration registration = new RegistrationFacade().find(id);

		// Check if it exists
		if (registration == null) {
			return createNotFound(id);
		}

		// Return registration
		else {
			HttpStatus status = HttpStatus.OK;
			return new ResponseEntity<Object>(registration, status);
		}
	}

	/**
	 * Update student matching given id with given body
	 * 
	 * @param id
	 * @param registration
	 * @return
	 */
	@RequestMapping(path = "/registrations/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateRegistration(@PathVariable long id, @RequestBody Registration registration) {

		// Get missing fields
		List<String> missingFields = getMissingFields(registration);
		if (registration.getCreatedDate() == null) missingFields.add("createdDate");

		// Check missing fields
		if (missingFields.size() > 0) {
			MissingFieldsError mfe = new MissingFieldsError(missingFields);
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			return new ResponseEntity<Object>(mfe, status);
		}

		// Check if registration exists
		Registration existing = new RegistrationFacade().find(id);
		if (existing == null) {
			return createNotFound(id);
		}

		// Check if group exists
		long groupId = registration.getGroupId();
		Group existingGroup = new GroupFacade().find(groupId);

		if (existingGroup == null) {
			ErrorMessage msg = new ErrorMessage("No group with id " + groupId + " found");
			HttpStatus status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Object>(msg, status);
		}

		// Check if student exists
		long studentId = registration.getStudentId();
		Student existingStudent = new StudentFacade().find(studentId);

		if (existingStudent == null) {
			ErrorMessage msg = new ErrorMessage("No student with id " + studentId + " found");
			HttpStatus status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Object>(msg, status);
		}

		// Update registration
		registration.setEntityId(id);
		registration.setModifiedDate(Calendar.getInstance());
		registration = new RegistrationFacade().update(registration);
		HttpStatus status = HttpStatus.OK;
		return new ResponseEntity<Object>(registration, status);
	}

	/**
	 * Delete the registration matching the given id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/registrations/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteRegistration(@PathVariable long id) {
		new RegistrationFacade().delete(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	/**
	 * Create not found response
	 * 
	 * @param id
	 * @return
	 */
	private ResponseEntity<Object> createNotFound(long id) {
		ErrorMessage msg = new ErrorMessage("No registration with id " + id + " found");
		HttpStatus status = HttpStatus.NOT_FOUND;
		return new ResponseEntity<Object>(msg, status);
	}

	/**
	 * Create and return a list of missing fields
	 * 
	 * @param registration
	 * @return
	 */
	private List<String> getMissingFields(Registration registration) {
		List<String> missingFields = new ArrayList<String>();
		if (registration.getGroupId() == null) missingFields.add("groupId");
		if (registration.getStudentId() == null) missingFields.add("studentId");
		return missingFields;
	}
}
