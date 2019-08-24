package special;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.PropLoader;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class FSUVerify extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if(!event.getAuthor().isBot())
            System.out.println(event.getAuthor() + " Has messaged NoleBot: " + event.getMessage().getContentRaw());
        else
            return;
        if(event.getMessage().getContentRaw().toLowerCase().startsWith("verify"))
        {
            try {
                String trimmedMessage = event.getMessage().getContentRaw().replaceAll("verify", "");
                verifyUser(trimmedMessage, event);
            } catch (IOException e) {
                System.out.println("There was an IOException for user: " + event.getAuthor().getName() + " with Message:\n" + event.getMessage().getContentRaw());
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                System.out.println("There was a GeneralSecurityException for user: " + event.getAuthor().getName() + " with Message:\n" + event.getMessage().getContentRaw());
                e.printStackTrace();
            }
        }

    }

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final File CREDENTIALS_FILE_PATH = new File("data/config/credentials.json");


    private void verifyUser(String message, PrivateMessageReceivedEvent event) throws IOException, GeneralSecurityException
    {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(SCOPES);

        String[] trimmedMessage = message.trim().split("//s");
        String password = trimmedMessage[0];
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String SPREADSHEET_ID = PropLoader.getProp("google_spreadsheet_id");
        final String APPLICATION_NAME = PropLoader.getProp("google_app_name");
        final String RANGE = "encodes!B1:C";

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEET_ID, RANGE)
                .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("something is broken");
        }
        else {
            boolean foundPass = false;

            for(List rows : values)
            {
                if(rows.contains(password) && rows.size() == 1)
                {
                    foundPass = true;

                    Member member = event.getJDA().getGuildById("138481681630887936").getMemberById(event.getAuthor().getId());

                    if(member == null)
                    {
                        event.getChannel().sendMessage("You are not in the Esports at FSU server. Please join it and try again.").queue();
                    }
                    else
                    {
                        Role role = member.getGuild().getRoleById("347620911379382272");

                        member.getGuild().getController().addSingleRoleToMember(member, role).queue();
                        System.out.println("Adding " + role.getName() + " to member: " + member.getEffectiveName());
                        rows.add("Verified");
                        response.setValues(values);

                        UpdateValuesResponse result = service.spreadsheets().values().update(SPREADSHEET_ID, response.getRange(), response)
                                .setValueInputOption("RAW")
                                .execute();

                        System.out.printf("%d cells updated.\n", result.getUpdatedCells());
                        event.getChannel().sendMessage("You have been verified. Thank you!").queue();
                    }
                }
            }

            if(!foundPass)
                event.getChannel().sendMessage("This code was not found to be eligible. Please double check for capitalization. Contact an administrator if you find this to be in error.").queue();
        }
    }


}
