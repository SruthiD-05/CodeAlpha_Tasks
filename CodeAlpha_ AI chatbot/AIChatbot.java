import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;

/**
 * ENHANCED AI CHATBOT - INTERNSHIP READY VERSION
 * Features:
 * - Save/Load Chat History
 * - Bot Learning (remembers new responses)
 * - Professional GUI
 * - NLP with Pattern Matching
 * - Sentiment Analysis
 */

class ChatbotEngine {
    
    private Map<String, java.util.List<String>> knowledgeBase;
    private Map<String, Pattern> patterns;
    private java.util.List<String> greetings;
    private java.util.List<String> farewells;
    private Random random;
    private static final String KNOWLEDGE_FILE = "chatbot_knowledge.dat";
    
    public ChatbotEngine() {
        this.knowledgeBase = new HashMap<>();
        this.patterns = new HashMap<>();
        this.greetings = new ArrayList<>();
        this.farewells = new ArrayList<>();
        this.random = new Random();
        
        initializeKnowledgeBase();
        initializePatterns();
        loadLearnedKnowledge(); // Load previously learned responses
    }
    
    private void initializeKnowledgeBase() {
        greetings.addAll(Arrays.asList(
            "Hello! How can I help you today? 😊",
            "Hi there! What can I do for you?",
            "Greetings! How may I assist you?",
            "Hey! What would you like to know?",
            "Hi! Nice to meet you! 👋"
        ));
        
        farewells.addAll(Arrays.asList(
            "Goodbye! Have a great day! 👋",
            "See you later! Take care!",
            "Bye! Feel free to come back anytime!",
            "Farewell! It was nice talking to you!",
            "Take care! Bye! 😊"
        ));
        
        knowledgeBase.put("name", Arrays.asList(
            "I'm an AI chatbot created to help answer your questions! 🤖",
            "You can call me ChatBot. I'm here to assist you!",
            "I'm an artificial intelligence assistant designed to help you.",
            "My name is AI Chatbot, and I'm here to help! 😊"
        ));
        
        knowledgeBase.put("help", Arrays.asList(
            "I can answer questions, have conversations, and provide information on various topics like AI, programming, and more!",
            "I'm here to help! You can ask me about AI, machine learning, Java, time, or just chat with me!",
            "I can help with questions about technology, programming, AI, and general conversation. Just ask away!",
            "Ask me about artificial intelligence, machine learning, programming, or anything else! 💡",
            "You can also teach me new things! Just say 'learn: topic = response'"
        ));
        
        knowledgeBase.put("weather", Arrays.asList(
            "I don't have real-time weather data, but you can check weather.com or your local weather service. ☀️",
            "For current weather information, I recommend checking a weather website or app like Weather.com! 🌤️",
            "I cannot access real-time weather data, but there are many great weather apps available!"
        ));
        
        knowledgeBase.put("time", Arrays.asList(
            "The current time is: " + new Date().toString(),
            "Right now it's: " + new Date().toString()
        ));
        
        knowledgeBase.put("ai", Arrays.asList(
            "AI stands for Artificial Intelligence - the simulation of human intelligence by machines. It's a fascinating field! 🧠",
            "Artificial Intelligence involves creating systems that can learn, reason, and solve problems like humans do.",
            "AI is a field of computer science focused on creating intelligent machines that work and react like humans.",
            "AI enables machines to perform tasks that typically require human intelligence, like understanding language, recognizing patterns, and making decisions! 🤖"
        ));
        
        knowledgeBase.put("machine_learning", Arrays.asList(
            "Machine Learning is a subset of AI where systems learn from data without being explicitly programmed. 📊",
            "ML enables computers to learn and improve from experience automatically.",
            "Machine Learning uses algorithms to parse data, learn from it, and make predictions or decisions.",
            "In simple terms, Machine Learning is teaching computers to learn from examples rather than programming every rule! 🎓"
        ));
        
        knowledgeBase.put("java", Arrays.asList(
            "Java is a popular object-oriented programming language known for its 'write once, run anywhere' capability. ☕",
            "Java is used for building everything from mobile apps to enterprise systems.",
            "Java is a versatile, platform-independent programming language widely used in software development.",
            "Java was created by Sun Microsystems and is now owned by Oracle. It's one of the most popular programming languages! 💻"
        ));
        
        knowledgeBase.put("programming", Arrays.asList(
            "Programming is the process of creating instructions for computers to execute tasks. 💻",
            "Programming involves writing code in various languages to build software applications.",
            "It's the art and science of creating software by writing code that computers can understand.",
            "Programming is like giving instructions to a computer in a language it understands! 🖥️"
        ));
        
        knowledgeBase.put("nlp", Arrays.asList(
            "NLP stands for Natural Language Processing - it helps computers understand human language! 📝",
            "Natural Language Processing enables machines to read, understand, and derive meaning from human languages.",
            "NLP combines linguistics and AI to help computers process and analyze natural language data.",
            "I use NLP techniques to understand what you're asking me! It's how I can chat with you! 💬"
        ));
        
        knowledgeBase.put("creator", Arrays.asList(
            "I was created as an AI chatbot project using Java! 👨‍💻",
            "I'm built with Java, using NLP techniques and pattern matching!",
            "My creator used Java programming with Natural Language Processing to build me! 🤖"
        ));
        
        knowledgeBase.put("howareyou", Arrays.asList(
            "I'm doing great, thanks for asking! How are you? 😊",
            "I'm functioning perfectly! How can I help you today?",
            "I'm excellent! Ready to answer your questions! 🤖"
        ));
        
        knowledgeBase.put("thanks", Arrays.asList(
            "You're welcome! Happy to help! 😊",
            "No problem! Anytime!",
            "Glad I could help! Feel free to ask more questions!",
            "You're very welcome! 🎉"
        ));
        
        knowledgeBase.put("learn_instruction", Arrays.asList(
            "To teach me something new, use this format: 'learn: keyword = response'",
            "You can teach me by typing: 'learn: topic = answer'. For example: 'learn: python = Python is a programming language'",
            "I can learn! Just type: 'learn: [topic] = [response]' and I'll remember it! 🧠"
        ));
        
        knowledgeBase.put("default", Arrays.asList(
            "I'm not sure I understand. Could you rephrase that? 🤔",
            "Interesting! Could you tell me more?",
            "I don't have information on that specific topic. Can you ask something else?",
            "Hmm, I'm not quite sure about that. Try asking about AI, programming, or my capabilities!",
            "Could you ask that in a different way? I want to make sure I understand you correctly! 💭"
        ));
    }
    
