/*
 * This file is generated by jOOQ.
 */
package fi.hopeton.jooq.tables;


import fi.hopeton.jooq.DefaultSchema;
import fi.hopeton.jooq.Keys;
import fi.hopeton.jooq.tables.records.EventDateRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EventDate extends TableImpl<EventDateRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>EVENT_DATE</code>
     */
    public static final EventDate EVENT_DATE = new EventDate();

    /**
     * The column <code>EVENT_DATE.VERSION</code>.
     */
    public final TableField<EventDateRecord, Long> VERSION = createField(DSL.name("VERSION"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>EVENT_DATE.ID</code>.
     */
    public final TableField<EventDateRecord, Long> ID = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>EVENT_DATE.PROPOSED_DATE</code>.
     */
    public final TableField<EventDateRecord, LocalDate> PROPOSED_DATE = createField(DSL.name("PROPOSED_DATE"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column <code>EVENT_DATE.EVENT_ID</code>.
     */
    public final TableField<EventDateRecord, Long> EVENT_ID = createField(DSL.name("EVENT_ID"), SQLDataType.BIGINT.nullable(false), this, "");
    private transient Events _events;

    private EventDate(Name alias, Table<EventDateRecord> aliased) {
        this(alias, aliased, null);
    }

    private EventDate(Name alias, Table<EventDateRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>EVENT_DATE</code> table reference
     */
    public EventDate(String alias) {
        this(DSL.name(alias), EVENT_DATE);
    }

    /**
     * Create an aliased <code>EVENT_DATE</code> table reference
     */
    public EventDate(Name alias) {
        this(alias, EVENT_DATE);
    }

    /**
     * Create a <code>EVENT_DATE</code> table reference
     */
    public EventDate() {
        this(DSL.name("EVENT_DATE"), null);
    }

    public <O extends Record> EventDate(Table<O> child, ForeignKey<O, EventDateRecord> key) {
        super(child, key, EVENT_DATE);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Identity<EventDateRecord, Long> getIdentity() {
        return (Identity<EventDateRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<EventDateRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_5;
    }

    @Override
    public List<UniqueKey<EventDateRecord>> getKeys() {
        return Arrays.<UniqueKey<EventDateRecord>>asList(Keys.CONSTRAINT_5);
    }

    @Override
    public List<ForeignKey<EventDateRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<EventDateRecord, ?>>asList(Keys.CONSTRAINT_59);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EventDateRecord> getRecordType() {
        return EventDateRecord.class;
    }

    public Events events() {
        if (_events == null)
            _events = new Events(this, Keys.CONSTRAINT_59);

        return _events;
    }

    @Override
    public EventDate as(String alias) {
        return new EventDate(DSL.name(alias), this);
    }

    @Override
    public EventDate as(Name alias) {
        return new EventDate(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EventDate rename(String name) {
        return new EventDate(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EventDate rename(Name name) {
        return new EventDate(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, LocalDate, Long, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
