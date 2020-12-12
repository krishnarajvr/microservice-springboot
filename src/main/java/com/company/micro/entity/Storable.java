

package com.company.micro.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * <h1>Storable</h1>
 * Base class for all entities.
 */
@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
public class Storable extends AbstractStorable {

    private static final long serialVersionUID = -1185360610331389067L;

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    /**
     * Created Time.
     */
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime createdTime;
    /**
     * Updated Time.
     */
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime updatedTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        final Storable otherObject = (Storable) other;

        if (this.getId() == null) {
            return otherObject.getId() == null;
        }

        if (otherObject.getId() == null) {
            return false;
        }

        return this.getId().equals(otherObject.getId());

    }

    @Override
    public int hashCode() {
        return this.getId() == null ? 0 : this.getId().hashCode();
    }

}