    private void initializePatterns() {
        patterns.put("greeting", Pattern.compile(
            "\\b(hi+|hello+|hey+|hii+|helo|hola|greetings|good morning|good afternoon|good evening|howdy|sup|what'?s up|hiya|yo)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("farewell", Pattern.compile(
            "\\b(bye+|good ?bye|see you|see ya|farewell|exit|quit|later|cya|take care|gotta go|gtg)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("name", Pattern.compile(
            "\\b(what'?s? your name|who are you|your name|tell me your name|what are you called)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("help", Pattern.compile(
            "\\b(help|assist|support|what can you do|how to use|what do you do|your capabilities)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("weather", Pattern.compile(
            "\\b(weather|temperature|forecast|climate|rain|sunny|hot|cold)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("time", Pattern.compile(
            "\\b(time|clock|what time|current time|date|today)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("ai", Pattern.compile(
            "\\b(artificial intelligence|AI|what is ai|tell me about ai|explain ai)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("machine_learning", Pattern.compile(
            "\\b(machine learning|ML|what is ml|deep learning|neural network)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("java", Pattern.compile(
            "\\b(java|what is java|tell me about java|explain java)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("programming", Pattern.compile(
            "\\b(programming|coding|software development|developer|code|develop)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("nlp", Pattern.compile(
            "\\b(natural language processing|NLP|what is nlp|text processing)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("creator", Pattern.compile(
            "\\b(who created you|who made you|your creator|who built you|who developed you)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("howareyou", Pattern.compile(
            "\\b(how are you|how'?re you|how are you doing|how'?s it going|you okay|you good)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("thanks", Pattern.compile(
            "\\b(thank+s?|thank you|thx|ty|appreciate|grateful)\\b",
            Pattern.CASE_INSENSITIVE
        ));
        
        patterns.put("learn_instruction", Pattern.compile(
            "\\b(how to teach|teach you|how do i teach|learn command)\\b",
            Pattern.CASE_INSENSITIVE
        ));
    }
    
    public String getResponse(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "Please say something! 😊";
        }
        
