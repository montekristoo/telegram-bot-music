import com.github.axet.vget.VGet;
import com.github.axet.vget.vhs.YouTubeInfo;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//import models.S3File;
//import play.db.ebean.Model;
//import play.mvc.Controller;
//import play.mvc.Result;
//import play.mvc.Http;

//import views.html.index;

import java.util.List;
import java.util.UUID;


import lombok.Lombok;

import java.io.*;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import lombok.SneakyThrows;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import org.telegram.telegrambots.meta.updateshandlers.DownloadFileCallback;

import javax.sound.sampled.*;



public class MyBot extends TelegramLongPollingBot {


   /* static public String QuotesRandom() throws FileNotFoundException {
        ArrayList<String> quotesContent = new ArrayList<String>();
        Random rand = new Random();
        Scanner scanner = null;

        scanner = new Scanner(new FileInputStream("F:\\hcj\\Maven\\src\\main\\java\\quotes.txt"));

        scanner.useDelimiter("}");

        while (scanner.hasNext()) {
            quotesContent.add(scanner.next());
        }

        scanner.close();

        int randNumber = rand.nextInt(quotesContent.size()+1);

        return quotesContent.get(randNumber);

    }*/

    @Override
    public String getBotUsername() {
        return "Kr1sto_Bot";
    }

    @Override
    public String getBotToken() {
        return "5311245074:AAG5RU6w5UMac1V0hz2iBaV6Qir-CKxlt4M";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    //private void handleMessage (Message message) throws TelegramApiException, IOException, YoutubeDLException {

        //  execute(SendMessage.builder().text(message.getText()).
        //       chatId(message.
        //               getChatId().
        //   toString()).
        //     build());
  //  }

    public boolean isValidUrl(Update update) {
        if (!update.getMessage().getText().toString().matches("^(http(s)?:\\/\\/)?((w){3}.)?(music\\.)?youtu(be|.be)?(\\.com)?\\/.+")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText(" ❌ Invalid link. Send me a correct link from youtube video.");
            try {
                execute(sendMessage);
            } catch (TelegramApiException ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public void AudioSend (Update update, File file) {
        SendAudio sendAudio = new SendAudio();
        sendAudio.setChatId(update.getMessage().getChatId().toString());
        sendAudio.setAudio(new InputFile(file));

        try {
            execute(sendAudio);
        } catch (TelegramApiException ee) {
            SendMessage sendMessagee = new SendMessage();
            sendMessagee.setChatId(update.getMessage().getChatId().toString());
            sendMessagee.setText(" ❌ Error. The file could not be downloaded.");
            try {
                execute(sendMessagee);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    public void SendingMP3(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText("⚙ Data processing...");
        try {
            execute(sendMessage);
        } catch (TelegramApiException ee) {
            ee.printStackTrace();
        }
        String videoUrl = update.getMessage().getText().toString();

        Document document = null;
        try {
            document = Jsoup.connect(videoUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String title = document.title().replaceAll(" - YouTube$", "");
        title = title.replace("\"", "");

           File file = new File(title + ".mp3");
           String directory = System.getProperty("user.dir");
            YoutubeDLRequest request = new YoutubeDLRequest(videoUrl, directory);
            YoutubeDL.setExecutablePath("F:\\hcj\\TelegramBot1\\yt-dlp.exe");
            request.setOption("no-mark-watched");
            request.setOption("ignore-errors");
            request.setOption("no-playlist");
            request.setOption("extract-audio");
            request.setOption("audio-format \"mp3\"");
            request.setOption("output \"" + title + ".%(ext)s\"");
            YoutubeDLResponse response = null;
            try {
                response = YoutubeDL.execute(request);
            } catch (YoutubeDLException e) {
                e.printStackTrace();
            }
            AudioSend(update, file);
            file.delete();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            if (isValidUrl(update)) {
                SendingMP3(update);
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }


}
