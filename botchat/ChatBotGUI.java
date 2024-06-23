package botchat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.annotation.Annotation;
import java.util.*;

public class ChatBotGUI extends JFrame {
    
    private StanfordCoreNLP pipeline;
    private Map<String, String> triggerResponses;
    private JTextArea chatArea;
    private JTextField userInputField;
    private JButton sendButton;
    

    public ChatBotGUI() {
        // Initialize NLP pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        pipeline = new StanfordCoreNLP(props);

        // Define trigger responses
        triggerResponses = new HashMap<>();
        triggerResponses.put("help", "Sure, I'm here to help! What do you need assistance with?");
        triggerResponses.put("order", "I can help you with your order. Please provide your order number.");
        triggerResponses.put("problem", "I'm sorry to hear you're having a problem. Can you describe the issue?");

        // Setup GUI
        setTitle("ChatBot");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        userInputField = new JTextField();
        sendButton = new JButton("Send");

        inputPanel.add(userInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleUserInput();
            }
        });

        userInputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleUserInput();
            }
        });

        setVisible(true);
    }

    private void handleUserInput() {
        String userInput = userInputField.getText();
        if (userInput.trim().isEmpty()) {
            return;
        }
        chatArea.append("You: " + userInput + "\n");
        String response = getResponse(userInput);
        chatArea.append("Bot: " + response + "\n");
        userInputField.setText("");
    }

    public String getResponse(String input) {
        // Annotate the input text
        Annotation document = new Annotation(input);
        pipeline.annotate(document);

        // Extract words and named entities
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        Set<String> detectedTriggers = new HashSet<>();
        
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class).toLowerCase();
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                // Check for trigger points
                if (triggerResponses.containsKey(word)) {
                    detectedTriggers.add(word);
                }
            }
        }

        // Generate response based on detected triggers
        if (detectedTriggers.isEmpty()) {
            return "I'm not sure I understand. Can you please clarify?";
        } else {
            StringBuilder response = new StringBuilder();
            for (String trigger : detectedTriggers) {
                response.append(triggerResponses.get(trigger)).append(" ");
            }
            return response.toString().trim();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatBotGUI();
            }
        });
    }
}