        String originalInput = userInput.trim();
        userInput = originalInput.toLowerCase();
        
        // Check for learning command
        if (userInput.startsWith("learn:")) {
            return processLearning(originalInput);
        }
        
        // Check for greeting FIRST
        if (patterns.get("greeting").matcher(userInput).find()) {
            return getRandomResponse(greetings);
        }
        
        // Check for farewell
        if (patterns.get("farewell").matcher(userInput).find()) {
            return getRandomResponse(farewells);
        }
        
        // Check for "how are you"
        if (patterns.get("howareyou").matcher(userInput).find()) {
            return getRandomResponse(knowledgeBase.get("howareyou"));
        }
        
        // Check for thanks
        if (patterns.get("thanks").matcher(userInput).find()) {
            return getRandomResponse(knowledgeBase.get("thanks"));
        }
        
        // Check all other knowledge base patterns
        for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
            String key = entry.getKey();
            
            if (key.equals("greeting") || key.equals("farewell") || 
                key.equals("howareyou") || key.equals("thanks")) {
                continue;
            }
            
            if (entry.getValue().matcher(userInput).find()) {
                if (knowledgeBase.containsKey(key)) {
                    return getRandomResponse(knowledgeBase.get(key));
                }
            }
        }
        
        // Sentiment analysis
        if (containsPositiveWords(userInput)) {
            return "I'm glad to hear that! 😊 How else can I help you?";
        }
        
        if (containsNegativeWords(userInput)) {
            return "I'm sorry to hear that. 😔 Is there anything I can do to help?";
        }
        
        if (userInput.contains("?")) {
            return "That's a great question! " + getRandomResponse(knowledgeBase.get("default"));
        }
        
        return getRandomResponse(knowledgeBase.get("default"));
    }
    
    /**
     * LEARNING FEATURE - Bot can learn new responses
     */
    private String processLearning(String input) {
        try {
            // Format: learn: keyword = response
            String content = input.substring(6).trim(); // Remove "learn:"
            String[] parts = content.split("=", 2);
            
            if (parts.length != 2) {
                return "❌ Invalid format! Use: 'learn: keyword = response'\nExample: 'learn: python = Python is a programming language'";
            }
            
            String keyword = parts[0].trim().toLowerCase();
            String response = parts[1].trim();
            
            if (keyword.isEmpty() || response.isEmpty()) {
                return "❌ Both keyword and response must not be empty!";
            }
            
            // Add to knowledge base
            if (!knowledgeBase.containsKey(keyword)) {
                knowledgeBase.put(keyword, new ArrayList<>());
                // Create pattern for new keyword
                patterns.put(keyword, Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE));
            }
            
            knowledgeBase.get(keyword).add(response);
            
            // Save learned knowledge
            saveLearnedKnowledge();
            
            return "✅ Great! I've learned that '" + keyword + "' = '" + response + "'!\nTry asking me about '" + keyword + "' now! 🧠";
            
        } catch (Exception e) {
            return "❌ Error processing learning command. Please use format: 'learn: keyword = response'";
        }
    }
    
    /**
     * Save learned knowledge to file
     */
    private void saveLearnedKnowledge() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(KNOWLEDGE_FILE))) {
            // Create a map of only learned knowledge (exclude default ones)
            Map<String, java.util.List<String>> learnedOnly = new HashMap<>();
            
            // Save all custom learned entries
            for (Map.Entry<String, java.util.List<String>> entry : knowledgeBase.entrySet()) {
                String key = entry.getKey();
                // Skip default knowledge base entries
                if (!isDefaultKnowledge(key)) {
                    learnedOnly.put(key, entry.getValue());
                }
            }
            
            oos.writeObject(learnedOnly);
            System.out.println("✅ Learned knowledge saved!");
        } catch (IOException e) {
            System.err.println("❌ Error saving learned knowledge: " + e.getMessage());
        }
    }
    
    /**
     * Load previously learned knowledge
     */
    @SuppressWarnings("unchecked")
    private void loadLearnedKnowledge() {
        File file = new File(KNOWLEDGE_FILE);
        if (!file.exists()) {
            return; // No learned knowledge yet
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(KNOWLEDGE_FILE))) {
            Map<String, java.util.List<String>> learned = (Map<String, java.util.List<String>>) ois.readObject();
            
            // Add learned knowledge to current knowledge base
            for (Map.Entry<String, java.util.List<String>> entry : learned.entrySet()) {
                String key = entry.getKey();
                knowledgeBase.put(key, entry.getValue());
                // Recreate pattern
                patterns.put(key, Pattern.compile("\\b" + key + "\\b", Pattern.CASE_INSENSITIVE));
            }
            
            System.out.println("✅ Loaded " + learned.size() + " learned topics!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("⚠️ Could not load learned knowledge: " + e.getMessage());
        }
    }
    
    private boolean isDefaultKnowledge(String key) {
        java.util.List<String> defaultKeys = Arrays.asList(
            "name", "help", "weather", "time", "ai", "machine_learning", 
            "java", "programming", "nlp", "creator", "howareyou", 
            "thanks", "learn_instruction", "default"
        );
        return defaultKeys.contains(key);
    }
    
    private boolean containsPositiveWords(String input) {
        String[] positiveWords = {"good", "great", "excellent", "awesome", "wonderful", 
                                  "fantastic", "love", "happy", "happiness", "joy", "amazing",
                                  "perfect", "brilliant", "superb", "outstanding", "delighted",
                                  "excited", "glad", "nice", "beautiful", "lovely"};
        for (String word : positiveWords) {
            if (input.contains(word)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean containsNegativeWords(String input) {
        String[] negativeWords = {"bad", "terrible", "awful", "hate", "sad", "sadness",
                                  "angry", "upset", "disappointed", "frustrated", "annoyed",
                                  "horrible", "worst", "poor", "unhappy", "depressed",
                                  "worried", "anxious", "stressed", "problem"};
        for (String word : negativeWords) {
            if (input.contains(word)) {
                return true;
            }
        }
        return false;
    }
    
    private String getRandomResponse(java.util.List<String> responses) {
        if (responses == null || responses.isEmpty()) {
            return "I'm not sure how to respond to that.";
        }
        return responses.get(random.nextInt(responses.size()));
    }
}

public class AIChatbot extends JFrame {
    
    private ChatbotEngine chatbot;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    private JButton saveButton;
    private JButton loadButton;
    private int messageCount = 0;
    private static final String CHAT_HISTORY_FILE = "chat_history.txt";
    
    public AIChatbot() {
        chatbot = new ChatbotEngine();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("AI Chatbot - Enhanced Version 🤖");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(Color.WHITE);
        chatArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        displayWelcomeMessage();
        
        inputField.requestFocus();
        
        // Auto-save on close
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                autoSaveChatHistory();
            }
        });
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 118, 210));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("🤖 AI Chatbot - Enhanced Edition");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("NLP • Machine Learning • Save/Load • Bot Learning");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(new Color(25, 118, 210));
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
        
        JLabel statusLabel = new JLabel("● Online | Messages: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(new Color(76, 175, 80));
        statusLabel.setName("statusLabel");
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(new Color(240, 242, 245));
        inputPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        inputField.addActionListener(e -> sendMessage());
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sendButton.setBackground(new Color(25, 118, 210));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setOpaque(true);
        sendButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> sendMessage());
        
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setOpaque(true);
        saveButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveChatHistory());
        
        loadButton = new JButton("Load");
        loadButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loadButton.setBackground(new Color(255, 152, 0));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);
        loadButton.setBorderPainted(false);
        loadButton.setOpaque(true);
        loadButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadButton.addActionListener(e -> loadChatHistory());
        
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clearButton.setBackground(new Color(244, 67, 54));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setOpaque(true);
        clearButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> clearChat());
        
        buttonPanel.add(sendButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);
        
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        return inputPanel;
    }
    
    private void displayWelcomeMessage() {
        String welcome = 
                "╔════════════════════════════════════════════════════════════╗\n" +
                "║         AI Chatbot - Enhanced Edition 🤖                  ║\n" +
                "╚════════════════════════════════════════════════════════════╝\n\n" +
                "Hello! I'm your intelligent AI assistant! 👋\n\n" +
                "✨ NEW FEATURES:\n" +
                "  💾 Save/Load Chat History - Keep your conversations!\n" +
                "  🧠 Bot Learning - Teach me new things!\n" +
                "  📊 Message Counter - Track your activity\n\n" +
                "💬 I can help you with:\n" +
                "  • AI, Machine Learning, Programming\n" +
                "  • General questions and conversations\n" +
                "  • Learning new topics from you!\n\n" +
                "🎓 Teach me something new:\n" +
                "  Type: learn: [keyword] = [response]\n" +
                "  Example: learn: python = Python is a programming language\n\n" +
                "💡 Try these:\n" +
                "  • \"Hi\" or \"Hello\"\n" +
                "  • \"What is AI?\"\n" +
                "  • \"How to teach you?\"\n" +
                "  • Teach me something new!\n\n" +
                "Type your message and press Enter or click Send!\n" +
                "────────────────────────────────────────────────────────────\n\n";
        
        chatArea.append(welcome);
    }
    
    private void sendMessage() {
        String userMessage = inputField.getText().trim();
        
        if (userMessage.isEmpty()) {
            return;
        }
        
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        chatArea.append("You [" + timestamp + "]: " + userMessage + "\n");
        
        String botResponse = chatbot.getResponse(userMessage);
        
        timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        chatArea.append("🤖 Bot [" + timestamp + "]: " + botResponse + "\n\n");
        
        messageCount++;
        updateMessageCount();
        
        inputField.setText("");
        
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        
        inputField.requestFocus();
    }
    
    private void updateMessageCount() {
        // Find and update status label
        for (Component comp : ((JPanel)((JPanel)getContentPane().getComponent(0)).getComponent(0)).getComponents()) {
            if (comp instanceof JLabel && comp.getName() != null && comp.getName().equals("statusLabel")) {
                ((JLabel)comp).setText("● Online | Messages: " + messageCount);
                break;
            }
        }
    }
    
    /**
     * SAVE CHAT HISTORY FEATURE
     */
    private void saveChatHistory() {
        try {
            String chatText = chatArea.getText();
            Files.write(Paths.get(CHAT_HISTORY_FILE), chatText.getBytes());
            
            JOptionPane.showMessageDialog(this, 
                "✅ Chat history saved successfully!\n" +
                "File: " + CHAT_HISTORY_FILE + "\n" +
                "Messages: " + messageCount,
                "Save Successful",
                JOptionPane.INFORMATION_MESSAGE);
                
            System.out.println("✅ Chat saved to: " + CHAT_HISTORY_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error saving chat history:\n" + e.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * LOAD CHAT HISTORY FEATURE
     */
    private void loadChatHistory() {
        File file = new File(CHAT_HISTORY_FILE);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ No saved chat history found!\n" +
                "File: " + CHAT_HISTORY_FILE,
                "No History",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(CHAT_HISTORY_FILE)));
            
            int choice = JOptionPane.showConfirmDialog(this,
                "Load previous chat history?\n" +
                "This will replace current conversation.",
                "Load Chat History",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (choice == JOptionPane.YES_OPTION) {
                chatArea.setText(content);
                JOptionPane.showMessageDialog(this,
                    "✅ Chat history loaded successfully!",
                    "Load Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error loading chat history:\n" + e.getMessage(),
                "Load Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * AUTO-SAVE on close
     */
    private void autoSaveChatHistory() {
        if (messageCount > 0) {
            try {
                String chatText = chatArea.getText();
                Files.write(Paths.get(CHAT_HISTORY_FILE), chatText.getBytes());
                System.out.println("✅ Auto-saved chat history on exit!");
            } catch (IOException e) {
                System.err.println("❌ Auto-save failed: " + e.getMessage());
            }
        }
    }
    
    private void clearChat() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear the chat history?\n" +
            "Messages: " + messageCount,
            "Clear Chat",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            chatArea.setText("");
            messageCount = 0;
            updateMessageCount();
            displayWelcomeMessage();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AIChatbot gui = new AIChatbot();
            gui.setVisible(true);
            
            System.out.println("╔════════════════════════════════════════════════════╗");
            System.out.println("║    AI Chatbot Enhanced - INTERNSHIP READY! 🚀    ║");
            System.out.println("╚════════════════════════════════════════════════════╝");
            System.out.println("\n✨ NEW FEATURES:");
            System.out.println("  💾 Save/Load Chat History");
            System.out.println("  🧠 Bot Learning (teach new responses)");
            System.out.println("  📊 Message tracking");
            System.out.println("  🔄 Auto-save on exit");
            System.out.println("\n💡 Try teaching the bot:");
            System.out.println("  learn: python = Python is a programming language");
            System.out.println("  learn: react = React is a JavaScript library\n");
        });
    }
}