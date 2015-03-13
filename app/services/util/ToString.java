package services.util;

import java.util.Collection;

/**
 * ToString ...
 *
 * @author Vadim Martos
 * @date 12/13/12
 */
public class ToString {
    private StringBuilder sb = new StringBuilder();

    public ToString(String title) {
        sb.append(title).append(" ");
    }

    public ToString(Object o) {
        this(o.getClass().getName());
    }

    public ToString add(String name, String value) {
        sb.append(name).append("=\"").append(value == null ? "null" : value).append("\" ");
        return this;
    }

    public ToString add(String name, Object value) {
        return add(name, value == null ? "null" : value.toString());
    }

    public ToString add(String name, int value) {
        sb.append(name).append("=").append(value).append(" ");
        return this;
    }

    public ToString addGroup(String name, String value) {
        sb.append(name).append("={").append(value == null ? "null" : value).append("} ");
        return this;
    }

    public ToString add(String name, String[] value) {
        if (value != null) {
            sb.append(name).append("=[");
            for (String v : value)
                sb.append("\"").append(v == null ? "null" : v).append("\" ");
            sb.append("] ");
        }
        return this;
    }

    public ToString addGroup(String name, Collection value) {
        if (value != null) {
            sb.append(name).append("={");
            for (Object v : value) {
                sb.append(v == null ? "null" : v.toString()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("} ");
        }
        return this;
    }

    public String toString() {
        return sb.toString();
    }
}
