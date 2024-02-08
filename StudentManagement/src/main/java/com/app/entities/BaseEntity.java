package com.app.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

//public Long getId() {
//	return id;
//}
//
//public void setId(Long id) {
//	this.id = id;
//}
//
//public BaseEntity(Long id) {
//	super();
//	this.id = id;
//}
//
//@Override
//public String toString() {
//	return "BaseEntity [id=" + id + "]";
//}

}
