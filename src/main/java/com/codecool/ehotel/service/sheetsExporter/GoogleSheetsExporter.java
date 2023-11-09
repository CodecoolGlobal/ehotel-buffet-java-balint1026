package com.codecool.ehotel.service.sheetsExporter;
import com.codecool.ehotel.model.Guest;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class GoogleSheetsExporter {
    private static final String APPLICATION_NAME = "Google Sheets API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

    // Constructor
    public GoogleSheetsExporter() {

    }
    public void exportDataToGoogleSheets(List<List<Guest>> season) {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            String spreadsheetId = "1wwnD3r4XfDbirIveGFRGgVKbeJa6LAEFeh9bpcZxVTw";
            Sheets service = createSheetsService(HTTP_TRANSPORT);
            String tabName = LocalDateTime.now().toString().substring(11,19);
            String valueInputOption = "RAW";
                AddTabToGoogleSheet(service,spreadsheetId, tabName);
            List<List<Object>> allValues = new ArrayList<>();

            for (int dayIndex = 0; dayIndex < season.size(); dayIndex++) {
                List<Guest> day = season.get(dayIndex);
                List<Object> dayValues = new ArrayList<>();
                dayValues.add("Day " + (dayIndex + 1)); // Add day information

                for (Guest guest : day) {
                    dayValues.add(guest.name());
                    dayValues.add(guest.checkIn().toString().substring(5) + " --- " + guest.checkOut().toString().substring(5));
                    dayValues.add("");
                }

                allValues.add(dayValues);
            }

            ValueRange body = new ValueRange().setValues(allValues);
            String range = tabName + "!A1:ZZZ99999";
            UpdateValuesResponse response = updateSheetValues(service, spreadsheetId, range, body, valueInputOption);
            printResult(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Sheets createSheetsService(NetHttpTransport HTTP_TRANSPORT)
            throws IOException, GeneralSecurityException {
        Credential credentials = getCredentials(HTTP_TRANSPORT);
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static BatchUpdateSpreadsheetResponse AddTabToGoogleSheet(Sheets service, String sheetName, String tabName)
            throws IOException {
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties()
                .setTitle(tabName))));
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        return service.spreadsheets().batchUpdate(sheetName, body).execute();
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
