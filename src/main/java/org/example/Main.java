package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new JavaCommunityBot("8051342115:AAEU-CKx0etwAipiADKKdG44yQ0fwp5e4yM"));
            System.out.println("Bot started successfully!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}