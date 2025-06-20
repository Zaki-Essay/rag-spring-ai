package com.gaga.ragspringai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class RagSpringAiApplication implements CommandLineRunner {

    private final VectorStore vectorStore;

    private final ChatModel chatModel;

    private final ChatMemory chatMemory;

    public RagSpringAiApplication(VectorStore vectorStore, ChatModel chatModel, ChatMemory chatMemory) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
    }

    public static void main(String[] args) {
        SpringApplication.run(RagSpringAiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> contents = List.of(
                "Spring Boot menggunakan dependency injection (DI) untuk mengelola dan menginjeksi dependensi antar komponen aplikasi secara otomatis.",
                "Dengan DI, Spring bertanggung jawab atas pembuatan dan pengelolaan lifecycle dari objek yang dibutuhkan oleh komponen lain.",
                "Annotation @Component menandai sebuah kelas sebagai kandidat bean yang akan dikelola oleh Spring container.",
                "Annotation @Service digunakan untuk mendeklarasikan kelas sebagai service layer, sekaligus menjadikannya bean yang bisa diinject.",
                "@Repository digunakan untuk komponen yang berinteraksi dengan database, dan memberikan tambahan fitur seperti exception translation.",
                "Spring Boot secara otomatis melakukan scan terhadap package dan sub-package tempat kelas dengan anotasi @Component berada.",
                "@Autowired memungkinkan Spring untuk menginjeksi dependency secara otomatis ke dalam field, constructor, atau setter method.",
                "Constructor injection lebih direkomendasikan dibandingkan field injection karena menjamin semua dependency tersedia saat objek dibuat.",
                "Setter injection memberikan fleksibilitas namun rawan error karena dependency bisa saja tidak diset sebelum digunakan.",
                "Field injection mudah digunakan tapi menyulitkan testing karena dependency bersifat private dan final tidak dapat digunakan.",
                "Annotation @Qualifier digunakan bersama @Autowired untuk memilih salah satu dari beberapa bean yang memiliki tipe yang sama.",
                "Circular dependency terjadi ketika dua atau lebih bean saling membutuhkan satu sama lain, menyebabkan error saat konteks dibuat.",
                "Bean bisa juga dideklarasikan secara manual dengan anotasi @Bean dalam kelas konfigurasi yang ditandai dengan @Configuration.",
                "Saat menggunakan konfigurasi manual, kita harus mengembalikan instance dari bean yang diinginkan dalam method dengan @Bean.",
                "Spring mendeteksi kelas-kelas annotated melalui component scanning berdasarkan package utama aplikasi Spring Boot.",
                "Dependency injection meningkatkan loose coupling dan membuat komponen menjadi lebih mudah diuji dan dipelihara.",
                "@Primary dapat digunakan untuk menentukan bean default jika ada lebih dari satu kandidat untuk tipe tertentu.",
                "Salah satu best practice dalam DI adalah membuat dependency bersifat immutable (final) dan menyuntikkannya via constructor.",
                "Dengan dependency injection, aplikasi tidak perlu membuat instance dari dependency secara eksplisit dengan keyword new.",
                "Spring Boot dapat secara otomatis mengonfigurasi dependency tertentu jika pustaka pihak ketiga tersedia di classpath.",
                "Jika Spring tidak dapat menemukan bean untuk diinject, maka akan terjadi NoSuchBeanDefinitionException saat aplikasi dijalankan.",
                "Salah satu keunggulan DI adalah mengurangi boilerplate code dan meningkatkan testability melalui mock injection.",
                "Annotation @ComponentScan digunakan untuk mengatur area mana saja yang perlu discan untuk mendeteksi bean secara otomatis.",
                "Bean yang didefinisikan secara manual dengan @Bean memiliki nama default sesuai dengan nama method-nya.",
                "Saat menggunakan @Autowired pada beberapa constructor, Spring akan mengalami ambiguity dan bisa gagal inject.",
                "DI di Spring juga memungkinkan pembuatan prototype bean yang akan dibuat ulang setiap kali diinject.",
                "Lifecycle dari bean singleton dikelola sepenuhnya oleh Spring dan hanya dibuat satu kali per ApplicationContext.",
                "Annotation @Lazy bisa digunakan untuk menunda pembuatan bean sampai benar-benar dibutuhkan.",
                "@Scope dapat digunakan untuk menentukan scope dari bean, seperti singleton, prototype, request, atau session.",
                "Spring Boot juga mendukung dependency injection pada ApplicationRunner dan CommandLineRunner untuk setup awal aplikasi."
        );

        List<Document> documents = IntStream.range(0, contents.size())
                .mapToObj(i -> Document.builder()
                        .text(contents.get(i))
                        .build()
                ).toList();

        vectorStore.add(documents);
    }

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore)
                )
                .build();
    }
}
