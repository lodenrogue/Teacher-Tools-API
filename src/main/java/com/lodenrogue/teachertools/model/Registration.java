package com.lodenrogue.teachertools.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "registrations")
public class Registration {

	@Id
	@GeneratedValue
	@Column(name = "id")
	@JsonProperty(value = "id")
	private long entityId;

	@Column(name = "student_id")
	private Long studentId;

	@Column(name = "group_id")
	private Long groupId;

	@Column(name = "drop_date")
	private Calendar dropDate;

	@Column(name = "created_date")
	private Calendar createdDate;

	@Column(name = "modified_date")
	private Calendar modifiedDate;

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Calendar getDropDate() {
		return dropDate;
	}

	public void setDropDate(Calendar dropDate) {
		this.dropDate = dropDate;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
