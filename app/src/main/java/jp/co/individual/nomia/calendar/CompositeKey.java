package jp.co.individual.nomia.calendar;

import java.io.Serializable;
import java.util.Objects;

public class CompositeKey implements Serializable {

    private final String date;

    private final String type;

    public CompositeKey(String date, String type) {
        this.date = date;
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        CompositeKey other = (CompositeKey) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;

        return true;
    }

}
