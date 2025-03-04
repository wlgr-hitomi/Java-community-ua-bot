package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class JavaCommunityBot extends TelegramLongPollingBot {
    private static final String BOT_USERNAME = "java_community_ukraine_bot";
    private final String botToken;

    public JavaCommunityBot(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    sendMessage(chatId, "Welcome to the Java Community Bot! Use /help to see available commands.");
                    break;
                case "/help":
                    sendMessage(chatId, "Available commands:\n/start - Start the bot\n/help - Show this help message :)");
                    break;
                default:
                    sendMessage(chatId, "Unknown command. Use /help to see available commands.");
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
