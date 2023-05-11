package logic;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class is a JSON deserializer for the PlayField class.
 * It is used to deserialize JSON representations of PlayField objects.
 *
 * @author LiChieh Chou
 */
public class PlayFieldDeserializer implements JsonDeserializer<PlayField> {
    @Override
    public PlayField deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject sourcePosObj = jsonObject.get("source").getAsJsonObject();
        Position sourcePos = new Position(sourcePosObj.get("x").getAsInt(),sourcePosObj.get("y").getAsInt());
        boolean overflow = jsonObject.get("overflow").getAsBoolean();
        JsonArray boardArr1 = jsonObject.get("board").getAsJsonArray();

        int columns = boardArr1.size();
        int rows = ((JsonArray) boardArr1.get(0)).size();

        PlayField newPlayField = new PlayField(columns, rows, overflow);
        newPlayField.setSourcePos(sourcePos);

        for (int i = 0;i < columns;i++){
            for (int j = 0;j < rows;j++){
                Pipe pipe = new Pipe();
                pipe.setPipe(((JsonArray) boardArr1.get(i)).get(j).getAsInt());
                newPlayField.setField(new Position(i,j),pipe);
            }
        }
        return newPlayField;
    }
}
