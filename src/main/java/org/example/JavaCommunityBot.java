package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaCommunityBot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "java_community_ukraine_bot";
    private final String botToken;
    private final QuestionBank questionBank = new QuestionBank();
    private final Map<Long, Question> pendingVerifications = new HashMap<>();
    private final Map<Long, Timer> verificationTimers = new HashMap<>();
    private int successfulVerifications = 0;
    private final Map<Long, User> users = new HashMap<>();

    private static final Logger LOGGER = Logger.getLogger(JavaCommunityBot.class.getName());

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
        if (update.hasMessage()) {
            Message message = update.getMessage();

            // Перевірка на нового учасника
            if (message.getNewChatMembers() != null && !message.getNewChatMembers().isEmpty()) {
                message.getNewChatMembers().forEach(member -> startVerification(member.getId(), message.getChatId()));
            } else if (message.hasText()) {
                long chatId = message.getChatId();
                long userId = message.getFrom().getId();
                String messageText = message.getText();
                    switch (messageText) {
                        case "/start":
                            sendMessage(chatId, "Ласкаво просимо в Java Community Bot!\nНатисніть /help щоб побачити можливі команди.");
                            break;
                        case "/help":
                            sendMessage(chatId, "Можливі команди:\n /start - натисніть щоб почати або перезапустити бота;\n/help - команда для перегляду можливих команд\n/stats - команда для перегляду статистики.");
                            break;
                        case "/stats":
                            sendMessage(chatId, "Успішно пройшли верифікацію: " + successfulVerifications);
                            break;
                        default:
                            handleUserResponse(chatId, userId, messageText);
                    }
            }
        }
    }

    private void startVerification(long userId, long chatId) {
        Question question = questionBank.getRandomQuestion();
        pendingVerifications.put(userId, question);

        users.putIfAbsent(userId, new User(userId, "User_" + userId));

        sendMessage(chatId, "@" + userId + ", щоб підтвердити, що ви не бот, дайте відповідь на запитання:\n" +
                question.getQuestion());

        // Запускаємо таймер на 5 хвилин (300 000 мс)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pendingVerifications.containsKey(userId)) {
                    sendMessage(chatId, "@" + userId + ", ви не пройшли верифікацію вчасно! Доступ обмежено.");
                    pendingVerifications.remove(userId);
                    User user = users.get(userId);
                    if (user != null){
                        user.setState(UserState.READ_ONLY);
                    }
                }
            }
        }, 300000);

        verificationTimers.put(userId, timer);
    }

    private void handleUserResponse(long chatId, long userId, String userAnswer) {
        if (pendingVerifications.containsKey(userId)) {
            Question question = pendingVerifications.get(userId);
            User user = users.getOrDefault(userId, new User(userId, "User_" + userId));


            if (question.getAnswer().equalsIgnoreCase(userAnswer.trim())) {
                sendMessage(chatId, "Верифікація успішна! Ласкаво просимо!");

                user.setState(UserState.VERIFIED);
                pendingVerifications.remove(userId);
                verificationTimers.get(userId).cancel();
                verificationTimers.remove(userId);
                successfulVerifications++;
            } else {
                if (user.getState() == UserState.PENDING_VERIFICATION) {
                sendMessage(chatId, "Неправильна відповідь, у вас є ще одна спроба.");
                user.setState(UserState.READ_ONLY);
                } else {
                    sendMessage(chatId, "Ви не пройшли верифікацію, доступ обмежено.");
                    pendingVerifications.remove(userId);
                    verificationTimers.get(userId).cancel();
                    verificationTimers.remove(userId);
                }
            }
            users.put(userId, user);
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.log(Level.SEVERE, "Помилка надсилання повідомлення", e);
        }
    }
}
