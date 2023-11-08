package com.codecool.ehotel.service.sheetsExporter;
import com.codecool.ehotel.model.Guest;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

public class GoogleSheetsExporter {
    private static final String APPLICATION_NAME = "Google Sheets API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

    // Constructor
    public GoogleSheetsExporter() {
        // Initialize any necessary components or settings here
    }
    public void exportDataToGoogleSheets(List<List<Guest>> season) {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            String spreadsheetId = "1wwnD3r4XfDbirIveGFRGgVKbeJa6LAEFeh9bpcZxVTw";
            Sheets service = createSheetsService(HTTP_TRANSPORT);
            String valueInputOption = "RAW";
            List<List<Object>> allValues = new ArrayList<>();

            List<List<Guest>> rotatedSeason = rotateMatrixToColumn(season);

            for (List<Guest> day : rotatedSeason) {
                List<Object> dayValues = new ArrayList<>();
                for (Guest guest : day) {
                    dayValues.add(guest.name());
                    dayValues.add(guest.checkIn().toString());
                    dayValues.add(guest.checkOut().toString());
                }
                allValues.add(dayValues);
            }

            String range = "B2:ZZZ99999";
            ValueRange body = new ValueRange().setValues(allValues);
            UpdateValuesResponse response = updateSheetValues(service, spreadsheetId, range, body, valueInputOption);
            printResult(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<List<Guest>> rotateMatrixToColumn(List<List<Guest>> matrix) {
        int rows = matrix.size();
        int maxCols = 0;

        // Find the maximum number of columns in any row
        for (List<Guest> row : matrix) {
            int rowSize = row.size();
            if (rowSize > maxCols) {
                maxCols = rowSize;
            }
        }

        List<List<Guest>> rotatedMatrix = new ArrayList<>();

        for (int j = 0; j < maxCols; j++) {
            List<Guest> column = new ArrayList<>();
            rotatedMatrix.add(column);
        }

        for (int j = 0; j < maxCols; j++) {
            for (int i = 0; i < rows; i++) {
                List<Guest> row = matrix.get(i);
                if (j < row.size()) {
                    rotatedMatrix.get(j).add(row.get(j));
                }
            }
        }

        return rotatedMatrix;
    }


    private Sheets createSheetsService(NetHttpTransport HTTP_TRANSPORT)
            throws IOException, GeneralSecurityException {
        Credential credentials = getCredentials(HTTP_TRANSPORT);
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }



    private UpdateValuesResponse updateSheetValues(Sheets service, String spreadsheetId, String range, ValueRange body, String valueInputOption)
            throws IOException {
        Sheets.Spreadsheets.Values.Update request = service.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption(valueInputOption);
        return request.execute();
    }

    private void printResult(UpdateValuesResponse response) {
        System.out.println("Updated " + response.getUpdatedCells() + " cells.");
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        InputStream in = GoogleSheetsExporter.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("online")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
