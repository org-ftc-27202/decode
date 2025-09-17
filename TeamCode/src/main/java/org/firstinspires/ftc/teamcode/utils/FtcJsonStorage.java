package org.firstinspires.ftc.teamcode.utils;

import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.ArtifactSequence;
import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.Coords;
import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.DateMs;
import org.firstinspires.ftc.teamcode.utils.DecodeDataTypes.MotorPositions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import android.content.Context;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FtcJsonStorage {
    private final Context appContext;

    private final File internalFilesDir;

    public FtcJsonStorage(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        appContext = context.getApplicationContext();
        internalFilesDir = appContext.getFilesDir();
    }

    public String getInternalDir() {
        return internalFilesDir.getAbsolutePath();
    }

    private String getJsonFromMotorPositions(MotorPositions motorPositions) {
        StringBuilder motorPositionsJson = new StringBuilder();
        List<Map.Entry<String, Double>> motorPositionEntries = motorPositions.getEntries();
        for (int i = 0; i < motorPositionEntries.size(); i++) {
            motorPositionsJson
                    .append("[\"")
                    .append(motorPositionEntries.get(i).getKey())
                    .append("\", ")
                    .append(motorPositionEntries.get(i).getValue())
                    .append((i != motorPositionEntries.size() - 1) ? "]," : "]")
                    .append("\n");
        }
        return motorPositionsJson.toString();
    }

    private String getJsonFromArguments(ArtifactSequence artifactSequence, Coords coords, MotorPositions motorPositions, DateMs dateMs) {
        String[] seqString = artifactSequence.toStringArray();
        return "{\n" +
               "  \"artifactSequence\": [\"" + seqString[0] + "\", \"" + seqString[1] + "\", \"" + seqString[2] + "\"],\n" +
               "  \"endingPosition\": {\n" +
               "    \"x\": " + coords.getX() + ",\n" +
               "    \"y\": " + coords.getY() + ",\n" +
               "    \"z\": " + coords.getZ() + ",\n" +
               "    \"pitch\": " + coords.getPitch() + ",\n" +
               "    \"roll\": " + coords.getRoll() + ",\n" +
               "    \"yaw\": " + coords.getYaw() + "\n" +
               "  },\n" +
               "  \"motorPositions\": [\n" +
               getJsonFromMotorPositions(motorPositions) +
               "  ],\n" +
               "  \"saveDateMillis\": " + dateMs.getDateMs() + "\n" +
               "}";
    }

    public String[] getInternalFileNames() {
        return internalFilesDir.list();
    }

    public void writeToInternalStorage(@NonNull String filename, @NonNull ArtifactSequence artifactSequence, @NonNull Coords coords, @NonNull MotorPositions motorPositions, @NonNull DateMs dateMs) {
        String jsonStringContent = getJsonFromArguments(artifactSequence, coords, motorPositions, dateMs);
        FileOutputStream fos = null;

        try {
            fos = appContext.openFileOutput(filename, Context.MODE_PRIVATE);
            // Context.MODE_PRIVATE: Default mode. If the file already exists, it will be overwritten.
            // Context.MODE_APPEND: If the file already exists, data will be appended to the end.

            fos.write(jsonStringContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            // telemetry.addData("Internal Storage", "Error saving: " + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readFromJsonInternalStorage(String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fis = null;
        String fileContent;

        try {
            fis = appContext.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fileContent = stringBuilder.toString();
            // telemetry.addData("Internal Storage", "Content: " + fileContent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "File not found";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileContent;
    }

    private String[] jsonArrayToStringArray(JSONArray array) {
        String[] stringArray = new String[array.length()];
        try {
            for (int i = 0; i < array.length(); i++) {
                stringArray[i] = array.getString(i);
            }
            return stringArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Returning an empty array doesn't matter because it will catch error in other function anyways
        return stringArray;
    }

    private double[] jsonArrayToDoubleArray(JSONArray array) {
        double[] doubleArray = new double[array.length()];
        try {
            for (int i = 0; i < array.length(); i++) {
                doubleArray[i] = array.getDouble(i);
            }
            return doubleArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Returning an empty array doesn't matter because it will catch error in other function anyways
        return doubleArray;
    }

    public boolean readAndParseAutoData( String filename, ArtifactSequence dstArtifactSequence, Coords dstCoords, MotorPositions dstMotorPositions, DateMs dstDateMs) {
        try {
            String jsonStringContent = readFromJsonInternalStorage(filename);
            if (!jsonStringContent.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStringContent);

                    JSONArray artifactSequence = jsonObject.getJSONArray("artifactSequence");

                    if (artifactSequence.length() != 3) {
                        throw new IllegalArgumentException("Artifact sequence must have 3 elements");
                    }

                    dstArtifactSequence.setSequence(jsonArrayToStringArray(artifactSequence));

                    JSONObject coords = jsonObject.getJSONObject("endingPosition");

                    dstCoords.setCoords(
                            coords.getDouble("x"),
                            coords.getDouble("y"),
                            coords.getDouble("z"),
                            coords.getDouble("pitch"),
                            coords.getDouble("roll"),
                            coords.getDouble("yaw")
                    );

                    JSONArray motorPositionsArray = jsonObject.getJSONArray("motorPositions");

                    for (int i = 0; i < motorPositionsArray.length(); i++) {
                        JSONArray motorPositions = motorPositionsArray.getJSONArray(i);
                        dstMotorPositions.addMotorPosition(
                                motorPositions.getString(0),
                                motorPositions.getDouble(1)
                        );
                    }

                    dstDateMs.setDateMs(jsonObject.getLong("saveDateMillis"));

                    // Return Successful Fetch Calibration Data
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // SOMETHING WENT WRONG, RELY ON ODOMETRY

        // Return Failure Fetch Calibration Data
        return false;
    }
}