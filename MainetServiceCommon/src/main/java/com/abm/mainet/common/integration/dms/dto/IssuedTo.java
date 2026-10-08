package com.abm.mainet.common.integration.dms.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


@XmlAccessorType(XmlAccessType.FIELD)
public class IssuedTo {

    @XmlElementWrapper(name = "Persons", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    @XmlElement(name = "Person", namespace = "https://www.digitallocker.gov.in/schema/issuer/v1/pullurirequest")
    private List<Person> persons;

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
}
