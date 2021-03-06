/*
 * This file is generated by jOOQ.
 */
package fi.hopeton.jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long version;

    public Events() {
    }

    public Events(Events value) {
        this.id = value.id;
        this.name = value.name;
        this.version = value.version;
    }

    public Events(
            Long id,
            String name,
            Long version
    ) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    /**
     * Getter for <code>EVENTS.ID</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>EVENTS.ID</code>.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>EVENTS.NAME</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>EVENTS.NAME</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for <code>EVENTS.VERSION</code>.
     */
    public Long getVersion() {
        return this.version;
    }

    /**
     * Setter for <code>EVENTS.VERSION</code>.
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Events other = (Events) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Events (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(version);

        sb.append(")");
        return sb.toString();
    }
}
