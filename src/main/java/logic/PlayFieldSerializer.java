package logic;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * The PlayFieldSerializer class is responsible for serializing PlayField objects into JSON format.
 * It implements the JsonSerializer interface to convert PlayField objects to a JsonElement that can be serialized and stored.
 *
 * @author LiChieh Chou
 */
public class PlayFieldSerializer implements JsonSerializer<PlayField> {
    @Override
    public JsonElement serialize(PlayField playField, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject sourceObj = new JsonObject();
        sourceObj.addProperty("x",playField.getSourcePos().getX());
        sourceObj.addProperty("y",playField.getSourcePos().getY());

        JsonArray boardArr1 = new JsonArray();
        JsonArray boardArr2;
        for (int i = 0;i < playField.getColumns();i++) {
            boardArr2 = new JsonArray();
            for (int j = 0;j < playField.getRows();j++) {
                boardArr2.add(playField.getField(new Position(i,j)).getPipeNumber());
            }
            boardArr1.add(boardArr2);
        }

        JsonObject obj = new JsonObject();
        obj.add("source",sourceObj);
        obj.addProperty("overflow",playField.getOverflow());
        obj.add("board",boardArr1);

        return obj;
    }
}