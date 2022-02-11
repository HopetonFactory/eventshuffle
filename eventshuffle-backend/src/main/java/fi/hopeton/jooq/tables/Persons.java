/*
 * This file is generated by jOOQ.
 */
package fi.hopeton.jooq.tables;


import fi.hopeton.jooq.DefaultSchema;
import fi.hopeton.jooq.Keys;
import fi.hopeton.jooq.tables.records.PersonsRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Persons extends TableImpl<PersonsRecord> {

    /**
     * The reference instance of <code>PERSONS</code>
     */
    public static final Persons PERSONS = new Persons();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>PERSONS.PERSON_ID</code>.
     */
    public final TableField<PersonsRecord, Integer> PERSON_ID = createField(DSL.name("PERSON_ID"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>PERSONS.NAME</code>.
     */
    public final TableField<PersonsRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(100), this, "");

    private Persons(Name alias, Table<PersonsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Persons(Name alias, Table<PersonsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>PERSONS</code> table reference
     */
    public Persons(String alias) {
        this(DSL.name(alias), PERSONS);
    }

    /**
     * Create an aliased <code>PERSONS</code> table reference
     */
    public Persons(Name alias) {
        this(alias, PERSONS);
    }

    /**
     * Create a <code>PERSONS</code> table reference
     */
    public Persons() {
        this(DSL.name("PERSONS"), null);
    }

    public <O extends Record> Persons(Table<O> child, ForeignKey<O, PersonsRecord> key) {
        super(child, key, PERSONS);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PersonsRecord> getRecordType() {
        return PersonsRecord.class;
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Identity<PersonsRecord, Integer> getIdentity() {
        return (Identity<PersonsRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<PersonsRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_2;
    }

    @Override
    public List<UniqueKey<PersonsRecord>> getKeys() {
        return Arrays.<UniqueKey<PersonsRecord>>asList(Keys.CONSTRAINT_2);
    }

    @Override
    public Persons as(String alias) {
        return new Persons(DSL.name(alias), this);
    }

    @Override
    public Persons as(Name alias) {
        return new Persons(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Persons rename(String name) {
        return new Persons(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Persons rename(Name name) {
        return new Persons(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
