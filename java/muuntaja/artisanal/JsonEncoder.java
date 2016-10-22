package muuntaja.artisanal;

import clojure.lang.*;
import com.fasterxml.jackson.core.io.CharTypes;
import java.util.Iterator;

public final class JsonEncoder {
    private StringBuilder sb;

    public JsonEncoder() {
        this.sb = new StringBuilder(1024);
    }

    public String toString() {
        return this.sb.toString();
    }

    public final void mapToJson(APersistentMap data) {
        this.sb.append("{");

        Iterator iterator = data.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            if (count++ > 0) { this.sb.append(","); }

            MapEntry entry = (MapEntry)iterator.next();
            Object key = entry.getKey();
            if (key instanceof Keyword) {
                this.stringToJson(((Keyword)key).toString().substring(1));
            } else {
                this.stringToJson(key.toString());
            }
            sb.append(":");
            this.toJson(entry.getValue());
        }

        this.sb.append("}");
    }

    public final void vectorToJson(APersistentVector data) {
        this.sb.append("[");
        Iterator iterator = data.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            if (count++ > 0) { this.sb.append(","); }
            this.toJson(iterator.next());
        }
        this.sb.append("]");
    }

    public final void stringToJson(String data) {
        this.sb.append("\"");
        CharTypes.appendQuoted(this.sb, data);
        this.sb.append("\"");
    }

    public final void toJson(Object data) {
        if (data == null) {
            this.sb.append("null");
        } else if (data instanceof String) {
            this.stringToJson((String)data);
        } else if (data instanceof Keyword) {
            this.stringToJson(((Keyword)data).toString().substring(1));
        } else if (data instanceof APersistentMap) {
            this.mapToJson((APersistentMap)data);
        } else if (data instanceof APersistentVector) {
            this.vectorToJson((APersistentVector)data);
        } else if (data instanceof Long) {
            this.sb.append(((Long)data).toString());
        } else if (data instanceof Double) {
            this.sb.append(((Double)data).toString());
        } else if (data instanceof BigInt) {
            this.sb.append(((BigInt)data).toString());
        } else if (data instanceof Boolean) {
            if (data == Boolean.TRUE) {
                this.sb.append("true");
            } else {
                this.sb.append("false");
            }
        } else {
            this.sb.append(data.toString());
        }
    }
}
