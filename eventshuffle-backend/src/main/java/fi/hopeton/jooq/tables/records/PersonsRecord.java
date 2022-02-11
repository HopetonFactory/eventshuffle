/*
 * This file is generated by jOOQ.
 */
package fi.hopeton.jooq.tables.records;


import fi.hopeton.jooq.tables.Persons;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class PersonsRecord extends UpdatableRecordImpl<PersonsRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached PersonsRecord
     */
    public PersonsRecord() {
        super(Persons.PERSONS);
    }

    /**
     * Create a detached, initialised PersonsRecord
     */
    public PersonsRecord(Integer personId, String name) {
        super(Persons.PERSONS);

        setPersonId(personId);
        setName(name);
    }

    /**
     * Getter for <code>PERSONS.PERSON_ID</code>.
     */
    public Integer getPersonId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>PERSONS.PERSON_ID</code>.
     */
    public void setPersonId(Integer value) {
        set(0, value);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>PERSONS.NAME</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>PERSONS.NAME</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Persons.PERSONS.PERSON_ID;
    }

    @Override
    public Field<String> field2() {
        return Persons.PERSONS.NAME;
    }

    @Override
    public Integer component1() {
        return getPersonId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Integer value1() {
        return getPersonId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public PersonsRecord value1(Integer value) {
        setPersonId(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public PersonsRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public PersonsRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }
}
