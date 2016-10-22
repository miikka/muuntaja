package muuntaja.artisanal;

import clojure.lang.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Iterator;

public final class JsonDecoder {
    public static final IPersistentMap mapFromJson(JsonParser jp) throws IOException {
        ITransientMap ret = PersistentHashMap.EMPTY.asTransient();

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            jp.nextToken();
            ret = ret.assoc(fieldName, fromJson(jp));
        }

        return ret.persistent();
    }

    public static final IPersistentCollection vectorFromJson(JsonParser jp) throws IOException {
        ITransientCollection ret = PersistentVector.EMPTY.asTransient();
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            ret = ret.conj(fromJson(jp));
        }
        return ret.persistent();
    }

    public static final Object fromJson(JsonParser jp) throws IOException {
        JsonToken token = jp.getCurrentToken();
        if (token == JsonToken.VALUE_STRING) {
            return jp.getText();
        } else if (token == JsonToken.VALUE_FALSE) {
            return false;
        } else if (token == JsonToken.VALUE_TRUE) {
            return true;
        } else if (token == JsonToken.VALUE_NULL) {
            return null;
        } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
            return jp.getDoubleValue();
        } else if (token == JsonToken.VALUE_NUMBER_INT) {
            try {
                return jp.getLongValue();
            } catch (JsonParseException e) {
                return jp.getBigIntegerValue();
            }
        } else if (token == JsonToken.START_ARRAY) {
            return vectorFromJson(jp);
        } else if (token == JsonToken.START_OBJECT) {
            return mapFromJson(jp);
        } else {
            throw new IOException("Unexpected token: " + token.toString());
        }
    }
}
